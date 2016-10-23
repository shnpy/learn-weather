package com.learn_weather.sun.learnweather.util;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.util.Log;

import com.learn_weather.sun.learnweather.db.LearnWeatherDB;
import com.learn_weather.sun.learnweather.model.City;
import com.learn_weather.sun.learnweather.model.County;
import com.learn_weather.sun.learnweather.model.Province;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Sun on 2016/10/20.
 */
@TargetApi(value=19)
public class Utility {
     private static final String TAG="TT";
    private static final String localTag="Utility" + "__";

    public synchronized static boolean handleProvincesResponse(LearnWeatherDB
                                                                       learnWeatherDB, String response) {
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces=response.split(",");
            if (allProvinces != null && allProvinces.length > 0) {
                for (String p : allProvinces) {
                    String[] array=p.split("\\|");
                    Province province=new Province();
                    province.setProvinveCode(array[0]);
                    province.setProvinceName(array[1]);

                    learnWeatherDB.saveProvince(province);
                }
            }
            return true;
        }
        return false;

    }

    public static boolean handleCityResponse(LearnWeatherDB learnWeatherDB,
                                             String response, int provinceId) {
        if (!TextUtils.isEmpty(response)) {
            String[] allCities=response.split(",");
            if (allCities != null && allCities.length > 0) {
                for (String c : allCities) {
                    String[] array=c.split("\\|");
                    City city=new City();
                    city.setCityCode(array[0]);
                    city.setCityName(array[1]);
                    city.setProvinceId(provinceId);

                    learnWeatherDB.saveCity(city);
                }
                return true;

            }
        }
        return false;
    }

    public static boolean handleCountiesResponse(LearnWeatherDB
                                                         learnWeatherDB,
                                                 String response, int cityId) {
        if (!TextUtils.isEmpty(response)) {
            String[] counties=response.split(",");
            if (counties != null && counties.length > 0) {
                for (String c : counties) {
                    String[] array=c.split("\\|");
                    County county=new County();
                    county.setCountyCode(array[0]);
                    county.setCountyName(array[1]);
                    county.setCityId(cityId);
                    learnWeatherDB.saveCounty(county);
                }
                return true;
            }
        }
        return false;
    }

    public static void handleWeatherResponse(Context context, String response) {
        try {
            Log.d(TAG, localTag + "handleWeatherResponse: begin to handle " +
                    "weather info of json");
            JSONObject jsonObject=new JSONObject(response);
            JSONObject weatherInfo=jsonObject.getJSONObject("weatherinfo");
            String cityName=weatherInfo.getString("city");
            String weatherCode=weatherInfo.getString("cityid");
            String temp1=weatherInfo.getString("temp1");
            String temp2=weatherInfo.getString("temp2");
            String weatherDesp=weatherInfo.getString("weather");
            String publishTime=weatherInfo.getString("ptime");
            saveWeatherInfo(context, cityName, weatherCode, temp1, temp2,
                    weatherDesp, publishTime);
        } catch (JSONException e) {
            Log.d(TAG, localTag + "handleWeatherResponse: error:"+e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * 将服务器返回的所有天气信息存储到SharedPreferences文件中。
     */
    public static void saveWeatherInfo(Context context, String cityName,
                                       String weatherCode, String temp1,
                                       String temp2, String weatherDesp,
                                       String publishTime) {
        Log.d(TAG, localTag + "saveWeatherInfo: begin to save the weather " +
                "info");

        SimpleDateFormat sdf=new SimpleDateFormat("yyyy年M月d日",Locale.CHINA);
        SharedPreferences.Editor editor=PreferenceManager
                .getDefaultSharedPreferences(context).edit();
        editor.putBoolean("city_selected", true);
        editor.putString("city_name", cityName);
        editor.putString("weather_code", weatherCode);
        editor.putString("temp1", temp1);
        editor.putString("temp2", temp2);
        editor.putString("weather_desp", weatherDesp);
        editor.putString("publish_time", publishTime);
        editor.putString("current_date", sdf.format(new Date()));
        editor.commit();
    }


}
