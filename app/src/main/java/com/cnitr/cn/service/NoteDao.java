package com.cnitr.cn.service;

import com.cnitr.cn.MyApplication;
import com.cnitr.cn.entity.NoteEntity;

import java.util.List;

/**
 * Created by YangChen on 2018/8/10.
 */

public class NoteDao {

    private static NoteDao instance;

    private NoteDao() {

    }

    public static NoteDao getInstance() {
        if (instance == null) {
            instance = new NoteDao();
        }
        return instance;
    }

    public List<NoteEntity> getAll() {
        List<NoteEntity> result;
        try {
            result = MyApplication.getDb().findAll(NoteEntity.class);
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public boolean save(NoteEntity entity) {
        boolean result = true;
        try {
            MyApplication.getDb().saveOrUpdate(entity);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean add(NoteEntity entity) {
        boolean result = true;
        try {
            MyApplication.getDb().saveBindingId(entity);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }

    public boolean delete(NoteEntity entity) {
        boolean result = true;
        try {
            MyApplication.getDb().delete(entity);
        } catch (Exception e) {
            result = false;
        }
        return result;
    }


}
