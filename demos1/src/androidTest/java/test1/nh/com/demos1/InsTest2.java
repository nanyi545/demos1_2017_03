package test1.nh.com.demos1;

import android.support.test.runner.AndroidJUnit4;
import android.test.InstrumentationTestCase;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Created by Administrator on 16-1-10.
 */
@RunWith(AndroidJUnit4.class)
public class InsTest2 extends InstrumentationTestCase {

    @Test
    public void test() throws Exception {
        final int expected = 1;
        final int reality = 1;
        assertEquals(expected, reality);
    }

}
