package com.angramainyu.logintest;

import android.app.Activity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.*;

import java.util.ArrayList;

/**
 * 登录界面
 */

public class RegisterActivity extends Activity
        implements View.OnClickListener {
    //布局内的控件
    private EditText et_name;
    private EditText et_password;
    private EditText et_password_check;
    private Button mRegisterBtn;
    private ImageView iv_see_password;
    private  String TAG = "Register";
    private LoadingDialog mLoadingDialog; //显示正在加载的对话框
    private ArrayList<User> mUsers; // 用户列表


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initViews();
        setupEvents();
        mUsers = Utils.getUserList(RegisterActivity.this);
    }

    private void initViews() {
        mRegisterBtn = (Button) findViewById(R.id.btn_register);
        et_name = (EditText) findViewById(R.id.et_account);
        et_password = (EditText) findViewById(R.id.et_password);
        et_password_check = (EditText) findViewById(R.id.et_password_check);
        iv_see_password = (ImageView) findViewById(R.id.iv_see_password);
    }

    private void setupEvents() {
        mRegisterBtn.setOnClickListener(this);
        iv_see_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_register:
                register(); //进行注册
                break;
            case R.id.iv_see_password:
                setPasswordVisibility();    //改变图片并设置输入框的文本可见或不可见
                break;
        }
    }

    /**
     * 模拟注册情况
     */
    private void register() {

        //先做一些基本的判断，比如输入的用户命为空，密码为空，网络不可用多大情况，都不需要去链接服务器了，而是直接返回提示错误
        if (getAccount().isEmpty()){
            showToast("你输入的账号为空！");
            return;
        }

        if (getPassword().isEmpty()){
            showToast("你输入的密码为空！");
            return;
        }

        if (getPasswordCheck().isEmpty()){
            showToast("你输入的确认密码为空！");
            return;
        }

        if (!getPasswordCheck().equals(getPassword())){
            showToast("你输入的两次密码不一样！");
            return;
        }

        //创建子线程来进行注册操作
        showLoading();//显示加载框
        Thread RegisterRunnable = new Thread() {

            @Override
            public void run() {
                super.run();
                setLoginBtnClickable(false);//点击登录后，设置登录按钮不可点击状态

                //判断账号和密码
                String username = getAccount();
                String password = getPassword();
                User curUser = null;
//                遍历所有用户寻找是否存在
                try {
                    for(User user: mUsers){
                        if(user.getId().equals(username)){
                            curUser = user;
                            break;
                        }
                    }
                }catch (Exception e) {
                    e.printStackTrace();
                }
                if(curUser != null && !curUser.equals("")){
                    showToast("用户名已存在");
                }else{
//                将账号密码存入本地文件之中
                    curUser = new User(username, password);
                    mUsers.add(curUser);
                    Log.i(TAG, mUsers.get(0).getId()+mUsers.get(0).getPwd());
                    try {
                        Utils.saveUserList(RegisterActivity.this,mUsers);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    showToast("注册成功");
                }
                setLoginBtnClickable(true);  //这里解放登录按钮，设置为可以点击
                hideLoading();//隐藏加载框
                finish();
            }
        };
        RegisterRunnable.start();


    }

    /**
     * 设置密码可见和不可见的相互转换
     */
    private void setPasswordVisibility() {
        if (iv_see_password.isSelected()) {
            iv_see_password.setSelected(false);
            //密码不可见
            et_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            et_password_check.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

        } else {
            iv_see_password.setSelected(true);
            //密码可见
            et_password.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            et_password_check.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        }

    }

    /**
     * 获取账号
     */
    public String getAccount() {
        return et_name.getText().toString().trim();//去掉空格
    }

    /**
     * 获取密码
     */
    public String getPassword() {
        return et_password.getText().toString().trim();
    }

    /**
     * 获取确认密码
     */
    public String getPasswordCheck() {
        return et_password_check.getText().toString().trim();
    }

    /**
     * 是否可以点击登录按钮
     *
     * @param clickable
     */
    public void setLoginBtnClickable(boolean clickable) {
        mRegisterBtn.setClickable(clickable);
    }


    /**
     * 显示加载的进度款
     */
    public void showLoading() {
        if (mLoadingDialog == null) {
            mLoadingDialog = new LoadingDialog(this, getString(R.string.loading), false);
        }
        mLoadingDialog.show();
    }


    /**
     * 隐藏加载的进度框
     */
    public void hideLoading() {
        if (mLoadingDialog != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    mLoadingDialog.hide();
                }
            });

        }
    }


    /**
     * 监听回退键
     */
    @Override
    public void onBackPressed() {
        if (mLoadingDialog != null) {
            if (mLoadingDialog.isShowing()) {
                mLoadingDialog.cancel();
            } else {
                finish();
            }
        } else {
            finish();
        }

    }

    /**
     * 页面销毁前回调的方法
     */
    protected void onDestroy() {
        if (mLoadingDialog != null) {
            mLoadingDialog.cancel();
            mLoadingDialog = null;
        }
        super.onDestroy();
    }


    public void showToast(final String msg) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });

    }

}
