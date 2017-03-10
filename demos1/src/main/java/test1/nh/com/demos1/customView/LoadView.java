package test1.nh.com.demos1.customView;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.DecelerateInterpolator;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 16-7-30.
 */
public class LoadView extends View {


    private int viewWidth;
    private int viewHeight;

    public LoadView(Context context) {
        super(context);
    }

    public LoadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        loadInitValues(context, attrs);
    }





    private void loadInitValues(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.LoadView
        );
        try {
            ratio=a.getFloat(R.styleable.LoadView_loadPercent, 0);
            bckGroundRes=a.getResourceId(R.styleable.LoadView_loadingBackground,0);
            imgRes=a.getResourceId(R.styleable.LoadView_loadingImage,0);
            loadingDirection=a.getInt(R.styleable.LoadView_loadingDirection,HORIZONTAL_LOADING);
            Log.i("ccc",""+loadingDirection);
        } finally {
            a.recycle();
        }
    }

    public LoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        loadInitValues(context, attrs);
    }

    boolean bitmapsReady=false;
    int bckGroundRes,imgRes;
    Bitmap loadingImg, loadingBg;
    float height,width;
    private int loadingDirection;
    private static final int HORIZONTAL_LOADING=10;
    private static final int VERTICAL_LOADING=11;

    private void loadImages(){
        loadingImg =getScaledBitmap(BitmapFactory.decodeResource(getResources(), imgRes),viewWidth);
        loadingBg =getScaledBitmap(BitmapFactory.decodeResource(getResources(), bckGroundRes),viewWidth);
        height= loadingImg.getHeight();
        width= loadingBg.getWidth();
        Log.i("ccc","ondraw----    width:"+width+"  height:"+height);
        bitmapsReady=true;
    }

    public void setProgress(float ratio){
        this.ratio = ratio;
        invalidate();
    }


    public void animateTo(float ratio){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, ratio);
        valueAnimator.setStartDelay(100);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new DecelerateInterpolator(2f));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LoadView.this.setProgress((Float)valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    public void animateRepeated(){
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, 1f);
        valueAnimator.setDuration(2000);
        valueAnimator.setRepeatCount(Integer.MAX_VALUE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LoadView.this.setProgress((Float)valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }





    public static void startLoading(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.layout_load_indicator, null);
        LoadView loadView= (LoadView) dialoglayout.findViewById(R.id.loadview);

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setView(dialoglayout);
        AlertDialog dialog=builder.create();
//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        dialog.show();
        dialog.getWindow().setLayout(325, 325);

//        dialog.getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        loadView.animateTo(1);
        Log.i("ccc","width:"+loadView.width+"  height:"+loadView.height);
    }


    public static void startLoading2(Activity activity){
        LayoutInflater inflater = activity.getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.layout_load_indicator, null);
        LoadView loadView= (LoadView) dialoglayout.findViewById(R.id.loadview);


        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(dialoglayout);

        dialog.show();
        loadView.animateRepeated();
    }




    float ratio=0f;

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (!bitmapsReady)loadImages();
        if (loadingDirection==HORIZONTAL_LOADING) {
            canvas.save();   //*************
            //**********-----restrict the drawing area----
            canvas.clipRect(0, 0, width * ratio, height);
            canvas.drawBitmap(loadingImg, 0, 0, null);
            canvas.restore();//*************

            canvas.save();   //*************
            //**********-----restrict the drawing area----
            canvas.clipRect(width * ratio, 0, width, height);
            canvas.drawBitmap(loadingBg, 0, 0, null);
            canvas.restore();//*************
        } else if (loadingDirection==VERTICAL_LOADING){
            canvas.save();   //*************
            //**********-----restrict the drawing area----
            canvas.clipRect(0, height*(1-ratio), width , height);
            canvas.drawBitmap(loadingImg, 0, 0, null);
            canvas.restore();//*************

            canvas.save();   //*************
            //**********-----restrict the drawing area----
            canvas.clipRect(0, 0, width , height*(1-ratio));
            canvas.drawBitmap(loadingBg, 0, 0, null);
            canvas.restore();//*************

        }
    }

    /**
     * Returns a bitmap scaled to a specific width.
     */
    private Bitmap getScaledBitmap(Bitmap bitmap, float width) {
        float scale = width / bitmap.getWidth();
        return Bitmap.createScaledBitmap(bitmap, (int) (bitmap.getWidth() * scale),
                (int) (bitmap.getHeight() * scale), false);
    }



    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        //Get the width measurement
        int widthSize = View.resolveSize(getDesiredWidth(), widthMeasureSpec);

        //Get the height measurement
        int heightSize = View.resolveSize(getDesiredHeight(), heightMeasureSpec);

        //MUST call this to store the measurements
        Log.i("ccc","onmeasure: widthSize"+widthSize+"   heightSize:"+heightSize);
        setMeasuredDimension(widthSize, heightSize);

    }

    private int getDesiredHeight() {
        return 150;
    }
    private int getDesiredWidth() {
        return 150;
    }





    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth=w;
        viewHeight=h;
    }



}
