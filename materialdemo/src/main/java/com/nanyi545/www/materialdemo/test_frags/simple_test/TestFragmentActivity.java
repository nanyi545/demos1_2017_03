package com.nanyi545.www.materialdemo.test_frags.simple_test;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.nanyi545.www.materialdemo.R;

public class TestFragmentActivity extends AppCompatActivity implements TestFragment2.OnFragmentInteractionListener {


    public static void start(Context c){
        Intent i=new Intent(c,TestFragmentActivity.class);
        c.startActivity(i);
    }


    Fragment f1;
    String[] tags;

    Fragment f2;
    Fragment f3;


    /**
     *   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,f1,tags[0]).commit();
     *
     *   under this mode :  upon each replace() call
     *
     *    new Fragment :  onCreate()
     *    old Fragment :  onDestroyView()
     *    old Fragment :  onDestroy()
     *    old Fragment :  onDetach()
     *    new Fragment :  onCreateView()
     *
     *    ** only the visible fragment is added
     */
    private static final int REPLACE_MODE=1;

    /**
     *   getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,f1,tags[0]).addToBackStack(null).commit();
     *   ---------------------------------------
     *       @Override
            public void onBackPressed() {
                if (getFragmentManager().getBackStackEntryCount() > 0) {
                    getFragmentManager().popBackStack();
                } else {
                    super.onBackPressed();
                }
            }

     *
     *   under this mode :  upon each replace() call
     *
     *    new Fragment :  onCreate()  --->  optional , only called the first time
     *    old Fragment :  onDestroyView()
     *    new Fragment :  onCreateView()
     *
     *
     */
    private static final int REPLACE_ADD_TO_BACKSTACK_MODE=2;


    /**
     *              if (f1.isAdded()){
                        getSupportFragmentManager().beginTransaction().show(f1).commit();
                        getSupportFragmentManager().beginTransaction().hide(f2).commit();
                        getSupportFragmentManager().beginTransaction().hide(f3).commit();
                    } else {
                        getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,f1,tags[0]).commit();
                    }

     *   under this mode :  upon each replace() call
     *
     *    new Fragment :  onCreate()      --->  optional , only called the first time
     *    new Fragment :  onCreateView()  --->  optional , only called the first time
     *
     */
    private static final int SHOW_MODE=3;


    /**
     *   http://stackoverflow.com/questions/13149446/android-fragments-when-to-use-hide-show-or-add-remove-replace
     *
     *. If you use a FragmentTransaction to hide the fragment, then it can still be in the running state of its lifecycle, but its UI has been detached from the window so it's no longer visible.
     * So you could technically still interact with the fragment and reattach its UI later you need to.
     *
     * If you replace the fragment, the you are actually pulling it out of the container and it will go through all of the teardown events in the lifecycle
     * (onPause, onStop, etc) and if for some reason you need that fragment again you would have to insert it back into the container and let it run through all of its initialization again.
     *
     */

//    private int switchMode=REPLACE_MODE;
//    private int switchMode=REPLACE_ADD_TO_BACKSTACK_MODE;
    private int switchMode=SHOW_MODE;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_fragment);


        drawFullScreenUnderStatusBar();


        tags=new String[3];
        tags[0]="f1";
        tags[1]="f2";
        tags[2]="f3";
        f1=BlankFragment.newInstance(tags[0],R.drawable.grad_teal);
        f2=TestFragment2.newInstance(tags[1],R.color.Red100);
        f3=TestFragment2.newInstance(tags[2],R.color.Red300);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,f1,tags[0]).commit();


        TextView btn1= (TextView) findViewById(R.id.btn_frag1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switchMode==REPLACE_MODE){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,f1,tags[0]).commit();
                }
                if (switchMode==REPLACE_ADD_TO_BACKSTACK_MODE){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,f1,tags[0]).addToBackStack(null).commit();
                }
                if (switchMode==SHOW_MODE){
                    if (f1.isAdded()){
                        getSupportFragmentManager().beginTransaction().show(f1).commit();
                        getSupportFragmentManager().beginTransaction().hide(f2).commit();
                        getSupportFragmentManager().beginTransaction().hide(f3).commit();
                    } else getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,f1,tags[0]).commit();
                }

            }
        });
        TextView btn2= (TextView) findViewById(R.id.btn_frag2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchMode==REPLACE_MODE){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,f2,tags[1]).commit();
                }

                if (switchMode==REPLACE_ADD_TO_BACKSTACK_MODE){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,f2,tags[1]).addToBackStack(null).commit();
                }
                if (switchMode==SHOW_MODE){
                    if (f2.isAdded()){
                        getSupportFragmentManager().beginTransaction().show(f2).commit();
                        getSupportFragmentManager().beginTransaction().hide(f1).commit();
                        getSupportFragmentManager().beginTransaction().hide(f3).commit();

                    } else getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,f2,tags[1]).commit();
                }
                
            }
        });
        TextView btn3= (TextView) findViewById(R.id.btn_frag3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (switchMode==REPLACE_MODE){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,f3,tags[2]).commit();
                }
                if (switchMode==REPLACE_ADD_TO_BACKSTACK_MODE){
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_holder,f3,tags[2]).addToBackStack(null).commit();
                }
                if (switchMode==SHOW_MODE){
                    if (f3.isAdded()){
                        getSupportFragmentManager().beginTransaction().show(f3).commit();
                        getSupportFragmentManager().beginTransaction().hide(f2).commit();
                        getSupportFragmentManager().beginTransaction().hide(f1).commit();
                    } else getSupportFragmentManager().beginTransaction().add(R.id.fragment_holder,f3,tags[2]).commit();
                }


            }
        });

        TextView btn4= (TextView) findViewById(R.id.btn4);
        btn4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("aaa","f1.isAdded():"+f1.isAdded()+"  f2.isAdded():"+f2.isAdded()+"   f3.isAdded():"+f3.isAdded());   //    if USE_REPLACE ... only one fragment is added at a time ....
            }
        });

    }



    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }




    private void drawFullScreenUnderStatusBar() {
        if  (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE| View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT  ) {
            Window window = getWindow();
            //设置StatusBar为透明显示,需要在setContentView之前完成操作
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                    WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }


    @Override
    public void onFragmentInteraction(String interaction) {
        Log.i("aaa","call from fragment:"+interaction);
    }


}
