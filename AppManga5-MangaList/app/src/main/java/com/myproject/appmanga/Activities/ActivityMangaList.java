package com.myproject.appmanga.Activities;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.myproject.appmanga.Adapters.AdapterMangaList;
import com.myproject.appmanga.R;
import com.myproject.appmanga.Utilities.APIConnecction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

public class ActivityMangaList extends AppCompatActivity {

    private Context context = this;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private LinearLayoutManager linearLayoutManager;
    private RecyclerView recyclerView;
    private AdapterMangaList adapter;
    private ArrayList<JSONObject> data;
    private View coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manga_list);

        initializeToolbar();
        initializeComponents();
        initializeListeners();

        InternetConnection internetConnection = new InternetConnection();
        internetConnection.execute();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if(id == android.R.id.home){
            super.onBackPressed();
        }

        return super.onOptionsItemSelected(item);
    }

    public void initializeToolbar(){
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        if(actionBar != null){
            final Drawable upArrow = ContextCompat.getDrawable(context, R.drawable.ic_arrow_back_white_24dp);
            actionBar.setHomeAsUpIndicator(upArrow);
            actionBar.setTitle(getResources().getString(R.string.title_activity_manga_chapter));
            toolbar.setTitleTextColor(getResources().getColor(R.color.colorTextIcons));
        }
    }

    public void initializeComponents(){
        recyclerView = (RecyclerView) findViewById(R.id.scroll);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
    }

    public void initializeListeners(){

    }

    public void initializeList(ArrayList<JSONObject> dataForAdapter){
        linearLayoutManager = new LinearLayoutManager(context);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        linearLayoutManager.scrollToPosition(0);
        adapter = new AdapterMangaList(context, R.layout.item_manga_list, dataForAdapter);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private class InternetConnection extends AsyncTask<String,Void,String>{
        private JSONObject resultAPICall;

        @Override
        protected void onPreExecute(){

        }

        @Override
        protected String doInBackground(String... urls){
            String response = "";

            try {
                resultAPICall = APIConnecction.getListOfManga();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return response;
        }

        @Override
        protected  void onPostExecute(String result){
            data = new ArrayList<>();
            if(resultAPICall != null){
                try{
                    JSONArray arrayOfMangas = resultAPICall.getJSONArray("manga");

                    for (int i = 0; i < arrayOfMangas.length() ; i++) {
                        JSONObject temp = arrayOfMangas.getJSONObject(i);

                        data.add(temp);
                    }

                    initializeList(data);
                }catch (JSONException e){
                    e.printStackTrace();
                }

            }
        }
    }

}
