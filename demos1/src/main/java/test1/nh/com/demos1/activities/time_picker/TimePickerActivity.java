package test1.nh.com.demos1.activities.time_picker;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;

import java.util.LinkedList;

import test1.nh.com.demos1.R;

public class TimePickerActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,TimePickerActivity.class);
        c.startActivity(i);
    }

    LinkedList<String> genTestData(){
        LinkedList<String> ret=new LinkedList<>();
        for (int ii=0;ii<1000;ii++){
            ret.add("abcdefg-asdfa"+ii);
        }
        return ret;
    }

    protected DisplayMetrics dm = new DisplayMetrics();

    ScrollView mScrollView;
    GestureDetector mGestureDetector;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        setContentView(R.layout.activity_time_picker);

        final RecyclerView rv= (RecyclerView) findViewById(R.id.endless);
        final LinearLayoutManager manager=new LinearLayoutManager(this);
        rv.setLayoutManager(manager);

        final EndlessRVAdapter adapter=new EndlessRVAdapter(genTestData(),this);
        rv.setAdapter(adapter);

        final int height=(int)(150*dm.density/5);  //  150dp == height of of RV, 5 items in the RV
        final int helfHeight=height/2;
        rv.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                int first=manager.findFirstVisibleItemPosition();
                int last=manager.findLastVisibleItemPosition();
                int first_c=manager.findFirstCompletelyVisibleItemPosition();
                int last_c=manager.findLastCompletelyVisibleItemPosition();
                int offset=rv.computeVerticalScrollOffset();
                Log.i("AAA","FIRST:"+first+"  first_c:"+first_c   +"  last:"+last+"  last_c:"+last_c+"  offset:"+offset);
                int center;
                if (offset%height<helfHeight) {
                    center=(first_c+last_c)/2;
                } else {
                    center=(first_c+last_c)/2+1;
                }
                adapter.setCenterPosition(center);
            }
        });



        lateInit.sendEmptyMessageDelayed(1,1000);


        final int density = (int)getResources().getDisplayMetrics().density;

        mScrollView = (ScrollView) findViewById(R.id.scrollView1);
        mGestureDetector = new GestureDetector(getApplicationContext(),new GestureDetector.SimpleOnGestureListener() {
            int selectedPage=0;
            @Override
            public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                if(velocityY<0){
                    selectedPage = Math.min(3, selectedPage+1);
                }else{
                    selectedPage = Math.max(0, selectedPage-1);
                }
                mScrollView.smoothScrollTo(0, 150 * density * selectedPage);
                return super.onFling(e1, e2, velocityX, velocityY);
            }
        });
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                mGestureDetector.onTouchEvent(event);
                return true;
            }
        });






    }



    Handler lateInit=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            Rotate3dAnimation.animationTextView(TimePickerActivity.this,R.id.test_tv1,50,40);
            Rotate3dAnimation.animationTextView(TimePickerActivity.this,R.id.test_tv2,0,20);
            Rotate3dAnimation.animationTextView2(TimePickerActivity.this,R.id.test_tv4,0,-20);
            Rotate3dAnimation.animationTextView2(TimePickerActivity.this,R.id.test_tv5,50,-40);

        }
    };
}
