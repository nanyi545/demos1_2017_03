package test1.nh.com.demos1.activities;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

import test1.nh.com.demos1.R;


public class AlertTestActivity extends AppCompatActivity {

    Button b1;
    AlertDialog alertDialog;
    final String provinces[] = new String[]{"吉林", "辽宁", "黑龙江", "上海", "浙江"};

    ProgressDialog proDialog;

    AlertDialog alertDialog3;

    Toast mToast1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alert_test);
        b1 = (Button) findViewById(R.id.button1);

        mToast1=Toast.makeText(this,"用不关闭的toast",Toast.LENGTH_SHORT);

        // -------access Toast by using reflection-----
        boolean tf=false;
        try {
            Field field=mToast1.getClass().getDeclaredField("localLOGV");
            field.setAccessible(true);
//            field.setBoolean(mToast1,true);
            tf=(boolean)field.get(mToast1);
//            Field fieldModif=Field.class.getDeclaredField("modifiers");
//            fieldModif.setAccessible(true);
//            fieldModif.setInt();
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("Toast",Log.getStackTraceString(e));
        }
        // -------set Toast log on--------
        Log.i("Toast",""+tf);

        Toast.makeText(this, "--------", Toast.LENGTH_SHORT).show(); // this toast prints a log message : test1.nh.com.demos1 made a Toast: --------





    }


    /**
     * ------alert dialog with chioces--------
     * --click b1---
     * ----------------------why theme wrapper doesn't work????------
     *
     * @param view
     */
    @TargetApi(11)
    public void choices_alert_show(View view) {
//        alertDialog  = new AlertDialog.Builder(this)
//        alertDialog  = new AlertDialog.Builder(new ContextThemeWrapper(this,R.style.AlertDialogTheme1))
        alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogTheme1)
                .setTitle("this is the title")
//                .setMessage("this is the message")   // this is in conflic with setItems
                .setCancelable(false)
//                .setItems(provinces, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        Toast.makeText(AlertTestActivity.this, "you have selected:" + provinces[which], Toast.LENGTH_SHORT).show();
//                    }
//                })
                .setMultiChoiceItems(provinces, new boolean[]{false, false, false, false, false}, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        Toast.makeText(AlertTestActivity.this, "you have selected:" + provinces[which], Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("negative",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("AAA", "negative do nothing");
                            }
                        })
                .setPositiveButton("positive",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("AAA", "positive do nothing");
                                dialog.dismiss();
                            }
                        })
                .create();
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Teal50)));  // this works, using themewrapper doesn't work
        // what is the relation between alertDialog and its window??-----------it works for the V7.app.AlertDialog
        alertDialog.show();
    }


    /**
     * ------progress dialog --------
     * --click b2---
     *
     * @param view
     */

    public void progress_alert_show(View view) {
        proDialog = new ProgressDialog(this, R.style.AlertDialogTheme1);
        proDialog.setMessage("this is the message");
//        proDialog.setTitle("this is the title");
//        proDialog.setIcon(R.drawable.basketbal);
//        proDialog.setProgressStyle(proDialog.STYLE_HORIZONTAL);  //
        proDialog.setProgressStyle(proDialog.STYLE_SPINNER);       // spinner style的 setMax/setProgress 没用
        proDialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.Teal200)));  // this works, using themewrapper doesn't work
        proDialog.show();

    }


    /**
     * ------a costum dialog --------
     * --click b3---
     *
     * @param view
     */
    public void costum_alert_show(View view) {
        AlertDialog.Builder mAlertBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_signin, null);
        android.support.design.widget.TextInputLayout tlo1 = (android.support.design.widget.TextInputLayout) rootView.findViewById(R.id.username_wrapper);
        tlo1.setHint("user---name");

        mAlertBuilder.setView(rootView)
                .setPositiveButton("signing in", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        // sign in the user ...
                        Toast.makeText(AlertTestActivity.this, "login...", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(AlertTestActivity.this, "cancel...", Toast.LENGTH_SHORT).show();
                    }
                });
        alertDialog3 = mAlertBuilder.create();
        alertDialog3.show();
    }


    /**
     * control toast--open/close by reflection
     *  works for api10,    4.0以上不能用？？？ why??
     * @param view
     */
    public void openToast(View view) {
        try {
            Field field=mToast1.getClass().getDeclaredField("mTN");
            field.setAccessible(true);
            Object obj=field.get(mToast1);
            Method method=obj.getClass().getDeclaredMethod("show",(Class[])null);
            method.invoke(obj,new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("BBB",Log.getStackTraceString(e));
        }
    }

    public void closeToast(View view) {
        try {
            Field field=mToast1.getClass().getDeclaredField("mTN");
            field.setAccessible(true);
            Object obj=field.get(mToast1);
            Method method=obj.getClass().getDeclaredMethod("hide",new Class[0]);
            method.invoke(obj,new Object[]{});
        } catch (Exception e) {
            e.printStackTrace();
            Log.i("BBB", Log.getStackTraceString(e));
        }
    }



}
