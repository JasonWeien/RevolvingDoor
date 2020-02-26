package lafaya.revolvingdoor.model;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import java.util.HashMap;
import java.util.List;
import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.utils.AutoCountListView;
import lafaya.revolvingdoor.utils.DataBase;
import lafaya.revolvingdoor.view.MainActivity;
import lafaya.revolvingdoor.view.PageParameter;

public class ParameterSliding {
    private AutoCountListView grid_parSliding;
//    private Context mContext = MainActivity.sContext;

    //查询菜单位置
    private int posChecked = 0;
    public boolean checkWaiting = false;

    //当前菜单中被选中的参数位置
    public int posParSelected = 0;

    public int posInfoSelected = 0;


    // 可读可写参数
    public void initializeParSliding(Activity inactivity){
        grid_parSliding = inactivity.findViewById(R.id.grid_parSliding);

        // 参数初始化
        DataBase.instance().mapNormalSliding = ParameterUpdate.instance().paraNormalUpdate(DataBase.instance().mapNormalSliding,null,0);


        //grid 监听
        grid_parSliding.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                posParSelected = pos;
                PageParameter.instance().gridViewParaSelected(pos,DataBase.instance().mapNormalSliding);
            }
        });
        //
        updateGrideParSliding(inactivity);
    }



    // 初始化 parameter grid view
    public void updateGrideParSliding(Activity activity){
        List<HashMap<String, Object>> list;

        // 参数显示写入：按上到下顺序写入其它、速度、位置、电流、PWM参数
        list = ParameterUpdate.instance().listParaNormal(DataBase.instance().mapNormalSliding);

        //写入grid view
        SimpleAdapter saImageItems = new SimpleAdapter(activity,
                list,
                R.layout.parameter_gridlist,
                new String[] { "text", "value", "unit"},
                new int[] { R.id.parameter_grid_name, R.id.parameter_grid_value, R.id.parameter_grid_unit});
        grid_parSliding.setAdapter(saImageItems);
    }


    // grid view 使能，当进行参数输入操作时，禁止grid view操作。
    public void enableGrid(boolean flag){
        grid_parSliding.setEnabled(flag);
    }

    //查询操作，当展开菜单时，查询参数。
    public boolean checkParaSliding(){
        // 按菜单从上到下查询参数

        ParameterUpdate.instance().setParameter(posChecked, MainActivity.sContext.getString(R.string.addAutoDoor), "");
        //查询下一个参数
        // 在菜单展开的情况下，继续查询数据。
        if(checkWaiting){
            posChecked++;
        }

        //查询到最后，结束查询，否则继续查询
        if(posChecked >= 46){
            posChecked = 0;
            checkWaiting = false;
            return true;
        }else {
            return false;
        }
    }


 }
