package test1.nh.com.demos1.activities.matDesign;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;

import test1.nh.com.demos1.R;

public class TintActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tint);
        setTitle("Tinting NoGood before API21");

        Resources res = getResources();
        Drawable image= res.getDrawable(R.drawable.ic_dialpad_black_18dp);
        int color = res.getColor(R.color.Red400);
        image.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        ImageView tv4= (ImageView) findViewById(R.id.imageView4);
        tv4.setBackgroundDrawable(image);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tint, menu);
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
