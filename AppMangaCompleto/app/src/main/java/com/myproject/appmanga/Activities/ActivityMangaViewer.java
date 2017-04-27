package com.myproject.appmanga.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;
import android.view.WindowManager;

import com.myproject.appmanga.Adapters.SamplePagerAdapter;
import com.myproject.appmanga.R;
import com.myproject.appmanga.Utilities.APIConnection;
import com.myproject.appmanga.Utilities.HackyViewPager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityMangaViewer extends AppCompatActivity {

    ViewPager mViewPager;
    private Context context = this;
    private ArrayList<String> listImages;
    private String chapterId;

    private String imageLoaderURL = "http://cdn.mangaeden.com/mangasimg/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manga_viewer);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
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
        mViewPager   = (HackyViewPager) findViewById(R.id.view_pager);

    }

    public void initializeListeners(){

    }

    public void initializeData(){

        mViewPager.setAdapter(new SamplePagerAdapter(listImages));
    }

    private class InternetConnection extends AsyncTask<String,Void,String> {
        private ProgressDialog pd;
        private JSONObject resultAPICall;

        @Override
        protected void onPreExecute(){
            //pd = ProgressDialog.show(context, "Cargando" , "Cargando...",true,false,null);
        }

        @Override
        protected String doInBackground(String... urls) {
            String response = "";

            try {
                resultAPICall = APIConnection.getMangaChapterImages(chapterId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return response;  //To change body of implemented methods use File | Settings | File Templates.

        }

        @Override
        protected void onPostExecute(String result){
            listImages = new ArrayList<>();
            if(resultAPICall != null){
                try {
                    JSONArray arrayOfMangas = resultAPICall.getJSONArray("images");

                    for (int i = arrayOfMangas.length()-1; i >= 0  ; i--) {
                        JSONArray temp = arrayOfMangas.getJSONArray(i);
                        String urlImageChapter = imageLoaderURL + temp.get(1);

                        listImages.add(urlImageChapter);
                    }

                    initializeData();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            //pd.dismiss();

        }
    }

}
