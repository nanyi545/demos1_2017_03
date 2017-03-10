package rm.module_net;

import com.webcon.dataProcess.DataArray;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

/**
 * Created by Administrator on 16-1-13.
 */
public class DataArrayTest {

    private DataArray dataArray;

    @Before
    public void before(){

    }

    @After
    public void after(){

    }


    @Test
    public void test1(){
        dataArray=new DataArray(5);
        int[] ints={1,2};
        dataArray.pushData(ints);
        int[] ints_expected={1,2,0,0,0};
        assertArrayEquals(ints_expected,dataArray.getBreathArray());
    }

    @Test
    public void test2(){
        dataArray=new DataArray(10);
        int[] ints={1,2,99};
        dataArray.pushData(ints);
        int[] ints_expected={1,2,99,0,0,0,0,0,0,0};
        assertArrayEquals(ints_expected,dataArray.getBreathArray());
    }


    @Test
    public void test3(){
        dataArray=new DataArray();
        int[] ints={1,2,99,66};
        dataArray.pushData(ints);
        int[] ints_expected=new int[300];
        System.arraycopy(ints,0,ints_expected,0,ints.length);
        assertArrayEquals(ints_expected,dataArray.getBreathArray());
    }



}
