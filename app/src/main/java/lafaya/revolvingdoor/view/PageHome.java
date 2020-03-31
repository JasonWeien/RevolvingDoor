package lafaya.revolvingdoor.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageButton;
//import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import java.util.Timer;
import java.util.TimerTask;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.serilport.RS485Protocol;
import lafaya.revolvingdoor.serilport.RS485SendCommand;
import lafaya.revolvingdoor.serilport.SerialPortThread;
import lafaya.revolvingdoor.utils.DataBase;

public class PageHome {
    //线程定义
    private Handler handler;

    //
    private RelativeLayout activity_home;

    //报警代码
    private LinearLayout mlayout_fragment_error;
    private TextView mtext_error_code;
    private TextView mtext_error_decribe;

    //运行模式图标及文字
    private ImageButton mbutton_home_mode;
    private TextView mtext_runing_mode;

//    //门信息：位置、编号、类型
//    private TextView mtext_door_number;
//    private TextView mtext_door_address;
//    private TextView mtext_door_type;
//    private ImageView mimage_door_type;

    // ============
    //参数更新：运行模式，报警代码
    private String tempRunningMode = MainActivity.sContext.getString(R.string.RDModeManual);
    private String tempErrorCode = MainActivity.sContext.getString(R.string.RDErrorCode00);

    public boolean initFlag = false;



    //Context, 连接MainActivity.
    private Context mContext;

    //====================
    //实时状态更新线程
    private PageHomeThread mpageHomeThread;

    //类接口函数定义
    private static class InstanceHolder {
        private static PageHome sManager = new PageHome();
    }
    public static PageHome instance(){
        return PageHome.InstanceHolder.sManager;
    }

