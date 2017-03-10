package test1.nh.com.demos1.mvpSQL.pack.views;

import android.util.Log;

import java.util.Iterator;
import java.util.List;

import test1.nh.com.demos1.utils.sqlite.Person4DB;

/**
 * Created by Administrator on 16-1-5.
 */
public class ViewSQL_impLog implements IViewSQL {

    @Override
    public void setPersonLoading(boolean isLoading) {

    }

    @Override
    public void setLoading(boolean isLoading) {

    }

    @Override
    public void refreshPersons(List<Person4DB> list) {
        Iterator<Person4DB> i1=list.iterator();
        int count=0;
        while (i1.hasNext()){
            Log.i("AAA",""+count+"th entry is:"+i1.next().toString());
            count=count+1;
        }
    }

    @Override
    public Person4DB getPerson() {
        return null;
    }
}
