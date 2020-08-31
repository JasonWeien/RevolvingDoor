package lafaya.revolvingdoor.view;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.utils.SystemUIUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class LaunchActivity extends Activity {

    protected SystemUIUtils systemUI = new SystemUIUtils();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        systemUI.hideNavKey(LaunchActivity.this);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
//              Intent intent = new Intent(LaunchActivity.this, LoginActivity.class);
                //取消登录界面，直接转到MainActivity
                Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
                //显式启动新的Activity
                startActivity(intent);
                //结束当前Activity
                LaunchActivity.this.finish();
            }
        },2000);
    }
}
