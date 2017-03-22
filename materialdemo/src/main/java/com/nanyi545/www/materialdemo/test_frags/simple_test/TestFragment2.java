package com.nanyi545.www.materialdemo.test_frags.simple_test;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.nanyi545.www.materialdemo.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link TestFragment2.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link TestFragment2#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TestFragment2 extends Fragment {


    private static final String KEY1 = "param1";
    private static final String KEY2 = "param2";

    private String name;
    private int backgroundColor;

    private OnFragmentInteractionListener mListener;

    public TestFragment2() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TestFragment2.
     */
    public static TestFragment2 newInstance(String name, int backGroundColorId) {
        TestFragment2 fragment = new TestFragment2();
        Bundle args = new Bundle();
        args.putString(KEY1, name);
        args.putInt(KEY2, backGroundColorId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(KEY1);
            backgroundColor = getArguments().getInt(KEY2);
            Log.i("aaa","TestFragment--onCreate--name:"+name);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_test_fragment2, container, false);

        TextView tv= (TextView) view.findViewById(R.id.fragment_name);
        tv.setText(name);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonPressed(name);
            }
        });

        FrameLayout root= (FrameLayout) view;
        root.setBackgroundResource(backgroundColor);
        Log.i("aaa","TestFragment--onCreateView--name:"+name);
        return view;
    }


    @Override
    public void onDestroy() {
        Log.i("aaa","TestFragment--onDestroy--name:"+name);
        super.onDestroy();
    }


    @Override
    public void onDestroyView() {
        Log.i("aaa","TestFragment--onDestroyView--name:"+name);
        super.onDestroyView();
    }





    public void onButtonPressed(String interaction) {
        if (mListener != null) {
            mListener.onFragmentInteraction(interaction);
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }


    @Override
    public void onDetach() {
        Log.i("aaa","TestFragment--onDetach--name:"+name);
        super.onDetach();
        mListener = null;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(String interaction);
    }
}
