package com.dongua.geather;


import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class Weather extends Activity {

    final String WEATHER_URL ="http://wthrcdn.etouch.cn/weather_mini?citykey=";

    ImageView pngtpye;
    TextView text_wd;
    TextView text_fx;
    TextView text_gm;
    TextView text_name;
    TextView firdayinfo;
    TextView secdayinfo;
    TextView thrdayinfo;
    TextView foudayinfo;
    TextView fifdayinfo;
    Button chgcitybutton;


    String CityID;
    String CityNameEn;
    String strName;
    String strID;

    String WeatherUrl;
    Handler mHandler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    String text1 = msg.obj.toString();
                    text_wd.setText(text1);
                    break;
                case 2:
                    String text2 = msg.obj.toString();
                    text_fx.setText("风向:"+text2);
                    break;
                case 3:
                    String text3 = msg.obj.toString();
                    text_gm.setText("小贴士:"+text3);
                    break;
                case 4:
                    String text4 = msg.obj.toString();
                    if(text4.equals("晴"))
                        pngtpye.setImageResource(R.drawable.qingtian);
                    else if(text4.equals("多云"))
                        pngtpye.setImageResource(R.drawable.duoyun);
                    else if(text4.equals("中雨"))
                        pngtpye.setImageResource(R.drawable.zhongyu);
                    else if(text4.equals("小雨"))
                        pngtpye.setImageResource(R.drawable.xiaoyu);
                    break;
                case 5:
                    String text5 = msg.obj.toString();
                    firdayinfo.setText(text5);
                    break;
                case 6:
                    String text6 = msg.obj.toString();
                    secdayinfo.setText(text6);
                    break;
                case 7:
                    String text7 = msg.obj.toString();
                    thrdayinfo.setText(text7);
                    break;
                case 8:
                    String text8 = msg.obj.toString();
                    foudayinfo.setText(text8);
                    break;
                case 9:
                    String text9 = msg.obj.toString();
                    fifdayinfo.setText(text9);
                    break;

            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.weather);

        text_wd =(TextView)findViewById(R.id.text_wendu);
        text_fx =(TextView)findViewById(R.id.text_fengxiang);
        text_gm =(TextView)findViewById(R.id.text_ganmao);
        text_name=(TextView)findViewById(R.id.text_name);
        pngtpye=(ImageView)findViewById(R.id.pngtype);
        firdayinfo=(TextView)findViewById(R.id.firdayinfo);
        secdayinfo=(TextView)findViewById(R.id.secdayinfo);
        thrdayinfo=(TextView)findViewById(R.id.thrdayinfo);
        foudayinfo=(TextView)findViewById(R.id.foudayinfo);
        fifdayinfo=(TextView)findViewById(R.id.fifdayinfo);

        chgcitybutton=(Button)findViewById(R.id.changecitybutton);
        chgcitybutton.setOnClickListener(new ChangeButtonListener());


        HorizontalScrollView horizontalScrollView = (HorizontalScrollView)findViewById(R.id.hscroll);
        horizontalScrollView.setVerticalScrollBarEnabled(false);


        getData();
        text_name.setText(strName);

        setGuided();




//        Intent intent = getIntent();
//        strID = intent.getStringExtra("CityID");
        WeatherUrl =WEATHER_URL+strID;
//        strName =intent.getStringExtra("CityName");
        chgcitybutton.setText(strName);


        //WeatherUrl ="http://www.weather.com.cn/adat/sk/"+CityID+".html";
