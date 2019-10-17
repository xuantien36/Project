package com.t3h.project;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class WebViewActivity extends AppCompatActivity {

    private WebView webView;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        webView = findViewById(R.id.wv_webview);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Loading, please wait...");
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        webView.getSettings().setJavaScriptEnabled(true);
        if (url.startsWith("http")){
            webView.loadUrl(url);
        } else{
            Toast.makeText(this, "The news has been downloaded!", Toast.LENGTH_SHORT).show();
            webView.loadUrl("file://"+url);

        }

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                dialog.dismiss();
            }

        });



    }

}
