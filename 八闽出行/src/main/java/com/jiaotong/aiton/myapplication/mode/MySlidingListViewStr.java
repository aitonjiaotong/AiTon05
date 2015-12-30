package com.jiaotong.aiton.myapplication.mode;

/**
 * Created by Administrator on 2015/12/21.
 */
public class MySlidingListViewStr {
    int mImageID;
    String mTitle;
    String mSubTitle;
    String mPrice;

    public MySlidingListViewStr()
    {

    }

    public MySlidingListViewStr(int imageID, String title, String subTitle, String price) {
        mImageID = imageID;
        mTitle = title;
        mSubTitle = subTitle;
        mPrice = price;
    }

    public int getImageID() {
        return mImageID;
    }

    public void setImageID(int imageID) {
        mImageID = imageID;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getSubTitle() {
        return mSubTitle;
    }

    public void setSubTitle(String subTitle) {
        mSubTitle = subTitle;
    }

    public String getPrice() {
        return mPrice;
    }

    public void setPrice(String price) {
        mPrice = price;
    }

    @Override
    public String toString() {
        return "MySlidingListViewStr{" +
                "mImageID=" + mImageID +
                ", mTitle='" + mTitle + '\'' +
                ", mSubTitle='" + mSubTitle + '\'' +
                ", mPrice='" + mPrice + '\'' +
                '}';
    }
}
