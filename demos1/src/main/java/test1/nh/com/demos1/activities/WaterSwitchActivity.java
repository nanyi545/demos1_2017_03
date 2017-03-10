package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;

import github.chenupt.springindicator.SpringIndicator;
import test1.nh.com.demos1.R;

public class WaterSwitchActivity extends AppCompatActivity {


    private ViewPager viewPager;

    /**装分页显示的view的数组*/
    private ArrayList<View> pageViews;
    private ImageView imageView;

    /**将小圆点的图片用数组表示*/
    private ImageView[] imageViews;

    //包裹滑动图片的LinearLayout
    private ViewGroup viewPics;

    //包裹小圆点的LinearLayout
    private ViewGroup viewPoints;

    private SpringIndicator indicator;



    public static void start(Context c){
        Intent i=new Intent(c,WaterSwitchActivity.class);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //将要分页显示的View装入数组中
        LayoutInflater inflater = getLayoutInflater();
        pageViews = new ArrayList<View>();
        pageViews.add(inflater.inflate(R.layout.viewpager_page1, null));
        pageViews.add(inflater.inflate(R.layout.viewpager_page2, null));
        pageViews.add(inflater.inflate(R.layout.viewpager_page3, null));

        //创建imageviews数组，大小是要显示的图片的数量
        imageViews = new ImageView[pageViews.size()];
        //从指定的XML文件加载视图
        viewPics = (ViewGroup) inflater.inflate(R.layout.activity_water_switch, null);

        //实例化小圆点的linearLayout和viewpager
        viewPoints = (ViewGroup) viewPics.findViewById(R.id.viewGroup);
        viewPager = (ViewPager) viewPics.findViewById(R.id.guidePages);
        indicator = (SpringIndicator) viewPics.findViewById(R.id.indicator);

        setContentView(viewPics);

        //设置viewpager的适配器和监听事件
        viewPager.setAdapter(new GuidePageAdapter(pageViews));
        indicator.setViewPager(viewPager);
    }



    static class GuidePageAdapter extends PagerAdapter{

        private ArrayList<View> pageViews;

        public GuidePageAdapter(ArrayList<View> pageViews){
            this.pageViews=pageViews;

        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager)container).removeView(pageViews.get(position));
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ((ViewPager) container).addView(pageViews.get(position));
            return pageViews.get(position);
        }

        @Override
        public int getCount() {
            return pageViews.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }


}
