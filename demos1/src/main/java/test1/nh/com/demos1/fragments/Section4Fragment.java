package test1.nh.com.demos1.fragments;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.DrawerActivity;
import test1.nh.com.demos1.activities.MainActivity_from;

/**
 * Created by Administrator on 15-9-30.
 */
public class Section4Fragment extends Fragment {

    private int mId=10086;  //notification id, An identifier for this notification unique within your application.

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section4";

    private Context mContext;
    private Button setTheme;
    private TextView tv_window_size;

    private int largeNotificationIndex;

    //empty constructor
    public Section4Fragment(){
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static Section4Fragment newInstance(int sectionNumber) {
        Section4Fragment fragment = new Section4Fragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_section4_drawer, container, false);


        //---------------Notification Test------------
//        final int mId=10086;  //notification id, An identifier for this notification unique within your application.

        final int mId2=20086;

        Button b_sendNoti=(Button)rootView.findViewById(R.id.button4_sendNoti);
        b_sendNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager mNotificationManager =
                        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);  //
                mNotificationManager.notify(mId2, createNotice());  // mId++
            }
        });


        // cancel notification---
        Button b_ccancelNoti=(Button)rootView.findViewById(R.id.button4_cancelNoti);
        b_ccancelNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NotificationManager mNotificationManager =
                        (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);  //
                mNotificationManager.cancel(--mId);
            }
        });



        Button b_sendLNoti=(Button)rootView.findViewById(R.id.button4_sendLNoti);
        b_sendLNoti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (largeNotificationIndex<1) {
                    NotificationManager mNotificationManager =
                            (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);  //
                    mNotificationManager.notify(mId2, createLargeNotice2());
                } else{
                    NotificationManager mNotificationManager =
                            (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);  //
                    mNotificationManager.notify(mId2, createLargeNotice());
                }
                largeNotificationIndex++;
            }
        });
        //--------------end of Notification Test-------


        //-------------- theme Test-------
        setTheme= (Button) rootView.findViewById(R.id.id_setTheme);
        setTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().setTheme(R.style.CrazyTheme);
            }
        });
        //--------------end of theme Test-------

        //-------------- display pixels-------
        tv_window_size=(TextView)rootView.findViewById(R.id.textView);
        DisplayMetrics o = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(o);
        int widthPixels = o.widthPixels;
        int heightPixels = o.heightPixels;
        float density = o.density;
//        Log.i("AAA","widthPixels="+widthPixels+"   heightPixels="+heightPixels+"   density="+density);
        tv_window_size.setText("widthPixels=" + widthPixels + "   heightPixels=" + heightPixels +"   density="+density);
        //-------------- end of display pixels-------

        return rootView;


    }

    //-----------Notification Test------------------
    private Notification createNotice() {

        //点通知跳转，用pendingIntent--->
        Intent intent = new Intent(mContext, MainActivity_from.class);
        PendingIntent pintent = PendingIntent.getActivity(mContext, 0, intent, 0);

        // 从资源文件转到bitmap
        Resources res=getResources();
        Bitmap bmp= BitmapFactory.decodeResource(res,R.drawable.icon1);

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
                        .setContentIntent(pintent)
                        .setSmallIcon(R.drawable.icon_shield_green)
                        .setLargeIcon(bmp)
                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("My notification")
                        .setContentText("Hello World!");
        return mBuilder.build();
    }

    // 点通知，然后下滑可以展开大通知
    private Notification createLargeNotice() {

        //点通知跳转，用pendingIntent--->
        Intent intent = new Intent(mContext, MainActivity_from.class);
        PendingIntent pintent = PendingIntent.getActivity(mContext, 0, intent, 0);

        // 从资源文件转到bitmap
        Resources res=getResources();
        Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.basketbal);


        String[] events = {"event--1","event--2","event--3","event--4","event--5","event--6","event--7","event--8"};


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
//                        .setContentIntent(pintent)
                        .setSmallIcon(R.drawable.icon_shield_green)
//                        .setLargeIcon(bmp)
//                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("------------"+events.length)
                        .setContentText("large notification???");

        //use Inbox style big view
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Event tracker details:");

        // Moves events into the big view
        for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        // Moves the big view style object into the notification object.
        mBuilder.setStyle(inboxStyle);


        return mBuilder.build();
    }




    private Notification createLargeNotice2() {

        //点通知跳转，用pendingIntent--->
        Intent intent = new Intent(mContext, MainActivity_from.class);
        PendingIntent pintent = PendingIntent.getActivity(mContext, 0, intent, 0);

        // 从资源文件转到bitmap
        Resources res=getResources();
        Bitmap bmp= BitmapFactory.decodeResource(res, R.drawable.basketbal);


        String[] events = {"event--1","event--2","event--3","event--4"};


        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mContext)
//                        .setContentIntent(pintent)
                        .setSmallIcon(R.drawable.icon_shield_green)
//                        .setLargeIcon(bmp)
//                        .setWhen(System.currentTimeMillis())
                        .setContentTitle("------------"+events.length)
                        .setContentText("large notification???");

        //use Inbox style big view
        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();

        // Sets a title for the Inbox style big view
        inboxStyle.setBigContentTitle("Event tracker details:");

        // Moves events into the big view
        for (int i=0; i < events.length; i++) {
            inboxStyle.addLine(events[i]);
        }

        // Moves the big view style object into the notification object.
        mBuilder.setStyle(inboxStyle);


        return mBuilder.build();
    }



    //-----------end of Notification Test-----------


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //设置activity label
        ((DrawerActivity) activity).onSectionAttached(
                getArguments().getInt(ARG_SECTION_NUMBER));
        mContext=activity;
    }

}
