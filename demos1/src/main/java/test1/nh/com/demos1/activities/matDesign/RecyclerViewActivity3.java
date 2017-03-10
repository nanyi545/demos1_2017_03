package test1.nh.com.demos1.activities.matDesign;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.matDesign.adapter_MD.RecyclerListAdapter;
import test1.nh.com.demos1.activities.matDesign.utils_MD.OnStartDragListener;
import test1.nh.com.demos1.activities.matDesign.utils_MD.SimpleItemTouchHelperCallback;

public class RecyclerViewActivity3 extends AppCompatActivity {

    private ItemTouchHelper mItemTouchHelper;
    private OnStartDragListener myOnStartDragListener=new OnStartDragListener(){
        @Override
        public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
            mItemTouchHelper.startDrag(viewHolder);
        }
    };

    private RecyclerView mRecyclerView;
    private RecyclerListAdapter adapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_view3);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);

        adapter = new RecyclerListAdapter(this,myOnStartDragListener);

        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter);
        mItemTouchHelper = new ItemTouchHelper(callback);
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

}
