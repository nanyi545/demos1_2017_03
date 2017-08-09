package test1.nh.com.demos1.activities.pull_load;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import test1.nh.com.demos1.R;


/**
 * Created by Administrator on 15-10-12.
 */
public class MyRVAdapter extends RecyclerView.Adapter<MyRVAdapter.ViewHolder> {


    public interface OnItemClickLitener {
        void onItemClick(View view, int position);
        void onItemLongClick(View view, int position);
        void onNodata(View view);
    }

    public OnItemClickLitener mOnItemClickLitener;

    public void setOnItemClickLitener1(OnItemClickLitener mOnItemClickLitener) {
        this.mOnItemClickLitener = mOnItemClickLitener;
    }


    private List<String> mDataset;


    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        private TextView mTextView;
        private int viewType;

        public ViewHolder(View v) {
            super(v);
        }
    }

    // Provide a suitable constructor (depends on the kind of dataset)
    public MyRVAdapter(List<String> myDataset) {
        mDataset = myDataset;
    }

    // Create new views (invoked by the layout manager)
    @Override
    public MyRVAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                     int viewType) {
        // create a new view
        ViewHolder vh=null;
        switch(viewType){
            case NORMAL_TYPE:
                View v = new TextView(parent.getContext());
                v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                vh= new ViewHolder(v);
                vh.mTextView = (TextView) v;
                vh.mTextView.setBackgroundResource(R.color.Red100);
                vh.mTextView.setPadding(0,200,0,200);
                vh.viewType=viewType;
                break;
            case NODATA_TYPE:
                View v1 = new TextView(parent.getContext());
                v1.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT));
                vh= new ViewHolder(v1);
                vh.mTextView = (TextView) v1;
                vh.mTextView.setBackgroundResource(R.color.Teal200);
                vh.mTextView.setText("---no data---");
                vh.viewType=viewType;
                break;

        }


        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        switch(holder.viewType){
            case NORMAL_TYPE:
                holder.mTextView.setText(mDataset.get(position));
                if (mOnItemClickLitener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            int pos = holder.getLayoutPosition();
                            mOnItemClickLitener.onItemClick(holder.itemView, pos);
                        }
                    });

                    holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                        @Override
                        public boolean onLongClick(View v) {
                            int pos = holder.getLayoutPosition();
                            mOnItemClickLitener.onItemLongClick(holder.itemView, pos);
                            return false;
                        }
                    });
                }
                break;
            case NODATA_TYPE:
                if (mOnItemClickLitener != null) {
                    holder.itemView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            mOnItemClickLitener.onNodata(holder.itemView);
                        }
                    });
                }
                break;
        }
    }

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        if(mDataset.size()==0)  return 1;
        else {return mDataset.size();}
    }

    private static final int NORMAL_TYPE=11;
    private static final int NODATA_TYPE=111;


    @Override
    public int getItemViewType(int position) {
        if(mDataset.size()>0) return NORMAL_TYPE ;
        else return NODATA_TYPE;
    }


}
