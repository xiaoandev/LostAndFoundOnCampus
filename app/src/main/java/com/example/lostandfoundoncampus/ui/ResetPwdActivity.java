package com.example.lostandfoundoncampus.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.MaterialEditText;
import android.widget.Toast;

import com.example.lostandfoundoncampus.BaseActivity;
import com.example.lostandfoundoncampus.R;
import com.example.lostandfoundoncampus.utils.AppManager;
import com.example.lostandfoundoncampus.utils.CommonUtils;
import com.mob.MobSDK;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

public class ResetPwdActivity extends BaseActivity implements View.OnClickListener {

    /**
     * MobTech: App Key
     */
    private final static String APP_KEY = "31c20ef8e4590";
    /**
     * MobTech: App Secret
     */
    private final static String APP_SECRETE = "71395c5833a6750d4e722b07b61edc06";
    /**
     * 手机号输入框
     */
    private MaterialEditText phoneEt;
    /**
     * 验证码输入框
     */
    private MaterialEditText verifyCodeEt;
    /**
     * 获取验证码按钮
     */
    private Button getVerifyCodeBtn;
    /**
     * 重新获取时间
     */
    private int reacquireTime = 60;
    /**
     * 0:设置为初始化值 1：请求获取验证码 2：提交用户输入的验证码判断是否正确
     */
    public int smsFlag = 0;

    private MaterialEditText new_password;
    private MaterialEditText new_again_password;
    private Button certain_reset_password;
    private EventHandler eventHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_pwd);
        MobSDK.init(this, APP_KEY, APP_SECRETE);
        initView();

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

        eventHandler = new EventHandler() {
            @Override
            public void afterEvent(int event, int result, Object data) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    //回调完成
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                        //提交短信验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ResetPwdActivity.this, "验证成功",
                                        Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ResetPwdActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        //获取验证码成功
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(ResetPwdActivity.this, "验证码已发送",
                                        Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable)data).printStackTrace();
                    Throwable throwable = (Throwable) data;
                    try {
                        JSONObject obj = new JSONObject(throwable.getMessage());
                        final String des = obj.optString("detail");
                        if (!TextUtils.isEmpty(des)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(ResetPwdActivity.this, "提交错误信息",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        //注册回调监听接口
        SMSSDK.registerEventHandler(eventHandler);
    }

    private void initView() {
        new_password = (MaterialEditText) findViewById(R.id.new_password);
        new_again_password = (MaterialEditText) findViewById(R.id.new_again_password);
        certain_reset_password = (Button) findViewById(R.id.certain_reset_password);
        phoneEt = (MaterialEditText) findViewById(R.id.user_phone);
        verifyCodeEt = (MaterialEditText) findViewById(R.id.verify_code);
        getVerifyCodeBtn = (Button) findViewById(R.id.get_verify_code);

        getVerifyCodeBtn.setOnClickListener(this);
        certain_reset_password.setOnClickListener(this);
    }


    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == -9) {
                getVerifyCodeBtn.setText("重新发送(" + reacquireTime + ")");
            } else if (msg.what == -8) {
                getVerifyCodeBtn.setText("获取验证码");
                getVerifyCodeBtn.setClickable(true);
                reacquireTime = 60;
            } else if (msg.what == 2) {
                Toast.makeText(ResetPwdActivity.this, "验证码获取成功，注意查看",
                        Toast.LENGTH_SHORT).show();
            } else if (msg.what == 3) {
                Toast.makeText(ResetPwdActivity.this, "获取验证码失败，请填写正确的手机号",
                        Toast.LENGTH_SHORT).show();
            } else if (msg.what == 4) {
                Toast.makeText(ResetPwdActivity.this, "验证码错误",
                        Toast.LENGTH_SHORT).show();
            } else {
                int event = msg.arg1;
                int result = msg.arg2;
                Object data = msg.obj;
                Log.e("event", "event=" + event);
                if (result == SMSSDK.RESULT_COMPLETE) {
                    // 短信注册成功后，返回MainActivity,然后提示
                    if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {// 提交验证码成功
                        Toast.makeText(ResetPwdActivity.this, "提交验证码成功",
                                Toast.LENGTH_SHORT).show();
//                        Intent intent = new Intent(ResetPwdActivity.this,
//                                LoginActivity.class);
//                        startActivity(intent);
                    } else if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                        Toast.makeText(ResetPwdActivity.this, "正在获取验证码",
                                Toast.LENGTH_SHORT).show();
                    } else if (event == SMSSDK.EVENT_GET_SUPPORTED_COUNTRIES) {
                        //返回支持发送验证码的国家列表
                    }
                } else {
                    ((Throwable) data).printStackTrace();
                    //此语句代表接口返回失败
                    //获取验证码失败。短信验证码验证失败（用flage标记来判断）
                    if (smsFlag==1) {
                        handler.sendEmptyMessage(3);
                    } else if (smsFlag==2) {
                        handler.sendEmptyMessage(4);
                    }
                }

            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.get_verify_code:
                String phoneIn = phoneEt.getText().toString();
                SMSSDK.getVerificationCode("86", phoneIn);
                //把按钮变成不可点击，并且显示倒计时（正在获取）
                getVerifyCodeBtn.setBackgroundResource(R.drawable.ic_verify_button_touch);
                getVerifyCodeBtn.setClickable(false);
                getVerifyCodeBtn.setText("重新发送(" + reacquireTime + ")");
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        for (; reacquireTime > 0; reacquireTime--) {
                            handler.sendEmptyMessage(-9);
                            if (reacquireTime <= 0) {
                                break;
                            }
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        handler.sendEmptyMessage(-8);
                    }
                }).start();
                break;
            case R.id.certain_reset_password:
                String phoneOut = phoneEt.getText().toString();
                String verifyCode = verifyCodeEt.getText().toString();
                SMSSDK.submitVerificationCode("86", phoneOut, verifyCode);
//                String password = new_password.getText().toString().trim();
//                String again_password = new_again_password.getText().toString().trim();
//                if (!password.equals(again_password)) {
//                    Toast.makeText(this, "两次输入的密码不相同，请重新输入！", Toast.LENGTH_SHORT).show();
//                } else {
//                    Intent intent = getIntent();
//                    String phone = intent.getStringExtra("input_phone");
//                    User user = UserHelpUtil.findUserByPhone(phone);
//                    if (user == null) {
//                        Toast.makeText(this, "该用户不存在！", Toast.LENGTH_SHORT).show();
//                    } else if (user.getUserPwd().equals(password)) {
//                        Toast.makeText(this, "新密码与原密码相同！", Toast.LENGTH_SHORT).show();
//                    } else {
//                        user.setUserPwd(password);
//                        user.save();
//                        Toast.makeText(this, "密码更改成功，即将跳转到登录界面", Toast.LENGTH_SHORT).show();
//                        new Handler().postDelayed(new Runnable() {
//                            public void run() {
//                                Intent loginIntent = new Intent(ResetPwdActivity.this, LoginActivity.class);
//                                startActivity(loginIntent);
//                                finish();
//                            }
//                        }, 2000); //延迟2秒跳转
//                    }
//                }
//                break;
        }
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

    // 使用完EventHandler需注销，否则可能出现内存泄漏
    @Override
    protected void onDestroy() {
        super.onDestroy();
        SMSSDK.unregisterEventHandler(eventHandler);
    }
}