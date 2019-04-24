package com.cnitr.cn.service;

import com.cnitr.cn.MyApplication;
import com.cnitr.cn.entity.UserEntity;

/**
 * Created by YangChen on 2018/8/10.
 */

public class UserDao {

    private static UserDao instance;

    private UserDao() {

    }

    public static UserDao getInstance() {
        if (instance == null) {
            instance = new UserDao();
        }
        return instance;
    }

    public UserEntity getUser() {
        UserEntity result;
        try {
            result = MyApplication.getDb().findFirst(UserEntity.class);
        } catch (Exception e) {
            result = null;
        }
        return result;

    }

    public boolean saveUser(UserEntity entity) {
        boolean result = true;
        try {
            MyApplication.getDb().saveOrUpdate(entity);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }


}
