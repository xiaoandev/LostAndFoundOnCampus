package com.example.lostandfoundoncampus.ui;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lostandfoundoncampus.R;
import com.example.lostandfoundoncampus.adapter.CommentAdapter;
import com.example.lostandfoundoncampus.bean.Comment;
import com.example.lostandfoundoncampus.bean.Post;
import com.example.lostandfoundoncampus.bean.User;
import com.example.lostandfoundoncampus.view.MyListView;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.lzy.ninegrid.preview.NineGridViewClickAdapter;
import com.makeramen.roundedimageview.RoundedImageView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CommentActivity extends AppCompatActivity implements View.OnClickListener{

    private ArrayList<Comment> list = new ArrayList<>();
    private CommentAdapter commentAdapter;
    private Post post = new Post();
    private User user;
    private String obj;
    private AlertDialog alertDialog;
    private ArrayList<String> picList = new ArrayList<>();

    /**
     * 发表作者名
     */
    private TextView tv_publish_author_name;
    /**
     * 发表时间
     */
    private TextView tv_publish_time;
    /**
     * 发表的文字内容
     */
    private TextView tv_publish_content;
    /**
     * 点赞图标，使用TextView方便计数
     */
    private TextView tv_content_good;
    /**
     * 作者的用户头像
     */
    private RoundedImageView riv_author_icon;
    /**
     * 发表的图片
     */
    private NineGridView ngv_publish_picture;
    /**
     * 评论图标
     */
    private ImageView iv_content_comment;
    /**
     * 分享图标
     */
    private ImageView iv_content_share;
    /**
     * 返回图标
     */
    private ImageView iv_back_deal;
    /**
     * 删除发表内容图标
     */
    private ImageView iv_content_del;
    /**
     * 评论列表
     */
    private MyListView commentListView;
    /**
     * 评论输入框
     */
    private EditText et_content;
    /**
     * 提交评论按钮
     */
    private Button btn_comment_commit;
    /**
     * 评论显示区域
     */
    private LinearLayout ll_area_commit;
    /**
     * 发表的内容区域，包含文字和图片
     */
    private LinearLayout ll_message;
    /**
     * 是否存在图片
     */
    private Boolean isHaven;
    /**
     * 帖子作者id
     */
    private String author_url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        initViews();
    }

    private void initViews() {
        tv_publish_author_name = (TextView) findViewById(R.id.tv_publish_author_name);
        tv_publish_time = (TextView) findViewById(R.id.tv_publish_time);
        tv_publish_content = (TextView) findViewById(R.id.tv_publish_content);
        tv_content_good = (TextView) findViewById(R.id.tv_content_good);
        iv_content_comment = (ImageView) findViewById(R.id.iv_content_comment);
        iv_content_share = (ImageView) findViewById(R.id.iv_content_share);
        iv_back_deal = (ImageView) findViewById(R.id.iv_back_deal);
        iv_content_del = (ImageView) findViewById(R.id.iv_content_del);
        riv_author_icon = (RoundedImageView) findViewById(R.id.riv_author_icon);
        ngv_publish_picture = (NineGridView) findViewById(R.id.ngv_publish_picture);
        commentListView = (MyListView) findViewById(R.id.comment_list_view);
        et_content = (EditText) findViewById(R.id.et_content);
        btn_comment_commit = (Button) findViewById(R.id.btn_comment_commit);
        ll_message = (LinearLayout) findViewById(R.id.ll_message);
        ll_area_commit = (LinearLayout) findViewById(R.id.ll_area_commit);

        tv_publish_author_name.setText(getIntent().getStringExtra("username"));
        tv_publish_time.setText(getIntent().getStringExtra("publish_time"));
        tv_publish_content.setText(getIntent().getStringExtra("publish_content"));
        tv_content_good.setText(getIntent().getStringExtra("goods"));
        obj = getIntent().getStringExtra("obj");
        String headUrl = getIntent().getStringExtra("head");

//        if (getIntent().getStringExtra("isHaven").equals("true")) {
//            isHaven = true;
//        }
        post.setObjectId(obj);
        Glide.with(CommentActivity.this).load(headUrl).into(riv_author_icon);
        user = BmobUser.getCurrentUser(User.class);
        if (getIntent().getStringArrayListExtra("infoList") == null) {
            ngv_publish_picture.setVisibility(View.GONE);
        } else {
            picList = getIntent().getStringArrayListExtra("infoList");
            initPics(picList);
        }

        findComments();
        commentAdapter = new CommentAdapter(list, this);
        commentListView.setAdapter(commentAdapter);

    }


    /**
     * 加载帖子图片集合
     * @param picList
     */
    public void initPics(List<String> picList) {
        //判断是否有图片
        if (picList.size() > 0) {
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            for (int i = 0; i < picList.size(); i++) {
                ImageInfo info = new ImageInfo();
                info.setThumbnailUrl(picList.get(i));
                info.setBigImageUrl(picList.get(i));
                imageInfo.add(info);
            }
            ngv_publish_picture.setAdapter(new NineGridViewClickAdapter(CommentActivity.this, imageInfo));
        } else {
            ngv_publish_picture.setVisibility(View.GONE);
        }
    }

    /**
     * 加载框
     */
    private void showDialog() {
        //首先得到整个View
        LayoutInflater inflater = getLayoutInflater();
        alertDialog = new AlertDialog.Builder(this)
                .setCancelable(false)
                .setTitle("数据加载中...")
                .setView(R.layout.dialog_com)
                .setCancelable(true)
                .show();
    }

    /**
     * 发表评论
     * @param comment
     */
    private void publishComment(String comment) {
        if (user == null) {
            toast("发表评论前请登录");
            return;
        } else if (TextUtils.isEmpty(comment)) {
            toast("发表评论不能为空");
            return;
        }
        showReplyDialog();
        final Comment mComment = new Comment();
        mComment.setContent(comment);
        mComment.setPost(post);
        mComment.setUser(user);
        mComment.setName(user.getUsername());
        mComment.setTime(getTime());
        mComment.setUserHead(user.getHead());
        mComment.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    alertDialog.dismiss();
                    findComments();
                    toast("回复评论成功");
                    commentAdapter.notifyDataSetInvalidated();
                    et_content.setText("");
                } else {
                    toast(e.toString());
                    alertDialog.dismiss();
                }
            }
        });
    }

    /**
     * 查找评论
     */
    private void findComments() {
        showDialog();
        BmobQuery<Comment> query = new BmobQuery<>();
        list.clear();
        Post post = new Post();
        post.setObjectId(obj);
        query.addWhereEqualTo("post", new BmobPointer(post));
        query.include("user,author,post.author,comment.time,comment.user");
        query.findObjects(new FindListener<Comment>() {
            @Override
            public void done(List<Comment> arg0, BmobException e) {
                if (e == null) {
                    list.addAll(arg0);
                    alertDialog.dismiss();
                    commentAdapter.notifyDataSetInvalidated();
                } else {
                    alertDialog.dismiss();
                    toast("查询评论失败" + e.toString());
                    commentAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 获取时间
     */
    public String getTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
        Date curDate = new Date(System.currentTimeMillis()); //获取当前时间
        return formatter.format(curDate);
    }

    /**
     * 回复评论弹框
     */
    private void showReplyDialog() {
        LayoutInflater inflater = getLayoutInflater();
        alertDialog = new AlertDialog.Builder(this)
                .setTitle("回复评论中...")
                .setView(R.layout.dialog_com)
                .show();
    }

    /**
     * 隐藏评论输入框
     */
    private void hideCommentDialog() {
        InputMethodManager imm = (InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);
    }

    /**
     * 弹出输入框
     */
    public void popUpDialog() {
        et_content.requestFocus();
        InputMethodManager imm = (InputMethodManager) et_content.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
    }

    /**
     * 删除帖子
     */
    private void delContent() {
        Post p = new Post();
        p.setObjectId(obj);
        if (this.user.getObjectId().equals(author_url)) {
            p.delete(new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        toast("删除成功");
                        CommentActivity.this.finish();
                    } else {
                        toast("失败：" + e.getMessage() + "," + e.getErrorCode());
                    }
                }
            });
        } else {
            toast("您无权限删除别人发的帖子哦");
        }
    }

    /**
     * 获取帖子作者信息objID
     */
    public void getAuthorUrl() {
        final String[] obj_info = {""};
        final BmobQuery<BmobUser> query = new BmobQuery<>();
        query.addWhereEqualTo("username", tv_publish_author_name.getText().toString());
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> list, BmobException e) {
                alertDialog.dismiss();
                for (BmobUser data : list) {
                    obj_info[0] = data.getObjectId();
                    author_url = obj_info[0];
                }
            }
        });
    }

    /**
     * 点赞
     */
    public void goodUpdate() {
        Post post = new Post();
        post.setObjectId(obj);
        post.increment("praise");
        //不知道什么原因点赞后图片会消失，所以标记一下
        post.setHaveIcon(isHaven);
        post.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toast("点赞成功！");
                } else {
                    toast("点赞失败！");
                }
            }
        });
    }

    /**
     * 分享
     */
    public void contentShare() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/*");
        intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
        intent.putExtra(Intent.EXTRA_TEXT, tv_publish_content.getText().toString()
                + "_" + tv_publish_author_name.getText().toString() + "_ _来自朋友圈");
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(Intent.createChooser(intent, "分享"));
    }

    private void toast(String date) {
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_comment_commit:  //提交评论内容
                String commentCommit = et_content.getText().toString();
                publishComment(commentCommit);
                break;
            case R.id.tv_content_good:   //点赞
                goodUpdate();
                break;
            case R.id.iv_content_comment:   //评论
                popUpDialog();
                break;
            case R.id.iv_content_share:   //分享
                contentShare();
                break;
            case R.id.iv_content_del:   //删除帖子
                delContent();
                break;
            case R.id.iv_back_deal:  //返回
                CommentActivity.this.finish();  //关闭详情页
                break;
            default:
                break;
        }
    }
}