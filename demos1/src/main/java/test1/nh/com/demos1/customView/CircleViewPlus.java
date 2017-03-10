package test1.nh.com.demos1.customView;

/**
 * Created by zhengdonghui1 on 15/7/15.
 */
import android.animation.AnimatorSet;
import android.animation.FloatEvaluator;
import android.animation.IntEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.DecelerateInterpolator;

import test1.nh.com.demos1.R;


public class CircleViewPlus extends View {

    private int startAngle=150;  // default start angle
    private int getTotalArcLength(){  // get total arc length so that the arc is symetric with respect to 90deg
        return 360-(startAngle-90)*2;
    }



    Paint paint,textpaint,progresspaint,roundpaint,s_roundpaint,textpaint_3digit;
    RectF area;
    String str = "";
    float value=0f;
    int txt_X = 100,txt_Y = 100;
    float c_x = 0,c_y = 0,radius = 0;
    float s_c_x = 0,s_c_y = 0,s_radius = 13f;
    //LinearGradient shader,progressshader;


    int arcBarColor,progressBarColor,progressTextColor;  // 圆弧背景色，圆弧进度色，进度字体色


    public CircleViewPlus(Context context, AttributeSet attrs, int defStyle,int startAngle) {
        this(context, attrs, defStyle);
        this.startAngle=startAngle;
    }


    public CircleViewPlus(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.CircleViewPlus
        );

        try {
            arcBarColor = a.getColor(R.styleable.CircleViewPlus_arcBarColor, 0xff888888);
            progressBarColor= a.getColor(R.styleable.CircleViewPlus_progressBarColor, 0xff000000);
            progressTextColor= a.getColor(R.styleable.CircleViewPlus_progressTextColor, 0xffffffff);
            value=a.getFloat(R.styleable.CircleViewPlus_progress, 0);
        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle();
        }


