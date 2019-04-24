package com.cnitr.cn.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Transient;

import java.io.Serializable;

/**
 * Created by YangChen on 2018/11/23.
 */

@Entity(nameInDb = "note")
public class Note implements Serializable {

    @Transient
    private static final long serialVersionUID = 86131465461321L;

    @Id(autoincrement = true)
    private Long id;

    // 备忘内容
    @Property(nameInDb = "content")
    @NotNull
    private String content;

    // 备忘内容
    @Property(nameInDb = "time")
    private long time;

    @Generated(hash = 1573714968)
    public Note(Long id, @NotNull String content, long time) {
        this.id = id;
        this.content = content;
        this.time = time;
    }

    @Generated(hash = 1272611929)
    public Note() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return this.time;
    }

    public void setTime(long time) {
        this.time = time;
    }


}
