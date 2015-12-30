package com.jiaotong.aiton.myapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jiaotong.aiton.myapplication.R;

public class Flight extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flight);
        initUI();
        setListener();
    }

    private void setListener() {
        findViewById(R.id.back).setOnClickListener(this);
    }

    private void initUI() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                break;
        }
    }
}
