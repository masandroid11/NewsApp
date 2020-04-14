package com.udacityprojects.newsapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacityprojects.newsapp.HeadlinesWidgetProvider;
import com.udacityprojects.newsapp.R;
import com.udacityprojects.newsapp.adapters.TabPagerAdapter;
import com.udacityprojects.newsapp.models.Article;
import com.udacityprojects.newsapp.utilities.DataUtils;
import com.udacityprojects.newsapp.utilities.NetworkUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.viewPager) ViewPager pager;
    @BindView(R.id.tabLayout) TabLayout tabLayout;
    private List<Article> headlines;
    private TabPagerAdapter adapter;
    private DatabaseReference bookmarksDb;
    private List<Article> bookmarkList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bookmarksDb = FirebaseDatabase.getInstance().getReference(DataUtils.DATABASE_PATH);

        ButterKnife.bind(this);

        if (NetworkUtils.isConnectedToNetwork(this)){
            new LoadHeadlinesTask().execute();
        }else {
            noConnectionMessage();
        }



    }

    private void noConnectionMessage(){
        Toast.makeText(this,R.string.no_connection_err,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        ComponentName provider = new ComponentName(this, HeadlinesWidgetProvider.class);

        bookmarksDb.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                bookmarkList.clear();
                for ( DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Article article = snapshot.getValue(Article.class);
                    bookmarkList.add(article);
                }

                if (bookmarkList != null){
                    int[] ids = appWidgetManager.getAppWidgetIds(provider);
                    if (bookmarkList.size() == 0){
                        HeadlinesWidgetProvider.updateHeadlinesWidget(getApplicationContext(),appWidgetManager,bookmarkList,-1,ids);
                    }else {
                        HeadlinesWidgetProvider.updateHeadlinesWidget(getApplicationContext(),appWidgetManager,bookmarkList,1,ids);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private class LoadHeadlinesTask extends AsyncTask<Void, Void, List<Article>>{
        @Override
        protected List<Article> doInBackground(Void... voids) {
            try {
                String json = NetworkUtils.getHttpResponse(NetworkUtils.buildHeadlineUrl(NetworkUtils.COUNTRY_PREF).toString());

                headlines = DataUtils.getNewsDetails(json);
                return headlines;

            }catch (Exception e){
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),R.string.loading_headlines_err,Toast.LENGTH_LONG).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(List<Article> articles) {
            super.onPostExecute(articles);

            if (articles != null){
                adapter = new TabPagerAdapter(getSupportFragmentManager(),getApplicationContext());
                pager.setAdapter(adapter);
                tabLayout.setupWithViewPager(pager);
            }
        }
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
                startActivity(new Intent(MainActivity.this,BookmarksActivity.class));
                return true;
            case R.id.action_refresh:
                if (NetworkUtils.isConnectedToNetwork(this)){
                    new LoadHeadlinesTask().execute();
                }else {
                    noConnectionMessage();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public List<Article> getHeadlines() {
        return headlines;
    }
}
