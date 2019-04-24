package com.cnitr.cn.entity;

import java.util.List;

/**
 * Created by YangChen on 2018/7/5.
 */

public class DiscoveryEntity {

    private List<DiscoveryEntity> results;

    private String _id;
    private String url;
    private String who;
    private String desc;
    private int width;
    private int height;

    public List<DiscoveryEntity> getResults() {
        return results;
    }

    public void setResults(List<DiscoveryEntity> results) {
        this.results = results;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getWho() {
        return who;
    }

    public void setWho(String who) {
        this.who = who;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
