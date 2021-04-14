package com.example.lostandfoundoncampus.ui;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.lostandfoundoncampus.R;
import com.example.lostandfoundoncampus.bean.Post;
import com.example.lostandfoundoncampus.bean.User;
import com.example.lostandfoundoncampus.utils.CircleTransform;
import com.example.lostandfoundoncampus.utils.ImageLoader;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.ninegrid.ImageInfo;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity implements View.OnClickListener{

    private EditText et_content;
    private GridView publishGridView;
    private GridAdapter gridAdapter;
    private TextView tv_publish, tv_cancel;
    private int size = 0;
    private String content;
    private BmobUser user;
    private ArrayList<ImageItem> imageItems;
    ProgressDialog dialog = null; //进度条

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_edit);

        initViews();
    }

    private void initViews() {
        tv_publish = (TextView) findViewById(R.id.tv_publish);
        tv_cancel = (TextView) findViewById(R.id.tv_cancel);
        et_content = (EditText) findViewById(R.id.et_content);
        publishGridView = (GridView) findViewById(R.id.publish_gridView);

        user = BmobUser.getCurrentUser(User.class);
        gridAdapter = new GridAdapter();
        publishGridView.setAdapter(gridAdapter);

        tv_publish.setOnClickListener(this);
        tv_cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_publish:
                content = et_content.getText().toString();
                if (content.length() < 1 && size == 0) {
                    toast("文字内容不能为空");
                } else {
//                    tv_publish.setEnabled(false);
                    tv_upload_database();
                }
                break;
            case R.id.tv_cancel:
                EditActivity.this.finish();
                break;
            default:
                break;
        }
    }

    private class GridAdapter extends BaseAdapter {

        public GridAdapter() {
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
                convertView = LayoutInflater.from(EditActivity.this).inflate(R.layout.grid_layout, null);
                holder.image_voice = (ImageView) convertView.findViewById(R.id.grid_img);
                convertView.setTag(holder);
            } else {
                holder = (GridAdapter.ViewHolder) convertView.getTag();
            }
            if (imageItems == null) {
                holder.image_voice.setImageResource(R.mipmap.add_icon);
            } else {
                if (position == imageItems.size()) {
                    holder.image_voice.setImageResource(R.mipmap.add_icon);
                } else {
                    File file = new File(imageItems.get(position).path);
                    if (file.exists()) {
                        Bitmap bm = BitmapFactory.decodeFile(imageItems.get(position).path);
                        holder.image_voice.setImageBitmap(CircleTransform.centerSquareScaleBitmap(bm,100));
                    }
                }
            }
            holder.image_voice.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if ((imageItems != null && position == imageItems.size()) || imageItems == null) {
                        addImage();
                    }
                }
            });
            return convertView;
        }

        class ViewHolder {
            private ImageView image_voice;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            if (data != null && requestCode == 100) {
                ArrayList<ImageInfo> imageInfos = new ArrayList<>();
                imageItems = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                gridAdapter.notifyDataSetChanged();
                size = imageItems.size();
            } else {
                toast("没有选择图片");
            }
        }
    }

    /**
     * 上传图片
     */
    private void tv_upload_database() {
        //隐藏软硬盘
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        toast("发布中...");
//        String username = user.getUsername();
        String username = "测试号01";
        final Post post = new Post();
        post.setContent(content);
        post.setUserName(username);
        post.setPraise(0);
        post.setTime(getTime());
        post.setUserIcon(getIntent().getStringExtra("headUrl"));
        if (size == 0) {
            post.setHaveIcon(false);
            post.save(new SaveListener<String>() {
                @Override
                public void done(String s, BmobException e) {
                    if (e == null) {
                        et_content.setText("");
                        toast("发表成功");
                        Log.d("DEBUG-EditActivity", "发表成功01");
                        Intent intent = new Intent(EditActivity.this, CommentActivity.class);
                        startActivity(intent);
//                        finish();
                    } else {
                        toast("失败" + e.toString());
                        Log.d("DEBUG-EditActivity", "发表失败");
                    }
                }
            });
            return;
        }

        size = 0;
        final String[] filePaths = new String[imageItems.size()];
        for (int i = 0; i < imageItems.size(); i++) {
            filePaths[i] = imageItems.get(i).path;
        }
        dialog = new ProgressDialog(EditActivity.this);
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        dialog.setTitle("上传图片中...");
        dialog.setIndeterminate(false);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {
            @Override
            public void onSuccess(List<BmobFile> list, List<String> list1) {
                Log.d("DEBUG-EditActivity", "上传图片成功");
                //如果数量相等，则代表文件全部上传完成
                if (list1.size() == filePaths.length) {
                    post.setHeadImgUrl(list1);
                    post.setHaveIcon(true);
                    post.save(new SaveListener<String>() {
                        @Override
                        public void done(String s, BmobException e) {
                            if (e == null) {
                                Log.d("DEBUG-EditActivity", "发表成功02");
                                toast("发表成功");
                                Intent intent = new Intent(EditActivity.this, CommentActivity.class);
                                startActivity(intent);
//                                finish();
                            } else {

                            }
                        }
                    });
                }
            }

            @Override
            public void onProgress(int i, int i1, int i2, int i3) {
                dialog.setProgress(12);
            }

            @Override
            public void onError(int i, String s) {
                Log.d("DEBUG-EditActivity", "上传图片失败");
                dialog.dismiss();
            }
        });
    }

    /**
     * 添加图片
     */
    private void addImage () {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setImageLoader(new ImageLoader());
        imagePicker.setMultiMode(true);   //多选
        imagePicker.setShowCamera(true);  //显示拍照按钮
        imagePicker.setSelectLimit(9);    //最多选择X张
        imagePicker.setCrop(false);       //不进行裁剪
        Intent intent = new Intent(EditActivity.this, ImageGridActivity.class);
        startActivityForResult(intent, 100);
    }

    /**
     * 获取时间
     * @return
     */
    public String getTime () {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日 HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis()); //获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 显示提示信息
     * @param message
     */
    private void toast (String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
