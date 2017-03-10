package test1.nh.com.demos1.mvpSQL.pack.di.modules;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import test1.nh.com.demos1.mvpSQL.pack.db.ContactTable;

/**
 * Created by Administrator on 16-1-31.
 */
@Module
public class TableModule {

    @Provides
    @Singleton
    public ContactTable getContactTable(){
        return new ContactTable();
    }



}
