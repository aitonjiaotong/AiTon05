package com.jiaotong.aiton.myapplication.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jiaotong.aiton.myapplication.R;
import com.jiaotong.aiton.myapplication.mode.SuggestInfo;
import com.jiaotong.aiton.myapplication.utils.Constants;

import java.util.ArrayList;
import java.util.List;

public class SelectLocationActivity extends AppCompatActivity implements View.OnClickListener, OnGetGeoCoderResultListener
{

    private ImageView mGetOnAddress;
    private MapView mBmapView;
    private BaiduMap mBaiduMap;
    private ImageView mBack;
    private double mLatitude;
    private double mLongitude;
    private ListView mListViewSuggest;
    private List<SuggestInfo> mListViewSuggestData = new ArrayList<SuggestInfo>();
    private ListViewSuggestAdapter mAdapter;
    private ImageView mManualLocation;
    private GeoCoder mGeoCoder;
    private double mNewLatitude;
    private double mNewLongitude;
    private double[] mDataLatLng;
    private TextView mChooseCity;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        initIntent();
        initUI();
        setOnClick();
    }

    private void initIntent()
    {
        Intent intent = getIntent();
        if (intent != null)
        {
            mLatitude = intent.getDoubleExtra(Constants.MapIntentKey.LATITUDE_KEY, -1);
            mLongitude = intent.getDoubleExtra(Constants.MapIntentKey.LONGITUDE_KEY, -1);
            mNewLatitude = intent.getDoubleExtra(Constants.MapIntentKey.NEW_LATITUDE_KEY, -1);
            mNewLongitude = intent.getDoubleExtra(Constants.MapIntentKey.NEW_LONGITUDE_KEY, -1);
        }
    }

    private void initUI()
    {
        initLocationImage();//初始化定位图标的位置
        initBaiduMap();
        initToCurrLocation();//初始化当前的定位
        initListView();
    }


    private void initToCurrLocation()
    {
        LatLng latLng = new LatLng(mNewLatitude, mNewLongitude);
        MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
        mBaiduMap.animateMapStatus(msu);

        /**------添加初始定位时的位置图标-----------*/
        //定义Maker坐标点
        LatLng point = new LatLng(mLatitude, mLongitude);
        //构建Marker图标
        BitmapDescriptor bitmap = BitmapDescriptorFactory.fromResource(R.mipmap.mine_location);
        //构建MarkerOption，用于在地图上添加Marker
        OverlayOptions option = new MarkerOptions()
                .position(point)
                .icon(bitmap);
        //在地图上添加Marker，并显示
        mBaiduMap.addOverlay(option);

        // 初始化搜索模块，注册事件监听------***地理反编码***------
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(this);
        /**----地图初始化后对坐标进行反地理编码，以此获得百度API建议的周边Poi信息----*/
        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(mNewLatitude, mNewLongitude)));
    }

    private void setOnClick()
    {
        mBack.setOnClickListener(this);
        mManualLocation.setOnClickListener(this);
        mChooseCity.setOnClickListener(this);
    }

    private void initListView()
    {
        mListViewSuggest = (ListView) findViewById(R.id.lv_search_suggest);
        mAdapter = new ListViewSuggestAdapter();
        mListViewSuggest.setAdapter(mAdapter);
        mListViewSuggest.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                if(mListViewSuggestData != null && mListViewSuggestData.size()>0)
                {
                    LatLng latLng = mListViewSuggestData.get(position).getLatLng();
                    mDataLatLng = new double[]{latLng.latitude, latLng.longitude};
                }
                Intent data = new Intent();
                data.putExtra(Constants.MapIntentKey.DATA_LATLNG_KEY,mDataLatLng);
                setResult(Constants.MapIntentKey.RESULT_CODE, data);
                finish();
            }
        });
    }

    private void initLocationImage()
    {
        /**----初始化定位图标，使其底部尖角处位于中心点位置----*/
        mGetOnAddress = (ImageView) findViewById(R.id.iv_get_on_address_icon);
        mGetOnAddress.measure(0, 0);
        int mManualLocationToInitMeasuredHeight = mGetOnAddress.getMeasuredHeight();
        mGetOnAddress.setY(-(mManualLocationToInitMeasuredHeight / 2));

        mBack = (ImageView) findViewById(R.id.iv_back);
        mManualLocation = (ImageView) findViewById(R.id.iv_btn_manual_location);
        mChooseCity = (TextView) findViewById(R.id.tv_city_name);
    }


    private void initBaiduMap()
    {
        mBmapView = (MapView) findViewById(R.id.bmapView);
        mBaiduMap = mBmapView.getMap();
        //构造一个更新地图的msu对象，然后设置该对象为缩放等级(比例尺)，最后设置地图状态。
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
        mBaiduMap.setMapStatus(msu);
        //隐藏地图上缩放按钮的控件
        hideZoomView(mBmapView);
        //设置地图滑动状态界面的监听
        setMapStatusChangeListener();
    }

    /**
     * 地图状态改变监听回调
     */
    private void setMapStatusChangeListener()
    {
        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener()
        {
            public void onMapStatusChangeStart(MapStatus mapStatus)
            {
            }

            public void onMapStatusChange(MapStatus mapStatus)
            {
            }

            public void onMapStatusChangeFinish(MapStatus mapStatus)
            {
                updateMapState(mapStatus);
            }
        });
    }

    /**
     * 更新地图移动后的状态
     */
    private void updateMapState(MapStatus status)
    {
        LatLng centerLatLng = status.target;
        /**----地图移动后获取的新坐标进行反地理编码----*/
        // 反Geo搜索
        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(centerLatLng));
    }

    /**
     * 设置逆地理编码查询回调接口【实现界面中心点不变，地图移动后可动态获得地图中心点地理位置信息】
     */
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult)
    {
    }

    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult)
    {
        mListViewSuggestData.clear();
        List<PoiInfo> poiList = reverseGeoCodeResult.getPoiList();
        if(poiList != null && poiList.size()>0)
        {
            for (int i = 0; i < poiList.size(); i++)
            {
                String name = poiList.get(i).name;
                String address = poiList.get(i).address;
                LatLng latLng = poiList.get(i).location;
                mListViewSuggestData.add(new SuggestInfo(name, address,latLng));
            }
        }
        mAdapter.notifyDataSetChanged();
        mListViewSuggest.smoothScrollToPosition(0);
    }

    /**
     * 隐藏缩放控件
     */
    private void hideZoomView(MapView mapView)
    {
        // 隐藏缩放控件
        int childCount = mapView.getChildCount();
        View zoom = null;
        for (int i = 0; i < childCount; i++)
        {
            View child = mapView.getChildAt(i);
            if (child instanceof ZoomControls)
            {
                zoom = child;
                break;
            }
        }
        zoom.setVisibility(View.GONE);
    }


    class ListViewSuggestAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            return mListViewSuggestData.size();
        }

        @Override
        public Object getItem(int position)
        {
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            View layout = getLayoutInflater().inflate(R.layout.suggest_listview_item, null);
            TextView address = (TextView) layout.findViewById(R.id.tv_suggest_address);
            TextView street = (TextView) layout.findViewById(R.id.tv_suggest_street);
            final ImageView favorites = (ImageView) layout.findViewById(R.id.iv_suggest_favorites);
            favorites.setOnClickListener(new View.OnClickListener()
            {
                public void onClick(View v)
                {
                    Toast.makeText(SelectLocationActivity.this, "地址收藏成功", Toast.LENGTH_SHORT).show();
                    favorites.setImageResource(R.mipmap.favorites_normal1);
                }
            });
            if (mListViewSuggestData != null && mListViewSuggestData.size() > 0)
            {
                if (position == 0)
                {
                    address.setTextColor(getResources().getColor(R.color.aiton_basic_deep_color));
                    favorites.setVisibility(View.GONE);
                }
                address.setText(mListViewSuggestData.get(position).getAddress());
                street.setText(mListViewSuggestData.get(position).getStreet());
            }

            return layout;
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
    }

    @Override
    public void onStop()
    {
        super.onStop();
        //停止定位图层
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mBmapView.onDestroy();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mBmapView.onResume();
    }

    @Override
    public void onPause()
    {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mBmapView.onPause();
    }


    @Override
    public void onClick(View v)
    {
        Intent intent = new Intent();
        switch (v.getId())
        {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_btn_manual_location:
                LatLng latLng = new LatLng(mLatitude, mLongitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                break;
            case R.id.tv_city_name:
                intent.setClass(SelectLocationActivity.this,LetterIndexActivity.class);
                startActivityForResult(intent, Constants.IntentKey.CHOOSE_CITY_KEY);
                break;
        }
    }
}
