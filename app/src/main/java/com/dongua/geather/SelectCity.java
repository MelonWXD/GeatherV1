package com.dongua.geather;


import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;

public class SelectCity extends Activity {

    EditText mEditText;
    ListView mListView;
    CityDb mCityDb;
    ArrayList<HashMap<String, Object>> mCityData;
    SimpleAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectciy);

        mListView = (ListView) findViewById(R.id.mListView);
        mEditText = (EditText) findViewById(R.id.mEditText);


        mCityData = new ArrayList<HashMap<String, Object>>();
        SQLiteDatabase mDb = openOrCreateDatabase("cityname.db",
                SQLiteDatabase.CREATE_IF_NECESSARY, null);
        Cursor mCursor = mDb.rawQuery("select * from " + "t_city", null);

        mCityDb = new CityDb();
        mCityData = mCityDb.InitCityList(mCursor);

        mAdapter = new SimpleAdapter(this,mCityData,R.layout.item,
                new String[]{"name_sheng", "name_shi", "name_qu","name_id"},
                new int[]{R.id.name_sheng, R.id.name_shi, R.id.name_qu,R.id.CityID});

        mListView.setAdapter(mAdapter);
        mTextChanged();

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent =new Intent();
                intent.putExtra("CityName",mCityData.get(position).get("name_qu")+"");
                intent.putExtra("CityID",mCityData.get(position).get("name_id")+"");
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void mTextChanged()
    {
        mEditText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {}
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {}
            @Override
            public void afterTextChanged(Editable s) {
                new Handler().post(editChanged);
            }
        });
    }
    Runnable editChanged = new Runnable() {

        @Override
        public void run() {

            mCityData.clear();
            String data = mEditText.getText().toString();//编辑框内容获取
            int length = mCityDb.QuName.size();

            for(int i = 0; i < length; i++){
                HashMap<String,Object> item = new HashMap<String,Object>();
                if(mCityDb.QuName.get(i).contains(data)
                        || mCityDb.ShiName.get(i).contains(data)
                        || mCityDb.ShengName.get(i).contains(data)){
                    item.put("name_qu",mCityDb.QuName.get(i));
                    item.put("name_shi",mCityDb.ShiName.get(i));
                    item.put("name_sheng",mCityDb.ShengName.get(i));
                    item.put("name_id",mCityDb.IDName.get(i));
                    mCityData.add(item);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    };

}
