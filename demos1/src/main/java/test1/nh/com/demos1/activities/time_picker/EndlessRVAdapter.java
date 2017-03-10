package test1.nh.com.demos1.activities.time_picker;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import java.util.LinkedList;

import test1.nh.com.demos1.R;


/**
 * Created by Administrator on 15-10-12.
 */
public class EndlessRVAdapter extends RecyclerView.Adapter<EndlessRVAdapter.ViewHolder>  {




    private int centerPosition=2;

    public void setCenterPosition(int centerPosition) {
        this.centerPosition = centerPosition;
        notifyDataSetChanged();
    }

    public interface OnItemClickLitener
    {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
    }

    public OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener1(OnItemClickLitener mOnItemClickLitener)
    {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    private LinkedList<String> mDataset;



    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;
        public ViewHolder(View v) {
            super(v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public EndlessRVAdapter(LinkedList<String> myDataset) {
        mDataset = myDataset;
    }
    public EndlessRVAdapter(LinkedList<String> myDataset,Activity base_) {
        mDataset = myDataset;
        this.base=base_;
    }


    // Create new views (invoked by the layout manager)
    @Override
    public EndlessRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.endless_text_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        TextView tv_item=(TextView)v.findViewById(R.id.id_tv);
        vh.mTextView=tv_item;
//        vh.mTextView.
        return vh;
    }

    Activity base;

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final int modPosition= position ;
        holder.mTextView.setText(mDataset.get(modPosition));

        if (position==centerPosition) {
            holder.mTextView.setTextColor(base.getResources().getColor(R.color.Red600));
        } else {
            holder.mTextView.setTextColor(base.getResources().getColor(R.color.color_black));
        }

        int different=(position-centerPosition);
        switch (different){
            case -2:
                holder.mTextView.setRotationX(60);
                holder.mTextView.setTextSize(15);
//                TextAnimationUtil.rotateTo(60f,holder.mTextView);
                break;
            case -1:
                holder.mTextView.setRotationX(40);
                holder.mTextView.setTextSize(17);
                break;
            case 0:
                holder.mTextView.setRotationX(0);
                holder.mTextView.setTextSize(19);
                break;
            case 1:
                holder.mTextView.setRotationX(-40);
                holder.mTextView.setTextSize(17);
                break;
            case 2:
                holder.mTextView.setRotationX(-60);
                holder.mTextView.setTextSize(15);
                break;
            default :
                break;
        }

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    mOnItemClickLitener.onItemClick(holder.itemView, modPosition);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    mOnItemClickLitener.onItemLongClick(holder.itemView, modPosition);
                    return false;
                }
            });
        }
    }

//    Rotate3dAnimation.animationTextView(ByHourOrderActivity.this,R.id.test_tv1,50,40);
//    Rotate3dAnimation.animationTextView(ByHourOrderActivity.this,R.id.test_tv2,0,20);
//    Rotate3dAnimation.animationTextView2(ByHourOrderActivity.this,R.id.test_tv4,0,-20);
//    Rotate3dAnimation.animationTextView2(ByHourOrderActivity.this,R.id.test_tv5,50,-40);


    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}
