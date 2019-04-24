package com.cnitr.cn.service;

import com.cnitr.cn.MyApplication;
import com.cnitr.cn.entity.BannerEntity;

import java.util.List;

/**
 * Created by YangChen on 2018/8/10.
 */

public class BannerDao {

    private static BannerDao instance;

    private BannerDao() {

    }

    public static BannerDao getInstance() {
        if (instance == null) {
            instance = new BannerDao();
        }
        return instance;
    }

    public List<BannerEntity> getAll() {
        List<BannerEntity> result;
        try {
            result = MyApplication.getDb().findAll(BannerEntity.class);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public boolean save(BannerEntity entity) {
        boolean result = true;
        try {
            MyApplication.getDb().saveOrUpdate(entity);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean add(BannerEntity entity) {
        boolean result = true;
        try {
            MyApplication.getDb().saveBindingId(entity);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean delete(BannerEntity entity) {
        boolean result = true;
        try {
            MyApplication.getDb().delete(entity);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean deleteAll() {
        boolean result = true;
        try {
            MyApplication.getDb().delete(BannerEntity.class);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }


}
