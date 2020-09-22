package lafaya.revolvingdoor.view;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.serilport.RS485Protocol;
import lafaya.revolvingdoor.serilport.RS485SendCommand;
import lafaya.revolvingdoor.serilport.SerialPortThread;
import lafaya.revolvingdoor.utils.DataBase;
import lafaya.revolvingdoor.utils.GridUtils;

public class PageMode {

    private Activity activity;
    private Handler handler;

    private RelativeLayout activity_mode;

    private GridView grid_mode_list;
    private GridUtils gridUtils = new GridUtils();

    private Timer mTimer = null;
    private TimerTask mTimerTask = null;

    public boolean modeChanging = false;

    //运行模式 图标
    private int[] mode_list_on = {R.drawable.ic_mode_normal_on, R.drawable.ic_mode_winter_on, R.drawable.ic_mode_summer_on,
            R.drawable.ic_mode_slidingauto_on, R.drawable.ic_mode_slidingexit_on, R.drawable.ic_mode_slidingopen_on,
            R.drawable.ic_mode_lock_on,R.drawable.ic_mode_manual_on};
    //运行模式 名称
    private  String[] mode_name = {MainActivity.sContext.getString(R.string.RDModeNormalName),
            MainActivity.sContext.getString(R.string.RDModeWinterName),
            MainActivity.sContext.getString(R.string.RDModeSummerName),
            MainActivity.sContext.getString(R.string.RDModeSlidingAutoName),
            MainActivity.sContext.getString(R.string.RDModeSlidingExitName),
            MainActivity.sContext.getString(R.string.RDModeSlidingOpenName),
            MainActivity.sContext.getString(R.string.RDModeLockName),
            MainActivity.sContext.getString(R.string.RDModeManualName)};

    //当前gird选择的运行模式
    private String mSelectedMode = DataBase.instance().mRunningMode;
    private Button mbutton_modesave;

    private boolean flashFlag = false;

    //类接口函数定义
    private static class InstanceHolder {
        private static PageMode sManager = new PageMode();
    }
    public static PageMode instance(){
        return PageMode.InstanceHolder.sManager;
    }


    public void PageInit(Activity inactivity, Handler inhandler){
        activity = inactivity;
        handler = inhandler;
        //layout 初始化
        activity_mode = activity.findViewById(R.id.layout_activity_mode);
        //运行模式显示初始化
        grid_mode_list = activity.findViewById(R.id.grid_mode_list);

        // 按键初始化
        mbutton_modesave = activity.findViewById(R.id.button_modesave);
        showButtonSave(flashFlag);

        //grid操作监听处理
        gridModeOnClick();
        //按键监听处理
        onClickButtonSave();
    }

    //页面显示 / 隐藏
    public void showPageMode(boolean flag){
        if(flag){
            activity_mode.setVisibility(View.VISIBLE);
            showModeGrid(activity,DataBase.instance().mRunningMode,DataBase.instance().mRunningMode);
        }else{
            //清除状态
            flashFlag = false;
            mSelectedMode = DataBase.instance().mRunningMode;
            stopTimer();
            activity_mode.setVisibility(View.GONE);
        }
    }

