package test1.nh.com.demos1.activities;

import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.customView.CircleViewPlus;
import test1.nh.com.demos1.customView.ExpandView;
import test1.nh.com.demos1.customView.LoadView;
import test1.nh.com.demos1.customView.PieChartc;
import test1.nh.com.demos1.customView.WaveView;

public class CustomViewActivity extends AppCompatActivity {


    private ExpandView shape;

    private CircleViewPlus cvplus;
    private RelativeLayout layout;

    private TextView animateTV;


    int test1;

    static class MyRunnable implements Runnable{
        @Override
        public void run() {
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main_activity_from, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_folder_tab);


        Resources res = getResources();

        final PieChartc pie = (PieChartc) this.findViewById(R.id.costum3);
        pie.addItem("Agamemnon", 2, res.getColor(R.color.seafoam));
        pie.addItem("Bocephus", 3.5f, res.getColor(R.color.chartreuse));
        pie.addItem("Calliope", 2.5f, res.getColor(R.color.emerald));
        pie.addItem("Daedalus", 3, res.getColor(R.color.bluegrass));
        pie.addItem("Euripides", 1, res.getColor(R.color.turquoise));
        pie.addItem("Ganymede", 3, res.getColor(R.color.slate));

        ((Button) findViewById(R.id.Reset)).setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                pie.setCurrentItem(0);
            }
        });




        shape = (ExpandView) findViewById(R.id.ev1);
        shape.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (shape.state == ExpandView.EXPAND_STATE) {
                    contractView();
                    shape.state = ExpandView.CONTRACT_STATE;
                }
                else {
                    expandView();
                    shape.state = ExpandView.EXPAND_STATE;
                }
////--------------------why below doesn't work/???
//                if (shape.state == ExpandView.EXPAND_STATE) {
//                    contractView();
//                    shape.state = ExpandView.CONTRACT_STATE;
//                }
//                if (shape.state == ExpandView.CONTRACT_STATE) {
//                    expandView();
//                    shape.state = ExpandView.EXPAND_STATE;
//                }
            }
        });


        // circular view with progress indicator !
        cvplus=(CircleViewPlus)findViewById(R.id.cvplus);
        layout=(RelativeLayout) findViewById(R.id.container_rel);
//        cvplus.setProgress(50);
//        cvplus.resetCircleView(layout);


        animateTV=(TextView)findViewById(R.id.animate_tv);



        lv1= (LoadView) findViewById(R.id.loadview);
        lateInit.sendEmptyMessageDelayed(1,1000);


        WaveView wa=(WaveView) findViewById(R.id.wave);
        wa.startWave();
    }

    LoadView lv1;
    Handler lateInit=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            lv1.animateTo(1f);
        }
    };



    public void animateTextView(int initialValue, int finalValue, final TextView  textview) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(initialValue, finalValue);
        valueAnimator.setDuration(1500);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                textview.setText(valueAnimator.getAnimatedValue().toString());
            }
        });
        valueAnimator.start();
    }


    PopupWindow pw;
    @Override
    protected void onResume() {
        super.onResume();
        cvplus.animateTo(6);
        animateTextView(0,2000,animateTV);


        // dim it ?
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        pw = new PopupWindow(inflater.inflate(R.layout.popup_example, null, false),100,100, true);

    }

    public void showPop(View v){
        pw.showAtLocation(this.findViewById(R.id.relative_page), Gravity.CENTER, 0, 0);
        WindowManager.LayoutParams windowManager = getWindow().getAttributes();
        windowManager.dimAmount = 0.75f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void contractView() {
//        Log.i("AAA",""+shape.getMeasuredWidth());
        ValueAnimator anim = ValueAnimator.ofInt(shape.getMeasuredWidth(), 108);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = shape.getLayoutParams();
                layoutParams.width = val;
                shape.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(1000);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private void expandView() {
        ValueAnimator anim = ValueAnimator.ofInt(shape.getMeasuredWidth(), 324);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                int val = (Integer) valueAnimator.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = shape.getLayoutParams();
                layoutParams.width = val;
                shape.setLayoutParams(layoutParams);
            }
        });
        anim.setDuration(1000);
        anim.start();
    }


}
