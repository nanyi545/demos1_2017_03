package com.nanyi545.www.materialdemo.testPullToRefresh;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.util.Log;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Created by Administrator on 2017/2/28.
 *
 * this Class supports pull down  and up  ....
 *
 */
public class CoordinatorPullable extends CoordinatorLayout {


    public CoordinatorPullable(Context context) {
        super(context);
    }

    public CoordinatorPullable(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CoordinatorPullable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }




    private RevealContent revealContent;
    protected void setRevelContent(RevealContent revealContent){
        this.revealContent=revealContent;
    }
    protected RevealContent getRevealContent() {
        return revealContent;
    }
    public void setRevealContent(Class<? extends RevealContent> cls){
        try {
            Log.i("nnn","class name"+cls.getName());
            Constructor c=cls.getConstructor(Context.class);
            cls.cast(c.newInstance(getContext())).init(getContext(),this);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    private int revealHeight =100;
    public void setRevealHeight(int revealHeight) {
        this.revealHeight = revealHeight;
    }

    public int getRevealHeight() {
        return revealHeight;
    }
    /**-----------end of reveal content-----------**/




}
