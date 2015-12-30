package com.jiaotong.aiton.myapplication.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.jiaotong.aiton.myapplication.R;

public class UsedAddress extends Activity implements View.OnClickListener, AdapterView.OnItemClickListener {

    private ListView mCollection_address_list;
    private String mWhere;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_used_address);
        Intent intent = getIntent();
        mWhere = intent.getStringExtra("where");
        initUI();
        setListener();
    }

    private void setListener() {
        findViewById(R.id.back).setOnClickListener(this);
        mCollection_address_list.setOnItemClickListener(this);
    }

    private void initUI() {
        mCollection_address_list = (ListView) findViewById(R.id.collection_address_list);
        mCollection_address_list.setAdapter(new MyAdapter());
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent= new Intent();
        if ("from".equals(mWhere)){
            intent.setAction("usedAddressFrom");

            intent.putExtra("usedAddressFromStr","侨谊幼儿园(禾山一路)");
        }else if ("to".equals(mWhere)){
            intent.setAction("usedAddressTo");

            intent.putExtra("usedAddressToStr","侨谊幼儿园(禾山一路)");
        }
        sendBroadcast(intent);
        finish();
    }

    class MyAdapter extends BaseAdapter {

             @Override
             public int getCount() {
                 return 3;
             }

             @Override
             public Object getItem(int position) {
                 return null;
             }

             @Override
             public long getItemId(int position) {
                 return 0;
             }

             @Override
             public View getView(int position, View convertView, ViewGroup parent) {
                 View inflate = getLayoutInflater().inflate(R.layout.usedaddressitem, null);
                 return inflate;
             }
         }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back:
                finish();
                overridePendingTransition(R.anim.fade_in, R.anim.push_down_out);
                break;
        }
    }
}
