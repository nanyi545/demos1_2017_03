package test1.nh.com.demos1.activities.matDesign;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.transition.Slide;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import test1.nh.com.demos1.R;

public class MatDesignPanelActivity extends AppCompatActivity {

    Button b_recyclerView1;
    Button b_elevation;
    Button b_recyclerView2;
    Button b_recyclerView3;
    Button b_cardView1;
    Button b_tint;
    Button b_outline;
    Button b_vpDemo;

    Button b_transitionDemo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mat_design_panel);

        //recycler view 1: just like list view
        b_recyclerView1=(Button)findViewById(R.id.button9);
        b_recyclerView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent = new Intent(MatDesignPanelActivity.this, RecyclerViewActivity.class);
                startActivity(mIntent);
            }
        });

        //test of elevation
        b_elevation=(Button)findViewById(R.id.button8);
        b_elevation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(MatDesignPanelActivity.this,ElevationActivity.class);
                startActivity(mIntent);
            }
        });

        //recycler view 2: grid view
        b_recyclerView2=(Button)findViewById(R.id.button10);
        b_recyclerView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(MatDesignPanelActivity.this,RecyclerViewActivity2.class);
                startActivity(mIntent);
            }
        });


        // recycler view 3: drag-----> drag & drop and swipe-to-dismiss
        b_recyclerView3=(Button)findViewById(R.id.button22);
        b_recyclerView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(MatDesignPanelActivity.this,RecyclerViewActivity3.class);
                startActivity(mIntent);
            }
        });


        //card view 1: what is card View
        b_cardView1=(Button)findViewById(R.id.button11);
        b_cardView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(MatDesignPanelActivity.this,CardViewActivity.class);
                startActivity(mIntent);
            }
        });

        // tinting----->change color--
        b_tint=(Button)findViewById(R.id.button12);
        b_tint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(MatDesignPanelActivity.this,TintActivity.class);
                startActivity(mIntent);
            }
        });

        // outline----->
        b_outline=(Button)findViewById(R.id.button13);
        b_outline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(MatDesignPanelActivity.this,OutLineActivity.class);
                startActivity(mIntent);
            }
        });


        // view pager----->
        b_vpDemo=(Button)findViewById(R.id.button16);
        b_vpDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(MatDesignPanelActivity.this,VPActivity.class);
                startActivity(mIntent);
            }
        });


        //-----transition ----->
        setupTransition();

        b_transitionDemo=(Button)findViewById(R.id.button23);
        b_transitionDemo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mIntent=new Intent(MatDesignPanelActivity.this,TransitionActivity1.class);
                transitionTo(mIntent);
            }
        });

    }

    @TargetApi(21)
    private void setupTransition(){
        Slide slide = new Slide();
        slide.setDuration(1000);
        getWindow().setExitTransition(slide);
    }

    @TargetApi(21)
    void transitionTo(Intent i) {
        final Pair<View, String>[] pairs = TransitionHelper.createSafeTransitionParticipants(this, true);
        ActivityOptionsCompat transitionActivityOptions = ActivityOptionsCompat.makeSceneTransitionAnimation(this, pairs);
        startActivity(i, transitionActivityOptions.toBundle());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mat_design_panel, menu);
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