    //初始化Home layout，
    public void PageInit(Activity inactivity, Handler inhandler){
        handler = inhandler;
        mContext = MainActivity.sContext;

        // 主页面布局初始化，指向对应ID
        activity_home = inactivity.findViewById(R.id.layout_activity_home);
        // 错误代码小片段初始化
        mlayout_fragment_error = inactivity.findViewById(R.id.layout_fragment_error);
        mtext_error_code = inactivity.findViewById(R.id.text_error_code);
        mtext_error_decribe = inactivity.findViewById(R.id.text_error_decribe);

        // 运行模式显示初始化
        mtext_runing_mode = inactivity.findViewById(R.id.text_runing_mode);
        mbutton_home_mode = inactivity.findViewById(R.id.button_home_mode);

        mpageHomeThread = new PageHomeThread();
        mpageHomeThread.start();
//        if(mpageHomeThread.isAlive()){
////            mpageHomeThread.interrupt();
//        }else {
//
//        }
        // 启动按键触摸监听
        onButtonModeClick();



    }
    // 初始化查询
    private void initStatus(){
//        SerialPortThread.instance().sendMsg(RS485SendCommand.instance().CmdRevolvingInit(
//                mContext.getString(R.string.addRevolvingDoor)));
        //查询运行模式
        SerialPortThread.instance().sendMsg(RS485SendCommand.instance().CmdRevolvingDoorMode(
                mContext.getString(R.string.addRevolvingDoor),""));
        //延迟查询是否已经完成查询；
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                if(!initFlag){
                    //初始化失败请检测是否连接成功。
                    Message msg = new Message();
                    msg.what = 0;
                    Bundle bundle = new Bundle();
                    bundle.putString("msg","初始化失败，请检测是否连接成功");
                    msg.setData(bundle);
                    handler.sendMessage(msg);
                }
            }
        },2000); // 延时1秒

    }


    //检测运行模式图标是否被触碰
    private void onButtonModeClick(){
        mbutton_home_mode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 发运运行模式查询\设置命令
                SerialPortThread.instance().sendMsg(RS485SendCommand.instance().CmdRevolvingDoorMode(
                        mContext.getString(R.string.addRevolvingDoor),""));
            }
        });
    }


    //运行模式显示
    private void updateRunningMode(String mode){
        if(mode.equals(mContext.getString(R.string.RDModeNormal))){
            mtext_runing_mode.setText(mContext.getString(R.string.RDModeNormalName));
            mbutton_home_mode.setImageResource(R.drawable.ic_mode_normal_on);

        }else if(mode.equals(mContext.getString(R.string.RDModeSummer))){
            mtext_runing_mode.setText(mContext.getString(R.string.RDModeSummerName));
            mbutton_home_mode.setImageResource(R.drawable.ic_mode_summer_on);

        }else if(mode.equals(mContext.getString(R.string.RDModeWinter))){
            mtext_runing_mode.setText(mContext.getString(R.string.RDModeWinterName));
            mbutton_home_mode.setImageResource(R.drawable.ic_mode_winter_on);

        }else if(mode.equals(mContext.getString(R.string.RDModeSlidingAuto))){
            mtext_runing_mode.setText(mContext.getString(R.string.RDModeSlidingAutoName));
            mbutton_home_mode.setImageResource(R.drawable.ic_mode_slidingauto_on);

        }else if(mode.equals(mContext.getString(R.string.RDModeSlidingExit))){
            mtext_runing_mode.setText(mContext.getString(R.string.RDModeSlidingExitName));
            mbutton_home_mode.setImageResource(R.drawable.ic_mode_slidingexit_on);

        }else if(mode.equals(mContext.getString(R.string.RDModeSlidingOpen))){
            mtext_runing_mode.setText(mContext.getString(R.string.RDModeSlidingOpenName));
            mbutton_home_mode.setImageResource(R.drawable.ic_mode_slidingopen_on);

        }else if(mode.equals(mContext.getString(R.string.RDModeLock))){
            mtext_runing_mode.setText(mContext.getString(R.string.RDModeLockName));
            mbutton_home_mode.setImageResource(R.drawable.ic_mode_lock_on);

        }else if(mode.equals(mContext.getString(R.string.RDModeManual))){
            mtext_runing_mode.setText(mContext.getString(R.string.RDModeManualName));
            mbutton_home_mode.setImageResource(R.drawable.ic_mode_manual_on);

        }
    }

    //运行异常代码显示
    private void updateErrorCode(String errorcode){
        if(errorcode.equals(mContext.getString(R.string.RDErrorCode00))){
            mtext_error_code.setText(mContext.getString(R.string.RDErrorCode00));
            mtext_error_decribe.setText(mContext.getString(R.string.RDErrorCode00Describe));
            // 错误代码窗口，默认不显示
            mlayout_fragment_error.setVisibility(View.GONE);
            return;
        }else if(errorcode.equals(mContext.getString(R.string.RDErrorCode01))){
            mtext_error_code.setText("异常代码：0x" + mContext.getString(R.string.RDErrorCode01));
            mtext_error_decribe.setText(mContext.getString(R.string.RDErrorCode01Describe));
        }else{
            mtext_error_code.setText("异常代码：0x" + errorcode);
            mtext_error_decribe.setText(mContext.getString(R.string.RDErrorCode00Describe));
        }
        // 错误代码窗口，默认不显示
        mlayout_fragment_error.setVisibility(View.VISIBLE);
    }

    //显示刷新
    private class PageHomeThread extends Thread{
        @Override
        public void run(){
            while (!isInterrupted())//非阻塞过程中通过判断中断标志来退出
                if((!tempRunningMode.equals(DataBase.instance().mRunningMode)) || (!tempErrorCode.equals(DataBase.instance().mErrorCode))) {
                    //运行模式更新
                    tempRunningMode = DataBase.instance().mRunningMode;
                    //报警代码更新
                    tempErrorCode = DataBase.instance().mErrorCode;
                    handleSendMsg();
                }
            }
    }

    //主线程显示数据更新
    private void handleSendMsg(){
        Message msg = new Message();
        msg.what = 1;
        Bundle bundle = new Bundle();
        bundle.putString("msg1",tempRunningMode);
        bundle.putString("msg2",tempErrorCode);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }

    //更新主页面显示的信息：运行械、故障代码等
    public void updateShowMsg(String mode, String errorcode){
        updateRunningMode(mode);
        updateErrorCode(errorcode);

    }

    //主页面显示 / 隐藏
    public void showPageHome(boolean flag){
        if(flag){
            if(!initFlag){
                //初始化查询
                initStatus();
            }
            updateShowMsg(DataBase.instance().mRunningMode, DataBase.instance().mErrorCode);
            activity_home.setVisibility(View.VISIBLE);
        }else{
            activity_home.setVisibility(View.GONE);
        }
    }

    public void stopHomeThread(){
        mpageHomeThread.interrupt();
        mpageHomeThread = null;
    }







}
