package test1.nh.com.demos1.entities.testPoly;

/**
 * Created by Administrator on 16-3-26.
 */
public class Woman extends Talker{
    @Override
    public void talk() {
        System.out.println("I am a woman");
    }

    @Override
    public void letTalk(Talker talker) {
        System.out.println("letter-woman");
        talker.talk();
    }

}
