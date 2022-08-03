package com.jaroid.android47_day9_nguyenhuutung.databases;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.jaroid.android47_day9_nguyenhuutung.models.City;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.converter.gson.GsonConverterFactory;

public class Database {

    private Context mContext;
    private ArrayList<City> mListCity;

    public Database(Context mContext) {
        this.mContext = mContext;
    }

    public ArrayList<City> getListCityFromAssets()
    {
        mListCity = new ArrayList<>();
        try {
            InputStream inputStream = mContext.getAssets().open("city.list.min.json");
            int count = inputStream.available();

            byte[] citiesByte = new byte[count];
            inputStream.read(citiesByte);
            inputStream.close();

            String citiesJson = new String(citiesByte,"UTF-8");

            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<City>>(){}.getType();
            mListCity = gson.fromJson(citiesJson, type);

        } catch (IOException e) {
            e.printStackTrace();
        }


        return mListCity;
    }
}

