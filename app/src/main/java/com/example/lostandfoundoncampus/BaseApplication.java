package com.example.lostandfoundoncampus;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.lostandfoundoncampus.utils.ImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.view.CropImageView;
import com.lzy.ninegrid.NineGridView;

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
//		NineGridView.setImageLoader(new );
		ImagePicker imagePicker = ImagePicker.getInstance();
		imagePicker.setImageLoader(new ImageLoader());   //设置图片加载器
		imagePicker.setShowCamera(true);  //显示拍照按钮
		imagePicker.setCrop(true);  //允许剪裁（单选才有效）
		imagePicker.setSaveRectangle(true);  //是否按矩形区域保存
		imagePicker.setSelectLimit(9);  //选中数量限制
		imagePicker.setStyle(CropImageView.Style.RECTANGLE); //裁剪框的形状
		imagePicker.setFocusWidth(800);  //裁剪框的宽度。
		imagePicker.setFocusHeight(800);  //裁剪框的高度。
		imagePicker.setOutPutX(1000); //保存文件的宽度
		imagePicker.setOutPutY(1000);  //保存文件的高度。 单位像素
	}

	private class GlideImageLoader implements NineGridView.ImageLoader {
		@Override
		public void onDisplayImage(Context context, ImageView imageView, String url) {
			Glide.with(context).load(url)
					.placeholder(R.drawable.ic_default_image)
					.error(R.drawable.ic_default_image)
					.into(imageView);
		}

		@Override
		public Bitmap getCacheImage(String url) {
			return null;
		}
	}

}
