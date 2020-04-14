package com.udacityprojects.newsapp;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;
import com.udacityprojects.newsapp.models.Article;
import com.udacityprojects.newsapp.ui.DetailActivity;

import java.io.IOException;
import java.util.List;

/**
 * Implementation of App Widget functionality.
 */
public class HeadlinesWidgetProvider extends AppWidgetProvider {

    static RemoteViews views;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, List<Article> articles, int position,
                                int appWidgetId) {


        // Construct the RemoteViews object
        views = new RemoteViews(context.getPackageName(), R.layout.headlines_widget_provider);
        views.setTextViewText(R.id.page_num_tv,position + "/" + articles.size());
        views.setTextViewText(R.id.widget_title_tv,articles.get(position - 1).getTitle());

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(Article.PARCELABLE_KEY,articles.get(position - 1));
        PendingIntent pendingIntent = PendingIntent.getActivity(context,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_layout,pendingIntent);

        Intent nextIntent = new Intent(context,WidgetService.class);
        nextIntent.setAction(WidgetService.ACTION_SHOW_NEXT);
        nextIntent.putExtra(WidgetService.EXTRA_CURRENT,position);
        PendingIntent nextPendingIntent = PendingIntent.getService(context,0,nextIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_next_iv,nextPendingIntent);

        Intent previousIntent = new Intent(context,WidgetService.class);
        previousIntent.setAction(WidgetService.ACTION_SHOW_PREVIOUS);
        previousIntent.putExtra(WidgetService.EXTRA_CURRENT,position);
        PendingIntent previousPendingIntent = PendingIntent.getService(context,0,previousIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.widget_previous_iv,previousPendingIntent);


        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
    }

    public static void updateHeadlinesWidget(Context context, AppWidgetManager appWidgetManager, List<Article> articles, int position,
                                             int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds ){
            updateAppWidget(context,appWidgetManager,articles,position,appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

