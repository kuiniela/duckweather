package com.example.duckweather.gson;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Weather {
    public String code;  //返回是否成功返回值
    public String updatetime;
    public List<daily> daily_forecast;
}
