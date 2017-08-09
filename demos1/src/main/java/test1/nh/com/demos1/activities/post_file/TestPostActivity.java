package test1.nh.com.demos1.activities.post_file;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import org.apache.commons.io.FileUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import test1.nh.com.demos1.R;

public class TestPostActivity extends AppCompatActivity {


    public static void start(Context c){
        Intent i=new Intent(c,TestPostActivity.class);
        c.startActivity(i);
    }



    private void createFileIfNotExist__(){
        SerializableManager.saveSerializable(this,"123\n456\n789\n","test");
        Log.i("ccc","read: " + SerializableManager.readSerializable(this,"test"));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_post);
        createFileIfNotExist__();
    }






    public void doUploadTxt(View v){
        final File upload = getFileStreamPath("test");

        byte[] data=null;
        try {
            data=((String)SerializableManager.readSerializable(this,"test")).getBytes("utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
//        try {
//            data=FileUtils.readFileToByteArray(upload);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
        if (data!=null){
            final byte[] finalData = data;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final MediaType MEDIA_TYPE_TXT = MediaType.parse("text/plain; charset=utf-8");
//                    final MediaType MEDIA_TYPE_TXT =MediaType.parse("text/x-markdown; charset=utf-8");
                    MultipartBody.Builder formBodyBuilder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM);
                    formBodyBuilder.addFormDataPart("type", "txt");
                    formBodyBuilder.addFormDataPart("file" , "test", RequestBody.create(MEDIA_TYPE_TXT, finalData));
//                    formBodyBuilder.addFormDataPart("file" , "test", RequestBody.create(MEDIA_TYPE_TXT, upload));
                    MultipartBody requestBody = formBodyBuilder.build();
                    Request request = new Request.Builder()
                            .url("http://nanyi5452.pythonanywhere.com/open/api/post_file/")
                            .post(requestBody)  // post type
                            .build();
                    Response response = null;
                    OkHttpClient client=new OkHttpClient();
                    try {
                        response = client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String jsonStr = "";
                    try {
                        jsonStr = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("ccc","upload return: " + jsonStr);

                }
            }).start();
        }
    }


    public void doUploadImg(View v){
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.s);
        byte[] data=null;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        data = stream.toByteArray();

        if (data!=null){
            final byte[] finalData = data;

            new Thread(new Runnable() {
                @Override
                public void run() {
                    final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/png");
//                    final MediaType MEDIA_TYPE_TXT =MediaType.parse("text/x-markdown; charset=utf-8");
                    MultipartBody.Builder formBodyBuilder = new MultipartBody.Builder()
                            .setType(MultipartBody.FORM);
                    formBodyBuilder.addFormDataPart("type", "png");
                    formBodyBuilder.addFormDataPart("file" , "test", RequestBody.create(MEDIA_TYPE_PNG, finalData));
//                    formBodyBuilder.addFormDataPart("file" , "test", RequestBody.create(MEDIA_TYPE_TXT, upload));
                    MultipartBody requestBody = formBodyBuilder.build();
                    Request request = new Request.Builder()
                            .url("http://nanyi5452.pythonanywhere.com/open/api/post_file/")
                            .post(requestBody)  // post type
                            .build();
                    Response response = null;
                    OkHttpClient client=new OkHttpClient();
                    try {
                        response = client.newCall(request).execute();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    String jsonStr = "";
                    try {
                        jsonStr = response.body().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.i("ccc","upload return: " + jsonStr);

                }
            }).start();
        }
    }


}
