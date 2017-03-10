package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import dmax.dialog.SpotsDialog;
import test1.nh.com.demos1.R;

public class LoadingTestActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,LoadingTestActivity.class);
        c.startActivity(i);
    }


    public void startAni(View v){
        dialog.show();

    }

    public void stopAni(View v){
        dialog.dismiss();
    }


    SpotsDialog dialog;  // BY mit





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading_test);
//        dialog = new SpotsDialog(this,R.style.CustomLoading);
        dialog = new SpotsDialog(this,"登陆中 亲稍后");

    }


}
