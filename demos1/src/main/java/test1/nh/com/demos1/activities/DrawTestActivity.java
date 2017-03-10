package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.daimajia.swipe.SwipeLayout;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.matDesign.adapter_MD.MyRVAdapter;
import test1.nh.com.demos1.activities.matDesign.adapter_MD.MyRVAdapter3;
import test1.nh.com.demos1.activities.matDesign.utils_MD.DividerItemDecoration;
import test1.nh.com.demos1.customView.DragLayout;

public class DrawTestActivity extends AppCompatActivity {


    public static void start(Context c){
        Intent i=new Intent(c,DrawTestActivity.class);
        c.startActivity(i);
    }



    DragLayout container;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_test);

        container=(DragLayout)findViewById(R.id.drag_container);

        container.setmDragView(findViewById(R.id.drag_view1));




        SwipeLayout swipeLayout =  (SwipeLayout)findViewById(R.id.sample1);

        //set show mode.
        swipeLayout.setShowMode(SwipeLayout.ShowMode.LayDown);

        //add drag edge.(If the BottomView has 'layout_gravity' attribute, this line is unnecessary)
        swipeLayout.addDrag(SwipeLayout.DragEdge.Left, findViewById(R.id.bottom_wrapper));

        swipeLayout.addSwipeListener(new SwipeLayout.SwipeListener() {
            @Override
            public void onClose(SwipeLayout layout) {
                //when the SurfaceView totally cover the BottomView.
            }

            @Override
            public void onUpdate(SwipeLayout layout, int leftOffset, int topOffset) {
                //you are swiping.
            }

            @Override
            public void onStartOpen(SwipeLayout layout) {

            }

            @Override
            public void onOpen(SwipeLayout layout) {
                //when the BottomView totally show.
            }

            @Override
            public void onStartClose(SwipeLayout layout) {

            }

            @Override
            public void onHandRelease(SwipeLayout layout, float xvel, float yvel) {
                //when user's hand released.
            }
        });


        //-------------------------------------
        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.test_rv);

        // use this setting to improve performance if you know that changes------------------
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager--------------------------------------------------------
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);



        // specify an adapter ---------------------------------------------------------------
        String[] myDataset={"RView1--MDtheme","elevation test--MDtheme1"};
        MyRVAdapter3 mAdapter = new MyRVAdapter3(myDataset);


        mRecyclerView.setAdapter(mAdapter);


    }
}
