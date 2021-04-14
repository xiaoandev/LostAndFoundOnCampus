package com.example.lostandfoundoncampus.adapter;

import android.content.Context;
import android.text.style.AlignmentSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lostandfoundoncampus.R;
import com.example.lostandfoundoncampus.bean.Publish;
import com.example.lostandfoundoncampus.bean.advertisement;
import com.example.lostandfoundoncampus.view.MyGridView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.smssdk.gui.DefaultContactViewItem;

public class ListViewAdapter extends BaseAdapter {

    private Context context;
    private List<Publish> list;
    List<String> imageUrl;
    GrideViewAdapter grideViewAdapter;

    private DisplayImageOptions options = new DisplayImageOptions.Builder()
            .showStubImage(R.mipmap.ic_launcher)  //设置图片下载期间显示的图片
            .showImageForEmptyUri(R.mipmap.ic_launcher) //设置图片uri为空或者是错位的时候显示的图片
            .showImageOnFail(R.mipmap.ic_launcher)  //设置图片加载或解码过程中发生错误显示的图片
            .cacheInMemory(true)  //设置下载的图片是否缓存在内存中
            .cacheOnDisk(true)  //设置下载的图片是否缓存在SD卡中
            .displayer(new RoundedBitmapDisplayer(20))  //设置成圆角图片
            .build();    //创建配置的DisplayImageOption对象

    ImageLoader imageLoader = ImageLoader.getInstance();

    public ListViewAdapter(Context context, List<Publish> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_publish, null);
            viewHolder = new ViewHolder();
            viewHolder.user_icon = (ImageView) convertView.findViewById(R.id.user_icon);
            viewHolder.user_name = (TextView) convertView.findViewById(R.id.user_name);
            viewHolder.publish_message = (TextView) convertView.findViewById(R.id.publish_message);
            viewHolder.publish_time = (TextView) convertView.findViewById(R.id.publish_time);
            viewHolder.gridView = (MyGridView) convertView.findViewById(R.id.publish_picture);

            //给当前的GridView设置一个位置标记
            viewHolder.gridView.setTag(position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Publish publish = list.get(position);
        viewHolder.user_name.setText(publish.getUserName()); //用户名
        viewHolder.publish_message.setText(publish.getMessage());  // 发表内容
        viewHolder.publish_time.setText(publish.getTime()); //发表时间

        //显示发表信息的用户的头像
        BmobQuery<advertisement> categoryBmobQuery = new BmobQuery<>();
        categoryBmobQuery.addWhereEqualTo("name", publish.getUserName()); //根据用户名查找
        categoryBmobQuery.findObjects(new FindListener<advertisement>() {
            @Override
            public void done(List<advertisement> object, BmobException e) {
                if (e == null) {
                    String userIcon = object.get(0).getUserIcon().getFileUrl();
                    Log.d("BMOB", userIcon);
                    imageLoader.displayImage(userIcon, viewHolder.user_icon, options);
                } else {
                    Log.d("BMOB", e.toString());
                }
            }
        });
        //没有图片资源就隐藏GridView
        if (imageUrl == null || imageUrl.size() == 0) {
            viewHolder.gridView.setVisibility(View.GONE);
        } else {
            grideViewAdapter = new GrideViewAdapter(context, imageUrl);
            viewHolder.gridView.setAdapter(grideViewAdapter);
        }
        return convertView;
    }


    public class ViewHolder {
        ImageView user_icon;
        TextView user_name;
        TextView publish_message;
        TextView publish_time;
        MyGridView gridView;
    }
}
