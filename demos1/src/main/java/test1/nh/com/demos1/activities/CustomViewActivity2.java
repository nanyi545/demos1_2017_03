package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.transparent_activity.TransparentActivity;
import test1.nh.com.demos1.customView.LoadView;
import test1.nh.com.demos1.customView.PieChart;

public class CustomViewActivity2 extends AppCompatActivity {


    public static void start(Context c){
        Intent i=new Intent(c,CustomViewActivity2.class);
        c.startActivity(i);
    }


    DisplayMetrics dm=new DisplayMetrics();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view2);

        PieChart pie= (PieChart) findViewById(R.id.test_pie);

        int[] percentages={10,20,30,50,66,70,100};
        int[] colors={
                ContextCompat.getColor(CustomViewActivity2.this,R.color.pie_chart0),
                ContextCompat.getColor(CustomViewActivity2.this,R.color.pie_chart1),
                ContextCompat.getColor(CustomViewActivity2.this,R.color.pie_chart2),
                ContextCompat.getColor(CustomViewActivity2.this,R.color.pie_chart3),
                ContextCompat.getColor(CustomViewActivity2.this,R.color.pie_chart4),
                ContextCompat.getColor(CustomViewActivity2.this,R.color.pie_chart5),
                ContextCompat.getColor(CustomViewActivity2.this,R.color.pie_chart6),
        };

        pie.setData(percentages,colors,19898);

        pie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(CustomViewActivity2.this, TransparentActivity.class);
                CustomViewActivity2.this.startActivity(i);
            }
        });


    }

    public void startLoad(View v){
//        getWindowManager().getDefaultDisplay().getMetrics(dm);
//        Log.i("ccc",""+dm.density+"    "+(75*dm.density));
        LoadView.startLoading2(this);
    }
}
