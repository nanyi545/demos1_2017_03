package com.nanyi545.www.hybridtest;

import android.content.Context;
import android.util.Log;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/10/25.
 */
public class PieChartInterface {

    WebView webView;
    PieChartInterface(WebView webView){
        this.webView = webView;
    }


    public void restartAnimation(){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            webView.evaluateJavascript("restartAnimation();", null);
        } else {
            webView.loadUrl("javascript:restartAnimation();");
        }
    }


    @android.webkit.JavascriptInterface
    /**
     * requestAnimationFrame() --->  is only support after KITKAT,  for android
     */
    public boolean animationAvailable(){
        return (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT);
    }

    @android.webkit.JavascriptInterface
    public String getPercentages(){
        return "[10,20,30,40,100]";
    }


    @android.webkit.JavascriptInterface
    public String getColors(){
        return "[\"rgba(255,0,0,1)\",\"rgba(0,255,0,1)\",\"rgba(0,0,255,1)\",\"rgba(255,96,48,1)\",\"rgba(255,96,48,0.4)\"]";
    }

    @android.webkit.JavascriptInterface
    public float getTotal(){
        return 188008;
    }


    @android.webkit.JavascriptInterface
    public void log(String message){
        Log.i("JS_LOG",message);
    }



}
