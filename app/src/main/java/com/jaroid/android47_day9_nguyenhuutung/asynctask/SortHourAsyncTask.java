package com.jaroid.android47_day9_nguyenhuutung.asynctask;

import android.os.AsyncTask;
import android.util.Log;

import com.jaroid.android47_day9_nguyenhuutung.interfaces.ISortedHoursListener;
import com.jaroid.android47_day9_nguyenhuutung.models.DataHours;
import com.jaroid.android47_day9_nguyenhuutung.models.Day;

import java.util.ArrayList;
import java.util.List;

public class SortHourAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = SortHourAsyncTask.class.getName();

    private List<DataHours> mData;
    private ArrayList<Day> mOutput;
    private static String currentDate = " ";

    private ISortedHoursListener mCallback;

    public SortHourAsyncTask(List<DataHours> data, ISortedHoursListener callback) {
        this.mData = data;
        this.mCallback = callback;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        mOutput = new ArrayList<>();
        mOutput.clear();
    }

    @Override
    protected Void doInBackground(Void... voids) {

        for(int i = 0; i < mData.size(); i++){
            String dt_txt = mData.get(i).getDtTxt();
            String date = dt_txt.split(" ")[0];
            String time = dt_txt.split(" ")[1];
            if(mOutput.size() <= 0){
                Day firstDay = new Day();
                firstDay.setDate(date);
                ArrayList<DataHours> dayHours = new ArrayList<>();
                dayHours.add(mData.get(i));
                firstDay.setDataHours(dayHours);
                currentDate = date;
                firstDay.setExpand(false);
                mOutput.add(firstDay);
            }else {
                if(currentDate.equals(date)){
                    Day currentDay = mOutput.get(mOutput.size() - 1);
                    ArrayList<DataHours> currentHours = currentDay.getDataHours();
                    currentHours.add(mData.get(i));
                }else {
                    Day newDay = new Day();
                    newDay.setDate(date);
                    ArrayList<DataHours> dayHours = new ArrayList<>();
                    dayHours.add(mData.get(i));
                    newDay.setDataHours(dayHours);
                    currentDate = date;
                    newDay.setExpand(false);
                    mOutput.add(newDay);
                }
            }
        }
        Log.d(TAG, "doInBackground: " + mOutput.size());
        return null;
    }

    @Override
    protected void onPostExecute(Void unused) {
        super.onPostExecute(unused);
        if(mCallback != null){
            mCallback.onSortedHoursListener(mOutput);
        }
    }
}
