package com.nanyi545.www.hybridtest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class NonFullWindowActivity extends AppCompatActivity {


    public static void start(Context c){
        Intent i=new Intent(c,NonFullWindowActivity.class);
        c.startActivity(i);
    }

    WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_non_full_window);

        webView= (WebView) findViewById(R.id.my_web_view);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());  // this is needed to trigger alert !!
        webView.setWebViewClient(new WebViewClient());

        // ----  enable JS
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        pieChartInterface=new PieChartInterface(webView);

        webView.addJavascriptInterface(pieChartInterface, "Android");

        webView.loadUrl("file:///android_asset/www/canvas_test_page/canvas_pie_chart.html");
//        webView.loadUrl("file:///android_asset/www/canvas_test_page/canvas_test2.html");


    }


    PieChartInterface pieChartInterface;
    public void restartWebview(View v){
        pieChartInterface.log("click--1");
        pieChartInterface.restartAnimation();
        pieChartInterface.log("click--2");
    }

}
