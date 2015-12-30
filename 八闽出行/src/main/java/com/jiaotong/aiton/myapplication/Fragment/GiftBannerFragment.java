package com.jiaotong.aiton.myapplication.Fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jiaotong.aiton.myapplication.R;
import com.jiaotong.aiton.myapplication.utils.Constants;

/**
 * A simple {@link Fragment} subclass.
 */
public class GiftBannerFragment extends Fragment
{
    private int imageID;
    private ImageView mBannerImage;

    public GiftBannerFragment()
    {
    }

    public static GiftBannerFragment newInstance(int imageID)
    {
        GiftBannerFragment giftBannerFragment = new GiftBannerFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(Constants.Key.IMAGEID,imageID);
        giftBannerFragment.setArguments(bundle);
        return giftBannerFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        if(getArguments() != null)
        {
            imageID = getArguments().getInt(Constants.Key.IMAGEID);
        }
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View layout = inflater.inflate(R.layout.fragment_gift_banner, null);
        mBannerImage = (ImageView) layout.findViewById(R.id.iv_gift_banner);
        mBannerImage.setImageResource(imageID);
        return layout;
    }


}
