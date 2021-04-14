package com.example.lostandfoundoncampus.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.example.lostandfoundoncampus.R;
import com.example.lostandfoundoncampus.ui.EditActivity;
import com.example.lostandfoundoncampus.utils.CircleTransform;
import com.example.lostandfoundoncampus.utils.ImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import java.io.File;
import java.util.ArrayList;

public class GridAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<ImageItem> imageItems;

    public GridAdapter(Context context, ArrayList<ImageItem> imageItems) {
        this.context = context;
        this.imageItems = imageItems;
    }

    @Override
    public int getCount() {
        if (imageItems == null)
            return 1;
        else
            return imageItems.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return imageItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        GridAdapter.ViewHolder holder = null;
        if (convertView == null) {
            holder = new GridAdapter.ViewHolder();
            convertView = View.inflate(context, R.layout.grid_layout, null);
            holder.image_voice = (ImageView) convertView.findViewById(R.id.grid_img);
            convertView.setTag(holder);
        } else {
            holder = (GridAdapter.ViewHolder) convertView.getTag();
        }

        if (imageItems == null) {
            holder.image_voice.setImageResource(R.drawable.ic_add);
        } else {
            if (position == imageItems.size()) {
                holder.image_voice.setImageResource(R.drawable.ic_add);
            } else {
                File file = new File(imageItems.get(position).path);
                if (file.exists()) {
                    Bitmap bm = BitmapFactory.decodeFile(imageItems.get(position).path);
                    holder.image_voice.setImageBitmap(CircleTransform.centerSquareScaleBitmap(bm, 100));
                }
            }
        }
        holder.image_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (imageItems != null && position == imageItems.size() || imageItems == null) {
//                    addImage();
                }
            }
        });
        return convertView;
    }

    class ViewHolder {
        private ImageView image_voice;
    }

    //改为用内部类实现
//    private void addImage() {
//        ImagePicker imagePicker = ImagePicker.getInstance();
//        imagePicker.setImageLoader(new ImageLoader());
//        imagePicker.setMultiMode(true);  //多选
//        imagePicker.setShowCamera(true);  //显示拍照按钮
//        imagePicker.setSelectLimit(9);   //设置最多选择的图片数量
//        imagePicker.setCrop(false);    //不进行裁剪
//        Intent intent = new Intent(EditActivity.this, ImageGridActivity.class);
//    }

}
