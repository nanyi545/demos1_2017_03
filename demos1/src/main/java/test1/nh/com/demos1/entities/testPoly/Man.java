package test1.nh.com.demos1.entities.testPoly;

/**
 * Created by Administrator on 16-3-26.
 */
public class Man extends Talker {

    @Override
    public void letTalk(Talker talker) {
        System.out.println("letter-man");
        talker.talk();
    }

    @Override
    public void talk() {
        System.out.println("I am a man");
    }
    public void fight(){System.out.println("man fight");}
}
