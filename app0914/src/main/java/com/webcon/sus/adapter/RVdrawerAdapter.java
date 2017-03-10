package com.webcon.sus.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.webcon.sus.demo.R;

/**
 * Created by Administrator on 15-10-12.
 */
public class RVdrawerAdapter extends RecyclerView.Adapter<RVdrawerAdapter.ViewHolder>  {


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


    private String[] mDataset;
    private int[] m_icons;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView mTextView;
        public ImageView mIVicon;

        public ViewHolder(View v) {
            super(v);
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("AAA", "当前点击的位置：" + getLayoutPosition());
//                }
//            });
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public RVdrawerAdapter(String[] myDataset,int[] drawable_ids) {
        mDataset = myDataset;
        m_icons=drawable_ids;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public RVdrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.rv_drawer_item, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        TextView tv_item=(TextView)v.findViewById(R.id.tv_text);
        vh.mTextView=tv_item;
        ImageView iv_item=(ImageView)v.findViewById(R.id.iv_icon);
        vh.mIVicon=iv_item;
//        int temp= vh.getLayoutPosition();  //  -1 ??  // createViewHolder 之后才能获得准确的position
//        Log.i("AAA",""+temp);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.mTextView.setText(mDataset[position]);
        holder.mIVicon.setImageResource(m_icons[position]);

//        Log.i("AAA","current Position"+position);
        //rv中载入（显示）当前item的时候执行此函数--可以在此设置item的特性

        // 如果设置了回调，则设置点击事件
        if (mOnItemClickLitener != null)
        {
            holder.itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemClick(holder.itemView, pos);
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View v)
                {
                    int pos = holder.getLayoutPosition();
                    mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                    return false;
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
