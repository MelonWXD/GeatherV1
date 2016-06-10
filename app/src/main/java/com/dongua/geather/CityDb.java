package com.dongua.geather;


import android.database.Cursor;


import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by dongua on 2016/5/20.
 */
public class CityDb {

    static ArrayList<HashMap<String, Object>> mData ;
    static ArrayList<String> QuName ;
    static ArrayList<String> ShiName ;
    static ArrayList<String> ShengName ;
    static ArrayList<String> IDName ;


    public static  ArrayList<HashMap<String, Object>> InitCityList(Cursor mCursor){
        mData = new ArrayList<HashMap<String, Object>>();
        QuName = new ArrayList<String>();
        ShiName = new ArrayList<String>();
        ShengName = new ArrayList<String>();
        IDName = new ArrayList<String>();

        while (mCursor.moveToNext()) {
            HashMap<String, Object> item = new HashMap<String, Object>();
            item.put("name_id", mCursor.getString(0));
            IDName.add(mCursor.getString(0));
            //            item.put("name_quEn",mCursor.getString(1));
            //            QuNameEn.add(mCursor.getString(1));
            item.put("name_qu", mCursor.getString(2));
            QuName.add(mCursor.getString(2));
            item.put("name_shi", mCursor.getString(4) + "市");
            ShiName.add(mCursor.getString(4)+"市");
            if(("北京重庆天津上海").contains(mCursor.getString(6))){
                item.put("name_sheng", "");
                ShengName.add("");
            }else{
                item.put("name_sheng", mCursor.getString(6)+"省  ");
                ShengName.add(mCursor.getString(6) +"省  ");
            }
            mData.add(item);
        }

        mCursor.close();
        return mData;
    }


}
