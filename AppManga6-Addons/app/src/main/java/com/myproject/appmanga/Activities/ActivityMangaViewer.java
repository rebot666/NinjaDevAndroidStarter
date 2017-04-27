package com.myproject.appmanga.Activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.myproject.appmanga.Adapters.SamplePagerAdapter;
import com.myproject.appmanga.R;
import com.myproject.appmanga.Utilities.APIConnecction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityMangaViewer extends AppCompatActivity {

    private ViewPager viewPager;
    private Context context = this;
    private ArrayList<String> listImages;
    private String chapterId;

    private String imageLoaderURL = "http://cdn.mangaeden.com/mangasimg/"; //Load images API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_manga_viewer);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.hide();
        }

        manageIntent();
        initializeComponents();
        initializeListeners();

        InternetConnection internetConnection = new InternetConnection();
        internetConnection.execute();
    }

    public void manageIntent(){
        Intent intent = getIntent();
        if(intent != null){
            chapterId = intent.getStringExtra("chapterId");
        }
    }

    public void initializeComponents(){
        viewPager = (ViewPager) findViewById(R.id.view_pager);
    }

    public void initializeListeners(){

    }

    private class InternetConnection extends AsyncTask<String,Void,String>{
        private JSONObject resultAPICall;

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(String... strings) {

            try {
                resultAPICall = APIConnecction.getMangaChapterImages(chapterId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String result){
            listImages = new ArrayList<>();
            if(resultAPICall != null){
                try {
                    JSONArray arrayOfMangas = resultAPICall.getJSONArray("images");

                    for (int i = arrayOfMangas.length()-1; i >= 0 ; i--) {
                        JSONArray temp = arrayOfMangas.getJSONArray(i);

                        String urlImage = imageLoaderURL + temp.getString(1);

                        listImages.add(urlImage);
                    }

                    initializeList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public void initializeList(){
        viewPager.setAdapter(new SamplePagerAdapter(listImages));
    }

}
