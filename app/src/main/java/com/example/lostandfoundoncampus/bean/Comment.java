package com.example.lostandfoundoncampus.bean;

import cn.bmob.v3.BmobObject;

public class Comment extends BmobObject {
    /**
     * 评论内容
     */
    private static final long serialVersionUID = 1L;
    private String content;
    private User user;
    private Post post;
    private String name;
    private String time;
    private String userHead;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUserHead() {
        return userHead;
    }

    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }
}
