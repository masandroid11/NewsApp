package com.udacityprojects.newsapp.utilities;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.udacityprojects.newsapp.models.Article;
import com.udacityprojects.newsapp.models.ArticleResponse;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DataUtils {

    public static String DATABASE_PATH = "bookmarks";

    public static List<Article> getNewsDetails(String json){

        ArticleResponse response = new Gson().fromJson(json,ArticleResponse.class);
        List<Article> articleList = response.getArticleList();

        return articleList;
    }

    public static String formatDataTime(String dateTime){
        String formatted = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("E, d MMM yyyy", Locale.getDefault());
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(dateTime);
            formatted = dateFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();

        }

        return formatted;
    }

    public static String removeExtraChar(String content){
        if (content != null){
            int index = content.indexOf("[");
            Log.d("index", "removeExtraChar: " + index);
            if (index != -1) return content.substring(0,index);
            return content;
        }
        return "";
    }
}
