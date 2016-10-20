package com.learn_weather.sun.learnweather.util;

import android.text.TextUtils;

import com.learn_weather.sun.learnweather.db.LearnWeatherDB;
import com.learn_weather.sun.learnweather.model.City;
import com.learn_weather.sun.learnweather.model.County;
import com.learn_weather.sun.learnweather.model.Province;

/**
 * Created by Sun on 2016/10/20.
 */

public class Utility {

    public synchronized static boolean handleProvincesResponse
            (LearnWeatherDB learnWeatherDB, String response){
        if (!TextUtils.isEmpty(response)) {
            String[] allProvinces=response.split(",");
            if (allProvinces != null&&allProvinces.length>0) {
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
                                             String response,int provinceId){
        if(!TextUtils.isEmpty(response)){
            String[] allCities=response.split(",");
            if (allCities != null&&allCities.length>0) {
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

    public static boolean handleCountiesResponse(LearnWeatherDB learnWeatherDB,
                                                 String response,int cityId){
        if (!TextUtils.isEmpty(response)) {
            String[] counties=response.split(",");
            if (counties != null&&counties.length>0) {
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


}
