package test1.nh.com.demos1.activities.bessel;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.customView.CircleImageView;

public class BesselActivity extends AppCompatActivity {



    public static void start(Context c){
        Intent i=new Intent(c,BesselActivity.class);
        c.startActivity(i);
    }


    ViewPager vp;


    BounceIndicator bounceIndicator;

    public void addCount(View v){
        bounceIndicator.addCount();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bessel);

        vp= (ViewPager) findViewById(R.id.bessel_vp);

        ArrayList<Fragment> list=new ArrayList<>();
        list.add(new ImgFragment());
        list.add(new ImgFragment());
        list.add(new ImgFragment());


        vp.setAdapter(new TestFragmentPagerAdapter(getSupportFragmentManager(),list) );
        vp.setOnPageChangeListener(new CircularViewPagerHandler(vp));


        bounceIndicator= (BounceIndicator) findViewById(R.id.bouncing);
    }





    private static class TestFragmentPagerAdapter extends FragmentStatePagerAdapter{

        private ArrayList<Fragment> mFragments = new ArrayList<Fragment>();


        public TestFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public TestFragmentPagerAdapter(FragmentManager fm,ArrayList<Fragment> mFragments) {
            super(fm);
            this.mFragments=mFragments;
        }


        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }
    }





}
