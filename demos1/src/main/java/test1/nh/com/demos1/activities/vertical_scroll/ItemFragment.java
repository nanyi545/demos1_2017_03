package test1.nh.com.demos1.activities.vertical_scroll;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.vertical_scroll.dummy.DummyContent;
import test1.nh.com.demos1.activities.vertical_scroll.dummy.DummyContent.DummyItem;

import java.util.List;

/**
 *
 *
 */
public class ItemFragment extends Fragment {

    private static final String KEY1 = "xxx";


    private int count = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ItemFragment() {
    }

    public static ItemFragment newInstance(int count) {
        ItemFragment fragment = new ItemFragment();
        Bundle args = new Bundle();
        args.putInt(KEY1, count);
        fragment.setArguments(args);
        return fragment;
    }

    TextView testIv;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            count = getArguments().getInt(KEY1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item_list, container, false);
        testIv= (TextView) view.findViewById(R.id.txt);
        testIv.setText("--------"+count);
        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

}
