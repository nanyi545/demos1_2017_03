package com.nanyi545.www.materialdemo.testPullToRefresh.eleme;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.AppBarLayout;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.nanyi545.www.materialdemo.R;
import com.nanyi545.www.materialdemo.collapse_layout.CollapsHolder;
import com.nanyi545.www.materialdemo.testPullToRefresh.CoordinatorPullToRefresh2;
import com.nanyi545.www.materialdemo.testPullToRefresh.RevealContentImp;
import com.nanyi545.www.materialdemo.testPullToRefresh.StoppableScrollView;


/**
 * imitate 饿了么
 */
public class TestPullRefreshActivity4 extends AppCompatActivity {


    private static final String TAG=TestPullRefreshActivity4.class.getSimpleName();

    public static void start(Context c){
        Intent i=new Intent(c,TestPullRefreshActivity4.class);
        c.startActivity(i);
    }

    AppBarLayout appBarLo,fakeAppBarLo;
    RelativeLayout fakeSearchHolder,searchHolder;
    EditText fakeSearcher,searcher;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);   // use this to draw below status bar


        setContentView( R.layout.activity_test_pull_refresh4);


        CoordinatorPullToRefresh2 holder= (CoordinatorPullToRefresh2) findViewById(R.id.activity_test_pull_refresh4);
        holder.setRevealContent(RevealContentImp.class);


        final StoppableScrollView ssv= (StoppableScrollView) findViewById(R.id.ssv);
        holder.setViews(ssv);

        appBarLo= (AppBarLayout) findViewById(R.id.appbar);
        fakeAppBarLo= (AppBarLayout) findViewById(R.id.fake_appbar);
        fakeSearchHolder= (RelativeLayout) findViewById(R.id.fake_searchHolder);

        searchHolder= (RelativeLayout) findViewById(R.id.gap);
        searcher= (EditText) findViewById(R.id.searcher);
        fakeSearcher= (EditText) findViewById(R.id.fake_searcher);

        final CollapsHolder holder1= (CollapsHolder) findViewById(R.id.collapse_holder1);


        final EditText search= (EditText) findViewById(R.id.searcher);
//        search.setHintTextColor(Color.BLACK);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] pos=new int[2];
                search.getLocationInWindow(pos);

                Intent i= TestPullRefreshA4_SearchActivity.getStartIntent(TestPullRefreshActivity4.this,pos[1],pos[0],search.getWidth(),search.getHeight(),appBarLo.getHeight(),holder1.getAppearantHeight());
                TestPullRefreshActivity4.this.startActivityForResult(i,REQUEST_SEARCH_CODE);
            }
        });




    }



    private static final int REQUEST_SEARCH_CODE=11;


    float  appbarEndHeight,appbarStartHeight;
    float  holderMarginTopStart,holderMarginTopEnd;
    float  marginLeftStart,marginLeftEnd;
    float  paddingLeftStart,paddingLeftEnd;
    int paddingTop,paddingBottom,paddingRight;


    Scroller animator;

    Handler animateHandler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(animator.computeScrollOffset()){
                int progress=animator.getCurrX();

                float appbarCurrentHeight=(appbarEndHeight-appbarStartHeight)/1000*progress+appbarStartHeight;


                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fakeAppBarLo.getLayoutParams();
                params.height= (int) appbarCurrentHeight;
                fakeAppBarLo.setLayoutParams(params);


                float holdCurrentMarginTop=(holderMarginTopEnd-holderMarginTopStart)/1000*progress+holderMarginTopStart;
                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) fakeSearchHolder.getLayoutParams();
                params1.setMargins(0, (int) holdCurrentMarginTop,0,0);



                float currentMarginLeft=(marginLeftEnd-marginLeftStart)/1000*progress+marginLeftStart;
                RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) fakeSearcher.getLayoutParams();
                params2.leftMargin= (int) currentMarginLeft;
                searcher.setLayoutParams(params2);



                float currentPaddingLeft=(paddingLeftEnd-paddingLeftStart)/1000*progress+paddingLeftStart;
                fakeSearcher.setPadding((int) currentPaddingLeft,paddingTop,paddingRight,paddingBottom);

                Log.i(TAG,"currentMarginLeft:"+currentMarginLeft+"   currentPaddingLeft:"+currentPaddingLeft);



                animateHandler.sendEmptyMessage(0);


            } else {

                fakeAppBarLo.setVisibility(View.INVISIBLE);
                searchHolder.setVisibility(View.VISIBLE);

            }

        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_SEARCH_CODE){

            if (animator==null){
                animator=new Scroller(this);
            }

            fakeAppBarLo.setVisibility(View.VISIBLE);
            searchHolder.setVisibility(View.INVISIBLE);


            final float appBarHeight=data.getFloatExtra(TestPullRefreshA4_SearchActivity.KEY1,-1);
            final float toolBarMarginTop=data.getFloatExtra(TestPullRefreshA4_SearchActivity.KEY2,-1);
            final float toolBarMarginLeft= data.getFloatExtra(TestPullRefreshA4_SearchActivity.KEY3,-1);
            final float eTpaddingLeft= data.getFloatExtra(TestPullRefreshA4_SearchActivity.KEY4,-1);



            if(ViewCompat.isLaidOut(fakeAppBarLo)){
                startTransitionAnimation(appBarHeight,toolBarMarginTop,toolBarMarginLeft,eTpaddingLeft);
            } else {
                fakeAppBarLo.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        if(fakeAppBarLo.getViewTreeObserver().isAlive()){
                            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                                fakeAppBarLo.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                            } else {
                                fakeAppBarLo.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                            }
                        }
                        startTransitionAnimation(appBarHeight,toolBarMarginTop,toolBarMarginLeft,eTpaddingLeft);
                    }

                });


            }







        }


    }




    private void startTransitionAnimation(float appBarHeight,float toolBarMarginTop,float toolBarMarginLeft,float eTpaddingLeft) {
        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) fakeAppBarLo.getLayoutParams();
        appbarStartHeight=fakeAppBarLo.getHeight();
        appbarEndHeight=appBarHeight;

        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) fakeSearchHolder.getLayoutParams();
        holderMarginTopStart=0;
        holderMarginTopEnd=toolBarMarginTop;


        RelativeLayout.LayoutParams params2 = (RelativeLayout.LayoutParams) fakeSearcher.getLayoutParams();
        marginLeftEnd=params2.leftMargin;
        marginLeftStart=toolBarMarginLeft;
        params2.leftMargin= (int) toolBarMarginLeft;
        fakeSearcher.setLayoutParams(params2);


        paddingBottom=fakeSearcher.getPaddingBottom();
        paddingTop=fakeSearcher.getPaddingTop();
        paddingRight=fakeSearcher.getPaddingRight();
        paddingLeftEnd=fakeSearcher.getPaddingLeft();
        paddingLeftStart=eTpaddingLeft;
        fakeSearcher.setPadding((int) paddingLeftStart,paddingTop,paddingRight,paddingBottom);


        animator.startScroll(0,0,1000,0,500);
        animateHandler.sendEmptyMessage(0);
    }





}
