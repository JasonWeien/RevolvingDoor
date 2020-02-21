package lafaya.revolvingdoor.view;

import android.app.Activity;
import android.os.Handler;
import android.view.View;
import android.widget.Adapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.serilport.ByteUtil;
import lafaya.revolvingdoor.serilport.RS485SendCommand;
import lafaya.revolvingdoor.serilport.SerialPortThread;
import lafaya.revolvingdoor.utils.AutoCountListView;
import lafaya.revolvingdoor.utils.DataBase;
import lafaya.revolvingdoor.utils.GridUtils;

public class PageMaintenance {

    private Activity activity;
    private Handler handler;
    private RelativeLayout activity_maintenance;
    private LinearLayout layout_msg_maintenance;
    private RelativeLayout layout_runningmonitor;
//    private LinearLayout layout_runningmonitor;

    private TextView text_msg_maintenance;

    private Button button_mainrestart, button_mainreset, button_runningmonitor;
    private Button button_maintenance_return, button_maintenance_save;

    private Button button_runningmonitor_return;
    private AutoCountListView grid_runningmonitor_msg;
    private TextView text_port_status1,text_port_status2,text_port_status3,text_port_status4,text_port_status5,text_port_status6
            ,text_port_status7,text_port_status8,text_port_status9,text_port_status10,text_port_status11
            ,text_port_status12,text_port_status13,text_port_status14,text_port_status15,text_port_status16
            ,text_port_status17,text_port_status18,text_port_status19,text_port_status20;
    private TextView text_dip_status1,text_dip_status2,text_dip_status3,text_dip_status4,text_dip_status5,text_dip_status6
            ,text_dip_status7,text_dip_status8,text_dip_status9,text_dip_status10;
    private TextView text_system_status1,text_system_status2,text_system_status3,text_system_status4,text_system_status5,text_system_status6
            ,text_system_status7,text_system_status8;

    boolean msg_Restart = false;

    //


    //类接口函数定义
    private static class InstanceHolder {
        private static PageMaintenance sManager = new PageMaintenance();
    }
    public static PageMaintenance instance(){
        return PageMaintenance.InstanceHolder.sManager;
    }

