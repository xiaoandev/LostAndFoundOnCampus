package com.example.lostandfoundoncampus.ui;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.MaterialEditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfoundoncampus.BaseActivity;
import com.example.lostandfoundoncampus.R;
import com.example.lostandfoundoncampus.bean.User;
import com.example.lostandfoundoncampus.utils.CommonUtils;

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private MaterialEditText usernameEdit;
    private MaterialEditText passwordEdit;
    private Button loginBt;
    private TextView registerTv;
    private TextView forgetPwd;
    private TextView loginByPhone;
    private CheckBox rememberPwd;
    private CheckBox showPwd;
    private SharedPreferences loginPreferences;
    private SharedPreferences.Editor loginEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在加载布局文件前判断是否登录过
//        loginPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//        loginEditor = loginPreferences.edit();
        //方式一：使用SharedPreferences记录登录状态，实现自动登录
        //getBoolean("SignedIn", false); 找不到"SignedIn"所对应的键值是默认返回false
//        if (loginPreferences.getBoolean("SignedIn", false)) {
//            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
//            startActivity(intent);
//            LoginActivity.this.finish();
//        }
        //方式二：使用BmobUser自带的API实现自动登录
        if (BmobUser.isLogin()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            LoginActivity.this.finish();
        }
        setContentView(R.layout.activity_login);
        initViews();
        registerListener();
//        isShowPwd(showPwd);//设置密码显示或不显示
    }

    private void initViews() {
        usernameEdit = (MaterialEditText)findViewById(R.id.login_username);
        passwordEdit = (MaterialEditText)findViewById(R.id.login_password);
        loginBt = (Button)findViewById(R.id.login_button);
        registerTv = (TextView) findViewById(R.id.goto_register);
        forgetPwd = (TextView) findViewById(R.id.forget_pwd);
        loginByPhone = (TextView) findViewById(R.id.tv_login_by_phone);
        rememberPwd = (CheckBox)findViewById(R.id.remember_password);
        showPwd = (CheckBox)findViewById(R.id.show_password);
    }

    private void registerListener() {
        loginBt.setOnClickListener(this);
        registerTv.setOnClickListener(this);
        forgetPwd.setOnClickListener(this);
        loginByPhone.setOnClickListener(this);
    }

    //设置密码是否可见
    private void isShowPwd(CheckBox showPwd) {
        showPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //显示明文，即设置为可见的密码
                    passwordEdit.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    //将光标移至末尾
                } else {
                    //不显示明文，即设置为不可见的密码
                    passwordEdit.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    //将光标移至末尾
                }
                passwordEdit.setSelection(passwordEdit.getText().length());
            }
        });
    }

    private void login_judge() {
        if (isUserNameAndPwdValid()) {
            final String name = usernameEdit.getText().toString().trim();
            final String password = passwordEdit.getText().toString().trim();
            CommonUtils.showProgressDialog(LoginActivity.this, "正在登陆...");
            User loginUser = new User();
            loginUser.setUsername(name);
            loginUser.setPassword(password);
            loginUser.login(new SaveListener<User>() {
                @Override
                public void done(User user, BmobException e) {
                    if (e == null) {
                        CommonUtils.hideProgressDialog();
                        Toast.makeText(LoginActivity.this, "用户登录成功", Toast.LENGTH_SHORT).show();
                        Intent in_success = new Intent(LoginActivity.this, MainActivity.class);
//                        loginEditor.putBoolean("SignedIn", true);
//                        loginEditor.commit();
                        startActivity(in_success);
//                        LoginActivity.this.finish();
                        //检查复选框是否被选中
                        if (!rememberPwd.isChecked()) {
                            usernameEdit.setText("");
                            passwordEdit.setText("");
                        }
                    } else {
                        CommonUtils.hideProgressDialog();
                        Toast.makeText(LoginActivity.this, "账户名或密码不正确", Toast.LENGTH_SHORT).show();
                        Log.d("RegisterActivity-DEBUG", e.getMessage());
                    }

                }
            });
        }
    }

    //判断信息是否填写完整
    private boolean isUserNameAndPwdValid() {
        if (usernameEdit.getText().toString().trim().equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (passwordEdit.getText().toString().trim().equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }


    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login_button:
                login_judge();
                //Toast.makeText(this, "登录成功", Toast.LENGTH_SHORT).show();
                break;
            case R.id.goto_register:
                intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.forget_pwd:
                intent = new Intent(LoginActivity.this, ResetPwdActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_login_by_phone:
                intent = new Intent(LoginActivity.this, LoginByPhoneActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        CommonUtils.showExitDialog(LoginActivity.this);

    }
}
