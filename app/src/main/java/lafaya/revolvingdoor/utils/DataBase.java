package lafaya.revolvingdoor.utils;


import java.util.HashMap;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.view.MainActivity;

public class DataBase {

    public String mErrorCode = MainActivity.sContext.getString(R.string.RDErrorCode00);
//    public String mErrorCodeList = MainActivity.sContext.getString(R.string.RDModeNormal);

    public String mRunningMode = MainActivity.sContext.getString(R.string.RDModeNormal);


    //参数值
    //旋转门专用参数：距离、驱动、其它
    public HashMap<String, Integer> mapParaRevolving;
    public HashMap<String, Integer> mapInfoRevolving;

    // 通用参数：平滑门、门翼左、门翼右
    public HashMap<String, Integer> mapNormalSliding;
    public HashMap<String, Integer> mapNormalWingL;
    public HashMap<String, Integer> mapNormalWingR;

    public HashMap<String, Integer> mapInfoSliding;
    public HashMap<String, Integer> mapInfoWingL;
    public HashMap<String, Integer> mapInfoWingR;


    // linelayout 只显示一种。
    public enum DoorType{NON,
        REVOLVING,
        SLIDING,
        WINGL,
        WINGR;}

//    public HashMap<String, Integer> mapLengthSliding;
//    public HashMap<String, Integer> mapLengthWingL;
//    public HashMap<String, Integer> mapLengthWingR;
//
//    //速度参数：平滑门、门翼左、门翼右
//    public HashMap<String, Integer> mapSpeedSliding;
//    public HashMap<String, Integer> mapSpeedWingL;
//    public HashMap<String, Integer> mapSpeedWingR;
//
//    //电流参数：平滑门、门翼左、门翼右
//    public HashMap<String, Integer> mapCurrentSliding;
//    public HashMap<String, Integer> mapCurrentWingL;
//    public HashMap<String, Integer> mapCurrentWingR;
//
//    //PWM参数：平滑门、门翼左、门翼右
//    public HashMap<String, Integer> mapMCPWMSliding;
//    public HashMap<String, Integer> mapMCPWMWingL;
//    public HashMap<String, Integer> mapMCPWMWingR;
//
//    //其它参数：平滑门、门翼左、门翼右
//    public HashMap<String, Integer> mapOtherSliding;
//    public HashMap<String, Integer> mapOtherWingL;
//    public HashMap<String, Integer> mapOtherWingR;

    //类接口函数定义
    private static class InstanceHolder {
        private static DataBase sManager = new DataBase();
    }
    public static DataBase instance(){
        return DataBase.InstanceHolder.sManager;
    }

}
