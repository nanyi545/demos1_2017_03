package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.matDesign.adapter_MD.MyRVAdapter3;
import test1.nh.com.demos1.activities.matDesign.adapter_MD.MyRVAdapter4;

public class ViewPagerTestActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,ViewPagerTestActivity.class);
        c.startActivity(i);
    }


    ViewPager vp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_pager_test);
        vp= (ViewPager) findViewById(R.id.test_vp);

        LayoutInflater inflater = getLayoutInflater();
        ArrayList<View> pageViews = new ArrayList<View>();
        pageViews.add(inflater.inflate(R.layout.viewpager_page4, null));
        pageViews.add(inflater.inflate(R.layout.viewpager_page5, null));

        vp.setAdapter(new GuidePageAdapter(pageViews));



        //---------
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.test_rv);

        // use this setting to improve performance if you know that changes------------------
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager--------------------------------------------------------
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        // specify an adapter ---------------------------------------------------------------
        String[] myDataset={"RView1--MDtheme","elevation test--MDtheme1"};
        MyRVAdapter4 mAdapter = new MyRVAdapter4(this,myDataset);
        mAdapter.setOnItemClickLitener1(new MyRVAdapter4.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(ViewPagerTestActivity.this,""+position,Toast.LENGTH_SHORT).show();
            }
        });

        mRecyclerView.setAdapter(mAdapter);







    }






    public static class GuidePageAdapter extends PagerAdapter {

        private ArrayList<View> pageViews;

        public GuidePageAdapter(ArrayList<View> pageViews){
            this.pageViews=pageViews;

        }

        @Override
        public float getPageWidth(int position) {
            if (position==0) {return 1f;}
            else {return 0.3f;}
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
