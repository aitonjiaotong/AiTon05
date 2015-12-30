package com.jiaotong.aiton.myapplication.Activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.jiaotong.aiton.myapplication.R;

public class CallCarForOthers extends AppCompatActivity implements View.OnClickListener {

    private ImageView mNameInputCancle;
    private EditText mNameInputEdit;
    private ImageView mPhoneNumCancle;
    private EditText mPhoneInputEdit;
    private boolean isMessageNotify=true;
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String name = intent.getStringExtra("name");
            String phoneNumber = intent.getStringExtra("phoneNumber");
            mNameInputEdit.setText(name);
            mPhoneInputEdit.setText(phoneNumber);
        }
    };
    private ImageView mIsMessageNotify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_call_car_for_others);
        initUI();
        inttListener();
    }

    private void initUI() {
        mPhoneNumCancle = (ImageView) findViewById(R.id.phoneNumCancle);
        mNameInputCancle = (ImageView) findViewById(R.id.nameInputCancle);
        mNameInputEdit = (EditText) findViewById(R.id.nameInputEdit);
        mPhoneInputEdit = (EditText) findViewById(R.id.phoneInputEdit);
        mIsMessageNotify = (ImageView) findViewById(R.id.MessageNotify);
    }

    private void inttListener() {
        findViewById(R.id.CancleCallCarForOther_rela).setOnClickListener(this);
        findViewById(R.id.nameInputCancle_rela).setOnClickListener(this);
        findViewById(R.id.phoneNumCancle_rela).setOnClickListener(this);
        mNameInputEdit.addTextChangedListener(new NameTextWatcher());
        mPhoneInputEdit.addTextChangedListener(new PhoneNumTextWatcher());
        findViewById(R.id.MailList_linear).setOnClickListener(this);
        findViewById(R.id.complete).setOnClickListener(this);
        mIsMessageNotify.setOnClickListener(this);
    }

    class NameTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                mNameInputCancle.setVisibility(View.VISIBLE);
                mNameInputEdit.setSelection(s.length());
            } else {
                mNameInputCancle.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    class PhoneNumTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() > 0) {
                mPhoneNumCancle.setVisibility(View.VISIBLE);
                mPhoneInputEdit.setSelection(s.length());
            } else {
                mPhoneNumCancle.setVisibility(View.GONE);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()) {
            case R.id.MessageNotify:
                isMessageNotify=!isMessageNotify;
                /**
                 * 此处需添加发送短信，由后台发送
                 */
                if (isMessageNotify){
                    mIsMessageNotify.setImageResource(R.mipmap.icon_selected);
                }else{
                    mIsMessageNotify.setImageResource(R.mipmap.icon_unselected);
                }
                break;
            case R.id.CancleCallCarForOther_rela:
                finish();
                break;
            case R.id.complete:
                if (mNameInputEdit.getText().toString().trim().equals("")){
                    toast("填写真实姓名 享百万保障");

                }else {
                    if (mPhoneInputEdit.getText().toString().trim().equals("")) {
                        toast("请输入乘车人电话号码");
                    } else {
                        intent.setAction("Passenger");
                        intent.putExtra("name",mNameInputEdit.getText().toString().trim());
                        intent.putExtra("phoneNumber",mPhoneInputEdit.getText().toString().trim());
                        sendBroadcast(intent);
                        finish();
                    }
                }
                break;
            case R.id.nameInputCancle_rela:
                mNameInputEdit.setText("");
                break;
            case R.id.phoneNumCancle_rela:
                mPhoneInputEdit.setText("");
                break;
            case R.id.MailList_linear:
                intent.setClass(CallCarForOthers.this, CheckPassenger.class);
                startActivity(intent);
                break;
        }
    }
    private void toast(final String str) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();
        IntentFilter intent = new IntentFilter();
        intent.addAction("cantact");
        registerReceiver(receiver, intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(receiver);
    }
}
