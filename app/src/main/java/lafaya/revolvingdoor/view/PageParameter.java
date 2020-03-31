package lafaya.revolvingdoor.view;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;


import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import lafaya.revolvingdoor.model.ParameterRevolving;
import lafaya.revolvingdoor.model.ParameterSliding;
import lafaya.revolvingdoor.model.ParameterUpdate;
import lafaya.revolvingdoor.model.ParameterWingL;
import lafaya.revolvingdoor.model.ParameterWingR;


import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.utils.DataBase;
import lafaya.revolvingdoor.utils.SystemUIUtils;

// 参数调节界面说明：
// 打开对应菜单时，执行以下动作：
// 1、当打开对应菜单时，按菜单内序列从上到下查询，若查询不成功，
//      则在指定等待时间过后继续查询下一个参数。
// 2、当收起时停止查询
//
// 修改参数：
//


public class PageParameter {
    private Activity activity;
    private Handler handler;

    private RelativeLayout activity_parameter;
    //
    private Button button_parRevolving, button_parSliding,button_parWingL,button_parWingR;
    private LinearLayout layout_parameter_revolvingdoor,layout_parameter_sliding, layout_parameter_wingl, layout_parameter_wingr;

    //
    private ParameterRevolving parameterRevolving = new ParameterRevolving();
    private ParameterSliding parameterSliding = new ParameterSliding();
    private ParameterWingL parameterWingL = new ParameterWingL();
    private ParameterWingR parameterWingR = new ParameterWingR();

    //
    private TextView text_parameter_settitle,text_parameter_setold;
    private EditText edittext_parameter_setnew;
    private SeekBar seekbar_parameter_set;
    //参数保存或取消
    private Button button_parameter_save,button_parameter_cancle;
    private RelativeLayout layout_parameter_dialog;

    // linelayout 只显示一种。
//    public enum DoorType{NON,
//        REVOLVING,
//        SLIDING,
//        WINGL,
//        WINGR;}

    private DataBase.DoorType layoutVisible = DataBase.DoorType.NON;

    private int dialog_maxvalue = 1;
    private int dialog_minvalue = 0;
    private float dialog_step = 1.0f;
    //
    private Timer mTimer = null;
    private TimerTask mTimerTask = null;
    //
    Context mContext =MainActivity.sContext;
    //类接口函数定义
    private static class InstanceHolder {
        private static PageParameter sManager = new PageParameter();
    }
    public static PageParameter instance(){
        return PageParameter.InstanceHolder.sManager;
    }

