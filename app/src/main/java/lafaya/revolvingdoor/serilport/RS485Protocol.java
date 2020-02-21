package lafaya.revolvingdoor.serilport;

import android.content.Context;
import android.os.Bundle;
import android.os.Message;

import java.util.HashMap;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.model.ParameterUpdate;
import lafaya.revolvingdoor.utils.DataBase;
import lafaya.revolvingdoor.view.MainActivity;
import lafaya.revolvingdoor.view.PageHome;
import lafaya.revolvingdoor.view.PageMode;
import lafaya.revolvingdoor.view.PageMore;
import lafaya.revolvingdoor.view.PageParameter;

/**
 * RS485通讯协议类，包括命令字发送、接收处理等。
 *
 * 作者：Jeff Young
 * 版本：ver 1.0
 * 日期：2019/10/24
 *
 */

public class RS485Protocol {
    private Context mContext = MainActivity.sContext;


    //类接口函数定义
    private static class InstanceHolder {
        private static RS485Protocol sManager = new RS485Protocol();
    }
    public static RS485Protocol instance(){
        return RS485Protocol.InstanceHolder.sManager;
    }

    /*===========================================================================================*/

    /* 接收校验
     * 校验码：两两参数进行异或（去除握手字和结束字）
     * @ param message 输入命令字符串
     * @ return 经校验后的命令字符串
     *
     * */
    public String ReceiveCheck(String command){

        //长度过少，没有起始字，没有结束字，均视为无效命令。
        if((command.length() < 6) || (!command.substring(0,1).equals("~")) || (!command.substring(command.length()-1).equals("\r"))){
            return null;
        }
        //去掉起始字和结束字。
       String stringTmp = command.replaceAll("[\\[\\]\\s,]", "").substring(1,command.length()-1);
       if(stringTmp.length() % 2 != 0) {
           return null;
       }
       char checkCode = 0x00; //校验码
        // 高、低位直接求异或，生成校验码
        for(int i = 0; i < (stringTmp.length() - 2); i+=2){
            checkCode ^= (char)(Integer.valueOf(stringTmp.substring(i,i+1),16) * 16
                    + Integer.valueOf(stringTmp.substring(i+1,i+2),16));
        }

        //接收数据的校验码是否和接收到的校验码一致
        if(Integer.toHexString(checkCode & 0x00FF).toUpperCase().
                equals(stringTmp.substring(stringTmp.length()-2))){
            return stringTmp.substring(0,stringTmp.length()-2);
       }else{
           return null;
       }
    }


    /* @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    *                                          接收数据处理
    *
    * 接收处理流程:
    * 1、检测命令字是否满足格式要求
    * 2、校验码检测
    * 3、检测命令字类型范围：模式开关命令、旋转门专用命令、其它命令
    * 4、检测地址：0 广播，1 自动门，2 模式开关， 3 电池， 4 PC， 5 旋转门， 6 门翼1， 7门翼2， 8 信号控制器
    * 5、根据命令字进行处理
    * 6、数据更新
    * 7、检查是否在自动查询参数，若是则查询下一个参数
    *
    * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
    *  查询状态：旋转门参数查询、平滑门参数查询、门翼参数查询
    *
    *
    *
    * @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@*/
    public void CmdReceiveProcess(String RXcommand){
        //接收命令字：地址（2 bytes） + 命令字 （2/4 bytes）+ 参数 (0 / 2 / 4 bytes)
        if(RXcommand == null){
            return;
        }
        //短命令字

        //长命令字 8Axx、8Bxx、A0xx
        if(RXcommand.substring(2,4).equals(mContext.getString(R.string.cmdSPSoftVersion).substring(0,2))){
            CmdReceiveSP(RXcommand);

        }else if(RXcommand.substring(2,4).equals(mContext.getString(R.string.cmdRDVersion).substring(0,2))){
            CmdReceiveRD(RXcommand);
        }else {//短命令字
            CmdReceiveOther(RXcommand);
        }
    }

