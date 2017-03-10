

package test1.nh.com.demos1.activities.tab_layout_viewpager;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import test1.nh.com.demos1.R;

/**
 * https://guides.codepath.com/android/Google-Play-Style-Tabs-using-TabLayout#design-support-library
 */
public class TestTabActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,TestTabActivity.class);
        c.startActivity(i);
    }

    SampleFragmentPagerAdapter pagerAdapter;


    RelativeLayout rl1;
    ValueAnimator hideBottomPanel,show;
    private int state=0;  //  0 open  , 1 closed
    public void toggleBar(){
        if (state==0){
            hideBottomPanel.start();
            state=1;
            return;
        }
        if (state==1){
            show.start();
            state=0;
            return;
        }
    }

    private void initHideAnimation() {
        hideBottomPanel = ValueAnimator.ofInt(100, 0);
        hideBottomPanel.setStartDelay(500);
        hideBottomPanel.setInterpolator(new DecelerateInterpolator());
        hideBottomPanel.setDuration(500);
        hideBottomPanel.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) rl1.getLayoutParams();
                params1.height=(Integer) valueAnimator.getAnimatedValue();
                rl1.setLayoutParams(params1);
            }
        });

        show = ValueAnimator.ofInt(0, 100);
        show.setStartDelay(500);
        show.setInterpolator(new DecelerateInterpolator());
        show.setDuration(500);
        show.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) rl1.getLayoutParams();
                params1.height=(Integer) valueAnimator.getAnimatedValue();
                rl1.setLayoutParams(params1);
            }
        });

    }






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mai);
        initHideAnimation();

        rl1= (RelativeLayout) findViewById(R.id.messagte_bar);
        findViewById(R.id.test_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleBar();
            }
        });

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        pagerAdapter=new SampleFragmentPagerAdapter(getSupportFragmentManager(),
                TestTabActivity.this);
        viewPager.setAdapter(pagerAdapter);

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(pagerAdapter.getTabView(i));
        }




    }

}
