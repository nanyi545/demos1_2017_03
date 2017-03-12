package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewTreeObserver;
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


    public static void start(Context c){
        Intent i=new Intent(c,TestPullRefreshActivity4.class);
        c.startActivity(i);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);   // use this to draw below status bar

        setContentView( R.layout.activity_test_pull_refresh4);

        CoordinatorPullToRefresh2 holder= (CoordinatorPullToRefresh2) findViewById(R.id.activity_test_pull_refresh4);
        holder.setRevealContent(RevealContentImp.class);


        final StoppableScrollView ssv= (StoppableScrollView) findViewById(R.id.ssv);
        holder.setViews(ssv);

        final ImageView iconLocation= (ImageView) findViewById(R.id.location_icon);
        ViewTreeObserver viewTreeObserver = iconLocation.getViewTreeObserver();
        if (viewTreeObserver.isAlive()) {
            viewTreeObserver.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {


                    RelativeLayout.LayoutParams params= (RelativeLayout.LayoutParams) iconLocation.getLayoutParams();
                    RelativeLayout parent= (RelativeLayout) iconLocation.getParent();
                    int iconHeight = iconLocation.getHeight();
                    int parentHeight=parent.getHeight();
                    Log.i("ccc","iconHeight:"+iconHeight+"    parentHeight:"+parentHeight);

                    int newTopMargin=(parentHeight-iconHeight)/2;
                    params.topMargin=newTopMargin;
//                    params.bottomMargin=newTopMargin;
                    iconLocation.setLayoutParams(params);


                    if(iconLocation.getViewTreeObserver().isAlive()){
                        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                            iconLocation.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                        } else {
                            iconLocation.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                        }
                    }

                }
            });
        }


    }


}
