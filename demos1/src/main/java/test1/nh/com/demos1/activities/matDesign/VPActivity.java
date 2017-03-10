package test1.nh.com.demos1.activities.matDesign;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.matDesign.adapter_MD.MyVPAdapter;
import test1.nh.com.demos1.fragments.AnimationFragment;
import test1.nh.com.demos1.fragments.MPchartDemoFragment;
import test1.nh.com.demos1.utils.images.BitmapUtil;

public class VPActivity extends FragmentActivity {

    Button b1;
    Button b2;


    ViewPager viewPager;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    //cursor to show current position in VP
    ImageView cursorIV;
    int cursorImgWidth;
    int cursorOffset;
    int cursorIndex;  // current VP index

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vp);
        initCursorImage();
        b1= (Button) findViewById(R.id.b_item1);
        b2= (Button) findViewById(R.id.b_item2);

        // setup the list of fragments
        AnimationFragment fragment1 = new AnimationFragment();
        MPchartDemoFragment fragment2 = new MPchartDemoFragment();
        fragments.add(fragment1);
        fragments.add(fragment2);

        viewPager = (ViewPager) findViewById(R.id.vp_pager);
        MyVPAdapter adapter = new MyVPAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);
        viewPager.addOnPageChangeListener(new MyPageChangeListener());

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(0);
            }
        });

        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewPager.setCurrentItem(1);
            }
        });

    }




    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        int one = cursorOffset * 2 + cursorImgWidth; // page1 --> page2 偏移量

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageSelected(int arg0) {
            Animation animation = null;
            switch (arg0) {
                // 移动到0
                case 0:
                    if (cursorIndex == 1) {
                        animation = new TranslateAnimation(one, 0, 0, 0);
                    }
                    break;
                // 移动到1
                case 1:
                    if (cursorIndex == 0) {
                        animation = new TranslateAnimation(cursorOffset, one, 0, 0);
                    }
                    break;
            }
            cursorIndex = arg0;
            if(animation != null){
                animation.setFillAfter(true); // 图片停在动画停止位置
                animation.setDuration(300);
                cursorIV.startAnimation(animation);
            }

        }
    }


    /**
     * 初始化游标
     */
    private void initCursorImage() {
        Bitmap mp = BitmapUtil.overlapBitmapColor(BitmapFactory.decodeResource(
                        getResources(), R.drawable.bg_mutual_main_cursorimg),
                getResources().getColor(R.color.Teal600));
        cursorIV = (ImageView) findViewById(R.id.main_iv_cursor);
        cursorIV.setImageBitmap(mp);
        cursorImgWidth = mp.getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screentWidth = dm.widthPixels;
        cursorOffset = (screentWidth / 2 - cursorImgWidth) / 2;
        Matrix matrix = new Matrix();
        matrix.postTranslate(cursorOffset, 0);
        cursorIV.setImageMatrix(matrix);
    }



}
