package com.example.lostandfoundoncampus.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.lostandfoundoncampus.R;
import com.example.lostandfoundoncampus.bean.Comment;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CommentAdapter extends BaseAdapter {

    private ArrayList<Comment> list;
    private Context context;

    public CommentAdapter(ArrayList<Comment> list, Context context) {
        this.list = list;
        this.context = context;
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
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_content, null);
            holder = new ViewHolder();
            holder.tvName = (TextView) convertView.findViewById(R.id.tv_content_author);
            holder.head = (RoundedImageView) convertView.findViewById(R.id.riv_content_icon);
            holder.tvTime = (TextView) convertView.findViewById(R.id.tv_publish_time);
            holder.tvContent = (TextView) convertView.findViewById(R.id.tv_message);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        final Comment comment = list.get(position);
        String mContent = list.get(position).getContent();
        if (mContent == null || mContent.length() <= 0) {
            holder.tvContent.setVisibility(View.GONE);
        } else {
            holder.tvContent.setVisibility(View.VISIBLE);
            holder.tvContent.setText(mContent);
        }
        holder.tvName.setText(list.get(position).getName());
        holder.tvTime.setText(list.get(position).getUpdatedAt());
        Picasso.with(context).load(list.get(position).getUserHead()).into(holder.head);
        return convertView;
    }

    class ViewHolder {
        TextView tvName;
        TextView tvContent;
        TextView tvTime;
        RoundedImageView head;
    }
}
