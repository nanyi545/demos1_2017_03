package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.TextView;

import com.baidu.apistore.sdk.ApiCallBack;
import com.baidu.apistore.sdk.ApiStoreSDK;
import com.baidu.apistore.sdk.network.Parameters;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import pl.droidsonroids.gif.GifImageView;
import test1.nh.com.demos1.R;

public class BaiduAPIActivity extends AppCompatActivity {

    public static void start(Context context) {
        final Intent intent = new Intent(context, BaiduAPIActivity.class);
        context.startActivity(intent);
    }

    TextView mTextView;
    String[] urls=new String[2000];
    ArrayList<String> url_list=new ArrayList();

    WebView webView;
    int url_id;

    GifImageView gifImageView;
    int gif_url_id;

    public void loadURL(View view){
        webView.loadUrl(urls[url_id]);
        url_id=url_id+1;
    }


    public void loadURL_fromList(View view){
        Uri uri=Uri.parse(url_list.get(gif_url_id));
        Log.i("AAA","uri???"+uri.toString());
        gifImageView.setImageURI(uri);  // uri is ok but not working???
        gif_url_id=gif_url_id+1;
        Log.i("AAA",""+gif_url_id);
    }




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baidu_api);
        mTextView=(TextView)findViewById(R.id.tv_baidu);

        webView = (WebView) findViewById(R.id.webview);
        gifImageView=(GifImageView)findViewById(R.id.gifiv1_baidu);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setLoadWithOverviewMode(true);
//        webView.getSettings().setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);

        Parameters para = new Parameters();
        para.put("word", "%E7%8C%AB%E7%8C%AB"+"+jpg");  // keyword of search  -->猫猫  ？？？ 这是神马编码？？
//        para.put("word", "CAT+jpg");  // keyword of search
        para.put("pn", "1");  // 返回 起始页号码---
        para.put("rn", "60");  // 返回图片数目---
        ApiStoreSDK.execute("http://apis.baidu.com/image_search/search/search",
                ApiStoreSDK.GET,
                para,
                new ApiCallBack() {
                    @Override
                    public void onSuccess(int status, String responseString) {
                        Log.i("AAA", "onSuccess");
//                        mTextView.setText(responseString);
                        try {
                            JSONObject jsonRootObj = new JSONObject(responseString);
                            JSONObject jsonDataObj = jsonRootObj.optJSONObject("data");
                            JSONArray jsonArray=jsonDataObj.optJSONArray("ResultArray");
                            for (int ii=0;ii<jsonArray.length();ii++){
                                Log.i("AAA",""+jsonArray.getJSONObject(ii).optString("ObjUrl"));
                                urls[ii]=jsonArray.getJSONObject(ii).optString("ObjUrl");   // store to array
                                url_list.add(jsonArray.getJSONObject(ii).optString("ObjUrl")); // store to collection
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onComplete() {
                        Log.i("AAA", "onComplete");
                    }

                    @Override
                    public void onError(int status, String responseString, Exception e) {
                        Log.i("AAA", "onError, status: " + status);
                        Log.i("AAA", "errMsg: " + (e == null ? "" : e.getMessage()));
                        mTextView.setText(Log.getStackTraceString(e));
                    }
               });
    }
}
