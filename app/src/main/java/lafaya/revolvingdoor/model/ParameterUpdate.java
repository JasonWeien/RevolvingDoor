package lafaya.revolvingdoor.model;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.serilport.RS485Protocol;
import lafaya.revolvingdoor.serilport.RS485SendCommand;
import lafaya.revolvingdoor.serilport.SerialPortThread;
import lafaya.revolvingdoor.utils.GridUtils;
import lafaya.revolvingdoor.view.MainActivity;


public class ParameterUpdate{


    private Handler handler;
    private paraUpdateThread mparaUpdateThread;

    //类接口函数定义
    private static class InstanceHolder {
        private static ParameterUpdate sManager = new ParameterUpdate();
    }
    public static ParameterUpdate instance(){
        return ParameterUpdate.InstanceHolder.sManager;
    }

    public void  startThread(Handler hd){
        handler = hd;
        mparaUpdateThread = new paraUpdateThread();
        mparaUpdateThread.start();
    }

    public void  stopThread(){
        mparaUpdateThread.interrupt();
        mparaUpdateThread = null;
    }

    private class paraUpdateThread extends Thread {
        @Override
        public void run() {
            while (true) {
                if (SerialPortThread.instance().isReceiveMsg) {
                    String paraBuffer;
                    SerialPortThread.instance().isReceiveMsg = false;
                    // 数据校验并处理CmdReceiveProcess
                    paraBuffer = RS485Protocol.instance().ReceiveCheck(SerialPortThread.instance().mReBuffer);
                    RS485Protocol.instance().CmdReceiveProcess(paraBuffer);
                    // ---------命令字接收完成，并校验通过。
//                if(paraBuffer != null){
//                    RS485Protocol.instance().
//                }

                    // test ==========
//                    showMsg(paraBuffer);
                    // ======================
                }
            }
        }
    }

    //===================test =======
    //主线程显示数据
    public void showMsg(String text){
        Message msg = new Message();
        msg.what = 99;
        Bundle bundle = new Bundle();
        bundle.putString("msg",text);
        msg.setData(bundle);
        handler.sendMessage(msg);
    }
    //=============================

