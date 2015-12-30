package com.jiaotong.aiton.myapplication.mode;

import com.baidu.mapapi.model.LatLng;

/**
 * Created by Administrator on 2015/12/28.
 */
public class SuggestInfo
{
    String Address;
    String Street;
    LatLng latLng;

    public SuggestInfo(String address, String street, LatLng latLng)
    {
        this.Address = address;
        this.Street = street;
        this.latLng = latLng;
    }

    public SuggestInfo()
    {
    }


    @Override
    public String toString()
    {
        return "SuggestInfo{" +
                "Address='" + Address + '\'' +
                ", Street='" + Street + '\'' +
                ", latLng=" + latLng +
                '}';
    }

    public LatLng getLatLng()
    {
        return latLng;
    }

    public void setLatLng(LatLng latLng)
    {
        this.latLng = latLng;
    }

    public String getAddress()
    {
        return Address;
    }

    public void setAddress(String address)
    {
        Address = address;
    }

    public String getStreet()
    {
        return Street;
    }

    public void setStreet(String street)
    {
        Street = street;
    }

}
