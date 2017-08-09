package test1.nh.com.demos1.activities.pull_load;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test1.nh.com.demos1.R;

public class TestPullLoadActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,TestPullLoadActivity.class);
        c.startActivity(i);
    }
    List<String> items;

    LoadList.LoadViewHolder topViewHolder=new TopViewHolder();
    LoadList.LoadViewHolder bottomVH=new TopViewHolder(){
        @Override
        int getLayoutId() {
            return R.layout.pullload_bottom;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_pull_load);
        LoadList list= (LoadList) findViewById(R.id.load_list);


        list.setLoadViewHolders(topViewHolder,bottomVH);

        list.getRecyclerView().setLayoutManager(new LinearLayoutManager(this));
        items=new ArrayList<>();
//        items.add("hehe1");items.add("hehe2");items.add("hehe3");items.add("hehe4");items.add("hehe5");
//        items.add("haha6");items.add("haha7");items.add("haha8");items.add("haha9");items.add("haha10");
        final MyRVAdapter adapter=new MyRVAdapter(items);
        list.getRecyclerView().setAdapter(adapter);

//        list.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                items.add("hehe1");items.add("hehe2");items.add("hehe3");items.add("hehe4");items.add("hehe5");
//                items.add("haha6");items.add("haha7");items.add("haha8");items.add("haha9");items.add("haha10");
//                adapter.notifyDataSetChanged();
//            }
//        },4000);

    }



    private static class TopViewHolder extends LoadList.LoadViewHolder{

        protected TopViewHolder() {
            super(LoadList.LoadViewHolder.LOAD_FREE);
        }

        TextView state,progress;
        ProgressBar animator;

        @Override
        int getLayoutId() {
            return R.layout.pullload_top;
        }

        @Override
        void initViewHolder(View holderView) {
            animator= (ProgressBar) holderView.findViewById(R.id.id_progressBar_top);
            state = (TextView) holderView.findViewById(R.id.top_load_state);
            progress = (TextView) holderView.findViewById(R.id.top_load_progress);
        }

        @Override
        void onDragToThreshold() {
            state.setVisibility(View.VISIBLE);
            state.setText("下拉刷新");
            animator.setVisibility(View.INVISIBLE);
        }

        @Override
        void onDragBeyondThreshold() {
            state.setVisibility(View.VISIBLE);
            state.setText("松开刷新");
            animator.setVisibility(View.INVISIBLE);
        }

        @Override
        void onRelease() {
            animator.setVisibility(View.VISIBLE);
            state.setText("加载中");
        }

        @Override
        void updateProgress(float progress) {
            this.progress.setText("current:"+progress);
        }
    }



}
