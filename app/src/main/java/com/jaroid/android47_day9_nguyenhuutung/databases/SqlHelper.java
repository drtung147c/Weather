package com.jaroid.android47_day9_nguyenhuutung.databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SqlHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "Cities.db";
    private static final int VERSION = 1;
    private static final String TABLE_NAME = "City";
    private static final String NAME_COLUMN = "CityName";
    private static final String CREATE_TABLE = "CREATE TABLE "+ TABLE_NAME+ "("+ NAME_COLUMN +" TEXT NOT NULL);";
    private SQLiteDatabase sqLiteDatabase;

    public SqlHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addNewCity(String newCityName)
    {
        if(!checkCityIsExisted(newCityName)) {
            sqLiteDatabase = getWritableDatabase();

            ArrayList<String> listCityName = getListCityName();
            for (int i = 0; i < listCityName.size(); i++) {
                String cityName = listCityName.get(i);
                if (cityName.equals(newCityName)) {
                    sqLiteDatabase.delete(TABLE_NAME, NAME_COLUMN + " LIKE \'" + cityName + "\'", null);
                }
            }

            ContentValues contentValues = new ContentValues();
            contentValues.put(NAME_COLUMN, newCityName);

            sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
            sqLiteDatabase.close();
        }else {
            Log.d("SqlHelper", "addNewCity: city existed");
        }
    }

    private boolean checkCityIsExisted(String cityName){
        ArrayList<String> listCity = getListCityName();
        for(int i = 0; i < listCity.size(); i++)
        {
            if(listCity.get(i).equals(cityName)){
                return true;
            }
        }
        return  false;
    }

    public ArrayList<String> getListCityName()
    {
        ArrayList<String> listCity = new ArrayList<>();
        sqLiteDatabase = getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor cursor = sqLiteDatabase.rawQuery(sql, null);
        cursor.moveToFirst();
        while (cursor.isAfterLast() == false)
        {
            int indexColumn = cursor.getColumnIndex(NAME_COLUMN);
            String cityName = cursor.getString(indexColumn);
            listCity.add(cityName);
            cursor.moveToNext();
        }
        return listCity;
    }

    public void removeCityName(String cityName) {
        sqLiteDatabase = getWritableDatabase();

        sqLiteDatabase.delete(TABLE_NAME, NAME_COLUMN + " LIKE \'" + cityName + "\'", null);

        sqLiteDatabase.close();
    }
}
