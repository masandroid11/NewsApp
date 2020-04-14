package com.udacityprojects.newsapp.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleResponse {
    @SerializedName("articles")
    private List<Article> articleList;

    public List<Article> getArticleList() {
        return articleList;
    }
}
