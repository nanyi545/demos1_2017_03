package test1.nh.com.themeapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.Toast;

import test1.nh.com.themeapp.R;
import test1.nh.com.themeapp.utils.MyRVAdapter;
import test1.nh.com.themeapp.utils.MyRecyclerScroll;

public class FABactivity extends AppCompatActivity {



    private Context mContext;

    //  com.getbase.floatingactionbutton.FloatingActionButton
    private com.getbase.floatingactionbutton.FloatingActionButton fab;
    private FloatingActionButton fab_support;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private MyRVAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fabactivity);

        mContext=this;

        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.simple_grow3);
        fab = (com.getbase.floatingactionbutton.FloatingActionButton) findViewById(R.id.fab);

//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // Your FAB click action here...
//                Toast.makeText(getBaseContext(), "FAB Clicked", Toast.LENGTH_SHORT).show();
//            }
//        });
//        fab.startAnimation(animation1);  // simple grow


        // TODO why scale animation does not work for FloatingActionButton？？？
        Animation animation2 = AnimationUtils.loadAnimation(this, R.anim.simple_grow3);
        fab_support=(FloatingActionButton) findViewById(R.id.fab_support);
        fab_support.startAnimation(animation2);  // simple grow


        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        // use this setting to improve performance if you know that changes------------------
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);
        // use a linear layout manager--------------------------------------------------------
        mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter ---------------------------------------------------------------
        String[] myDataset={"item1","item2","item3","item4","item5","item6","item7-3","item8","1","-1","-2","-3","-4"};
        mAdapter = new MyRVAdapter(myDataset);

        mAdapter.setOnItemClickLitener1(new MyRVAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(FABactivity.this, position + " click",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(FABactivity.this, position + " long click",
                        Toast.LENGTH_SHORT).show();
            }
        });


        mRecyclerView.setAdapter(mAdapter);

        // set scroll listener
        mRecyclerView.addOnScrollListener(new MyRecyclerScroll() {
            @Override
            public void show() {
                fab.animate().translationY(0).setInterpolator(new DecelerateInterpolator(2)).start();
            }

            @Override
            public void hide() {
                DisplayMetrics dm = new DisplayMetrics();
                getWindowManager().getDefaultDisplay().getMetrics(dm);

                int[] loc = new int[2];
                fab.getLocationOnScreen(loc); //
                int distance = dm.heightPixels - loc[1]; // screen height - view top
                fab.animate().translationY(distance).setInterpolator(new AccelerateInterpolator(2)).start();
            }
        });





    }
}
