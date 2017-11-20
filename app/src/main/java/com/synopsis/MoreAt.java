package com.synopsis;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.RelativeLayout;

public class MoreAt extends AppCompatActivity {
WebView mWebview;
    String Url;
    RelativeLayout close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_more_at);
        mWebview=(WebView)findViewById(R.id.webView);
        mWebview.setWebViewClient(new MyBrowser());
        close=(RelativeLayout)findViewById(R.id.close);

        Intent in=getIntent();
        Url=in.getStringExtra("URL");
//        WebSettings webSettings = mWebview.getSettings();
//        mWebview.getSettings().setJavaScriptEnabled(true); // enable javascript
//        mWebview .loadUrl(Url);
//        mWebview.setWebViewClient(new WebViewClient() {
//            public boolean shouldOverrideUrlLoading(WebView view, String url) {
//                if (url != null && url.startsWith("http://")) {
//                    view.getContext().startActivity(
//                            new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//                    return true;
//                } else {
//                    return false;
//                }
//            }
//        });
        if (Build.VERSION.SDK_INT >= 19) {
            mWebview.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        }
        else {
            mWebview.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }
        mWebview.getSettings().setLoadsImagesAutomatically(true);
        mWebview.getSettings().setJavaScriptEnabled(true);
        mWebview.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        mWebview.loadUrl(Url);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MoreAt.this.finish();
//                LayoutInflater inflater =getLayoutInflater();
//                View layout = inflater.inflate(R.layout.custom_progressbar,null);
//                final PopupWindow popupWindoww = new PopupWindow(layout, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//
//                ProgressBar progress = (ProgressBar) layout.findViewById(R.id.progressBar2);
//                popupWindoww.showAtLocation(layout, ViewGroup.LayoutParams.WRAP_CONTENT, 0, 180);
//                popupWindoww.setFocusable(true);
//                popupWindoww.update();



            }
        });
    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
