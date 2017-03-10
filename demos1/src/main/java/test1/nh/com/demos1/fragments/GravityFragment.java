package test1.nh.com.demos1.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.DrawerActivity;

/**
 * Created by Administrator on 15-9-30.
 */
public class GravityFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    //empty constructor
    public GravityFragment(){
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GravityFragment newInstance(int sectionNumber) {
        GravityFragment fragment = new GravityFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get section id from intent
        // getArg--return the Bundle
        int section_number=getArguments().getInt(ARG_SECTION_NUMBER);

        View rootView = inflater.inflate(R.layout.fragment_gravity_drawer, container, false);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // 如果called by DrawerActivity 设置activity label
        try {    //  called by DrawerActivity
            ((DrawerActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}
