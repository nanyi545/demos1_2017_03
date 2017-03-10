package test1.nh.com.demos1.activities.selectTime;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 16-8-31.
 */
public class TimeSelector {


    private Activity activity;
    private PopupWindow popupWindow;
    private View parent;

    public TimeSelector(Activity activity, View parent) {
        this.activity = activity;
        this.parent = parent;
    }


    RecyclerView dayRv;
    SelectRVAdapter dayAdapter;


    private ArrayList<Integer> getDays(){
        ArrayList<Integer> ret=new ArrayList<>();
        for (int aa=0;aa<30;aa++){
            ret.add(aa);
        }
        return ret;
    }

    public void show() {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.popuwindow_settime, null);


            DisplayMetrics dm=new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
            final int height=(int)(24 * dm.density);  //  item height is 32dp
            final int helfHeight=height/2;
            Log.i("CCC","height:"+height+"  helfHeight:"+helfHeight+"  dm.density:"+dm.density);


            dayRv= (RecyclerView) view.findViewById(R.id.select_date_rv);
            LinearLayoutManager manager=new LinearLayoutManager(activity);
            dayRv.setLayoutManager(manager);
            dayAdapter=new SelectRVAdapter(getDays(),activity);
            dayRv.setAdapter(dayAdapter);
            dayRv.addOnScrollListener(new RecyclerView.OnScrollListener() {


                int currentOffset;
                int currentCenter=3;

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    if (newState==0) {
                        int stickToGridAmount = (currentCenter - 3) * height - currentOffset;
                        Log.i("CCC","stickToGridAmount:"+stickToGridAmount);//  newState 1 START   newState 0 END

                        dayRv.scrollBy(0, stickToGridAmount);
                    }
                }


                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {


                    int offset=dayRv.computeVerticalScrollOffset();
//                    Log.i("CCC","offset:"+offset+"  dy:"+dy);


                    int center=3;

                    if (offset%height<helfHeight) {
                        center=offset/height+3;
                    } else {
                        center=offset/height+1+3;
                    }

                    dayAdapter.setCenterPosition(center);
                    currentOffset=offset;
                    currentCenter=center;

                }
            });





            TextView tv_cancel= (TextView) view.findViewById(R.id.cancel_time);
            tv_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });

            TextView tv_confirm= (TextView) view.findViewById(R.id.confirm_time);
            tv_confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dayRv.scrollBy(0, height);
                }
            });


            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        }

        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAtLocation(parent, Gravity.BOTTOM | Gravity.CENTER, 0, 0);

    }




}