    //
    public void PageInit(Activity inactivity, Handler inhandler) {
        activity = inactivity;
        handler = inhandler;

        //
        activity_maintenance = activity.findViewById(R.id.layout_activity_maintenance);

        layout_msg_maintenance = activity.findViewById(R.id.layout_msg_maintenance);
        layout_msg_maintenance.setVisibility(View.GONE);
        text_msg_maintenance = activity.findViewById(R.id.text_msg_maintenance);
        button_maintenance_return = activity.findViewById(R.id.button_maintenance_return);
        button_maintenance_save = activity.findViewById(R.id.button_maintenance_save);


        //系统重置、系统复位
        button_mainrestart = activity.findViewById(R.id.button_mainrestart);
        button_mainreset = activity.findViewById(R.id.button_mainreset);
        button_runningmonitor = activity.findViewById(R.id.button_runningmonitor);
//        button_hardwaremonitor = activity.findViewById(R.id.button_hardwaremonitor);

        //运行监控
        layout_runningmonitor = activity.findViewById(R.id.layout_runningmonitor);
        layout_runningmonitor.setVisibility(View.GONE);
        grid_runningmonitor_msg = activity.findViewById(R.id.grid_runningmonitor_msg);
        //
        initPortStatus();
        //
        initDIPStatus();

        button_runningmonitor_return = activity.findViewById(R.id.button_runningmonitor_return);


        // 重启按键
        button_mainrestart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出确认按键
                msg_Restart = true;
                showMsgButton(MainActivity.sContext.getString(R.string.msgMaintenancRestart),true);
            }
        });

        // 恢复出厂设置
        button_mainreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //弹出确认按键
                msg_Restart = false;
                showMsgButton(MainActivity.sContext.getString(R.string.msgMaintenancReset),true);
            }
        });

        // 实时监控
        button_runningmonitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SerialPortThread.instance().sendMsg(RS485SendCommand.instance().cmdPCRunningMonitor("0","01"));
                updateRunningMonitor("");
                layout_runningmonitor.setVisibility(View.VISIBLE);
            }
        });
        button_runningmonitor_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SerialPortThread.instance().sendMsg(RS485SendCommand.instance().cmdPCRunningMonitor("0","00"));
                layout_runningmonitor.setVisibility(View.GONE);
            }
        });


        // 硬件监控


        //
        button_maintenance_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMsgButton("",false);
            }
        });
        button_maintenance_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tempCommand = "";
                if(msg_Restart){
                    tempCommand = RS485SendCommand.instance().CmdPCRestart("0");
                }else{
                    tempCommand = RS485SendCommand.instance().CmdPCRestore("0");
                }
                SerialPortThread.instance().sendMsg(tempCommand);
                showMsgButton("",false);
            }
        });

    }

    //
    private void enableButton(boolean flag){
        button_mainrestart.setEnabled(flag);
        button_mainreset.setEnabled(flag);
        button_runningmonitor.setEnabled(flag);
//        button_hardwaremonitor.setEnabled(flag);
    }
    //显示信息
    private void showMsgButton(String msg, boolean flag){
        if(flag) {
            layout_msg_maintenance.setVisibility(View.VISIBLE);
            text_msg_maintenance.setText(msg);
            enableButton(false);
        }else{
            layout_msg_maintenance.setVisibility(View.GONE);
//            text_msg_maintenance.setText(msg);
            enableButton(true);
        }
    }


    //页面显示 / 隐藏
    public void showPageMaintenance(boolean flag){
        if(flag){
            activity_maintenance.setVisibility(View.VISIBLE);
        }else{
            activity_maintenance.setVisibility(View.GONE);
            //清除状态
            layout_msg_maintenance.setVisibility(View.GONE);
            layout_runningmonitor.setVisibility(View.GONE);
            enableButton(true);
        }
    }

    // 运行监控显示：


    public void updateRunningMonitor(String value){
        updateGridViewMsg(activity, value);

        updatePSystemStatus("8304");
        updatePortStatus("FFFFFF");
        updateDIPStatus("FFFF");

    }
    //参数字表
//      1       2~4         5~7         8~10        11~12       13~14       15~16
//    类型    历史次数    当前次数    当前速度    当前电流    历史电流      PWM值
//      17          18          19
//    当前温度  历史温度    门开度

    private void updateGridViewMsg(Activity activity,String value){
        List<HashMap<String, Object>> list = new ArrayList<>();

//        list.add(GridUtils.instance().getGridViewString("门类型", "旋转自动门"));
//        list.add(GridUtils.instance().getGridViewString("门类型", "门翼1"));
//        list.add(GridUtils.instance().getGridViewString("门类型", "门翼2"));
        list.add(GridUtils.instance().getGridViewStringValueAndUnit("门类型", "平滑自动门",""));
        list.add(GridUtils.instance().getGridViewStringValueAndUnit("开度", "0","%"));
        list.add(GridUtils.instance().getGridViewStringValueAndUnit("历史总次数", "0","次"));
        list.add(GridUtils.instance().getGridViewStringValueAndUnit("本阶段次数", "0","次"));
        list.add(GridUtils.instance().getGridViewStringValueAndUnit("当前速度", "0","mm/s"));
        list.add(GridUtils.instance().getGridViewStringValueAndUnit("当前PWM", "0",""));
        list.add(GridUtils.instance().getGridViewStringValueAndUnit("当前电流", "0","mA"));
        list.add(GridUtils.instance().getGridViewStringValueAndUnit("历史最大电流", "0","mA"));
        list.add(GridUtils.instance().getGridViewStringValueAndUnit("当前系统温度", "0","℃"));
        list.add(GridUtils.instance().getGridViewStringValueAndUnit("历史最大温度", "0","℃"));
        //写入grid view
        SimpleAdapter saImageItems = new SimpleAdapter(activity,
                list,
                R.layout.item_msg_status,
                new String[] { "text", "value", "unit"},
                new int[] { R.id.text_msg_status_name, R.id.text_msg_status_value, R.id.text_msg_status_unit});
        grid_runningmonitor_msg.setAdapter(saImageItems);
    }

    //    硬件接口状态
