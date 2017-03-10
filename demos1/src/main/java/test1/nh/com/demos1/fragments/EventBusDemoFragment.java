package test1.nh.com.demos1.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;

import de.greenrobot.event.EventBus;
import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.DrawerActivity;
import test1.nh.com.demos1.eventBusDemo.AddProEvent;

/**
 * Created by Administrator on 15-9-30.
 */
public class EventBusDemoFragment extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section4";
    ProgressBar p1;
    private Context mContext;


    /**
     * eventBus handler, handle-->AddProEvent
     * @param addEvent
     */
    public void onEventMainThread(AddProEvent addEvent){
        Log.i("EVENT","123");
        p1.incrementProgressBy(5);
        p1.incrementSecondaryProgressBy(5);
    }

    //empty constructor
    public EventBusDemoFragment(){
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static EventBusDemoFragment newInstance(int sectionNumber) {
        EventBusDemoFragment fragment = new EventBusDemoFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        EventBus.getDefault().register(this); // register this class for eventbus

        View rootView = inflater.inflate(R.layout.fragment_eventbusdemo_drawer, container, false);


        // demonstrastion of event bus----------
        Button b_addProgress=(Button)rootView.findViewById(R.id.button6);
        p1=(ProgressBar)rootView.findViewById(R.id.id_progressBar1);
        b_addProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddProEvent addEvent=new AddProEvent();
                EventBus.getDefault().post(addEvent); // post  AddProEvent
            }
        });


        //------demonstrastion of broadcast receiver----------
        // register this activity for AddBarReceiver
        receiver1=new AddBarReceiver();
        IntentFilter ifilter=new IntentFilter();
        ifilter.addAction("whatEverString");
        mContext.registerReceiver(receiver1, ifilter);

        Button b_addProgress_2=(Button)rootView.findViewById(R.id.button9);
        b_addProgress_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent("whatEverString");
                mContext.sendBroadcast(intent1);  // send broadcast receiver
            }
        });



        return rootView;
    }


    AddBarReceiver receiver1;

    // demonstrastion of broadcast----------
    public class AddBarReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
//            Log.i("AAA","increase progressbar--by broadcast receiver");
            p1.incrementProgressBy(5);
            p1.incrementSecondaryProgressBy(5);
        }
    }



    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {    //  called by DrawerActivity
            ((DrawerActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (Exception e) {
            e.printStackTrace();
//            Log.i("AAA", "Fragment called not by DrawerActivity");
        }
        mContext=activity;
    }

}
