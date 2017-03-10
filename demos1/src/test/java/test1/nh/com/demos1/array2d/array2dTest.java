package test1.nh.com.demos1.array2d;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Administrator on 15-12-30.
 */
public class array2dTest {

    private void dumpArray(char[] chars){
        int intLength=chars.length;
        StringBuilder sb=new StringBuilder();
        for (int ii=0;ii<intLength-1;ii++){
            sb.append(chars[ii]+"-");
        }
        sb.append(""+chars[intLength-1]);
        System.out.println(sb.toString());
    }

    private void dumpArray(char[][] chars){
        int intLength=chars.length;
        for (int ii=0;ii<intLength;ii++){
            dumpArray(chars[ii]);
        }
    }


    private void dumpArray(int[] ints){
        int intLength=ints.length;
        StringBuilder sb=new StringBuilder();
        for (int ii=0;ii<intLength-1;ii++){
            sb.append(ints[ii]+"-");
        }
        sb.append(""+ints[intLength-1]);
        System.out.println(sb.toString());
    }

    private void dumpArray(int[][] ints){
        int intLength=ints.length;
        for (int ii=0;ii<intLength;ii++){
            dumpArray(ints[ii]);
        }
    }

    int[][] ints;


    @Before
    public void before(){

    }

    @After
    public void after(){

    }


    @Test
    public void test1(){
        ints=new int[][]{{1,2,3},{4,5,6}};
        dumpArray(ints);
        int width=ints[0].length;
        int height=ints.length;

        System.out.println("width:"+width+"  height:"+height);
    }


    @Test
    public void test2(){
        ints=new int[][]{{1,2,3,4,5},{4,5,6},{1}};
        dumpArray(ints);
        int width0=ints[0].length;    // width
        int width1=ints[1].length;
        int width2=ints[2].length;

        int height=ints.length; // height

        System.out.println("width0:"+width0+"  width1:"+width1+"  width2:"+width2+"  height:"+height);
    }




    @Test
    public void test3(){
        ints=new int[][]{{1,2,3,4,5},{4,5,6,7,8},{8,7,6,5,4}};
        dumpArray(ints);

        for (int a=0;a<3;a++){
            System.out.println(""+ints[a][0]); // ints[height][width]
        }

    }

    final char EMPTY_BLOCK='.';

    @Test
    public void test4(){
        char[][] chars2d=new char[3][6];
        chars2d[0]="123456".toCharArray();
        chars2d[1]="abcdef".toCharArray();
        chars2d[2]="()*#@%".toCharArray();
        chars2d[0][3]=EMPTY_BLOCK;
        dumpArray(chars2d);
    }














}
