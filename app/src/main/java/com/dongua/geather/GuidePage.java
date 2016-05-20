package com.dongua.geather;


        import android.app.Activity;
        import android.content.Context;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.os.Bundle;
        import android.os.Handler;
        import android.os.Message;
        import android.view.View;
        import android.widget.Button;
        import android.widget.TextView;
        import android.widget.Toast;

        import java.io.File;
        import java.io.FileOutputStream;
        import java.io.InputStream;
        import java.io.OutputStream;

public  class GuidePage extends Activity {



    Button CityButton;
    Button YesButton;
    TextView NameText;
    String strName="未选择城市";
    String strID;
    String strNameEn;
    String NameResult;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.guidepage);
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


        CityButton = (Button)findViewById(R.id.CityButton);
        CityButton.setOnClickListener(new CityButtonListener());
        NameText = (TextView)findViewById(R.id.CityName);
        YesButton = (Button)findViewById(R.id.Enter);
        YesButton.setOnClickListener(new YesButtonListener());

        boolean mFirst = isFirstEnter(GuidePage.this,GuidePage.this.getClass().getName());
        if(!mFirst){
            mHandler.sendEmptyMessage(SWITCH_WEATHER);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (resultCode){
            case RESULT_OK:
                strName = data.getStringExtra("CityName");
                //strNameEn = data.getStringExtra("CityNameEn");

                strID =data.getStringExtra("CityID");
                //Message nameMsg = Message.obtain();
                //nameMsg.obj = strName;
                //mHandler.sendMessage(nameMsg);
                mHandler.post(getCityName);
                break;
            default:
        }

    }


    Runnable getCityName = new Runnable() {
        @Override
        public void run() {
            NameText.setText(strName);
        }
    };

    class CityButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent =new Intent(GuidePage.this,SelectCity.class);
            startActivityForResult(intent,1);
        }
    }
    class YesButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if(strName == null){
                Toast.makeText(GuidePage.this,"你没有选择城市名字",Toast.LENGTH_SHORT);
            }
            else{
                Intent intent =new Intent(GuidePage.this,Weather.class);
//                intent.putExtra("CityID",strID);
//                intent.putExtra("CityName",strName);
                SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
                editor.putString("CityName",strName);
                editor.putString("CityID",strID);
                editor.putBoolean("IsFirst",true);
                editor.commit();
                startActivity(intent);
                finish();
            }
        }
    }

    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private boolean isFirstEnter(Context context,String className){
        if(context==null || className==null||"".equalsIgnoreCase(className))return false;
        String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE)
                .getString(KEY_GUIDE_ACTIVITY, "");//取得所有类名 如 com.my.MainActivity
        if(mResultStr.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }

    //*************************************************
    // Handler:跳转至不同页面
    //*************************************************
    private final static int SWITCH_WEATHER = 1000;
    private final static int SWITCH_GUIDACTIVITY = 1001;
    public Handler mHandler = new Handler(){
        public void handleMessage(Message msg) {
            switch(msg.what){
                case SWITCH_WEATHER:
                    Intent mIntent =new Intent(GuidePage.this,Weather.class);
                    startActivity(mIntent);
                    finish();
                    break;
            }
            super.handleMessage(msg);
        }
    };

}