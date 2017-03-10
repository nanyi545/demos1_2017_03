package test1.nh.com.themeapp.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Switch;

import com.kyleduo.switchbutton.SwitchButton;

import test1.nh.com.themeapp.R;
import test1.nh.com.themeapp.fragements.NavigationDrawerFragment;

public class DrawerActivityRipple extends AppCompatActivity implements NavigationDrawerFragment.NavigationDrawerCallbacks {




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }


    @Override
    public void onNavigationDrawerItemSelected(int position) {
        Log.i("AAA","clicked"+position);
    }

    private Context mContext;
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private String mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_activity_ripple);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mContext=this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.syncState();

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
//        mTitle = getTitle();
        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout),toolbar);



        Switch sw1=(Switch)findViewById(R.id.open);

//        android.support.v7.widget.SwitchCompat sw2=

        SwitchButton sb_md=(SwitchButton)findViewById(R.id.sb_md);
//        sb_md.setThumbColorRes(R.color.Blue500);
//        sb_md.setBackColorRes(R.color.Red100);
//        sb_md.setTintColor(R.color.Purple100);
    }
}
