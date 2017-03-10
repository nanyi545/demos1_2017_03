package test1.nh.com.demos1.activities.hybrid_test;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import test1.nh.com.demos1.R;

public class WebViewActivity extends AppCompatActivity {

    public static void start(Context c){
        Intent i=new Intent(c,WebViewActivity.class);
        c.startActivity(i);
    }


    WebView webView;
    EditText editText1;
    Button button1,button2;


    //  JavaScript 是所有现代浏览器以及 HTML5 中的默认脚本语言。

    public void loadLocal(View v){
//        webView.loadUrl("file:///android_asset/www/yellow.html");
//        webView.loadUrl("file:///android_asset/www/frameset.html");
//        webView.loadUrl("file:///android_asset/www/tables.html");
//        webView.loadUrl("file:///android_asset/www/fee.html");
//        webView.loadUrl("file:///android_asset/www/sample1.html");
//        webView.loadUrl("file:///android_asset/www/h5sample1.html");
//        webView.loadUrl("file:///android_asset/www/js1.html");
//        webView.loadUrl("file:///android_asset/www/js2_button.html");
//        webView.loadUrl("file:///android_asset/www/js3_change.html");
//        webView.loadUrl("file:///android_asset/www/js4_light.html");
//        webView.loadUrl("file:///android_asset/www/js5_change_color.html");
//        webView.loadUrl("file:///android_asset/www/js6_input.html");
//        webView.loadUrl("file:///android_asset/www/js7_function.html");
//        webView.loadUrl("file:///android_asset/www/js8_external_function.html");
//        webView.loadUrl("file:///android_asset/www/js9_variable.html");
//        webView.loadUrl("file:///android_asset/www/js10_array.html");
//        webView.loadUrl("file:///android_asset/www/js11_object.html");
//        webView.loadUrl("file:///android_asset/www/js12_functions.html");
//        webView.loadUrl("file:///android_asset/www/js13_gettime.html");
//        webView.loadUrl("file:///android_asset/www/js14_functions.html");   // TODO MODIFY THIS .....

//        webView.addJavascriptInterface(new JSInterface(this), "Android");
//        webView.loadUrl("file:///android_asset/www/js15_array.html");
//        webView.loadUrl("file:///android_asset/www/js16_calculator.html");
//        webView.loadUrl("file:///android_asset/www/js17_time.html");
//        webView.loadUrl("file:///android_asset/www/js18_string.html");
//        webView.loadUrl("file:///android_asset/www/js19_clock.html");
//        webView.loadUrl("file:///android_asset/www/js20_changetable.html");
//        webView.loadUrl("file:///android_asset/www/js21_selection_tabs.html");
//        webView.loadUrl("file:///android_asset/www/jq1_test.html");
//        webView.loadUrl("file:///android_asset/www/jq2_selectors.html");  // 层级 selector
//        webView.loadUrl("file:///android_asset/www/jq3_content_selectors.html");


//        webView.loadUrl("file:///android_asset/www/jq8_addElement.html");
//        webView.loadUrl("file:///android_asset/www/jq20_navigation.html");
        webView.loadUrl("file:///android_asset/www/jqm2_transition.html");

//        webView.addJavascriptInterface(new JSInterface(this), "Android"); //You will access this via Android.method(args);
//        webView.loadUrl("file:///android_asset/www/js_call_android.html");
//        webView.loadUrl("file:///android_asset/www/test_layout.html");
        
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView= (WebView) findViewById(R.id.my_web_view);
        webView.setHorizontalScrollBarEnabled(true);
        webView.setWebChromeClient(new WebChromeClient());  // this is needed to trigger alert !!
        webView.setWebViewClient(new WebViewClient());

        // enable JS
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);

        editText1= (EditText) findViewById(R.id.path);
        button1= (Button) findViewById(R.id.button1);
        button2= (Button) findViewById(R.id.button2);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                String url=editText1.getText().toString();
                String url="http://blog.csdn.net/nanyi545/article/details/54408851";
                webView.loadUrl(url);
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url=editText1.getText().toString();
                webView.loadData(url,"text/html","utf-8");
            }
        });

    }
}
