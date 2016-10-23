package com.learn_weather.sun.learnweather.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.learn_weather.sun.learnweather.R;
import com.learn_weather.sun.learnweather.service.AutoUpdateService;
import com.learn_weather.sun.learnweather.util.HttpCallbackListener;
import com.learn_weather.sun.learnweather.util.HttpUtil;
import com.learn_weather.sun.learnweather.util.Utility;

/**
 * Created by Sun on 2016/10/20.
 */

public class WeatherActivity extends Activity {
     private static final String TAG="TT";
    private final String localTag=getClass().getSimpleName() + "__";

    private LinearLayout weatherInfoLayout;

    private TextView cityNameText;
    private TextView publishText;
    private TextView weatherDespText;
    private TextView temp1Text;
    private TextView temp2Text;
    private TextView currentDateText;
    private Button switchCity;
    private Button refreshWeather;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.d(TAG, localTag + "onCreate: start");


        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.weather_layout);

        weatherInfoLayout=(LinearLayout) findViewById(R.id.weather_info_layout);
        cityNameText=(TextView)findViewById(R.id.city_name);
        publishText=(TextView) findViewById(R.id.publish_text);
        weatherDespText=(TextView) findViewById(R.id.weather_desp);
        temp1Text=(TextView) findViewById(R.id.temp1);
        temp2Text=(TextView) findViewById(R.id.temp2);
        currentDateText=(TextView) findViewById(R.id.current_date);
        switchCity=(Button) findViewById(R.id.switch_city);
        refreshWeather=(Button) findViewById(R.id.refresh_weather);
        String countyCode=getIntent().getStringExtra("county_code");
        if (!TextUtils.isEmpty(countyCode)) {

            publishText.setText("同步中……");
            weatherInfoLayout.setVisibility(View.INVISIBLE);
            cityNameText.setVisibility(View.INVISIBLE);
            queryWetherCode(countyCode);
        } else {
            showWeather();

        }
        switchCity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WeatherActivity.this,
                        ChooseAreaActivity.class);
                intent.putExtra("from_weather_activity", true);
                startActivity(intent);
                finish();
            }
        });

        refreshWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                publishText.setText("同步中...");
                SharedPreferences prefs=PreferenceManager
                        .getDefaultSharedPreferences(WeatherActivity.this);
                String weatherCode=prefs.getString("weather_code", "");
                if (!TextUtils.isEmpty(weatherCode)) {
                    queryWetherInfo(weatherCode);
                }
            }
        });


    }



    /**
     * 查询县级代号所对应的天气代号。
     */
    private void queryWetherCode(String countyCode) {
        String address="http://www.weather.com.cn/data/list3/city" +
                countyCode + ".xml";
        queryFromServer(address, "countyCode");
    }

    /**
     * 查询天气代号所对应的天气。
     */
    private void queryWetherInfo(String weatherCode) {
        String address="http://www.weather.com.cn/data/cityinfo/" +
                weatherCode + ".html";
        Log.d(TAG, localTag + "queryWetherInfo: query the address:"+address);
        queryFromServer(address, "weatherCode");
    }


    /**
     * 根据传入的地址和类型去向服务器查询天气代号或者天气信息。
     */
    private void queryFromServer(final String address, final String type) {
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {

                Log.d(TAG, localTag + "onFinish: handle the response!  " +
                        "type="+type);
                if ("countyCode".equals(type)) {
                    Log.d(TAG, localTag + "onFinish: handle the countyCode");
                    if (!TextUtils.isEmpty(response)) {
                        Log.d(TAG, localTag + "onFinish: response is not " +
                                "empty! response="+response);
                        String[] array=response.split("\\|");
                        if (array != null && array.length == 2) {
                            String weatherCode=array[1];
                            queryWetherInfo(weatherCode);
                        }else {
                            Log.d(TAG, localTag + "onFinish: tha response has" +
                                    " been cut!");
                        }
                    }
                } else if ("weatherCode".equals(type)) {
                    Log.d(TAG, localTag + "onFinish: handle the weather code");
                    Utility.handleWeatherResponse(WeatherActivity.this, response);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, localTag + "run: begin to show weather");
                            showWeather();
                        }
                    });
                }
            }

                @Override public void onError (final Exception e){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, localTag + "\nrun: onError:"+e.toString());
                            publishText.setText("同步失败");
                        }
                    });
                }
            });
        }

    /**
     * 从SharedPreferences文件中读取存储的天气信息，并显示到界面上。
     */
    private void showWeather() {
        Log.d(TAG, localTag + "showWeather: started");

        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences
                (this);
        String cityName=prefs.getString("city_name", "未找到");
        String temp1=prefs.getString("temp1", "");
        String temp2=prefs.getString("temp2", "");
        String weatherDeps=prefs.getString("weather_desp", "");
        String publishTime=prefs.getString("publish_time", "");
        String currentDate=prefs.getString("current_date", "");
        Log.d(TAG, localTag + "showWeather: \n"+
        cityName+" "+temp1+"-"+temp2+" "+weatherDeps+"  "+currentDate);

        Log.d(TAG, localTag + "showWeather:cityname="+cityName);
        temp1Text.setText(temp1);
        temp2Text.setText(temp2);
        weatherDespText.setText(weatherDeps);
        publishText.setText("今天" + publishTime + "发布");
        currentDateText.setText(currentDate);
        cityNameText.setText(cityName);
        weatherInfoLayout.setVisibility(View.VISIBLE);
        cityNameText.setVisibility(View.VISIBLE);
        Intent intent=new Intent(this, AutoUpdateService.class);
        startService(intent);

    }

    }

