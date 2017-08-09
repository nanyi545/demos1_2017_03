package test1.nh.com.demos1.activities.state_pattern;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import test1.nh.com.demos1.R;

public class StatePatternActivity extends AppCompatActivity {



    public static void start(Context c){
        Intent i=new Intent(c,StatePatternActivity.class);
        c.startActivity(i);
    }




    private abstract class ViewState{
        abstract void updateView();
    }

    private class State0 extends ViewState {
        @Override
        void updateView() {
            tv1.setText("______");tv2.setText("((((");tv3.setText("*****");tv4.setText("xxxxxxx");
        }
    }
    private class State1 extends ViewState {
        @Override
        void updateView() {
            tv1.setText("state1");tv2.setText("hehe");tv3.setText("haha");tv4.setText("heihei");
        }
    }

    private class State2 extends ViewState {
        @Override
        void updateView() {
            tv1.setText("yyyy");tv2.setText("state2");tv3.setText("haha");tv4.setText("heihei");
        }
    }

    private class State3 extends ViewState {
        @Override
        void updateView() {
            tv1.setText("!!!!");tv2.setText("~~~~");tv3.setText("state3");tv4.setText("heihei");
        }
    }

    private class State4 extends ViewState {
        @Override
        void updateView() {
            tv1.setText("jjjjj");tv2.setText("hehe");tv3.setText("haha");tv4.setText("state4");
        }
    }



    TextView tv1,tv2,tv3,tv4;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_state_pattern);
        tv1= (TextView) findViewById(R.id.tv1);
        tv2= (TextView) findViewById(R.id.tv2);
        tv3= (TextView) findViewById(R.id.tv3);
        tv4= (TextView) findViewById(R.id.tv4);
        setState(new State0());
    }






    ViewState state;




    private void setState(ViewState state){
        this.state=state;
        this.state.updateView();
    }



    public void changeState(View view){
        int id=view.getId();
        switch(id) {
            case R.id.btn1 :
                setState(new State1());
                break;
            case R.id.btn2 :
                setState(new State2());
                break;
            case R.id.btn3 :
                setState(new State3());
                break;
            case R.id.btn4 :
                setState(new State4());
                break;
        }
    }



}
