package test1.nh.com.demos1.utils.fileOperation;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

/**
 * Created by Administrator on 16-1-23.
 */
public class ByteOperations {


    public static void dumpByteArray(byte[] byteArray){
        StringBuilder sb1=new StringBuilder();
        for (int aa=0;aa<byteArray.length;aa++){
            sb1.append(""+aa+"th number "+byteArray[aa]+"\n");
        }
        Log.i("AAA",sb1.toString());
    }


    public static byte[] intToByteArray1(int i) {
        byte[] result = new byte[4];
        result[0] = (byte)((i >> 24) & 0xFF);  // highest byte
        result[1] = (byte)((i >> 16) & 0xFF);
        result[2] = (byte)((i >> 8) & 0xFF);
        result[3] = (byte)(i & 0xFF);
        return result;
    }


    public static byte[] intToByteArray2(int i) throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buf);
        out.writeInt(i);
        out.writeInt(i);
        byte[] b = buf.toByteArray();
        out.close();
        buf.close();
        return b;
    }
    public static byte[] intToByteArray2(int[] i) throws Exception {
        ByteArrayOutputStream buf = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(buf);
        for (int aa=0;aa<i.length;aa++) out.writeInt(i[aa]);
        byte[] b = buf.toByteArray();
        out.close();
        buf.close();
        return b;
    }





}
