package com.cnitr.cn.entity;

import java.util.List;

/**
 * Created by YangChen on 2018/7/5.
 */

public class ExpressEntity {

    private String expTextName;
    private String mailNo;
    private boolean flag;
    private int status;
    private List<Express> data;

    public String getExpTextName() {
        return expTextName;
    }

    public void setExpTextName(String expTextName) {
        this.expTextName = expTextName;
    }

    public String getMailNo() {
        return mailNo;
    }

    public void setMailNo(String mailNo) {
        this.mailNo = mailNo;
    }

    public boolean isFlag() {
        return flag;
    }

    public void setFlag(boolean flag) {
        this.flag = flag;
    }

    public List<Express> getData() {
        return data;
    }

    public void setData(List<Express> data) {
        this.data = data;
    }

    public static class Express{

        private String context;

        private String time;

        public String getContext() {
            return context;
        }

        public void setContext(String context) {
            this.context = context;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }
    }

}
