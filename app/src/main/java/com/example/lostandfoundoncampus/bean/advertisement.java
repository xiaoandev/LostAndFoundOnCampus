package com.example.lostandfoundoncampus.bean;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobFile;

public class advertisement extends BmobObject {
    private String name; //用户名
    private BmobFile UserIcon;  //用户头像

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BmobFile getUserIcon() {
        return UserIcon;
    }

    public void setUserIcon(BmobFile userIcon) {
        UserIcon = userIcon;
    }
}
