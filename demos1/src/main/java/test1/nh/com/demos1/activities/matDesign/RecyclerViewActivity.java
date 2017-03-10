package test1.nh.com.demos1.activities.matDesign;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.matDesign.adapter_MD.MyRVAdapter;
import test1.nh.com.demos1.activities.matDesign.utils_MD.DividerItemDecoration;

public class RecyclerViewActivity extends Activity {

    private Context mContext;
    private RecyclerView mRecyclerView;
    private MyRVAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes------------------
        // in content do not change the layout size of the RecyclerView
//        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager--------------------------------------------------------
        mLayoutManager = new LinearLayoutManager(this);
//        mLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false);
        mRecyclerView.setLayoutManager(mLayoutManager);

//        //添加分割线--in fragments
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(
//                getActivity(), DividerItemDecoration.HORIZONTAL_LIST)); //
        // add item dividor------------------------------------------------------------------
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL_LIST));  //vertical list
//        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,
//                DividerItemDecoration.HORIZONTAL_LIST));  //horizontal list


        // specify an adapter ---------------------------------------------------------------
        String[] myDataset={"RView1--MDtheme","elevation test--MDtheme1","go go go","5","4","3","2","1","-1","-2","-3","-4"};
        mAdapter = new MyRVAdapter(myDataset);

        mAdapter.setOnItemClickLitener1(new MyRVAdapter.OnItemClickLitener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(RecyclerViewActivity.this, position + " click",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(RecyclerViewActivity.this, position + " long click",
                        Toast.LENGTH_SHORT).show();
            }
        });


        mRecyclerView.setAdapter(mAdapter);






    }


}
