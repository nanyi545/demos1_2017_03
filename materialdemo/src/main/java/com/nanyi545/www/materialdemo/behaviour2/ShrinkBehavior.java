package com.nanyi545.www.materialdemo.behaviour2;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.List;

/**
 * Created by Administrator on 2017/2/27.
 */
public class ShrinkBehavior  extends CoordinatorLayout.Behavior<FloatingActionButton> {


    //   https://medium.com/google-developers/intercepting-everything-with-coordinatorlayout-behaviors-8c6adc140c26#.gtjhw5n2w
    //  --> Behavior is actually stored in the LayoutParams of each View 
    //  -->
//    FancyBehavior fancyBehavior = new FancyBehavior();
//    CoordinatorLayout.LayoutParams params =
//            (CoordinatorLayout.LayoutParams) yourView.getLayoutParams();
//    params.setBehavior(fancyBehavior);


    public ShrinkBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        return dependency instanceof Snackbar.SnackbarLayout;
    }

    @Override
    public boolean onDependentViewChanged(CoordinatorLayout parent, FloatingActionButton child, View dependency) {
        float translationY = getFabTranslationYForSnackbar(parent, child);
        float percentComplete = -translationY / dependency.getHeight();
        float scaleFactor = 1 - percentComplete;

        child.setScaleX(scaleFactor);
        child.setScaleY(scaleFactor);
        return false;
    }


    private float getFabTranslationYForSnackbar(CoordinatorLayout parent,
                                                FloatingActionButton fab) {
        float minOffset = 0;
        final List<View> dependencies = parent.getDependencies(fab); 
        for (int i = 0, z = dependencies.size(); i < z; i++) {
            final View view = dependencies.get(i);
            if (view instanceof Snackbar.SnackbarLayout && parent.doViewsOverlap(fab, view)) {
                minOffset = Math.min(minOffset,
                        ViewCompat.getTranslationY(view) - view.getHeight());
                Log.i("ccc","ViewCompat.getTranslationY(Snackbar.SnackbarLayout):"+ViewCompat.getTranslationY(view)+"    Snackbar.SnackbarLayout height:"+view.getHeight());
            }
        }

        return minOffset;
    }



}
