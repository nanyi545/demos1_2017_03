package test1.nh.com.demos1.write2disk.ioOperations;

import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Administrator on 16-2-15.
 */
public class IntArrayIOoperation {

    private File file; // file to be operated
    private List<Integer> tempIntList;
    private int tempListSize;  // size of the int buffer, above which a "write" IO operation is triggered


    public IntArrayIOoperation(File file){
        this.file=file;
        tempIntList=new ArrayList();
        tempListSize=10;
    }

    public void appendInt2file(int a){
        tempIntList.add(a);
        Log.i("AAA", "appending..." + a +"   size" + tempIntList.size());
        if (tempIntList.size()==tempListSize){
            Log.i("AAA", "t?f..." +(tempIntList.size()==tempListSize)  +"   size" + tempIntList.size());
            flushFile();
        }
    }

    private void flushFile() {
        DataOutputStream dos = null;
        try {
            Log.i("AAA", "writing...");
            dos = new DataOutputStream(new FileOutputStream(file,true));
            int index = 0;
            int[] aaa=convert2intArray(tempIntList);
            while (index < aaa.length) {
                dos.writeInt(aaa[index]);
                index++;
                Log.i("AAA", "writing..." + index+"   in thread:"+Thread.currentThread().getName());
            }
            dos.close();
            tempIntList.clear();
            DataInputStream dis = new DataInputStream(new FileInputStream(file));
            int totalBytes = dis.available();
            while (totalBytes > 0) {
                Log.i("AAA", "read recording..." + dis.readInt()+"   in thread:"+Thread.currentThread().getName());
                totalBytes = dis.available();
            }
            dis.close();


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("AAA", "notfound...");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("AAA", "IOException e");
        }
    }



    // convert an Integer arraylist to an int array
    private static int[] convert2intArray(List<Integer> list){
        int[] convertedArray=new int[list.size()];
        Iterator<Integer> iterator=list.iterator();
        int tempIndex=0;
        while (iterator.hasNext()){
            convertedArray[tempIndex]=iterator.next();
            tempIndex++;
        }
        return convertedArray;
    }


}
