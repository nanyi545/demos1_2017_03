package test1.nh.com.demos1.activities.matDesign.adapter_MD;

import android.app.Activity;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.ViewPagerTestActivity;

/**
 * Created by Administrator on 15-10-12.
 */
public class MyRVAdapter4 extends RecyclerView.Adapter<MyRVAdapter4.ViewHolder>  {



    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
    }

    public OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener1(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    private String[] mDataset;
    private Activity context;

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRVAdapter4(Activity context,String[] myDataset) {
        mDataset = myDataset;
        this.context=context;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRVAdapter4.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_text_view4, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);

        ViewPager vp= (ViewPager) v.findViewById(R.id.rv_pager);

        LayoutInflater inflater = context.getLayoutInflater();
        ArrayList<View> pageViews = new ArrayList<View>();
        View subview1=inflater.inflate(R.layout.viewpager_page4, null);
        pageViews.add(subview1);
        View subview2=inflater.inflate(R.layout.viewpager_page5, null);
        pageViews.add(subview2);

        vp.setAdapter(new ViewPagerTestActivity.GuidePageAdapter(pageViews));
        TextView tv_item=(TextView)subview2.findViewById(R.id.rv_id_test);
        vh.mTextView=tv_item;
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position]);
//        Log.i("AAA","current Position"+position);
        //rv中载入（显示）当前item的时候执行此函数--可以在此设置item的特性


        holder.mTextView.setClickable(true);
//        holder.mTextView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(context,""+position,Toast.LENGTH_SHORT).show();
//            }
//        });

        if (mOnItemClickLitener != null)
        {
            holder.mTextView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.mTextView, pos);
                }
            });
        }

    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.length;
    }

}