    // grid 操作监听处理
    private void gridModeOnClick(){
        grid_mode_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(!modeChanging) {
                    mSelectedMode = "0" + Integer.toHexString(position + 1);
                    if (mSelectedMode.equals(DataBase.instance().mRunningMode)) {
                        stopTimer();
                    } else {
                        startTimer();
                    }
                }
            }
        });
    }

    // 按键监听，当需要修改运行模式时，显示保存
    private void onClickButtonSave(){
        mbutton_modesave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!modeChanging) {
                    //发送模式修改命令：
                    SerialPortThread.instance().sendMsg(RS485SendCommand.instance().CmdRevolvingDoorMode(
                            MainActivity.sContext.getString(R.string.addRevolvingDoor), mSelectedMode));
                    //=============================
                    //等待修改完成，开始修改计时。
                    modeChanging = true;
                    mbutton_modesave.setText("正在修改模式！");
                    modeChangingWait(); //等待修改完成
                }
                //=============================

            }
        });

    }
    //=========================================================
    //收到运行模式指令，则进行修改完成处理。
    public void modeChanged(){
        if(!mSelectedMode.equals(DataBase.instance().mRunningMode)){
            //未按指定模式修改，显示修改失败，显示当前实际运行模式
            mSelectedMode = DataBase.instance().mRunningMode;
            logShow("模式修改失败！");
        }else{
            logShow("模式修改完成！");
        }

    }

    //日志显示
    private void logShow(String logText){
        Message msg = new Message();
        msg.what = 0;
        Bundle bundle = new Bundle();
        bundle.putString("msg",logText);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    //结束修改，清除修改标志位和闪烁计时器
    public void modeChangeFinished(){
        //如果模式修改失败，则显示回原来的模式
        modeChanging = false;
        stopTimer();
    }

    //运行模式修改等待，超过等待时间未收到完成标志，则视为修改失败。
    private void modeChangingWait(){
        //延迟查看是否完成修改
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(!mSelectedMode.equals(DataBase.instance().mRunningMode)){
                    logShow("模式修改失败");
                }

 //               if(modeChanging){
 //                   //模式修改失败。
 //                   logShow("模式修改失败！");
 //               }
                else
                    logShow("模式修改成功");
            }
        },1000); // 延时 2秒
    }

    //=========================================================
    //保存按键显示 / 隐藏
    private void showButtonSave(boolean flag){
        mbutton_modesave.setText("保存");
        if(flag) {
            mbutton_modesave.setVisibility(View.VISIBLE);//显示
        }else{
            mbutton_modesave.setVisibility(View.GONE);//隐藏
        }
    }
    //运行模式grid显示刷新
    private void showModeGrid(Activity activity, String selected_mode, String runing_mode){
        List<HashMap<String, Object>> list = new ArrayList<>();

        //未被选择时的运行模式；

        int[] mode_list = {R.drawable.ic_mode_normal_off, R.drawable.ic_mode_winter_off, R.drawable.ic_mode_summer_off,
                R.drawable.ic_mode_slidingauto_off, R.drawable.ic_mode_slidingexit_off, R.drawable.ic_mode_slidingopen_off,
                R.drawable.ic_mode_lock_off, R.drawable.ic_mode_manual_off};

        int running_position = Integer.parseInt(runing_mode) - 1;
        int selected_position = Integer.parseInt(selected_mode) - 1;

        mode_list[selected_position] = mode_list_on[selected_position];
        mode_list[running_position] = mode_list_on[running_position];

        for(int i=0;i<mode_list.length;i++){
            list.add(gridUtils.getGridViewData(mode_list[i], mode_name[i]));
        }
        //写入grid view
        SimpleAdapter saImageItems = new SimpleAdapter(activity,
                list,
                R.layout.mode_gridlist,
                new String[] { "image", "text"},
                new int[] { R.id.image_mode_list, R.id.image_mode_name});
        grid_mode_list.setAdapter(saImageItems);
    }

    //=========================================================
    // 闪烁显示模式
    public void flashShow(){
        //需要闪烁显示
        if(flashFlag){
            flashFlag = false;
            showModeGrid(activity, mSelectedMode,DataBase.instance().mRunningMode);
        }else{
            flashFlag = true;
            showModeGrid(activity,DataBase.instance().mRunningMode,DataBase.instance().mRunningMode);
        }
    }
    // 开启闪烁定时器
    private void startTimer(){
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if(mTimerTask != null){
            mTimerTask.cancel();
            mTimerTask = null;
        }

        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                Message msg = new Message();
                msg.what = 2;
                handler.sendMessage(msg);
            }
        };
        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 0,1000);

        flashFlag = true;
        //显示保存按键
        showButtonSave(true);
    }
    // 关闭闪烁定时器
    private void stopTimer(){
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if(mTimerTask != null){
            mTimerTask.cancel();
            mTimerTask = null;
        }
        flashFlag = false;
        //隐藏保存按键
        showButtonSave(false);
        //刷新模式显示
        showModeGrid(activity,DataBase.instance().mRunningMode,DataBase.instance().mRunningMode);
    }
    //=========================================================
}
