package com.example.lostandfoundoncampus.adapter;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.lostandfoundoncampus.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import java.util.List;

public class GrideViewAdapter extends BaseAdapter {

    private Context context;
    private List<String> picture;
    private DisplayImageOptions options;

    public GrideViewAdapter(Context context, List<String> picture) {
        this.context = context;
        this.picture = picture;
    }

    @Override
    public int getCount() {
        return picture.size();
    }

    @Override
    public Object getItem(int position) {
        return picture.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.item_grideview, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.iv_image);

        options = new DisplayImageOptions.Builder()
                .showStubImage(R.mipmap.ic_launcher)   //设置图片下载期间显示的图 片
                .showImageForEmptyUri(R.mipmap.ic_launcher)  //设置图片uri为空后者是错位的时候显示的图片
                .showImageOnFail(R.mipmap.ic_launcher)  //设置图片加载或解码过程中发生错误显示的图片
                .cacheInMemory(true)    //设置下载的图片是否缓存在内存中
                .cacheOnDisk(true)  //设置下载的图片是否缓存在SD卡中
                .build();    //创建配置过程的DisplayImageOption对象

        Log.d("ppp", picture.get(position));
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(picture.get(position), imageView, options);
        return view;
    }
}
