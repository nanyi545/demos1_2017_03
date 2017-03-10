package test1.nh.com.demos1.activities.selectTime;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import test1.nh.com.demos1.R;


/**
 * Created by Administrator on 15-10-12.
 */
public class SelectRVAdapter extends RecyclerView.Adapter<SelectRVAdapter.ViewHolder>  {


    /**
     * @return  date: 0 is today, 1 is tomorrow  ....
     */
    public int getDate(){
        return mDataset.get(centerPosition);
    }

    private int centerPosition=3;

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


    private ArrayList<Integer>  mDataset;


    private Activity host;

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
    public SelectRVAdapter(ArrayList<Integer> myDataset,Activity activity) {
        mDataset = myDataset;
        this.host=activity;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public SelectRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                          int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.endless_text_view, parent, false);
        // set the view's size, margins, paddings and layout parameters
        ViewHolder vh = new ViewHolder(v);
        TextView tv_item=(TextView)v.findViewById(R.id.id_tv);
        vh.mTextView=tv_item;
        return vh;
    }


    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element

        final int modPosition= position ;
        holder.mTextView.setText(""+mDataset.get(modPosition));


        int different=(position-centerPosition);
        switch (different){
            case -4:
                holder.mTextView.setRotationX(50);
                holder.mTextView.setTextSize(15);
                holder.mTextView.setTextColor(host.getResources().getColor(R.color.Gray200));
                break;
            case -3:
                holder.mTextView.setRotationX(45);
                holder.mTextView.setTextSize(16);
                holder.mTextView.setTextColor(host.getResources().getColor(R.color.Gray300));
                break;
            case -2:
                holder.mTextView.setRotationX(35);
                holder.mTextView.setTextSize(17);
                holder.mTextView.setTextColor(host.getResources().getColor(R.color.Gray400));
                break;
            case -1:
                holder.mTextView.setRotationX(20);
                holder.mTextView.setTextSize(18);
                holder.mTextView.setTextColor(host.getResources().getColor(R.color.Gray600));
                break;
            case 0:
                holder.mTextView.setRotationX(0);
                holder.mTextView.setTextSize(21);
                holder.mTextView.setTextColor(host.getResources().getColor(R.color.Gray900));
                break;
            case 1:
                holder.mTextView.setRotationX(-20);
                holder.mTextView.setTextSize(18);
                holder.mTextView.setTextColor(host.getResources().getColor(R.color.Gray600));
                break;
            case 2:
                holder.mTextView.setRotationX(-35);
                holder.mTextView.setTextSize(17);
                holder.mTextView.setTextColor(host.getResources().getColor(R.color.Gray400));
                break;
            case 3:
                holder.mTextView.setRotationX(-45);
                holder.mTextView.setTextSize(16);
                holder.mTextView.setTextColor(host.getResources().getColor(R.color.Gray300));
                break;
            case 4:
                holder.mTextView.setRotationX(-50);
                holder.mTextView.setTextSize(15);
                holder.mTextView.setTextColor(host.getResources().getColor(R.color.Gray200));
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




    @Override
    public int getItemCount() {
        return mDataset.size();
    }



}
