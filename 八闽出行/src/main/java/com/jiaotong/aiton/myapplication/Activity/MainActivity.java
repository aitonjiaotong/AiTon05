package com.jiaotong.aiton.myapplication.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ZoomControls;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.jiaotong.aiton.myapplication.R;
import com.jiaotong.aiton.myapplication.custom.SlidingMenu;
import com.jiaotong.aiton.myapplication.utils.Constants;
import com.jiaotong.aiton.myapplication.utils.GpsUtils;

import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, OnGetGeoCoderResultListener
{
    private boolean isTouch = true;
    private RelativeLayout mMain_mine;
    private MapView mBmapView;
    private BaiduMap mBaiduMap;
    //定位相关
    public LocationClient mLocationClient = null;
    public BDLocationListener myListener = new MyLocationListener();
    private boolean isFirstIn = true;
    private double mLatitude;
    private double mLongitude;
    private String addressStr;

    //自定义定位图标
    private BitmapDescriptor mIconLocation;
    private SlidingMenu mMenu;
    private RelativeLayout mCover_cover;
    private TextView mOrderDetail;
    private GeoCoder mGeoCoder;
    private ImageView mManualLocationToInit;
    private ImageView mGuideIcon;
    private Dialog mDialog;

    //地图每次移动后中心点的位置
    private LatLng mCenterLatLng;
    private TextView mOrderAddressDetail;

    private LinearLayout ll_order_detail;
    private String mName;
    private String mAddress;
    private UiSettings mUiSettings;
    private double mNewLatitude;
    private double mNewLongitude;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initLocation();
        initUI();
        mMenu = (SlidingMenu) findViewById(R.id.id_menu);
    }


    private void initLocation()
    {
        mLocationClient = new LocationClient(getApplicationContext());     //声明LocationClient类
        mLocationClient.registerLocationListener(myListener);    //注册监听函数
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 5000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认false，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        mLocationClient.setLocOption(option);
        //初始化定位的图标
        mIconLocation = BitmapDescriptorFactory.fromResource(R.mipmap.mine_location);
    }

    private void initUI()
    {
        findViewById(R.id.recharge).setOnClickListener(this);
        findViewById(R.id.login_info_linear).setOnClickListener(this);
        /**----底部Tab选项的点击事件----*/
        findViewById(R.id.booking_car).setOnClickListener(this);
        findViewById(R.id.call_car).setOnClickListener(this);
        findViewById(R.id.air_car).setOnClickListener(this);
        findViewById(R.id.iv_btn_gift).setOnClickListener(this);

        /**----手动定位到初始位置----*/
        mManualLocationToInit = (ImageView) findViewById(R.id.iv_btn_manual_location);
        mManualLocationToInit.setOnClickListener(this);

        /**----当前定位信息显示布局----*/
        ll_order_detail = (LinearLayout) findViewById(R.id.ll_order_detail);
        ll_order_detail.setOnClickListener(this);//设置跳转到手动选择定位位置页面
        mOrderAddressDetail = (TextView) findViewById(R.id.tv_address_dateil);//地图定位后所显示的较为具体的地理位置信息
        mOrderDetail = (TextView) findViewById(R.id.tv_order_detail);//地图定位后所显示的地理位置（TextView）

        /**----初始化定位图标，使其底部尖角处位于中心点位置----*/
        mGuideIcon = (ImageView) findViewById(R.id.iv_guide_icon);
        mGuideIcon.measure(0, 0);
        int mManualLocationToInitMeasuredHeight = mGuideIcon.getMeasuredHeight();
        mGuideIcon.setY(-(mManualLocationToInitMeasuredHeight / 2));
        findViewById(R.id.ll_order_detail).setY(-(mManualLocationToInitMeasuredHeight / 2));

        /**----侧滑菜单相关----*/
        final RelativeLayout cover = (RelativeLayout) findViewById(R.id.cover);
        mCover_cover = (RelativeLayout) findViewById(R.id.cover_cover);
        SlidingMenu id_menu = (SlidingMenu) findViewById(R.id.id_menu);

        /**----百度地图相关----*/
        mBmapView = (MapView) findViewById(R.id.bmapView);
        View v = mBmapView.getChildAt(0);//这个view实际上就是我们看见的绘制在表面的地图图层
        //设置地图拦截scrollview触摸监听
        v.setOnTouchListener(new View.OnTouchListener()
        {
            public boolean onTouch(View v, MotionEvent event)
            {
                if (event.getAction() == MotionEvent.ACTION_UP)
                {
                    mMenu.requestDisallowInterceptTouchEvent(false);
                } else
                {
                    mMenu.requestDisallowInterceptTouchEvent(true);
                }
                return false;
            }
        });
        mBaiduMap = mBmapView.getMap();
        //构造一个更新地图的msu对象，然后设置该对象为缩放等级(比例尺)，最后设置地图状态。
        MapStatusUpdate msu = MapStatusUpdateFactory.zoomTo(18.0f);
        mBaiduMap.setMapStatus(msu);
        mMain_mine = (RelativeLayout) findViewById(R.id.main_mine_rela);
        mMain_mine.setOnClickListener(this);

        /**----侧滑菜单逻辑功能实现相关----*/
        //slidingmenu空白处返回监听
        mCover_cover.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                mMenu.toggle();
                isTouch = true;
                mCover_cover.setVisibility(View.GONE);
            }
        });
        //scrollview控制布局覆盖
        id_menu.setOnSlidingMenuOnSrollChangeListener(new SlidingMenu.SlidingMenuOnScrollChangeListener()
        {
            @Override
            public void onSlidingMenuScrollChange(int l, int t, int oldl, int oldt)
            {
                boolean isOpen = mMenu.getIsOpen();
                isTouch = !isOpen;
                if (isOpen)
                {
                    mCover_cover.setVisibility(View.VISIBLE);
                } else
                {
                    mCover_cover.setVisibility(View.GONE);
                }
            }
        });
        //覆盖布局拦截baidumap触摸监听
        cover.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return !isTouch;
            }
        });
        //隐藏地图上缩放按钮的控件
        hideZoomView(mBmapView);

        // 初始化搜索模块，注册事件监听------***地理反编码***------
        mGeoCoder = GeoCoder.newInstance();
        mGeoCoder.setOnGetGeoCodeResultListener(this);

        //设置地图滑动状态界面的监听
        setMapStatusChangeListener();

        /**------百度地图UI手势操作相关------*/
        mUiSettings = mBaiduMap.getUiSettings();
        mUiSettings.setOverlookingGesturesEnabled(false);//取消俯视手势操作
        mUiSettings.setRotateGesturesEnabled(false);//取消旋转手势操作
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
                ll_order_detail.setVisibility(View.GONE);
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
        mCenterLatLng = status.target;
        /**获取新的经纬度*/
        mNewLatitude = mCenterLatLng.latitude;
        mNewLongitude = mCenterLatLng.longitude;

        /**----地图移动后获取的新坐标进行反地理编码----*/
        // 反Geo搜索
        mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(mCenterLatLng));
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


    @Override
    public void onStart()
    {
        super.onStart();
        // 开启定位图层
        mBaiduMap.setMyLocationEnabled(true);
        //开启定位
        if (!mLocationClient.isStarted())
        {
            mLocationClient.start();
        }
    }

    @Override
    public void onStop()
    {
        super.onStop();
        //停止定位图层
        mBaiduMap.setMyLocationEnabled(false);
        //停止定位
        mLocationClient.stop();
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
            case R.id.recharge:
                intent.setClass(MainActivity.this,ReCharge.class);
                startActivity(intent);
                break;
            case R.id.login_info_linear:
                intent.setClass(MainActivity.this, MineInfo.class);
                startActivity(intent);
                break;
            case R.id.main_mine_rela:
                isTouch = false;
                mMenu.toggle();
                mCover_cover.setVisibility(View.VISIBLE);
                break;
            case R.id.booking_car:
                intent.setClass(MainActivity.this, BookingCar.class);
                startActivity(intent);
                break;
            case R.id.call_car:
                intent.setClass(MainActivity.this, CallCar.class);
                startActivity(intent);
                break;
            case R.id.air_car:
                intent.setClass(MainActivity.this, AirCar.class);
                startActivity(intent);
                break;
            case R.id.iv_btn_manual_location:
                if (!GpsUtils.gPSIsOPen(MainActivity.this))
                {
                    mDialog = new Dialog(MainActivity.this);
                    mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    mDialog.setContentView(R.layout.layout_gps_dialog);
                    TextView mPositive = (TextView) mDialog.findViewById(R.id.tv_btn_setting);
                    TextView mNegative = (TextView) mDialog.findViewById(R.id.tv_btn_cancel);
                    mPositive.setOnClickListener(this);
                    mNegative.setOnClickListener(this);

                    mDialog.setCanceledOnTouchOutside(false);
                    mDialog.show();
                }
                LatLng latLng = new LatLng(mLatitude, mLongitude);
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                break;
            case R.id.tv_btn_setting:
                mDialog.dismiss();
                // 跳转到GPS相关设置界面
                GpsUtils.EnterGPSInterface(MainActivity.this);
                break;
            case R.id.tv_btn_cancel:
                mDialog.dismiss();
                break;
            case R.id.iv_btn_gift:
                intent.setClass(MainActivity.this, GiftActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_order_detail:
                /**-----将定位的信息传递到SelectLocationActivity-----*/
                intent.setClass(MainActivity.this, SelectLocationActivity.class);
                intent.putExtra(Constants.MapIntentKey.LATITUDE_KEY, mLatitude);
                intent.putExtra(Constants.MapIntentKey.LONGITUDE_KEY, mLongitude);
                intent.putExtra(Constants.MapIntentKey.NEW_LATITUDE_KEY, mNewLatitude);
                intent.putExtra(Constants.MapIntentKey.NEW_LONGITUDE_KEY, mNewLongitude);
                startActivityForResult(intent, Constants.MapIntentKey.REQUEST_CODE);
                break;

        }
    }

    /**
     * 百度地图定位的监听回调
     */
    private class MyLocationListener implements BDLocationListener
    {
        @Override
        public void onReceiveLocation(BDLocation bdLocation)
        {
            // 构造定位数据
            MyLocationData data = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())//获得半径
                    .latitude(bdLocation.getLatitude())//获得经度
                    .longitude(bdLocation.getLongitude())//获得纬度
                    .build();
            //设置定位数据
            mBaiduMap.setMyLocationData(data);

            //设置自定义图标
            MyLocationConfiguration config = new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL, true, mIconLocation);
            mBaiduMap.setMyLocationConfigeration(config);

            //初始化经纬度
            mLatitude = bdLocation.getLatitude();
            mLongitude = bdLocation.getLongitude();

            //第一次进入，定位到所在位置
            if (isFirstIn)
            {
                LatLng latLng = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
                MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
                mBaiduMap.animateMapStatus(msu);
                isFirstIn = false;
                addressStr = bdLocation.getAddrStr();
                //初始定位后将获取的地理文字位置设置到
                mOrderDetail.setText(addressStr);

                // 反Geo搜索
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(new LatLng(mLatitude, mLongitude)));
            }
            ll_order_detail.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 设置逆地理编码查询回调接口【实现界面中心点不变，地图移动后可动态获得地图中心点地理位置信息】
     */
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult)
    {
    }

    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult)
    {
        if (reverseGeoCodeResult != null)
        {
            mAddress = reverseGeoCodeResult.getAddress();
            List<PoiInfo> poiList = reverseGeoCodeResult.getPoiList();
            if (poiList != null && poiList.size() > 0)
            {
                mName = poiList.get(0).name;
                mOrderDetail.setText(mAddress);
                mOrderAddressDetail.setText(mName);
                ll_order_detail.setVisibility(View.VISIBLE);
            } else
            {
                Toast.makeText(MainActivity.this, "该区域不在范围内，请重新选择！", Toast.LENGTH_SHORT).show();
                ll_order_detail.setVisibility(View.GONE);
                return;
            }
        }


    }

    /**
     * SelectLocationAcitivty【选择上车地点界面】回跳后的数据处理
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if(data != null)
        {
            double[] dataLatlng = data.getDoubleArrayExtra(Constants.MapIntentKey.DATA_LATLNG_KEY);
            double latitude = dataLatlng[0];
            double longitude = dataLatlng[1];
            LatLng latLng = new LatLng(latitude, longitude);
            MapStatusUpdate msu = MapStatusUpdateFactory.newLatLng(latLng);
            mBaiduMap.animateMapStatus(msu);
        }
    }
}
