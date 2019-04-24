package com.cnitr.cn.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by YangChen on 2018/7/5.
 */

@Table(name = "bookmark")
public class CommonWebEntity implements Serializable {

    @Column(name = "_id", isId = true)
    private int _id;

    // 标题
    @Column(name = "title")
    private String title;

    // 地址
    @Column(name = "url")
    private String url;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}


