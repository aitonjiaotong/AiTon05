package com.jiaotong.aiton.myapplication.Activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;

import com.jiaotong.aiton.myapplication.Fragment.GiftBannerFragment;
import com.jiaotong.aiton.myapplication.R;

public class GiftActivity extends FragmentActivity implements View.OnClickListener
{

    private ViewPager mViewPager;
    private MyViewPagerAdapter mViewPagerAdapter;
    private int[] mImageId = new int[]{R.mipmap.aa, R.mipmap.ab, R.mipmap.ac, R.mipmap.ad, R.mipmap.ae};
    private RelativeLayout mClose;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gift);
        initUI();
        setOnclick();
    }

    private void initUI()
    {
        mViewPager = (ViewPager) findViewById(R.id.vp_gift_ad);
        getFragmentManager();
        mViewPagerAdapter = new MyViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(mViewPagerAdapter);
        mClose = (RelativeLayout) findViewById(R.id.rl_close);
    }

    private void setOnclick()
    {
        mClose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.rl_close:
                finish();
                break;
        }
    }

    class MyViewPagerAdapter extends FragmentPagerAdapter
    {

        public MyViewPagerAdapter(FragmentManager fm)
        {
            super(fm);
        }

        @Override
        public Fragment getItem(int position)
        {
            return new GiftBannerFragment().newInstance(mImageId[position]);
        }

        @Override
        public int getCount()
        {
            return mImageId.length;
        }
    }


}
