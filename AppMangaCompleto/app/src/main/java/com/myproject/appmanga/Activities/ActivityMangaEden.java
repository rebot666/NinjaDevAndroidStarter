package com.myproject.appmanga.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;

import com.myproject.appmanga.R;

public class ActivityMangaEden extends AppCompatActivity {

    private Context context = this;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private WebView webView;
    private String mUrl = "http://www.mangaeden.com";
    private View coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_eden);


        initializeToolbar();
        initializeComponents();
        initializeListeners();
        initializeData();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if(id == android.R.id.home){
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void initializeToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if (actionBar != null) {
            final Drawable upArrow = ContextCompat.getDrawable(context,R.drawable.ic_arrow_back_white_24dp);
            actionBar.setHomeAsUpIndicator(upArrow);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(getResources().getString(R.string.title_activity_manga_list));
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorTextIcons));
        }
    }

    public void initializeComponents(){
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        webView = (WebView) findViewById(R.id.webview);

    }

    public void initializeListeners(){

    }

    public void initializeData(){
        //Create new webview Client to show progress dialog
        //When opening a url or click on link

        webView.setWebViewClient(new WebViewClient() {
            ProgressDialog progressDialog;

            //If you will not use this method url links are opeen in new brower not in webview
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                progressDialog = new ProgressDialog(context);
                progressDialog.setMessage(getResources().getString(R.string.loading));
                progressDialog.show();
                view.loadUrl(url);
                return true;
            }

            //Show loader on url load
            public void onLoadResource (WebView view, String url) {
                if (progressDialog == null) {

                    // in standard case YourActivity.this
                    /*progressDialog = new ProgressDialog(News2Web.this);
                    if(!progressDialog.isShowing()){
                        progressDialog.setMessage("Loading...");
                        progressDialog.show();
                    } */

                }
            }
            public void onPageFinished(WebView view, String url) {
                try{
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                        progressDialog = null;
                    }
                }catch(Exception exception){
                    exception.printStackTrace();
                }
            }

        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.getSettings().setBuiltInZoomControls(true);

        //Load url in webview
        webView.loadUrl(mUrl);
    }

}
