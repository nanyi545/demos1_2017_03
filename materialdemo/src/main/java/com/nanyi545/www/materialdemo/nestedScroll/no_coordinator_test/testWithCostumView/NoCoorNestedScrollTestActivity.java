package com.nanyi545.www.materialdemo.nestedScroll.no_coordinator_test.testWithCostumView;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.nanyi545.www.materialdemo.R;


/**
 *   test of  nested scroll protocol
 *     using custom views
 */

public class NoCoorNestedScrollTestActivity extends AppCompatActivity {


    public static void start(Context c){
        Intent i=new Intent(c,NoCoorNestedScrollTestActivity.class);
        c.startActivity(i);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_no_coor_nested_scroll_test);
    }


}
