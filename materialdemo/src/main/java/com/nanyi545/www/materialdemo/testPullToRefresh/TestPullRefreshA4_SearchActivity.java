package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.nanyi545.www.materialdemo.R;

public class TestPullRefreshA4_SearchActivity extends AppCompatActivity {



    private static final String TAG= TestPullRefreshA4_SearchActivity.class.getSimpleName();



    AppBarLayout appbar;
    float appbarStartHeight,appbarEndHeight;

    EditText searcher;
    RelativeLayout searchHolder;
    float holderMarginTopStart,holderMarginTopEnd;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);   // use this to draw below status bar
        overridePendingTransition(0, 0);


        animator=new Scroller(this);

        setContentView(R.layout.activity_search);


        appbar= (AppBarLayout) findViewById(R.id.app_bar);
        searchHolder= (RelativeLayout) findViewById(R.id.searchHolder);

        initTransitionView();



    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(0, 0);

        Intent returnIntent = getIntent();
        returnIntent.putExtra(APP_BAR_HEIGHT_KEY,appbarStartHeight);
        returnIntent.putExtra(TOOL_BAR_MARGIN_TOP,holderMarginTopStart);
    }

    private void initTransitionView(){

        Bundle bundle=getIntent().getExtras();
        int etTop=bundle.getInt(TOP_KEY);
        int etLeft=bundle.getInt(LEFT_KEY);
        int etWidth=bundle.getInt(WIDTH_KEY);
        int etHeight=bundle.getInt(HEIGHT_KEY);
        final int appBarHeight=bundle.getInt(APP_BAR_HEIGHT_KEY);
        final int toolBarMarginTop=bundle.getInt(TOOL_BAR_MARGIN_TOP);




        appbar.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
                appbarEndHeight=appbar.getHeight();


                params.height=appBarHeight;
                appbarStartHeight=appBarHeight;



                Log.i(TAG,"appbarStartHeight:"+appbarStartHeight+"  appbarEndHeight:"+appbarEndHeight);

                appbar.setLayoutParams(params);


                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) searchHolder.getLayoutParams();
                params1.setMargins(0,toolBarMarginTop,0,0);

                holderMarginTopStart=toolBarMarginTop;
                holderMarginTopEnd=0;

                if(appbar.getViewTreeObserver().isAlive()){
                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                        appbar.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    } else {
                        appbar.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }


                final ObjectAnimator animator1 = ObjectAnimator.ofInt(appbar,"layout_height",appBarHeight, (int)appbarEndHeight );
                final ObjectAnimator animator2 = ObjectAnimator.ofInt(searchHolder,"layout_marginTop",toolBarMarginTop, 0 );


                animator.startScroll(0,0,1000,0,1000);
                animateHandler.sendEmptyMessage(0);
            }
        });
    }




    Scroller animator;
    Handler animateHandler=new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            if (animator.computeScrollOffset()){
                int progress=animator.getCurrX();

                float appbarCurrentHeight=(appbarEndHeight-appbarStartHeight)/1000*progress+appbarStartHeight;

                CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) appbar.getLayoutParams();
                params.height= (int) appbarCurrentHeight;
                appbar.setLayoutParams(params);


                float holdCurrentMarginTop=(holderMarginTopEnd-holderMarginTopStart)/1000*progress+holderMarginTopStart;

                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) searchHolder.getLayoutParams();
                params1.setMargins(0, (int) holdCurrentMarginTop,0,0);
                
                animateHandler.sendEmptyMessage(0);
            }

        }
    };












    private static final String TOP_KEY="TOP_KEY";
    private static final String LEFT_KEY="LEFT_KEY";
    private static final String WIDTH_KEY="WIDTH_KEY";
    private static final String HEIGHT_KEY="HEIGHT_KEY";
    public static final String APP_BAR_HEIGHT_KEY="APP_BAR_HEIGHT_KEY";
    public static final String TOOL_BAR_MARGIN_TOP="TOOL_BAR_MARGIN_TOP";

    public static Intent getStartIntent(Context c,int top,int left,int width, int height,int appBarHeight,int toolBarMarginTop){
        Intent startIntent = new Intent(c, TestPullRefreshA4_SearchActivity.class);
        startIntent.putExtra(TOP_KEY,top);
        startIntent.putExtra(LEFT_KEY,left);
        startIntent.putExtra(WIDTH_KEY,width);
        startIntent.putExtra(HEIGHT_KEY,height);
        startIntent.putExtra(APP_BAR_HEIGHT_KEY,appBarHeight);
        startIntent.putExtra(TOOL_BAR_MARGIN_TOP,toolBarMarginTop);
        return startIntent;
    }









}
