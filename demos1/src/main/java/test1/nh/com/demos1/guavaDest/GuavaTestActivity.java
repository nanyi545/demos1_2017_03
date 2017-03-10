package test1.nh.com.demos1.guavaDest;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import test1.nh.com.demos1.R;

import static com.google.common.base.Preconditions.checkNotNull;

public class GuavaTestActivity extends AppCompatActivity {


    public static void start(Context c){
        Intent i1=new Intent(c,GuavaTestActivity.class);
        c.startActivity(i1);
    }

    Object o1=null;
    Object o2=new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guava_test);

        testCheckNull();


    }



    private void testCheckNull() {
        Object o;
//        o = checkNotNull(o1, "object o1 cannot be null"); // execution of this pat will throw a null pointer exception
        o = checkNotNull(o2, "object o2 cannot be null");
    }


}
