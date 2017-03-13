package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nanyi545.www.materialdemo.R;
import com.nanyi545.www.materialdemo.collapse_layout.CollapsHolder;
import com.nanyi545.www.materialdemo.utils.MyRVAdapter;


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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);   // use this to draw below status bar

        Log.i(TAG,"--TestPullRefreshActivity4--ONCREATE--");
        setContentView( R.layout.activity_test_pull_refresh4);


        CoordinatorPullToRefresh2 holder= (CoordinatorPullToRefresh2) findViewById(R.id.activity_test_pull_refresh4);
        holder.setRevealContent(RevealContentImp.class);


        final StoppableScrollView ssv= (StoppableScrollView) findViewById(R.id.ssv);
        holder.setViews(ssv);

        appBarLo= (AppBarLayout) findViewById(R.id.appbar);
        fakeAppBarLo= (AppBarLayout) findViewById(R.id.fake_appbar);

        RelativeLayout gap= (RelativeLayout) findViewById(R.id.gap);
        final CollapsHolder holder1= (CollapsHolder) findViewById(R.id.collapse_holder1);


        final EditText search= (EditText) findViewById(R.id.searcher);
//        search.setHintTextColor(Color.BLACK);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int[] pos=new int[2];
                search.getLocationInWindow(pos);

                Intent i=TestPullRefreshA4_SearchActivity.getStartIntent(TestPullRefreshActivity4.this,pos[1],pos[0],search.getWidth(),search.getHeight(),appBarLo.getHeight(),holder1.getAppearantHeight());
                TestPullRefreshActivity4.this.startActivityForResult(i,REQUEST_SEARCH_CODE);
            }
        });

    }



    private static final int REQUEST_SEARCH_CODE=11;


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_SEARCH_CODE){


            fakeAppBarLo.setVisibility(View.VISIBLE);
            appBarLo.setVisibility(View.INVISIBLE);


            final int appBarHeight=data.getIntExtra(TestPullRefreshA4_SearchActivity.APP_BAR_HEIGHT_KEY,0);
            final int toolBarMarginTop=data.getIntExtra(TestPullRefreshA4_SearchActivity.TOOL_BAR_MARGIN_TOP,0);

            Log.i(TAG,"appBarHeight:"+appBarHeight+"   toolBarMarginTop:"+toolBarMarginTop);


        }

    }






}
