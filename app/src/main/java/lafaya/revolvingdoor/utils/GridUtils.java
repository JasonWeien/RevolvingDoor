package lafaya.revolvingdoor.utils;

import java.util.HashMap;

public class GridUtils {

    //类接口函数定义
    private static class InstanceHolder {
        private static GridUtils sManager = new GridUtils();
    }
    public static GridUtils instance(){
        return GridUtils.InstanceHolder.sManager;
    }

    public HashMap<String, Object> getGridViewValueAndUnit(String text, int value, String unit) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("text", text);
        map.put("value", value);
        map.put("unit", unit);
        return (map);
    }
    public HashMap<String, Object> getGridViewStringValueAndUnit(String text, String value, String unit) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("text", text);
        map.put("value", value);
        map.put("unit", unit);
        return (map);
    }

    public HashMap<String, Object> getGridViewValue(String text, int value) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("text", text);
        map.put("value", value);
//        map.put("unit", unit);
        return (map);
    }

    public HashMap<String, Object> getGridViewString(String text, String unit) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("text", text);
//        map.put("value", value);
        map.put("unit", unit);
        return (map);
    }

    public HashMap<String, Object> getGridViewData(int image, String text) {
        HashMap<String, Object> map = new HashMap<>();
        map.put("image", image);
        map.put("text", text);
        return (map);
    }

}
