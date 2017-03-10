package test1.nh.com.demos1;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.LinkedList;
import java.util.List;

import test1.nh.com.demos1.utils.TestObject;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * Created by Administrator on 16-1-8.
 */
@RunWith(MockitoJUnitRunner.class)
public class UnitTest2 {

    private static final int FAKE_INT= 789;
//    TestObject mMockObj = Mockito.mock(TestObject.class);
    @Mock TestObject mMockObj;

    @Before
    public void before(){
        // Given a mocked Context injected into the object under test...
        when(mMockObj.getLength()).thenReturn(FAKE_INT);
    }

    @After
    public void after(){
    }


    @Test
    public void test1(){
        int result = mMockObj.getLength();
        assertEquals(result,78910);
    }

    @Test
    public void test2(){
        int result = mMockObj.getLength();
        assertEquals(result,FAKE_INT);
    }


    @Test
    public void verifyBehaviour(){
        // mock creation
        List mockedList = mock(List.class);
        // using mock object - it does not throw any "unexpected interaction" exception
        mockedList.add("one");
        mockedList.clear();
        // selective, explicit, highly readable verification
        verify(mockedList).add("one");
//        verify(mockedList).clear();
    }


    @Test
    public void verifyBehaviour1(){
        //You can mock concrete classes, not just interfaces
        LinkedList mockedList = mock(LinkedList.class);

        //stubbing
        when(mockedList.get(0)).thenReturn("first");
        when(mockedList.get(1)).thenThrow(new RuntimeException());

        //following prints "first"
        System.out.println(mockedList.get(0));

        //following throws runtime exception
//        System.out.println(mockedList.get(1));  // throw runtime exception --> this test fails

        //following prints "null" because get(999) was not stubbed
        System.out.println(mockedList.get(999));

        //Although it is possible to verify a stubbed invocation, usually it's just redundant
        //If your code cares what get(0) returns, then something else breaks (often even before verify() gets executed).
        //If your code doesn't care what get(0) returns, then it should not be stubbed. Not convinced? See here.
        verify(mockedList).get(0);
    }



    @Test
    public void verifyBehaviour2() {
        LinkedList mockedList = mock(LinkedList.class);

        //stubbing using built-in anyInt() argument matcher
        when(mockedList.get(anyInt())).thenReturn("element");

//        //stubbing using custom matcher (let's say isValid() returns your own matcher implementation):
//        when(mockedList.contains(argThat(isValid()))).thenReturn("element");

        //following prints "element"
        System.out.println(mockedList.get(999));

        //you can also verify using an argument matcher
        verify(mockedList).get(anyInt());
    }


    @Test
    public void verifyInvocation(){
        LinkedList mockedList = mock(LinkedList.class);

        //using mock
        mockedList.add("once");

        mockedList.add("twice");
        mockedList.add("twice");

        mockedList.add("three times");
        mockedList.add("three times");
        mockedList.add("three times");

        //following two verifications work exactly the same - times(1) is used by default
        verify(mockedList).add("once");
        verify(mockedList, times(1)).add("once");

        //exact number of invocations verification
        verify(mockedList, times(2)).add("twice");
        verify(mockedList, times(3)).add("three times");

        //verification using never(). never() is an alias to times(0)
        verify(mockedList, never()).add("never happened");

        //verification using atLeast()/atMost()
        verify(mockedList, atLeastOnce()).add("three times");
//        verify(mockedList, atLeast(2)).add("five times"); // this will fail..
        verify(mockedList, atMost(5)).add("three times");
    }





}
