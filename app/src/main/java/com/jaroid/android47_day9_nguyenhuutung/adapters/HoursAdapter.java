package com.jaroid.android47_day9_nguyenhuutung.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.annotation.GlideOption;
import com.jaroid.android47_day9_nguyenhuutung.R;
import com.jaroid.android47_day9_nguyenhuutung.models.DataHours;
import com.jaroid.android47_day9_nguyenhuutung.utils.Global;

import java.util.ArrayList;

public class HoursAdapter extends RecyclerView.Adapter<HoursAdapter.HoursViewHolder> {

    private ArrayList<DataHours> mDataHours;
    private Context mContext;

    public HoursAdapter(ArrayList<DataHours> mDataHours) {
        this.mDataHours = mDataHours;
    }

    @NonNull
    @Override
    public HoursViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.mContext = parent.getContext();
        View view = LayoutInflater.from(mContext).inflate(R.layout.layout_info_hours, parent, false);
        return new HoursViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoursViewHolder holder, int position) {

        DataHours dataHours = mDataHours.get(position);

        holder.tvTime.setText(dataHours.getDtTxt().split(" ")[1]+ "");
        holder.tvTemp.setText(Global.convertKelvinToCelsius(dataHours.getMain().getTemp()) + "");
        holder.tvDescription.setText(dataHours.getWeather().get(0).getDescription() + "");

        String iconUrl = Global.URL_ICON + dataHours.getWeather().get(0).getIcon() + Global.PICTURE_FORMAT;
        Glide.with(mContext).load(iconUrl).into(holder.imgIcon);
    }

    @Override
    public int getItemCount() {
        return mDataHours.size();
    }

    public class HoursViewHolder extends RecyclerView.ViewHolder {

        TextView tvTime, tvTemp, tvDescription;
        ImageView imgIcon;

        public HoursViewHolder(@NonNull View itemView) {
            super(itemView);

            tvTime = itemView.findViewById(R.id.tvTime);
            tvTemp = itemView.findViewById(R.id.tvTemp);
            tvDescription = itemView.findViewById(R.id.tvDescription);
            imgIcon = itemView.findViewById(R.id.imgIcon);
        }
    }
}
