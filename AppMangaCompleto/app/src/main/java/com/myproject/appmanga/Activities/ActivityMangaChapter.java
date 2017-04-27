package com.myproject.appmanga.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myproject.appmanga.Adapters.AdapterMangaChapters;
import com.myproject.appmanga.R;
import com.myproject.appmanga.Utilities.APIConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityMangaChapter extends AppCompatActivity {

    private Context context = this;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private AdapterMangaChapters adapter;
    private ArrayList<JSONArray> data;
    private View coordinatorLayout;
    private ImageView imageTitle;
    private TextView description;
    private String mangaId;

    private String imageLoaderURL = "http://cdn.mangaeden.com/mangasimg/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_chapter);

        manageIntent();
        initializeToolbar();
        initializeComponents();
        initializeListeners();

        InternetConnection internetConnection = new InternetConnection();
        internetConnection.execute();
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

    public void manageIntent(){
        Intent intent = getIntent();
        if(intent != null){
            mangaId = intent.getStringExtra("mangaId");
        }
    }

    public void initializeToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_arrow_back_white_24dp);
            collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
            collapsingToolbarLayout.setTitle(getResources().getString(R.string.title_activity_manga_chapter));
            collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
//            collapsingToolbarLayout.setExpandedTitleTextColor(getResources().getColor(android.R.color.transparent));
//            collapsingToolbarLayout.setBackgroundColor();
            collapsingToolbarLayout.setCollapsedTitleTextColor(getResources().getColor(R.color.colorTextIcons));

        }
    }

    public void initializeComponents(){
        recyclerView      = (RecyclerView) findViewById(R.id.scroll);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        imageTitle        = (ImageView) findViewById(R.id.image_title);
        description       = (TextView) findViewById(R.id.description);
    }

    public void initializeListeners(){

    }

    public void initializeList(){
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        adapter = new AdapterMangaChapters(context, R.layout.item_manga_chapters, data);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
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
                resultAPICall = APIConnection.getMangaInfo(mangaId);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return response;  //To change body of implemented methods use File | Settings | File Templates.

        }

        @Override
        protected void onPostExecute(String result){
            data = new ArrayList<>();
            if(resultAPICall != null){
                try {
                    collapsingToolbarLayout.setTitle(Html.fromHtml(resultAPICall.getJSONArray("aka").get(0).toString()));

                    description.setText(Html.fromHtml(resultAPICall.getString("description")));

                    Picasso.with(context).
                            load(imageLoaderURL + resultAPICall.getString("image")).
                            error(R.drawable.placeholder_ni).
                            placeholder(R.drawable.placeholder_ld).
                            into(imageTitle);

                    JSONArray arrayOfMangas = resultAPICall.getJSONArray("chapters");

                    for (int i = 0; i < arrayOfMangas.length() ; i++) {
                        JSONArray temp = arrayOfMangas.getJSONArray(i);

                        data.add(temp);
                    }

                    initializeList();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            //pd.dismiss();

        }
    }

}
