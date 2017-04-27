package com.myproject.appmanga.Utilities;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Ma. Rocio on 23/11/2016.
 */

public class APIConnecction {

    private static String http                 = "http://";
    private static String https                = "https://";
    private static String server               = "www.mangaeden.com/api/";
    private static String language             = "0";

    private static String getMangaChapterImagesURL = http + server + "chapter/";
    private static String getMangaChaptersURL      = http + server + "manga/";
    private static String getListOfMangaByIndexURL = http + server + "list/"+ language;
    private static String getListOfMangaURL        = http + server + "list/"+ language;

    private static OkHttpClient client = new OkHttpClient();

    public static JSONObject getMangaChapterImages(String chapterId) throws IOException, JSONException {
        Request request = new Request.Builder()
                .url(getMangaChapterImagesURL + chapterId)
                .build();

        Response response = client.newCall(request).execute();
        return new JSONObject(response.body().string());
    }


}
