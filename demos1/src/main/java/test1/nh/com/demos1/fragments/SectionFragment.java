package test1.nh.com.demos1.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.DrawerActivity;

/**
 * Created by Administrator on 15-9-30.
 */
public class SectionFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    //empty constructor
    public SectionFragment(){
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SectionFragment newInstance(int sectionNumber) {
        SectionFragment fragment = new SectionFragment();
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

        View rootView = inflater.inflate(R.layout.fragment_drawer, container, false);
        TextView tv1=(TextView)rootView.findViewById(R.id.section_label);
        tv1.setText("" + section_number);
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //如果attach到drawerActivity上 设置activity label
        try {    //  called by DrawerActivity
            ((DrawerActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (Exception e) {
            e.printStackTrace();
//            Log.i("AAA", "Fragment called not by DrawerActivity");
        }

    }



}
