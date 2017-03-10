package test1.nh.com.demos1.entities.testOveride;

/**
 * Created by Administrator on 16-4-10.
 */
public class AA implements IPrint {

    public int a;
    public AA(int a){
        this.a=a;
    }
    public AA(){
        this.a=0;
    }


    @Override
    public void printThis() {
        System.out.println("number:"+this.a);
    }

    @Override
    public void printThis(String str) {
        System.out.println("number:"+this.a+" by"+str);
    }
}
