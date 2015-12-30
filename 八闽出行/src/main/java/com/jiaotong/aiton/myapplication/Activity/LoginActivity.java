package com.jiaotong.aiton.myapplication.Activity;

import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jiaotong.aiton.myapplication.R;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
        setOnclick();
    }

    private void setOnclick() {
        mBack.setOnClickListener(this);
    }

    private void initUI() {

        TextView tv_protocol = (TextView) findViewById(R.id.tv_protocol);
        tv_protocol.setPaintFlags(Paint. UNDERLINE_TEXT_FLAG);

        mBack = (ImageView) findViewById(R.id.iv_login_back);


    }


    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.iv_login_back:
                finish();
                break;
            default:
                break;
        }
    }
}