        init();
        // TODO Auto-generated constructor stub
    }

    public CircleViewPlus(Context context, AttributeSet attrs) {
        super(context, attrs);

        TypedArray a = context.obtainStyledAttributes(
                attrs,
                R.styleable.CircleViewPlus
        );

        try {
            arcBarColor = a.getColor(R.styleable.CircleViewPlus_arcBarColor, 0xff888888);
            progressBarColor= a.getColor(R.styleable.CircleViewPlus_progressBarColor, 0xff000000);
            progressTextColor= a.getColor(R.styleable.CircleViewPlus_progressTextColor, 0xffffffff);
            value=a.getFloat(R.styleable.CircleViewPlus_progress, 0);
        } finally {
            // release the TypedArray so that it can be reused.
            a.recycle();
        }

        init();
        // TODO Auto-generated constructor stub
    }

    public CircleViewPlus(Context context) {
        super(context);
        init();
        // TODO Auto-generated constructor stub
    }

    public void setProgress(float value){
        this.value = value;
        resetCircleView();
        invalidate();
    }




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


    int viewWidth;
    int viewHeight;

    public void resetCircleView(int value){
        this.value = value;
        resetCircleView();
    }

    public void animateTo(int percent){
//        final ObjectAnimator animator = ObjectAnimator.ofFloat(this, "progress",0f, (float)percent);
//        animator.setDuration(3000L);
//        animator.setEvaluator(new FloatEvaluator());
//        animator.setInterpolator(new DecelerateInterpolator(1));
//        animator.start();

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0f, percent);
        valueAnimator.setStartDelay(100);
        valueAnimator.setDuration(2000);
        valueAnimator.setInterpolator(new DecelerateInterpolator(1f));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                CircleViewPlus.this.setProgress((Float)valueAnimator.getAnimatedValue());
            }
        });
        valueAnimator.start();
    }

    public void resetCircleView(){
//        Log.i("hcyd","resetCiecleView.value="+value);


//        Log.i("hcyd","this.value="+this.value);
//        float radius = WidgetController.getWidth(view)/2*0.87f;   // the ratio is set to 0.87f, so that the arc does not get clipped by the container (relative layout)
        float radius = viewWidth/2*0.87f;

//        float width = 5f;
        float width = radius/10;


        double textsize =viewWidth/2*0.3;
        int size = (int)textsize;
        this.c_x = viewWidth/2;
        this.c_y = viewHeight/2;
        float R = viewWidth/2;
        float r = radius;
        double x = R+r*Math.cos((value*getTotalArcLength()/100+startAngle)*Math.PI/180);
        double y = R+r*Math.sin((value*getTotalArcLength()/100+startAngle)*Math.PI/180);

        this.s_c_x = (float)x;
        this.s_c_y = (float)y;
        this.radius = viewWidth/2;
//        Log.i("hcyd","radios="+radius);
//        Log.i("hcyd","width="+WidgetController.getWidth(view));
//        Log.i("hcyd","height="+WidgetController.getHeight(view));
//        Log.i("hcyd","StrokeWidth="+paint.getStrokeWidth());
        this.paint.setStrokeWidth(width);
        this.progresspaint.setStrokeWidth(width);

        s_radius=width*1.4f;
        textpaint.setStrokeWidth(s_radius/10);


        this.textpaint.setTextSize(s_radius);
        this.textpaint_3digit.setTextSize(s_radius*0.75f);

        this.area = new RectF(viewWidth/2-radius,
                viewWidth/2-radius,
                viewWidth/2+radius,
                viewWidth/2+radius);
//        Log.i("hcyd", "area=" + area.toString());
        this.txt_X = viewWidth/2 - size-20;
        this.txt_Y = viewWidth/2 + size/3;

//        invalidate();
    }

    public void init() {
        paint = new Paint();
        paint.setStrokeWidth(5f);
//        paint.setColor(getResources().getColor(R.color.Red200));
        paint.setColor(arcBarColor);

        paint.setStyle(Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAntiAlias(true);
        paint.setDither(true);
        progresspaint = new Paint();
        progresspaint.setStrokeWidth(5f);
//        progresspaint.setColor(getResources().getColor(R.color.Red600));
        progresspaint.setColor(progressBarColor);

        progresspaint.setStyle(Style.STROKE);
        progresspaint.setStrokeCap(Paint.Cap.ROUND);
        progresspaint.setStrokeJoin(Paint.Join.ROUND);
        progresspaint.setAntiAlias(true);
        progresspaint.setDither(true);
        roundpaint = new Paint();
        roundpaint.setColor(getResources().getColor(R.color.Blue500));
        s_roundpaint = new Paint();
//        s_roundpaint.setColor(getResources().getColor(R.color.Red600));   // progress indicator circle
        s_roundpaint.setColor(progressBarColor);

        textpaint = new Paint();
        textpaint.setTextSize(35f);
        textpaint_3digit = new Paint();
        textpaint_3digit.setTextSize(35f);

//        textpaint.setColor(getResources().getColor(R.color.White));// progress indicator text
        textpaint.setColor(progressTextColor);
        textpaint_3digit.setColor(progressTextColor);

        area=new RectF(0,0,233,233);

//        shader =new LinearGradient(0, 0, 400, 0, getResources().getColor(R.color.app_theme_orange),

//                getResources().getColor(R.color.app_theme_orange),
//                Shader.TileMode.CLAMP);
//        progressshader =
//        paint.setShader(shader);
        resetCircleView();

    }



    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        //canvas.drawColor(Color.WHITE);
//        canvas.drawCircle(c_x, c_y, radius * 0.72f, roundpaint);  // empty circle in the center ...  not needed here
        resetCircleView();
        canvas.drawArc(area, startAngle, getTotalArcLength(), false, paint);
        //canvas.drawCircle();
        int value_int=(int)value;

        if (value_int == 0){
            canvas.drawText(""+value_int+"%", s_c_x-s_radius*(0.6f), s_c_y+s_radius*(0.4f), textpaint);
        }else {
            canvas.drawArc(area, startAngle, value*getTotalArcLength()/100 , false, progresspaint);
            canvas.drawCircle(s_c_x,s_c_y,s_radius,s_roundpaint);  // progress indicator
            if ((value_int>=0)&&(value_int<=9)) canvas.drawText(""+value_int+"%", s_c_x-s_radius*(0.6f), s_c_y+s_radius*(0.4f), textpaint);   // number on the indicator 0-9    -->  the ratio are set to place the number in the middle of the circle
            if ((value_int>9)&&(value_int<=99)) canvas.drawText(""+value_int+"%", s_c_x-s_radius*(0.85f), s_c_y+s_radius*(0.4f), textpaint); // number on the indicator 10-99  -->  the ratio are set to place the number in the middle of the circle
            if (value_int>99) canvas.drawText(""+value_int+"%", s_c_x-s_radius*(0.85f), s_c_y+s_radius*(0.3f), textpaint_3digit);                 // number on the indicator >99    -->  the ratio are set to place the number in the middle of the circle
        }
//        canvas.drawText(str, txt_X, txt_Y, textpaint);
    }

}
