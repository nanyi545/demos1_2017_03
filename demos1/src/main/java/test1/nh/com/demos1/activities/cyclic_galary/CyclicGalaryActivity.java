package test1.nh.com.demos1.activities.cyclic_galary;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.roughike.bottombar.BottomBar;

import java.lang.reflect.Field;

import test1.nh.com.demos1.R;

public class CyclicGalaryActivity extends AppCompatActivity {


    public static void start(Context c){
        Intent i=new Intent(c,CyclicGalaryActivity.class);
        c.startActivity(i);
    }



    FrameLayout fragmentHolder;
    FragmentFirstPage fragment1;
    FragmentSecond fragment2;
    FragmentThird fragment3;
    FragmentFour fragment4;
    Fragment current;
    String[] tags = {"frag1","frag2","frag3","frag4"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cyclic_galary);


        fragmentHolder= (FrameLayout) findViewById(R.id.home_fragment);


        fragment1=new FragmentFirstPage();
        fragment2=new FragmentSecond();
        fragment3=new FragmentThird();
        fragment4=new FragmentFour();

        current=fragment1;

        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.add(R.id.home_fragment, fragment1,tags[0]).commit();

        initRavBottomNavi();
//        initBottomNavigation();
//        initBottomNavigationRoughike();

    }




    private void switchContent( Fragment to, String tag) {
        if (current != to) {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            if (!to.isAdded()) {
                transaction.hide(current).add(R.id.home_fragment, to,tag).commit(); // 隐藏当前的fragment，add下一个到Activity中
            } else {
                transaction.hide(current).show(to).commit(); // 隐藏当前的fragment，显示下一个
            }
            current = to;
        }
    }




    private void initRavBottomNavi(){
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation);


        bottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);
        bottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_RIPPLE);

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0:switchContent(fragment1,tags[0]);break;
                    case 1:switchContent(fragment2,tags[1]);break;
                    case 2:switchContent(fragment3,tags[2]);break;
                    case 3:switchContent(fragment4,tags[3]);break;
                    default:break;
                }
            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });

        BadgeItem numberBadgeItem = new BadgeItem()
                .setBackgroundColorResource(R.color.Red500)
                .setText("15")
                .setHideOnSelect(false);

//        bottomNavigationBar
//                .addItem(new BottomNavigationItem(R.drawable.ic_action_pan_up, "首页").setInactiveIconResource(R.drawable.ic_action_pan_down).setActiveColorResource(R.color.Blue400))
//                .addItem(new BottomNavigationItem(R.drawable.ic_action_pan_down, "订单").setInactiveIconResource(R.drawable.ic_action_pan_up).setBadgeItem(numberBadgeItem).setActiveColorResource(R.color.Teal400))
//                .addItem(new BottomNavigationItem(R.drawable.ic_action_pan_left, "发现").setInactiveIconResource(R.drawable.ic_action_pan_right).setActiveColorResource(R.color.Purple400))
//                .addItem(new BottomNavigationItem(R.drawable.ic_action_zoom_out, "我的").setInactiveIconResource(R.drawable.ic_action_zoom_in).setActiveColorResource(R.color.Orange400))
//                .initialise();
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.drawable.ic_action_pan_up, "首页").setInactiveIconResource(R.drawable.ic_action_pan_down))
                .addItem(new BottomNavigationItem(R.drawable.ic_action_pan_down, "订单").setInactiveIconResource(R.drawable.ic_action_pan_up).setBadgeItem(numberBadgeItem))
                .addItem(new BottomNavigationItem(R.drawable.ic_action_pan_left, "发现").setInactiveIconResource(R.drawable.ic_action_pan_right))
                .addItem(new BottomNavigationItem(R.drawable.ic_action_zoom_out, "我的").setInactiveIconResource(R.drawable.ic_action_zoom_in))
                .setActiveColor(R.color.Gray500)
                .initialise();

    }



    private void initBottomNavigationRoughike(){
        BottomBar bottomBar = (BottomBar) findViewById(R.id.bottom_navigation);

//        BottomBarTab nearby = bottomBar.getTabWithId(R.id.tab_2);
//        nearby.setBadgeCount(5);

// Remove the badge when you're done with it.
//        nearby.removeBadge();


    }



    // need design lib 25
//    private void initBottomNavigation(){
//        BottomNavigationView bottomNavigationView= (BottomNavigationView) findViewById(R.id.bottom_navigation);
//
//
//        Field f = null;
//        try {
//            f = bottomNavigationView.getClass().getDeclaredField("mMenuView");
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//        f.setAccessible(true);
//        BottomNavigationMenuView menuView=null;
//        try {
//            menuView = (BottomNavigationMenuView) f.get(bottomNavigationView); //IllegalAccessException
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//
//        try {
//            f=menuView.getClass().getDeclaredField("mButtons");
//        } catch (NoSuchFieldException e) {
//            e.printStackTrace();
//        }
//        f.setAccessible(true);
//        BottomNavigationItemView[] mButtons=null;
//        try {
//            mButtons = (BottomNavigationItemView[]) f.get(menuView); //IllegalAccessException
//        } catch (IllegalAccessException e) {
//            e.printStackTrace();
//        }
//
//        for(int i=0;i<mButtons.length;i++){
//            mButtons[i].setShiftingMode(false);
//            mButtons[i].setChecked(true);
//            mButtons[i].setChecked(false);
//        }
//        mButtons[0].setChecked(true);
//
//
//        bottomNavigationView.setOnNavigationItemSelectedListener(
//                new BottomNavigationView.OnNavigationItemSelectedListener() {
//                    @Override
//                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                        switch (item.getItemId()) {
//                            case R.id.action_favorites:
//                                switchContent(fragment1,tags[0]);
//                                break;
//                            case R.id.action_schedules:
//                                switchContent(fragment2,tags[1]);
//                                break;
//                            case R.id.action_music:
//                                switchContent(fragment3,tags[2]);
//                                break;
//                            case R.id.action_my:
//                                switchContent(fragment4,tags[3]);
//                                break;
//                        }
//                        return false;
//                    }
//                });
//
//    }

}
