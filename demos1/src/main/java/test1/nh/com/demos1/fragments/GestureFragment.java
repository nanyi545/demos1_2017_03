package test1.nh.com.demos1.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.DrawerActivity;

/**
 * Created by Administrator on 15-9-30.
 */
public class GestureFragment extends Fragment {

    private Context mcontext;
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    //empty constructor
    public GestureFragment(){
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static GestureFragment newInstance(int sectionNumber) {
        GestureFragment fragment = new GestureFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private ImageView iv1;
    private ImageView iv2;


    //for detecting common gestures
    private View.OnTouchListener viewTouch=new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = MotionEventCompat.getActionMasked(event);
            switch(action) {
                case (MotionEvent.ACTION_DOWN) :
                    Log.d("GESTURE", "Action was DOWN");
                    return true;
                case (MotionEvent.ACTION_MOVE) :
                    Log.d("GESTURE","Action was MOVE  rawX:"+event.getRawX());
                    return true;
                case (MotionEvent.ACTION_UP) :
                    Log.d("GESTURE","Action was UP");
                    return true;
                case (MotionEvent.ACTION_CANCEL) :
                    Log.d("GESTURE","Action was CANCEL");
                    return true;
                case (MotionEvent.ACTION_OUTSIDE) :
                    Log.d("GESTURE","Movement occurred outside bounds " +
                            "of current screen element");
                    return true;
                default :
                    return true;
            }
        }
    };



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mcontext=getActivity();


        View rootView = inflater.inflate(R.layout.fragment_gesture_drawer, container, false);
        iv2=(ImageView)rootView.findViewById(R.id.iv_test_red);
        iv1=(ImageView)rootView.findViewById(R.id.iv_test);

        //for detecting common gestures
        iv1.setOnTouchListener(viewTouch);

//Detecting All Supported Gestures------>is in actionbar activity
// drawer activity can not be used to monitor gesture----->already monitor drawer open/close

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //设置activity label------------
        try {
            ((DrawerActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (Exception e) {  // in case this fragment is called not by  DrawerActivity
            e.printStackTrace();
//            Log.i("AAA","ANIMATIONfragment called not by DrawerActivity");
        }


    }



}
