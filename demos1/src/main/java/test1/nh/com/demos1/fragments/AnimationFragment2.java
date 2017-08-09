package test1.nh.com.demos1.fragments;

import android.animation.AnimatorSet;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.DrawerActivity;

/**
 * Created by Administrator on 15-9-30.
 */
public class AnimationFragment2 extends Fragment {

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    //empty constructor
    public AnimationFragment2(){
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AnimationFragment2 newInstance(int sectionNumber) {
        AnimationFragment2 fragment = new AnimationFragment2();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @TargetApi(11)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get section id from intent
        // getArg--return the Bundle
        int section_number=getArguments().getInt(ARG_SECTION_NUMBER);

        View rootView = inflater.inflate(R.layout.fragment_anim2_drawer, container, false);

        // scale animation----View animation
        final Animation anim_grow = AnimationUtils.loadAnimation(getActivity(), R.anim.simple_grow);
        final ImageView iv1= (ImageView) rootView.findViewById(R.id.iv_grow);
        Button iv1_grow= (Button) rootView.findViewById(R.id.startButton);
        iv1_grow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv1.startAnimation(anim_grow);  // simple grow
            }
        });



        // translate animation----View animation+ animator set
        final ImageView iv2= (ImageView) rootView.findViewById(R.id.iv_move);
        Button iv2_move= (Button) rootView.findViewById(R.id.startButton2);

        final TranslateAnimation anim_move = new TranslateAnimation(0, 100, 0, 0);
//        animOpen.setRepeatCount(5);
        anim_move.setFillAfter(true);  // false --> go back to original position , true --> stay at final position
        anim_move.setDuration(1000);
//        animOpen.setAnimationListener(animationOpenListener);
        iv2_move.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv2.startAnimation(anim_move);  // move
            }
        });



        // rotate animation----View animation
        final ImageView iv4= (ImageView) rootView.findViewById(R.id.iv_rotate);
        Button iv4_rotate= (Button) rootView.findViewById(R.id.startButton4);

        iv4_rotate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv4.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.rotate_indefinitely));
            }
        });




        //---------property animation-----------
        final ImageView iv3= (ImageView) rootView.findViewById(R.id.iv_color);
        final LinearLayout container3= (LinearLayout) rootView.findViewById(R.id.container3);
        final TextView tv3= (TextView) rootView.findViewById(R.id.tv_color);

        Button iv3_color= (Button) rootView.findViewById(R.id.startButton3);
        final ObjectAnimator animator = ObjectAnimator.ofInt(tv3, "textColor",getResources().getColor(R.color.Red200), getResources().getColor(R.color.Red600));
        final ObjectAnimator animator1 = ObjectAnimator.ofInt(iv3, "backgroundColor",getResources().getColor(R.color.Red200), getResources().getColor(R.color.Red600));

        animator.setDuration(1000L);
        animator.setEvaluator(new ArgbEvaluator());
        animator.setInterpolator(new DecelerateInterpolator(2));

        animator1.setDuration(1000L);
        animator1.setEvaluator(new ArgbEvaluator());
        animator1.setInterpolator(new DecelerateInterpolator(2));

        iv3_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                animator.start();
//                animator1.start();
                AnimatorSet animation = new AnimatorSet();
//                animation.playTogether(animator, animator1);
                animation.playSequentially(animator, animator1);
                animation.start();
            }
        });
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //如果attach到drawerActivity上 设置activity label
        try {    //  called by DrawerActivity
            ((DrawerActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (Exception e) {
            e.printStackTrace();
//            Log.i("AAA", "Fragment called not by DrawerActivity");
        }

    }



}
