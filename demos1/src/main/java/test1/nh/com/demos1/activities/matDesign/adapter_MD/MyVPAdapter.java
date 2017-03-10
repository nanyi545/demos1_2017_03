package test1.nh.com.demos1.activities.matDesign.adapter_MD;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.ArrayList;

/**
 * Created by Administrator on 15-10-27.
 */
public class MyVPAdapter extends FragmentPagerAdapter {
    private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();

    public MyVPAdapter(FragmentManager fm) {
        super(fm);
    }

    public MyVPAdapter(FragmentManager fm, ArrayList<Fragment> fragments) {
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
