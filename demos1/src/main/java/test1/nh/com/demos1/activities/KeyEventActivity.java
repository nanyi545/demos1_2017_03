package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;

import test1.nh.com.demos1.R;


public class KeyEventActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i1=new Intent(c,KeyEventActivity.class);
        c.startActivity(i1);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_key_event);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.i("TESTKEY","Key down--- keyEvent:"+event.toString());
        return true;  // true---> handled   false--> further propagate
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.i("TESTKEY","Key up--- keyEvent:"+event.toString());
        return true;  // true---> handled   false--> further propagate
    }


    @Override
    public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
        Log.i("TESTKEY","Key multi--- keyEvent:"+event.toString());
        return true;  // true---> handled   false--> further propagate
    }


    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        Log.i("TESTKEY","Key long pressed--- keyEvent:"+event.toString());
        return true;  // true---> handled   false--> further propagate
    }

    @Override
    public boolean onKeyShortcut(int keyCode, KeyEvent event) {
        Log.i("TESTKEY","Key shortcut--- keyEvent:"+event.toString());
        return true;  // true---> handled   false--> further propagate
    }
}