    //=============================可修改参数集定义 ===============================================
    // 旋转门专用参数集
    public List<HashMap<String, Object>> listParameterRD(HashMap<String, Integer> listmap){
        List<HashMap<String, Object>> list = new ArrayList<>();
        // other
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDSpeedNormal),
                listmap.get(MainActivity.sContext.getString(R.string.parRDSpeedNormal)),"mm/s"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDSpeedW_S),
                listmap.get(MainActivity.sContext.getString(R.string.parRDSpeedW_S)),"mm/s"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDSpeedIdle),
                listmap.get(MainActivity.sContext.getString(R.string.parRDSpeedIdle)),"mm/s"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDSpeedSlow),
                listmap.get(MainActivity.sContext.getString(R.string.parRDSpeedSlow)),"mm/s"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDCurrentLimitMax),
                listmap.get(MainActivity.sContext.getString(R.string.parRDCurrentLimitMax)),"mA"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDCurrentRunningLearn),
                listmap.get(MainActivity.sContext.getString(R.string.parRDCurrentRunningLearn)),"mA"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDDragRunningThreshold),
                listmap.get(MainActivity.sContext.getString(R.string.parRDDragRunningThreshold)),"mN"));
        // length
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDPositionLock),
                listmap.get(MainActivity.sContext.getString(R.string.parRDPositionLock)),"mm"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDPositionSlidingStop),
                listmap.get(MainActivity.sContext.getString(R.string.parRDPositionSlidingStop)),"mm"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDPositionWinterStop),
                listmap.get(MainActivity.sContext.getString(R.string.parRDPositionWinterStop)),"mm"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDPositionRisk1Start),
                listmap.get(MainActivity.sContext.getString(R.string.parRDPositionRisk1Start)),"mm"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDPositionRisk1End1),
                listmap.get(MainActivity.sContext.getString(R.string.parRDPositionRisk1End1)),"mm"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDPositionRisk1End2),
                listmap.get(MainActivity.sContext.getString(R.string.parRDPositionRisk1End2)),"mm"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDPositionRisk1End2),
                listmap.get(MainActivity.sContext.getString(R.string.parRDPositionRisk2Start)),"mm"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDPositionRisk1End2),
                listmap.get(MainActivity.sContext.getString(R.string.parRDPositionRisk2End1)),"mm"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDPositionRisk1End2),
                listmap.get(MainActivity.sContext.getString(R.string.parRDPositionRisk2End2)),"mm"));
        //driver
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDDriverCanBaudRate),
                listmap.get(MainActivity.sContext.getString(R.string.parRDDriverCanBaudRate))," "));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDDriverControlMode),
                listmap.get(MainActivity.sContext.getString(R.string.parRDDriverControlMode))," "));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDDriverSpeedUp),
                listmap.get(MainActivity.sContext.getString(R.string.parRDDriverSpeedUp))," "));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDDriverBraking),
                listmap.get(MainActivity.sContext.getString(R.string.parRDDriverBraking))," "));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDDriverEmergencyBraking),
                listmap.get(MainActivity.sContext.getString(R.string.parRDDriverEmergencyBraking))," "));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDDriverPIDSpeed),
                listmap.get(MainActivity.sContext.getString(R.string.parRDDriverPIDSpeed))," "));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDDriverPIDSubSPeed),
                listmap.get(MainActivity.sContext.getString(R.string.parRDDriverPIDSubSPeed))," "));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parRDDriverPIDCurrent),
                listmap.get(MainActivity.sContext.getString(R.string.parRDDriverPIDCurrent))," "));

        return list;
    }
    public HashMap<String, Integer> paraRDUpdate(HashMap<String, Integer> listmap,String index, int value){
        if(listmap == null){
            listmap = new HashMap<>();
            // other
            listmap.put(MainActivity.sContext.getString(R.string.parRDSpeedNormal),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDSpeedW_S),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDSpeedIdle),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDSpeedSlow),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDCurrentLimitMax),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDCurrentRunningLearn),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDDragRunningThreshold),0);
            // length
            listmap.put(MainActivity.sContext.getString(R.string.parRDPositionLock),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDPositionSlidingStop),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDPositionWinterStop),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDPositionSummerStop),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDPositionRisk1Start),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDPositionRisk1End1),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDPositionRisk1End2),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDPositionRisk2Start),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDPositionRisk2End1),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDPositionRisk2End2),0);
            //driver
            listmap.put(MainActivity.sContext.getString(R.string.parRDDriverCanBaudRate),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDDriverControlMode),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDDriverSpeedUp),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDDriverBraking),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDDriverEmergencyBraking),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDDriverPIDSpeed),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDDriverPIDSubSPeed),0);
            listmap.put(MainActivity.sContext.getString(R.string.parRDDriverPIDCurrent),0);

        }else {
            if(index != null){
                listmap.put(index,value);
            }
        }
        return listmap;
    }

    //通用参数集
    //=========================================
    //距离参数，7个
    //速度参数
    //电流参数
    //PWM参数
    //其它参数
    // pos 0~14 , other
    // pos 15~21 , speed
    // pos 22~28 , length
    // pos 29~35 , current
    // pos 36~45 , mcpwm

    public List<HashMap<String, Object>> listParaNormal(HashMap<String, Integer> listmap){
        List<HashMap<String, Object>> list = new ArrayList<>();

        //other
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDOpenRate),
                listmap.get(MainActivity.sContext.getString(R.string.parSDOpenRate)),"%"));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDSmoothness),
                listmap.get(MainActivity.sContext.getString(R.string.parSDSmoothness))));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDKeepOpenTime),
                listmap.get(MainActivity.sContext.getString(R.string.parSDKeepOpenTime)),"s"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDResistanceRetryTimesMax),
                listmap.get(MainActivity.sContext.getString(R.string.parSDResistanceRetryTimesMax)),"次"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDResistanceRetryInterval),
                listmap.get(MainActivity.sContext.getString(R.string.parSDResistanceRetryInterval)),"ms"));
        if(listmap.get(MainActivity.sContext.getString(R.string.parSDReverseBreak)) == 0) {
            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDReverseBreak),
                    "禁止"));
        }else {
            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDReverseBreak),
                    "允许"));
        }

        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDKeepModeOpenTime),
                listmap.get(MainActivity.sContext.getString(R.string.parSDKeepModeOpenTime)),"s"));

        if(listmap.get(MainActivity.sContext.getString(R.string.parSDBatteryEnable)) == 0) {
            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDBatteryEnable),
                    "禁止"));
        }else{
            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDBatteryEnable),
                    "允许"));
        }
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDTempOverUpper),
                listmap.get(MainActivity.sContext.getString(R.string.parSDTempOverUpper)),"℃"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDTempOverLower),
                listmap.get(MainActivity.sContext.getString(R.string.parSDTempOverLower)),"℃"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDTestModeLockFrequency),
                listmap.get(MainActivity.sContext.getString(R.string.parSDTestModeLockFrequency)),"%"));

        if(listmap.get(MainActivity.sContext.getString(R.string.parSDPowerLowMotion)) == 0) {
            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDPowerLowMotion),
                    "锁门"));
        }else{
            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDPowerLowMotion),
                    "开门"));
        }
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDLockMotionDelay),
                listmap.get(MainActivity.sContext.getString(R.string.parSDLockMotionDelay)),"ms"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDLockRetryTimes),
                listmap.get(MainActivity.sContext.getString(R.string.parSDLockRetryTimes)),"次"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDLockRetryInterval),
                listmap.get(MainActivity.sContext.getString(R.string.parSDLockRetryInterval)),"ms"));

        // speed
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDSpeedOpenMax),
                listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedOpenMax)),"mm/s"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDSpeedCloseMax),
                listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedCloseMax)),"mm/s"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDSpeedCrawl),
                listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedCrawl)),"mm/s"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDSpeedSlow),
                listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedSlow)),"mm/s"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDSpeedDropRate),
                listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedDropRate)),"%"));
        //
        if(listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedLock)) == 0) {
            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDSpeedLock),
                    "未锁定"));
        }else {
            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDSpeedLock),
                    "锁定"));
        }

        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDSpeedUpTimeMin),
                listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedUpTimeMin)),"ms"));
        // length
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthCrawlOpen),
                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthCrawlOpen))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthEndOpen),
                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthEndOpen))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthCrawlClose),
                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthCrawlClose))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthEndClose),
                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthEndClose))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthHoldUp),
                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthHoldUp))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthEndDown),
                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthEndDown))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthSlowOpen),
                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthSlowOpen))));
        // current
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentLimitMax),
                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentLimitMax)),"mA"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentLimitOne),
                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentLimitOne)),"mA"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentLimitTwo),
                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentLimitTwo)),"mA"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentLimitThree),
                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentLimitThree)),"mA"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentLimitFour),
                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentLimitFour)),"mA"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentKeepOpen),
                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentKeepOpen)),"mA"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentKeepClose),
                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentKeepClose)),"mA"));
        // mcpwm
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPIDPeriod),
                listmap.get(MainActivity.sContext.getString(R.string.parSDPIDPeriod))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPIDDrive),
                listmap.get(MainActivity.sContext.getString(R.string.parSDPIDDrive))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPIDRecall),
                listmap.get(MainActivity.sContext.getString(R.string.parSDPIDRecall))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPIDBreak),
                listmap.get(MainActivity.sContext.getString(R.string.parSDPIDBreak))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPIDReverse),
                listmap.get(MainActivity.sContext.getString(R.string.parSDPIDReverse))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPWMMax),
                listmap.get(MainActivity.sContext.getString(R.string.parSDPWMMax))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPWMMin),
                listmap.get(MainActivity.sContext.getString(R.string.parSDPWMMin))));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDPWMEvenSpeedReduceThreshold),
                listmap.get(MainActivity.sContext.getString(R.string.parSDPWMEvenSpeedReduceThreshold)),"%"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDPWMBreakSpeedIncreaseThreshold),
                listmap.get(MainActivity.sContext.getString(R.string.parSDPWMBreakSpeedIncreaseThreshold)),"%"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDPWMCrawlSpeedReducePWMThreshold),
                listmap.get(MainActivity.sContext.getString(R.string.parSDPWMCrawlSpeedReducePWMThreshold)),"%"));
        return list;
    }

    public HashMap<String, Integer> paraNormalUpdate(HashMap<String, Integer> listmap,String index, int value){
        if(listmap == null){
            listmap = new HashMap<>();
            //other
            listmap.put(MainActivity.sContext.getString(R.string.parSDOpenRate),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDSmoothness),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDKeepOpenTime),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDResistanceRetryTimesMax),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDResistanceRetryInterval),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDReverseBreak),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDKeepModeOpenTime),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDBatteryEnable),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDTempOverUpper),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDTempOverLower),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDTestModeLockFrequency),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDPowerLowMotion),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDLockMotionDelay),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDLockRetryTimes),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDLockRetryInterval),0);
            // speed
            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedOpenMax),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedCloseMax),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedCrawl),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedSlow),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedDropRate),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedLock),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedUpTimeMin),0);
            // length
            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthCrawlOpen),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthEndOpen),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthCrawlClose),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthEndClose),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthHoldUp),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthEndDown),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthSlowOpen),0);
            // current
            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentLimitMax),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentLimitOne),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentLimitTwo),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentLimitThree),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentLimitFour),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentKeepOpen),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentKeepClose),0);
            // mcpwm
            listmap.put(MainActivity.sContext.getString(R.string.parSDPIDPeriod),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDPIDDrive),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDPIDRecall),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDPIDBreak),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDPIDReverse),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDPWMMax),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDPWMMin),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDPWMEvenSpeedReduceThreshold),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDPWMBreakSpeedIncreaseThreshold),0);
            listmap.put(MainActivity.sContext.getString(R.string.parSDPWMCrawlSpeedReducePWMThreshold),0);
        }else {
            if(index != null){
                listmap.put(index,value);
            }
        }
        return listmap;
    }

