package com.jiaotong.aiton.myapplication.Activity;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jiaotong.aiton.myapplication.R;
import com.jiaotong.aiton.myapplication.mode.BookingCarInfo;

import java.util.ArrayList;

public class CallCar extends AppCompatActivity implements View.OnClickListener {

    private LinearLayout mEndAddress;

    //待用解析数据。暂用模拟数据
    private ArrayList<BookingCarInfo> mBookingCarInfos = new ArrayList<BookingCarInfo>();
    private RelativeLayout mCall_car_forOther;

    private TextView mName;
    private TextView mPhoneNumber;
    private TextView mFrom;
    private TextView mTo;

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
        setContentView(R.layout.activity_call_car);
        initUI();
        setListener();
        addBookingCarInfos();
        /**
         * 初始化车辆选择
         */
        checkCar(0);
    }
    private void addBookingCarInfos() {
        mBookingCarInfos.add(new BookingCarInfo("套餐价¥50.0(含20分钟8公里)", "超出按¥0.5/分钟+¥3.8/公里计费", "大众帕萨特/丰田凯美瑞或类似5座车型"));
        mBookingCarInfos.add(new BookingCarInfo("套餐价¥350.0(含4小时50公里", "超出按¥0.6/分钟+¥4.0/公里计费", "大众帕萨特/丰田凯美瑞或类似5座车型"));
        mBookingCarInfos.add(new BookingCarInfo("套餐价¥500.0(含4小时50公里", "超出按¥0.6/分钟+¥4.0/公里计费", "大众帕萨特/丰田凯美瑞或类似5座车型"));
    }
    private void setListener() {
        mCall_car_forOther.setOnClickListener(this);
        findViewById(R.id.call_car_back).setOnClickListener(this);
        mName.addTextChangedListener(new NameTextWatcher());
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

        mCall_car_forOther = (RelativeLayout) findViewById(R.id.call_car_forOther);
        mEndAddress = (LinearLayout) findViewById(R.id.endAddress);
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
                intent.setClass(CallCar.this, EstimatePrice.class);
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
                intent.setClass(CallCar.this, UsedAddress.class);
                intent.putExtra("where", "from");
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.fade_out);
                break;
            case R.id.Collection02:
                intent.setClass(CallCar.this, UsedAddress.class);
                intent.putExtra("where", "to");
                startActivity(intent);
                overridePendingTransition(R.anim.push_up_in, R.anim.fade_out);
                break;
            case R.id.call_car_forOther:
                intent.setClass(CallCar.this, CallCarForOthers.class);
                startActivity(intent);
                break;
            case R.id.call_car_back:
                finish();
                break;
        }
    }
    private void checkCar(int checkCarID) {
        carTv[checkCarID].setTextColor(getResources().getColor(R.color.black));
        carImg[checkCarID].setImageResource(R.mipmap.ucar_check);
        carTv[(checkCarID + 1) % 3].setTextColor(getResources().getColor(R.color.text_color_gray));
        carImg[(checkCarID + 1) % 3].setImageResource(R.mipmap.passenger_msg_unselected);
        carTv[(checkCarID + 2) % 3].setTextColor(getResources().getColor(R.color.text_color_gray));
        carImg[(checkCarID + 2) % 3].setImageResource(R.mipmap.passenger_msg_unselected);
    }
    private void setDialog() {
        View commit_dialog = getLayoutInflater().inflate(R.layout.commit_dialog, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(CallCar.this);
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
