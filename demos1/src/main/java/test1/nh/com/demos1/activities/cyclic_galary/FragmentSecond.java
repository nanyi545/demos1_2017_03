package test1.nh.com.demos1.activities.cyclic_galary;


import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import test1.nh.com.demos1.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentSecond extends Fragment {


    public FragmentSecond() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_fragment_second, container, false);
    }

}