    private void CmdReceiveSP(String RXcommand){
        // 根据地址，查询或配置各类参数

        String addr = RXcommand.substring(1,2);//接收到发送到的地址：1 自动门， 6 门翼1， 7 门翼2

        if((!addr.equals(mContext.getString(R.string.addAutoDoor))) &&
                (!addr.equals(mContext.getString(R.string.addSwingL))) &&
                (!addr.equals(mContext.getString(R.string.addSwingR)))){
            return;
        }

        String cmdTemp =  RXcommand.substring(2,6);

        //命令处理
        //接收命令字：地址（2 bytes） + 命令字 （2/4 bytes）+ 参数 (0 / 2 / 4 bytes)
        // 接收到距离类命令

        int value = 0; //命令附带的参数

        if(RXcommand.length() > 6){
            value = Integer.parseInt(RXcommand.substring(6),16);
        }

        // 仅读参数
        // 速度参数
        // 位置参数
        // 电流参数
        // MCPWM参数
        // 其它参数

        if(cmdTemp.equals(mContext.getString(R.string.cmdSPSoftVersion))){
            //8A00 软件版本号
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPTripStart))) {
            //8A01 行程起点
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPTripStart))) {
            //8A02 行程终点
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPTripHalf))) {
            //8A03 半天位置
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPSpeedMin))) {
            //8A04 门体速度过低门槛
            rxParameter(addr,mContext.getString(R.string.parSDSpeedSlow), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPPIDPeriod))) {
            //8A05 PID调节中断间隔
            rxParameter(addr,mContext.getString(R.string.parSDPIDPeriod), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPSpeedLock))) {
            //8A06 速度锁定标志
            rxParameter(addr,mContext.getString(R.string.parSDSpeedLock), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPLockDelay))) {
            //8A07 锁动作延时
            rxParameter(addr,mContext.getString(R.string.parSDLockMotionDelay), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPLockRetryNB))) {
            //8A08 锁失败重试次数
            rxParameter(addr,mContext.getString(R.string.parSDLockRetryTimes), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPLockRetryPeriod))) {
            //8A09 锁重试间隔
            rxParameter(addr,mContext.getString(R.string.parSDLockRetryInterval), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPSpeedEvenPWMThreshold))) {
            //8A0A 匀速段超速不减PWM门槛
            rxParameter(addr,mContext.getString(R.string.parSDPWMEvenSpeedReduceThreshold), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPSpeedCrawlPWMThreshold))) {
            //8A0B 爬行、末段超速不减PWM门槛
            rxParameter(addr,mContext.getString(R.string.parSDPWMCrawlSpeedReducePWMThreshold), value);
        }
        //8A0C 。。。
        else if(cmdTemp.equals(mContext.getString(R.string.cmdSPSpeedBreakPWMThreshold))) {
            //8A0D 刹车段低速不增PWM门槛
            rxParameter(addr,mContext.getString(R.string.parSDPWMBreakSpeedIncreaseThreshold), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPDoorOpenRate))) {
            //8A0E 门扇开度
            rxParameter(addr,mContext.getString(R.string.parSDOpenRate), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPForceSlowOpenLength))) {
            //8A0F 开门初段强制缓行距离
            rxParameter(addr,mContext.getString(R.string.parSDLengthSlowOpen), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPSpeedCrawl))) {
            //8A10 爬行速度
            rxParameter(addr,mContext.getString(R.string.parSDSpeedCrawl), value);
        }
        //8A11 双机联动
        else if(cmdTemp.equals(mContext.getString(R.string.cmdSPSpeedDropRate))) {
            //8A12 速度跌落比
            rxParameter(addr,mContext.getString(R.string.parSDSpeedDropRate), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPCrawlOpenLength))) {
            //8A13 开门爬行距离
            rxParameter(addr,mContext.getString(R.string.parSDLengthCrawlOpen), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPCrawlCloseLength))) {
            //8A14 关门爬行距离
            rxParameter(addr,mContext.getString(R.string.parSDLengthCrawlClose), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPSlowOpenLength))) {
            //8A15 开门末段距离
            rxParameter(addr,mContext.getString(R.string.parSDLengthEndOpen), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPSlowCloseLength))) {
            //8A16 关门末段距离
            rxParameter(addr,mContext.getString(R.string.parSDLengthEndClose), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPHoldCloseUpper))) {
            //8A17 关门保持距离上限
            rxParameter(addr,mContext.getString(R.string.parSDLengthHoldUp), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPHoldCloseLower))) {
            //8A18 关门保持距离下限
            rxParameter(addr,mContext.getString(R.string.parSDLengthEndDown), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPTheoryOpenBreakDistance))) {
            //8A19 最近一次开门刹车距离
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPTheoryCloseBreakDistance))) {
            //8A1A 最近一次关门刹车距离
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPTheorySpeedUpDistance))) {
            //8A1B 最近一次加速速度最大值
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPActualSpeedUpDistance))) {
            //8A1C 最近一次实际加速距离
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPActualBreakDistance))) {
            //8A1D 最近一次实际刹车距离
        }
        else if(cmdTemp.equals(mContext.getString(R.string.cmdSPSpeedUpTimeMin))) {
            //8A1E 最小加速时间
            rxParameter(addr,mContext.getString(R.string.parSDSpeedUpTimeMin), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPReverseBreak))) {
            //8A1F 反向刹车
            rxParameter(addr,mContext.getString(R.string.parSDReverseBreak), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPPWMMin))) {
            //8A20 PWM最小值
            rxParameter(addr,mContext.getString(R.string.parSDPWMMin), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPPWMMax))) {
            //8A21 PWM最大值
            rxParameter(addr,mContext.getString(R.string.parSDPWMMax), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPLimitCurrentMax))) {
            //8A22 最大电流限流值
            rxParameter(addr,mContext.getString(R.string.parSDCurrentLimitMax), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPCurrentLevel1))) {
            //8A23 一档电流限流值
            rxParameter(addr,mContext.getString(R.string.parSDCurrentLimitOne), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPCurrentLevel2))) {
            //8A24 二档电流限流值
            rxParameter(addr,mContext.getString(R.string.parSDCurrentLimitTwo), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPCurrentLevel3))) {
            //8A25 三档电流限流值
            rxParameter(addr,mContext.getString(R.string.parSDCurrentLimitThree), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPCurrentLevel4))) {
            //8A26 四档电流限流值
            rxParameter(addr,mContext.getString(R.string.parSDCurrentLimitFour), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPCurrentHoldOpen))) {
            //8A27 开门保持电流
            rxParameter(addr,mContext.getString(R.string.parSDCurrentKeepOpen), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPCurrentHoldClose))) {
            //8A28 关门保持电流
            rxParameter(addr,mContext.getString(R.string.parSDCurrentKeepClose), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPPIDDrive))) {
            //8A29 正向驱动PID系数
            rxParameter(addr,mContext.getString(R.string.parSDPIDDrive), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPPIDRecall))) {
            //8A2A 撤油门PID系数
            rxParameter(addr,mContext.getString(R.string.parSDPIDRecall), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPPIDBreak))) {
            //8A2B 再生刹车PID系数
            rxParameter(addr,mContext.getString(R.string.parSDPIDBreak), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPPIDReverse))) {
            //8A2C 反向刹车PID系数
            rxParameter(addr,mContext.getString(R.string.parSDPIDReverse), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPDoorSmoothness))) {
            //8A2D 运行平稳度
            rxParameter(addr,mContext.getString(R.string.parSDSmoothness), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPOpenModeHoldTime))) {
            //8A2E 常开模式保持时间
            rxParameter(addr,mContext.getString(R.string.parSDKeepModeOpenTime), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPCurrentIntegral))) {
            //8A2F 历史电流积分值
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPRunCycleThisEpoch))) {
            //8A30 本次累计开关门总次数
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPRunCycleAllEpoch))) {
            //8A31 历史累计开关门总次数
        }
        else if(cmdTemp.equals(mContext.getString(R.string.cmdSPTempOverUpper))) {
            //8A32 超温报警上限温度
            rxParameter(addr,mContext.getString(R.string.parSDTempOverUpper), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPTempOverLower))) {
            //8A33 超温报警下限温度
            rxParameter(addr,mContext.getString(R.string.parSDTempOverLower), value);
        }
        //8A34 CAN单元地址
        else if(cmdTemp.equals(mContext.getString(R.string.cmdSPBatteryEnable))) {
            //8A35 备用电池使能
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPResistanceRetryNB))) {
            //8A36 开、关门遇阻重试次数
            rxParameter(addr,mContext.getString(R.string.parSDResistanceRetryTimesMax), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPResistanceRetryPeriod))) {
            //8A37 开、关门遇阻重试间隔
            rxParameter(addr,mContext.getString(R.string.parSDResistanceRetryInterval), value);
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPTestModeLockFrequency))) {
            //8A38 测试模式锁频率
            rxParameter(addr,mContext.getString(R.string.parSDTestModeLockFrequency), value);
        }
        //8A41 本次历史使用电流最大值
        //8A42
        else if(cmdTemp.equals(mContext.getString(R.string.cmdSPTemperature))) {
            //8A43 当前系统温度
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPLastRestartMsg))) {
            //8A45 系统最近10次复位原因
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPFatalErrorMsg))) {
            //8A46 严重错误信息
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdSPLicenseState))) {
            //8A47 试用版本状态
        }
        //8A51 备用电源
        //8A52 备用电源
        //8A54 备用电源
        //8A54 备用电源
        //8A55 备用电源
        //8A56 备用电源
        //8A57 备用电源

        //接收到数据后，需要更新数据，需要在handle线程上执行
        PageParameter.instance().updateMsg();
        PageMore.instance().updateMsg();

    }



    //
    private void CmdReceiveRD(String RXcommand){
        String cmdTemp = RXcommand.substring(4,6);
        if(cmdTemp.equals(mContext.getString(R.string.cmdRDInit).substring(2,4))){
            if(RXcommand.length() > 8) {
                DataBase.instance().mRunningMode = RXcommand.substring(6, 8);
                DataBase.instance().mErrorCode = RXcommand.substring(8, 10);
                if (!PageHome.instance().initFlag) {
                    PageHome.instance().initFlag = true;
                }
            }
        }
        else if(cmdTemp.equals(mContext.getString(R.string.cmdRDVersion).substring(2,4))){
            //软件版本号命令
        }else if(cmdTemp.equals(mContext.getString(R.string.cmdRDMode).substring(2,4))){
            //运行模式命令 ，容错处理，如果是不带参数查询，不理会。
            if((RXcommand.length() > 6) && (Integer.parseInt(RXcommand.substring(6,8)) <=
                    Integer.parseInt(mContext.getString(R.string.RDModeManual))) &&  (Integer.parseInt(RXcommand.substring(6,8)) >=
                    Integer.parseInt(mContext.getString(R.string.RDModeNormal)))){
                DataBase.instance().mRunningMode = RXcommand.substring(6, 8);
                //如果是处于模式修改过程中，进行模式修改后处理。
                if(PageMode.instance().modeChanging){
                    PageMode.instance().modeChanged();
                }
            }

        }else if(cmdTemp.equals(mContext.getString(R.string.cmdRDErrorCode).substring(2,4))) {
            //旋转门异常代码 8B03。
            if ((RXcommand.length() > 6) && (Integer.parseInt(RXcommand.substring(6, 8)) <
                    Integer.parseInt(mContext.getString(R.string.RDModeManual)))) {
                DataBase.instance().mRunningMode = RXcommand.substring(6, 8);
            }
        }

    }

    private void CmdReceiveOther(String RXcommand){
        String cmdTemp = RXcommand.substring(2,4);

        //初始化请求查询命令
        if(cmdTemp.equals(mContext.getString(R.string.cmdADInit))){
            //读取运行模式和异常代码

        }else if(cmdTemp.equals(mContext.getString(R.string.cmdPCLastErrorCode))) {
            //异常代码
//            DataBase.instance().mErrorCode = RXcommand.substring(4,6);
        }

    }


    //
    private void rxParameter(String addr, String index, int value){
        if(addr.equals(mContext.getString(R.string.addAutoDoor))) {
            DataBase.instance().mapNormalSliding = ParameterUpdate.instance().paraNormalUpdate(DataBase.instance().mapNormalSliding, index, value);
        }else if(addr.equals(mContext.getString(R.string.addSwingR))) {
            DataBase.instance().mapNormalWingR = ParameterUpdate.instance().paraNormalUpdate(DataBase.instance().mapNormalWingR, index, value);
        }else{
            DataBase.instance().mapNormalWingL = ParameterUpdate.instance().paraNormalUpdate(DataBase.instance().mapNormalWingL, index, value);
        }
    }



}
