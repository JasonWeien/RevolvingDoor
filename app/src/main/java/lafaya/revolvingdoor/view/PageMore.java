package lafaya.revolvingdoor.view;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.model.InformationRevolving;
import lafaya.revolvingdoor.model.InformationSliding;
import lafaya.revolvingdoor.model.InformationWingL;
import lafaya.revolvingdoor.model.InformationWingR;
import lafaya.revolvingdoor.model.ParameterUpdate;
import lafaya.revolvingdoor.utils.AutoCountListView;
import lafaya.revolvingdoor.utils.DataBase;
import lafaya.revolvingdoor.utils.SystemUIUtils;




/*
* 界面：
* 一、门系统可读信息
* 1、旋转门信息
* 2、平滑自动门信息
* 3、门翼信息
* 二、App信息
* 1、软件更新
* 2、企业简介
* 3、通讯配置
*
* 流程：
* 一、进入后，
*
*
*
*
* */

public class PageMore {
    private Activity activity;
    private Handler handler;

    private RelativeLayout activity_more;


    private InformationRevolving infoRevolving = new InformationRevolving();
    private InformationSliding infoSliding = new InformationSliding();
    private InformationWingL infoWingL = new InformationWingL();
    private InformationWingR infoWingR = new InformationWingR();


    private Button button_infoRevolving, button_infoSliding,button_infoWingL,button_infoWingR;

//    private LinearLayout layout_info_RevolvingDoor;

    private LinearLayout layout_info_AutoDoor;


    private DataBase.DoorType layoutVisible = DataBase.DoorType.NON;

    //========================
    private AutoCountListView grid_infoAutoDoor;
    //查询菜单位置
    private int posChecked = 0;
    public boolean checkWaiting = false;



    //===========================
    Context mContext =MainActivity.sContext;
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;

    //类接口函数定义
    private static class InstanceHolder {
        private static PageMore sManager = new PageMore();
    }
    public static PageMore instance(){
        return PageMore.InstanceHolder.sManager;
    }

    //
    public void PageInit(Activity inactivity, Handler inhandler) {
        activity = inactivity;
        handler = inhandler;

        //
        activity_more = activity.findViewById(R.id.layout_activity_more);

        // 旋转门主体
        // 平滑门
        // 门翼1
        // 门翼2
        button_infoRevolving = activity.findViewById(R.id.button_infoRevolving);
//        layout_info_RevolvingDoor = activity.findViewById(R.id.layout_info_RevolvingDoor);
//        infoRevolving.initializeInfoRevolving(activity);

        button_infoSliding = activity.findViewById(R.id.button_infoSliding);
        button_infoWingL = activity.findViewById(R.id.button_infoWingL);
        button_infoWingR = activity.findViewById(R.id.button_infoWingR);

        layout_info_AutoDoor = activity.findViewById(R.id.layout_info_AutoDoor);

//
//
//        layout_info_SlidingDoor = activity.findViewById(R.id.layout_info_SlidingDoor);
//        infoSliding.initializeInfoSliding(activity);
//
//        layout_info_WingL = activity.findViewById(R.id.layout_info_WingL);
//        infoWingL.initializeInfoWingL(activity);
//
//        layout_info_WingR = activity.findViewById(R.id.layout_info_WingR);
//        infoWingR.initializeInfoWingR(activity);

        grid_infoAutoDoor = activity.findViewById(R.id.grid_infoAutoDoor);

        // 初始化各信息参数
        DataBase.instance().mapInfoRevolving = ParameterUpdate.instance().paraInfoUpdate(DataBase.instance().mapInfoRevolving,null,0);
        DataBase.instance().mapInfoSliding = ParameterUpdate.instance().paraInfoUpdate(DataBase.instance().mapInfoSliding,null,0);
        DataBase.instance().mapInfoWingL = ParameterUpdate.instance().paraInfoUpdate(DataBase.instance().mapInfoWingL,null,0);
        DataBase.instance().mapInfoWingR = ParameterUpdate.instance().paraInfoUpdate(DataBase.instance().mapInfoWingR,null,0);



        //初始化界面
        updateLayout();

        // 按下、展开时刷新信息

        /* 旋转门参数*/
        button_infoRevolving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoRevolvingPressed();
            }
        });

        /* 平滑自动门参数*/
        button_infoSliding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoSlidingPressed();
            }
        });

        /* 门翼1参数*/
        button_infoWingL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoWingLPressed();
            }
        });

        /* 门翼2参数*/
        button_infoWingR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                infoWingRPressed();
            }
        });


        //gird 监听
        grid_infoAutoDoor.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
