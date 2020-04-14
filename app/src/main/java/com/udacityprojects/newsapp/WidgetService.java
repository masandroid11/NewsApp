package com.udacityprojects.newsapp;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.udacityprojects.newsapp.models.Article;
import com.udacityprojects.newsapp.utilities.DataUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class WidgetService extends IntentService {

    public static final String ACTION_SHOW_NEXT = "com.udacityprojects.newsapp.action.show_next";
    public static final String ACTION_SHOW_PREVIOUS = "com.udacityprojects.newsapp.action.show_previous";

    private DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(DataUtils.DATABASE_PATH);
    private List<Article> articles = new ArrayList<>();

    // TODO: Rename parameters
    public static final String EXTRA_CURRENT = "com.udacityprojects.newsapp.extra.CURRENT";

    public WidgetService() {
        super("WidgetService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionNext(Context context, int current) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_SHOW_NEXT);
        intent.putExtra(EXTRA_CURRENT, current);
        context.startService(intent);
    }

    /**
     * Starts this service to perform action Baz with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionPrevious(Context context, int current) {
        Intent intent = new Intent(context, WidgetService.class);
        intent.setAction(ACTION_SHOW_PREVIOUS);
        intent.putExtra(EXTRA_CURRENT, current);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_SHOW_NEXT.equals(action)) {
                final int param1 = intent.getIntExtra(EXTRA_CURRENT,0);
                handleActionNext(param1);
            } else if (ACTION_SHOW_PREVIOUS.equals(action)) {
                final int param1 = intent.getIntExtra(EXTRA_CURRENT,0);
                handleActionPrevious(param1);
            }
        }
    }


    private void handleActionNext(final int current) {



        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articles.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Article article = snapshot.getValue(Article.class);

                    articles.add(article);
                }
                if (articles != null && (current + 1) <= articles.size()){
                    handleUpdateWidget(articles,current + 1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void handleActionPrevious(final int current) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                articles.clear();
                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Article article = snapshot.getValue(Article.class);

                    articles.add(article);
                }
                if (articles != null && (current - 1) > 0){
                    handleUpdateWidget(articles,current - 1);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void handleUpdateWidget(List<Article> articles, int current){
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(getApplicationContext());
        ComponentName provider = new ComponentName(getApplicationContext(),HeadlinesWidgetProvider.class);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(provider);
        HeadlinesWidgetProvider.updateHeadlinesWidget(getApplicationContext(),appWidgetManager,articles,current,appWidgetIds);
    }
}
