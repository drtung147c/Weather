package com.jaroid.android47_day9_nguyenhuutung.models;

import java.util.ArrayList;

public class Day {

    private String date;
    private ArrayList<DataHours> dataHours;
    private boolean isExpand;

    public Day(String date, ArrayList<DataHours> dataHours, boolean isExpand) {
        this.date = date;
        this.dataHours = dataHours;
        this.isExpand = isExpand;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public Day() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<DataHours> getDataHours() {
        return dataHours;
    }

    public void setDataHours(ArrayList<DataHours> dataHours) {
        this.dataHours = dataHours;
    }
}
