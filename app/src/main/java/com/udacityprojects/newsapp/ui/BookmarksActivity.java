package com.udacityprojects.newsapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacityprojects.newsapp.HeadlinesWidgetProvider;
import com.udacityprojects.newsapp.R;
import com.udacityprojects.newsapp.adapters.HeadlinesAdapter;
import com.udacityprojects.newsapp.models.Article;
import com.udacityprojects.newsapp.utilities.DataUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookmarksActivity extends AppCompatActivity implements HeadlinesAdapter.OnClickHandler {

    private DatabaseReference bookmarksDb;
    private List<Article> bookmarkList = new ArrayList<>();
    private HeadlinesAdapter adapter;
    @BindView(R.id.bookmarks_rv)
    RecyclerView bookmarksRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        bookmarksDb = FirebaseDatabase.getInstance().getReference(DataUtils.DATABASE_PATH);

        adapter = new HeadlinesAdapter(this);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this
                ,LinearLayoutManager.VERTICAL,false);
        bookmarksRecyclerView.hasFixedSize();
        bookmarksRecyclerView.setLayoutManager(layoutManager);


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
                adapter.setHeadlines(bookmarkList);
                bookmarksRecyclerView.setAdapter(adapter);

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

    @Override
    public void onClick(int position) {
        Intent intent = new Intent(this,DetailActivity.class);
        intent.putExtra(Article.PARCELABLE_KEY,bookmarkList.get(position));

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bookmarks_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }else if (id == R.id.action_home){
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
        }
        return true;
    }
}
