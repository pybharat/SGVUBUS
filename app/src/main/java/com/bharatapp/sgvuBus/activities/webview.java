package com.bharatapp.sgvuBus.activities;



import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.widget.Toolbar;

import com.bharatapp.sgvuBus.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import im.delight.android.webview.AdvancedWebView;

public class webview extends Activity implements AdvancedWebView.Listener {
    private AdvancedWebView mWebView;
    private String url;
    private Bitmap favicon;
    Toolbar toolbar;
    BottomNavigationView bottomNavigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        mWebView = (AdvancedWebView) findViewById(R.id.webview);
        toolbar=findViewById(R.id.actionbar1);
        bottomNavigationView=findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.home:
                        Intent i=new Intent(webview.this, dashboard.class);
                        i.putExtra("poster",1);
                        startActivity(i);
                        return true;
                    case R.id.profile:
                        Intent i1=new Intent(webview.this,profile.class);
                        startActivity(i1);
                        return true;
                    case R.id.lms:
                        Intent i2=new Intent(webview.this, webview.class);
                        i2.putExtra("url","https://mygyanvihar.com/2020");
                        startActivity(i2);
                        return true;
                    default:return false;
                }

            }
        });
        mWebView.setListener(this, this);
        mWebView.setMixedContentAllowed(false);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            url = bundle.getString("url");

        }
        mWebView.setCookiesEnabled(true);
        mWebView.setMixedContentAllowed(true);
        mWebView.loadUrl(url);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWebView.onResume();
        // ...
    }


    @Override
    protected void onPause() {
        mWebView.onPause();
        // ...
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        mWebView.onDestroy();

        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        mWebView.onActivityResult(requestCode, resultCode, intent);

    }

    @Override
    public void onBackPressed() {
        if (!mWebView.onBackPressed()) { return; }

        super.onBackPressed();
    }


    @Override
    public void onPageStarted(String url, Bitmap favicon) {

    }

    @Override
    public void onPageFinished(String url) { }

    @Override
    public void onPageError(int errorCode, String description, String failingUrl) { }

    @Override
    public void onDownloadRequested(String url, String suggestedFilename, String mimeType, long contentLength, String contentDisposition, String userAgent) { }

    @Override
    public void onExternalPageRequest(String url) { }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

}
