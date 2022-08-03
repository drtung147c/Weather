package com.jaroid.android47_day9_nguyenhuutung.activities;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.jaroid.android47_day9_nguyenhuutung.R;
import com.jaroid.android47_day9_nguyenhuutung.adapters.CityViewPagerAdapter;
import com.jaroid.android47_day9_nguyenhuutung.adapters.HistoryAdapter;
import com.jaroid.android47_day9_nguyenhuutung.databases.Database;
import com.jaroid.android47_day9_nguyenhuutung.databases.SqlHelper;
import com.jaroid.android47_day9_nguyenhuutung.fragment.CityFragment;
import com.jaroid.android47_day9_nguyenhuutung.fragment.LoginFragment;
import com.jaroid.android47_day9_nguyenhuutung.models.City;
import com.jaroid.android47_day9_nguyenhuutung.models.CityWeather;
import com.jaroid.android47_day9_nguyenhuutung.models.Forecast;
import com.jaroid.android47_day9_nguyenhuutung.retrofit.IWeatherServices;
import com.jaroid.android47_day9_nguyenhuutung.retrofit.RetrofitClient;
import com.jaroid.android47_day9_nguyenhuutung.utils.DepthPageTransformer;
import com.jaroid.android47_day9_nguyenhuutung.utils.Global;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.relex.circleindicator.CircleIndicator;
import me.relex.circleindicator.CircleIndicator2;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = MainActivity.class.getName();
    private static final int REQUEST_CODE_PERMISSION = 123;
    @BindView(R.id.imgAdd)
    ImageView imgAdd;
    @BindView(R.id.imgLocation)
    ImageView imgLocation;
    @BindView(R.id.ciVpCityName)
    CircleIndicator ciVpCityName;
    @BindView(R.id.vpCityName)
    ViewPager vpCityName;
    @BindView(R.id.actvSearch)
    AutoCompleteTextView actvSearch;
    @BindView(R.id.imgSearch)
    ImageView imgSearch;
    @BindView(R.id.imgUser)
    ImageView imgUser;


    private ArrayList<String> mListCityName;
    private ArrayList<City> mListCities;
    private ArrayList<String> mListNameAssets;
    private CityViewPagerAdapter mCityViewPagerAdapter;
    private SqlHelper mSqlHelper;
    private static final int CLICKED_CITY = 1234;
    private int currentPosition = 0;
    private IWeatherServices mWeatherServices;
    private GoogleSignInOptions options;
    private GoogleSignInClient mGoogleSignInClient;
    private final int LOGIN_GOOGLE = 212345;
    private LoginFragment loginFragment;
    private final String DATABASE_URL = "https://newweather-4bfbb-default-rtdb.firebaseio.com/";
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference reference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initGoogle();

        //Test write firebase database
        firebaseDatabase = FirebaseDatabase.getInstance(DATABASE_URL);
        reference = firebaseDatabase.getReference();


