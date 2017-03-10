package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test1.nh.com.demos1.R;
import test1.nh.com.demos1.mvpDemo.pack.IDemoModel;
import test1.nh.com.demos1.mvpDemo.pack.IDemoView;
import test1.nh.com.demos1.mvpDemo.pack.MVPpresenterDemo;

public class MVP_Activity extends AppCompatActivity {

    public static void start(Context context) {
        final Intent intent = new Intent(context, MVP_Activity.class);
        context.startActivity(intent);
    }

    @Bind(R.id.button_save) Button b_save;
    @Bind(R.id.button_load) Button b_load;
    @Bind(R.id.et_id) EditText et_id;
    @Bind(R.id.et_fn) EditText et_fn;
    @Bind(R.id.et_ln) EditText et_ln;

    @OnClick(R.id.button_save) void doSave(){
        myDemoPresenter.saveINFO();
    }
    @OnClick(R.id.button_load) void doLoad(){
        myDemoPresenter.loadInfo();
    }


    IDemoView myDemoView;
    IDemoModel myDemoModel;
    MVPpresenterDemo myDemoPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvp_);
        ButterKnife.bind(this);


        myDemoView=new IDemoView(){
            @Override public String getFirstName_view() { return et_fn.getText().toString();}
            @Override public int getId_view() { return Integer.parseInt(et_id.getText().toString());}
            @Override public void setFirstName_view(String firstName) {et_fn.setText(firstName);}
            @Override public String getLastName_view() {return et_ln.getText().toString(); }
            @Override public void setId_view(int id) {et_id.setText(""+id);}
            @Override public void setLastName_view(String lastName) {et_ln.setText(lastName);}
        };


        myDemoModel=new IDemoModel(){
            SharedPreferences demoPreferences = getSharedPreferences("demoPreference",Context.MODE_PRIVATE);
            @Override public int get_Id() { return demoPreferences.getInt("id",0);}
            @Override public String getFirstName() {return demoPreferences.getString("firstName","");}
            @Override public String getLastName() {return demoPreferences.getString("lastName","");}
            @Override public void saveFirstName(String firstName) {demoPreferences.edit().putString("firstName",firstName).apply();}
            @Override public void saveId(int id) {demoPreferences.edit().putInt("id",id).apply();}
            @Override public void saveLastName(String lastName) {demoPreferences.edit().putString("lastName",lastName).apply();}
        };


        myDemoPresenter=new MVPpresenterDemo(myDemoView,myDemoModel);







    }







}
