package com.example.duckweather.util;

import android.text.TextUtils;
import android.util.Log;

import com.example.duckweather.db.City;
import com.example.duckweather.db.County;
import com.example.duckweather.db.Province;
import com.example.duckweather.gson.Weather;
import com.example.duckweather.gson.daily;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class Utility {
    /*解析和处理服务器返回的省级数据 */
    public static boolean handleProvinceResponse(String response){
        if (!TextUtils.isEmpty(response)){  //如果返回的数据不为空
            try {
                //将所有的省级数据解析出来，并组装成实体类对像
                JSONArray allProvinces = new JSONArray(response);
                for (int i=0;i<allProvinces.length();i++){
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setProvinceName(provinceObject.getString("name"));
                    province.setProvinceCode(provinceObject.getInt("id"));
                    //将该实体类对象存入数据库
                    province.save();
                }
                return true;//解析成功
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;//解析失败
    }

    /*解析和处理服务器返回的市级数据 */
    public static boolean handleCityResponse(String response,int provinceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCities = new JSONArray(response);
                for (int i=0;i<allCities.length();i++){
                    JSONObject cityObject = allCities.getJSONObject(i);
                    City city = new City();
                    city.setCityCode(cityObject.getInt("id"));
                    city.setCityName(cityObject.getString("name"));
                    city.setProvinceId(provinceId);  //所属的省级代号
                    city.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    /*解析和处理服务器返回的县级数据 */
    public static boolean handleCountyResponse(String response,int cityCode){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i=0;i<allCounties.length();i++){
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    //县级天气信息
                    county.setWeatherId(countyObject.getString("weather_id"));
                    //所属的市级代号
                    county.setCityId(cityCode);
                    county.setCountyCode(countyObject.getInt("id"));
                    county.save();
                }
                return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
    public static Weather handleWeatherResponse(String response){
        try {
            //通过JSONObject和JSONArray将天气数据中的主体内容解析出来
            /*JSONObject jsonObject = new JSONObject(response);
            Log.i("Test", "jsonObject:"+response);
            JSONArray jsonArray = jsonObject.getJSONArray("daily");
            String weatherContent = jsonArray.getJSONObject(0).toString();*/
            //将JSON数据转换成Weather对象
            JSONObject jsonObject = new JSONObject(response);
            Weather weather=new Weather();
            weather.code=jsonObject.getString("code");
            weather.updatetime=jsonObject.getString("updateTime");
            JSONArray jsonArray = jsonObject.getJSONArray("daily");
            Gson gson=new Gson();
            weather.daily_forecast=gson.fromJson(jsonArray.toString(), new TypeToken<List<daily>>() {
            }.getType());
            return weather;
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Test", "Fail");
        }
        return null;
    }
}
