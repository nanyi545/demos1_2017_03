package test1.nh.com.demos1.activities.draw_under_statusbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ContentFrameLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.PopupWindow;

import test1.nh.com.demos1.R;

public class DrawUnderStatusbarActivity extends AppCompatActivity {


    public static void start(Context c){
        Intent i=new Intent(c,DrawUnderStatusbarActivity.class);
        c.startActivity(i);
    }


    @Override
    protected void onResume() {
        super.onResume();
        Log.i("hehe","onResume");  //  no life cycle called for AlertDialog
    }


    @Override
    protected void onPause() {
        super.onPause();
        Log.i("hehe","onPause");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if  (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT  ) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        setContentView(R.layout.activity_draw_under_statusbar);


    }


    public void click(View v){
        int id=v.getId();
        switch (id){
            case R.id.show_alert:
                showAlert();
                break;
            case R.id.show_pop:
                showPop();
                break;
            case R.id.show_pop_inflator:
                showPopView();
                break;
            default:
                break;
        }

    }


    private void showPopView(){
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View view = layoutInflater.inflate(R.layout.this_is_a_pop, null);

        View root=getWindow().getDecorView().getRootView();
        Log.i("hehe","root:"+root.getClass().getSimpleName());


        View root1= (View) findViewById(R.id.holder).getParent();
        Log.i("hehe","root1:"+root1.getClass().getSimpleName());

        View root2=  findViewById(R.id.holder).getRootView();
        Log.i("hehe","root2:"+root2.getClass().getSimpleName());


        if(root1 instanceof FrameLayout){
            View popup=root1.findViewById(R.id.pop_up);
            if (popup==null){
                FrameLayout casted= (FrameLayout) root1;
                view.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
                casted.addView(view);
                view.bringToFront();
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view.setVisibility(View.INVISIBLE);
                    }
                });
            } else {
                popup.bringToFront();
                popup.setVisibility(View.VISIBLE);
            }

        }

    }


    PopupWindow popupWindow;

    private void showPop() {
        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.this_is_a_pop, null);
        popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

//        popupWindow.showAsDropDown(findViewById(R.id.show_pop));
        popupWindow.showAsDropDown(findViewById(R.id.anchor));
//        popupWindow.showAtLocation(findViewById(R.id.anchor), Gravity.NO_GRAVITY , 0, 300);

//        View rootView = getWindow().getDecorView().getRootView();
//        popupWindow.showAtLocation(rootView, Gravity.NO_GRAVITY, 0, -200);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }





    AlertDialog alert;
    private void showAlert() {
        alert = new AlertDialog.Builder(this,R.style.AppTheme_NoActionBar_UnderStatusBar_FullScreenAlert).create();   //  android.R.style.Theme_Black_NoTitleBar_Fullscreen
//        alert = new AlertDialog.Builder(this,R.style.dialog).create();   //          this doesn't work
//        alert.setTitle(" is it full screen??");
        alert.setCanceledOnTouchOutside(true);
        alert.show();
        alert.setContentView(R.layout.this_is_an_alert);
    }






}
