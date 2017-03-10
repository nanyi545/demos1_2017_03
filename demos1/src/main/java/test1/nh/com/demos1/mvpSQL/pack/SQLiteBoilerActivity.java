package test1.nh.com.demos1.mvpSQL.pack;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import test1.nh.com.demos1.R;
import test1.nh.com.demos1.mvpSQL.pack.presenters.SQLpresenter1;
import test1.nh.com.demos1.mvpSQL.pack.views.IViewSQL;
import test1.nh.com.demos1.utils.sqlite.Person4DB;

public class SQLiteBoilerActivity extends AppCompatActivity {

    public static void start(Context context){
        final Intent intent=new Intent(context,SQLiteBoilerActivity.class);
        context.startActivity(intent);
    }




    IViewSQL myViewSQL;
    @Bind(R.id.et_name) EditText et_name;
    @Bind(R.id.et_age) EditText et_age;
    @Bind(R.id.mlp_load) com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar load_progBar;
    @Bind(R.id.mlp_add) com.lsjwzh.widget.materialloadingprogressbar.CircleProgressBar add_progBar;

    SQLpresenter1 log_presenter;

    @Bind(R.id.b_load) Button b_load;
    @Bind(R.id.b_add) Button b_add;

    @OnClick(R.id.b_load) void loadDBtolog(){
        log_presenter.refreshView();
    }
    @OnClick(R.id.b_add) void addPersonTodb(){
        log_presenter.addPerson();
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sqlite_boiler);
        ButterKnife.bind(this);


        myViewSQL=new IViewSQL() {
            @Override
            // print to logcat
            public void refreshPersons(List<Person4DB> list) {
                Iterator<Person4DB> i1=list.iterator();
                int count=0;
                while (i1.hasNext()){
                    Log.i("AAA",""+count+"th entry is:"+i1.next().toString());
                    count=count+1;
                }
            }

            @Override
            public Person4DB getPerson() {
                return new Person4DB(Integer.parseInt(et_age.getText().toString()),et_name.getText().toString());
            }

            @Override
            public void setLoading(boolean isLoading) {
                if (isLoading){
                    load_progBar.setVisibility(View.VISIBLE);
                    b_load.setText("");
                }
                else{
                    load_progBar.setVisibility(View.INVISIBLE);
                    b_load.setText("LOAD TO LOG");
                }
            }


            @Override
            public void setPersonLoading(boolean isLoading) {
                if (isLoading){
                    add_progBar.setVisibility(View.VISIBLE);
                    b_add.setText("");
                }
                else{
                    add_progBar.setVisibility(View.INVISIBLE);
                    b_add.setText("add to DB");
                }
            }
        };

        log_presenter=new SQLpresenter1(this,myViewSQL);

    }



}
