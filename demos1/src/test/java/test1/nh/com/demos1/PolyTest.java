package test1.nh.com.demos1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import test1.nh.com.demos1.entities.testPoly.Itest;
import test1.nh.com.demos1.entities.testPoly.Man;
import test1.nh.com.demos1.entities.testPoly.Talker;
import test1.nh.com.demos1.entities.testPoly.TestClass;
import test1.nh.com.demos1.entities.testPoly.Woman;

/**
 * Created by Administrator on 15-12-30.
 */
public class PolyTest {





    @Before
    public void before(){
    }

    @After
    public void after(){
    }


    @Test
    public void test1(){
        Talker man=new Man();
        man.talk();
    }

    @Test
    public void test2(){
        Talker man=new Man();
//        man.fight();  // Talker interface can not access the fight() method in Man
    }


    @Test
    public void test3(){
        Talker[] talkers=new Talker[3];
        talkers[0]=new Man();
        talkers[1]=new Man();
        talkers[2]=new Woman();
        for (Talker talker:talkers){
            talker.talk();
        }
    }


    @Test
    public void checkDynamicCalling(){
        Talker letter=new Man();
        Talker letter1=new Talker();

        Talker man=new Man();
        Talker women=new Woman();

        letter.letTalk(man);
        letter.letTalk(women);

        letter1.letTalk(man);
        letter1.letTalk(women);
    }

    @Test
    public void testAdapterPattern() {
        Itest test1=new TestClass();
        test1.javaPrint();
    }


}
