package test1.nh.com.demos1.activities.tab_layout_viewpager;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.matDesign.adapter_MD.MyRVAdapter3;

/**
 * Created by Administrator on 2016/11/16.
 */
public class PageFragment extends Fragment {

    public static final String ARG_PAGE = "ARG_PAGE";

    private int mPage;

    public static PageFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        PageFragment fragment = new PageFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(ARG_PAGE);
        initHideAnimation();
    }

    ValueAnimator hideBottomPanel,show;
    private void initHideAnimation() {
        hideBottomPanel = ValueAnimator.ofInt(400, 0);
        hideBottomPanel.setStartDelay(500);
        hideBottomPanel.setInterpolator(new DecelerateInterpolator());
        hideBottomPanel.setDuration(500);
        hideBottomPanel.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) rl1.getLayoutParams();
                params1.height=(Integer) valueAnimator.getAnimatedValue();
                rl1.setLayoutParams(params1);
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl2.getLayoutParams();
//                params.setMargins(0, 0, 0, (Integer) valueAnimator.getAnimatedValue() - (int) (40 * dm.density));
//                rl2.setLayoutParams(params);
            }
        });

        show = ValueAnimator.ofInt(0, 400);
        show.setStartDelay(500);
        show.setInterpolator(new DecelerateInterpolator());
        show.setDuration(500);
        show.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) rl1.getLayoutParams();
                params1.height=(Integer) valueAnimator.getAnimatedValue();
                rl1.setLayoutParams(params1);
//                RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rl2.getLayoutParams();
//                params.setMargins(0, 0, 0, (Integer) valueAnimator.getAnimatedValue() - (int) (40 * dm.density));
//                rl2.setLayoutParams(params);
            }
        });

    }


    RelativeLayout rl1,rl2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_page, container, false);
        TextView textView = (TextView) view.findViewById(R.id.test_tv);
        textView.setText("Fragment #" + mPage);
        rl1= (RelativeLayout) view.findViewById(R.id.rl1);
        rl1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BBB","RL1 CLICKED");
                hideBottomPanel.start();
            }
        });
        rl2= (RelativeLayout) view.findViewById(R.id.rl2);
        rl2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("BBB","RL2 CLICKED");
                show.start();
            }
        });

        RecyclerView mRecyclerView = (RecyclerView) view.findViewById(R.id.test_rv);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);

        String[] myDataset={"RView1--MDtheme","elevation test--MDtheme1","12123","13123","-----"};
        MyRVAdapter3 mAdapter = new MyRVAdapter3(myDataset);
        mRecyclerView.setAdapter(mAdapter);

        return view;
    }



}
