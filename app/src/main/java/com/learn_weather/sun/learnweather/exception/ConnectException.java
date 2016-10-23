package com.learn_weather.sun.learnweather.exception;

/**
 * Created by Sun on 2016/10/21.
 */

public class ConnectException extends Exception {

    public ConnectException() {
    }
    public ConnectException(String message){
        super(message);
    }
}
