package com.jiaotong.aiton.myapplication.mode;

/**
 * Created by zjb on 2015/12/24.
 */
public class BookingCarInfo {
    String thePrice;
    String AddPrice;
    String carType;

    public String getThePrice() {
        return thePrice;
    }

    public void setThePrice(String thePrice) {
        this.thePrice = thePrice;
    }

    public String getAddPrice() {
        return AddPrice;
    }

    public void setAddPrice(String addPrice) {
        AddPrice = addPrice;
    }

    public String getCarType() {
        return carType;
    }

    public void setCarType(String carType) {
        this.carType = carType;
    }

    public BookingCarInfo(String thePrice, String addPrice, String carType) {
        this.thePrice = thePrice;
        AddPrice = addPrice;
        this.carType = carType;
    }

}
