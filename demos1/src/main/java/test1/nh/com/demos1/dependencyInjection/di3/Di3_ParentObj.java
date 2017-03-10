package test1.nh.com.demos1.dependencyInjection.di3;

import javax.inject.Inject;

/**
 * Created by Administrator on 16-1-7.
 */
public class Di3_ParentObj {

    public Di3_Obj child;

    @Inject
    public Di3_ParentObj(Di3_Obj child){
        this.child=child;
    }

}
