package com.jiaotong.aiton.myapplication.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.jiaotong.aiton.myapplication.R;
import com.jiaotong.aiton.myapplication.WheelTimePicker.DatePicker;
import com.jiaotong.aiton.myapplication.WheelTimePicker.TimePicker;
import com.jiaotong.aiton.myapplication.mode.BookingCarInfo;

import java.util.ArrayList;
import java.util.Calendar;

public class BookingCar extends AppCompatActivity implements View.OnClickListener {
    private Calendar calendar;
    private String selectDate, selectTime;
    //选择时间与当前时间，用于判断用户选择的是否是以前的时间
    private int currentHour, currentMinute, currentDay, selectHour, selectMinute, selectDay;
    private DatePicker dp_test;
    private TimePicker tp_test;
    private TextView tv_ok, tv_cancel;    //确定、取消button

    private TextView[] ActionBarTv = new TextView[3];
    private LinearLayout mVoucher;
    private LinearLayout mEndAddress;

    //待用解析数据。暂用模拟数据
    private ArrayList<BookingCarInfo> mBookingCarInfos = new ArrayList<BookingCarInfo>();
    private RelativeLayout mCall_car_forOther;

    private TextView mName;
    private TextView mPhoneNumber;
    private TextView mFrom;
    private TextView mTo;
    private TextView mData_tv;
    private TextView mTime_tv;

