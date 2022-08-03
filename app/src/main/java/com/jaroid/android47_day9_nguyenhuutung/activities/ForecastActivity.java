package com.jaroid.android47_day9_nguyenhuutung.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.jaroid.android47_day9_nguyenhuutung.R;
import com.jaroid.android47_day9_nguyenhuutung.adapters.ForecastAdapter;
import com.jaroid.android47_day9_nguyenhuutung.asynctask.SortHourAsyncTask;
import com.jaroid.android47_day9_nguyenhuutung.interfaces.IIClickItemRecycleView;
import com.jaroid.android47_day9_nguyenhuutung.interfaces.ISortedHoursListener;
import com.jaroid.android47_day9_nguyenhuutung.models.DataHours;
import com.jaroid.android47_day9_nguyenhuutung.models.Day;
import com.jaroid.android47_day9_nguyenhuutung.models.Forecast;
import com.jaroid.android47_day9_nguyenhuutung.retrofit.IWeatherServices;
import com.jaroid.android47_day9_nguyenhuutung.retrofit.RetrofitClient;
import com.jaroid.android47_day9_nguyenhuutung.utils.Global;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForecastActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String TAG = ForecastActivity.class.getName();

    @BindView(R.id.imgBack)
    ImageView imgBack;
    @BindView(R.id.rv5Day3Hours)
    RecyclerView rv5Day3Hours;

    private String mCityName;
    private IWeatherServices mWeatherServices;
    private ArrayList<Day> mDays;
    private ForecastAdapter mForecastAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        initData();
    }

    private void initData() {

        mCityName = getIntent().getStringExtra(Global.CITY_NAME_KEY);
        mWeatherServices = RetrofitClient.createServices(IWeatherServices.class);
        mWeatherServices.getForecastByCityName("Hà Nội", Global.APP_ID, "vi").enqueue(new Callback<Forecast>() {
            @Override
            public void onResponse(Call<Forecast> call, Response<Forecast> response) {
                if(response.code() == 200 && response.message().equals("OK")){
                    Forecast forecast = response.body();
                    new SortHourAsyncTask(forecast.getList(), sortedHoursListener).execute();
                }
            }

            @Override
            public void onFailure(Call<Forecast> call, Throwable t) {

            }
        });

    }

    private ISortedHoursListener sortedHoursListener = new ISortedHoursListener() {
        @Override
        public void onSortedHoursListener(ArrayList<Day> days) {

            mDays = new ArrayList<>();
            mDays.clear();
            mDays.addAll(days);
            initView();
        }
    };
    private void initView() {
        ButterKnife.bind(this);
        imgBack.setOnClickListener(this);

        if(mDays != null && mDays.size() > 0) {
            mForecastAdapter = new ForecastAdapter(mDays);
            rv5Day3Hours.setAdapter(mForecastAdapter);
            mForecastAdapter.setCallback(clickItemRecycleView);
            mForecastAdapter.notifyDataSetChanged();
        }else {
            Toast.makeText(getApplicationContext(), "Do not have forecast data ", Toast.LENGTH_SHORT).show();
        }
    }

    private IIClickItemRecycleView clickItemRecycleView = new IIClickItemRecycleView() {
        @Override
        public void onItemClickListener(View view, int position) {
            Day day = mDays.get(position);
            day.setExpand(!day.isExpand());
            mDays.set(position, day);
            mForecastAdapter.notifyItemChanged(position);
        }
    };


    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.imgBack:
                finish();
                break;
        }

    }
}
