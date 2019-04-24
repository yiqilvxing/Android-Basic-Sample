package com.cnitr.cn.entity;

import java.util.List;

/**
 * Created by YangChen on 2018/11/1.
 */

public class HeCitySearchEntity {

    private List<HeCityBase> HeWeather6;

    public List<HeCityBase> getHeWeather6() {
        return HeWeather6;
    }

    public void setHeWeather6(List<HeCityBase> heWeather6) {
        HeWeather6 = heWeather6;
    }

    public static class HeCityBase {
        private List<Basic> basic;

        public List<Basic> getBasic() {
            return basic;
        }

        public void setBasic(List<Basic> basic) {
            this.basic = basic;
        }

        public static class Basic {
            private String cid;
            private String location;
            private String parent_city;
            private String admin_area;
            private String cnty;
            private String lat;
            private String lon;

            public String getCid() {
                return cid;
            }

            public void setCid(String cid) {
                this.cid = cid;
            }

            public String getLocation() {
                return location;
            }

            public void setLocation(String location) {
                this.location = location;
            }

            public String getParent_city() {
                return parent_city;
            }

            public void setParent_city(String parent_city) {
                this.parent_city = parent_city;
            }

            public String getAdmin_area() {
                return admin_area;
            }

            public void setAdmin_area(String admin_area) {
                this.admin_area = admin_area;
            }

            public String getCnty() {
                return cnty;
            }

            public void setCnty(String cnty) {
                this.cnty = cnty;
            }

            public String getLat() {
                return lat;
            }

            public void setLat(String lat) {
                this.lat = lat;
            }

            public String getLon() {
                return lon;
            }

            public void setLon(String lon) {
                this.lon = lon;
            }
        }
    }


}
