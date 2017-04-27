package com.myproject.appmanga.Adapters;

import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;

import com.myproject.appmanga.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import uk.co.senab.photoview.PhotoView;

/**
 * Created by Ma. Rocio on 22/11/2016.
 */

public class SamplePagerAdapter extends PagerAdapter {

    /*private static final int[] sDrawables = { R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper,
            R.drawable.wallpaper, R.drawable.wallpaper, R.drawable.wallpaper };*/
    private ArrayList<String> items;
    public SamplePagerAdapter(ArrayList<String> items){
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public View instantiateItem(ViewGroup container, int position) {
        System.out.println("Salida->" + items.get(position));
        PhotoView photoView = new PhotoView(container.getContext());

        OkHttpClient client = new OkHttpClient.Builder()
                .protocols(Collections.singletonList(Protocol.HTTP_1_1))
                .build();

        Picasso.with(container.getContext()).
                load(items.get(position)).
                error(R.drawable.placeholder_ni).
                placeholder(R.drawable.placeholder_ld).
                into(photoView);

        // Now just add PhotoView to ViewPager and return it
        container.addView(photoView, ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.MATCH_PARENT);

        return photoView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

}