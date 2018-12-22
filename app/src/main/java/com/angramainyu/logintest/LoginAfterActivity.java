package com.angramainyu.logintest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

/**
 * 登录成功后的界面
 */
public class LoginAfterActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_after);
    }

    /**
     * 返回登录界面，不消除用户和密码
     */
    public void toLogin(View view) {
        startActivity(new Intent(this, LoginActivity.class));

    }

    /**
     * 返回登录界面，消除用密码
     */
    public void toLogin2(View view) {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
