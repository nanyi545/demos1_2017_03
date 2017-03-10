package test1.nh.com.demos1;

import org.apache.commons.math3.complex.Complex;
import org.apache.commons.math3.transform.DftNormalization;
import org.apache.commons.math3.transform.FastFourierTransformer;
import org.apache.commons.math3.transform.TransformType;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Administrator on 15-12-30.
 */
public class FFTtest {


    DftNormalization norm1=DftNormalization.STANDARD;
    FastFourierTransformer transformer=new FastFourierTransformer(norm1);

    private void dumpArray(int[] ints){
        int intLength=ints.length;
        for (int ii=0;ii<intLength;ii++){
            System.out.print(" "+ints[ii]);
        }
        System.out.println("end of array");
    }
    private void dumpArray(double[] doubles){
        int intLength=doubles.length;
        for (int ii=0;ii<intLength;ii++){
            System.out.println(" "+doubles[ii]);
        }
        System.out.println("end of array");
    }



    @Before
    public void before(){

    }

    @After
    public void after(){

    }


    @Test
    public void test1(){
        double [] input = new double[8];
        input[0] = 0.0;
        input[1] = 162.6345596729059;
        input[2] = 230.0;
        input[3] = 162.63455967290594;
        input[4] = 2.8166876380389125E-14;
        input[5] = -162.6345596729059;
        input[6] = -230.0;
        input[7] = -162.63455967290597;
        double[] tempConversion = new double[input.length];

        Complex[] complx = transformer.transform(input, TransformType.FORWARD);  // forward transform, as apposed to inverse transform
        for (int i = 0; i < complx.length; i++) {
            double rr = (complx[i].getReal());
            double ri = (complx[i].getImaginary());
            tempConversion[i] = Math.sqrt((rr * rr) + (ri * ri));
        }

        dumpArray(tempConversion);

    }


    @Test
    public void test2(){
        double [] input = new double[8];
        for (int aa=0;aa<8;aa++){
            input[aa]=aa+1;
        }

        double[] tempConversion = new double[input.length];

        Complex[] complx = transformer.transform(input, TransformType.FORWARD);  // forward transform, as apposed to inverse transform
        for (int i = 0; i < complx.length; i++) {
            double rr = (complx[i].getReal());
            double ri = (complx[i].getImaginary());
            tempConversion[i] = Math.sqrt((rr * rr) + (ri * ri));
        }

        dumpArray(tempConversion);

    }



}
