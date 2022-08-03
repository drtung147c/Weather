package com.jaroid.android47_day9_nguyenhuutung.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jaroid.android47_day9_nguyenhuutung.R;
import com.jaroid.android47_day9_nguyenhuutung.adapters.HistoryAdapter;
import com.jaroid.android47_day9_nguyenhuutung.databases.SqlHelper;
import com.jaroid.android47_day9_nguyenhuutung.interfaces.IIClickItemRecycleView;
import com.jaroid.android47_day9_nguyenhuutung.models.CityWeather;
import com.jaroid.android47_day9_nguyenhuutung.retrofit.IWeatherServices;
import com.jaroid.android47_day9_nguyenhuutung.retrofit.RetrofitClient;
import com.jaroid.android47_day9_nguyenhuutung.utils.DeletionSwipeHelper;
import com.jaroid.android47_day9_nguyenhuutung.utils.Global;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HistoryActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.llLoading)
    LinearLayout llLoading;
    @BindView(R.id.rvHistory)
    RecyclerView rvHistory;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @BindView(R.id.imgBack)
    ImageView imgBack;

    private IWeatherServices iWeatherServices;
    private ArrayList<CityWeather> mListCityWeather;
    private ArrayList<String> mListCityName;
    private Callback<CityWeather> cityWeather;
    private HistoryAdapter mHistoryAdapter;
    private SqlHelper mSqlHelper;

    private static final int CLICKED_CITY = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        mListCityWeather = new ArrayList<>();
        mListCityWeather.clear();
        mListCityName = new ArrayList<>();
        mListCityName.clear();
        mSqlHelper = new SqlHelper(getApplicationContext());
        mListCityName.addAll(mSqlHelper.getListCityName());
        iWeatherServices = RetrofitClient.createServices(IWeatherServices.class);
        if( mListCityName.size() > 0)
        {
            loadData(mListCityName);
        }else
        {
            //Do somethingg
        }
    }

    private void loadData(ArrayList<String> listCityName) {

        llLoading.setVisibility(View.VISIBLE);

        for(int i = 0; i < listCityName.size(); i++)
        {
            String cityName = listCityName.get(i);
            iWeatherServices.getWeatherByCityName(cityName, Global.APP_ID, "en").enqueue(cityWeatherCallback(i));
        }
    }

    private Callback<CityWeather> cityWeatherCallback(int position){
        Callback<CityWeather> cityWeatherCallback = new Callback<CityWeather>() {
            @Override
            public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                if(response.code() == 200)
                {
                    CityWeather cityWeather = response.body();
                    mListCityWeather.add(cityWeather);
                    if(position == mListCityName.size() - 1){
                        mHistoryAdapter = new HistoryAdapter(mListCityWeather);
                        mHistoryAdapter.setCallback(clickItemRecycleView);
                        rvHistory.setAdapter(mHistoryAdapter);
                        llLoading.setVisibility(View.GONE);
                        mHistoryAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<CityWeather> call, Throwable t) {
                Toast.makeText(getApplicationContext(), "Fail when load city",Toast.LENGTH_SHORT).show();
            }
        };
        return cityWeatherCallback;
    }

    private IIClickItemRecycleView clickItemRecycleView = new IIClickItemRecycleView() {
        @Override
        public void onItemClickListener(View view, int position) {
            backToMain(position);
        }
    };

    private void backToMain(int position) {
        Intent intent = new Intent();
        intent.putExtra("POSITION", position);
        setResult(RESULT_OK,intent);
        finish();
    }

    private void initView() {

        ButterKnife.bind(this);
        imgBack.setOnClickListener(this);
        imgSearch.setOnClickListener(this);

        ItemTouchHelper.Callback iteCallback = new DeletionSwipeHelper(0,0, getApplicationContext(), swipeListener);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(iteCallback);
        itemTouchHelper.attachToRecyclerView(rvHistory);

    }

    private DeletionSwipeHelper.OnSwipeListener swipeListener = (viewHolder, position) -> {
        String cityName = mListCityName.get(position);
        mSqlHelper.removeCityName(cityName);
        mListCityWeather.remove(position);
        mHistoryAdapter.notifyDataSetChanged();
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.imgBack:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }
    }
}