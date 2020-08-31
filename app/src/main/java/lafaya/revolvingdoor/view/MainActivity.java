package lafaya.revolvingdoor.view;

import lafaya.revolvingdoor.model.ParameterUpdate;
import lafaya.revolvingdoor.serilport.RS485SendCommand;
import lafaya.revolvingdoor.serilport.SerialPortThread;
import lafaya.revolvingdoor.utils.GridUtils;
import lafaya.revolvingdoor.utils.SystemUIUtils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import lafaya.revolvingdoor.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

//
public class MainActivity extends Activity {

    public static Context sContext;
    private GridView grid_menu;
    private GridUtils gridUtils = new GridUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SystemUIUtils.instance().hideNavKey(MainActivity.this);
        super.onCreate(savedInstanceState);
        super.onWindowFocusChanged(true);
        //设置一个显示界面，可动态显示切换View
        setContentView(R.layout.activity_main);
        //返回应用的上下文。
        sContext = getApplicationContext();

        // 下端菜单按键布局
        grid_menu = findViewById(R.id.grid_menu);
        //初始化各个页面
        PageHome.instance().PageInit(this,handler);
        PageMode.instance().PageInit(this,handler);
        PageParameter.instance().PageInit(this,handler);
        PageMore.instance().PageInit(this,handler);
        PageMaintenance.instance().PageInit(this,handler);

        // 串口通讯初始化
        SerialPortThread.instance().open();
        //参数更新线程初始化
        ParameterUpdate.instance().startThread(handler);//检测是否已


        //默认启动后，显示主页面，同时显示对应的菜单按键
        updateView(0);

        //监控菜单的操作
        onClickMenuGrid();

    }
    // 监听菜单grid操作
    private void onClickMenuGrid(){
        grid_menu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                updateView(position);
                //发送查询命令：7E 21 80 校验字 0D
                SerialPortThread.instance().sendMsg(RS485SendCommand.instance().CmdQueryInit(
                        MainActivity.sContext.getString(R.string.addAutoDoor)));
            }
        });

        //禁止grid view 上下滑动
        grid_menu.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return MotionEvent.ACTION_MOVE == motionEvent.getAction();
            }
        });
    }

    //页面下部导航图标初始化
    private void updateMenuGride(Activity activity, int position){
        List<HashMap<String, Object>> list = new ArrayList<>();
        int[] menu_list = {R.drawable.ic_home, R.drawable.ic_mode, R.drawable.ic_par_tune, R.drawable.ic_maintenance, R.drawable.ic_more};
        int[] menu_list_on = {R.drawable.ic_home_on, R.drawable.ic_mode_on, R.drawable.ic_tune_on, R.drawable.ic_maintenance_on, R.drawable.ic_more_on};
        String[] menu_name = {"主页", "模式", "参数", "维护", "更多"};

        menu_list[position] = menu_list_on[position];

        for(int i=0;i<menu_list.length;i++){
            list.add(gridUtils.getGridViewData(menu_list[i], menu_name[i]));
        }
        //写入grid view
        SimpleAdapter saImageItems = new SimpleAdapter(activity,
                list,
                R.layout.menu_gridlist,
                new String[] { "image", "text"},
                new int[] { R.id.image_menu_list, R.id.image_menu_name});
        grid_menu.setAdapter(saImageItems);

    }

    // 更新页面显示
    private void updateView(int position){
        //更新菜单显示
        updateMenuGride(this,position);

        //更新各页面的显示
        boolean[] menu_view = {false,false,false,false,false};
        menu_view[position] = true;
        PageHome.instance().showPageHome(menu_view[0]);
        PageMode.instance().showPageMode(menu_view[1]);
        PageParameter.instance().showPageParameter(menu_view[2]);
        PageMaintenance.instance().showPageMaintenance(menu_view[3]);
        PageMore.instance().showPageMore(menu_view[4]);
    }

    // 菜单grid操作使能 or 禁止
//    public void enableMenuGrid(boolean state){
//        grid_menu.setEnabled(state);
//    }

//===================================================================

    @SuppressLint("HandlerLeak")
    public Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg){
            Bundle bd = msg.getData();
            switch (msg.what){
                case 0:// 异常信息显示
                    //如果是正在修改运行模式，则进行运行械处理。
                    if(PageMode.instance().modeChanging){
                        PageMode.instance().modeChangeFinished();
                    }
                    //显示信息内容
                    Toast.makeText(sContext,bd.getString("msg"),Toast.LENGTH_SHORT).show();
                    break;
                case 1://主页信息更新
                    String text1 = bd.getString("msg1");
                    String text2 = bd.getString("msg2");
                    PageHome.instance().updateShowMsg(text1,text2);
                    break;
                case 2://模式界面闪烁显示
                    PageMode.instance().flashShow();
                    break;
                case 3://参数查询更新
                    PageParameter.instance().updateLayout();
                    PageMore.instance().updateLayout();

                    break;
                case 99:
//                    String text = bd.getString("msg");
                    Toast.makeText(sContext,bd.getString("msg"),Toast.LENGTH_SHORT).show();
                    break;
                default:
                        break;
            }

        }
    };

    //
    @Override
    protected  void onDestroy() {
        super.onDestroy();
        SerialPortThread.instance().onDestroy();
        ParameterUpdate.instance().stopThread();
        PageHome.instance().stopHomeThread();
    }



}
