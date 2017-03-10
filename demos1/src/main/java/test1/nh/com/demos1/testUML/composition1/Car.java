package test1.nh.com.demos1.testUML.composition1;

import java.util.List;

/**
 * Created by Administrator on 16-5-7.
 */
public class Car {

    private Engine e1;
    private List<Tyre> tyres;

    public Car(Engine e1){
        this.e1=e1;
    }
    public Car(Engine e1,List<Tyre> tyres){
        this.e1=e1;
        this.tyres=tyres;
    }


    public void setEngine(Engine e1){
        this.e1=e1;
    }


}
