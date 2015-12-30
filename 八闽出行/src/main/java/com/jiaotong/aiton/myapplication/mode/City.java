package com.jiaotong.aiton.myapplication.mode;

import java.util.List;

/**
 * Created by Administrator on 2015/12/29.
 */
public class City
{

    /**
     * city_id : 2419
     * centerx : 116.407413
     * district_online : 1
     * centery : 39.904214
     * name : 北京
     * province : 北京
     * zone_online : 1
     * pinyin : beijing
     */

    private List<HotcityEntity> hotcity;
    /**
     * city_id : 152900
     * centerx : 105.73546
     * district_online : 1
     * centery : 38.857855
     * name : 阿拉善盟
     * province : 内蒙古
     * zone_online : 1
     * pinyin : alashanmeng
     */

    private List<AllcityEntity> allcity;

    public void setHotcity(List<HotcityEntity> hotcity)
    {
        this.hotcity = hotcity;
    }

    public void setAllcity(List<AllcityEntity> allcity)
    {
        this.allcity = allcity;
    }

    public List<HotcityEntity> getHotcity()
    {
        return hotcity;
    }

    public List<AllcityEntity> getAllcity()
    {
        return allcity;
    }

    public static class HotcityEntity
    {
        private String city_id;
        private String centerx;
        private String district_online;
        private String centery;
        private String name;
        private String province;
        private String zone_online;
        private String pinyin;

        public void setCity_id(String city_id)
        {
            this.city_id = city_id;
        }

        public void setCenterx(String centerx)
        {
            this.centerx = centerx;
        }

        public void setDistrict_online(String district_online)
        {
            this.district_online = district_online;
        }

        public void setCentery(String centery)
        {
            this.centery = centery;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public void setProvince(String province)
        {
            this.province = province;
        }

        public void setZone_online(String zone_online)
        {
            this.zone_online = zone_online;
        }

        public void setPinyin(String pinyin)
        {
            this.pinyin = pinyin;
        }

        public String getCity_id()
        {
            return city_id;
        }

        public String getCenterx()
        {
            return centerx;
        }

        public String getDistrict_online()
        {
            return district_online;
        }

        public String getCentery()
        {
            return centery;
        }

        public String getName()
        {
            return name;
        }

        public String getProvince()
        {
            return province;
        }

        public String getZone_online()
        {
            return zone_online;
        }

        public String getPinyin()
        {
            return pinyin;
        }
    }

    public static class AllcityEntity
    {
        private String city_id;
        private double centerx;
        private String district_online;
        private double centery;
        private String name;
        private String province;
        private String zone_online;
        private String pinyin;

        public void setCity_id(String city_id)
        {
            this.city_id = city_id;
        }

        public void setCenterx(double centerx)
        {
            this.centerx = centerx;
        }

        public void setDistrict_online(String district_online)
        {
            this.district_online = district_online;
        }

        public void setCentery(double centery)
        {
            this.centery = centery;
        }

        public void setName(String name)
        {
            this.name = name;
        }

        public void setProvince(String province)
        {
            this.province = province;
        }

        public void setZone_online(String zone_online)
        {
            this.zone_online = zone_online;
        }

        public void setPinyin(String pinyin)
        {
            this.pinyin = pinyin;
        }

        public String getCity_id()
        {
            return city_id;
        }

        public double getCenterx()
        {
            return centerx;
        }

        public String getDistrict_online()
        {
            return district_online;
        }

        public double getCentery()
        {
            return centery;
        }

        public String getName()
        {
            return name;
        }

        public String getProvince()
        {
            return province;
        }

        public String getZone_online()
        {
            return zone_online;
        }

        public String getPinyin()
        {
            return pinyin;
        }
    }
}
