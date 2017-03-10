package test1.nh.com.demos1.activities.generic_test;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import test1.nh.com.demos1.R;

/**
 * Created by Administrator on 2016/12/19.
 */
public class GenericAdapter<T>  extends RecyclerView.Adapter< GenericAdapter<T>.ViewHolder > {


    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView dispay;
        public ViewHolder(View v) {
            super(v);
        }
    }

    ArrayList<T> data;

    public GenericAdapter(ArrayList<T> data,Formatter<T> formatter ) {
        this.data = data;
        this.formatter=formatter;
    }

    public interface Formatter<T>{
        String format(T t);
    }

    private Formatter<T> formatter;


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.generic_adapter_lo, parent, false);
        ViewHolder vh = new ViewHolder(v);
        vh.dispay= (TextView) v.findViewById(R.id.display_tv);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.dispay.setText(formatter.format(data.get(position)));
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


}
