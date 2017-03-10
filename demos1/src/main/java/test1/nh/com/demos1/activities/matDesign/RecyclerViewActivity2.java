package test1.nh.com.demos1.activities.matDesign;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import test1.nh.com.demos1.activities.matDesign.adapter_MD.MyRVAdapter;
import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.matDesign.utils_MD.DividerGridItemDecoration;

public class RecyclerViewActivity2 extends Activity {

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view2);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view2);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new GridLayoutManager(this,3);
        mRecyclerView.setLayoutManager(mLayoutManager);
//        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4,StaggeredGridLayoutManager.HORIZONTAL));

        // add item dividor
        mRecyclerView.addItemDecoration(new DividerGridItemDecoration(this));

        // specify an adapter (see also next example)
        String[] myDataset={"RView1--MDtheme","elevation test--MDtheme1","go go go","5","4","3","2","1","-1","-2","-3","-4"};
        mAdapter = new MyRVAdapter(myDataset);
        mRecyclerView.setAdapter(mAdapter);


    }


}
