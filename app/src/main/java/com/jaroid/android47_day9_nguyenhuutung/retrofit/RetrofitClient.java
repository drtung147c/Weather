package com.jaroid.android47_day9_nguyenhuutung.retrofit;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static String BASE_URL = "https://api.openweathermap.org/data/2.5/";
    private static Retrofit.Builder builder = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create());
    private static Retrofit retrofit;

    public static Retrofit getInstance()
    {
        if (retrofit == null)
        {
            retrofit = builder.build();
        }
        return retrofit;
    }

    public static <T> T createServices(Class<T> servicesClass)
    {
        return getInstance().create(servicesClass);
    }

}