    //=====================================================
    public void PageInit(Activity inactivity, Handler inhandler){

        activity = inactivity;
        handler = inhandler;
//        mContext =

        activity_parameter = activity.findViewById(R.id.layout_activity_parameter);

        //旋转门参数
        button_parRevolving = activity.findViewById(R.id.button_parRevolving);
        layout_parameter_revolvingdoor = activity.findViewById(R.id.layout_parameter_revolvingdoor);
        parameterRevolving.initializeParRevolving(activity);

        button_parSliding = activity.findViewById(R.id.button_parSliding);
        layout_parameter_sliding = activity.findViewById(R.id.layout_parameter_sliding);
        parameterSliding.initializeParSliding(activity);

        button_parWingL = activity.findViewById(R.id.button_parWingL);
        layout_parameter_wingl = activity.findViewById(R.id.layout_parameter_wingl);
         parameterWingL.initializeParWingL(activity);

        button_parWingR = activity.findViewById(R.id.button_parWingR);
        layout_parameter_wingr = activity.findViewById(R.id.layout_parameter_wingr);
        parameterWingR.initializeParWingR(activity);

        //参数输入初始化 ================
        //parameter set dialog
        layout_parameter_dialog = (RelativeLayout) activity.findViewById(R.id.layout_parameter_dialog);
        layout_parameter_dialog.setVisibility(View.GONE);
        text_parameter_settitle = (TextView)activity.findViewById(R.id.text_parameter_settitle);
        edittext_parameter_setnew = (EditText)activity.findViewById(R.id.edittext_parameter_setnew);
        text_parameter_setold = (TextView)activity.findViewById(R.id.text_parameter_setold);
        seekbar_parameter_set = (SeekBar)activity.findViewById(R.id.seekbar_parameter_set);

        //save & cancle button
        button_parameter_save = (Button)activity.findViewById(R.id.button_parameter_save);
        button_parameter_cancle = (Button)activity.findViewById(R.id.button_parameter_cancle);
        showDialog(false);
        // ===============================

        //初始化页面布局
        updateLayout();


        // 参数设置 ~ 查询监控

        /* 旋转门主体参数*/
        button_parRevolving.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parRevolvingPressed();
            }
        });

        /* 平滑自动门参数*/
        button_parSliding.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parSlidingPressed();
            }
        });

        /* 门翼1参数*/
        button_parWingL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parWingLPressed();
            }
        });

        /* 门翼2参数*/
        button_parWingR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parWingRPressed();
            }
        });

        /*参数修改操作监听*/
        parameter_changed_listener();

        //取消参数修改
        button_parameter_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                paramter_cancle();
            }
        });
        // 保存参数
        button_parameter_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                parameter_saving();
            }
        });

    }

    //页面显示 / 隐藏
    public void showPageParameter(boolean flag){
        if(flag){
            activity_parameter.setVisibility(View.VISIBLE);
        }else{
            layoutVisible = DataBase.DoorType.NON;
            updateLayout();
            activity_parameter.setVisibility(View.GONE);
        }
    }

    /* 旋转门主体参数*/
    private void parRevolvingPressed(){
        if(layoutVisible == DataBase.DoorType.REVOLVING){
            layoutVisible = DataBase.DoorType.NON;
            parameterRevolving.checkWaiting = false;
        }else{
            layoutVisible = DataBase.DoorType.REVOLVING;
            parameterRevolving.checkWaiting = true;
        }
        updateLayout();
    }

    /* 平滑自动门参数*/
    private void parSlidingPressed(){
        if(layoutVisible == DataBase.DoorType.SLIDING){
            layoutVisible = DataBase.DoorType.NON;
            parameterSliding.checkWaiting = false;
        }else{
            layoutVisible = DataBase.DoorType.SLIDING;
            parameterSliding.checkWaiting = true;
        }

        updateLayout();
    }

    /* 门翼1参数*/
    private void parWingLPressed(){
        if(layoutVisible == DataBase.DoorType.WINGL){
            layoutVisible = DataBase.DoorType.NON;
            parameterWingL.checkWaiting = false;
        }else{
            layoutVisible = DataBase.DoorType.WINGL;
            parameterWingL.checkWaiting = true;
        }

        updateLayout();
    }

    /* 门翼2参数*/
    private void parWingRPressed(){
        if(layoutVisible == DataBase.DoorType.WINGR){
            layoutVisible = DataBase.DoorType.NON;
            parameterWingR.checkWaiting = false;
        }else{
            layoutVisible = DataBase.DoorType.WINGR;
            parameterWingR.checkWaiting = true;
        }

        updateLayout();
    }



    // 显示更改
    private void layoutChange(int[] state_tmp){
        layout_parameter_revolvingdoor.setVisibility(state_tmp[0]);
        layout_parameter_sliding.setVisibility(state_tmp[1]);
        layout_parameter_wingl.setVisibility(state_tmp[2]);
        layout_parameter_wingr.setVisibility(state_tmp[3]);
    }



    //界面显示更新
    public void updateLayout(){
        int[] state_tmp = {View.GONE, View.GONE, View.GONE, View.GONE};

        //根据当前显示的参数页面，执行如下操作：
        // 1、显示/隐藏
        // 2、查询参数
        // 3、

        switch (layoutVisible){
            case NON:
                showDialog(false);
                //如果在查询参数，则停止查询
                stopTimer();
                break;
            case REVOLVING:
                state_tmp[0] = View.VISIBLE;
                //查询参数，以上一次查询位置为起点
                if(parameterRevolving.checkWaiting) {
                    parameterRevolving.checkParaRevolving();
                    startTimer();
                }
                break;
            case SLIDING:
                state_tmp[1] = View.VISIBLE;
                //查询参数
                //查询参数，以上一次查询位置为起点
                if(parameterSliding.checkWaiting) {
                    parameterSliding.checkParaSliding();
                    startTimer();
                }
                break;
            case WINGL:
                state_tmp[2] = View.VISIBLE;
                //查询参数
                //查询参数，以上一次查询位置为起点
                if(parameterWingL.checkWaiting) {
                    parameterWingL.checkParaWingL();
                    startTimer();
                }
                break;
            case WINGR:
                state_tmp[3] = View.VISIBLE;
                //查询参数
                //查询参数，以上一次查询位置为起点
                if(parameterWingR.checkWaiting) {
                    parameterWingR.checkParaWingR();
                    startTimer();
                }
                break;
            default:
                break;
        }

        //菜单展开或收起操作
        layoutChange(state_tmp);

        //更改菜单键箭头显示方向,展开时向下，收起时向上
        SystemUIUtils.instance().buttonIconChange(mContext,button_parRevolving,state_tmp[0]);
        SystemUIUtils.instance().buttonIconChange(mContext,button_parSliding,state_tmp[1]);
        SystemUIUtils.instance().buttonIconChange(mContext,button_parWingL,state_tmp[2]);
        SystemUIUtils.instance().buttonIconChange(mContext,button_parWingR,state_tmp[3]);
        parameterRevolving.updateGrideParRevolving(activity);
        parameterSliding.updateGrideParSliding(activity);
        parameterWingL.updateGrideParWingL(activity);
        parameterWingR.updateGrideParWingR(activity);

        // 当展开时，仅显示当前被按下的按键
        if(layoutVisible == DataBase.DoorType.NON){
            button_parRevolving.setVisibility(View.VISIBLE);
            button_parSliding.setVisibility(View.VISIBLE);
            button_parWingL.setVisibility(View.VISIBLE);
            button_parWingR.setVisibility(View.VISIBLE);
        }else {
            button_parRevolving.setVisibility(state_tmp[0]);
            button_parSliding.setVisibility(state_tmp[1]);
            button_parWingL.setVisibility(state_tmp[2]);
            button_parWingR.setVisibility(state_tmp[3]);
        }

    }

    // 参数设置对话框
    public void parameter_dialog(boolean flag,String name,int maxv, int minv, int nowv, float step){
        dialog_maxvalue = maxv;
        dialog_minvalue = minv;
        dialog_step = step;

        if(flag){
            //需要禁止其它操作

            text_parameter_settitle.setText(name);
            text_parameter_setold.setText(Integer.toString(nowv));

            //显示数字键盘及光标
            edittext_parameter_setnew.setText(Integer.toString(nowv));
            edittext_parameter_setnew.setFocusable(true);
            edittext_parameter_setnew.setFocusableInTouchMode(true);
            edittext_parameter_setnew.requestFocus();

            //InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            //inputManager.showSoftInput(edittext_parameter_setnew, 0);

            seekbar_parameter_set.setMax((int)((maxv - minv)/step));
            seekbar_parameter_set.setProgress((int)((nowv - minv)/step));
            showDialog(true);
        }else {
            showDialog(false);
        }
    }

    // parameter changed listener
    private void parameter_changed_listener(){
        //seekbar listener
        seekbar_parameter_set.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                edittext_parameter_setnew.setText(Integer.toString((int)(seekbar_parameter_set.getProgress() * dialog_step) + dialog_minvalue));
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //edittext listener
        edittext_parameter_setnew.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                if(count > 0) {
                    //updata seekbar
                    int tmp = Integer.parseInt(edittext_parameter_setnew.getText().toString());
                    if ((tmp >= dialog_minvalue) && (tmp <= dialog_maxvalue)) {
                        seekbar_parameter_set.setProgress((int)((tmp - dialog_minvalue) / dialog_step));
                    } else if (tmp > dialog_maxvalue) {
                        edittext_parameter_setnew.setText(Integer.toString(dialog_maxvalue));
                    }
                    Editable b = edittext_parameter_setnew.getText();
                    edittext_parameter_setnew.setSelection(b.length());
                }
            }
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    //parameter cancle
    private void paramter_cancle(){
        //清除参数
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(edittext_parameter_setnew.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        parameter_dialog(false," ",0,0,0,0);
    }

    //parameter saving
    private void parameter_saving(){

        String value = Integer.toHexString(Integer.parseInt(edittext_parameter_setnew.getText().toString())).toUpperCase();
        if((value.length() % 2) != 0){
            value = "0" + value;
        }

        //获取当前参数，并发送保存
         switch (layoutVisible) {
            case REVOLVING://旋转门
                ParameterUpdate.instance().setRDParameter(parameterRevolving.posSelected, value);
                break;
            case SLIDING://平滑门
                ParameterUpdate.instance().setParameter(parameterSliding.posParSelected, MainActivity.sContext.getString(R.string.addAutoDoor),value);
                break;
            case WINGR:
                ParameterUpdate.instance().setParameter(parameterWingR.posParSelected, MainActivity.sContext.getString(R.string.addSwingR),value);
                break;
            case WINGL:
                ParameterUpdate.instance().setParameter(parameterWingL.posParSelected, MainActivity.sContext.getString(R.string.addSwingL),value);
                break;
            default:
                break;
        }

        //关闭参数设置窗口和数字键盘，显示参数设置中。
        InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(edittext_parameter_setnew.getWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
        parameter_dialog(false," ",0,0,0,0);

    }

    // 按键显示 or 禁止
    private void showDialog(boolean flag){
        if(flag){
            layout_parameter_dialog.setVisibility(View.VISIBLE);
            button_parRevolving.setEnabled(false);
            parameterRevolving.enableGrid(false);
            button_parSliding.setEnabled(false);
            parameterSliding.enableGrid(false);
            button_parWingR.setEnabled(false);
            parameterWingR.enableGrid(false);
            button_parWingL.setEnabled(false);
            parameterWingL.enableGrid(false);

        }else {
            layout_parameter_dialog.setVisibility(View.GONE);
            button_parRevolving.setEnabled(true);
            parameterRevolving.enableGrid(true);
            button_parSliding.setEnabled(true);
            parameterSliding.enableGrid(true);
            button_parWingR.setEnabled(true);
            parameterWingR.enableGrid(true);
            button_parWingL.setEnabled(true);
            parameterWingL.enableGrid(true);
        }
    }

    //根据位置，进行相应操作
    // pos 0~14 , other
    // pos 15~21 , speed
    // pos 22~28 , length
    // pos 29~35 , current
    // pos 36~45 , mcpwm
    public void gridViewParaSelected(int pos, HashMap<String, Integer> listTemp){
        String name = "";
        int maxv = 0;
        int minv = 0;
        int nowv = 0;
        float step = 0;
        switch (pos) {
            case 0://门扇开度
                name = mContext.getString(R.string.parSDOpenRate);
                maxv = 100;
                minv = 0;
                nowv = listTemp.get(name);
                step = 1;
                break;
            case 1://运行平稳度
                name = mContext.getString(R.string.parSDSmoothness);
                maxv = 10;
                minv = 1;
                nowv = listTemp.get(name);
                step = 1;
                break;
            case 2://开门保持时间
                name = mContext.getString(R.string.parSDKeepOpenTime);
                maxv = 60;
                minv = 0;
                nowv = listTemp.get(name);
                step = 1;
                break;
            case 3://最大遇阻重试次数
                name = mContext.getString(R.string.parSDResistanceRetryTimesMax);
                maxv = 10;
                minv = 0;
                nowv = listTemp.get(name);
                step = 1;
                break;
            case 4://遇阻重试间隔
                name = mContext.getString(R.string.parSDResistanceRetryInterval);
                maxv = 60;
                minv = 0;
                nowv = listTemp.get(name);
                step = 10;
                break;
            case 5://遇阻重试间隔
                name = mContext.getString(R.string.parSDReverseBreak);
                maxv = 1;
                minv = 0;
                nowv = listTemp.get(name);
                step = 1;
                break;
            case 6://常开模式保持时间
                name = mContext.getString(R.string.parSDKeepModeOpenTime);
                maxv = 60;
                minv = 0;
                nowv = listTemp.get(name);
                step = 1;
                break;
            case 7://备用电源
                name = mContext.getString(R.string.parSDBatteryEnable);
                maxv = 1;
                minv = 0;
                nowv = listTemp.get(name);
                step = 1;
                break;
            case 8://超温上限温度
                name = mContext.getString(R.string.parSDTempOverUpper);
                maxv = 100;
                minv = 60;
                nowv = listTemp.get(name);
                step = 1;
                break;
            case 9://超温下限温度
                name = mContext.getString(R.string.parSDTempOverLower);
                maxv = 60;
                minv = 40;
                nowv = listTemp.get(name);
                step = 1;
                break;
            case 10://测试模式锁动作频率
                name = mContext.getString(R.string.parSDTestModeLockFrequency);
                maxv = 60;
                minv = 0;
                nowv = listTemp.get(name);
                step = 1;
                break;
            case 11://系统失电动作
                name = mContext.getString(R.string.parSDPowerLowMotion);
                maxv = 1;
                minv = 0;
                nowv = listTemp.get(name);
                step = 1;
                break;
            case 12://锁动作延迟时间
                name = mContext.getString(R.string.parSDLockMotionDelay);
                maxv = 60;
                minv = 0;
                nowv = listTemp.get(name);
                step = 1;
                break;
            case 13://锁动作失败重试次数
                name = mContext.getString(R.string.parSDLockRetryTimes);
                maxv = 30;
                minv = 0;
                nowv = listTemp.get(name);
                step = 1;
                break;
            case 14://锁动作失败重试间隔
                name = mContext.getString(R.string.parSDLockRetryInterval);
                maxv = 60;
                minv = 0;
                nowv = listTemp.get(name);
                step = 10;
                break;
            case 15://开门最大速度
                name = mContext.getString(R.string.parSDSpeedOpenMax);
                maxv = 700;
                minv = 10;
                nowv = listTemp.get(mContext.getString(R.string.parSDSpeedOpenMax));
                step = 10;
                break;
            case 16://关门最大速度
                name = mContext.getString(R.string.parSDSpeedCloseMax);
                maxv = 700;
                minv = 10;
                nowv = listTemp.get(mContext.getString(R.string.parSDSpeedCloseMax));
                step = 10;
                break;
            case 17://缓行速度
                name = mContext.getString(R.string.parSDSpeedCrawl);
                maxv = 200;
                minv = 5;
                nowv = listTemp.get(mContext.getString(R.string.parSDSpeedCrawl));
                step = 1;
                break;
            case 18://速度过低门槛
                name = mContext.getString(R.string.parSDSpeedSlow);
                maxv = 50;
                minv = 1;
                nowv = listTemp.get(mContext.getString(R.string.parSDSpeedSlow));
                step = 1;
                break;
            case 19://速度跌落比
                name = mContext.getString(R.string.parSDSpeedDropRate);
                maxv = 95;
                minv = 25;
                nowv = listTemp.get(mContext.getString(R.string.parSDSpeedDropRate));
                step = 1;
                break;
            case 20://速度锁定
                name = mContext.getString(R.string.parSDSpeedLock);
                maxv = 1;
                minv = 0;
                nowv = listTemp.get(mContext.getString(R.string.parSDSpeedLock));
                step = 1;
                break;
            case 21://最小加速时间
                name = mContext.getString(R.string.parSDSpeedUpTimeMin);
                maxv = 2000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parSDSpeedUpTimeMin));
                step = 100;
                break;
            case 22://开门爬行距离
                name = mContext.getString(R.string.parSDLengthCrawlOpen);
                maxv = 5000;
                minv = 0;
                nowv = listTemp.get(mContext.getString(R.string.parSDLengthCrawlOpen));
                step = 100;
                break;
            case 23://开门末段距离
                name = mContext.getString(R.string.parSDLengthEndOpen);
                maxv = 5000;
                minv = 0;
                nowv = listTemp.get(mContext.getString(R.string.parSDLengthEndOpen));
                step = 100;
                break;
            case 24://关门爬行距离
                name = mContext.getString(R.string.parSDLengthCrawlClose);
                maxv = 5000;
                minv = 0;
                nowv = listTemp.get(mContext.getString(R.string.parSDLengthCrawlClose));
                step = 100;
                break;
            case 25://关门末段距离
                name = mContext.getString(R.string.parSDLengthEndClose);
                maxv = 5000;
                minv = 0;
                nowv = listTemp.get(mContext.getString(R.string.parSDLengthEndClose));
                step = 100;
                break;
            case 26://关门保持上限
                name = mContext.getString(R.string.parSDLengthHoldUp);
                maxv = 5000;
                minv = 0;
                nowv = listTemp.get(mContext.getString(R.string.parSDLengthHoldUp));
                step = 100;
                break;
            case 27://关门保持下限
                name = mContext.getString(R.string.parSDLengthEndDown);
                maxv = 5000;
                minv = 0;
                nowv = listTemp.get(mContext.getString(R.string.parSDLengthEndDown));
                step = 100;
                break;
            case 28://开门缓行距离
                name = mContext.getString(R.string.parSDLengthSlowOpen);
                maxv = 5000;
                minv = 0;
                nowv = listTemp.get(mContext.getString(R.string.parSDLengthSlowOpen));
                step = 100;
                break;
            case 29://最大电流限流值
                name = mContext.getString(R.string.parSDCurrentLimitMax);
                maxv = 15000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parSDCurrentLimitMax));
                step = 100;
                break;
            case 30://电流限流一档
                name = mContext.getString(R.string.parSDCurrentLimitOne);
                maxv = 15000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parSDCurrentLimitOne));
                step = 100;
                break;
            case 31://电流限流二档
                name = mContext.getString(R.string.parSDCurrentLimitTwo);
                maxv = 15000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parSDCurrentLimitTwo));
                step = 100;
                break;
            case 32://电流限流三档
                name = mContext.getString(R.string.parSDCurrentLimitThree);
                maxv = 15000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parSDCurrentLimitThree));
                step = 100;
                break;
            case 33://电流限流四档
                name = mContext.getString(R.string.parSDCurrentLimitFour);
                maxv = 15000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parSDCurrentLimitFour));
                step = 100;
                break;
            case 34://开门保持电流
                name = mContext.getString(R.string.parSDCurrentKeepOpen);
                maxv = 1000;
                minv = 10;
                nowv = listTemp.get(mContext.getString(R.string.parSDCurrentKeepOpen));
                step = 10;
                break;
            case 35://关门保持电流
                name = mContext.getString(R.string.parSDCurrentKeepClose);
                maxv = 1000;
                minv = 10;
                nowv = listTemp.get(mContext.getString(R.string.parSDCurrentKeepClose));
                step = 10;
                break;
            case 36://PID调节周期
                name = mContext.getString(R.string.parSDPIDPeriod);
                maxv = 100;
                minv = 1;
                nowv = listTemp.get(mContext.getString(R.string.parSDPIDPeriod));
                step = 1;
                break;
            case 37://PID驱动系数
                name = mContext.getString(R.string.parSDPIDDrive);
                maxv = 50;
                minv = 1;
                nowv = listTemp.get(mContext.getString(R.string.parSDPIDDrive));
                step = 1;
                break;
            case 38://PID撤消系数
                name = mContext.getString(R.string.parSDPIDRecall);
                maxv = 100;
                minv = 20;
                nowv = listTemp.get(mContext.getString(R.string.parSDPIDRecall));
                step = 1;
                break;
            case 39://PID制动系数
                name = mContext.getString(R.string.parSDPIDBreak);
                maxv = 100;
                minv = 1;
                nowv = listTemp.get(mContext.getString(R.string.parSDPIDBreak));
                step = 1;
                break;
            case 40://PID反向系数
                name = mContext.getString(R.string.parSDPIDReverse);
                maxv = 50;
                minv = 1;
                nowv = listTemp.get(mContext.getString(R.string.parSDPIDReverse));
                step = 1;
                break;
            case 41://PWM最大值
                name = mContext.getString(R.string.parSDPWMMax);
                maxv = 400;
                minv = 300;
                nowv = listTemp.get(mContext.getString(R.string.parSDPWMMax));
                step = 1;
                break;
            case 42://PWM最小值
                name = mContext.getString(R.string.parSDPWMMin);
                maxv = 100;
                minv = 0;
                nowv = listTemp.get(mContext.getString(R.string.parSDPWMMin));
                step = 1;
                break;
            case 43://匀速不减PWM门槛
                name = mContext.getString(R.string.parSDPWMEvenSpeedReduceThreshold);
                maxv = 300;
                minv = 10;
                nowv = listTemp.get(mContext.getString(R.string.parSDPWMEvenSpeedReduceThreshold));
                step = 10;
                break;
            case 44://刹车不增PWM门槛
                name = mContext.getString(R.string.parSDPWMBreakSpeedIncreaseThreshold);
                maxv = 500;
                minv = 10;
                nowv = listTemp.get(mContext.getString(R.string.parSDPWMBreakSpeedIncreaseThreshold));
                step = 10;
                break;
            case 45://爬行、末段不减PWM门槛
                name = mContext.getString(R.string.parSDPWMCrawlSpeedReducePWMThreshold);
                maxv = 300;
                minv = 10;
                nowv = listTemp.get(mContext.getString(R.string.parSDPWMCrawlSpeedReducePWMThreshold));
                step = 10;
                break;
            default:
                return;
        }
        PageParameter.instance().parameter_dialog(true,name, maxv, minv, nowv, step);
    }


    // pos 0~6 , other
    // pos 7~15 , length
    // pos 16~23 , driver
    // 旋转门的参数显示
    public void gridViewRDParaSelected(int pos, HashMap<String, Integer> listTemp){
        String name = "";
        int maxv = 0;
        int minv = 0;
        int nowv = 0;
        float step = 0;
        switch (pos){
            case 0://高速运行速度
                name = mContext.getString(R.string.parRDSpeedHigh);
                maxv = 1000;
                minv = 10;
                nowv = listTemp.get(mContext.getString(R.string.parRDSpeedHigh));
                step = 10;
                break;
            case 1://中速运行速度
                name = mContext.getString(R.string.parRDSpeedMid);
                maxv = 600;
                minv = 10;
                nowv = listTemp.get(mContext.getString(R.string.parRDSpeedMid));
                step = 10;
                break;
            case 2://低速运行速度
                name = mContext.getString(R.string.parRDSpeedLow);
                maxv = 400;
                minv = 10;
                nowv = listTemp.get(mContext.getString(R.string.parRDSpeedLow));
                step = 10;
                break;
            case 3://危险区域宽度
                name = mContext.getString(R.string.parRDRiskWidth);
                maxv = 1500;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parRDRiskWidth));
                step = 10;
                break;
            case 4://危险区域起点
                name = mContext.getString(R.string.parRDRiskOrigin);
                maxv = 30000;
                minv = 0;
                nowv = listTemp.get(mContext.getString(R.string.parRDRiskOrigin));
                step = 10;
                break;
            case 5://夜间锁门位置
                name = mContext.getString(R.string.parRDPositionLock);
                maxv = 30000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parRDPositionLock));
                step = 100;
                break;
            case 6://冬季停机位置
                name = mContext.getString(R.string.parRDPositionStopWinter);
                maxv = 30000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parRDPositionStopWinter));
                step = 100;
                break;
            case 7://夏季停机位置
                name = mContext.getString(R.string.parRDPositionStopSummer);
                maxv = 10000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parRDPositionStopSummer));
                step = 10;
                break;
            case 8://平滑门停机位置
                name = mContext.getString(R.string.parRDPositionStopSliding);
                maxv = 10000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parRDPositionStopSliding));
                step = 10;
                break;
            case 9://怠速停转角度
                name = mContext.getString(R.string.parRDAngleIdling);
                maxv = 10000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parRDAngleIdling));
                step = 10;
                break;
            case 10://冬季停转角度
                name = mContext.getString(R.string.parRDAngleWinter);
                maxv = 10000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parRDAngleWinter));
                step = 10;
                break;
            case 11://夏季停转角度
                name = mContext.getString(R.string.parRDAngleSummer);
                maxv = 10000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parRDAngleSummer));
                step = 10;
                break;
            case 12://残障停转角度
                name = mContext.getString(R.string.parRDAngleDisability);
                maxv = 10000;
                minv = 100;
                nowv = listTemp.get(mContext.getString(R.string.parRDAngleDisability));
                step = 10;
                break;
            default:
                return;
        }
        PageParameter.instance().parameter_dialog(true,name, maxv, minv, nowv, step);

    }


    private void startTimer(){
        stopTimer();
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if(checkFinished()){
                    stopTimer();
                }else {
                    // 如果还没查完，则继续查
                    if((parameterRevolving.checkWaiting) || (parameterSliding.checkWaiting) || (parameterWingR.checkWaiting) || (parameterWingL.checkWaiting)) {
                        startTimer();
                    }
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
                return parameterRevolving.checkParaRevolving();
            case SLIDING://平滑门
                return parameterSliding.checkParaSliding();
            case WINGR:
                return parameterWingR.checkParaWingR();
            case WINGL:
                return parameterWingL.checkParaWingL();
            default:
                return true;
        }
    }



    // handle 更新
    public void updateMsg(){
        Message msg = new Message();
        msg.what = 3;
        Bundle bundle = new Bundle();
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
}
