package com.jiaotong.aiton.myapplication.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.github.volley.HTTPUtils;
import com.github.volley.VolleyListener;
import com.jiaotong.aiton.myapplication.R;
import com.jiaotong.aiton.myapplication.custom.LetterIndexView;
import com.jiaotong.aiton.myapplication.mode.City;
import com.xmbc.utils.GsonUtils;

import java.util.ArrayList;
import java.util.List;

public class LetterIndexActivity extends AppCompatActivity
{
    private TextView tv_mLetter;
    private ListView mListView;
    private LetterIndexView mIndexListLetter;
    private MyListAdapter mAdapter;
    private String mCityUrl = "http://7xnuiw.com1.z0.glb.clouddn.com/city.txt";

    private List<City.AllcityEntity> mCityData = new ArrayList<City.AllcityEntity>();

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_letter_index);

        initUI();
        initData();
    }

    private void initData()
    {
        HTTPUtils.get(LetterIndexActivity.this, mCityUrl, new VolleyListener()
        {
            public void onErrorResponse(VolleyError volleyError)
            {
            }
            public void onResponse(String s)
            {
                City city = GsonUtils.parseJSON(s, City.class);
                List<City.AllcityEntity> allcity = city.getAllcity();
                mCityData.addAll(allcity);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void initUI()
    {
        mListView = (ListView) this.findViewById(R.id.listView_for_City);
        tv_mLetter = (TextView) this.findViewById(R.id.tv_letter);
        mIndexListLetter = (LetterIndexView) this.findViewById(R.id.letter_index_view);
        mIndexListLetter.setOnGetLetterListener(new LetterIndexView.GetLetterListener()
        {
            public void onLetterChanged(String letter)
            {
                tv_mLetter.setVisibility(View.VISIBLE);
                tv_mLetter.setText(letter);
                //更新ListView的行数显示
                int searchLetter_index = searchLetter(letter);
                mListView.setSelection(searchLetter_index);
            }
            public void onActionUp()
            {
                tv_mLetter.setVisibility(View.GONE);
            }
        });
        mAdapter = new MyListAdapter();
        mListView.setAdapter(mAdapter);
    }

    class MyListAdapter extends BaseAdapter
    {
        private View layout;
        public int getCount()
        {
            return mCityData.size();
        }
        public Object getItem(int position)
        {
            return null;
        }
        public long getItemId(int position)
        {
            return 0;
        }
        public View getView(int position, View convertView, ViewGroup parent)
        {
            layout = getLayoutInflater().inflate(R.layout.choose_city_listview_item, null);
            TextView tv_fristletter = (TextView) layout.findViewById(R.id.tv_fristletter);
            TextView tv_city = (TextView) layout.findViewById(R.id.tv_city);
            tv_city.setText(mCityData.get(position).getName());
            //当前行首字母
            String substring_fristletter = mCityData.get(position).getPinyin().substring(0, 1);
            tv_fristletter.setText(substring_fristletter);

            if(position > 0)
            {
                //上一行首字母
                String lastletter = mCityData.get(position - 1).getPinyin().substring(0, 1);
                if(substring_fristletter.equals(lastletter))
                {
                    tv_fristletter.setVisibility(View.GONE);
                }
            }
            return layout;
        }
    }
    /**
     * 搜索用户点击自定义的IndexListView控件所返回的字母
     */
    public int searchLetter(String letter)
    {
        for (int i = 0; i < mCityData.size(); i++)
        {
            String string = mCityData.get(i).getPinyin();
            if(string.toUpperCase().startsWith(letter))
            {
                return i;
            }
        }
        return -1;
    }


}
