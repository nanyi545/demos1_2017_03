package test1.nh.com.demos1.customView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.OvershootInterpolator;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 16-9-5.
 */
public class PlayAreaView extends View {



    public PlayAreaView(Context context) {
        super(context);
        init(context);
    }

    public PlayAreaView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public PlayAreaView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    GestureDetector gestures;


    private void init(Context c){
        translate = new Matrix();
        gestures = new GestureDetector(c,new GestureListener(this));
        droid = BitmapFactory.decodeResource(getResources(), R.drawable.basketbal);
    }


    private Matrix translate;
    private Bitmap droid;


    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);

        canvas.drawBitmap(droid, translate, null);
        Matrix m = canvas.getMatrix();
//        Log.i("ccc", "Matrix: "+translate.toShortString());
//        Log.i("ccc", "Canvas: "+m.toShortString());

    }




    private Matrix animateStart;
    private Interpolator animateInterpolator;
    private long startTime;
    private long endTime;
    private float totalAnimDx;
    private float totalAnimDy;


    public void onAnimateMove(float dx, float dy, long duration) {
        animateStart = new Matrix(translate);
        animateInterpolator = new OvershootInterpolator();
        startTime = System.currentTimeMillis();
        endTime = startTime + duration;
        totalAnimDx = dx;
        totalAnimDy = dy;
        post(new Runnable() {
            @Override
            public void run() {
                onAnimateStep();
            }
        });
    }

    private void onAnimateStep() {
        long curTime = System.currentTimeMillis();
        float percentTime = (float) (curTime - startTime)
                / (float) (endTime - startTime);
        float percentDistance = animateInterpolator
                .getInterpolation(percentTime);
        float curDx = percentDistance * totalAnimDx;
        float curDy = percentDistance * totalAnimDy;
        translate.set(animateStart);
        onMove(curDx, curDy);

        //Log.v(DEBUG_TAG, "We're " + percentDistance + " of the way there!");
        if (percentTime < 1.0f) {
            post(new Runnable() {
                @Override
                public void run() {
                    onAnimateStep();
                }
            });
        }
    }

    public void onMove(float dx, float dy) {
        translate.postTranslate(dx, dy);
        invalidate();
    }

    public void onResetLocation() {
        translate.reset();
        invalidate();
    }

    public void onSetLocation(float dx, float dy) {
        translate.postTranslate(dx, dy);
    }



    private class GestureListener implements GestureDetector.OnGestureListener,
            GestureDetector.OnDoubleTapListener {

        PlayAreaView view;

        public GestureListener(PlayAreaView view) {
            this.view = view;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            Log.i("ccc", "onDown");
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2,
                               final float velocityX, final float velocityY) {
            Log.i("ccc", "onFling");
            final float distanceTimeFactor = 0.4f;
            final float totalDx = (distanceTimeFactor * velocityX / 2);
            final float totalDy = (distanceTimeFactor * velocityY / 2);

            view.onAnimateMove(totalDx, totalDy, (long) (1000 * distanceTimeFactor));
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i("ccc", "onDoubleTap");
            view.onResetLocation();
            return true;
        }

        @Override
        public void onLongPress(MotionEvent e) {
            Log.i("ccc", "onLongPress");
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            //Log.v(DEBUG_TAG, "onScroll");
            Log.i("ccc", "onScroll");
            view.onMove(-distanceX, -distanceY);
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            Log.i("ccc", "onShowPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            Log.i("ccc", "onSingleTapUp");
            return false;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            Log.i("ccc", "onDoubleTapEvent");
            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            Log.i("ccc", "onSingleTapConfirmed");
            return false;
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return gestures.onTouchEvent(event);
    }


}
