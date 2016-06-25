package com.dongua.geather;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class Weather extends AppCompatActivity {

    final String WEATHER_URL ="http://wthrcdn.etouch.cn/weather_mini?citykey=";
    HorizontalListView mainListView;
    SimpleAdapter mainAdapter;
    WindowManager windowManager;

    ArrayList<Map<String,Object>> mainList = new ArrayList<Map<String,Object>>();
    int Rfirpng;
    int Rsecpng;
    int Rthrpng;
    int Rfoupng;
    int Rfifpng;

    RelativeLayout weatherlayout ;

//    ArrayList<String> iconList = new ArrayList<String>();
//    ArrayList<String> infoString = new ArrayList<String>();

    ImageView TodayImg;
    TextView text_wd;
    TextView text_fx;
    TextView text_gm;
    TextView text_name;
    TextView firdayinfo;
    TextView secdayinfo;
    TextView thrdayinfo;
    TextView foudayinfo;
    TextView fifdayinfo;
    ImageView firpng,secpng,thrpng,foupng,fifpng;


    String CityID;
    String CityNameEn;
    String strName;
    String strID;

    String WeatherUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainpage);



        weatherlayout=(RelativeLayout)findViewById(R.id.weatherlayout);


        text_wd =(TextView)findViewById(R.id.text_wendu);
        text_fx =(TextView)findViewById(R.id.text_fengxiang);
        text_gm =(TextView)findViewById(R.id.text_ganmao);
        text_name=(TextView)findViewById(R.id.text_name);
        TodayImg=(ImageView)findViewById(R.id.pngtype);
        firdayinfo=(TextView)findViewById(R.id.firdayinfo);
        secdayinfo=(TextView)findViewById(R.id.secdayinfo);
        thrdayinfo=(TextView)findViewById(R.id.thrdayinfo);
        foudayinfo=(TextView)findViewById(R.id.foudayinfo);
        fifdayinfo=(TextView)findViewById(R.id.fifdayinfo);

        firpng =(ImageView)findViewById(R.id.firpng);
        secpng=(ImageView)findViewById(R.id.secpng);
        thrpng =(ImageView)findViewById(R.id.thrpng);
        foupng =(ImageView)findViewById(R.id.foupng);
        fifpng=(ImageView)findViewById(R.id.fifpng);


        Boolean isFirst = Isfirst();
        if(isFirst){
            creatDb();
            SharedPreferences.Editor settingEditor = getSharedPreferences("data",MODE_PRIVATE).edit();
            settingEditor.putBoolean("IsFirstIn",false);
            settingEditor.commit();
            strName = "北京";//初始化数据
            strID ="101010100";
            WeatherUrl =WEATHER_URL+strID;
            sendRequestWithHttpURLConnection();
            saveListData();
        }
        else{
            getListData();
        }


        mainListView=(HorizontalListView)findViewById(R.id.mainListView);
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        mainListView.setWindowsWidth(dm.widthPixels);

        String[] Keys = new String[]{"ftype","ftype","stype","ttype","fotype","fitype"
                ,"name","wendu","fengxiang","ganmao","finfo","sinfo","tinfo","foinfo","fiinfo"};
        int[] Ids = new int[]{R.id.pngtype,R.id.firpng,R.id.secpng,R.id.thrpng,R.id.foupng,R.id.fifpng
                ,R.id.text_name,R.id.text_wendu,R.id.text_fengxiang,R.id.text_ganmao
                ,R.id.firdayinfo,R.id.secdayinfo,R.id.thrdayinfo,R.id.foudayinfo,R.id.fifdayinfo};

        mainAdapter = new SimpleAdapter(this,mainList,R.layout.weather,Keys,Ids);
        mainListView.setAdapter(mainAdapter);

    }

    public boolean Isfirst(){
        SharedPreferences settings = getSharedPreferences("data",MODE_PRIVATE);
        Boolean First = settings.getBoolean("IsFirstIn",true);//没有字段则为第一次进入
        return First;
    }



    public void saveListData(){ //保存数据
        SharedPreferences.Editor editor = getSharedPreferences("ArrayData",MODE_PRIVATE).edit();
        SharedPreferences getData = getSharedPreferences("ArrayData",MODE_PRIVATE);
        int oldSize =getData.getInt("NUMBER",0);
        editor.putInt("NUMBER",mainList.size());//已有的Item数目
        for(int i=oldSize;i<mainList.size();i++){//把图片的R.ID存进去

            editor.putInt("Item"+i+"ftype",(int)mainList.get(i).get("ftype"));
            editor.putInt("Item"+i+"stype",(int)mainList.get(i).get("stype"));
            editor.putInt("Item"+i+"ttype",(int)mainList.get(i).get("ttype"));
            editor.putInt("Item"+i+"fotype",(int)mainList.get(i).get("fotype"));
            editor.putInt("Item"+i+"fitype",(int)mainList.get(i).get("fitype"));
            editor.putString("Item"+i+"name",(String)mainList.get(i).get("name"));
            editor.putString("Item"+i+"ID",(String)mainList.get(i).get("ID"));
            editor.putString("Item"+i+"wendu",(String)mainList.get(i).get("wendu"));
            editor.putString("Item"+i+"fengxiang",(String)mainList.get(i).get("fengxiang"));
            editor.putString("Item"+i+"ganmao",(String)mainList.get(i).get("ganmao"));
            editor.putString("Item"+i+"finfo",(String)mainList.get(i).get("finfo"));
            editor.putString("Item"+i+"sinfo",(String)mainList.get(i).get("sinfo"));
            editor.putString("Item"+i+"tinfo",(String)mainList.get(i).get("tinfo"));
            editor.putString("Item"+i+"foinfo",(String)mainList.get(i).get("foinfo"));
            editor.putString("Item"+i+"fiinfo",(String)mainList.get(i).get("fiinfo"));
        }
        editor.commit();
    }

    public void saveAfterRemove(){ //保存数据
        SharedPreferences.Editor editor = getSharedPreferences("ArrayData",MODE_PRIVATE).edit();
        editor.putInt("NUMBER",mainList.size());//已有的Item数目
        for(int i=0;i<mainList.size();i++){//把图片的R.ID存进去

            editor.putInt("Item"+i+"ftype",(int)mainList.get(i).get("ftype"));
            editor.putInt("Item"+i+"stype",(int)mainList.get(i).get("stype"));
            editor.putInt("Item"+i+"ttype",(int)mainList.get(i).get("ttype"));
            editor.putInt("Item"+i+"fotype",(int)mainList.get(i).get("fotype"));
            editor.putInt("Item"+i+"fitype",(int)mainList.get(i).get("fitype"));
            editor.putString("Item"+i+"name",(String)mainList.get(i).get("name"));
            editor.putString("Item"+i+"ID",(String)mainList.get(i).get("ID"));
            editor.putString("Item"+i+"wendu",(String)mainList.get(i).get("wendu"));
            editor.putString("Item"+i+"fengxiang",(String)mainList.get(i).get("fengxiang"));
            editor.putString("Item"+i+"ganmao",(String)mainList.get(i).get("ganmao"));
            editor.putString("Item"+i+"finfo",(String)mainList.get(i).get("finfo"));
            editor.putString("Item"+i+"sinfo",(String)mainList.get(i).get("sinfo"));
            editor.putString("Item"+i+"tinfo",(String)mainList.get(i).get("tinfo"));
            editor.putString("Item"+i+"foinfo",(String)mainList.get(i).get("foinfo"));
            editor.putString("Item"+i+"fiinfo",(String)mainList.get(i).get("fiinfo"));
        }
        editor.commit();
    }
    public void getListData(){ //获取数据
        SharedPreferences getData = getSharedPreferences("ArrayData",MODE_PRIVATE);
        int Size =getData.getInt("NUMBER", 0);//若取不到NUMBER值 则默认为0
        for(int i=0;i<Size;i++){
            HashMap<String,Object> item = new HashMap<String,Object>();

            item.put("today",getData.getInt("Item"+i+"today",R.drawable.daxue));
            item.put("ftype",getData.getInt("Item"+i+"ftype",R.drawable.daxue));
            item.put("stype",getData.getInt("Item"+i+"stype",R.drawable.daxue));
            item.put("ttype",getData.getInt("Item"+i+"ttype",R.drawable.daxue));
            item.put("fotype",getData.getInt("Item"+i+"fotype",R.drawable.daxue));
            item.put("fitype",getData.getInt("Item"+i+"fitype",R.drawable.daxue));
            item.put("name",getData.getString("Item"+i+"name","NULL"));
            item.put("ID",getData.getString("Item"+i+"ID","NULL"));
            item.put("wendu",getData.getString("Item"+i+"wendu","NULL"));
            item.put("fengxiang",getData.getString("Item"+i+"fengxiang","NULL"));
            item.put("ganmao",getData.getString("Item"+i+"ganmao","NULL"));
            item.put("finfo",getData.getString("Item"+i+"finfo","NULL"));
            item.put("sinfo",getData.getString("Item"+i+"sinfo","NULL"));
            item.put("tinfo",getData.getString("Item"+i+"tinfo","NULL"));
            item.put("foinfo",getData.getString("Item"+i+"foinfo","NULL"));
            item.put("fiinfo",getData.getString("Item"+i+"fiinfo","NULL"));
            mainList.add(item);
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu,menu);
        menu.add(Menu.NONE,Menu.FIRST+1,1,"添加").setIcon(android.R.drawable.ic_menu_add);
        menu.add(Menu.NONE,Menu.FIRST+2,2,"删除").setIcon(android.R.drawable.ic_menu_delete);
//        menu.add(Menu.NONE,Menu.FIRST+3,3,"更多").setIcon(android.R.drawable.ic_menu_help);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){

            case Menu.FIRST+1:
                Intent intent = new Intent(Weather.this,SelectCity.class);
                startActivityForResult(intent,1);//回调函数里JSON数据解析 add了

                break;
            case Menu.FIRST+2:
                int ItemPos = -mainListView.mDisplayOffset/1000;//获取准确子Item位置
                if(ItemPos>=0 && mainList.size()>1){
                    mainList.remove(ItemPos);
                    saveAfterRemove();
                    mainAdapter.notifyDataSetChanged();

                }
                else{
                    Toast.makeText(Weather.this,"请至少保留一个城市",Toast.LENGTH_LONG).show();
                }
                break;
            case Menu.FIRST+3:
                break;
        }
        return true;
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode){
            case RESULT_OK:
                strName = data.getStringExtra("CityName");
                strID =data.getStringExtra("CityID");
                mHandler.post(AddItem);
                break;
            default:
        }
    }
    Runnable AddItem = new Runnable() {
        @Override
        public void run() {
            WeatherUrl = WEATHER_URL+ strID;
            sendRequestWithHttpURLConnection();

//            saveListData();
//            //saveListData();
        }
    };



    private void sendRequestWithHttpURLConnection(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection connection = null;
                try {
                    URL url = new URL(WeatherUrl);
                    connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(8000);
                    connection.setReadTimeout(8000);
                    if (connection.getResponseCode() == 200) {// 判断请求码是否200，否则为失败

                        InputStream in = connection.getInputStream();
                        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        parseJSONWithJSONObject(response.toString());
                    }

                }
                catch ( Exception e){
                    e.printStackTrace();
                }
                finally {
                    if(connection!=null);
                    connection.disconnect();
                }
            }
        }).start();

    }




    private void parseJSONWithJSONObject(String jsonData){
        try{

            JSONObject jsonObject = new JSONObject(jsonData);
            JSONObject data = jsonObject.getJSONObject("data");
            JSONArray forecast = data.getJSONArray("forecast");
            JSONObject firstday =forecast.optJSONObject(0);
            JSONObject secondday =forecast.optJSONObject(1);
            JSONObject thirdday =forecast.optJSONObject(2);
            JSONObject fourthdday =forecast.optJSONObject(3);
            JSONObject fifthday =forecast.optJSONObject(4);



            String flowt = firstday.getString("low");
            String fhight = firstday.getString("high");
            String ftype = firstday.getString("type");
            String fdate  = firstday.getString("date");
            String finfo =fdate+"  "+ftype+"\n"+flowt+"\n"+fhight;
//            iconList.add(ftype);



            String wendu = ftype+"  "+data.getString("wendu")+"°C";
            String fengxiang = firstday.getString("fengxiang");
            String ganmao = data.getString("ganmao");
//
//            infoString.add(wendu);
//            infoString.add(fengxiang);
//            infoString.add(ganmao);
//            infoString.add(finfo);

            String slowt = secondday.getString("low");
            String shight = secondday.getString("high");
            String stype = secondday.getString("type");
            String sdate  = secondday.getString("date");
            String sinfo =sdate+"  "+stype+"\n"+slowt+"\n"+shight;
//            iconList.add(stype);
//            infoString.add(sinfo);

            String tlowt = thirdday.getString("low");
            String thight = thirdday.getString("high");
            String ttype = thirdday.getString("type");
            String tdate  = thirdday.getString("date");
            String tinfo =tdate+"  "+ttype+"\n"+tlowt+"\n"+thight;
//            iconList.add(ttype);
//            infoString.add(tinfo);


            String folowt = fourthdday.getString("low");
            String fohight = fourthdday.getString("high");
            String fotype = fourthdday.getString("type");
            String fodate  = fourthdday.getString("date");
            String foinfo =fodate+"  "+fotype+"\n"+folowt+"\n"+fohight;
//            iconList.add(fotype);
//            infoString.add(foinfo);

            String filowt = fifthday.getString("low");
            String fihight = fifthday.getString("high");
            String fitype = fifthday.getString("type");
            String fidate  = fifthday.getString("date");
            String fiinfo =fidate+"  "+fitype+"\n"+filowt+"\n"+fihight;
//            iconList.add(fitype);
//            infoString.add(fiinfo);

            Rfirpng = ChgToID(ftype); //imageview
            Rsecpng = ChgToID(stype);
            Rthrpng = ChgToID(ttype);
            Rfoupng = ChgToID(fotype);
            Rfifpng = ChgToID(fitype);

            HashMap<String,Object> item = new HashMap<String,Object>();
            item.put("ftype",Rfirpng);// String ,Int
            item.put("stype",Rsecpng);
            item.put("ttype",Rthrpng);
            item.put("fotype",Rfoupng);
            item.put("fitype",Rfifpng);
            item.put("name",strName);
            item.put("ID",strID);
            item.put("wendu",wendu);//String ,String
            item.put("fengxiang",fengxiang);
            item.put("ganmao",ganmao);
            item.put("finfo",finfo);
            item.put("sinfo",sinfo);
            item.put("tinfo",tinfo);
            item.put("foinfo",foinfo);
            item.put("fiinfo",fiinfo);
            mainList.add(item);
            saveListData();


            mainAdapter.notifyDataSetChanged();




//                Message message10 = new Message();
//                message10.what = 10;
//                message10.obj =iconList;
//                mHandler.sendMessage(message10);
//
//                Message message11 = new Message();
//                message11.what = 11;
//                message11.obj =infoString;
//                mHandler.sendMessage(message11);

        }
        catch (Exception e){
            e.printStackTrace();
        }
    }



    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 10://设置小图标  list
