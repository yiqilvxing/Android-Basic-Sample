package com.cnitr.cn.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by YangChen on 2018/7/5.
 */

@Table(name = "user")
public class UserEntity implements Serializable {

    @Column(name = "_id", isId = true)
    private int _id;

    // 用户名称
    @Column(name = "username")
    private String username;

    // 头像
    @Column(name = "headimgurl")
    private String headimgurl;

    // 地区
    @Column(name = "address")
    private String address;

    // 邮箱
    @Column(name = "email")
    private String email;

    // 电话
    @Column(name = "telephone")
    private String telephone;

    // 个性签名
    @Column(name = "signature")
    private String signature;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getHeadimgurl() {
        return headimgurl;
    }

    public void setHeadimgurl(String headimgurl) {
        this.headimgurl = headimgurl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
}


