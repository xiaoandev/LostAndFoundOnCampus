package com.example.lostandfoundoncampus;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import cn.bmob.v3.Bmob;

public class BaseApplication extends Application {
	public SharedPreferences mSharedPreferences;
	public static BaseApplication mInstance = null;
	@Override
	public void onCreate() {

		super.onCreate();

		mInstance = this;
		mSharedPreferences = getSharedPreferences(
				Constants.SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);

		Bmob.initialize(this, "1485c06469e6617f994f95fc888fea5a");

	}

}