//
//        CityNameEn = intent.getStringExtra("CityNameEn");
//        WeatherUrl ="https://api.thinkpage.cn/v3/weather/now.json?key=ryualdb6d8cgv8zd&location="+CityNameEn+"&language=zh-Hans&unit=c";

        sendRequestWithHttpURLConnection();
    }

    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private void setGuided(){
        SharedPreferences settings = getSharedPreferences(SHAREDPREFERENCES_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(KEY_GUIDE_ACTIVITY, "false");
        editor.commit();
    }
    private void getData(){
        SharedPreferences citydata = getSharedPreferences("data",MODE_PRIVATE);
        strName = citydata.getString("CityName","");
        strID = citydata.getString("CityID","");
    }
    private void setData(){
        SharedPreferences citydata = getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = citydata.edit();
        editor.putString("CityName",strName);
        editor.putString("CityID",strID);
        editor.commit();
    }




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




            String fengxiang = firstday.getString("fengxiang");
            String ganmao = data.getString("ganmao");

            String flowt = firstday.getString("low");
            String fhight = firstday.getString("high");
            String ftype = firstday.getString("type");
            String fdate  = firstday.getString("date");
            String finfo =fdate+"  "+ftype+"\n"+"最"+flowt+"\n"+"最"+fhight;

            String wendu = ftype+"  "+data.getString("wendu");




            String slowt = secondday.getString("low");
            String shight = secondday.getString("high");
            String stype = secondday.getString("type");
            String sdate  = secondday.getString("date");
            String sinfo =sdate+"  "+stype+"\n"+"最"+slowt+"\n"+"最"+shight;

            String tlowt = thirdday.getString("low");
            String thight = thirdday.getString("high");
            String ttype = thirdday.getString("type");
            String tdate  = thirdday.getString("date");
            String tinfo =tdate+"  "+ttype+"\n"+"最"+tlowt+"\n"+"最"+thight;

            String folowt = fourthdday.getString("low");
            String fohight = fourthdday.getString("high");
            String fotype = fourthdday.getString("type");
            String fodate  = fourthdday.getString("date");
            String foinfo =fodate+"  "+fotype+"\n"+"最"+folowt+"\n"+"最"+fohight;

            String filowt = fifthday.getString("low");
            String fihight = fifthday.getString("high");
            String fitype = fifthday.getString("type");
            String fidate  = fifthday.getString("date");
            String fiinfo =fidate+"  "+fitype+"\n"+"最"+filowt+"\n"+"最"+fihight;



            if(wendu==null)
            {wendu ="error";}
            else {
                Message message1 = new Message();
                message1.what = 1;
                message1.obj =wendu;
                mHandler.sendMessage(message1);

                Message message2 = new Message();
                message2.what = 2;
                message2.obj =fengxiang;
                mHandler.sendMessage(message2);

                Message message3 = new Message();
                message3.what = 3;
                message3.obj =ganmao;
                mHandler.sendMessage(message3);

                Message message4 = new Message();
                message4.what = 4;
                message4.obj =ftype;
                mHandler.sendMessage(message4);

                Message message5 = new Message();
                message5.what = 5;
                message5.obj =finfo;
                mHandler.sendMessage(message5);

                Message message6 = new Message();
                message6.what = 6;
                message6.obj =sinfo;
                mHandler.sendMessage(message6);

                Message message7 = new Message();
                message7.what = 7;
                message7.obj =tinfo;
                mHandler.sendMessage(message7);

                Message message8 = new Message();
                message8.what = 8;
                message8.obj =foinfo;
                mHandler.sendMessage(message8);

                Message message9 = new Message();
                message9.what = 9;
                message9.obj =fiinfo;
                mHandler.sendMessage(message9);

            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }

    class ChangeButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent =new Intent(Weather.this,SelectCity.class);
            startActivityForResult(intent,1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode){
            case RESULT_OK:
                strName = data.getStringExtra("CityName");
                //strNameEn = data.getStringExtra("CityNameEn");
                strID =data.getStringExtra("CityID");
                mHandler.post(ChgButtonText);
                break;
            default:
        }

    }

    Runnable ChgButtonText = new Runnable() {
        @Override
        public void run() {
            chgcitybutton.setText(strName);
            text_name.setText(strName+"  ");
            setData();
            WeatherUrl = WEATHER_URL+ strID;
            sendRequestWithHttpURLConnection();
        }
    };

}