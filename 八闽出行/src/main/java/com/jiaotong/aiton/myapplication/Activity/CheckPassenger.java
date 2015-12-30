package com.jiaotong.aiton.myapplication.Activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jiaotong.aiton.myapplication.R;
import com.jiaotong.aiton.myapplication.mode.Cantact;

import java.util.ArrayList;

public class CheckPassenger extends AppCompatActivity implements View.OnClickListener {

    private ListView mCantact_listview;
    private ArrayList<Cantact> mCantactInfos = new ArrayList<Cantact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_passenger);
        /**
         * 获取联系人
         */
        getCantact();
        initUI();
        setListener();
    }

    private void getCantact() {
        Cursor c = getApplicationContext().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,  null, null, null);


// 循环输出联系人号码
        String phoneNum,name;
        while (c.moveToNext()) {
            // 可以获取到电话号码
            phoneNum = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            name = c.getString(c.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            mCantactInfos.add(new Cantact(name,phoneNum));
        }
    }
    private void setListener() {
        findViewById(R.id.checkPassenger_back_rela).setOnClickListener(this);
        findViewById(R.id.checkPassenger_cancle).setOnClickListener(this);
        mCantact_listview.setOnItemClickListener(new MyItemClickListener());
    }

    private void initUI() {
        mCantact_listview = (ListView) findViewById(R.id.cantact_listview);
        mCantact_listview.setAdapter(new MyAdapter());
    }

    class MyItemClickListener implements AdapterView.OnItemClickListener{

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            finish();
            Intent intent=new Intent();
            intent.setAction("cantact");
            intent.putExtra("name",mCantactInfos.get(position).getName());
            intent.putExtra("phoneNumber",mCantactInfos.get(position).getPhoneNumber());
            sendBroadcast(intent);
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return mCantactInfos.size();
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
            View inflate = getLayoutInflater().inflate(R.layout.cantact_listitem, null);
            TextView name = (TextView) inflate.findViewById(R.id.name);
            TextView phoneNumber = (TextView) inflate.findViewById(R.id.phoneNumber);
            name.setText(mCantactInfos.get(position).getName());
            phoneNumber.setText(mCantactInfos.get(position).getPhoneNumber());
            return inflate;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.checkPassenger_back_rela:
                finish();
                break;
            case R.id.checkPassenger_cancle:
                finish();
                break;
        }
    }
}
