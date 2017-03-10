package test1.nh.com.demos1.activities.bessel;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import test1.nh.com.demos1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class ImgFragment extends Fragment {


    public ImgFragment() {}

    public void setResId(int resId){
        this.resId=resId;
    }

    ImageView iv;

    int resId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment



        View v=inflater.inflate(R.layout.fragment_img, container, false);
        iv = (ImageView) v.findViewById(R.id.fragment_img);
        if (resId!=0) {
            iv.setImageResource(resId);
        } else{
            iv.setImageDrawable(new ColorDrawable(Color.rgb((int)(Math.random()*250),(int)(Math.random()*250),(int)(Math.random()*250) )));
        }

        return v;
    }




}
