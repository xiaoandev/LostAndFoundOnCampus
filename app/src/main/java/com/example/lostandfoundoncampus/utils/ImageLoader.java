package com.example.lostandfoundoncampus.utils;

import android.app.Activity;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.File;

/**
 * created by XiaoAnDev on 2021/4/11.
 */
public class ImageLoader implements com.lzy.imagepicker.loader.ImageLoader {
    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)        //配置上下文
                .load(Uri.fromFile(new File(path)))  //设置图片路劲（fix #8，文件名包含 % 符号，无法识别）
                .diskCacheStrategy(DiskCacheStrategy.ALL)  //缓存全尺寸
                .into(imageView);
    }

    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity)        //配置上下文
                .load(Uri.fromFile(new File(path)))   //设置图片路劲（fix #8，文件名包含 % 符号，无法识别）
                .diskCacheStrategy(DiskCacheStrategy.ALL)   //缓存全尺寸
                .into(imageView);
    }

    @Override
    public void clearMemoryCache() {

    }
}
