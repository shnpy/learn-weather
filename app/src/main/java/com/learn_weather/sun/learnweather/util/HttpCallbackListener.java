package com.learn_weather.sun.learnweather.util;

/**
 * Created by Sun on 2016/10/20.
 */

public interface HttpCallbackListener {
    void onFinish(String response);
    void onError(Exception e);

}

