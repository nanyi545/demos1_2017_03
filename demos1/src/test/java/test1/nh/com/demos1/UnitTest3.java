package test1.nh.com.demos1;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by Administrator on 16-1-8.
 */
@RunWith(MockitoJUnitRunner.class)

public class UnitTest3 {
//    @Mock Context mMockContext;    //--->  can not find class def....

    private static final String FAKE_STRING = "HELLO WORLD";


    @Test
    public void readStringFromContext_LocalizedString() {

        Context mMockContext = Mockito.mock(Context.class);//--->  can not find class def....

        // Given a mocked Context injected into the object under test...
        when(mMockContext.getString(R.string.hello_world))
                .thenReturn(FAKE_STRING);

        String str=mMockContext.getString(R.string.hello_world);

        assertEquals(str,FAKE_STRING);
    }





}
