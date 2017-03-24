package test1.nh.com.demos1.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.bessel.BesselActivity;
import test1.nh.com.demos1.activities.cyclic_galary.CyclicGalaryActivity;
import test1.nh.com.demos1.activities.fling_test.FlingTestActivity;
import test1.nh.com.demos1.activities.generic_test.GenericTestActivity;
import test1.nh.com.demos1.activities.gesture_pass.GesturePassWordActivity;
import test1.nh.com.demos1.activities.horizontalScroll.HorizontalScrollActivity;
import test1.nh.com.demos1.activities.hybrid_test.WebViewActivity;
import test1.nh.com.demos1.activities.hybrid_test.WebViewActivity2;
import test1.nh.com.demos1.activities.interactive_chart.InteractiveActivity;
import test1.nh.com.demos1.activities.leaksTest.LeakActivity;
import test1.nh.com.demos1.activities.leaksTest.LeakTest2Activity;
import test1.nh.com.demos1.activities.matDesign.MatDesignPanelActivity;
import test1.nh.com.demos1.activities.range_seek.RangeSeekbarMainActivity;
import test1.nh.com.demos1.activities.selectTime.SelectTimeActivity;
import test1.nh.com.demos1.activities.tab_layout_viewpager.TestTabActivity;
import test1.nh.com.demos1.activities.test3d.Test3dActivity;
import test1.nh.com.demos1.activities.time_picker.TimePickerActivity;
import test1.nh.com.demos1.activities.vertical_scroll.VerticalScrollActivity;
import test1.nh.com.demos1.activityManagerActivity.ActManagerActivity;
import test1.nh.com.demos1.audioTest.AudioActivity;
import test1.nh.com.demos1.broadcastReceiverDemo.BCRActivity;
import test1.nh.com.demos1.guavaDest.GuavaTestActivity;
import test1.nh.com.demos1.ipcDemo.IPCactivity;
import test1.nh.com.demos1.launchMode.LaunchModeActivity;
import test1.nh.com.demos1.mvpSQL.pack.SQLiteBoilerActivity;
import test1.nh.com.demos1.okhttpDemo.OKhttpActivity;
import test1.nh.com.demos1.testDemo.UnitTestWithDIActivity;
import test1.nh.com.demos1.utils.DMapplication;
import test1.nh.com.demos1.utils.TextObtainer;
import test1.nh.com.demos1.utils.images.BitmapUtil;
import test1.nh.com.demos1.write2disk.Write2diskActivity;

public class MainActivity_from extends AppCompatActivity {
    private ImageView cursorIV;
    // 图片偏移量
    private int cursorOffset = 0;
    // 当前页卡编号
    private int cursorIndex = 0;
    // 图片宽度
    private int cursorImgWidth = 0;

    Context mContext;

    //测试数组初始化----
    private int[] callbackBuffer;

    //测试布尔初始值---
    private boolean isOK;


    // 测试AlertDialog  +  测试利用反射修改AlertDialog
    AlertDialog  alertDialog;

