package test1.nh.com.demos1.mvpSQL.pack.presenters;

import android.content.Context;
import android.util.Log;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import test1.nh.com.demos1.mvpSQL.pack.model.IModelSQL;
import test1.nh.com.demos1.mvpSQL.pack.model.Model1SQL;
import test1.nh.com.demos1.mvpSQL.pack.views.IViewSQL;
import test1.nh.com.demos1.mvpSQL.pack.views.ViewSQL_impLog;
import test1.nh.com.demos1.utils.sqlite.Person4DB;

/**
 * Created by Administrator on 16-1-5.
 */
public class SQLpresenter1 {

    private IModelSQL modelSQL;
    private IViewSQL mViewSQL=new ViewSQL_impLog();
    private boolean viewLock=false;  // ensure that when an action is performed, other UI interaction is disabled


    public SQLpresenter1(Context context){
        this.modelSQL=new Model1SQL(context);
    }


    public SQLpresenter1(Context context,IViewSQL mViewSQL){
        this.modelSQL=new Model1SQL(context);
        this.mViewSQL=mViewSQL;
    }


    public void refreshView(){
        if (!viewLock) {
            viewLock=true;
            mViewSQL.setLoading(true);

            Observable observable = modelSQL.getObs_Persons();

            Observer<List<Person4DB>> observer = new Observer<List<Person4DB>>() {
                @Override
                public void onCompleted() {
                    Log.i("AAA", "obs1 completed");
                }

                @Override
                public void onError(Throwable e) {
                    Log.i("AAA", "obs1 onerror");
                }

                @Override
                public void onNext(List list) {
                    mViewSQL.refreshPersons(list);
                    mViewSQL.setLoading(false);
                    viewLock=false;
                }
            };

            observable.observeOn(AndroidSchedulers.mainThread()).subscribe(observer); // observe on UI thread
            Log.i("AAA", "done subscribing");
        } else Log.i("AAA", "processing....");
    }


    public void addPerson(){
        if (!viewLock) {
            viewLock=true;
            Observable observable = modelSQL.getObs_Add(mViewSQL.getPerson());
            mViewSQL.setPersonLoading(true);

            Observer<Long> observer = new Observer<Long>() {
                @Override
                public void onCompleted() {
                    Log.i("AAA", "obs1 completed");
                }

                @Override
                public void onError(Throwable e) {
                    Log.i("AAA", "obs1 onerror");
                }

                @Override
                public void onNext(Long ret) {
                    Log.i("AAA", "added successful in row" + ret);
                    mViewSQL.setPersonLoading(false);
                    viewLock=false;
                }
            };
            observable.observeOn(AndroidSchedulers.mainThread()).subscribe(observer); // observe on UI thread
        } else Log.i("AAA", "processing....");
    }



}
