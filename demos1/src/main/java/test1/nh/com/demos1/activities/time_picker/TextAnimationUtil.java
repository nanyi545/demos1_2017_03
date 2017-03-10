package test1.nh.com.demos1.activities.time_picker;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

/**
 * Created by Administrator on 2016/7/20.
 */
public class TextAnimationUtil {



    public static void rotateTo(final float angle, final TextView tv){
        float startAngle=tv.getRotationX();
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(startAngle, angle);
//        valueAnimator.setStartDelay(100);
        valueAnimator.setDuration(400);
        valueAnimator.setInterpolator(new DecelerateInterpolator(1f));
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                tv.setRotationX((Float)valueAnimator.getAnimatedValue());
            }
        });

        valueAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                tv.setRotationX(angle);
            }
        });


        valueAnimator.start();

    }




}
