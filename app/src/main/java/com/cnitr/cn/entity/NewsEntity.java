package com.cnitr.cn.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by YangChen on 2018/7/5.
 */

@Table(name = "news")
public class NewsEntity implements Serializable {

    @Column(name = "_id", isId = true)
    private int _id;

    @Column(name = "id")
    private int id;

    // 标题名称
    @Column(name = "title")
    private String title;

    // 封面缩略图地址
    @Column(name = "cover")
    private String cover;

    // 链接地址
    @Column(name = "link")
    private String link;

    // 浏览量
    @Column(name = "visitCount")
    private int visitCount;

    @Column(name = "publisher")
    private String publisher;

    @Column(name = "details")
    private String details;

    @Column(name = "gmtCreate")
    private String gmtCreate;

    @Column(name = "detail")
    private String detail;

    @Column(name = "isDel")
    private int isDel;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getVisitCount() {
        return visitCount;
    }

    public void setVisitCount(int visitCount) {
        this.visitCount = visitCount;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public String getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public int getIsDel() {
        return isDel;
    }

    public void setIsDel(int isDel) {
        this.isDel = isDel;
    }
}