//                    ArrayList<String> smallIcon = (ArrayList)msg.obj;
//                    String icon0 = smallIcon.get(0);
//                    setDrawable(TodayImg,icon0);//大图标
//                    setDrawable(firpng,icon0);
//                    String icon1 = smallIcon.get(1);
//                    setDrawable(secpng,icon1);
//                    String icon2 = smallIcon.get(2);
//                    setDrawable(thrpng,icon2);
//                    String icon3 = smallIcon.get(3);
//                    setDrawable(foupng,icon3);
//                    String icon4 = smallIcon.get(4);
//                    setDrawable(fifpng,icon4);
                    break;
                case 11://设置文字信息
//                    ArrayList<String> infoString = (ArrayList)msg.obj;
//                    text_wd.setText(infoString.get(0)+"°C");
//                    text_fx.setText("风向:"+infoString.get(1));
//                    text_gm.setText("小贴士:"+infoString.get(2));
//                    firdayinfo.setText(infoString.get(3));
//                    secdayinfo.setText(infoString.get(4));
//                    thrdayinfo.setText(infoString.get(5));
//                    foudayinfo.setText(infoString.get(6));
//                    fifdayinfo.setText(infoString.get(7));
                    break;
            }
        }
    };


    private int ChgToID(String text) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);

        if (getString(R.string.qing).equals(text)) {
            if (hour > 18 || hour < 6) {
                return (R.drawable.qingtianye);
            }
            else {
                return (R.drawable.qingtian);
            }
        }
        else if (getString(R.string.duoyun).equals(text)) {
            if (hour > 18 || hour < 6) {
                return (R.drawable.duoyunye);
            }
            else {
                return (R.drawable.duoyun);
            }
        }
        else if (getString(R.string.yin).equals(text))
            return (R.drawable.yin);
        else if (getString(R.string.xiaoyu).equals(text))
            return (R.drawable.xiaoyu);
        else if (getString(R.string.zhongyu).equals(text))
            return (R.drawable.zhongyu);
        else if (getString(R.string.dayu).equals(text))
            return (R.drawable.dayu);
        else if (getString(R.string.zhenyu).equals(text))
            return (R.drawable.zhenyu);
        else if (getString(R.string.leizhenyu).equals(text))
            return (R.drawable.leizhenyu);
        else if (getString(R.string.xiaodaozhongyu).equals(text))
            return (R.drawable.xiaoyu);
        else if (getString(R.string.zhongdaodayu).equals(text))
            return (R.drawable.zhongyu);
        else if (getString(R.string.dadaobaoyu).equals(text))
            return (R.drawable.dayu);
        else
            return (R.drawable.error);
    }




