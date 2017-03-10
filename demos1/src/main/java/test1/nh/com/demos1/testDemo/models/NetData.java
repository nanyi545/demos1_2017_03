package test1.nh.com.demos1.testDemo.models;

/**
 * Created by Administrator on 16-1-11.
 */
public class NetData implements ItestData{

    @Override
    public String obtainTestString(String inputStr) {
        return "this is obtained from internet"+inputStr;
    }

}
