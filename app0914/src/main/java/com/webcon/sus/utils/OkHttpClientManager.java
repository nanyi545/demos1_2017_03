package com.webcon.sus.utils;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

/**
 * http工具
 * @author m
 */
public class OkHttpClientManager {
    private OkHttpClient mClient;
    private static OkHttpClientManager me;

    private OkHttpClientManager(){
        mClient = new OkHttpClient();
    }

    public static OkHttpClientManager getInstance(){
        if(me == null){
            synchronized (OkHttpClientManager.class){
                if(me == null){
                    me = new OkHttpClientManager();
                }
            }
        }
        return me;
    }


    /**
     * 同步Get的请求，获取最新的应用版本号
     */
    public String getAsString(String url) throws IOException {
        BufferedReader in = null;
        StringBuilder sb = new StringBuilder("");
        String line;
        try{
            Response response = getAsyn(url);
            if(response.code() != 200){
                return null;
            }
            in = new BufferedReader(new InputStreamReader(response.body().byteStream(), "utf-8"));
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line);
                sb.append(NL);
            }
        }finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return sb.toString();
    }

    /** 同步Get的请求 */
    private Response getAsyn(String url) throws IOException {
        final Request req = new Request.Builder()
                .url(url)
                .addHeader("Content-Type", "application/html; charset=utf-8")
                .build();
        Call call = mClient.newCall(req);
        mClient.setConnectTimeout(3000, TimeUnit.MILLISECONDS);
        return call.execute();
    }

}
