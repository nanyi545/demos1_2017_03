package test1.nh.com.demos1.activities.vertical_scroll;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.List;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 2017/3/28.
 */

public class TabScrollable extends LinearLayout implements SingleScrollView.Scrollable {

    class TestTabAdapter extends FragmentPagerAdapter {
        private List<Fragment> list_fragment;                         //fragment列表
        private List<String> list_Title;                              //tab名的列表

        public TestTabAdapter(FragmentManager fm, List<Fragment> list_fragment, List<String> list_Title) {
            super(fm);
            this.list_fragment = list_fragment;
            this.list_Title = list_Title;
        }

        @Override
        public Fragment getItem(int position) {
            return list_fragment.get(position);
        }

        @Override
        public int getCount() {
            return list_Title.size();
        }

        //此方法用来显示tab上的名字
        @Override
        public CharSequence getPageTitle(int position) {
            return list_Title.get(position % list_Title.size());
        }
    }



    public TabScrollable(Context context) {
        this(context,null);
    }

    public TabScrollable(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public TabScrollable(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }



    TabLayout tabLayout;
    ViewPager vp;

    private List<Fragment> list_fragment;                   //定义要装fragment的列表
    private List<String> list_title;                       //tab名称列表


    private void init(Context context){
        LayoutInflater inflater=((AppCompatActivity)context).getLayoutInflater();
        View root =inflater.inflate(R.layout.tab_test1,this,true);
        tabLayout= (TabLayout) root.findViewById(R.id.finance_content_tab);
        vp= (ViewPager) root.findViewById(R.id.finance_content_vp);

        //设置TabLayout的模式
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        //为TabLayout添加tab名称
        tabLayout.addTab(tabLayout.newTab().setText("tab1"));
        tabLayout.addTab(tabLayout.newTab().setText("tab2"));
        tabLayout.addTab(tabLayout.newTab().setText("tab3"));

        //将fragment装进列表中
        list_fragment = new ArrayList<>();
        list_fragment.add(ItemFragment.newInstance(1));
        list_fragment.add(ItemFragment.newInstance(2));
        list_fragment.add(ItemFragment.newInstance(3));
        //将tab装进列表
        list_title = new ArrayList<>();
        list_title.add("项目详情");
        list_title.add("相关资料");
        list_title.add("投资记录");


        TestTabAdapter adapter = new TestTabAdapter(((AppCompatActivity)context).getSupportFragmentManager(), list_fragment, list_title);

        //viewpager加载adapter
        vp.setAdapter(adapter);
        //TabLayout加载viewpager
        tabLayout.setupWithViewPager(vp);

    }








    @Override
    public boolean isScrollToBottom() {
        return true;  // this should depend on ViewPager current page ...
    }

    @Override
    public boolean isScrollToTop() {
        return true;
    }







}
