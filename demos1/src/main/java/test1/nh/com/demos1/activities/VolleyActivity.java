package test1.nh.com.demos1.activities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

import test1.nh.com.demos1.R;

public class VolleyActivity extends AppCompatActivity {

    public static void start(Context context) {
        final Intent intent = new Intent(context, VolleyActivity.class);
        context.startActivity(intent);
    }


    TextView tv1;
    ImageView imageView;
    String url = "http://www.haosou.com";
    String url_cat_gif="http://image.haosou.com/i?q=%E7%8C%AB%E7%8C%AB+gif&src=srp";
    String url_testImage="http://img.bleacherreport.net/img/images/photos/003/556/300/hi-res-d36a8768df5b4b500a60b0deeab25e5e_crop_north.jpg?w=630&h=420&q=75";

    RequestQueue queue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_volley);

        queue = Volley.newRequestQueue(this);


        // string request-->
        tv1 = (TextView) findViewById(R.id.textView1_volley);
        stringRequestVolley(100,queue);  // show the first 100 letters


        // json request-->
        jsonRequestVolley(queue);


        // image request-->
        imageView=(ImageView) findViewById(R.id.iv1_volley);
        imageReqVolley(queue);


    }

    private void imageReqVolley(RequestQueue queue) {
        // 第三第四个参数分别用于指定允许图片最大的宽度和高度，如果指定的网络图片的宽度或高度大于这里的最大值，
        // 则会对图片进行压缩，指定成0的话就表示不管图片有多大，都不会进行压缩。
        // 第五个参数用于指定图片的颜色属性，
        // Bitmap.Config下的几个常量都可以在这里使用，
        // 其中ARGB_8888可以展示最好的颜色属性，每个图片像素占据4个字节的大小，而RGB_565则表示每个图片像素占据2个
        ImageRequest imageRequest = new ImageRequest(
                url_testImage,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        imageView.setImageBitmap(response);
                    }
                }, 0, 0, ImageView.ScaleType.CENTER_INSIDE, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                imageView.setImageDrawable(new ColorDrawable( getResources().getColor(R.color.Red200)));
            }
        });
        queue.add(imageRequest);
    }

    private void jsonRequestVolley(RequestQueue queue) {
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest("http://www.weather.com.cn/adat/sk/101010100.html", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("AAA", response.toString());
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("AAA", error.getMessage(), error);
            }
        });
        queue.add(jsonObjectRequest);
    }


    private void stringRequestVolley(final int xxx,RequestQueue queue) {
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first xxx characters of the response string.
                        byte[] bytes={1};
                        String string;
                        try {
                            bytes=response.substring(0, xxx).getBytes("ISO-8859-1"); // volley string response uses ISO-8859-1 encoding
                            string=new String(bytes,0,bytes.length,"UTF-8");  // android string use utf-8 encoding
                            tv1.setText("Response is: " +string);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        tv1.setText("That didn't work!");
                    }
                });
        // Add the request to the RequestQueue.--->request will be sent
        queue.add(stringRequest);
    }


}