    TextView tv1_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity_from);


        tv1_main=(TextView)findViewById(R.id.textView1);
        tv1_main.setText(TextObtainer.obtainText());


        // ---test of actionbar activities---
        Button b_appbar1=(Button)findViewById(R.id.button20);
        b_appbar1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MainActivity_from.this, ActionBarActivity1.class);
                startActivity(mIntent);
            }
        });

        // ---good demo of APPbar---
        Button b_appbar2=(Button)findViewById(R.id.button21);
        b_appbar2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(MainActivity_from.this,ActionBarActivity2.class);
                startActivity(mIntent);
            }
        });





        // -------------log 打印测试---String.compareTo 测试
        Button b1=(Button)findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("AAA", "log test");
                Log.i("AAA", "1.2.3 compareTo 1.2.1"+"1.2.3".compareTo("1.2.1"));
                Log.i("AAA", "A compareTo B"+"A".compareTo("B"));
            }
        });
        // -end of------------log 打印测试

        // -------------activity跳转+drawer导航测试
        Button b2=(Button)findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(MainActivity_from.this,DrawerActivity.class);
                // test seriliazable object transmission
                Bundle b1=new Bundle();
                b1.putSerializable("object1", DMapplication.getInstance().getObj1());
                b1.putSerializable("object2", DMapplication.getInstance().getObj2());
                mIntent.putExtras(b1);

                startActivity(mIntent);
            }
        });

        Button b_md=(Button)findViewById(R.id.button7);
        b_md.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(MainActivity_from.this,MatDesignPanelActivity.class);
                startActivity(mIntent);
            }
        });


        //jump with animations
        Button jump_anim1=(Button)findViewById(R.id.button4);
        Button jump_anim2=(Button)findViewById(R.id.button5);

        jump_anim1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(MainActivity_from.this,DrawerActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.page_bottom_in, R.anim.page_bottom_out);
            }
        });

        jump_anim2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(MainActivity_from.this,DrawerActivity.class);
                startActivity(mIntent);
                overridePendingTransition(R.anim.page_left_in, R.anim.page_right_out);
            }
        });
        // -------------end of activity跳转+drawer导航测试


        // -------------image+matric+offset 测试-----------
        Bitmap mp = BitmapUtil.overlapBitmapColor(BitmapFactory.decodeResource(
                        getResources(), R.drawable.bg_mutual_main_cursorimg),
                getResources().getColor(R.color.Red_));
        cursorIV = (ImageView) findViewById(R.id.imageView1);
        cursorIV.setImageBitmap(mp);

        cursorImgWidth = mp.getWidth();
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screentWidth = dm.widthPixels;
//        cursorOffset = (screentWidth / 2 - cursorImgWidth) / 2;
        cursorOffset = 20;
        Matrix matrix = new Matrix();
        matrix.postTranslate(cursorOffset, 0);
        cursorIV.setImageMatrix(matrix);
        // ------------end of -image+matric+offset 测试-----------



        mContext=this;
        // alert dialog 测试-----------------------------
        Button b3=(Button)findViewById(R.id.button3);

        //alert Dialog --利用反射修改测试
        alertDialog  = new AlertDialog.Builder(this)
                .setTitle("this is the title")
                .setMessage("this is the message")
                .setCancelable(false)
                .setOnKeyListener(            //onKeyListener有用么？？？？
                        new DialogInterface.OnKeyListener() {
                            @Override
                            public boolean onKey(
                                    DialogInterface dialog,
                                    int keyCode,
                                    KeyEvent event) {
                                if (keyCode == KeyEvent.KEYCODE_SEARCH) {
                                    return true;
                                }
                                return false;
                            }
                        }
                )
                .setNegativeButton("negative",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("REFLECTION", "negative do nothing");
                            }
                        })
                .setPositiveButton("positive",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.i("REFLECTION", "positive do nothing");
                                dialog.dismiss();
                            }
                        }).create();


        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.show();
            }
        });
        // end of alert dialog 测试-----------------------------------------------------

        //---------------------------------
        // start of change alert-dialog functionality -------click negative---does not exit----
        class ButtonHandler extends Handler
        {
            private WeakReference<DialogInterface> mDialog;
            public ButtonHandler(DialogInterface dialog)
            {
                mDialog=new WeakReference <DialogInterface>(dialog);
            }

            @Override
            public void handleMessage(Message msg)
            {
                switch (msg.what)
                {
                    case  DialogInterface.BUTTON_POSITIVE:
                    case  DialogInterface.BUTTON_NEGATIVE:
                    case  DialogInterface.BUTTON_NEUTRAL:
                        ((DialogInterface.OnClickListener) msg.obj).onClick(mDialog
                                .get(), msg.what);
                        break;
                }
            }
        }


        try
        {
            Field field  =  alertDialog.getClass().getDeclaredField("mAlert");
            field.setAccessible( true );
            //   获得mAlert变量的值
            Object obj  =  field.get(alertDialog);
            field  =  obj.getClass().getDeclaredField("mHandler");
            field.setAccessible( true );
            //   修改mHandler变量的值，使用新的ButtonHandler类
            field.set(obj,new ButtonHandler(alertDialog));
        } catch  (Exception e)
        {
            Log.e("REFLECTION",Log.getStackTraceString(e));
        }
        // END of change alert-dialog functionality -------click negative and does not exit----



        // anim 测试-----------------------------
        final Button b_anim=(Button)findViewById(R.id.button_anim);
        final ImageView iv_anim=(ImageView)findViewById(R.id.iv_anim_test);

        final TranslateAnimation animBack = new TranslateAnimation(0, 0, 0, 0);
        animBack.setDuration(20);

        //animation生命周期方法
        final Animation.AnimationListener animationOpenListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                b_anim.setText("--in animation...");
                Log.i("AAA","start");
            }
            @Override
            public void onAnimationRepeat(Animation animation) {
                Log.i("AAA","repeat");
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                b_anim.setText("start anim");
//                iv_anim.setAnimation(animBack);
//                animBack.startNow();
                Log.i("AAA","end");
            }
        };

        //新建animation
        final TranslateAnimation animOpen = new TranslateAnimation(0, 100, 0, 100);
//        animOpen.setRepeatCount(5);
        animOpen.setFillAfter(false);  // false --> go back to original position , true --> stay at final position
        animOpen.setDuration(1000);
        animOpen.setAnimationListener(animationOpenListener);


        b_anim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("AAA", "animation click start");
//                iv_anim.setAnimation(animOpen);
//                animOpen.startNow();
//                animOpen.start();  // NOT WORKING WELL
                iv_anim.startAnimation(animOpen);  // move  // this is OK

                Log.i("AAA", "animation click end");
            }
        });

        // end of anim 测试-----------------------------



        // 初始化数组测试----------------+位移运算优先级测试-------------
        TextView arrayLshow=(TextView)findViewById(R.id.editText_arraytest);
        //arrayLshow.setText("length:"+callbackBuffer.length); //不赋值无法获取长度
        callbackBuffer = new int[]{1, 2, 33};
//        arrayLshow.setText("length:" + callbackBuffer.length);

