package com.myproject.appmanga.Activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.myproject.appmanga.R;

public class ActivitySplashscreen extends AppCompatActivity {

    private static final int SPLASH_DURATION = 2000;  //Duración del Splashscreen

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splashscreen);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.hide();
        }

        Handler handler = new Handler();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(ActivitySplashscreen.this, ActivityMangaList.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DURATION);
    }
}
