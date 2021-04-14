package com.example.lostandfoundoncampus.bean;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class User extends BmobUser {

    private BmobRelation collect;
    private String sex;
    private String img_url;
    private BmobFile photo;
    private BmobRelation myPost;
    private String words;
    private String head;

    public BmobRelation getCollect() {
        return collect;
    }

    public void setCollect(BmobRelation collect) {
        this.collect = collect;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public BmobFile getPhoto() {
        return photo;
    }

    public void setPhoto(BmobFile photo) {
        this.photo = photo;
    }

    public BmobRelation getMyPost() {
        return myPost;
    }

    public void setMyPost(BmobRelation myPost) {
        this.myPost = myPost;
    }

    public String getWords() {
        return words;
    }

    public void setWords(String words) {
        this.words = words;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    //    /**
//     * 头像
//     */
//    private BmobFile avatar;
//
//    /**
//     * 年龄
//     */
//    private Integer age;
//
//    /**
//     * 性别
//     */
//    private String gender;
//
//    public BmobFile getAvatar() {
//        return avatar;
//    }
//
//    public void setAvatar(BmobFile avatar) {
//        this.avatar = avatar;
//    }
//
//    public Integer getAge() {
//        return age;
//    }
//
//    public void setAge(Integer age) {
//        this.age = age;
//    }
//
//    public String getGender() {
//        return gender;
//    }
//
//    public void setGender(String gender) {
//        this.gender = gender;
//    }
}
