package com.cnitr.cn.entity;

import org.xutils.db.annotation.Column;
import org.xutils.db.annotation.Table;

import java.io.Serializable;

/**
 * Created by YangChen on 2018/11/1.
 */

@Table(name = "note")
public class NoteEntity implements Serializable {

    @Column(name = "_id", isId = true)
    private int _id;

    @Column(name = "id")
    private int id;

    // 备忘内容
    @Column(name = "content")
    private String content;

    // 时间
    @Column(name = "time")
    private long time;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
