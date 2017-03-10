package test1.nh.com.demos1.mvpSQL.pack.model;

import android.content.Context;

import rx.Observable;
import test1.nh.com.demos1.mvpSQL.pack.db.DBmanager;
import test1.nh.com.demos1.mvpSQL.pack.di.components.DBcomponent;
import test1.nh.com.demos1.mvpSQL.pack.di.components.DaggerDBcomponent;
import test1.nh.com.demos1.utils.sqlite.Person4DB;

/**
 * Created by Administrator on 16-1-5.
 */
public class Model1SQL implements IModelSQL {

    private DBmanager db_contacts;
    DBcomponent myDBcomponent= DaggerDBcomponent.builder().build();

    public Model1SQL(Context context){
        db_contacts=new DBmanager(context);
        myDBcomponent.inject(db_contacts);
    }


    @Override
    public Observable getObs_Persons() {
        return db_contacts.getPersonsObs();
    }

    @Override
    public Observable getObs_Add(Person4DB p1) {
        return db_contacts.getAddObs(p1);
    }


}
