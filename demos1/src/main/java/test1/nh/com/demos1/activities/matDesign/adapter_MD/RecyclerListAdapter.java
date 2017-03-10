package test1.nh.com.demos1.activities.matDesign.adapter_MD;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.matDesign.utils_MD.ItemTouchHelperAdapter;
import test1.nh.com.demos1.activities.matDesign.utils_MD.ItemTouchHelperViewHolder;
import test1.nh.com.demos1.activities.matDesign.utils_MD.OnStartDragListener;

/**
 * Created by Administrator on 15-11-3.
 */
public class RecyclerListAdapter extends RecyclerView.Adapter<RecyclerListAdapter.ItemViewHolder>
        implements ItemTouchHelperAdapter {

    private final List<String> mItems = new ArrayList<>();

    private final OnStartDragListener mDragStartListener;

    private Context context;

    public RecyclerListAdapter(Context context, OnStartDragListener dragStartListener) {
        mDragStartListener = dragStartListener;
        this.context=context;
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");
        mItems.add("http://www.90riji.com/wp-content/uploads/2013/02/20100315095707.jpg");

    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        ItemViewHolder itemViewHolder = new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {

        Log.i("AAA","showed:"+position);
        holder.textView.setText(mItems.get(position));

        // Start a drag whenever the handle view it touched
        holder.handleView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (MotionEventCompat.getActionMasked(event) == MotionEvent.ACTION_DOWN) {
                    mDragStartListener.onStartDrag(holder);
                }
                return false;
            }
        });

        //  -----use picasso to load images-------
//        Picasso.with(context).load(mItems.get(position)).into(holder.netImage);


    }

    @Override
    public void onItemDismiss(int position) {
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public static class ItemViewHolder extends RecyclerView.ViewHolder implements
            ItemTouchHelperViewHolder {

        public final TextView textView;
        public final ImageView handleView;
        public final ImageView netImage;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            handleView = (ImageView) itemView.findViewById(R.id.handle);
            netImage = (ImageView) itemView.findViewById(R.id.netImage);
        }

        @Override
        public void onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY);
        }

        @Override
        public void onItemClear() {
            itemView.setBackgroundColor(Color.GREEN);
        }
    }
}
