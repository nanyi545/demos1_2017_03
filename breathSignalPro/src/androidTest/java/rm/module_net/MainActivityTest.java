package rm.module_net;

import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import com.webcon.breath.freescale.ui.MainActivity;
import com.webcon.untils.BreathConstants;
import com.webcon.untils.fileOperation.FileOperation;
import com.webcon.untils.fileOperation.RecordsLocator;
import com.webcon.untils.fileOperation.recordsManagement.IOmanager;
import com.webcon.untils.fileOperation.recordsManagement.IntArrayIO;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;

import rx.Observer;

/**
 * Created by Administrator on 16-2-18.
 */
public class MainActivityTest extends ActivityInstrumentationTestCase2<MainActivity> {

    private MainActivity mMainActivity;
    RecordsLocator testRl;

    public MainActivityTest() {
        super(MainActivity.class);
    }


    int[] ymd1 = {2015, 1, 1}; // demo of finding  particular days
    int[] ymd2 = {2016, 2, 18};
    int[] ymd3 = {2016, 9, 18, 7, 6, 5, 43, 2, 1, 2, 3, 4, 5, 5};
    int[] ymd4 = {2030, 9, 18};

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mMainActivity = getActivity();
        testRl = new RecordsLocator(new GregorianCalendar(), mMainActivity, false);
    }


    public void testSimpleWrite() {
        File testFile = testRl.getRecFile();
        try {  // demo of writing tests
            DataOutputStream dos = new DataOutputStream(new FileOutputStream(testFile, true));
            dos.writeInt(1);
            dos.close();
            DataInputStream dis = new DataInputStream(new FileInputStream(testFile));
            int aa = dis.readInt();
            assertEquals(aa, 1);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void testSimpleRead() {
//        File testFile= new File(new File(new File(mMainActivity.getFilesDir(), BreathConstants.RECORD_FILE_FOLDER),"2016_02_29"),"14_20.test");
        File testFile= new File(new File(new File(mMainActivity.getFilesDir(), BreathConstants.RECORD_FILE_FOLDER),"2016_03_03"),"12_20.nhBreath");

        try {
            DataInputStream dis = new DataInputStream(new FileInputStream(testFile));
            int aa = dis.available();
            Log.i("CCC","file size in bytes;"+aa);
            while (aa>0){
                Log.i("CCC",""+dis.readInt());
                aa= dis.available();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.i("CCC","FileNotFoundException");
        } catch (IOException e) {
            e.printStackTrace();
            Log.i("CCC","IOException");
        }
    }




    public void testShowAllFiles() {
        List<File> dayFolders = testRl.getDayFolders(ymd1, ymd4);
        Iterator<File> i1=dayFolders.iterator();
        int counter=0;
        while (i1.hasNext()){  // iterate through to show all the files
            counter++;
            File dayFoler=i1.next();
            Log.i("CCC", "folder number:"+counter+":" + dayFoler.getName());
            FileOperation.printFiles(dayFoler,"CCC");
        }
    }



    public void testGetDays() {
        List<File> dayFolders = testRl.getDayFolders(ymd1, ymd2);
        Log.i("AAA", "" + dayFolders.toString());
    }


    public void testRXwrite() {
        File testFile = testRl.getRecFile();
        IOmanager writer = new IOmanager(new IntArrayIO(testFile));

        Observer<Callable> write_observer = new Observer<Callable>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if ("java.util.concurrent.TimeoutException".equals(e.toString())) {
                    Log.i("AAA", "time-out");
                }
            }

            @Override
            public void onNext(Callable func) {
                try {
                    func.call();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.i("AAA", Log.getStackTraceString(e));
                }
                Log.i("AAA", "on next" + Thread.currentThread().getName());
            }
        };
        writer.setWriteObserver(write_observer);

        writer.sendArray(ymd1);  //  threading is handled by rx-java
        try {
            Thread.currentThread().sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("AAA", "--first write--");
        writer.sendArray(ymd2);
        try {
            Thread.currentThread().sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.i("AAA", "--2nd write--");
        writer.sendArray(ymd3);
        Log.i("AAA", "--last write--");

    }


    public void testVoid(){
        mMainActivity.getFilesDir();
    }

    public void testRXread() {

//        File file= new File(new File(new File(mMainActivity.getFilesDir(), BreathConstants.RECORD_FILE_FOLDER),"2016_02_29"),"14_36.test");

        File file= new File(new File(new File(mMainActivity.getFilesDir(), BreathConstants.RECORD_FILE_FOLDER),"2016_03_03"),"12_20.nhBreath");
        // /data/data/rm.module_net/files/nh_breath_rec1/2016_2_19/13_28.test
        //   2016_02_29/14_36.test
        //   2016_03_03/12_20.nhBreath
        Log.i("CCC",""+file.getAbsolutePath());


        IOmanager reader = new IOmanager(new IntArrayIO(file));

        Observer<Callable<int[]>> read_observer = new Observer<Callable<int[]>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {}

            @Override
            public void onNext(Callable<int[]> func) {
                Log.i("CCC", "on next" + Thread.currentThread().getName());
                try {
                    int[] aa=func.call();
                    Log.i("CCC", "int array size:" + aa.length);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        reader.setReadObserver(read_observer);
        reader.readArrayFromFile(file);

    }


}
