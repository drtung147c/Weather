package com.jaroid.android47_day9_nguyenhuutung.utils;

public class Global {

    public static final  String IMAGE_WEATHER = "https://tse2.mm.bing.net/th?id=OIP.XwQR3nYxLJ5Xt_SkDl6cogHaHa&pid=Api&P=0&w=159&h=159";
    public static final String CURRENT_WEATHER_BY_CITY_NAME = "weather?";
    public static final String CURRENT_FORECAST_BY_CITY_NAME = "forecast?";
    public static final String APP_ID = "0ada167f744651019504adc9d662d900";
    public static final String CITY_NAME_KEY = "CITY_NAME";
    public static final String DEGREE_CHARACTER ="\u2103";

    public static final String PERCENT_UNIT = "%";

    public static final String HPA_UNIT = "hPa";

    public static final String URL_ICON = "http://openweathermap.org/img/wn/";
    public static final String PICTURE_FORMAT = ".png";

    public static int convertKelvinToCelsius(float kelVin)
    {
        return (int) Math.round(kelVin- 273.15) ;
    }
}
