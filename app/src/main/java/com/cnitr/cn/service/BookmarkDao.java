package com.cnitr.cn.service;

import com.cnitr.cn.MyApplication;
import com.cnitr.cn.entity.CommonWebEntity;

import java.util.List;

/**
 * Created by YangChen on 2018/8/10.
 */

public class BookmarkDao {

    private static BookmarkDao instance;

    private BookmarkDao() {

    }

    public static BookmarkDao getInstance() {
        if (instance == null) {
            instance = new BookmarkDao();
        }
        return instance;
    }

    public CommonWebEntity getBookmark(String url) {
        CommonWebEntity result;
        try {
            result = MyApplication.getDb().selector(CommonWebEntity.class).where("url", "=", url).findFirst();
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public List<CommonWebEntity> getBookmarkList() {
        List<CommonWebEntity> result;
        try {
            result = MyApplication.getDb().findAll(CommonWebEntity.class);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public boolean saveBookmark(CommonWebEntity entity) {
        boolean result = true;
        try {
            MyApplication.getDb().save(entity);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean deleteBookmark(int id) {
        boolean result = true;
        try {
            MyApplication.getDb().deleteById(CommonWebEntity.class, id);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }


}
