package lafaya.revolvingdoor.model;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.utils.AutoCountListView;
import lafaya.revolvingdoor.utils.DataBase;
import lafaya.revolvingdoor.view.PageParameter;

public class ParameterRevolving {

    private AutoCountListView grid_parRevolving;

    //查询位置
    private int posChecked = 0;
    public boolean checkWaiting = false;
    //当前菜单中被选中的参数位置
    public int posSelected = 0;



    //类接口函数定义
    private static class InstanceHolder {
        private static ParameterRevolving sManager = new ParameterRevolving();
    }
    public static ParameterRevolving instance(){
        return ParameterRevolving.InstanceHolder.sManager;
    }
    //

    public void initializeParRevolving(Activity inactivity){

        grid_parRevolving = inactivity.findViewById(R.id.grid_parRevolving);
        // 参数初始化
        DataBase.instance().mapParaRevolving = ParameterUpdate.instance().paraRDUpdate(DataBase.instance().mapParaRevolving,null,0);

        //grid 监听
        grid_parRevolving.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                posSelected = pos;
                // 显示对应的参数
                PageParameter.instance().gridViewRDParaSelected(pos,DataBase.instance().mapParaRevolving);
            }
        });

        updateGrideParRevolving(inactivity);

    }


    // 初始化 parameter grid view
    public void updateGrideParRevolving(Activity activity){

        // 参数显示写入：按上到下顺序写入其它、位置、驱动参数
        List<HashMap<String, Object>> list = ParameterUpdate.instance().listParameterRD(DataBase.instance().mapParaRevolving);

        //写入grid view
        SimpleAdapter saImageItems = new SimpleAdapter(activity,
                list,
                R.layout.parameter_gridlist,
                new String[] { "text", "value", "unit"},
                new int[] { R.id.parameter_grid_name, R.id.parameter_grid_value, R.id.parameter_grid_unit});
        grid_parRevolving.setAdapter(saImageItems);

    }

    // grid view 使能，当进行参数输入操作时，禁止grid view操作。
    public void enableGrid(boolean flag){
        grid_parRevolving.setEnabled(flag);
    }

    //查询操作，当展开菜单时，查询参数。
    public boolean checkParaRevolving(){
        // 按菜单从上到下查询参数

        ParameterUpdate.instance().setRDParameter(posChecked, "");
        //查询下一个参数
        if(checkWaiting){
            posChecked++;
        }

        //查询到最后，结束查询，否则继续查询
        if(posChecked >= 24){
            posChecked = 0;
            checkWaiting = false;
            return true;
        }else {
            return false;
        }
    }



}
