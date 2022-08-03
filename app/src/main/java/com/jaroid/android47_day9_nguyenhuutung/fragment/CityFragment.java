package com.jaroid.android47_day9_nguyenhuutung.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.jaroid.android47_day9_nguyenhuutung.R;
import com.jaroid.android47_day9_nguyenhuutung.activities.ForecastActivity;
import com.jaroid.android47_day9_nguyenhuutung.models.CityWeather;
import com.jaroid.android47_day9_nguyenhuutung.retrofit.IWeatherServices;
import com.jaroid.android47_day9_nguyenhuutung.retrofit.RetrofitClient;
import com.jaroid.android47_day9_nguyenhuutung.utils.Global;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CityFragment extends Fragment implements View.OnClickListener{

    @BindView(R.id.tvCityName)
    TextView tvCityName;
    @BindView(R.id.tvTemp)
    TextView tvTemp;
    @BindView(R.id.tvDescription)
    TextView tvDescription;
    @BindView(R.id.tvSpeedValue)
    TextView tvSpeedValue;
    @BindView(R.id.tvFeelingValue)
    TextView tvFeelingValue;
    @BindView(R.id.tvHumidityValue)
    TextView tvHumidityValue;
    @BindView(R.id.tvPressureValue)
    TextView tvPressureValue;
    @BindView(R.id.tvTimeZoneValue)
    TextView tvTimeZoneValue;
    @BindView(R.id.tvCountryValue)
    TextView tvCountryValue;
    @BindView(R.id.tvSunRiseValue)
    TextView tvSunRiseValue;
    @BindView(R.id.tvSunSetValue)
    TextView tvSunSetValue;
    @BindView(R.id.imgWeather)
    ImageView imgWeather;
    @BindView(R.id.btnFiveDaysThreeHours)
    Button btnFiveDaysThreeHours;


    private String mcityName = null;
    private IWeatherServices iWeatherServices;
    private Context mContext;

    public CityFragment() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static CityFragment newInstance(String cityName) {

        Bundle args = new Bundle();
        CityFragment fragment = new CityFragment();

        args.putString(Global.CITY_NAME_KEY, cityName);

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().getString(Global.CITY_NAME_KEY) != null) {
            this.mcityName = getArguments().getString(Global.CITY_NAME_KEY);
        } else {
            this.mcityName = "Hà Nội";
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_city, container, false);
        mContext = getActivity();
        initView(view);
        initData();
        return view;
    }

    private void initView(View view) {
        ButterKnife.bind(this, view);
        btnFiveDaysThreeHours.setOnClickListener(this);
    }

    private void initData() {
        iWeatherServices = RetrofitClient.createServices(IWeatherServices.class);
        iWeatherServices.getWeatherByCityName(this.mcityName, Global.APP_ID, "vi").enqueue(requestCityWeather);
    }

    private Callback<CityWeather> requestCityWeather = new Callback<CityWeather>() {

        @Override
        public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
            if (response.code() == 200) {
                CityWeather cityWeather = response.body();
                bindCityWeather(cityWeather);
            }
        }

        @Override
        public void onFailure(Call<CityWeather> call, Throwable t) {

        }
    };

    private void bindCityWeather(CityWeather cityWeather) {
        if (cityWeather != null) {
            tvCityName.setText(cityWeather.getName());
            int celsius = Global.convertKelvinToCelsius(cityWeather.getMain().getTemp());
            tvTemp.setText(celsius + Global.DEGREE_CHARACTER);
            String description = cityWeather.getWeather().get(0).getDescription();
            tvDescription.setText(description);
            String windSpeed = cityWeather.getWind().getSpeed() + " m/s ";
            tvSpeedValue.setText(windSpeed);
            tvFeelingValue.setText(Global.convertKelvinToCelsius(cityWeather.getMain().getFeelsLike()) + Global.DEGREE_CHARACTER);
            tvHumidityValue.setText(cityWeather.getMain().getHumidity() + Global.PERCENT_UNIT);
            tvPressureValue.setText(cityWeather.getMain().getPressure() + Global.HPA_UNIT);

            long epoch = 1655392954 ;
            Date date = new Date(epoch* 1000);
            DateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            format.setTimeZone(TimeZone.getTimeZone("ICT"));
            String formatted = format.format(date);
            tvTimeZoneValue.setText(formatted);

            tvCountryValue.setText(cityWeather.getSys().getCountry());

//            tvSunRiseValue.setText(cityWeather.getSys().getSunrise().toString());
//            tvSunSetValue.setText(cityWeather.getSys().getSunset().toString());

            Date timeSunRises = new Date(cityWeather.getSys().getSunrise() * 1000);

            SimpleDateFormat sunrise = new SimpleDateFormat("HH:mm:ss");
            String time = sunrise.format(timeSunRises);
            tvSunRiseValue.setText(time + " am");

            Date timeSunSets = new Date(cityWeather.getSys().getSunset() * 1000);
            SimpleDateFormat sunset = new SimpleDateFormat("HH:mm:ss");
            String time1 = sunset.format(timeSunSets);
            tvSunSetValue.setText(time1 + " pm");

            String imageLink = Global.URL_ICON + cityWeather.getWeather().get(0).getIcon() + Global.PICTURE_FORMAT;
            Glide.with(mContext).load(imageLink).into(imgWeather);
        } else {
            Toast.makeText(getContext(), "Không có dữ liệu", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnFiveDaysThreeHours:
                goToFiveDaysThreeHours();
                break;
        }
    }

    private void goToFiveDaysThreeHours() {

        Intent intent = new Intent(getActivity(), ForecastActivity.class);
        intent.putExtra(Global.CITY_NAME_KEY, mcityName);
        getActivity().startActivity(intent);
    }
}