//                posInfoSelected = pos;
//                ParameterUpdate.instance().readInfoOther(pos, MainActivity.sContext.getString(R.string.addAutoDoor));

            }
        });

    }

    /* 旋转门参数*/
    private void infoRevolvingPressed(){
        if(layoutVisible == DataBase.DoorType.REVOLVING){
            layoutVisible = DataBase.DoorType.NON;
            infoRevolving.checkWaiting = false;
        }else{
            layoutVisible = DataBase.DoorType.REVOLVING;
            infoRevolving.checkWaiting = true;
        }

        updateLayout();
    }


    /* 平滑自动门参数*/
    private void infoSlidingPressed(){
        if(layoutVisible == DataBase.DoorType.SLIDING){
            layoutVisible = DataBase.DoorType.NON;
            infoSliding.checkWaiting = false;
        }else{
            layoutVisible = DataBase.DoorType.SLIDING;
            infoSliding.checkWaiting = true;
        }

        updateLayout();
    }

    /* 门翼1参数*/
    private void infoWingLPressed(){
        if(layoutVisible == DataBase.DoorType.WINGL){
            layoutVisible = DataBase.DoorType.NON;
            infoWingL.checkWaiting = false;
        }else{
            layoutVisible = DataBase.DoorType.WINGL;
            infoWingL.checkWaiting = true;
        }

        updateLayout();
    }

    /* 门翼2参数*/
    private void infoWingRPressed(){
        if(layoutVisible == DataBase.DoorType.WINGR){
            layoutVisible = DataBase.DoorType.NON;
            infoWingR.checkWaiting = false;
        }else{
            layoutVisible = DataBase.DoorType.WINGR;
            infoWingR.checkWaiting = true;
        }

        updateLayout();
    }
