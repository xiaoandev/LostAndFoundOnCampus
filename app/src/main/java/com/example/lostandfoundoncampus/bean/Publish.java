package com.example.lostandfoundoncampus.bean;

import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class Publish extends BmobObject {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 用户发布帖子的内容
     */
    private String message;

    /**
     * 用户发布帖子的时间
     */
    private String time;

    /**
     * 用户最多能发9张图
     */
    private List<BmobFile> picture;

    /**
     *上传多少张图片
     */
    private int pictureNumber;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public List<BmobFile> getPicture() {
        return picture;
    }

    public void setPicture(List<BmobFile> picture) {
        this.picture = picture;
    }

    public int getPictureNumber() {
        return pictureNumber;
    }

    public void setPictureNumber(int pictureNumber) {
        this.pictureNumber = pictureNumber;
    }
}
