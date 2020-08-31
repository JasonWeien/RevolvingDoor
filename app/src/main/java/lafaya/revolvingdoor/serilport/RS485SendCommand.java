package lafaya.revolvingdoor.serilport;

import android.content.Context;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.view.MainActivity;

public class RS485SendCommand {
    private Context mContext = MainActivity.sContext;

    //类接口函数定义
    private static class InstanceHolder {
        private static RS485SendCommand sManager = new RS485SendCommand();
    }
    public static RS485SendCommand instance(){
        return RS485SendCommand.InstanceHolder.sManager;
    }

    /*===========================================================================================*/
    /* 生产协议校验码
     * 校验码生成：两两参数进行异或（去除握手字和结束字）
     * @ param message 输入命令字符串
     * @ return 校验码
     *
     * */
    private String CreatCheckCode(String command){
        if(command.length() % 2 != 0){
            return null;
        }
        char checkCode = 0x00; //校验码
        // 高、低位直接求异或，生成校验码
        for(int i = 0; i < command.length(); i+=2){
            checkCode ^= (char)(Integer.valueOf(command.substring(i,i+1),16) * 16
                    + Integer.valueOf(command.substring(i+1,i+2),16));
        }
        //注：字母转换为大写形式。。。
        return Integer.toHexString(checkCode & 0x00FF).toUpperCase();
    }


    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
     *                               命令字 - 发送
     * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
    //初始化查询命令，发送：0x80
    public String CmdQueryInit(String address){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPInit);
        //字符串“~”对应的十六进制为7E，“\r”对应十六进制为0D。
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //开门最高速度查询命令，发送：0x81
    public String CmdQueryOpenSpeedMax(String address){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPOpenSpeedMax);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //关门最高速度查询命令，发送：0x82
    public String CmdQueryCloseSpeedMax(String address){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPCloseSpeedMax);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //开门保持时间查询命令，发送：0x85
    public String CmdQueryDoorHoldTime(String address){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPDoorHoldTime);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //历史报警代码查询命令，发送：0x86
    public String CmdQueryHistoryErroCode(String address){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPHistoryErroCode);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //---------------------------------------------------------------------------------------------
    //软件版本
    public String CmdSPSoftVersion(String address){
        String command;
        // 查询命令，查询软件版本号
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPSoftVersion);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //行程起点
    public String CmdSPTripStart(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPTripStart);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //行程终点
    public String CmdSPTripEnd(String address){
        String command;
        // 查询命令，查询行程终点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPTripEnd);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //行程半开点
    public String CmdSPTripHalf(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPTripHalf);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //本次累计开关门总次数
    public String CmdSPRunCycleThisEpoch(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPRunCycleThisEpoch);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //历史累计开关门总次数
    public String CmdSPRunCycleAllEpoch(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPRunCycleAllEpoch);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //最近一次开门刹车距离计算值
    public String CmdSPTheoryOpenBreakDistance(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPTheoryOpenBreakDistance);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //最近一次关门刹车距离计算值
    public String CmdSPTheoryCloseBreakDistance(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPTheoryCloseBreakDistance);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //最近一次加速速度最大值计算值
    public String CmdSPTheorySpeedUpDistance(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPTheorySpeedUpDistance);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //最近一次实际加速距离
    public String CmdSPActualSpeedUpDistance(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPActualSpeedUpDistance);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //最近一次实际刹车距离
    public String CmdSPActualBreakDistance(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPActualBreakDistance);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //历史电流积分值
    public String CmdSPCurrentIntegral(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPCurrentIntegral);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //本次曾经使用的电流最大值
    public String CmdSPActualCurrentMax(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPActualCurrentMax);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //本次曾经使用的温度最大值
    public String CmdSPActualTemperatureMax(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPActualTemperatureMax);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //当前系统温度
    public String CmdSPTemperature(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPTemperature);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //当前报警代码
    public String CmdPCLastErrorCode(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCLastErrorCode);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //系统最近10次复位原因
    public String CmdSPLastRestartMsg(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPLastRestartMsg);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //严重错误信息
    public String CmdSPFatalErrorMsg(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPFatalErrorMsg);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //最近10次报警代码
    public String CmdPCLastTenErrorCode(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCLastTenErrorCode);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }


