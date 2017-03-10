package test1.nh.com.demos1.mvpSQL.pack.di.components;

import javax.inject.Singleton;

import dagger.Component;
import test1.nh.com.demos1.mvpSQL.pack.db.DBmanager;
import test1.nh.com.demos1.mvpSQL.pack.di.modules.TableModule;

/**
 * Created by Administrator on 16-1-31.
 */
@Singleton
@Component(modules={TableModule.class})
public interface DBcomponent {
    void inject(DBmanager myDBmanager);
}
