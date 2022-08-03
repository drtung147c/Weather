package com.jaroid.android47_day9_nguyenhuutung.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jaroid.android47_day9_nguyenhuutung.R;
import com.jaroid.android47_day9_nguyenhuutung.interfaces.IIClickItemRecycleView;
import com.jaroid.android47_day9_nguyenhuutung.models.CityWeather;
import com.jaroid.android47_day9_nguyenhuutung.utils.Global;

import java.util.ArrayList;

import butterknife.BindView;
import retrofit2.Callback;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder> {


    private Context mContext;
    private ArrayList<CityWeather> mListCityWeather;
    private IIClickItemRecycleView callback;

    public HistoryAdapter(ArrayList<CityWeather> mListCityWeather) {
        this.mListCityWeather = mListCityWeather;
    }

    public void setCallback(IIClickItemRecycleView callback) {
        this.callback = callback;
    }

    @NonNull
    @Override
    public HistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_item_history, parent, false);

        return new HistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HistoryViewHolder holder, int position) {
        CityWeather cityWeather = mListCityWeather.get(position);
        holder.tvCityName.setText(cityWeather.getName());
        holder.tvCountry.setText(cityWeather.getSys().getCountry());
        holder.tvTemp.setText(Global.convertKelvinToCelsius(cityWeather.getMain().getTemp()) + Global.DEGREE_CHARACTER);

        String detail = "Humidity: "+ cityWeather.getMain().getHumidity()+"% | " +
                convertDegreeToCardinalDirection(cityWeather.getWind().getDeg())+ " | " +
                cityWeather.getWind().getSpeed()+"m/s";
        holder.tvDetails.setText(detail);

        String imageLink = Global.URL_ICON + cityWeather.getWeather().get(0).getIcon() + Global.PICTURE_FORMAT;
        Glide.with(mContext).load(imageLink).into(holder.imgWeather);
    }

    public String convertDegreeToCardinalDirection(int directionInDegrees){
        String cardinalDirection = null;
        if( (directionInDegrees >= 348.75) && (directionInDegrees <= 360) ||
                (directionInDegrees >= 0) && (directionInDegrees <= 11.25)    ){
            cardinalDirection = "N";
        } else if( (directionInDegrees >= 11.25 ) && (directionInDegrees <= 33.75)){
            cardinalDirection = "NNE";
        } else if( (directionInDegrees >= 33.75 ) &&(directionInDegrees <= 56.25)){
            cardinalDirection = "NE";
        } else if( (directionInDegrees >= 56.25 ) && (directionInDegrees <= 78.75)){
            cardinalDirection = "ENE";
        } else if( (directionInDegrees >= 78.75 ) && (directionInDegrees <= 101.25) ){
            cardinalDirection = "E";
        } else if( (directionInDegrees >= 101.25) && (directionInDegrees <= 123.75) ){
            cardinalDirection = "ESE";
        } else if( (directionInDegrees >= 123.75) && (directionInDegrees <= 146.25) ){
            cardinalDirection = "SE";
        } else if( (directionInDegrees >= 146.25) && (directionInDegrees <= 168.75) ){
            cardinalDirection = "SSE";
        } else if( (directionInDegrees >= 168.75) && (directionInDegrees <= 191.25) ){
            cardinalDirection = "S";
        } else if( (directionInDegrees >= 191.25) && (directionInDegrees <= 213.75) ){
            cardinalDirection = "SSW";
        } else if( (directionInDegrees >= 213.75) && (directionInDegrees <= 236.25) ){
            cardinalDirection = "SW";
        } else if( (directionInDegrees >= 236.25) && (directionInDegrees <= 258.75) ){
            cardinalDirection = "WSW";
        } else if( (directionInDegrees >= 258.75) && (directionInDegrees <= 281.25) ){
            cardinalDirection = "W";
        } else if( (directionInDegrees >= 281.25) && (directionInDegrees <= 303.75) ){
            cardinalDirection = "WNW";
        } else if( (directionInDegrees >= 303.75) && (directionInDegrees <= 326.25) ){
            cardinalDirection = "NW";
        } else if( (directionInDegrees >= 326.25) && (directionInDegrees <= 348.75) ){
            cardinalDirection = "NNW";
        } else {
            cardinalDirection = "?";
        }

        return cardinalDirection;
    }

    @Override
    public int getItemCount() {
        return mListCityWeather.size();
    }


    public class HistoryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView tvCityName, tvCountry, tvTemp, tvDetails;
        ImageView imgWeather;

        public HistoryViewHolder(@NonNull View itemView) {
            super(itemView);

            tvCityName = itemView.findViewById(R.id.tvCityName);
            tvCountry = itemView.findViewById(R.id.tvCountry);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            tvDetails = itemView.findViewById(R.id.tvDetails);
            imgWeather = itemView.findViewById(R.id.imgWeather);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if(callback != null){
                callback.onItemClickListener(v, getAdapterPosition());
            }
        }
    }
}
