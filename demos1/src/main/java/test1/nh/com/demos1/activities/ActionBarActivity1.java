package test1.nh.com.demos1.activities;

import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar;

import test1.nh.com.demos1.R;

public class ActionBarActivity1 extends AppCompatActivity {

    // zoom------------------
    private ScaleGestureDetector mScaleDetector;
    private float mScaleFactor = 1.f;
    private class ScaleListener
            extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
//            mScaleFactor = detector.getScaleFactor()*mScaleFactor;
//            // Don't let the object get too small or too large.
//            mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));
//            Log.i("GESTURE","scale"+mScaleFactor);
            if (detector.getScaleFactor()>1) {
                Log.i("GESTURE", "expand");
                tvIndicator.setText("expand");
            }
            else {
                Log.i("GESTURE", "contract");
                tvIndicator.setText("contract");
            }
            return true;
        }
    }

    private TextView tvIndicator;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_bar1);
        
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        // Get a support ActionBar corresponding to this toolbar
        ActionBar ab = getSupportActionBar();
        // Enable the Up button
        ab.setDisplayHomeAsUpEnabled(true); //  an icon to indicate go back , in the position of naviIcon

        //Detecting All Supported Gestures----can this be applied to a particular view??
        // seems not likely -- need to override activity.onTouchEvent
        mDetector=new GestureDetectorCompat(this,myGesture);
        mDetector.setOnDoubleTapListener(myDoubleTapListener);


        // zoom------------------
        mScaleDetector = new ScaleGestureDetector(this, new ScaleListener());
        tvIndicator = (TextView) findViewById(R.id.tv_indicator);

        //-----test loading circle----------
        Button b1=(Button)findViewById(R.id.bn_switch_defence);
        p1=(CircleProgressBar)findViewById(R.id.mlp_loading_defence);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (p1.getVisibility()==View.VISIBLE){
                    p1.setVisibility(View.INVISIBLE);
                }else {
                    p1.setVisibility(View.VISIBLE);
                }
            }
        });

    }


    CircleProgressBar p1;

    //Detecting All Supported Gestures
    private GestureDetectorCompat mDetector;


    @Override
    public boolean onTouchEvent(MotionEvent event) {
//        Log.i("GESTURE","ontouchevent");
        this.mDetector.onTouchEvent(event);  // ---simple gesture----
        this.mScaleDetector.onTouchEvent(event);  //-----scale-------
        return super.onTouchEvent(event);
    }

    // GestureDetector.OnGestureListener
    private GestureDetector.OnGestureListener myGesture=new GestureDetector.OnGestureListener(){
        @Override
        public boolean onDown(MotionEvent e) {
            Log.i("GESTURE","down");
            return false;
        }
        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("GESTURE","longpress");
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
//        Log.i("GESTURE","onScroll---X:"+distanceX+"   Y:"+distanceY);
            if ((distanceY>15)&(distanceX<5)){Log.i("GESTURE","scroll up");tvIndicator.setText("up");}
            if ((distanceY<-15)&(distanceX<5)){Log.i("GESTURE","scroll down");tvIndicator.setText("down");}
            if ((distanceY<5)&(distanceX>15)){Log.i("GESTURE","scroll left");tvIndicator.setText("left");}
            if ((distanceY<5)&(distanceX<-15)){Log.i("GESTURE","scroll right");tvIndicator.setText("right");}
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }
    };


    //GestureDetector.OnDoubleTapListener
    private  GestureDetector.OnDoubleTapListener myDoubleTapListener=new GestureDetector.OnDoubleTapListener(){
        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("GESTURE","onDoubleTap");
            tvIndicator.setText("double Tap");
            return false;
        }
        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            return false;
        }
        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            return false;
        }
    };










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_action_bar_activity1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.action_settings1:
                Log.i("AAA", "action1");
                return true;
            case R.id.action_settings2:
                Log.i("AAA", "action2");
                return true;
            case R.id.action_balance:
                Log.i("AAA", "action_balance");
                return true;
            case R.id.action_clock:
                Log.i("AAA", "action_clock");
                return true;
            case R.id.action_search:
                Log.i("AAA", "action_clock");
                return true;
            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);
        }
    }
}
