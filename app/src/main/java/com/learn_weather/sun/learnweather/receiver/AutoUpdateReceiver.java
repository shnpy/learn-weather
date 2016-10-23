package com.learn_weather.sun.learnweather.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.learn_weather.sun.learnweather.service.AutoUpdateService;

/**
 * Created by Sun on 2016/10/23.
 */

public class AutoUpdateReceiver extends BroadcastReceiver {
     private static final String TAG="TT";
    private final String localTag=getClass().getSimpleName() + "__";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, localTag + "onReceive: the autoUpdateService will be " +
                "awakend regularly");
        Intent i=new Intent(context, AutoUpdateService.class);
        context.startService(i);

    }
}