    private int[] carTvID = {R.id.car_tv01, R.id.car_tv02, R.id.car_tv03};
    private int[] carImgID = {R.id.car_img01, R.id.car_img02, R.id.car_img03};
    private int[] carLinearID = {R.id.car_linear01, R.id.car_linear02, R.id.car_linear03};
    private TextView carTv[] = new TextView[3];
    private ImageView carImg[] = new ImageView[3];
    private LinearLayout carLinear[] = new LinearLayout[3];

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if ("Passenger".equals(action)){
                String name = intent.getStringExtra("name");
                String phoneNumber = intent.getStringExtra("phoneNumber");
                mName.setText(name);
                mPhoneNumber.setText(phoneNumber);
            }else if ("usedAddressFrom".equals(action)){
                String usedAddressFromStr = intent.getStringExtra("usedAddressFromStr");
                mFrom.setText(usedAddressFromStr);
            }else if ("usedAddressTo".equals(action)){
                String usedAddressToStr = intent.getStringExtra("usedAddressToStr");
                mTo.setText(usedAddressToStr);
                mTo.setTextColor(getResources().getColor(R.color.black));
            }


        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_car);
        initUI();
        setListener();
        addBookingCarInfos();
        /**
         * 初始化车辆选择
         */
        checkCar(0);
        /**
         * 初始化标题
         */
        setActionBarTv(0);
        /**
         * 初始化时间日期
         */
        initDateAndTime();
    }

    private void addBookingCarInfos() {
        mBookingCarInfos.add(new BookingCarInfo("套餐价¥50.0(含20分钟8公里)", "超出按¥0.5/分钟+¥3.8/公里计费", "大众帕萨特/丰田凯美瑞或类似5座车型"));
        mBookingCarInfos.add(new BookingCarInfo("套餐价¥350.0(含4小时50公里", "超出按¥0.6/分钟+¥4.0/公里计费", "大众帕萨特/丰田凯美瑞或类似5座车型"));
        mBookingCarInfos.add(new BookingCarInfo("套餐价¥500.0(含4小时50公里", "超出按¥0.6/分钟+¥4.0/公里计费", "大众帕萨特/丰田凯美瑞或类似5座车型"));
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void setListener() {
        for (int i = 0; i < ActionBarTv.length; i++) {
            ActionBarTv[i].setOnClickListener(this);
        }
        mCall_car_forOther.setOnClickListener(this);
        findViewById(R.id.bookingCar_back).setOnClickListener(this);
        mName.addTextChangedListener(new NameTextWatcher());
        findViewById(R.id.timeCheck).setOnClickListener(this);
        findViewById(R.id.Collection01).setOnClickListener(this);
        findViewById(R.id.Collection02).setOnClickListener(this);
        for (int i = 0; i < 3; i++) {
            carLinear[i].setOnClickListener(this);
        }
        findViewById(R.id.Estimate_rella).setOnClickListener(this);
        findViewById(R.id.commit).setOnClickListener(this);
    }

    class NameTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                mName.setVisibility(View.VISIBLE);
            } else {
                mName.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private void initUI() {

        mData_tv = (TextView) findViewById(R.id.data_tv);
        mTime_tv = (TextView) findViewById(R.id.time_tv);
        mCall_car_forOther = (RelativeLayout) findViewById(R.id.call_car_forOther);
        mVoucher = (LinearLayout) findViewById(R.id.Voucher);
        mEndAddress = (LinearLayout) findViewById(R.id.endAddress);
        ActionBarTv[0] = (TextView) findViewById(R.id.booking);
        ActionBarTv[1] = (TextView) findViewById(R.id.halfday);
        ActionBarTv[2] = (TextView) findViewById(R.id.wholeday);
        mName = (TextView) findViewById(R.id.name);
        mPhoneNumber = (TextView) findViewById(R.id.phoneNumber);
        for (int i = 0; i < 3; i++) {
            carTv[i] = (TextView) findViewById(carTvID[i]);
            carImg[i] = (ImageView) findViewById(carImgID[i]);
            carLinear[i] = (LinearLayout) findViewById(carLinearID[i]);
        }
        mFrom = (TextView) findViewById(R.id.from);
        mTo = (TextView) findViewById(R.id.to);
    }

    private void initDateAndTime() {
        calendar = Calendar.getInstance();
        selectDate =
                calendar.get(Calendar.MONTH) + "月"
                        + calendar.get(Calendar.DAY_OF_MONTH) + "日  "
                        + DatePicker.getDayOfWeekCN(calendar.get(Calendar.DAY_OF_WEEK));
        //选择时间与当前时间的初始化，用于判断用户选择的是否是以前的时间，如果是，弹出toss提示不能选择过去的时间
        selectDay = currentDay = calendar.get(Calendar.DAY_OF_MONTH);
        selectMinute = currentMinute = calendar.get(Calendar.MINUTE) + 10;
        selectHour = currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        selectTime = currentHour + ":" + ((currentMinute < 10) ? ("0" + currentMinute) : currentMinute);
        mData_tv.setText(selectDate);
        mTime_tv.setText(selectTime);
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.commit:
                if ("输入下车地址为您预估费用".equals(mTo.getText().toString().trim())){
                    setDialog();
                }else{
                    //直接跳转主界面等待叫车
                }
                break;
            case R.id.Estimate_rella:
                intent.setClass(BookingCar.this, EstimatePrice.class);
                startActivity(intent);
                break;
            case R.id.car_linear01:
                checkCar(0);
                break;
            case R.id.car_linear02:
                checkCar(1);
                break;
            case R.id.car_linear03:
                checkCar(2);
                break;
            case R.id.Collection01:
                intent.setClass(BookingCar.this, UsedAddress.class);
                intent.putExtra("where", "from");
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.fade_out);
                break;
            case R.id.Collection02:
                intent.setClass(BookingCar.this, UsedAddress.class);
                intent.putExtra("where", "to");
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.fade_out);
                break;
            case R.id.timeCheck:
                setPopupwindows();
                break;
            case R.id.booking:
                setActionBarTv(0);
                hideVoucherANDshowEndAddress();
                break;
            case R.id.halfday:
                setActionBarTv(1);
                showVoucherANDhideEndAddress();
                break;
            case R.id.wholeday:
                setActionBarTv(2);
                showVoucherANDhideEndAddress();
                break;
            case R.id.call_car_forOther:
                intent.setClass(BookingCar.this, CallCarForOthers.class);
                startActivity(intent);
                break;
            case R.id.bookingCar_back:
                finish();
                break;
        }
    }

    private void setDialog() {
        View commit_dialog = getLayoutInflater().inflate(R.layout.commit_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(BookingCar.this);
        final AlertDialog dialog = builder.setView(commit_dialog)
                .create();
        dialog.setCancelable(false);
        dialog.show();
        commit_dialog.findViewById(R.id.ISee).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void checkCar(int checkCarID) {
        carTv[checkCarID].setTextColor(getResources().getColor(R.color.black));
        carImg[checkCarID].setImageResource(R.mipmap.ucar_check);
        carTv[(checkCarID + 1) % 3].setTextColor(getResources().getColor(R.color.text_color_gray));
        carImg[(checkCarID + 1) % 3].setImageResource(R.mipmap.passenger_msg_unselected);
        carTv[(checkCarID + 2) % 3].setTextColor(getResources().getColor(R.color.text_color_gray));
        carImg[(checkCarID + 2) % 3].setImageResource(R.mipmap.passenger_msg_unselected);
    }

    private void setPopupwindows() {
        View inflate = getLayoutInflater().inflate(R.layout.time_picker_popupwindows, null);
        dp_test = (DatePicker) inflate.findViewById(R.id.dp_test);
        tp_test = (TimePicker) inflate.findViewById(R.id.tp_test);
        tv_ok = (TextView) inflate.findViewById(R.id.tv_ok);
        tv_cancel = (TextView) inflate.findViewById(R.id.tv_cancel);
        //设置滑动改变监听器
        dp_test.setOnChangeListener(dp_onchanghelistener);
        tp_test.setOnChangeListener(tp_onchanghelistener);
        //最后一个参数为true，点击PopupWindow消失,宽必须为match，不然肯呢个会导致布局显示不完全
        final PopupWindow popupWindow = new PopupWindow(inflate, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        //设置外部点击无效
        popupWindow.setOutsideTouchable(false);
        // 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
        BitmapDrawable bitmapDrawable = new BitmapDrawable();
        popupWindow.setBackgroundDrawable(bitmapDrawable);
        popupWindow.showAtLocation(inflate, Gravity.BOTTOM, 0, 0);

        //点击确定
        tv_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if (selectDay == currentDay) {    //在当前日期情况下可能出现选中过去时间的情况
                    if (selectHour < currentHour) {
                        Toast.makeText(getApplicationContext(), "不能选择过去的时间\n        请重新选择", Toast.LENGTH_SHORT).show();
                    } else if ((selectHour == currentHour) && (selectMinute < currentMinute)) {
                        Toast.makeText(getApplicationContext(), "不能选择过去的时间\n        请重新选择", Toast.LENGTH_SHORT).show();
                    } else if ((selectHour == currentHour) && (selectMinute > currentMinute) && (selectMinute < currentMinute + 10)) {
                        Toast.makeText(getApplicationContext(), "请提前十分钟预约\n        请重新选择", Toast.LENGTH_SHORT).show();
                    } else {
                        mData_tv.setText(selectDate);
                        mTime_tv.setText(selectTime);
                        popupWindow.dismiss();
                    }
                } else {
                    mData_tv.setText(selectDate);
                    mTime_tv.setText(selectTime);
                    popupWindow.dismiss();
                }
            }
        });

        //点击取消
        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                popupWindow.dismiss();
            }
        });
    }

    //listeners
    DatePicker.OnChangeListener dp_onchanghelistener = new DatePicker.OnChangeListener() {
        @Override
        public void onChange(int year, int month, int day, int day_of_week) {
            selectDay = day;
            selectDate = month + "月" + day + "日  " + DatePicker.getDayOfWeekCN(day_of_week);
        }
    };
    TimePicker.OnChangeListener tp_onchanghelistener = new TimePicker.OnChangeListener() {
        @Override
        public void onChange(int hour, int minute) {
            selectTime = hour + ":" + ((minute < 10) ? ("0" + minute) : minute);
            selectHour = hour;
            selectMinute = minute;
        }
    };

    private void showVoucherANDhideEndAddress() {
        mEndAddress.setVisibility(View.GONE);
        mVoucher.setVisibility(View.VISIBLE);
    }


    private void hideVoucherANDshowEndAddress() {
        mEndAddress.setVisibility(View.VISIBLE);
        mVoucher.setVisibility(View.GONE);
    }

    private void setActionBarTv(int a) {
        ActionBarTv[a].setTextColor(getResources().getColor(R.color.white));
        ActionBarTv[(a + 1) % 3].setTextColor(getResources().getColor(R.color.aiton_basic_color));
        ActionBarTv[(a + 2) % 3].setTextColor(getResources().getColor(R.color.aiton_basic_color));
        ActionBarTv[a].setBackgroundResource(R.drawable.actionbar_select_tv_shape);
        ActionBarTv[(a + 1) % 3].setBackgroundResource(R.drawable.actionbar_unselect_tv_shape);
        ActionBarTv[(a + 2) % 3].setBackgroundResource(R.drawable.actionbar_unselect_tv_shape);
    }

    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intent = new IntentFilter();
        intent.addAction("Passenger");
        intent.addAction("usedAddressFrom");
        intent.addAction("usedAddressTo");
        registerReceiver(receiver, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
