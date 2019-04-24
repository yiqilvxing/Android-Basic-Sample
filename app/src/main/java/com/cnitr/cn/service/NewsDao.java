package com.cnitr.cn.service;

import com.cnitr.cn.MyApplication;
import com.cnitr.cn.entity.NewsEntity;

import java.util.List;

/**
 * Created by YangChen on 2018/8/10.
 */

public class NewsDao {

    private static NewsDao instance;

    private NewsDao() {

    }

    public static NewsDao getInstance() {
        if (instance == null) {
            instance = new NewsDao();
        }
        return instance;
    }

    public List<NewsEntity> get() {
        List<NewsEntity> result;
        try {
            result = MyApplication.getDb().findAll(NewsEntity.class);
        } catch (Exception e) {
            result = null;
        }
        return result;

    }

    public boolean save(NewsEntity entity) {
        boolean result = true;
        try {
            MyApplication.getDb().saveOrUpdate(entity);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }


}
