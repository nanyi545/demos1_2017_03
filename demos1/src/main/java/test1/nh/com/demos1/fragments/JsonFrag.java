package test1.nh.com.demos1.fragments;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import test1.nh.com.demos1.R;
import test1.nh.com.demos1.activities.DrawerActivity;

/**
 * Created by Administrator on 15-9-30.
 */
public class JsonFrag extends Fragment {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    //empty constructor
    public JsonFrag(){
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static JsonFrag newInstance(int sectionNumber) {
        JsonFrag fragment = new JsonFrag();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    @TargetApi(11)
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // get section id from intent
        // getArg--return the Bundle
        int section_number=getArguments().getInt(ARG_SECTION_NUMBER);

        View rootView = inflater.inflate(R.layout.fragment_json_drawer, container, false);

        TextView output = (TextView) rootView.findViewById(R.id.textView1);

        // ----json object directly from string-----
        String strJson=
        "{\"Employee\" :[{\"id\":\"01\",\"name\":\"Gopal Varma\",\"salary\":\"500000\"},{ \"id\":\"02\",\"name\":\"Sairamkrishna\",\"salary\":\"500000\"}, {\"id\":\"03\",\"name\":\"Sathish kallakuri\",\"salary\":\"600000\"} ]}";
        String data = "";
        try {
            JSONObject jsonRootObject = new JSONObject(strJson);

            //Get the instance of JSONArray that contains JSONObjects
            // incase of no key:  optJSONArray--returns null, getJSONArray--throws exception
            JSONArray jsonArray = jsonRootObject.optJSONArray("Employee");

            //Iterate the jsonArray and print the info of JSONObjects
            for(int i=0; i < jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);

                int id = Integer.parseInt(jsonObject.optString("id").toString());
                String name = jsonObject.optString("name").toString();
                float salary = Float.parseFloat(jsonObject.optString("salary").toString());

                data += "Node"+i+" : \n id= "+ id +" \n Name= "+ name +" \n Salary= "+ salary +" \n ";
            }
            output.setText(data);
        } catch (JSONException e) {e.printStackTrace();}



        // ----json string from SD card-----
        File sdcard = Environment.getExternalStorageDirectory();
        //Get the text file
        File file = new File(sdcard,"jsonObj1");

        //Read text from file
        StringBuilder text = new StringBuilder();

        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            //You'll need to add proper error handling here
            Toast.makeText(getActivity(),"error in reading the file",Toast.LENGTH_LONG).show();
        }

        TextView output2 = (TextView) rootView.findViewById(R.id.textView_fromsd);
//        output2.setText(text);
        String jsonStrFromSD=new String(text);

        try {
            JSONObject jsonRootObject2 = new JSONObject(jsonStrFromSD);
            String jsonFromSd=""+jsonRootObject2.optString("name")+jsonRootObject2.optString("age")+jsonRootObject2.optString("isNBA");
            output2.setText(jsonFromSd);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //如果attach到drawerActivity上 设置activity label
        try {    //  called by DrawerActivity
            ((DrawerActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        } catch (Exception e) {
            e.printStackTrace();
//            Log.i("AAA", "Fragment called not by DrawerActivity");
        }

    }





}