//    private void setDrawable(ImageView imageView,String text){
//
//        Calendar c = Calendar.getInstance();
//        int hour = c.get(Calendar.HOUR_OF_DAY);
//            if(getString(R.string.qing).equals(text)) {
//                if (hour > 18 || hour < 6) {
//                    imageView.setImageResource(R.drawable.qingtianye);
//                } else {
//                    imageView.setImageResource(R.drawable.qingtian);
//                }
//            }
//            else if(getString(R.string.duoyun).equals(text)) {
//                if (hour > 18 || hour < 6) {
//                    imageView.setImageResource(R.drawable.duoyunye);
//                } else {
//                    imageView.setImageResource(R.drawable.duoyun);
//                }
//            }
//            else if(getString(R.string.yin).equals(text))
//                imageView.setImageResource(R.drawable.yin);
//            else if(getString(R.string.xiaoyu).equals(text))
//                imageView.setImageResource(R.drawable.xiaoyu);
//            else if(getString(R.string.zhongyu).equals(text))
//                imageView.setImageResource(R.drawable.zhongyu);
//            else if(getString(R.string.dayu).equals(text))
//                imageView.setImageResource(R.drawable.dayu);
//            else if(getString(R.string.zhenyu).equals(text))
//                imageView.setImageResource(R.drawable.zhenyu);
//            else if(getString(R.string.xiaozhongyu).equals(text))
//                imageView.setImageResource(R.drawable.xiaoyu);
//        }


    private void creatDb(){
        /**在这里插入SQLite拷贝到SD卡的函数*/
        String DB_PATH = "/data/data/com.dongua.geather/databases/";
        String DB_NAME = "cityname.db";
        // 检查 SQLite 数据库文件是否存在
        if ((new File(DB_PATH + DB_NAME)).exists() == false) {
            // 如 SQLite 数据库文件不存在，再检查一下 database 目录是否存在
            File f = new File(DB_PATH);
            // 如 database 目录不存在，新建该目录
            if (!f.exists()) {
                f.mkdir();
            }

            try {
                // 得到 assets 目录下我们实现准备好的 SQLite 数据库作为输入流
                InputStream is = getBaseContext().getAssets().open(DB_NAME);
                // 输出流
                OutputStream os = new FileOutputStream(DB_PATH + DB_NAME);

                // 文件写入
                byte[] buffer = new byte[1024];
                int length;
                while ((length = is.read(buffer)) > 0) {
                    os.write(buffer, 0, length);
                }

                // 关闭文件流
                os.flush();
                os.close();
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }


}


