package com.example.lostandfoundoncampus.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;

public class Post extends BmobObject {

    private String userIcon;  //头像
    private String userName;  //名字
    private String content;   //发表的文字内容
    private List<String> headImgUrl;  //图片的URL集合
    private boolean haveIcon;   //判断是否有图片
    private Integer praise;  //点赞
    private String time; //发表时间

    public String getUserIcon() {
        return userIcon;
    }

    public void setUserIcon(String userIcon) {
        this.userIcon = userIcon;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getHeadImgUrl() {
        return headImgUrl;
    }

    public void setHeadImgUrl(List<String> headImgUrl) {
        this.headImgUrl = headImgUrl;
    }

    public boolean isHaveIcon() {
        return haveIcon;
    }

    public void setHaveIcon(boolean haveIcon) {
        this.haveIcon = haveIcon;
    }

    public Integer getPraise() {
        return praise;
    }

    public void setPraise(Integer praise) {
        this.praise = praise;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
