package com.example.lostandfoundoncampus.ui;

import android.os.Bundle;
import android.content.Intent;
import android.text.InputType;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.MaterialEditText;
import android.widget.Toast;

import com.example.lostandfoundoncampus.BaseActivity;
import com.example.lostandfoundoncampus.R;
import com.example.lostandfoundoncampus.utils.AppManager;
import com.example.lostandfoundoncampus.utils.CommonUtils;
import com.example.lostandfoundoncampus.utils.UserHelpUtil;

import java.util.List;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * 注册界面代码
 * @author xiaoandev
 * @since 2021-3-31
 */
public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    /**
     * 注册用户名
     */
    private MaterialEditText registerUserName;
    /**
     * 手机号
     */
    private MaterialEditText registerPhone;
    /**
     * 注册用户密码
     */
    private MaterialEditText registerPwd;
    /**
     * 再次输入密码
     */
    private MaterialEditText registerPwdCheck;
    /**
     * 注册按钮
     */
    private Button registerBt;
    /**
     * 返回登录界面按钮
     */
    private Button backLoginBt;
    /**
     * 显示密码
     */
    private CheckBox registerShowPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化控件
        initViews();
        registerListener();
        isShowPwd(registerShowPwd);

        //初始标题栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //显示返回按钮
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }


    private void initViews() {
        registerUserName = (MaterialEditText) findViewById(R.id.register_username);
        registerPhone = (MaterialEditText) findViewById(R.id.register_phone);
        registerPwd = (MaterialEditText) findViewById(R.id.register_password);
        registerPwdCheck = (MaterialEditText) findViewById(R.id.register_again_password);
        registerBt = (Button) findViewById(R.id.register_ok_button);
        registerShowPwd = (CheckBox) findViewById(R.id.register_show_password);

    }

    private void registerListener(){
        registerBt.setOnClickListener(this);
    }

    /**
     * 监听按钮点击事件
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //返回按钮点击事件
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * 设置密码是否可见
     * @param showPwd
     */
    private void isShowPwd(CheckBox showPwd){
        showPwd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //显示明文，即设置为可见的密码
                    registerPwd.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    registerPwdCheck.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                } else {
                    //不显示明文，即设置为不可见的密码
                    registerPwd.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    registerPwdCheck.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                }
                //将光标移至末尾
                registerPwd.setSelection(registerPwd.getText().length());
                registerPwdCheck.setSelection(registerPwdCheck.getText().length());
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.register_ok_button:
                register_check();
                break;
            default:
                break;
        }
    }

    /**
     * 对填写的注册信息进行检查
     */
    private void register_check() {
        if (isUserNameAndPwdValid()) {
            final String name = registerUserName.getText().toString().trim();
            final String phone = registerPhone.getText().toString().trim();
            final String password = registerPwd.getText().toString().trim();
            final String rePwd = registerPwdCheck.getText().toString().trim();

            CommonUtils.showProgressDialog(RegisterActivity.this, "正在注册...");
            BmobQuery<BmobUser> userNameQuery = new BmobQuery<>();
            userNameQuery.addWhereEqualTo("username", name);
            userNameQuery.findObjects(new FindListener<BmobUser>() {
                @Override
                public void done(List<BmobUser> userList, BmobException e) {
                    if (userList.size() != 0) {
                        CommonUtils.hideProgressDialog();
                        Toast.makeText(RegisterActivity.this, "用户名已存在",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        if (!UserHelpUtil.isPhoneNumber(phone)) {
                            CommonUtils.hideProgressDialog();
                            Toast.makeText(RegisterActivity.this, "输入的手机号格式不正确！",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            BmobQuery<BmobUser> phoneQuery = new BmobQuery<>();
                            phoneQuery.addWhereEqualTo("mobilePhoneNumber", phone);
                            phoneQuery.findObjects(new FindListener<BmobUser>() {
                                @Override
                                public void done(List<BmobUser> phoneList, BmobException e) {
                                    if (phoneList.size() != 0) {
                                        CommonUtils.hideProgressDialog();
                                        Toast.makeText(RegisterActivity.this, "该手机号已绑定其他用户",
                                                Toast.LENGTH_SHORT).show();
                                    } else {
                                        if (!password.equals(rePwd)) {
                                            CommonUtils.hideProgressDialog();
                                            Toast.makeText(RegisterActivity.this, "两次输入的密码不匹配！", Toast.LENGTH_SHORT).show();
                                        } else {
                                            // 使用BmobSDK提供的注册功能
                                            BmobUser user = new BmobUser();
                                            user.setUsername(name);
                                            user.setPassword(password);
                                            user.setMobilePhoneNumber(phone);
                                            user.signUp(new SaveListener<BmobUser>() {
                                                @Override
                                                public void done(BmobUser BmobUser, BmobException e) {
                                                    if(e == null) {
                                                        CommonUtils.hideProgressDialog();
                                                        Toast.makeText(RegisterActivity.this, "注册成功",
                                                                Toast.LENGTH_SHORT).show();
                                                        Intent backIntent = new Intent(RegisterActivity.this,
                                                                LoginActivity.class);
                                                        startActivity(backIntent);
                                                    } else {
                                                        CommonUtils.hideProgressDialog();
                                                        Toast.makeText(RegisterActivity.this, "注册失败！",
                                                                Toast.LENGTH_SHORT).show();
                                                        Log.d("RegisterActivity-DEBUG", e.getMessage());
                                                    }
                                                }
                                            });
                                        }
                                    }

                                }
                            });

                        }
                    }
                }
            });
        }
    }

    /**
     * 判断用户信息是否填写完整
     * @return
     */
    private boolean isUserNameAndPwdValid() {
        if (registerUserName.getText().toString().trim().equals("")) {
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (registerPhone.getText().toString().trim().equals("")) {
            Toast.makeText(this, "手机号不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (registerPwd.getText().toString().trim().equals("")) {
            Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else if (registerPwdCheck.getText().toString().trim().equals("")) {
            Toast.makeText(this, "再次输入密码不能为空", Toast.LENGTH_SHORT).show();
            return false;
        } else {
            return true;
        }
    }
}
