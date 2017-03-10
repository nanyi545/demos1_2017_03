package test1.nh.com.demos1.entities.testOveride;

/**
 * Created by Administrator on 16-3-30.
 */
public class A {

    public int a;
    public A(int a){
            this.a=a;
        }
    public A(){
        this.a=0;
    }

    protected void printThis(){
            System.out.println("number:"+this.a);
        }
    protected void printThis(String str){
        System.out.println("number:"+this.a+" by"+str);
    }



}
