package com.dongua.geather;

import android.os.Message;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by dongua on 2016/6/5.
 */
public class JsonHandle {

    static ArrayList<String> iconList = new ArrayList<String>();
    static ArrayList<String> infoString = new ArrayList<String>();
//    final String WEATHER_URL ="http://wthrcdn.etouch.cn/weather_mini?citykey=";
    static String WeatherUrl;
    public void getURL(String URL){
        this.WeatherUrl = URL;
    }

    public void sendRequestWithHttpURLConnection(){
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

    public void parseJSONWithJSONObject(String jsonData){
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
            iconList.add(ftype);


            String wendu = ftype+"  "+data.getString("wendu");
            String fengxiang = firstday.getString("fengxiang");
            String ganmao = data.getString("ganmao");

            infoString.add(wendu);
            infoString.add(fengxiang);
            infoString.add(ganmao);
            infoString.add(finfo);

            String slowt = secondday.getString("low");
            String shight = secondday.getString("high");
            String stype = secondday.getString("type");
            String sdate  = secondday.getString("date");
            String sinfo =sdate+"  "+stype+"\n"+slowt+"\n"+shight;
            iconList.add(stype);
            infoString.add(sinfo);

            String tlowt = thirdday.getString("low");
            String thight = thirdday.getString("high");
            String ttype = thirdday.getString("type");
            String tdate  = thirdday.getString("date");
            String tinfo =tdate+"  "+ttype+"\n"+tlowt+"\n"+thight;
            iconList.add(ttype);
            infoString.add(tinfo);


            String folowt = fourthdday.getString("low");
            String fohight = fourthdday.getString("high");
            String fotype = fourthdday.getString("type");
            String fodate  = fourthdday.getString("date");
            String foinfo =fodate+"  "+fotype+"\n"+folowt+"\n"+fohight;
            iconList.add(fotype);
            infoString.add(foinfo);

            String filowt = fifthday.getString("low");
            String fihight = fifthday.getString("high");
            String fitype = fifthday.getString("type");
            String fidate  = fifthday.getString("date");
            String fiinfo =fidate+"  "+fitype+"\n"+filowt+"\n"+fihight;
            iconList.add(fitype);
            infoString.add(fiinfo);

            /**在这里最后得到2个List  图标和信息*/

//
//            if(wendu==null)
//            {wendu ="error";}
//            else {
//                Message message10 = new Message();
//                message10.what = 10;
//                message10.obj =iconList;
//                mHandler.sendMessage(message10);
//
//                Message message11 = new Message();
//                message11.what = 11;
//                message11.obj =infoString;
//                mHandler.sendMessage(message11);
//
//            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
}
