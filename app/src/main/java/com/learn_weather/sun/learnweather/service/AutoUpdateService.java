package com.learn_weather.sun.learnweather.service;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.util.Log;

import com.learn_weather.sun.learnweather.receiver.AutoUpdateReceiver;
import com.learn_weather.sun.learnweather.util.HttpCallbackListener;
import com.learn_weather.sun.learnweather.util.HttpUtil;
import com.learn_weather.sun.learnweather.util.Utility;

/**
 * Created by Sun on 2016/10/23.
 */

public class AutoUpdateService extends Service {
     private static final String TAG="TT";
    private final String localTag=getClass().getSimpleName() + "__";

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, localTag + "run: the thread to updateWeather  " +
                        "start");
                 updateWeather();

            }
        }).start();
        AlarmManager manager=(AlarmManager)getSystemService(ALARM_SERVICE);
        int eightHour=8*3600*1000;
        long triggerAtTime=SystemClock.elapsedRealtime()+eightHour  ;
        Intent i=new Intent(this,AutoUpdateReceiver.class);
        PendingIntent pi=PendingIntent.getBroadcast(this,0,i,0);
        Log.d(TAG, localTag + "onStartCommand: the pendingIntent to start the" +
                " receiver per 8 hours has been set");
        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP,triggerAtTime,pi);
        return super.onStartCommand(intent, flags, startId);
    }
    
    private void updateWeather() {
        SharedPreferences prefs=PreferenceManager.getDefaultSharedPreferences
                (this);
        String weatherCode=prefs.getString("weather_code","");
        String address="http://www.weather.com.cn/data/cityinfo/" +
                weatherCode + ".html";
        HttpUtil.sendHttpRequest(address, new HttpCallbackListener() {
            @Override
            public void onFinish(String response) {
                Utility.handleWeatherResponse(AutoUpdateService.this,response);
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();

            }
        });

    }
}
