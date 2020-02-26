package lafaya.revolvingdoor.model;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.utils.AutoCountListView;
import lafaya.revolvingdoor.utils.DataBase;
import lafaya.revolvingdoor.utils.GridUtils;
import lafaya.revolvingdoor.view.MainActivity;
import lafaya.revolvingdoor.view.PageParameter;

public class ParameterWingL {
    private Activity activity;
    private AutoCountListView grid_parWingL;


    //查询菜单位置
    private int posChecked = 0;
    public boolean checkWaiting = false;
    //当前菜单中被选中的参数位置
    public int posParSelected = 0;

    public int posInfoSelected = 0;

    public void initializeParWingL(Activity inactivity){
        activity = inactivity;

        grid_parWingL = activity.findViewById(R.id.grid_parWingL);
        // 参数初始化
        DataBase.instance().mapNormalWingL = ParameterUpdate.instance().paraNormalUpdate(DataBase.instance().mapNormalWingL,null,0);

        //grid 监听
        grid_parWingL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                posParSelected = pos;
                PageParameter.instance().gridViewParaSelected(pos,DataBase.instance().mapNormalWingL);

            }
        });
        updateGrideParWingL(activity);

    }
    // 初始化grid view
    public void updateGrideParWingL(Activity activity){
        List<HashMap<String, Object>> list ;

        list = ParameterUpdate.instance().listParaNormal(DataBase.instance().mapNormalWingL);
        //写入grid view
        SimpleAdapter saImageItems = new SimpleAdapter(activity,
                list,
                R.layout.parameter_gridlist,
                new String[] { "text", "value", "unit"},
                new int[] { R.id.parameter_grid_name, R.id.parameter_grid_value, R.id.parameter_grid_unit});
        grid_parWingL.setAdapter(saImageItems);
    }
    // grid view 使能，当进行参数输入操作时，禁止grid view操作。
    public void enableGrid(boolean flag){
        grid_parWingL.setEnabled(flag);
    }


    //查询操作，当展开菜单时，查询参数。
    public boolean checkParaWingL(){
        // 按菜单从上到下查询参数

        ParameterUpdate.instance().setParameter(posChecked, MainActivity.sContext.getString(R.string.addSwingL), "");
        //查询下一个参数
        //查询下一个参数
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
