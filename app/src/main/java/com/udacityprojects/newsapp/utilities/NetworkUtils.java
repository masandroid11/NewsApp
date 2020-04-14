package com.udacityprojects.newsapp.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;


import com.udacityprojects.newsapp.BuildConfig;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetworkUtils {

    private final static OkHttpClient client = new OkHttpClient();

    private final static String BASE_TOP_HEADLINES_URL = "https://newsapi.org/v2/top-headlines";
    private final static String API_KEY = BuildConfig.API_KEY;

    private final static String COUNTRY_KEY = "country";
    private final static String CATEGORY_KEY = "category";

    public final static String COUNTRY_PREF = "us";


    public static URL buildHeadlineUrl(String... params){
        Uri buildUri;
        if (params.length == 2){
            buildUri = Uri.parse(BASE_TOP_HEADLINES_URL).buildUpon()
                    .appendQueryParameter(COUNTRY_KEY,params[0])
                    .appendQueryParameter(CATEGORY_KEY,params[1])
                    .appendQueryParameter("apiKey",API_KEY)
                    .build();
        }else {
            buildUri = Uri.parse(BASE_TOP_HEADLINES_URL).buildUpon()
                    .appendQueryParameter(COUNTRY_KEY,params[0])
                    .appendQueryParameter("apiKey",API_KEY)
                    .build();
        }

        URL url = null;
        try {
            url = new URL(buildUri.toString());
        }catch (MalformedURLException e){
            e.printStackTrace();
        }

        return url;
    }

    public static String getHttpResponse(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static boolean isConnectedToNetwork(Context context){
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        return connectivityManager.getActiveNetworkInfo() != null;
    }
}