//        reference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                String newCity = snapshot.getValue(String.class);
//                Log.d(TAG, "onDataChange: " + newCity);
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Log.d(TAG, "onCancelled: " + error.getMessage());
//            }
//        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    private void initGoogle() {
        options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, options);
    }

    @Override
    protected void onResume() {
        super.onResume();
        initData();
        initView();

        GoogleSignInAccount lastGoogleSignInAccount = GoogleSignIn.getLastSignedInAccount(getApplicationContext());
        if (lastGoogleSignInAccount != null){
            //login before
            Toast.makeText(getApplicationContext(),"Welcome : " + lastGoogleSignInAccount.getEmail(), Toast.LENGTH_SHORT).show();
            imgUser.setVisibility(View.GONE);
        }else {
            //Do something

        }
        DatabaseReference cityNameRef = reference.child("name");
        cityNameRef.setValue("LonDon");
        cityNameRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String newCity = snapshot.getValue(String.class);
                mListCityName.add(newCity);
                mSqlHelper.addNewCity(newCity);
                updateData(mListCityName);
                scrolllToNewest();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void initView() {
        ButterKnife.bind(this);
        imgSearch.setOnClickListener(this);
        imgLocation.setOnClickListener(this);
        imgAdd.setOnClickListener(this);
        imgUser.setOnClickListener(this);

        updateData(mListCityName);

        ciVpCityName.setViewPager(vpCityName);

        ArrayAdapter arrayAdapter = new ArrayAdapter(getApplicationContext(),
                R.layout.support_simple_spinner_dropdown_item,
                mListNameAssets);
        actvSearch.setThreshold(0);
        actvSearch.setAdapter(arrayAdapter);
        actvSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String clickedCity = parent.getItemAtPosition(position).toString();
                mListCityName.add(clickedCity);
                mSqlHelper.addNewCity(clickedCity);
                updateData(mListCityName);
                scrolllToNewest();
                DatabaseReference city = firebaseDatabase.getReference("name");
                city.setValue(clickedCity);
            }
        });


    }

    private void updateData(ArrayList<String> data) {
        mCityViewPagerAdapter = new CityViewPagerAdapter(getSupportFragmentManager(), data);
        vpCityName.setAdapter(mCityViewPagerAdapter);
        vpCityName.setPageTransformer(false, new DepthPageTransformer());
        mCityViewPagerAdapter.notifyDataSetChanged();
        ciVpCityName.setViewPager(vpCityName);
        if (currentPosition > 0) {
            vpCityName.setCurrentItem(currentPosition, true);
        }
    }

    private void scrolllToNewest() {
        vpCityName.setCurrentItem(mListCityName.size() - 1, true);
    }

    private void scrolllToPosition(int position) {
        vpCityName.setCurrentItem(position, true);
    }

    private void initData() {
        mListCityName = new ArrayList<>();
        mListCityName.clear();

        mSqlHelper = new SqlHelper(getApplicationContext());
        Log.d(TAG, "initData: " + mSqlHelper.getListCityName().toString());

        mListCityName.addAll(mSqlHelper.getListCityName());
        if (mListCityName.size() == 0) {
            mListCityName.add("Ha Noi");
            mSqlHelper.addNewCity("Ha Noi");
        }

        Database database = new Database(getApplicationContext());
        mListCities = database.getListCityFromAssets();
        mListNameAssets = new ArrayList<>();
        for (int i = 0; i < mListCities.size(); i++) {
            mListNameAssets.add(mListCities.get(i).getName());
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgSearch:
                addCityByName();
                break;
            case R.id.imgAdd:
                gotoHistoryActivity();
                break;
            case R.id.imgLocation:
                getLocation();
                break;
            case R.id.imgUser:
                showLoginForm();
                break;
        }
    }

    private void addCityByName() {
        String currentCity = actvSearch.getText().toString();
        mWeatherServices = RetrofitClient.createServices(IWeatherServices.class);
        mWeatherServices.getWeatherByCityName(currentCity,Global.APP_ID,"vn").enqueue(new Callback<CityWeather>() {
            @Override
            public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                Log.d(TAG, "onResponse: "+response.toString());
                if (response.code() == 404){
                    actvSearch.setText("");
                    Toast.makeText(getApplicationContext(), ""+response.message(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.code() == 200){
                    CityWeather cityWeather = response.body();
                    String city = cityWeather.getName();
                    mSqlHelper.addNewCity(city);
                    mListCityName.clear();
                    mListCityName.addAll(mSqlHelper.getListCityName());
                    updateData(mListCityName);
                }

            }

            @Override
            public void onFailure(Call<CityWeather> call, Throwable t) {

            }
        });
    }

    private void showLoginForm() {
        loginFragment = LoginFragment.newInstance();
        loginFragment.setmGoogleSignInClient(mGoogleSignInClient);
        getSupportFragmentManager().beginTransaction().add(R.id.container1, loginFragment).commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void getLocation() {
        if (ContextCompat.checkSelfPermission(
                getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "getLocation: granted");
            getCurrentLocation();
        } else {
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_PERMISSION);
        }
    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = new FusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            Toast.makeText(getApplicationContext(), "Please grant location permission", Toast.LENGTH_SHORT).show();
            return;
        }
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                addCityByLocation(location.getLatitude(), location.getLongitude());
            }
        });
    }

    private void addCityByLocation(double lat, double lon) {
        mWeatherServices = RetrofitClient.createServices(IWeatherServices.class);
        Log.d(TAG, "addCityByLocation: "+lat+" | "+lon);
        mWeatherServices.getWeatherByLocation(lat+"", lon+"",Global.APP_ID, "en").enqueue(new Callback<CityWeather>() {
            @Override
            public void onResponse(Call<CityWeather> call, Response<CityWeather> response) {
                if(response.code() == 200){
                    CityWeather cityWeather = response.body();
                    String city = cityWeather.getName();
                    mSqlHelper.addNewCity(city);
                    mListCityName.clear();
                    mListCityName.addAll(mSqlHelper.getListCityName());
                    updateData(mListCityName);
                }
            }

            @Override
            public void onFailure(Call<CityWeather> call, Throwable t) {

            }
        });

    }

    private void gotoHistoryActivity() {
        Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
        startActivityForResult(intent, CLICKED_CITY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == CLICKED_CITY){
            if(resultCode == RESULT_OK){
                int position = data.getIntExtra("POSITION", 0);
                currentPosition = position;
                vpCityName.setCurrentItem(position, true);
            }
            if(resultCode == RESULT_CANCELED){

            }
        }
        if(requestCode == REQUEST_CODE_PERMISSION){
            getCurrentLocation();
        }
        if(requestCode == LOGIN_GOOGLE){
            Task<GoogleSignInAccount> accountTask = GoogleSignIn.getSignedInAccountFromIntent(data);
            getInfo(accountTask);
        }
    }
    private void getInfo(Task<GoogleSignInAccount> accountTask) {
        try {
            GoogleSignInAccount googleSignInAccount = accountTask.getResult(ApiException.class);
            Log.d(TAG, "getInfo: " + googleSignInAccount.getEmail());
            Toast.makeText(getApplicationContext(),"Welcome : " + googleSignInAccount.getEmail(), Toast.LENGTH_SHORT).show();
            getSupportFragmentManager().beginTransaction().remove(loginFragment).commit();
        } catch (ApiException e) {
            e.printStackTrace();
        }
    }
}