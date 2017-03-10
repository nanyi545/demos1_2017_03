package test1.nh.com.demos1.activities.interactive_chart;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import test1.nh.com.demos1.R;

public class InteractiveActivity extends Activity {

    public static void start(Context c){
        Intent i=new Intent(c,InteractiveActivity.class);
        c.startActivity(i);
    }

    private InteractiveLineGraphView mGraphView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_interactive);
        mGraphView = (InteractiveLineGraphView) findViewById(R.id.chart);

    }

    public void action1(View v){
        mGraphView.zoomIn();
    }
    public void action2(View v){
        mGraphView.zoomOut();
    }
    public void action3(View v){
        mGraphView.panLeft();
    }
    public void action4(View v){
        mGraphView.panRight();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main1, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_zoom_in:
                mGraphView.zoomIn();
                return true;

            case R.id.action_zoom_out:
                mGraphView.zoomOut();
                return true;

            case R.id.action_pan_left:
                mGraphView.panLeft();
                return true;

            case R.id.action_pan_right:
                mGraphView.panRight();
                return true;

            case R.id.action_pan_up:
                mGraphView.panUp();
                return true;

            case R.id.action_pan_down:
                mGraphView.panDown();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
