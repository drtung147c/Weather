package com.jaroid.android47_day9_nguyenhuutung.retrofit;

import com.jaroid.android47_day9_nguyenhuutung.models.CityWeather;
import com.jaroid.android47_day9_nguyenhuutung.models.Forecast;
import com.jaroid.android47_day9_nguyenhuutung.utils.Global;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IWeatherServices {

    @GET(Global.CURRENT_WEATHER_BY_CITY_NAME)
    Call<CityWeather> getWeatherByCityName(@Query("q") String cityName,
                                           @Query("appid") String appId,
                                           @Query("lang") String Language);
    @GET(Global.CURRENT_WEATHER_BY_CITY_NAME)
    Call<CityWeather> getWeatherByLocation(@Query("lat") String lat,
                                           @Query("lon") String lon,
                                           @Query("appid") String appId,
                                           @Query("lang") String Language);
    @GET(Global.CURRENT_FORECAST_BY_CITY_NAME)
    Call<Forecast> getForecastByCityName(@Query("q") String cityName,
                                         @Query("appid") String appId,
                                         @Query("lang") String Language);
}
