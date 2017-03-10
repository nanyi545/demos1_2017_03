package test1.nh.com.demos1.activities.matDesign;

import android.annotation.TargetApi;
import android.graphics.Outline;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;

import test1.nh.com.demos1.R;

public class OutLineActivity extends AppCompatActivity {

    ImageView iv_bask;
    ImageView iv_bask2;
    ImageView iv_bask3;
    ImageView iv_bask4;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_out_line);
        iv_bask= (ImageView) findViewById(R.id.imageView8);
        iv_bask2= (ImageView) findViewById(R.id.imageView9);
        iv_bask3= (ImageView) findViewById(R.id.imageView10);
        iv_bask4= (ImageView) findViewById(R.id.imageView11);

        // if api>=21
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {

            // create ViewOutlineProvider anonymous classes
            ViewOutlineProvider myOutlineProvider=new ViewOutlineProvider(){
                @Override
                @TargetApi(21)
                public void getOutline(View view, Outline outline) {
                    outline.setOval(0, 0, view.getWidth(), view.getHeight());
                }
            };

            ViewOutlineProvider myOutlineProvider2=new ViewOutlineProvider(){
                @Override
                @TargetApi(21)
                public void getOutline(View view, Outline outline) {
                    outline.setRect(0, 0, view.getWidth(), view.getHeight());
                }
            };

            ViewOutlineProvider myOutlineProvider3=new ViewOutlineProvider(){
                @Override
                @TargetApi(21)
                public void getOutline(View view, Outline outline) {
                    outline.setRoundRect(0, 0, view.getWidth(), view.getHeight(), 33f);
                }
            };

            iv_bask.setOutlineProvider(myOutlineProvider);// set outline
            iv_bask.invalidate();
            iv_bask.setClipToOutline(true);// clip View to outline

            // only when a view's outline is set the shadow from elevation can bee seen
            iv_bask2.setOutlineProvider(myOutlineProvider);  // circle outline cast circle shadow
            iv_bask3.setOutlineProvider(myOutlineProvider2); // rect outline cast rect shadow
            iv_bask4.setOutlineProvider(myOutlineProvider3); // RoundRect outline cast RoundRect shadow
        }






    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_out_line, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