//        //优先级测试
//        arrayLshow.setText(""+(1<<1 + 1));
//        arrayLshow.setText(""+((1<<1) + 1));

        //boolean初始测试
        arrayLshow.setText("ok?"+isOK);  //初始false

//        arrayLshow.setText("length:");
        // end of初始化数组测试-----------------------------


        // 时间format测试-----------------------------
        TextView timeShow=(TextView)findViewById(R.id.editText_formatTest);
        DateFormat sdf= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        sdf.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String mDate="2009-12-01 08_12_23";

        try {
            Date dAlarmDate=sdf.parse(mDate);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        timeShow.setText("time:"+mDate);
        // end of  时间format测试-----------------------------


    }

    public void jump_leak(View view){
        Intent i1=new Intent(this,LeakActivity.class);
        startActivity(i1);
    }
    public void jump_leak2(View view){
        Intent i1=new Intent(this,LeakTest2Activity.class);
        startActivity(i1);
    }



    public void jump_tab(View view){
        Intent i1=new Intent(this,CustomViewActivity.class);
        startActivity(i1);
    }

    public void jump_tab2(View view){
        Intent i1=new Intent(this,CustomViewActivity2.class);
        startActivity(i1);
    }


    public void jump_alert(View view){
        Intent i1=new Intent(this,AlertTestActivity.class);
        startActivity(i1);
    }


    public void jump_performance(View view){
        PerformanceActivity.start(this);
    }


    public void jump_sql(View view){
        SQLiteActivity.start(this);
    }


    public void jump_email(View view){
        Email_IO_Activity.start(this);
    }

    public void jump_rxjava(View view){
        RXjavaActivity.start(this);
    }

    public void jump_volley(View view){
        VolleyActivity.start(this);
    }

    public void jump_baiduAPI(View view){
        BaiduAPIActivity.start(this);
    }

    public void jump_image(View view){
        ImageActivity.start(this);
    }

    public void jump_fb(View view){
        FbReboundActivity.start(this);
    }

    public void jump_sqlBoiler(View view){
        SQLiteBoilerActivity.start(this);
    }

    public void jump_butterKnife(View view){ButterKnifeActivity.start(this);}

    public void jump_mvp(View view){MVP_Activity.start(this);}

    public void jump_junit(View view){
        UnitTestWithDIActivity.start(this);}

    public void jump_write2disk(View view){
        Write2diskActivity.start(this);}

    public void jump_Audio(View view){
        AudioActivity.start(this);}

    public void jump_ActManger(View view){
        ActManagerActivity.start(this);}


    public void jump_SimCard(View view){
        SimCardActivity.goHere(this);}

    public void jump_ipc(View view){
        IPCactivity.start(this);}


    public void jump_lauchMode(View view){
        LaunchModeActivity.start(this);}


    public void jump_BCR(View view){
        BCRActivity.start(this);}


    public void jump_http(View view){
        OKhttpActivity.start(this);}

    public void jump_guava(View view){
        GuavaTestActivity.start(this);}

    public void jump_loading(View view){
        LoadingTestActivity.start(this);}

    public void jump_keyTest(View view){
        KeyEventActivity.start(this);}

    public void jump_switch(View view){
        WaterSwitchActivity.start(this);}


    public void jump_amap(View view){
        AmapActivity1.start(this);}


    public void jump_drag(View view){
        DrawTestActivity.start(this);}

    public void jump_vp_test(View view){
        ViewPagerTestActivity.start(this);}



    public void jump_vp_timer_picker(View view){
        TimePickerActivity.start(this);}


    public void jump_webview(View view){
        WebViewActivity.start(this);}
    public void jump_webview2(View view){
        WebViewActivity2.start(this);}


    public void jump_seekbar(View view){
        RangeSeekbarMainActivity.start(this);}

    public void jump_test_anim(View view){
        PropertyAnimTestActivity.start(this);}

    public void jump_test_time(View view){
        SelectTimeActivity.start(this);}


    public void jump_test_interactive(View view){
        InteractiveActivity.start(this);}


    public void jump_h_scroll(View view){
        HorizontalScrollActivity.start(this);}

    public void jump_test3d_scroll(View view){
        Test3dActivity.start(this);}


    public void jump_fling_test(View v){
        FlingTestActivity.start(this);
    }

    public void jump_bessel_test(View v){
        BesselActivity.start(this);
    }


    public void jump_galaryTest(View v){
        CyclicGalaryActivity.start(this);
    }

    public void jump_tabTest(View v){
        TestTabActivity.start(this);
    }

    public void jump_genericTest(View v){
        GenericTestActivity.start(this);
    }


    public void jump_gesture_pwdt(View v){
        GesturePassWordActivity.start(this);
    }


    public void jump_vector(View view){
        VectorDrawableActivity.start(this);}

    public void jump_vertical_scroll(View view){
        VerticalScrollActivity.start(this);}


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_from, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.appbaractivity2) {
            Intent mIntent=new Intent(MainActivity_from.this,ActionBarActivity2.class);
            startActivity(mIntent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
