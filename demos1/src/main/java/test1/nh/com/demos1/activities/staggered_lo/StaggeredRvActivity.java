package test1.nh.com.demos1.activities.staggered_lo;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;

import java.util.List;

import test1.nh.com.demos1.R;

public class StaggeredRvActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,StaggeredRvActivity.class);
        c.startActivity(i);
    }


    private StaggeredGridLayoutManager  manager;
    private RecyclerView rv;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_rv);
        rv= (RecyclerView) findViewById(R.id.recycler_view);
        manager = new StaggeredGridLayoutManager(2, 1);
        rv.setLayoutManager(manager);

        List<RvItem> list = RvItem.getTestData();
        StaggerAdapter adapter = new StaggerAdapter(this, list);
        rv.setAdapter(adapter);
    }






}
