package com.learn_weather.sun.learnweather.model;

/**
 * Created by Sun on 2016/10/19.
 */

public class Province {
    private int id;
    private String provinceName;
    private String provinveCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id=id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName=provinceName;
    }

    public String getProvinveCode() {
        return provinveCode;
    }

    public void setProvinveCode(String provinveCode) {
        this.provinveCode=provinveCode;
    }
}