    //关门命令
    public String CmdPCDoorClose(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCDoorClose);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //开门命令
    public String CmdPCDoorOpen(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCDoorOpen);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //重启命令C
    public String CmdPCRestart(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCRestart);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //恢复出厂设置
    public String CmdPCRestore(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCRestore);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //测试模式
    public String CmdPCTestMode(String address, String setPara){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCTestMode);
        command += setPara;
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //实时运行数据采样
    public String CmdPCRunningDataSampling(String address, String setPara){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCRunningDataSampling);
        command += setPara;
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //硬件测试
    public String cmdPCHardwareMonitor(String address, String setPara){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCHardwareMonitor);
        command += setPara;
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //门体运行监控
    public String cmdPCRunningMonitor(String address, String setPara){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCRunningMonitor);
        command += setPara;
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //门类型
    public String CmdPCDoorType(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCDoorType);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //电机类型
    public String CmdPCMotorType(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCMotorType);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //锁反馈是否存在
    public String CmdPCLockFeedback(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCLockFeedback);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //模式开关类型
    public String CmdPCSwitchType(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCSwitchType);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //电流选择档位
    public String CmdPCCurrentLevel(String address){
        String command;
        // 查询命令，查询行程起点
        command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCCurrentLevel);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }




    //===============PWM 发送命令
    //PID调节周期
    public String CmdSPPIDPeriod(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPPIDPeriod);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");

    }
    //PID驱动系数
    public String CmdSPPIDDrive(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPPIDDrive);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");

    }
    //PID撤消系数
    public String CmdSPPIDRecall(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPPIDRecall);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");

    }
    //PID制动系数
    public String CmdSPPIDBreak(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPPIDBreak);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");

    }
    //PID反向系数
    public String CmdSPPIDReverse(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPPIDReverse);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");

    }
    //PWM最大值
    public String CmdSPPWMMax(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPPWMMax);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");

    }
    //PWM最小值
    public String CmdSPPWMMin(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPPWMMin);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //匀速不减PWM门槛
    public String CmdSPSpeedEvenPWMThreshold(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPSpeedEvenPWMThreshold);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //刹车不增PWM门槛
    public String CmdSPSpeedBreakPWMThreshold(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPSpeedBreakPWMThreshold);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //爬行、末段不减PWM门槛
    public String CmdSPSpeedCrawlPWMThreshold(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPSpeedCrawlPWMThreshold);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //===============PWM 发送命令 End

    //===============电流 发送命令
    //最大电流限流值
    public String CmdSPLimitCurrentMax(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPLimitCurrentMax);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //电流限流一档
    public String CmdSPCurrentLevel1(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPCurrentLevel1);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //电流限流二档
    public String CmdSPCurrentLevel2(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPCurrentLevel2);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //电流限流三档
    public String CmdSPCurrentLevel3(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPCurrentLevel3);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //电流限流四档
    public String CmdSPCurrentLevel4(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPCurrentLevel4);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //开门保持电流
    public String CmdSPCurrentHoldOpen(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPCurrentHoldOpen);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //关门保持电流
    public String CmdSPCurrentHoldClose(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPCurrentHoldClose);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //===============电流 发送命令 End

    //===============速度 发送命令
    //缓行速度
    public String CmdSPSpeedCrawl(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPSpeedCrawl);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //速度过低门槛
    public String CmdSPSpeedMin(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPSpeedMin);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //开门最大速度
    public String CmdPCOpenSpeedMax(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCOpenSpeedMax);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //关门最大速度
    public String CmdPCCloseSpeedMax(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCCloseSpeedMax);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //速度跌落比
    public String CmdSPSpeedDropRate(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPSpeedDropRate);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //速度锁定
    public String CmdSPSpeedLock(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPSpeedLock);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //最小加速时间
    public String CmdSPSpeedUpTimeMin(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPSpeedUpTimeMin);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //===============速度 发送命令 End
    //===============距离 发送命令
    //开门爬行距离
    public String CmdSPCrawlOpenLength(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPCrawlOpenLength);
        if(!setPara.equals("") ){
            //设置命令
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //开门末段距离
    public String CmdSPSlowOpenLength(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPSlowOpenLength);
        // 查询命令
        if(!setPara.equals("") ) {
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //关门爬行距离
    public String CmdSPCrawlCloseLength(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPCrawlCloseLength);
        // 查询命令
        if(!setPara.equals("") ) {
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //关门末段距离
    public String CmdSPSlowCloseLength(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPSlowCloseLength);
        // 查询命令
        if(!setPara.equals("") ) {
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //关门保持上限
    public String CmdSPHoldCloseUpper(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPHoldCloseUpper);
        // 查询命令
        if(!setPara.equals("") ) {
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //关门保持下限
    public String CmdSPHoldCloseLower(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPHoldCloseLower);
        // 查询命令
        if(!setPara.equals("") ) {
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //开门缓行距离
    public String CmdSPForceSlowOpenLength(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPForceSlowOpenLength);
        // 查询命令
        if(!setPara.equals("") ) {
            if(setPara.length() <4){
                setPara = "00" + setPara;
            }
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //===============电流 发送命令 End
    //===============其它 发送命令
    //锁动作延迟时间
    public String CmdSPLockDelay(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPLockDelay);
        // 查询命令
        if(!setPara.equals("") ){
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //锁动作失败重试次数
    public String CmdSPLockRetryNB(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPLockRetryNB);
        // 查询命令
        if(!setPara.equals("") ){
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //锁动作失败重试间隔
    public String CmdSPLockRetryPeriod(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPLockRetryPeriod);
        // 查询命令
        if(!setPara.equals("") ){
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //门扇开度
    public String CmdSPDoorOpenRate(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPDoorOpenRate);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //运行平稳度
    public String CmdSPDoorSmoothness(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPDoorSmoothness);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //开门保持时间
    public String CmdPCHoldOpenTime(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCHoldOpenTime);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //最大遇阻重试次数
    public String CmdSPResistanceRetryNB(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPResistanceRetryNB);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //遇阻重试间隔
    public String CmdSPResistanceRetryPeriod(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPResistanceRetryPeriod);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //反向刹车标志
    public String CmdSPReverseBreak(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPReverseBreak);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //常开模式保持时间
    public String CmdSPOpenModeHoldTime(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPOpenModeHoldTime);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //备用电源使能
    public String CmdSPBatteryEnable(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPBatteryEnable);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //超温上限温度
    public String CmdSPTempOverUpper(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPTempOverUpper);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;

        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //超温下限温度
    public String CmdSPTempOverLower(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPTempOverLower);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;

        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    //测试模式锁动作频率
    public String CmdSPTestModeLockFrequency(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPTestModeLockFrequency);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;

        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //系统失电动作
    public String CmdPCPowerLowState(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdPCPowerLowState);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;

        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //软件注册状态
    public String CmdSPLicenseState(String address, String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdSPLicenseState);
        // 查询命令
        if(!setPara.equals("") ){
            //设置命令
            command += setPara;

        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }


    //===============其它 发送命令 End
    //---------------------------------------------------------------------------------------------


    //---------------------------------------------------------------------------------------------
//    //旋转自动门运行模式查询，发送：0x8B00
//    public String CmdRevolvingInit(String address){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDInit);
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }

//    R/W，可读可写参数

    //旋转自动门运行模式查询，发送：0x8B01
    public String CmdRevolvingDoorMode(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDMode);
        // 查询命令，不含运行模式
        if(!setPara.equals("")) {
        //设置命令，提供需设置的运行模式
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门学习状态查询/设置，发送：0x8B02  ：
    public String CmdRevolvingLearningState(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDLearningState);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门危险区域宽度值 ，发送：0x8B03  ：
    public String CmdRevolvingRiskWidth(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDRiskWidth);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门危险区域起点位置 ，发送：0x8B04  ：
    public String CmdRevolvingRiskOrigin(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDRiskOrigin);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门夜间锁门位置 ，发送：0x8B05  ：
    public String CmdRevolvingPositionLock(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDPositionLock);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门冬季停机位置 ，发送：0x8B06  ：
    public String CmdRevolvingPositionStopWinter(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDPositionStopWinter);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门夏季停机位置 ，发送：0x8B07  ：
    public String CmdRevolvingPositionStopSummer(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDPositionStopSummer);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门平滑门停机位置 ，发送：0x8B08  ：
    public String CmdRevolvingPositionStopSliding(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDPositionStopSliding);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门怠速模式运转角度 ，发送：0x8B09  ：
    public String CmdRevolvingAngleIdling(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDAngleIdling);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门冬季模式运转角度 ，发送：0x8B0A  ：
    public String CmdRevolvingAngleWinter(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDAngleWinter);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门夏季模式运转角度 ，发送：0x8B0B  ：
    public String CmdRevolvingAngleSummer(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDAngleSummer);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门残障模式运转角度 ，发送：0x8B0C  ：
    public String CmdRevolvingAngleDisability(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDAngleDisability);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门高速运行速度 ，发送：0x8B0D  ：
    public String CmdRevolvingSpeedHigh(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDSpeedHigh);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门中速运行速度 ，发送：0x8B0E  ：
    public String CmdRevolvingSpeedMid(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDSpeedMid);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }

    //旋转自动门低速运行速度 ，发送：0x8B0F  ：
    public String CmdRevolvingSpeedLow(String address,String setPara){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDSpeedLow);
        // 查询命令，不含参数
        if(!setPara.equals("")) {
            //设置命令，提供需设置的参数
            command += setPara;
        }
        return("~" + command + CreatCheckCode(command)+ "\r");
    }


//    R:可读参数
    public String CmdRevolvingSoftVersion(String address){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDSoftVersion);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    public String CmdRevolvingTotalTrip(String address){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDTotalTrip);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    public String CmdRevolvingPortState(String address){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDStatePort);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    public String CmdRevolvingRunningState(String address){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDStateRunning);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }
    public String CmdRevolvingSystemState(String address){
        String command = mContext.getString(R.string.addProgramSwitch) +
                address + mContext.getString(R.string.cmdRDStateSystem);
        return("~" + command + CreatCheckCode(command)+ "\r");
    }



//    //运行速度
//    public String CmdRDSpeedNormal(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDSpeedNormal);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //冬季/夏季速度
//    public String CmdRDSpeedSummer(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDSpeedSummer);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //怠速速度
//    public String CmdRDSpeedIdling(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDSpeedIdling);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//
//    //残障速度
//    public String CmdRDDisabledSpeed(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDDisabledSpeed);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //最大允许运行电流
//    public String CmdRDCurrentLimitMax(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDCurrentLimitMax);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //运行电流学习
//    public String CmdRDCurrentLearn(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDCurrentLearn);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //运行阻力门槛
//    public String CmdRDResistanceThreshold(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDResistanceThreshold);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //=====
//    //锁门位置
//    public String CmdRDLockPosition(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDLockPosition);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //平滑门停机位置
//    public String CmdRDSlidingDoorPosition(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDSlidingDoorPosition);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //冬季停机位置
//    public String CmdRDWinterPostion(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDWinterPostion);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //夏季停机位置
//    public String CmdRDSummerPosition(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDSummerPosition);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //危险区域1起点位置
//    public String CmdRDRiskZone1Start(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDRiskZone1Start);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //危险区域1终点位置1
//    public String CmdRDRiskZone1End1(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDRiskZone1End1);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //危险区域1终点位置2
//    public String CmdRDRiskZone1End2(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDRiskZone1End2);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //危险区域2起点位置
//    public String CmdRDRiskZone2Start(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDRiskZone2Start);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //危险区域2终点位置1
//    public String CmdRDRiskZone2End1(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDRiskZone2End1);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //危险区域2终点位置2
//    public String CmdRDRiskZone2End2(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDRiskZone2End2);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //=======
//    //驱动器Can波特率
//    public String CmdRDDriverBaudRate(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDDriverBaudRate);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //驱动器控制模式
//    public String CmdRDDriverControlMode(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDDriverControlMode);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //驱动器加速命令
//    public String CmdRDDriverSpeedUp(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDDriverSpeedUp);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //驱动器减速命令
//    public String CmdRDDriverSpeedDown(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDDriverSpeedDown);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //驱动器急停命令
//    public String CmdRDDriverSpeedBreak(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDDriverSpeedBreak);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //驱动器PID速度控制
//    public String CmdRDDriverPIDMainSpeed(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDDriverPIDMainSpeed);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //驱动器PID子速度控制
//    public String CmdRDDriverPIDSubSpeed(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDDriverPIDSubSpeed);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }
//    //驱动器PID电流控制
//    public String CmdRDDriverPIDCurrent(String address,String setPara){
//        String command = mContext.getString(R.string.addProgramSwitch) +
//                address + mContext.getString(R.string.cmdRDDriverPIDCurrent);
//        // 查询命令
//        if(!setPara.equals("")) {
//            //设置命令
//            command += setPara;
//        }
//        return("~" + command + CreatCheckCode(command)+ "\r");
//    }



    // ========================



}
