package test1.nh.com.demos1.activities.gravity_test;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import test1.nh.com.demos1.R;

public class TestGravityActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,TestGravityActivity.class);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_gravity);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Log.i("gravity","    CENTER_VERTICAL:"+Gravity.CENTER_VERTICAL    //  16
                    +"    CENTER_HORIZONTAL:"+Gravity.CENTER_HORIZONTAL   //       1
                    + "   config:" +getResources().getConfiguration().getLayoutDirection());    // this is language direction , there are languages RTL(right to left)       //  script specific gravity
        }

    }

    Toast toast;

    public void test(View v){
        int id=v.getId();
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.test_gravity_1,null);

        switch(id){
            case R.id.btn1:
                toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.CENTER_VERTICAL,0,0);
//                toast.setGravity(Gravity.CENTER_VERTICAL, 100, 100);
//                toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
                break;
            case R.id.btn2:
                toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.BOTTOM, 0, 100);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
                break;
            case R.id.btn3:
                toast = new Toast(getApplicationContext());
                toast.setGravity(Gravity.FILL_VERTICAL, 0, 0);
                toast.setDuration(Toast.LENGTH_SHORT);
                toast.setView(layout);
                toast.show();
                break;


        }

    }




}

