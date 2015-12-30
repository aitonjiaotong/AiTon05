package com.jiaotong.aiton.myapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.jiaotong.aiton.myapplication.R;

public class EstimatePrice extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_estimate_price);
        setListener();
    }

    private void setListener() {
        findViewById(R.id.back_rela).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_rela:
                finish();
                break;
        }
    }
}
