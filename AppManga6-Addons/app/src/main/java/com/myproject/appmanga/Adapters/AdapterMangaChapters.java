package com.myproject.appmanga.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myproject.appmanga.Activities.ActivityMangaViewer;
import com.myproject.appmanga.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * Created by Ma. Rocio on 24/11/2016.
 */

public class AdapterMangaChapters extends RecyclerView.Adapter<AdapterMangaChapters.ViewHolder>{

    private Context context;
    private int layoutResourceId;
    private ArrayList<JSONArray> data;

    public AdapterMangaChapters(Context context, int layoutResourceId, ArrayList<JSONArray> data){
        this.context          = context;
        this.layoutResourceId = layoutResourceId;
        this.data             = data;
    }

    @Override
    public AdapterMangaChapters.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(layoutResourceId, parent, false);

        return new AdapterMangaChapters.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(AdapterMangaChapters.ViewHolder holder, int position) {
        final JSONArray object = data.get(position);

        if(position % 2 == 0){
            holder.chapterNumberText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.titleText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
            holder.backgroundLayout.setBackgroundColor(context.getResources().getColor(R.color.colorTextIcons));
        }else{
            holder.chapterNumberText.setTextColor(context.getResources().getColor(R.color.colorTextIcons));
            holder.titleText.setTextColor(context.getResources().getColor(R.color.colorTextIcons));
            holder.backgroundLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight));
        }

        try {
            holder.chapterNumberText.setText(object.getString(0));
            if(object.getString(2) != null){
                holder.titleText.setText(context.getResources().getString(R.string.chapter_lbl) + " " + object.getString(0));
            }else{
                holder.titleText.setText(Html.fromHtml(object.getString(2)));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.backgroundLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Intent intent = new Intent(context, ActivityMangaViewer.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.putExtra("chapterId", object.getString(3));
                    context.startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout backgroundLayout;
        TextView chapterNumberText, titleText;


        public ViewHolder(View itemView){
            super(itemView);

            backgroundLayout  = (LinearLayout) itemView.findViewById(R.id.background_layout);
            chapterNumberText = (TextView) itemView.findViewById(R.id.chapter_number);
            titleText         = (TextView) itemView.findViewById(R.id.chapter_title);
        }
    }

    public void add(JSONArray item, int position){
        data.add(position, item);
        notifyDataSetChanged();
    }

    public void remove(JSONArray item){
        int position = data.indexOf(item);
        data.remove(position);
        notifyDataSetChanged();
    }
}
