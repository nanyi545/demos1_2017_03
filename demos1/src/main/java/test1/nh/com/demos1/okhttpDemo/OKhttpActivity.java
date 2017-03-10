package test1.nh.com.demos1.okhttpDemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import test1.nh.com.demos1.R;

public class OKhttpActivity extends AppCompatActivity {

    public static void start(Context c1){
        Intent i1=new Intent(c1,OKhttpActivity.class);
        c1.startActivity(i1);
    }


    // more at    https://github.com/square/okhttp/wiki/Recipes


    //  ---------  Synchronous Get---------------------------------------
    private static final Runnable sRunnable1 =new Runnable(){

        private final OkHttpClient client = new OkHttpClient();

        @Override
        public void run() {
            Request request = new Request.Builder()
                    .url("http://publicobject.com/helloworld.txt")
                    .build();
            try {
                Response response = client.newCall(request).execute();  //Synchronous Get

                if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                Headers responseHeaders = response.headers();
                for (int i = 0; i < responseHeaders.size(); i++) {
                    Log.i("CCC",(responseHeaders.name(i) + ": " + responseHeaders.value(i)));
                }

                String str=response.body().string();
                //  The string() method on response body is convenient and efficient for small documents.
                // But if the response body is large (greater than 1 MiB), avoid string() because it will
                // load the entire document into memory. In that case, prefer to process the body as a stream.

                Log.i("CCC","string length"+str.length());
                Log.i("CCC",str);

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    //  ---------  Asynchronous Get---------------------------------------
    private static final Runnable asRunnable1 =new Runnable(){

        private final OkHttpClient client = new OkHttpClient();

        @Override
        public void run() {
            Request request = new Request.Builder()
                    .url("http://publicobject.com/helloworld.txt")
                    .build();


            client.newCall(request).enqueue(new Callback() {
                @Override public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.i("CCC",Log.getStackTraceString(e));
                }

                @Override public void onResponse(Call call, Response response) throws IOException {
                    if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);

                    Headers responseHeaders = response.headers();
                    for (int i = 0, size = responseHeaders.size(); i < size; i++) {
                        Log.i("CCC",responseHeaders.name(i) + ": " + responseHeaders.value(i));
                    }

                    Log.i("CCC",response.body().string());
                }
            });

        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_okhttp);

        // Synchronous Get
//        new Thread(sRunnable1).start();

        // Asynchronous Get
        new Thread(asRunnable1).start();
    }
}