//
//    //1 开门爬行距离
//    //2 开门末段距离
//    //3 关门爬行距离
//    //4 关门末段距离
//    //5 关门保持上限距离
//    //6 关门保持下限距离
//    //7 开门缓行距离
//    public List<HashMap<String, Object>> listParaLength(HashMap<String, Integer> listmap){
//        List<HashMap<String, Object>> list = new ArrayList<>();
//
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthCrawlOpen),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthCrawlOpen))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthEndOpen),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthEndOpen))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthCrawlClose),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthCrawlClose))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthEndClose),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthEndClose))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthHoldUp),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthHoldUp))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthEndDown),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthEndDown))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDLengthSlowOpen),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDLengthSlowOpen))));
////        list.add(gridUtils.getGridViewDatamore(MainActivity.sContext.getString(R.string.parSDLengthEndCloseLearn),
//////                100,""));
//        return list;
//    }
//
//    //距离参数，7个，采用HashMap保存
//    //1 开门爬行距离
//    //2 开门末段距离
//    //3 关门爬行距离
//    //4 关门末段距离
//    //5 关门保持上限距离
//    //6 关门保持下限距离
//    //7 开门缓行距离
//
//    public HashMap<String, Integer> paraLengthUpdate(HashMap<String, Integer> listmap,String index, int value){
//        if(listmap == null){
//            listmap = new HashMap<>();
//            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthCrawlOpen),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthEndOpen),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthCrawlClose),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthEndClose),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthHoldUp),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthEndDown),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDLengthSlowOpen),0);
//        }else {
//            if(index != null){
//                listmap.put(index,value);
//            }
//        }
//        return listmap;
//    }
//
//    //速度参数
//    public List<HashMap<String, Object>> listParaSpeed(HashMap<String, Integer> listmap){
//        List<HashMap<String, Object>> list = new ArrayList<>();
//
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDSpeedOpenMax),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedOpenMax)),"mm/s"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDSpeedCloseMax),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedCloseMax)),"mm/s"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDSpeedCrawl),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedCrawl)),"mm/s"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDSpeedSlow),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedSlow)),"mm/s"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDSpeedDropRate),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedDropRate)),"%"));
//        //
//        if(listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedLock)) == 0) {
//            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDSpeedLock),
//                    "未锁定"));
//        }else {
//            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDSpeedLock),
//                    "锁定"));
//        }
//
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDSpeedUpTimeMin),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDSpeedUpTimeMin)),"ms"));
//        return list;
//    }
//    //
//    public HashMap<String, Integer> paraSpeedUpdate(HashMap<String, Integer> listmap,String index, int value){
//        if(listmap == null){
//            listmap = new HashMap<>();
//            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedOpenMax),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedCloseMax),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedCrawl),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedSlow),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedDropRate),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedLock),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDSpeedUpTimeMin),0);
//        }else {
//            if(index != null){
//                listmap.put(index,value);
//            }
//        }
//        return listmap;
//    }
//
//    //电流参数
//    public List<HashMap<String, Object>> listParaCurrent(HashMap<String, Integer> listmap){
//        List<HashMap<String, Object>> list = new ArrayList<>();
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentLimitMax),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentLimitMax)),"mA"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentLimitOne),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentLimitOne)),"mA"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentLimitTwo),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentLimitTwo)),"mA"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentLimitThree),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentLimitThree)),"mA"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentLimitFour),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentLimitFour)),"mA"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentKeepOpen),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentKeepOpen)),"mA"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDCurrentKeepClose),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDCurrentKeepClose)),"mA"));
//        return list;
//    }
//
//    public HashMap<String, Integer> paraCurrentUpdate(HashMap<String, Integer> listmap,String index, int value){
//        if(listmap == null){
//            listmap = new HashMap<>();
//            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentLimitMax),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentLimitOne),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentLimitTwo),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentLimitThree),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentLimitFour),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentKeepOpen),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDCurrentKeepClose),0);
//        }else {
//            if(index != null){
//                listmap.put(index,value);
//            }
//        }
//        return listmap;
//    }
//
//    //PWM参数
//    public List<HashMap<String, Object>> listParaMCPWM(HashMap<String, Integer> listmap){
//        List<HashMap<String, Object>> list = new ArrayList<>();
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPIDPeriod),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDPIDPeriod))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPIDDrive),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDPIDDrive))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPIDRecall),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDPIDRecall))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPIDBreak),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDPIDBreak))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPIDReverse),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDPIDReverse))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPWMMax),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDPWMMax))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDPWMMin),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDPWMMin))));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDPWMEvenSpeedReduceThreshold),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDPWMEvenSpeedReduceThreshold)),"%"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDPWMBreakSpeedIncreaseThreshold),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDPWMBreakSpeedIncreaseThreshold)),"%"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDPWMCrawlSpeedReducePWMThreshold),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDPWMCrawlSpeedReducePWMThreshold)),"%"));
//        return list;
//    }
//
//    public HashMap<String, Integer> paraMCPWMUpdate(HashMap<String, Integer> listmap,String index, int value){
//        if(listmap == null){
//            listmap = new HashMap<>();
//            listmap.put(MainActivity.sContext.getString(R.string.parSDPIDPeriod),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDPIDDrive),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDPIDRecall),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDPIDBreak),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDPIDReverse),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDPWMMax),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDPWMMin),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDPWMEvenSpeedReduceThreshold),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDPWMBreakSpeedIncreaseThreshold),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDPWMCrawlSpeedReducePWMThreshold),0);
//        }else {
//            if(index != null){
//                listmap.put(index,value);
//            }
//        }
//        return listmap;
//    }
//
//    //其它参数
//    public List<HashMap<String, Object>> listParaOther(HashMap<String, Integer> listmap){
//        List<HashMap<String, Object>> list = new ArrayList<>();
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDOpenRate),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDOpenRate)),"%"));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.parSDSmoothness),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDSmoothness))));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDKeepOpenTime),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDKeepOpenTime)),"s"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDResistanceRetryTimesMax),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDResistanceRetryTimesMax)),"次"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDResistanceRetryInterval),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDResistanceRetryInterval)),"ms"));
//
//        if(listmap.get(MainActivity.sContext.getString(R.string.parSDReverseBreak)) == 0) {
//            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDReverseBreak),
//                    "禁止"));
//        }else {
//            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDReverseBreak),
//                    "允许"));
//        }
//
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDKeepModeOpenTime),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDKeepModeOpenTime)),"s"));
//
//        if(listmap.get(MainActivity.sContext.getString(R.string.parSDBatteryEnable)) == 0) {
//            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDBatteryEnable),
//                    "禁止"));
//        }else{
//            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDBatteryEnable),
//                    "允许"));
//        }
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDTempOverUpper),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDTempOverUpper)),"℃"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDTempOverLower),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDTempOverLower)),"℃"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDTestModeLockFrequency),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDTestModeLockFrequency)),"%"));
//
//        if(listmap.get(MainActivity.sContext.getString(R.string.parSDPowerLowMotion)) == 0) {
//            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDPowerLowMotion),
//                    "锁门"));
//        }else{
//            list.add(GridUtils.instance().getGridViewString(MainActivity.sContext.getString(R.string.parSDPowerLowMotion),
//                    "开门"));
//        }
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDLockMotionDelay),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDLockMotionDelay)),"ms"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDLockRetryTimes),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDLockRetryTimes)),"次"));
//        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.parSDLockRetryInterval),
//                listmap.get(MainActivity.sContext.getString(R.string.parSDLockRetryInterval)),"ms"));
//        return list;
//    }
//
//    public HashMap<String, Integer> paraOtherUpdate(HashMap<String, Integer> listmap,String index, int value){
//        if(listmap == null){
//            listmap = new HashMap<>();
//            listmap.put(MainActivity.sContext.getString(R.string.parSDOpenRate),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDSmoothness),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDKeepOpenTime),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDResistanceRetryTimesMax),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDResistanceRetryInterval),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDReverseBreak),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDKeepModeOpenTime),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDBatteryEnable),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDTempOverUpper),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDTempOverLower),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDTestModeLockFrequency),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDPowerLowMotion),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDLockMotionDelay),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDLockRetryTimes),0);
//            listmap.put(MainActivity.sContext.getString(R.string.parSDLockRetryInterval),0);
//        }else {
//            if(index != null){
//                listmap.put(index,value);
//            }
//        }
//        return listmap;
//    }
//============================= 结束 ===============================================\



    // 参数查询、设置命令发送
    //旋转门专用
    private void setParaRDOther(int index,String address, String value){
        String tempCommand = null;
        switch (index){
            case 0: //运行速度
                tempCommand = RS485SendCommand.instance().CmdRDSpeedNormal(address,value);
                break;
            case 1://冬季/夏季速度
                tempCommand = RS485SendCommand.instance().CmdRDSpeedSummer(address,value);
                break;
            case 2://怠速速度
                tempCommand = RS485SendCommand.instance().CmdRDSpeedIdling(address,value);
                break;
            case 3://残障速度
                tempCommand = RS485SendCommand.instance().CmdRDDisabledSpeed(address,value);
                break;
            case 4://最大允许运行电流
                tempCommand = RS485SendCommand.instance().CmdRDCurrentLimitMax(address,value);
                break;
            case 5://运行电流学习
                tempCommand = RS485SendCommand.instance().CmdRDCurrentLearn(address,value);
                break;
            case 6://运行阻力门槛
                tempCommand = RS485SendCommand.instance().CmdRDResistanceThreshold(address,value);
                break;
            default:
                break;
        }
        if(tempCommand == null){
            return;
        }
        SerialPortThread.instance().sendMsg(tempCommand);
    }
    private void setParaRDLength(int index,String address, String value){
        String tempCommand = null;
        switch (index){
            case 0: //锁门位置
                tempCommand = RS485SendCommand.instance().CmdRDLockPosition(address,value);
                break;
            case 1://平滑门停机位置
                tempCommand = RS485SendCommand.instance().CmdRDSlidingDoorPosition(address,value);
                break;
            case 2://冬季停机位置
                tempCommand = RS485SendCommand.instance().CmdRDWinterPostion(address,value);
                break;
            case 3://夏季停机位置
                tempCommand = RS485SendCommand.instance().CmdRDSummerPosition(address,value);
                break;
            case 4://危险区域1起点位置
                tempCommand = RS485SendCommand.instance().CmdRDRiskZone1Start(address,value);
                break;
            case 5://危险区域1终点位置1
                tempCommand = RS485SendCommand.instance().CmdRDRiskZone1End1(address,value);
                break;
            case 6://危险区域1终点位置2
                tempCommand = RS485SendCommand.instance().CmdRDRiskZone1End2(address,value);
                break;
            case 7://危险区域2起点位置
                tempCommand = RS485SendCommand.instance().CmdRDRiskZone2Start(address,value);
                break;
            case 8://危险区域2终点位置1
                tempCommand = RS485SendCommand.instance().CmdRDRiskZone2End1(address,value);
                break;
            case 9://危险区域2终点位置2
                tempCommand = RS485SendCommand.instance().CmdRDRiskZone2End2(address,value);
                break;
            default:
                break;
        }
        if(tempCommand == null){
            return;
        }
        SerialPortThread.instance().sendMsg(tempCommand);
    }
    private void setParaRDDriver(int index,String address, String value){
        String tempCommand = null;
        switch (index){
            case 0: //驱动器Can波特率
                tempCommand = RS485SendCommand.instance().CmdRDDriverBaudRate(address,value);
                break;
            case 1://驱动器控制模式
                tempCommand = RS485SendCommand.instance().CmdRDDriverControlMode(address,value);
                break;
            case 2://驱动器加速命令
                tempCommand = RS485SendCommand.instance().CmdRDDriverSpeedUp(address,value);
                break;
            case 3://驱动器减速命令
                tempCommand = RS485SendCommand.instance().CmdRDDriverSpeedDown(address,value);
                break;
            case 4://驱动器急停命令
                tempCommand = RS485SendCommand.instance().CmdRDDriverSpeedBreak(address,value);
                break;
            case 5://驱动器PID速度控制
                tempCommand = RS485SendCommand.instance().CmdRDDriverPIDMainSpeed(address,value);
                break;
            case 6://驱动器PID子速度控制
                tempCommand = RS485SendCommand.instance().CmdRDDriverPIDSubSpeed(address,value);
                break;
            case 7://驱动器PID电流控制
                tempCommand = RS485SendCommand.instance().CmdRDDriverPIDCurrent(address,value);
                break;
            default:
                break;
        }
        if(tempCommand == null){
            return;
        }
        SerialPortThread.instance().sendMsg(tempCommand);
    }

    public void setRDParameter(int pos, String value){
        // 7 + 9 + 8
        if(pos < 7){
            setParaRDOther(pos,MainActivity.sContext.getString(R.string.addRevolvingDoor),value);
        }else if(pos < 16){
            setParaRDLength(pos-7,MainActivity.sContext.getString(R.string.addRevolvingDoor),value);
        }else {
            setParaRDDriver(pos-16,MainActivity.sContext.getString(R.string.addRevolvingDoor),value);
        }
    }

    //其它门用
    // 速度命令
    private void setParaSpeed(int index,String address, String value){
        String tempCommand = null;
        switch (index){
            case 0: //开门最大速度
                tempCommand = RS485SendCommand.instance().CmdPCOpenSpeedMax(address,value);
                break;
            case 1://关门最大速度
                tempCommand = RS485SendCommand.instance().CmdPCCloseSpeedMax(address,value);
                break;
            case 2://缓行速度
                tempCommand = RS485SendCommand.instance().CmdSPSpeedCrawl(address,value);
                break;
            case 3://速度过低门槛
                tempCommand = RS485SendCommand.instance().CmdSPSpeedMin(address,value);
                break;
            case 4://速度跌落比
                tempCommand = RS485SendCommand.instance().CmdSPSpeedDropRate(address,value);
                break;
            case 5://速度锁定
                tempCommand = RS485SendCommand.instance().CmdSPSpeedLock(address,value);
                break;
            case 6://最小加速时间
                tempCommand = RS485SendCommand.instance().CmdSPSpeedUpTimeMin(address,value);
                break;
            default:
                break;
        }
        if(tempCommand == null){
            return;
        }
        SerialPortThread.instance().sendMsg(tempCommand);
    }
    // 位置命令
    private void setParaLength(int index,String address, String value){
        String tempCommand = null;
        switch (index){
            case 0: //开门爬行距离
                tempCommand = RS485SendCommand.instance().CmdSPCrawlOpenLength(address,value);
                break;
            case 1://开门末段距离
                tempCommand = RS485SendCommand.instance().CmdSPSlowOpenLength(address,value);
                break;
            case 2://关门爬行距离
                tempCommand = RS485SendCommand.instance().CmdSPCrawlCloseLength(address,value);
                break;
            case 3://关门末段距离
                tempCommand = RS485SendCommand.instance().CmdSPSlowCloseLength(address,value);
                break;
            case 4://关门保持上限
                tempCommand = RS485SendCommand.instance().CmdSPHoldCloseUpper(address,value);
                break;
            case 5://关门保持下限
                tempCommand = RS485SendCommand.instance().CmdSPHoldCloseLower(address,value);
                break;
            case 6://开门缓行距离
                tempCommand = RS485SendCommand.instance().CmdSPForceSlowOpenLength(address,value);
                break;
            default:
                break;
        }
        if(tempCommand == null){
            return;
        }
        SerialPortThread.instance().sendMsg(tempCommand);
    }

    // PWM命令
    private void setParaMCPWM(int index,String address, String value){
        String tempCommand = null;
        switch (index){
            case 0: //PID调节周期
                tempCommand = RS485SendCommand.instance().CmdSPPIDPeriod(address,value);
                break;
            case 1://PID驱动系数
                tempCommand = RS485SendCommand.instance().CmdSPPIDDrive(address,value);
                break;
            case 2://PID撤消系数
                tempCommand = RS485SendCommand.instance().CmdSPPIDRecall(address,value);
                break;
            case 3://PID制动系数
                tempCommand = RS485SendCommand.instance().CmdSPPIDBreak(address,value);
                break;
            case 4://PID反向系数
                tempCommand = RS485SendCommand.instance().CmdSPPIDReverse(address,value);
                break;
            case 5://PWM最大值
                tempCommand = RS485SendCommand.instance().CmdSPPWMMax(address,value);
                break;
            case 6://PWM最小值
                tempCommand = RS485SendCommand.instance().CmdSPPWMMin(address,value);
                break;
            case 7://匀速不减PWM门槛
                tempCommand = RS485SendCommand.instance().CmdSPSpeedEvenPWMThreshold(address,value);
                break;
            case 8://刹车不增PWM门槛
                tempCommand = RS485SendCommand.instance().CmdSPSpeedBreakPWMThreshold(address,value);
                break;
            case 9://爬行、末段不减PWM门槛
                tempCommand = RS485SendCommand.instance().CmdSPSpeedCrawlPWMThreshold(address,value);
                break;
            default:
                break;
        }
        if(tempCommand == null){
            return;
        }
        SerialPortThread.instance().sendMsg(tempCommand);
    }

    // 电流命令
    private void setParaCurrent(int index,String address, String value){
        String tempCommand = null;
        switch (index){
            case 0: //最大电流限流值
                tempCommand = RS485SendCommand.instance().CmdSPLimitCurrentMax(address,value);
                break;
            case 1://电流限流一档
                tempCommand = RS485SendCommand.instance().CmdSPCurrentLevel1(address,value);
                break;
            case 2://电流限流二档
                tempCommand = RS485SendCommand.instance().CmdSPCurrentLevel2(address,value);
                break;
            case 3://电流限流三档
                tempCommand = RS485SendCommand.instance().CmdSPCurrentLevel3(address,value);
                break;
            case 4://电流限流四档
                tempCommand = RS485SendCommand.instance().CmdSPCurrentLevel4(address,value);
                break;
            case 5://开门保持电流
                tempCommand = RS485SendCommand.instance().CmdSPCurrentHoldOpen(address,value);
                break;
            case 6://关门保持电流
                tempCommand = RS485SendCommand.instance().CmdSPCurrentHoldClose(address,value);
                break;
            default:
                break;
        }
        if(tempCommand == null){
            return;
        }
        SerialPortThread.instance().sendMsg(tempCommand);
    }

    // 其它命令
    private void setParaOther(int index,String address, String value){
        String tempCommand = null;
        switch (index){
            case 0: //门扇开度
                tempCommand = RS485SendCommand.instance().CmdSPDoorOpenRate(address,value);
                break;
            case 1://运行平稳度
                tempCommand = RS485SendCommand.instance().CmdSPDoorSmoothness(address,value);
                break;
            case 2://开门保持时间
                tempCommand = RS485SendCommand.instance().CmdPCHoldOpenTime(address,value);
                break;
            case 3://最大遇阻重试次数
                tempCommand = RS485SendCommand.instance().CmdSPResistanceRetryNB(address,value);
                break;
            case 4://遇阻重试间隔
                tempCommand = RS485SendCommand.instance().CmdSPResistanceRetryPeriod(address,value);
                break;
            case 5://反向刹车标志
                tempCommand = RS485SendCommand.instance().CmdSPReverseBreak(address,value);
                break;
            case 6://常开模式保持时间
                tempCommand = RS485SendCommand.instance().CmdSPOpenModeHoldTime(address,value);
                break;
            case 7://备用电源
                tempCommand = RS485SendCommand.instance().CmdSPBatteryEnable(address,value);
                break;
            case 8://超温上限温度
                tempCommand = RS485SendCommand.instance().CmdSPTempOverUpper(address,value);
                break;
            case 9://超温下限温度
                tempCommand = RS485SendCommand.instance().CmdSPTempOverLower(address,value);
                break;
            case 10://测试模式锁动作频率
                tempCommand = RS485SendCommand.instance().CmdSPTestModeLockFrequency(address,value);
                break;
            case 11://系统失电动作
                tempCommand = RS485SendCommand.instance().CmdPCPowerLowState(address,value);
                break;
            case 12://锁动作延迟时间
                tempCommand = RS485SendCommand.instance().CmdSPLockDelay(address,value);
                break;
            case 13://锁动作失败重试次数
                tempCommand = RS485SendCommand.instance().CmdSPLockRetryNB(address,value);
                break;
            case 14://锁动作失败重试间隔
                tempCommand = RS485SendCommand.instance().CmdSPLockRetryPeriod(address,value);
                break;
            default:
                break;
        }
        if(tempCommand == null){
            return;
        }
        SerialPortThread.instance().sendMsg(tempCommand);
    }


    public void setParameter(int pos, String address, String value){
        // pos 0~14 , other
        // pos 15~21 , speed
        // pos 22~28 , length
        // pos 29~35 , current
        // pos 36~45 , mcpwm
        if(pos < 15){
            setParaOther(pos, address,value);
        }else if(pos < 22){
            setParaSpeed(pos-15,address,value);
        }else if(pos < 29){
            setParaLength(pos-22,address,value);
        }else if(pos < 36){
            setParaCurrent(pos-29,address,value);
        }else {
            setParaMCPWM(pos-36,address,value);
        }
    }


    // 仅可读信息
    // 可读信息
    public List<HashMap<String, Object>> listInfoRD(HashMap<String, Integer> listmap){
        List<HashMap<String, Object>> list = new ArrayList<>();
        // other

        return list;
    }
    public HashMap<String, Integer> infoRDUpdate(HashMap<String, Integer> listmap,String index, int value){
        if(listmap == null){
            listmap = new HashMap<>();
            // other


        }else {
            if(index != null){
                listmap.put(index,value);
            }
        }
        return listmap;
    }

    public List<HashMap<String, Object>> listInfoNormal(HashMap<String, Integer> listmap){
        List<HashMap<String, Object>> list = new ArrayList<>();

        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.infoSPSoftVersion),
                listmap.get(MainActivity.sContext.getString(R.string.infoSPSoftVersion))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.infoSPTripStart),
                listmap.get(MainActivity.sContext.getString(R.string.infoSPTripStart))));
        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.infoSPTripEnd),
                listmap.get(MainActivity.sContext.getString(R.string.infoSPTripEnd))));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.infoSPRunCycleThisEpoch),
                listmap.get(MainActivity.sContext.getString(R.string.infoSPRunCycleThisEpoch)),"次"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.infoSPRunCycleAllEpoch),
                listmap.get(MainActivity.sContext.getString(R.string.infoSPRunCycleAllEpoch)),"次"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.infoSPActualCurrentMax),
                listmap.get(MainActivity.sContext.getString(R.string.infoSPActualCurrentMax)),"mA"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.infoSPActualTemperatureMax),
                listmap.get(MainActivity.sContext.getString(R.string.infoSPActualTemperatureMax)),"℃"));
        list.add(GridUtils.instance().getGridViewValueAndUnit(MainActivity.sContext.getString(R.string.infoSPTemperature),
                listmap.get(MainActivity.sContext.getString(R.string.infoSPTemperature)), "℃"));

