package test1.nh.com.demos1.activities.generic_test;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;

import test1.nh.com.demos1.R;

public class GenericTestActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,GenericTestActivity.class);
        c.startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_generic_test);
//        Box<Integer> intBox=new Box(new Integer(10));
//        intBox.inspect( new Float(0.8f) );

        ExtendedBox<Integer,Activity> extBox=new ExtendedBox(new Integer(10),this);
        extBox.inspect( new Float(0.8f) );

        RecyclerView test= (RecyclerView) findViewById(R.id.generic_test_rv);
        test.setLayoutManager(new LinearLayoutManager(this));


        ArrayList<String> data=new ArrayList();
        data.add("123123");data.add("adfasd");data.add("asdfa");data.add("asdfads");data.add("dafd");
        GenericAdapter<String> adapter=new GenericAdapter(data, new GenericAdapter.Formatter<String>() {
            @Override
            public String format(String o) {
                return "lala  "+o;
            }
        });
        test.setAdapter(adapter);



//        ArrayList<Integer> data=new ArrayList();
//        data.add(1);data.add(12);data.add(25);data.add(66);data.add(9090);
//        GenericAdapter2 adapter=new GenericAdapter2( new GenericAbsAdapter.Formatter<Integer>() {
//            @Override
//            public String format(Integer t) {
//                return "integer:"+t;
//            }
//        },data);



//        ArrayList<String> data2=new ArrayList();
//        data2.add("aasdf");data2.add("dsafdf");data2.add("dasfdsaf");data2.add("adfads");data2.add("sadfasd");
//        GenericAdapter2<String> adapter=new GenericAdapter2( new GenericAbsAdapter.Formatter<String>() {
//            @Override
//            public String format(String t) {
//                return "str:"+t;
//            }
//        },data2);




//        test.setAdapter(adapter);


    }
}
