package com.example.lostandfoundoncampus;

import android.os.Bundle;
import android.os.Handler;

import com.example.lostandfoundoncampus.utils.AppManager;
import com.example.lostandfoundoncampus.utils.NetWorkUtils;

import androidx.appcompat.app.AppCompatActivity;

public abstract class BaseActivity extends AppCompatActivity {

    protected BaseApplication mApplication;
    protected Handler mHandler;
    protected String TAG;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        NetWorkUtils.networkStateTips(this);
        mApplication = (BaseApplication) getApplication();
        TAG = this.getLocalClassName();
    }

}