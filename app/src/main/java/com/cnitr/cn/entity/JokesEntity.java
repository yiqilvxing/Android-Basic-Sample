package com.cnitr.cn.entity;

import java.util.List;

/**
 * Created by YangChen on 2018/11/6.
 */

public class JokesEntity {

    private JokesBaseEntity result;

    public JokesBaseEntity getResult() {
        return result;
    }

    public void setResult(JokesBaseEntity result) {
        this.result = result;
    }

    public static class JokesBaseEntity {

        private List<Jokes> data;

        public List<Jokes> getData() {
            return data;
        }

        public void setData(List<Jokes> data) {
            this.data = data;
        }

        public static class Jokes {
            private String content;
            private String hashId;
            private String unixtime;
            private String updatetime;

            public String getContent() {
                return content;
            }

            public void setContent(String content) {
                this.content = content;
            }

            public String getHashId() {
                return hashId;
            }

            public void setHashId(String hashId) {
                this.hashId = hashId;
            }

            public String getUnixtime() {
                return unixtime;
            }

            public void setUnixtime(String unixtime) {
                this.unixtime = unixtime;
            }

            public String getUpdatetime() {
                return updatetime;
            }

            public void setUpdatetime(String updatetime) {
                this.updatetime = updatetime;
            }
        }
    }

}
