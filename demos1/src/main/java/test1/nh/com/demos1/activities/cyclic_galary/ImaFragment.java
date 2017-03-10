package test1.nh.com.demos1.activities.cyclic_galary;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 2016/11/7.
 */
public class ImaFragment extends android.support.v4.app.Fragment {

    public ImaFragment() {
    }


    public ImaFragment(int resourceId) {
        this.resourceId = resourceId;
    }

    ImageView iv;

    int resourceId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.img_fragment_lo, container, false);
        iv= (ImageView) v.findViewById(R.id.frag_img);
        iv.setImageResource(resourceId);
        return v ;
    }




}
