package serial_port.demo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.IOException;

import rm.module_net.R;
import serial_port.utils.SerialPort;

public class SerialTestActivity extends Activity {


    public static void goTo(Context context){
        Intent i1=new Intent(context,SerialTestActivity.class);
        context.startActivity(i1);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_serial_test);

        //-----------freescale
//        File file=new File("/dev/ttymxc3");// exist true
//        File file1=new File("/ttymxc3");  // exist false
        //-------lenova test phone
//        File file=new File("/dev/ttyS2");// exist true
//        Log.i("breath_app","device exist"+(file.exists())+"     file.canRead()"+(file.canRead())+"  file.canWrite()"+(file.canWrite()));
//
//        try {
//            SerialPort por1=new SerialPort(file,115200,1);
//        } catch (IOException e) {
//            e.printStackTrace();
//            Log.i("breath_app","IOexception");
//        }


    }

}
