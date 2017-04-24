package test1.nh.com.demos1.customView;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 16-7-30.
 */
public class WaveView extends View {


    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initLookUpTable();
    }

    public WaveView(Context context) {
        super(context);
        initLookUpTable();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initLookUpTable();
    }




    private int viewWidth;
    private int viewHeight;


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewWidth=w;
        viewHeight=h;
    }

    private boolean initialized=false;


    float[] lookTable =new float[360];
    private void initLookUpTable(){
        for (int ii=0;ii<360;ii++){
            lookTable[ii]= (float) Math.sin(ii*2*Math.PI/360);
        }
    }


    private void init(Canvas canvas){
        if (!initialized) {
            Bitmap bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
            canvas.drawBitmap(bitmap, 0, 0, null);
            resetParametars();
            initialized=true;
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        init(canvas);
        drawWave(canvas);
    }

    private int offset=0;
    private void redrawWave(int offset){  // 360 --> 2pi
        this.offset=offset;
        invalidate();
    }


    public void startWave(){
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 360);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(6000);
//        valueAnimator.setRepeatMode(ValueAnimator.INFINITE);
        valueAnimator.setRepeatCount(Integer.MAX_VALUE);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                WaveView.this.redrawWave((Integer)valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }



    private static final int cycle =4;


    private float mDefaultWaterLevel,mDefaultAmplitude;
    private float mDefaultAngularFrequency;
    private Paint wavePaint1,wavePaint2;

    private void resetParametars(){
        wavePaint1 = new Paint();
        wavePaint1.setStrokeWidth(1f);
        wavePaint1.setColor(getResources().getColor(R.color.Red400T));
        wavePaint1.setAntiAlias(true);

        wavePaint2= new Paint();
        wavePaint2.setStrokeWidth(1f);
        wavePaint2.setColor(getResources().getColor(R.color.Red400T));
        wavePaint2.setAntiAlias(true);


        int period =viewWidth / cycle;  // the view contains "cycle" periods of wave   // period --> number of pixels per period
        mDefaultAngularFrequency =360f/period;  // increase 1 pixel  --> increase mDefaultAngularFrequency in the lookUpTable

        mDefaultWaterLevel=viewHeight/2;
        mDefaultAmplitude=viewHeight/6;

    }

    private void drawWave(Canvas canvas){

        canvas.drawColor(getResources().getColor(R.color.Teal400T));
        float waveX1 = 0;
        final float endX = viewWidth;
        final float endY = viewHeight;

        float tableIndex=0;
        while (waveX1 < endX) {

            int startY = (int) (mDefaultWaterLevel + mDefaultAmplitude * lookTable[((int)tableIndex+offset)%360]);
            canvas.drawLine(waveX1, startY, waveX1, endY, wavePaint1); // draw wave1
            int startY2 = (int) (mDefaultWaterLevel + mDefaultAmplitude * lookTable[((int)tableIndex+90+offset)%360]);   // offset by 1/4 period
            canvas.drawLine(waveX1, startY2, waveX1, endY, wavePaint2); // draw wave2
            waveX1++;

            tableIndex= (tableIndex+mDefaultAngularFrequency)%360;
        }

    }





}
