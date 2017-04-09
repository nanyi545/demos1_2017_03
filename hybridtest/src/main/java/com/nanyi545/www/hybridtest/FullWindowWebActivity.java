package com.nanyi545.www.hybridtest;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class FullWindowWebActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,FullWindowWebActivity.class);
        c.startActivity(i);
    }

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_window_web);

        webView= (WebView) findViewById(R.id.my_web_view);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());  // this is needed to trigger alert !!
        webView.setWebViewClient(new WebViewClient());


        // ----  enable JS
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new JSInterface(this), "Android");

//        webView.loadUrl("file:///android_asset/www/jqm2_transition.html");
//        webView.loadUrl("file:///android_asset/www/svg5.html");
//        webView.loadUrl("file:///android_asset/www/svg6_inkscape_test.html");
//        webView.loadUrl("file:///android_asset/svg6_inkscape_test.svg");
//        webView.loadUrl("file:///android_asset/www/ala_17_2_15/promo_page/promo.html");
//        webView.loadUrl("file:///android_asset/www/ala_17_2_15/reg_success_page/reg_success.html");
//        webView.loadUrl("file:///android_asset/www/ala_17_2_15/wechat_home/wechat_home.html");
//        webView.loadUrl("file:///android_asset/www/ala_17_2_15/ala_kai_huang_intro_page/kai_huang_intro.html");
//        webView.loadUrl("file:///android_asset/www/ala_17_2_15/ala_cleaning_intro_page/clean_intro.html");
//        webView.loadUrl("file:///android_asset/www/ala_17_2_15/ala_cleaning_intro_page_remote_basket/clean_intro.html");
//        webView.loadUrl("file:///android_asset/www/ala_17_2_15/ala_all_service_page/all_service.html");
//        webView.loadUrl("file:///android_asset/www/ala_17_2_15/ala_all_service_page_no_href/all_service.html");
//        webView.loadUrl("file:///android_asset/www/ala_17_2_15/ala_lottery_page/lottery.html");

//        webView.loadUrl("http://www.xiaojilicai.com/Wechat2/Activity/activity0901?from=singlemessage#item2");  //  page with a tags

        //  ...  canvas  tests ....
//        webView.loadUrl("file:///android_asset/www/canvas_test_page/canvas_test4_animation1.html");
        webView.loadUrl("file:///android_asset/www/canvas_test_page/canvas_test3_revise.html");

    }



}