//
//    // 显示更改
//    private void layoutChange(int[] state_tmp){
////        layout_info_RevolvingDoor.setVisibility(state_tmp[0]);
//        layout_info_AutoDoor.setVisibility()
//    }



    //界面显示更新
    public void updateLayout(){

        int[] state_tmp = {View.GONE, View.GONE, View.GONE, View.GONE};


        //根据当前显示的参数页面，执行如下操作：
        // 1、显示/隐藏
        // 2、查询参数
        // 3、

        switch (layoutVisible){
            case NON:
                //如果在查询参数，则停止查询
                stopTimer();
                break;
            case REVOLVING:
                state_tmp[0] = View.VISIBLE;
                //查询参数，以上一次查询位置为起点
//                if(checkWaiting){
//                    checkInfoAutoDoor(MainActivity.sContext.getString(R.string.addRevolvingDoor),12);
//                    startTimer();
//                }
                break;
            case SLIDING:
                state_tmp[1] = View.VISIBLE;
                //查询参数
                //查询参数，以上一次查询位置为起点
                if(checkWaiting){
                    checkInfoAutoDoor(MainActivity.sContext.getString(R.string.addAutoDoor),12);
                    startTimer();
                }
                break;
            case WINGL:
                state_tmp[2] = View.VISIBLE;
                //查询参数
                //查询参数，以上一次查询位置为起点
                if(checkWaiting){
                    checkInfoAutoDoor(MainActivity.sContext.getString(R.string.addSwingL),12);
                    startTimer();
                }

                break;
            case WINGR:
                state_tmp[3] = View.VISIBLE;
                //查询参数
                //查询参数，以上一次查询位置为起点
                if(checkWaiting){
                    checkInfoAutoDoor(MainActivity.sContext.getString(R.string.addSwingR),12);
                    startTimer();
                }
                break;
            default:
                break;
        }


        //菜单展开或收起操作
        if(layoutVisible == DataBase.DoorType.NON){
            layout_info_AutoDoor.setVisibility(View.GONE);
        }else {
            layout_info_AutoDoor.setVisibility(View.VISIBLE);
        }
//        layoutChange(state_tmp);

        //更改菜单键箭头显示方向,展开时向下，收起时向上
        SystemUIUtils.instance().buttonIconChange(mContext,button_infoRevolving,state_tmp[0]);
        SystemUIUtils.instance().buttonIconChange(mContext,button_infoSliding,state_tmp[1]);
        SystemUIUtils.instance().buttonIconChange(mContext,button_infoWingL,state_tmp[2]);
        SystemUIUtils.instance().buttonIconChange(mContext,button_infoWingR,state_tmp[3]);


        // 当展开时，仅显示当前被按下的按键
        if(layoutVisible == DataBase.DoorType.NON){
            button_infoRevolving.setVisibility(View.VISIBLE);
            button_infoSliding.setVisibility(View.VISIBLE);
            button_infoWingL.setVisibility(View.VISIBLE);
            button_infoWingR.setVisibility(View.VISIBLE);
        }else {
            button_infoRevolving.setVisibility(state_tmp[0]);
            button_infoSliding.setVisibility(state_tmp[1]);
            button_infoWingL.setVisibility(state_tmp[2]);
            button_infoWingR.setVisibility(state_tmp[3]);

//            updateGrideInfoAutoDoor(ParameterUpdate.instance().listInfoNormal(DataBase.instance().mapInfoRevolving));
            updateGrideInfoAutoDoor(ParameterUpdate.instance().listInfoNormal(DataBase.instance().mapInfoSliding));
            updateGrideInfoAutoDoor(ParameterUpdate.instance().listInfoNormal(DataBase.instance().mapInfoWingL));
            updateGrideInfoAutoDoor(ParameterUpdate.instance().listInfoNormal(DataBase.instance().mapInfoWingR));

        }

    }


    public void updateGrideInfoAutoDoor(List<HashMap<String, Object>> list){
        //写入grid view
        SimpleAdapter saImageItems = new SimpleAdapter(activity,
                list,
                R.layout.parameter_gridlist,
                new String[] { "text", "value", "unit"},
                new int[] { R.id.parameter_grid_name, R.id.parameter_grid_value, R.id.parameter_grid_unit});
        grid_infoAutoDoor.setAdapter(saImageItems);
    }

    //查询操作，当展开菜单时，查询参数。
    public boolean checkInfoAutoDoor(String address, int maxlength){
        // 按菜单从上到下查询参数

        ParameterUpdate.instance().readInfoOther(posChecked, address);
        //查询下一个参数
        // 在菜单展开的情况下，继续查询数据。
        if(checkWaiting){
            posChecked++;
        }

        //查询到最后，结束查询，否则继续查询
        if(posChecked >= maxlength){
            posChecked = 0;
            checkWaiting = false;
            return true;
        }else {
            return false;
        }
    }

    //==============================================================================================
    // 隔间逐个查看


    private void startTimer(){
        stopTimer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(checkFinished()){
                    stopTimer();
                }else {
                    // 如果还没查完，则继续查
                    if(checkWaiting){
                        startTimer();
                    }

//                    if((infoRevolving.checkWaiting) || (infoSliding.checkWaiting) || (checkWaiting) || (infoWingL.checkWaiting)) {
//                        startTimer();
//                    }
                }
            }
        };
        //如果定义器未初始化，则初始化
        mTimer = new Timer();
        mTimer.schedule(mTimerTask, 500,1000);
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

    }

    private boolean checkFinished() {
        switch (layoutVisible) {
            case NON:
                return true;
            case REVOLVING://旋转门
                return true;
//                return checkInfoAutoDoor(MainActivity.sContext.getString(R.string.addRevolvingDoor), 12);
            case SLIDING://平滑门
                return checkInfoAutoDoor(MainActivity.sContext.getString(R.string.addAutoDoor), 12);
//                return infoSliding.checkInfoSliding();
            case WINGR:
                return checkInfoAutoDoor(MainActivity.sContext.getString(R.string.addSwingR), 12);
            case WINGL:
                return checkInfoAutoDoor(MainActivity.sContext.getString(R.string.addSwingL), 12);
//                return infoWingL.checkInfoWingL();
            default:
                return true;
        }
    }

    //页面显示 / 隐藏
    public void showPageMore(boolean flag){
        if(flag){
            activity_more.setVisibility(View.VISIBLE);
        }else{
            layoutVisible = DataBase.DoorType.NON;
            activity_more.setVisibility(View.GONE);
            updateLayout();
        }
    }


}
