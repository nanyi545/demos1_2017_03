package com.nanyi545.www.materialdemo.test_frags.simple_test;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanyi545.www.materialdemo.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BlankFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BlankFragment extends Fragment {

    private static final String KEY1 = "param1";
    private static final String KEY2 = "param2";

    private String name;
    private int resourceId;


    public BlankFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @return A new instance of fragment BlankFragment.
     */
    public static BlankFragment newInstance(String param1, int resId) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        args.putString(KEY1, param1);
        args.putInt(KEY2, resId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(KEY1);
            resourceId = getArguments().getInt(KEY2);
            Log.i("aaa","BlankFragment--oncreate--name:"+name);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v=inflater.inflate(R.layout.fragment_blank, container, false);
        ImageView iv1= (ImageView) v.findViewById(R.id.iv1);
        iv1.setImageResource(resourceId);
        TextView tv1= (TextView) v.findViewById(R.id.fragment_name);
        tv1.setText(name);
        Log.i("aaa","BlankFragment--onCreateView--name:"+name);
        return v;
    }


    @Override
    public void onDestroy() {
        Log.i("aaa","BlankFragment--onDestroy--name:"+name);
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.i("aaa","BlankFragment--onDestroyView--name:"+name);
        super.onDestroyView();
    }

    @Override
    public void onDetach() {
        Log.i("aaa","BlankFragment--onDetach--name:"+name);
        super.onDetach();
    }


}
