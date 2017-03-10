package rm.module_net;

import com.webcon.dataProcess.dataUtils.ArrayAnalyser;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Created by Administrator on 16-1-14.
 */
public class ArrayAnalyserTest {

    private void dumpArray(int[] ints){
        int intLength=ints.length;
        for (int ii=0;ii<intLength;ii++){
            System.out.print(" "+ints[ii]);
        }
        System.out.println("end of array");
    }


    @Test
    public void testDiff1(){
        int[] ints={1,2,3,4};
        int[] expected_diff={1,1,1};
        assertArrayEquals(expected_diff, ArrayAnalyser.diff_array(ints));
    }


    @Test
    public void testDiff2(){
        int[] ints={4,3,2,1};
        int[] expected_diff={-1,-1,-1};
        assertArrayEquals(expected_diff, ArrayAnalyser.diff_array(ints));
    }

    @Test
    public void testDiff3(){
        int[] ints={4,3,2,1,2,8,4};
        int[] expected_diff={-1,-1,-1,1,6,-4};
        assertArrayEquals(expected_diff, ArrayAnalyser.diff_array(ints));
    }

    @Test
    public void testCross0_1(){
        int[] ints={-4,-3,-2,1,2,8,4,0,-1,-1,2};
        int[] expectedCross0={2};
        dumpArray(ArrayAnalyser.findCrossZeros_array(ints));
        assertArrayEquals(expectedCross0, ArrayAnalyser.findCrossZeros_array(ints));
    }

    @Test
    public void testCross0_2(){
        int[] ints={4,3,2,1,0,8,4,7,1,1,2};  // 没有crossZero point
        int[] expectedCross0={};
        dumpArray(ArrayAnalyser.findCrossZeros_array(ints));
        assertArrayEquals(expectedCross0, ArrayAnalyser.findCrossZeros_array(ints));
    }

    @Test
    public void testMin(){
        int[] ints={4,3,2,1,2,8,4};
        int[] expectedMins={3};
        dumpArray(ArrayAnalyser.findLocalMins(ints));
        assertArrayEquals(expectedMins, ArrayAnalyser.findLocalMins(ints));
    }

    @Test
    public void testMin2(){
        int[] ints={4,3,2,1,2,8,4,0,1,1,2};
        int[] expected_diff={-1,-1,-1,1,6,-4,-4,1,0,1};
        int[] expectedMins={3,7};
        assertArrayEquals(expected_diff, ArrayAnalyser.diff_array(ints));
        assertArrayEquals(expectedMins, ArrayAnalyser.findLocalMins(ints));
    }

    @Test
    public void testMinIndex(){
        int[] ints={4,3,2,1,2,8,4};
        int expectedIndex=3;
        assertEquals(expectedIndex, ArrayAnalyser.findMinIndex_inIntArray(ints));
    }

    @Test
    public void testMinIndex2(){
        int[] ints={4,3,2,1,2,8,4,-9};
        int expectedIndex=7;
        assertEquals(expectedIndex, ArrayAnalyser.findMinIndex_inIntArray(ints));
    }



    @Test
    public void testInterval(){
        List<Integer> intArray=new ArrayList<Integer>();
        intArray.add(new Integer(1));
        intArray.add(new Integer(3));
        intArray.add(new Integer(10));
        intArray.add(new Integer(12));
        System.out.println("pre list:"+intArray);
        List<Integer> intArray2=new ArrayList();
        intArray2=ArrayAnalyser.refinedWithInterval(intArray,3);
        System.out.println("after list:"+intArray2);
    }


    @Test
    public void testInterval2(){
        List<Integer> intArray=new ArrayList<Integer>();
        intArray.add(new Integer(1));
        intArray.add(new Integer(3));
        intArray.add(new Integer(10));
        intArray.add(new Integer(12));
        intArray.add(new Integer(14));
        System.out.println("pre list:"+intArray);
        List<Integer> intArray2=new ArrayList();
        intArray2=ArrayAnalyser.refinedWithInterval(intArray,3);
        System.out.println("after list:"+intArray2);
    }


    @Test
    public void testListReverse(){
        List<Integer> intArray=new ArrayList<Integer>();
        intArray.add(new Integer(1));
        intArray.add(new Integer(3));
        intArray.add(new Integer(10));
        intArray.add(new Integer(6));
        intArray.add(new Integer(14));
        System.out.println("original order:"+intArray);
        Collections.reverse(intArray);
        System.out.println("after reverse:"+intArray);
    }

    @Test
    public void testSubarray(){
        int[] ints={1,2,3,4,5,6,7,8};
        dumpArray(ArrayAnalyser.getSubArray(ints,2,5));
        int[] expected={3,4,5,6};
        System.out.println("---"+expected.hashCode()+"---"+ArrayAnalyser.getSubArray(ints,2,5).hashCode());// ---different hash code
        System.out.println("equals? "+expected.equals(ArrayAnalyser.getSubArray(ints,2,5)));  // ---false
        assertArrayEquals(expected,ArrayAnalyser.getSubArray(ints,2,5));
    }
    @Test
    public void testSubarray2(){
        int[] ints={1,2,3,4,5,6,7,8};
        int[] indexes={1,3,4,7};
        dumpArray(ArrayAnalyser.getSubArray(ints,indexes));
        int[] expected={2,4,5,8};
        assertArrayEquals(expected,ArrayAnalyser.getSubArray(ints,indexes));
    }




}
