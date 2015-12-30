package com.jiaotong.aiton.myapplication.utils;

/**
 * Created by Administrator on 2015/12/28.
 */
public class Constants
{
    /**
     * -------------------Activity 向 Fragment 传值过程中的 KEY---------------
     */
    public static final class Key
    {
        public static final String IMAGEID = "image_id";

    }

    public static final class IntentKey
    {
        public static final int CHOOSE_CITY_KEY = 1;
    }

    /**
     * ------------------百度地图定位界面跳转传值的KEY-------------
     */
    public static final class MapIntentKey
    {
        public static final int REQUEST_CODE = 1;//MainActivity 请求码
        public static final int RESULT_CODE = 1;//MainActivity 请求码

        public static final String LATITUDE_KEY = "Latitude";
        public static final String LONGITUDE_KEY = "Longitude";
        public static final String NEW_LATITUDE_KEY = "newLatitude";
        public static final String NEW_LONGITUDE_KEY = "newLongitude";
        public static final String DATA_LATLNG_KEY = "dataLatLng";
    }
}
