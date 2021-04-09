package com.example.lostandfoundoncampus.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

public class NetWorkUtils {

	/**
	 * 网络是否可用
	 * 
	 * @param context
	 * @return
	 */
	public static boolean isNetworkAvailable(Context context) {
		ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivity == null) {} else {
			NetworkInfo[] info = connectivity.getAllNetworkInfo();
			if (info != null) {
				for (int i = 0; i < info.length; i++) {
					if (info[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * 网络连接提示
	 * 
	 * @param context
	 * @return
	 */
	public static void networkStateTips(Context context) {
		if (!isNetworkAvailable(context)) {
			Toast.makeText(context, "网络故障，请先检查网络连接", Toast.LENGTH_SHORT).show();
		}
	}

}
