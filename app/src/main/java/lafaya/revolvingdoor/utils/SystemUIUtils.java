package lafaya.revolvingdoor.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;
import android.widget.Button;

import lafaya.revolvingdoor.R;

public class SystemUIUtils {

    //类接口函数定义
    private static class InstanceHolder {
        private static SystemUIUtils sManager = new SystemUIUtils();
    }
    public static SystemUIUtils instance(){
        return SystemUIUtils.InstanceHolder.sManager;
    }

    //系统显示兼容性处理
    public void hideNavKey(Context context) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            View v = ((Activity) context).getWindow().getDecorView();
            //隐藏虚拟按键，并且全屏
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = ((Activity) context).getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            //使状态栏和导航栏真正进入沉浸模式，即全屏模式。
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    // 按键箭头更改
    public void buttonIconChange(Context context, Button button_tmp, int state_tmp){
        if(state_tmp == View.VISIBLE) {
            Drawable homepressed = context.getResources().getDrawable(R.drawable.ic_expand_more_black);
            homepressed.setBounds(0, 0, homepressed.getMinimumWidth(), homepressed.getMinimumHeight());
            button_tmp.setCompoundDrawables(null, null, homepressed, null);
        }
        else{
            Drawable homepressed = context.getResources().getDrawable(R.drawable.ic_expand_less_black);
            homepressed.setBounds(0, 0, homepressed.getMinimumWidth(), homepressed.getMinimumHeight());
            button_tmp.setCompoundDrawables(null, null, homepressed, null);
        }
    }


}