//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.infoPCLastErrorCode),
//                listmap.get(MainActivity.sContext.getString(R.string.infoPCLastErrorCode))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.infoPCLastTenErrorCode),
//                listmap.get(MainActivity.sContext.getString(R.string.infoPCLastTenErrorCode))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.infoSPLastRestartMsg),
//                listmap.get(MainActivity.sContext.getString(R.string.infoSPLastRestartMsg))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.infoSPFatalErrorMsg),
//                listmap.get(MainActivity.sContext.getString(R.string.infoSPFatalErrorMsg))));
        return list;
    }

    public HashMap<String, Integer> paraInfoUpdate(HashMap<String, Integer> listmap,String index, int value){
        if(listmap == null){
            listmap = new HashMap<>();
            listmap.put(MainActivity.sContext.getString(R.string.infoSPSoftVersion),0);
            listmap.put(MainActivity.sContext.getString(R.string.infoSPTripStart),0);
            listmap.put(MainActivity.sContext.getString(R.string.infoSPTripEnd),0);
            listmap.put(MainActivity.sContext.getString(R.string.infoSPRunCycleThisEpoch),0);
            listmap.put(MainActivity.sContext.getString(R.string.infoSPRunCycleAllEpoch),0);
            listmap.put(MainActivity.sContext.getString(R.string.infoSPActualCurrentMax),0);
            listmap.put(MainActivity.sContext.getString(R.string.infoSPActualTemperatureMax),0);
            listmap.put(MainActivity.sContext.getString(R.string.infoSPTemperature),0);
//            listmap.put(MainActivity.sContext.getString(R.string.infoPCLastErrorCode),0);
//            listmap.put(MainActivity.sContext.getString(R.string.infoPCLastTenErrorCode),"00000000000000000000");
//            listmap.put(MainActivity.sContext.getString(R.string.infoSPLastRestartMsg),"00000000000000000000");
//            listmap.put(MainActivity.sContext.getString(R.string.infoSPFatalErrorMsg),"00000000000000000000");
        }else {
            if(index != null){
                listmap.put(index,value);
            }
        }
        return listmap;
    }



    public List<HashMap<String, Object>> listErrorNormal(HashMap<String, String> listmap){
        List<HashMap<String, Object>> list = new ArrayList<>();

//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.infoPCLastErrorCode),
//                listmap.get(MainActivity.sContext.getString(R.string.infoPCLastErrorCode))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.infoPCLastTenErrorCode),
//                listmap.get(MainActivity.sContext.getString(R.string.infoPCLastTenErrorCode))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.infoSPLastRestartMsg),
//                listmap.get(MainActivity.sContext.getString(R.string.infoSPLastRestartMsg))));
//        list.add(GridUtils.instance().getGridViewValue(MainActivity.sContext.getString(R.string.infoSPFatalErrorMsg),
//                listmap.get(MainActivity.sContext.getString(R.string.infoSPFatalErrorMsg))));
        return list;
    }

    public HashMap<String, String> paraErrorUpdate(HashMap<String, String> listmap,String index, String value){
        if(listmap == null){
            listmap = new HashMap<>();
//            listmap.put(MainActivity.sContext.getString(R.string.infoPCLastErrorCode),0);

            listmap.put(MainActivity.sContext.getString(R.string.infoPCLastTenErrorCode),"00010203040506070809");
            listmap.put(MainActivity.sContext.getString(R.string.infoSPLastRestartMsg),"0A0B0C0D0E0F10111213");
            listmap.put(MainActivity.sContext.getString(R.string.infoSPFatalErrorMsg),"1415161718191A200000");
        }else {
            if((index != null) && (value != "")){
                listmap.put(index,value);
            }
        }
        return listmap;
    }





    public void readInfoOther(int index,String address){
        String tempCommand = null;
        switch (index){
            case 0: //软件版本号
                tempCommand = RS485SendCommand.instance().CmdSPSoftVersion(address);
                break;
            case 1://本次累计运行次数
                tempCommand = RS485SendCommand.instance().CmdSPRunCycleThisEpoch(address);
                break;
            case 2://历史累计运行次数
                tempCommand = RS485SendCommand.instance().CmdSPRunCycleAllEpoch(address);
                break;
            case 3://学习到的行程起点
                tempCommand = RS485SendCommand.instance().CmdSPTripStart(address);
                break;
            case 4://学习到的行程终点
                tempCommand = RS485SendCommand.instance().CmdSPTripEnd(address);
                break;
            case 5://本次曾经使用的电流最大值
                tempCommand = RS485SendCommand.instance().CmdSPActualCurrentMax(address);
                break;
            case 6://本次曾经使用的温度最大值
                tempCommand = RS485SendCommand.instance().CmdSPActualTemperatureMax(address);
                break;
            case 7://系统当前温度
                tempCommand = RS485SendCommand.instance().CmdSPTemperature(address);
                break;
            case 8://当前报警代码
                tempCommand = RS485SendCommand.instance().CmdPCLastErrorCode(address);
                break;
            case 9://最近10次报警代码
                tempCommand = RS485SendCommand.instance().CmdPCLastTenErrorCode(address);
                break;
            case 10://最近10系统复位原因
                tempCommand = RS485SendCommand.instance().CmdSPLastRestartMsg(address);
                break;
            case 11://严重错误信息
                tempCommand = RS485SendCommand.instance().CmdSPFatalErrorMsg(address);
                break;
            default:
                break;
        }
        if(tempCommand == null){
            return;
        }
        SerialPortThread.instance().sendMsg(tempCommand);

    }


}
