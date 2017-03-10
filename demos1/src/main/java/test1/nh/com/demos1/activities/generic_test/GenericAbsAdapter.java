package test1.nh.com.demos1.activities.generic_test;

import android.support.v7.widget.RecyclerView;

import java.util.List;


/**
 * Created by Administrator on 2016/12/19.
 */
public abstract class GenericAbsAdapter<VH extends RecyclerView.ViewHolder,T> extends RecyclerView.Adapter<VH> {


//    private T t;
//    public T getT(){return t;}
//    public void setT(T t){
//        this.t=t;
//    }

     List<T> data;

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }





    public interface Formatter<U>{
        String format(U u);
    }


}