//      20
//    DIP1~8位置
//    21（7~0bit）
//    遥控A、遥控B、遥控C、遥控D、中心定位信号、外雷达、内雷达、电眼、
//    22（7~0bit）
//    按键1、按键2、内光幕、外光幕、电锁反馈、闭锁信号、开锁信号、电池使能
//    23（7~0bit）
//    门状态、市电状态、复位按钮、蜂鸣器、硬件故障、备用输出、DIP10、DIP9

//    26
//    LED段码：dp、GFEDCBA
    private void initPortStatus(){
        text_system_status1 = activity.findViewById(R.id.text_system_status1);
        text_system_status2 = activity.findViewById(R.id.text_system_status2);
        text_system_status3 = activity.findViewById(R.id.text_system_status3);
        text_system_status4 = activity.findViewById(R.id.text_system_status4);
        text_system_status5 = activity.findViewById(R.id.text_system_status5);
        text_system_status6 = activity.findViewById(R.id.text_system_status6);
        text_system_status7 = activity.findViewById(R.id.text_system_status7);
        text_system_status8 = activity.findViewById(R.id.text_system_status8);

        text_port_status1 = activity.findViewById(R.id.text_port_status1);
        text_port_status2 = activity.findViewById(R.id.text_port_status2);
        text_port_status3 = activity.findViewById(R.id.text_port_status3);
        text_port_status4 = activity.findViewById(R.id.text_port_status4);
        text_port_status5 = activity.findViewById(R.id.text_port_status5);
        text_port_status6 = activity.findViewById(R.id.text_port_status6);
        text_port_status7 = activity.findViewById(R.id.text_port_status7);
        text_port_status8 = activity.findViewById(R.id.text_port_status8);
        text_port_status9 = activity.findViewById(R.id.text_port_status9);
        text_port_status10 = activity.findViewById(R.id.text_port_status10);
        text_port_status11 = activity.findViewById(R.id.text_port_status11);
        text_port_status12 = activity.findViewById(R.id.text_port_status12);
        text_port_status13 = activity.findViewById(R.id.text_port_status13);
        text_port_status14 = activity.findViewById(R.id.text_port_status14);
        text_port_status15 = activity.findViewById(R.id.text_port_status15);
        text_port_status16 = activity.findViewById(R.id.text_port_status16);
        text_port_status17 = activity.findViewById(R.id.text_port_status17);
        text_port_status18 = activity.findViewById(R.id.text_port_status18);
        text_port_status19 = activity.findViewById(R.id.text_port_status19);
        text_port_status20 = activity.findViewById(R.id.text_port_status20);
    }

    private void updatePSystemStatus(String value){
//        0, 1~2, 3~5, 6, 7
//    测试模式、6~5当前门体状态（关、开启、开、关闭）、4~2运动阶段（停、加速、匀速、刹车、爬行、末段）、自学、定位
//        8~9, 10~12, 13, 14~15
//    5~3运行模式（单、闭、开、自）、联动心跳、1~0联动模式（双、从独、主独、独）
        if(value.length() < 4){
            return;
        }
        String Binaryvalue = ByteUtil.parseHexStr2Binary(value);

        if(Binaryvalue.charAt(11) == '0') {
            if(Binaryvalue.charAt(12) == '0'){
                text_system_status1.setText("单向模式");
            }else {
                text_system_status1.setText("常闭模式");
            }
        }else {
            if(Binaryvalue.charAt(12) == '0'){
                text_system_status1.setText("常开模式");
            }else {
                text_system_status1.setText("自动模式");
            }
        }

        if(Binaryvalue.charAt(14) == '0') {
            if(Binaryvalue.charAt(15) == '0'){
                text_system_status2.setText("双机联动");
            }else {
                text_system_status2.setText("从机独动");
            }
        }else {
            if(Binaryvalue.charAt(15) == '0'){
                text_system_status2.setText("主机独动");
            }else {
                text_system_status2.setText("各自独动");
            }
        }

        if(Binaryvalue.charAt(1) == '0') {
            if(Binaryvalue.charAt(2) == '0'){
                text_system_status3.setText("关门保持");
            }else {
                text_system_status3.setText("开门运动");
            }
        }else {
            if(Binaryvalue.charAt(2) == '0'){
                text_system_status3.setText("开门保持");
            }else {
                text_system_status3.setText("关门运动");
            }
        }

        if(Binaryvalue.charAt(3) == '0') {
            if(Binaryvalue.charAt(4) == '0') {
                if(Binaryvalue.charAt(5) == '0'){
                    text_system_status4.setText("停止");
                }else {
                    text_system_status4.setText("加速");
                }
            }else {
                if(Binaryvalue.charAt(5) == '0'){
                    text_system_status4.setText("匀速");
                }else {
                    text_system_status4.setText("刹车");
                }
            }

        }else {
            if(Binaryvalue.charAt(4) == '0') {
                if(Binaryvalue.charAt(5) == '0'){
                    text_system_status4.setText("爬行");
                }else {
                    text_system_status4.setText("末段");
                }
            }else {
                text_system_status4.setText("未知");
                }
        }

        if(Binaryvalue.charAt(0) == '1') {
            text_system_status5.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(6) == '1') {
            text_system_status6.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(7) == '1') {
            text_system_status7.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(13) == '1') {
            text_system_status8.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
    }
    private void updatePortStatus(String value){
        if(value.length() < 6){
            return;
        }
        String Binaryvalue = ByteUtil.parseHexStr2Binary(value);

        if(Binaryvalue.charAt(7) == '1') {
            text_port_status1.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(6) == '1') {
            text_port_status2.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(5) == '1') {
            text_port_status3.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(4) == '1') {
            text_port_status4.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(2) == '1') {
            text_port_status5.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(1) == '1') {
            text_port_status6.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(15) == '1') {
            text_port_status7.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(14) == '1') {
            text_port_status8.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(0) == '1') {
            text_port_status9.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(13) == '1') {
            text_port_status10.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(12) == '1') {
            text_port_status11.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(23) == '1') {
            text_port_status12.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(11) == '1') {
            text_port_status13.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(10) == '1') {
            text_port_status14.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(9) == '1') {
            text_port_status15.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(8) == '1') {
            text_port_status16.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(22) == '1') {
            text_port_status17.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(21) == '1') {
            text_port_status18.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(20) == '1') {
            text_port_status19.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(19) == '1') {
            text_port_status20.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }




    }
    //=================================
    private void initDIPStatus(){
        text_dip_status1 = activity.findViewById(R.id.text_dip_status1);
        text_dip_status2 = activity.findViewById(R.id.text_dip_status2);
        text_dip_status3 = activity.findViewById(R.id.text_dip_status3);
        text_dip_status4 = activity.findViewById(R.id.text_dip_status4);
        text_dip_status5 = activity.findViewById(R.id.text_dip_status5);
        text_dip_status6 = activity.findViewById(R.id.text_dip_status6);
        text_dip_status7 = activity.findViewById(R.id.text_dip_status7);
        text_dip_status8 = activity.findViewById(R.id.text_dip_status8);
        text_dip_status9 = activity.findViewById(R.id.text_dip_status9);
        text_dip_status10 = activity.findViewById(R.id.text_dip_status10);
    }
    private void updateDIPStatus(String value){
        if(value.length() < 4){
            return;
        }
        String Binaryvalue = ByteUtil.parseHexStr2Binary(value);
        if(Binaryvalue.charAt(7) == '1') {
            text_dip_status1.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(6) == '1') {
            text_dip_status2.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(5) == '1') {
            text_dip_status3.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(4) == '1') {
            text_dip_status4.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(3) == '1') {
            text_dip_status5.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(2) == '1') {
            text_dip_status6.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(1) == '1') {
            text_dip_status7.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(0) == '1') {
            text_dip_status8.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(14) == '1') {
            text_dip_status9.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
        if(Binaryvalue.charAt(15) == '1') {
            text_dip_status10.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary));
        }
    }


}
