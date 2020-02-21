package lafaya.revolvingdoor.view;

import lafaya.revolvingdoor.R;
import lafaya.revolvingdoor.utils.SystemUIUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {
    protected SystemUIUtils systemUI = new SystemUIUtils();

    Button button_login,button_register,button_fogotpassword;
    EditText text_username,text_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        systemUI.hideNavKey(LoginActivity.this);

        button_login = findViewById(R.id.button_login);
        button_register = findViewById(R.id.button_register);
        button_fogotpassword = findViewById(R.id.button_fogotpassword);

        text_username = findViewById(R.id.text_username);
        text_password = findViewById(R.id.text_password);

        login();
        /*
        // 监听登陆按键
        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });
         */
    }

    private void login(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        LoginActivity.this.finish();
    }

}
