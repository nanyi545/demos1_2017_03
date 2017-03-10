package test1.nh.com.demos1.dependencyInjection.di1;

/**
 * Created by Administrator on 15-12-29.
 * provide dependency to be injected
 * ----whether it is singleton depends on  getNetwork() method (with or without @Singleton), in NetworkApiModule
 */
public class NetworkApi {
    // fake network operation...
    public boolean validateUsers(String username,String password){
        if (null==username||username.length()==0) {return false;}
        else {return true;}
    }
}
