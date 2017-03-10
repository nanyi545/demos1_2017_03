package test1.nh.com.demos1.mvpSQL.pack.model;

import rx.Observable;
import test1.nh.com.demos1.utils.sqlite.Person4DB;

/**
 * Created by Administrator on 16-1-4.
 */
public interface IModelSQL {


    // has to be in worker thread
    Observable getObs_Persons();
    Observable getObs_Add(Person4DB p1);

}
