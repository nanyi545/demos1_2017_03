package test1.nh.com.demos1.activities.cyclic_galary;


import android.app.Fragment;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import java.util.ArrayList;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.matDesign.adapter_MD.MyVPAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentFirstPage extends android.support.v4.app.Fragment {


    public FragmentFirstPage() {
        // Required empty public constructor
    }


    private ArrayList<android.support.v4.app.Fragment> fragments = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    ViewPager viewPager;
    VpIndicator indicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v =inflater.inflate(R.layout.fragment_fragment_first_page, container, false);
//        fragments.add(new ImaFragment(R.drawable.fi));
//        fragments.add(new ImaFragment(R.drawable.fo));
//        fragments.add(new ImaFragment(R.drawable.f));
//        fragments.add(new ImaFragment(R.drawable.circle_gray));

        fragments.add(new ImaFragment(R.color.Blue300));
        fragments.add(new ImaFragment(R.color.Red300));
        fragments.add(new ImaFragment(R.color.Amber200));
        fragments.add(new ImaFragment(R.color.Purple300));


        viewPager = (ViewPager) v.findViewById(R.id.f1_vp);
        indicator= (VpIndicator) v.findViewById(R.id.vp_indicator);
        indicator.resetTotalCounts(4);
        AppCompatActivity host= (AppCompatActivity) getActivity();
        MyVPAdapter adapter = new MyVPAdapter(getChildFragmentManager(), fragments);

        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                Log.i("BBB","onPageScrolled:  position:"+position+"   positionOffset:"+positionOffset+"   positionOffsetPixels:"+positionOffsetPixels);
                indicator.update(position,positionOffset);
            }

            @Override
            public void onPageSelected(int position) {
                Log.i("BBB","onPageSelected:  position:"+position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                Log.i("BBB","onPageScrollStateChanged:  state:"+state);
            }
        });

        ScrollMenu menu= (ScrollMenu) v.findViewById(R.id.my_scroll_menu);
        menu.scrollIn();
        return v;
    }




}
