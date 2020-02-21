package lafaya.revolvingdoor.serilport;

import android.serialport.SerialPort;
import android.widget.Toast;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Handler;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.view.MainActivity;

/**
 * 串口通讯程类，包括串口发送、串口接收、串口打开与关闭
 *
 * 作者：Jeff Young
 * 版本：ver 1.0
 * 日期：2019/10/23
 *
 * @ return isReceiveMsg 接收数据待处理标志位，true = 待处理，false = 无需要处理
 * @ return mReBuffer 字节形式接收缓冲变量
 */
public class SerialPortThread{

    //初始定义
    private SerialPort mSerialPort;
    private OutputStream mOutputStream;
    private BufferedInputStream mInputStream;
    private ReceiveThread mReceiveThread;

    // 定义接收数据待处理标志位
    public boolean isReceiveMsg = false;
    // 定义接收数据缓冲变量
    public String mReBuffer;


    //类接口函数定义
    private static class InstanceHolder {
        private static SerialPortThread sManager = new SerialPortThread();
    }
    public static SerialPortThread instance(){
        return InstanceHolder.sManager;
    }


    // 打开串口
    public void open(){
        // 串口已经打开过了，先关闭串口
        if (mSerialPort != null) {
            close();
        }

//        new Handler().postDela
//        //延时：
//        TimerTask task = new TimerTask() {
//            @Override
//            public void run() {
//            }
//        };
//        Timer timer = new Timer();
//        timer.schedule(task, 500);

        //打开串口
        try {
            //配置串口端口号及波特率
            mSerialPort = new SerialPort(new File(MainActivity.sContext.getResources().getString(R.string.devicePath)),
                    Integer.parseInt(MainActivity.sContext.getResources().getString(R.string.baudRate)), 0);

            //配置接收接口，并打开接收线程
            mInputStream = new BufferedInputStream(mSerialPort.getInputStream());

            mReceiveThread = new ReceiveThread();
            mReceiveThread.start();

            //配置发送接口
            mOutputStream = mSerialPort.getOutputStream();
            ToastUtils("串口已打开");
        }catch (IOException e){
            e.printStackTrace();
            ToastUtils("串口打开失败");
        }

    }

    //关闭串口
    private void close(){
        //关闭接收线程
        try {
            mInputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            mReceiveThread.interrupt();
            mReceiveThread = null;
        }
        //关闭串口
        if (mSerialPort != null) {
            mSerialPort.close();
            mSerialPort = null;
        }
        ToastUtils("串口已关闭");
    }

    /*
    * @param msg 待发送的数据，字节数组形式
    * */
    public void sendMsg(String msg){
        if(mSerialPort != null){
            try {
                mOutputStream.write(msg.getBytes());//
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    //串口接收线程
    private class ReceiveThread extends Thread{
        @Override
        public void run(){
            while (!isInterrupted()){
                byte[] received = new byte[64];
                int size;
                try {
                    if (mInputStream == null) return;
                    //读取数据
                    size = mInputStream.read(received);
                    if (size > 0) {
                        //在没有数据待处理时，接收填充数据，否则放弃数据接收
                        if(!isReceiveMsg) {
                            mReBuffer = ByteUtil.bytesToAscii(received, size);
                            //数据待处理标志
                            isReceiveMsg = true;
                        }
//                        sendMsg(ByteUtil.bytesToAscii(received, size).getBytes());

                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //
    public void onDestroy() {
        close();
    }


    //======================
    //弹出窗口，显示信息，测试用
    private void ToastUtils(String message){
        Toast.makeText(MainActivity.sContext,message,Toast.LENGTH_SHORT).show();
    }

}
