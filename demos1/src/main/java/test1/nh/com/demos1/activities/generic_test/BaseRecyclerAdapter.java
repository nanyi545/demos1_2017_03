package test1.nh.com.demos1.activities.generic_test;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/20.
 */
public abstract class BaseRecyclerAdapter<T> extends RecyclerView.Adapter<BaseRecyclerAdapter.RecyclerViewHolder> {

    public interface ItemSelectedListener<T> {
        void onItemSelected(T t);
    }

    private final List<T> items = new ArrayList<>();

    private ItemSelectedListener<T> itemSelectedListener;

    public void setItems(List<T> items) {
        this.items.clear();
        this.items.addAll(items);
        notifyDataSetChanged();
    }

    public T getItemAt(int position) {
        return items.get(position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setItemSelectedListener(ItemSelectedListener<T> itemSelectedListener) {
        this.itemSelectedListener = itemSelectedListener;
    }
    protected void onItemSelected(int position) {
        if (itemSelectedListener != null) {
            itemSelectedListener.onItemSelected(getItemAt(position));
        }
    }


    @Override
    public void onBindViewHolder(RecyclerViewHolder recyclerViewHolder, int position) {
        recyclerViewHolder.setItem(getItemAt(position), position);
    }


    public void addItem(T item) {
        items.add(item);
        notifyItemInserted(getItemCount() - 1);
    }

    public List<T> getItems() {
        return new ArrayList<>(items);
    }

    public abstract class RecyclerViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener{

        public RecyclerViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        public abstract void setItem(T t, int position);

        @Override
        public void onClick(View v) {
            onItemSelected(getAdapterPosition());
        }

        protected T getItem() {
            return (T) getItemAt(getAdapterPosition());
        }
    }
}