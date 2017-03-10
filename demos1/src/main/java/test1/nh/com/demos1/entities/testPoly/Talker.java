package test1.nh.com.demos1.entities.testPoly;

/**
 * Created by Administrator on 16-3-26.
 */
public class Talker{

    public void talk(){
        System.out.println("un-specified");
    }

    public void letTalk(Talker talker){
        System.out.println("letter-un-specified");
        talker.talk();
    }
}
