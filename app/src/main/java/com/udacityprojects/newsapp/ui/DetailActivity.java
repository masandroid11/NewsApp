package com.udacityprojects.newsapp.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.widget.Toolbar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.udacityprojects.newsapp.R;
import com.udacityprojects.newsapp.models.Article;
import com.udacityprojects.newsapp.utilities.DataUtils;

import java.time.Instant;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    @BindView(R.id.article_image) ImageView articleImage;
    @BindView(R.id.title_tv) TextView title;
    @BindView(R.id.source_name_tv) TextView source;
    @BindView(R.id.article_date_tv) TextView date;
    @BindView(R.id.description_tv) TextView description;
    @BindView(R.id.content_tv) TextView content;
    @BindView(R.id.bookmarked_iv) ImageView bookmarkIcon;

    private final String TAG = getClass().getSimpleName();
    private Article article;
    private AdView mAdView;

    private boolean isBookmarked = false;

    private DatabaseReference bookmarkedDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

        if (getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        article = getIntent().getParcelableExtra(Article.PARCELABLE_KEY);

        setupArticleViews();

        bookmarkedDatabase = FirebaseDatabase.getInstance().getReference(DataUtils.DATABASE_PATH);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onStart() {
        super.onStart();

        bookmarkedDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    Article articleValue = snapshot.getValue(Article.class);
                    if (article.equals(articleValue)){
                        isBookmarked = true;
                    }
                }
                setupBookmarkIcon();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void setupArticleViews(){
        Picasso.get().load(article.getUrlToImage()).into(articleImage);
        title.setText(article.getTitle());
        source.setText(article.getSource().getName());
        date.setText(DataUtils.formatDataTime(article.getPublishedAt()));
        description.setText(article.getDescription());
        content.setText(DataUtils.removeExtraChar(article.getContent()));
    }

    private void setupBookmarkIcon(){
        if(isBookmarked){
            bookmarkIcon.setImageResource(R.drawable.ic_bookmark_24px);
        }else {
            bookmarkIcon.setImageResource(R.drawable.ic_bookmark_border_24px);
        }
    }

    public void onBookmarkClick(View view){
        if (isBookmarked){
            bookmarkedDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                        Article articleValue = snapshot.getValue(Article.class);
                        String id = snapshot.getKey();
                        if (article.equals(articleValue)){
                            bookmarkedDatabase.child(id).removeValue();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }else {
            String id = bookmarkedDatabase.push().getKey();
            bookmarkedDatabase.child(id).setValue(article);
        }
        isBookmarked = !isBookmarked;
        setupBookmarkIcon();
    }

    public void onReadFullStoryClick(View view){
        Uri uri = Uri.parse(article.getUrl());
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        startActivity(intent);
    }

    public void onShareClick(View view){
        String shareMessage = article.getTitle() + "\n" + article.getUrl();
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT,shareMessage);
        intent.setType("text/plain");

        startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.detail_menu,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home){
            finish();
        }else {
            Intent intent = new Intent(this,BookmarksActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
