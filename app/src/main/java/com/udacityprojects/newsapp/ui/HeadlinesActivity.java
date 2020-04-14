package com.udacityprojects.newsapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.udacityprojects.newsapp.R;
import com.udacityprojects.newsapp.adapters.TabPagerAdapter;
import com.udacityprojects.newsapp.models.Article;
import com.udacityprojects.newsapp.models.Category;
import com.udacityprojects.newsapp.utilities.DataUtils;
import com.udacityprojects.newsapp.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HeadlinesActivity extends AppCompatActivity {

    private String category;
    private List<Article> headlines;
    private HeadlinesFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_headlines);

        category = getIntent().getStringExtra(Category.PARCELABLE_KEY);
        category = category.toLowerCase();

        Log.d("HeadlineActivity", "onCreate: " + category);
        new LoadHeadlinesTask().execute();


    }

    private class LoadHeadlinesTask extends AsyncTask<Void, Void, List<Article>> {
        @Override
        protected List<Article> doInBackground(Void... voids) {
            try {
                String json = NetworkUtils.getHttpResponse(NetworkUtils.buildHeadlineUrl("us",category).toString());

                headlines = DataUtils.getNewsDetails(json);
                return headlines;

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            super.onPostExecute(articles);
            fragment = new HeadlinesFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.headlines_container,fragment).commit();
        }
    }

    public String getCategory() {
        return category;
    }

    public List<Article> getHeadlines() {
        return headlines;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.headlines_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.action_bookmarks:
                startActivity(new Intent(HeadlinesActivity.this,BookmarksActivity.class));
                return true;
            case R.id.action_refresh:
                new LoadHeadlinesTask().execute();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
