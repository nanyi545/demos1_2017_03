package test1.nh.com.demos1.write2disk;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import java.io.File;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.write2disk.ioOperations.IntArrayIOoperation;

public class Write2diskActivity extends AppCompatActivity {

    public static void start(Context context) {
        final Intent intent = new Intent(context, Write2diskActivity.class);
        context.startActivity(intent);
    }


    File f1;
    IntArrayIOoperation iOoperation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        f1=new File(getFilesDir(),"testIO");
        f1.getParentFile().mkdirs();  // incase of file not found exception

        iOoperation=new IntArrayIOoperation(f1);

        setContentView(R.layout.activity_write2disk);
        for (int a=0;a<20;a++){
            iOoperation.appendInt2file(a);
        }
    }
}
