package com.example.lostandfoundoncampus.ui;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import cn.smssdk.EventHandler;
import cn.smssdk.SMSSDK;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.PhoneNumberUtils;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lostandfoundoncampus.BaseActivity;
import com.example.lostandfoundoncampus.R;
import com.example.lostandfoundoncampus.utils.CommonUtils;
import com.example.lostandfoundoncampus.utils.TimeCountUtil;
import com.mob.MobSDK;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginByPhoneActivity extends BaseActivity implements View.OnClickListener {

    /**
     * MobTech: App Key
     */
    private final static String APP_KEY = "31c20ef8e4590";
    /**
     * MobTech: App Secret
     */
    private final static String APP_SECRETE = "71395c5833a6750d4e722b07b61edc06";

    private EditText phoneEt;
    private EditText inputVerificationCodeEt;
    private Button getVerificationCodeBtn;
    private Button loginByPhoneBtn;
    private TextView loginByUser;
    private TextView toRegister;
    private String phoneNumber;
    private String verificationCode;
    EventHandler eventHandler;
    private boolean flag = true;
    private TimeCountUtil mTimeCountUtil;
    private Button btn_test_query;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_by_phone);
        MobSDK.init(this, APP_KEY, APP_SECRETE);
        initViews();

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

        mTimeCountUtil = new TimeCountUtil(getVerificationCodeBtn, 60000, 1000);
        eventHandler = new EventHandler() {
          public void afterEvent(int event, int result, Object data) {
              Message msg = new Message();
              msg.arg1 = event;
              msg.arg2 = result;
              msg.obj = data;
              handler.sendMessage(msg);
          }
        };
        SMSSDK.registerEventHandler(eventHandler);
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

    /**
     * 使用Handler来分发Message对象到主线程中，处理事件
     */
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int event = msg.arg1;
            int result = msg.arg2;
            Object data = msg.obj;
            if (event == SMSSDK.EVENT_GET_VERIFICATION_CODE) {
                if (result == SMSSDK.RESULT_COMPLETE) {
                    boolean smart = (Boolean) data;
                    if (smart) {
                        Toast.makeText(LoginByPhoneActivity.this, "该手机号已经注册过，请重新输入",
                                Toast.LENGTH_SHORT).show();
                        //获取焦点
                        phoneEt.requestFocus();
                        return;
                    }
                }
            }
            if (result == SMSSDK.RESULT_COMPLETE) {
                if (event == SMSSDK.EVENT_SUBMIT_VERIFICATION_CODE) {
                    Toast.makeText(LoginByPhoneActivity.this, "验证码输入正确",
                            Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginByPhoneActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            } else {
                if (flag) {
                    getVerificationCodeBtn.setVisibility(View.VISIBLE);
                    Toast.makeText(LoginByPhoneActivity.this, "验证码获取失败请重新获取",
                            Toast.LENGTH_SHORT).show();
                    phoneEt.requestFocus();
                } else {
                    Toast.makeText(LoginByPhoneActivity.this, "验证码输入错误",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private void initViews() {
        phoneEt = (EditText) findViewById(R.id.et_phone);
        inputVerificationCodeEt = (EditText) findViewById(R.id.et_input_verify_code);
        getVerificationCodeBtn = (Button) findViewById(R.id.btn_get_code);
        loginByPhoneBtn = (Button) findViewById(R.id.btn_login_by_phone);
        loginByUser = (TextView) findViewById(R.id.tv_login_by_user);
        toRegister = (TextView) findViewById(R.id.tv_to_register);
        btn_test_query = (Button) findViewById(R.id.btn_test_query);

        btn_test_query.setOnClickListener(this);
        getVerificationCodeBtn.setOnClickListener(this);
        loginByPhoneBtn.setOnClickListener(this);
        loginByUser.setOnClickListener(this);
        toRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        final String phoneNumber = phoneEt.getText().toString().trim();
        switch (v.getId()) {
            //测试查询功能
//            case R.id.btn_test_query:
//                BmobQuery<BmobUser> userPhoneQuery = new BmobQuery<>();
//                userPhoneQuery.addWhereEqualTo("mobilePhoneNumber", phoneNumber);
//                userPhoneQuery.findObjects(new FindListener<BmobUser>() {
//                    @Override
//                    public void done(List<BmobUser> phoneList, BmobException e) {
//                        if (e == null) {
//                            Toast.makeText(LoginByPhoneActivity.this,"该手机号未注册",
//                                    Toast.LENGTH_SHORT).show();
//                            return;
//                        } else {
//                            Toast.makeText(LoginByPhoneActivity.this,"查询成功",
//                                    Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
            case R.id.btn_get_code:
                if (isPhoneNumber(phoneNumber)) {
                    mTimeCountUtil.start();
                    SMSSDK.getVerificationCode("86", phoneNumber);
                    inputVerificationCodeEt.requestFocus();
                } else {
                    Toast.makeText(LoginByPhoneActivity.this,"手机格式输入错误",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btn_login_by_phone:
                if (judgeVerificationCode())
                    SMSSDK.submitVerificationCode("86", phoneNumber, verificationCode);
                flag = false;
                break;
            case R.id.tv_login_by_user:
                intent = new Intent(LoginByPhoneActivity.this, LoginActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_to_register:
                intent = new Intent(LoginByPhoneActivity.this, RegisterActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    /**
     * 判断手机号是否符合规范
     * @param phoneNo
     * @return
     */
    public boolean isPhoneNumber(String phoneNo) {
        if (TextUtils.isEmpty(phoneNo)) {
            return false;
        }
        if (phoneNo.length() == 11) {
            for (int i = 0; i < 11; i++) {
                if (!PhoneNumberUtils.isISODigit(phoneNo.charAt(i))) {
                    return false;
                }
            }
            Pattern p = Pattern.compile("^((13[^4,\\D])" + "|(134[^9,\\D])" +
                    "|(14[5,7])" +
                    "|(15[^4,\\D])" +
                    "|(17[3,6-8])" +
                    "|(18[0-9]))\\d{8}$");
            Matcher m = p.matcher(phoneNo);
            return m.matches();
        }
        return false;
    }

    /**
     * 判断验证码是否填写正确
     * @return
     */
    private boolean judgeVerificationCode() {
        String phoneNumber = phoneEt.getText().toString().trim();
        if (isPhoneNumber(phoneNumber)) {
            if (TextUtils.isEmpty(inputVerificationCodeEt.getText().toString().trim())) {
                Toast.makeText(LoginByPhoneActivity.this,"请输入您的验证码",
                        Toast.LENGTH_SHORT).show();
                inputVerificationCodeEt.requestFocus();
                return false;
            } else {
                verificationCode = inputVerificationCodeEt.getText().toString().trim();
                return true;
            }
        } else {
            Toast.makeText(LoginByPhoneActivity.this,"请输入正确的手机号码",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}