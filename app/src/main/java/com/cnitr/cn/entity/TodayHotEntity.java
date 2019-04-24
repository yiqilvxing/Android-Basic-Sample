package com.cnitr.cn.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by YangChen on 2018/11/16.
 */

public class TodayHotEntity implements Serializable{

    private List<WechatItem.ResultBean.ListBean> hotNewsList;

    public TodayHotEntity(List<WechatItem.ResultBean.ListBean> hotNewsList) {
        this.hotNewsList = hotNewsList;
    }

    public List<WechatItem.ResultBean.ListBean> getHotNewsList() {
        return hotNewsList;
    }

    public void setHotNewsList(List<WechatItem.ResultBean.ListBean> hotNewsList) {
        this.hotNewsList = hotNewsList;
    }
}
