package com.webcon.sus.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * @author m
 */
public class StationPagerAdapter extends FragmentPagerAdapter{
    private ArrayList<Fragment> mList = new ArrayList<>();

    public StationPagerAdapter(FragmentManager fm){
        super(fm);
    }

    public StationPagerAdapter(FragmentManager fm, ArrayList<Fragment> list){
        super(fm);
        mList = list;
    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
