package com.cnitr.cn.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by YangChen on 2018/7/5.
 */

@Table(name = "message")
public class MessageEntity implements Serializable {

    @Column(name = "_id", isId = true)
    private int _id;

    // 消息
    @Column(name = "message")
    private String message;

    // 时间
    @Column(name = "time")
    private long time;

    // 已读
    @Column(name = "read")
    private boolean read;

    public int get_id() {
        return _id;
    }

    public void set_id(int _id) {
        this._id = _id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }
}


