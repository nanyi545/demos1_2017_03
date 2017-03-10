package test1.nh.com.demos1.utils.fileOperation.ioManagement;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.concurrent.Callable;

/**
 * Created by Administrator on 16-2-1.
 */
public class IntArrayIO {

    // create sub folder-->named by the date: YEAR_MONTH_DAY,  to store int array files
    private static File getSubdir(File parDir, Calendar c1){
        if (!parDir.exists()) {
            parDir.mkdirs();
        }
        String str=c1.get(Calendar.YEAR)+"_"+(c1.get(Calendar.MONTH)+1)+"_"+c1.get(Calendar.DAY_OF_MONTH);
        File saveFile = new File(parDir, str);
        if (!saveFile.exists()) {
            saveFile.mkdirs();
        }
        return saveFile;
    }

    // generate file name(used to store file)from time -->
    public static String generateFileNameFromDate(){
        Calendar c=new GregorianCalendar();
        String str=c.get(Calendar.HOUR_OF_DAY)+"_"+c.get(Calendar.MINUTE);
        return str;
    }

    // ----------actual operations-->
    private void write2File(Context c,int[] aaa,Calendar timeOfWrite){
        if (Environment.getExternalStorageState().equals(   //检查是否有ExternalStorage
                Environment.MEDIA_MOUNTED)) {
            File dir = c.getFilesDir();  // files directory
            File subdir=getSubdir(dir,timeOfWrite);
            String fileName= generateFileNameFromDate();
//            if (!dir.exists()) {
//                dir.mkdirs();
//            }
            File saveFile = new File(subdir, fileName);
            try {
                DataOutputStream dos = new DataOutputStream(new FileOutputStream(saveFile));
                int index = 0;
                while (index < aaa.length) {
                    dos.writeInt(aaa[index]);
                    index++;
                    Log.i("AAA", "writing..." + index+"   in thread:"+Thread.currentThread().getName());
                }
                dos.close();
                DataInputStream dis = new DataInputStream(new FileInputStream(saveFile));
                int totalBytes = dis.available();
                while (totalBytes > 0) {
                    Log.i("AAA", "read recording..." + dis.readInt()+"   in thread:"+Thread.currentThread().getName());
                    totalBytes = dis.available();
                }
                dis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    //-----wrap in callable :-----
    protected Callable getWriteCallable(final Context c,final int[] aaa,final Calendar timeOfWrite){
        return new Callable(){
            @Override
            public Object call() throws Exception {
                write2File(c,aaa,timeOfWrite);
                return null;
            }
        };

    }


}
