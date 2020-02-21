package lafaya.revolvingdoor.model;

import android.app.Activity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import java.util.HashMap;
import java.util.List;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.utils.DataBase;
import lafaya.revolvingdoor.view.MainActivity;
import lafaya.revolvingdoor.view.PageParameter;

public class InformationWingL {
    private Activity activity;

    private GridView grid_infoWingL;


    //查询菜单位置
    private int posChecked = 0;
    public boolean checkWaiting = false;
    //当前菜单中被选中的参数位置

//    public int posInfoSelected = 0;
//
//    // 仅可读参数
//    public void initializeInfoWingL(Activity inactivity){
//        //仅可读参数
//        grid_infoWingL = inactivity.findViewById(R.id.grid_infoWingL);
//        // 参数初始化
//        DataBase.instance().mapInfoWingL = ParameterUpdate.instance().paraInfoUpdate(DataBase.instance().mapInfoWingL,null,0);
//
//        //gird 监听
//        grid_infoWingL.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
////                posInfoSelected = pos;
//                ParameterUpdate.instance().readInfoOther(pos, MainActivity.sContext.getString(R.string.addAutoDoor));
//
//            }
//        });
//        updateGrideInfoWingL(inactivity);
//    }
//
//    //查询操作，当展开菜单时，查询参数。
//    public boolean checkInfoWingL(){
//        // 按菜单从上到下查询参数
//
//        ParameterUpdate.instance().readInfoOther(posChecked, MainActivity.sContext.getString(R.string.addAutoDoor));
//        //查询下一个参数
//        // 在菜单展开的情况下，继续查询数据。
//        if(checkWaiting){
//            posChecked++;
//        }
//
//        //查询到最后，结束查询，否则继续查询
//        if(posChecked >= 12){
//            posChecked = 0;
//            checkWaiting = false;
//            return true;
//        }else {
//            return false;
//        }
//    }
//
//    public void updateGrideInfoWingL(Activity activity){
//        List<HashMap<String, Object>> list;
//
//        // 参数显示写入：按上到下顺序写入其它、速度、位置、电流、PWM参数
//
//        list = ParameterUpdate.instance().listInfoNormal(DataBase.instance().mapInfoWingL);
//
//        //写入grid view
//        SimpleAdapter saImageItems = new SimpleAdapter(activity,
//                list,
//                R.layout.parameter_gridlist,
//                new String[] { "text", "value", "unit"},
//                new int[] { R.id.parameter_grid_name, R.id.parameter_grid_value, R.id.parameter_grid_unit});
//        grid_infoWingL.setAdapter(saImageItems);
//    }
}
