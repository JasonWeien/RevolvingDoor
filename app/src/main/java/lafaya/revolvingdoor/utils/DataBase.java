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
    public HashMap<String, String> mapInfoRevolving;

    public HashMap<String, String> mapErrorRevolving;

    // 通用参数：平滑门、门翼左、门翼右
    public HashMap<String, Integer> mapNormalSliding;
    public HashMap<String, Integer> mapNormalWingL;
    public HashMap<String, Integer> mapNormalWingR;

    public HashMap<String, String> mapErrorSliding;
    public HashMap<String, String> mapErrorWingL;
    public HashMap<String, String> mapErrorWingR;

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


    public String errorCodeDescribe(String code){
        String describe = "";
        if(code.equals("00")){
            describe = MainActivity.sContext.getString(R.string.errorCode00Describe);
        }else if(code.equals("01")){
            describe = MainActivity.sContext.getString(R.string.errorCode01Describe);
        }else if(code.equals("02")){
            describe = MainActivity.sContext.getString(R.string.errorCode02Describe);
        }else if(code.equals("03")){
            describe = MainActivity.sContext.getString(R.string.errorCode03Describe);
        }else if(code.equals("04")){
            describe = MainActivity.sContext.getString(R.string.errorCode04Describe);
        }else if(code.equals("05")){
            describe = MainActivity.sContext.getString(R.string.errorCode05Describe);
        }else if(code.equals("06")){
            describe = MainActivity.sContext.getString(R.string.errorCode06Describe);
        }else if(code.equals("07")){
            describe = MainActivity.sContext.getString(R.string.errorCode07Describe);
        }else if(code.equals("08")){
            describe = MainActivity.sContext.getString(R.string.errorCode08Describe);
        }else if(code.equals("09")){
            describe = MainActivity.sContext.getString(R.string.errorCode09Describe);
        }else if(code.equals("0A")){
            describe = MainActivity.sContext.getString(R.string.errorCode0ADescribe);
        }else if(code.equals("0B")){
            describe = MainActivity.sContext.getString(R.string.errorCode0BDescribe);
        }else if(code.equals("0C")){
            describe = MainActivity.sContext.getString(R.string.errorCode0CDescribe);
        }else if(code.equals("0D")){
            describe = MainActivity.sContext.getString(R.string.errorCode0DDescribe);
        }else if(code.equals("0E")){
            describe = MainActivity.sContext.getString(R.string.errorCode0EDescribe);
        }else if(code.equals("0F")){
            describe = MainActivity.sContext.getString(R.string.errorCode0FDescribe);
        }else if(code.equals("10")){
            describe = MainActivity.sContext.getString(R.string.errorCode10Describe);
        }else if(code.equals("11")){
            describe = MainActivity.sContext.getString(R.string.errorCode11Describe);
        }else if(code.equals("12")){
            describe = MainActivity.sContext.getString(R.string.errorCode12Describe);
        }else if(code.equals("13")){
            describe = MainActivity.sContext.getString(R.string.errorCode13Describe);
        }else if(code.equals("14")){
            describe = MainActivity.sContext.getString(R.string.errorCode14Describe);
        }else if(code.equals("15")){
            describe = MainActivity.sContext.getString(R.string.errorCode15Describe);
        }else if(code.equals("16")){
            describe = MainActivity.sContext.getString(R.string.errorCode16Describe);
        }else if(code.equals("17")){
            describe = MainActivity.sContext.getString(R.string.errorCode17Describe);
        }else if(code.equals("18")){
            describe = MainActivity.sContext.getString(R.string.errorCode18Describe);
        }else if(code.equals("19")){
            describe = MainActivity.sContext.getString(R.string.errorCode19Describe);
        }else if(code.equals("1A")){
            describe = MainActivity.sContext.getString(R.string.errorCode1ADescribe);
        }else if(code.equals("20")){
            describe = MainActivity.sContext.getString(R.string.errorCode20Describe);
        }



        return describe;
    }

}
