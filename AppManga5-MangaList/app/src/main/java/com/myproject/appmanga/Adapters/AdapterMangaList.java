package com.myproject.appmanga.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.myproject.appmanga.ActivityMangaChapter;
import com.myproject.appmanga.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Ma. Rocio on 01/01/2017.
 */

public class AdapterMangaList extends RecyclerView.Adapter<AdapterMangaList.ViewHolder>{
    private Context context;
    private int layoutResourceId;
    private ArrayList<JSONObject> data;
    private String imageLoaderURL = "http://cdn.mangaeden.com/mangasimg/";

    public AdapterMangaList(Context context, int layoutResourceId, ArrayList<JSONObject> data){
        this.context = context;
        this.layoutResourceId = layoutResourceId;
        this.data = data;
    }

    @Override
    public AdapterMangaList.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i){
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(layoutResourceId, viewGroup, false);

        return new AdapterMangaList.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(final AdapterMangaList.ViewHolder holder, int position){
        final JSONObject objeto = data.get(position);

        try{
            if(position %2 == 0){
                holder.statusText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.titleText.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                holder.backgrounLayout.setBackgroundColor(context.getResources().getColor(R.color.colorTextIcons));
            }else{
                holder.statusText.setTextColor(context.getResources().getColor(R.color.colorTextIcons));
                holder.titleText.setTextColor(context.getResources().getColor(R.color.colorTextIcons));
                holder.backgrounLayout.setBackgroundColor(context.getResources().getColor(R.color.colorPrimaryLight));
            }

            if(objeto.getString("s").equals("1")){
                holder.statusText.setText(context.getResources().getString(R.string.status) + " " + context.getResources().getString(R.string.on_going));
            }else if(objeto.getString("s").equals("2")){
                holder.statusText.setText(context.getResources().getString(R.string.status) + " " + context.getResources().getString(R.string.completed));
            }else{
                holder.statusText.setText(context.getResources().getString(R.string.status) + " " + context.getResources().getString(R.string.unknown));
            }

            holder.titleText.setText(Html.fromHtml(objeto.getString("t")));

            Picasso.with(context)
                    .load(imageLoaderURL + objeto.getString("im"))
                    .error(R.mipmap.ic_launcher)
                    .placeholder(R.mipmap.ic_launcher)
                    .into(holder.mangaImage);

            holder.backgrounLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try{
                        Intent intent = new Intent(context, ActivityMangaChapter.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP |
                                Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        intent.putExtra("mangaId", objeto.getString("i"));
                        context.startActivity(intent);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            });
        }catch (JSONException e){
            e.printStackTrace();
        }

    }

    @Override
    public int getItemCount(){
        return data.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout backgrounLayout;
        TextView statusText, titleText;
        ImageView mangaImage;

        public ViewHolder(View itemView){
            super(itemView);

            titleText  = (TextView) itemView.findViewById(R.id.manga_title);
            statusText = (TextView) itemView.findViewById(R.id.manga_status);
            mangaImage = (ImageView) itemView.findViewById(R.id.manga_image);
            backgrounLayout = (LinearLayout) itemView.findViewById(R.id.layout_background);
        }
    }

    public void add(JSONObject item, int position){
        data.add(position, item);
        notifyItemInserted(position);
    }

    public void remove(JSONObject item){
        int position = data.indexOf(item);
        data.remove(position);
        notifyItemRemoved(position);
    }
}
