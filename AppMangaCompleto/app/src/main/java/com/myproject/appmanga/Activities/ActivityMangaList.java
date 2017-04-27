package com.myproject.appmanga.Activities;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.myproject.appmanga.Adapters.AdapterMangaChapters;
import com.myproject.appmanga.Adapters.AdapterMangaList;
import com.myproject.appmanga.R;
import com.myproject.appmanga.Utilities.APIConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_manga_list, menu);
        // Associate searchable configuration with the SearchView

        final SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        MenuItem searchMenuItem = menu.findItem(R.id.btn_search);
        if (searchMenuItem == null) {
            return true;
        }
        SearchView searchView = (SearchView) searchMenuItem.getActionView();



        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                try {
                    searchData(query);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                try {
                    searchData(newText);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return false;
            }
        });

        return true;
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
        }else if(id == R.id.btn_help){
            Intent intent = new Intent(ActivityMangaList.this, ActivityMangaEden.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                    Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
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
        recyclerView      = (RecyclerView) findViewById(R.id.scroll);
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
                //resultAPICall = APIConnection.getListOfMangaByIndex("1");
                resultAPICall = APIConnection.getListOfManga();
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
                    JSONArray arrayOfMangas = resultAPICall.getJSONArray("manga");

                    for (int i = 0; i < arrayOfMangas.length() ; i++) {
                        JSONObject temp = arrayOfMangas.getJSONObject(i);

                        data.add(temp);
                    }

                    Collections.sort(data, ALPHA_ORDER);
                    initializeList(data);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            //pd.dismiss();

        }
    }

    private static Comparator<JSONObject> ALPHA_ORDER = new Comparator<JSONObject>() {
        public int compare(JSONObject str1, JSONObject str2) {
            int x = 0;
            try {
                x = String.CASE_INSENSITIVE_ORDER.compare(str1.getString("t"), str2.getString("t"));
                if (x== 0) {
                    x= str1.getString("t").compareTo(str2.getString("t"));
                }
                return x;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return x;
        }
    };

    public void searchData(String queryDataSearch) throws JSONException {
        if(queryDataSearch.length() > 3){
            ArrayList<JSONObject> searchListData = new ArrayList<>();
            for(JSONObject manga : data){
                if(manga.getString("t") != null && manga.getString("t").toLowerCase().contains(queryDataSearch.toLowerCase())){
                    searchListData.add(manga);
                }
            }

            if(searchListData.size() > 0){
                initializeList(searchListData);
            }else{
                initializeList(data);
            }
        }else{
            initializeList(data);
        }
    }

}
