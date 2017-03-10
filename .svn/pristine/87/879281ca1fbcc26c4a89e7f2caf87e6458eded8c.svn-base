package com.webcon.sus.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * viewPager的adapter
 * 
 * @author Vieboo
 * 
 */
public class MainPagerAdapter extends FragmentPagerAdapter {
	private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

	public MainPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public MainPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
		super(fm);
		mFragments = fragments;
	}

	@Override
	public int getCount() {
		return mFragments.size();
	}

	@Override
	public Fragment getItem(int arg0) {
		//System.out.println(arg0);
		return mFragments.get(arg0);

	}

	@Override
	public int getItemPosition(Object object) {
		return super.getItemPosition(object);
	}

}
