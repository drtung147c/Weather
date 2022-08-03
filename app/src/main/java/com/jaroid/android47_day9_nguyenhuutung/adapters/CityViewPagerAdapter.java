package com.jaroid.android47_day9_nguyenhuutung.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.jaroid.android47_day9_nguyenhuutung.R;
import com.jaroid.android47_day9_nguyenhuutung.fragment.CityFragment;

import java.util.ArrayList;

public class CityViewPagerAdapter extends FragmentStatePagerAdapter {

    private ArrayList<CityFragment> mlistCityFragments;
    private FragmentManager mFragmentManager;
    private ArrayList<String> mlistCityName;

    public CityViewPagerAdapter(@NonNull FragmentManager fm, ArrayList<String> listCityName) {
        super(fm);
        this.mFragmentManager = fm;
        this.mlistCityFragments = new ArrayList<>();
        this.mlistCityName = listCityName;
        for (int i = 0; i < listCityName.size(); i++) {
            CityFragment cityFragment = CityFragment.newInstance(listCityName.get(i));
            this.mlistCityFragments.add(cityFragment);
        }
    }


    @NonNull
    @Override
    public Fragment getItem(int position) {
        return this.mlistCityFragments.get(position);
    }

    @Override
    public int getCount() {
        return mlistCityName.size();
    }
}
