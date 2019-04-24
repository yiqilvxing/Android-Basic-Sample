package com.cnitr.cn.service;

import com.cnitr.cn.MyApplication;
import com.cnitr.cn.entity.MessageEntity;

import java.util.List;

/**
 * Created by YangChen on 2018/8/10.
 */

public class MessageDao {

    private static MessageDao instance;

    private MessageDao() {

    }

    public static MessageDao getInstance() {
        if (instance == null) {
            instance = new MessageDao();
        }
        return instance;
    }

    public List<MessageEntity> getAll() {
        List<MessageEntity> result;
        try {
            result = MyApplication.getDb().findAll(MessageEntity.class);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public boolean save(MessageEntity entity) {
        boolean result = true;
        try {
            MyApplication.getDb().saveOrUpdate(entity);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean add(MessageEntity entity) {
        boolean result = true;
        try {
            MyApplication.getDb().saveBindingId(entity);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean delete(MessageEntity entity) {
        boolean result = true;
        try {
            MyApplication.getDb().delete(entity);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }


}
