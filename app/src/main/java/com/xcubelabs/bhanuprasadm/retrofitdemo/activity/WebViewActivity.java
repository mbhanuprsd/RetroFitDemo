package com.xcubelabs.bhanuprasadm.retrofitdemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xcubelabs.bhanuprasadm.retrofitdemo.R;

public class WebViewActivity extends AppCompatActivity implements View.OnClickListener {

    private WebView webView;
    Button btnBack, btnForward, btnRefresh;
    TextView tvLink;
    private ProgressBar progressBar;
    public static final String URL = "https://www.medpost.com";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        webView = (WebView) findViewById(R.id.wvActivity);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnBack = (Button) findViewById(R.id.btnWVBack);
        btnForward = (Button) findViewById(R.id.btnWVForward);
        btnRefresh = (Button) findViewById(R.id.btnWVRefresh);
        tvLink = (TextView) findViewById(R.id.tvWVLink);

        btnBack.setOnClickListener(this);
        btnForward.setOnClickListener(this);
        btnRefresh.setOnClickListener(this);

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if (url.startsWith("tel:")) {
                    Intent numIntent = new Intent(Intent.ACTION_DIAL, Uri.parse(url));
                    startActivity(numIntent);
                } else if (url.startsWith("http:") || url.startsWith("https:")) {
                    view.loadUrl(url);
                }
                return true;
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);
                tvLink.setText(url);
                btnRefresh.setVisibility(View.GONE);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);
                btnRefresh.setVisibility(View.VISIBLE);

                if(webView.canGoBack()){
                    btnBack.setVisibility(View.VISIBLE);
                } else {
                    btnBack.setVisibility(View.GONE);
                }
                if(webView.canGoForward()){
                    btnForward.setVisibility(View.VISIBLE);
                } else {
                    btnForward.setVisibility(View.GONE);
                }
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
        webView.loadUrl(URL);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnWVBack:
                webView.goBack();
                break;
            case R.id.btnWVForward:
                webView.goForward();
                break;
            case R.id.btnWVRefresh:
                webView.reload();
                break;
            default:
                break;
        }
    }
}
