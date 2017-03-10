package rm.module_net;

import com.webcon.untils.alarmManagement.alarmEntities.BreathAlarm;
import com.webcon.untils.alarmManagement.alarmEntities.OverTimeAlarm;
import com.webcon.untils.alarmManagement.alarmEntities.SmallFluxAlarm;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.GregorianCalendar;

/**
 * Created by Administrator on 15-12-30.
 */
public class BreathAlarmTest {


    int[] a1=new int[300];
    BreathAlarm alarm1;
    BreathAlarm alarm2;

    @Before
    public void before(){

    }

    @After
    public void after(){

    }


    @Test
    public void test1(){
        int[] fluxes={4000,3000};
        alarm1=new SmallFluxAlarm(new GregorianCalendar(),a1,fluxes);
        System.out.println(alarm1.toString());
    }


    @Test
    public void test2(){
        alarm2=new OverTimeAlarm(new GregorianCalendar(),a1,63);
        System.out.println(alarm2.toString());
    }






}
