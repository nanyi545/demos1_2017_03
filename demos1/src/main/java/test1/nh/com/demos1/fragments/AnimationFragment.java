package test1.nh.com.demos1.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import java.util.ArrayList;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.DrawerActivity;
import test1.nh.com.demos1.utils.ShapeHolder;

/**
 * Created by Administrator on 15-9-30.
 */
public class AnimationFragment extends Fragment {


    private Context mcontext;


    /**
     * a custom view with itself a property animation
     * it contains four jumping balls
     */
    @TargetApi(11)
    public class MyAnimationView extends View implements ValueAnimator.AnimatorUpdateListener {

        public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();
        AnimatorSet animation = null;
        private float mDensity;

        public MyAnimationView(Context context) {
            super(context);

            mDensity = getContext().getResources().getDisplayMetrics().density;

            ShapeHolder ball0 = addBall(50f, 25f);
            ShapeHolder ball1 = addBall(150f, 25f);
            ShapeHolder ball2 = addBall(250f, 25f);
            ShapeHolder ball3 = addBall(350f, 25f);
        }

        private void createAnimation() {
            if (animation == null) {
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(balls.get(0), "y",
                        0f, getHeight() - balls.get(0).getHeight()).setDuration(1500);
                ObjectAnimator anim2 = anim1.clone();
                anim2.setTarget(balls.get(1));
                anim1.addUpdateListener(this);

                ShapeHolder ball2 = balls.get(2);
                ObjectAnimator animDown = ObjectAnimator.ofFloat(ball2, "y",
                        0f, getHeight() - ball2.getHeight()).setDuration(500);
                animDown.setInterpolator(new AccelerateInterpolator());
                ObjectAnimator animUp = ObjectAnimator.ofFloat(ball2, "y",
                        getHeight() - ball2.getHeight(), 0f).setDuration(500);
                animUp.setInterpolator(new DecelerateInterpolator());
                AnimatorSet s1 = new AnimatorSet();
                s1.playSequentially(animDown, animUp);
                animDown.addUpdateListener(this);
                animUp.addUpdateListener(this);
                AnimatorSet s2 = (AnimatorSet) s1.clone();
                s2.setTarget(balls.get(3));

                animation = new AnimatorSet();
                animation.playTogether(anim1, anim2, s1);
                animation.playSequentially(s1, s2);
            }
        }

        private ShapeHolder addBall(float x, float y) {
            OvalShape circle = new OvalShape();
            circle.resize(50f * mDensity, 50f * mDensity);
            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            shapeHolder.setX(x - 25f);
            shapeHolder.setY(y - 25f);
            int red = (int)(100 + Math.random() * 155);
            int green = (int)(100 + Math.random() * 155);
            int blue = (int)(100 + Math.random() * 155);
            int color = 0xff000000 | red << 16 | green << 8 | blue;
            Paint paint = drawable.getPaint(); //new Paint(Paint.ANTI_ALIAS_FLAG);
            int darkColor = 0xff000000 | red/4 << 16 | green/4 << 8 | blue/4;
            RadialGradient gradient = new RadialGradient(37.5f, 12.5f,
                    50f, color, darkColor, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            shapeHolder.setPaint(paint);
            balls.add(shapeHolder);
            return shapeHolder;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            for (int i = 0; i < balls.size(); ++i) {
                ShapeHolder shapeHolder = balls.get(i);
                canvas.save();
                canvas.translate(shapeHolder.getX(), shapeHolder.getY());
                shapeHolder.getShape().draw(canvas);
                canvas.restore();
            }
        }

        public void startAnimation() {
            createAnimation();
            animation.start();
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
//            Log.i("AAA", ( animation.getAnimatedValue()).toString());
        }

    }



    /**
     * a custom view with itself a property animation
     * it contains 1 jumping ball
     */
    @TargetApi(11)
    public class MyAnimationView2 extends View implements ValueAnimator.AnimatorUpdateListener {

        public final ArrayList<ShapeHolder> balls = new ArrayList<ShapeHolder>();
        AnimatorSet animation = null;
        private float mDensity;

        public MyAnimationView2(Context context) {
            super(context);

            mDensity = getContext().getResources().getDisplayMetrics().density;

            ShapeHolder ball0 = addBall(50f, 25f);
        }

        private void createAnimation() {
            if (animation == null) {
                ObjectAnimator anim1 = ObjectAnimator.ofFloat(balls.get(0), "y",
                        0f, getHeight() - balls.get(0).getHeight()).setDuration(1500);
                anim1.addUpdateListener(this);

                animation = new AnimatorSet();
                animation.playTogether(anim1);
            }
        }

        private ShapeHolder addBall(float x, float y) {
            OvalShape circle = new OvalShape();
            circle.resize(50f * mDensity, 50f * mDensity);
            ShapeDrawable drawable = new ShapeDrawable(circle);
            ShapeHolder shapeHolder = new ShapeHolder(drawable);
            shapeHolder.setX(x - 25f);
            shapeHolder.setY(y - 25f);
            int red = (int)(100 + Math.random() * 155);
            int green = (int)(100 + Math.random() * 155);
            int blue = (int)(100 + Math.random() * 155);
            int color = 0xff000000 | red << 16 | green << 8 | blue;
            Paint paint = drawable.getPaint(); //new Paint(Paint.ANTI_ALIAS_FLAG);
            int darkColor = 0xff000000 | red/4 << 16 | green/4 << 8 | blue/4;
            RadialGradient gradient = new RadialGradient(37.5f, 12.5f,
                    50f, color, darkColor, Shader.TileMode.CLAMP);
            paint.setShader(gradient);
            shapeHolder.setPaint(paint);
            balls.add(shapeHolder);
            return shapeHolder;
        }

        @Override
        protected void onDraw(Canvas canvas) {
            for (int i = 0; i < balls.size(); ++i) {
                ShapeHolder shapeHolder = balls.get(i);
                canvas.save();
                canvas.translate(shapeHolder.getX(), shapeHolder.getY());
                shapeHolder.getShape().draw(canvas);
                canvas.restore();
            }
        }

        public void startAnimation() {
            createAnimation();
            animation.start();
        }

        // callback method on every animation frame-----
        public void onAnimationUpdate(ValueAnimator animation) {
            invalidate();
            // prints the current "y" value
            // getAnimatedValue() method access PropertyValuesHolder (property/value sets being animated)
            // field in the ValueAnimator class
            Log.i("AAA", (animation.getAnimatedValue()).toString());
        }
    }





    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    //empty constructor
    public AnimationFragment(){
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static AnimationFragment newInstance(int sectionNumber) {
        AnimationFragment fragment = new AnimationFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mcontext=getActivity();

        View rootView = inflater.inflate(R.layout.fragment_animation_drawer, container, false);

        LinearLayout lcontainer = (LinearLayout)rootView.findViewById(R.id.container);
        final MyAnimationView animView = new MyAnimationView(mcontext);
        lcontainer.addView(animView);

        LinearLayout lcontainer2 = (LinearLayout)rootView.findViewById(R.id.container2);
        final MyAnimationView2 animView2 = new MyAnimationView2(mcontext);
        lcontainer2.addView(animView2);


        Button starter = (Button) rootView.findViewById(R.id.startButton);
        starter.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                animView.startAnimation();
                animView2.startAnimation();
            }
        });


        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //设置activity label------------

        try {
            ((DrawerActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (Exception e) {  // in case this fragment is called not by  DrawerActivity
            e.printStackTrace();
//            Log.i("AAA","ANIMATIONfragment called not by DrawerActivity");
        }


    }



}
