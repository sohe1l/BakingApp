package com.example.android.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;

import java.util.Set;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        String title = prefs.getString(context.getString(R.string.widget_title_key), context.getString(R.string.app_name));
        Set<String> ingredientsSet = prefs.getStringSet(context.getString(R.string.widget_ing_key), null);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_provider);

        views.setTextViewText(R.id.appwidget_title, title);

        Intent lvIntent = new Intent(context, ListViewWidgetService.class);
        try{
            String ingredients[] = new String[ingredientsSet.size()];
            ingredients = ingredientsSet.toArray(ingredients);
            lvIntent.putExtra(context.getString(R.string.widget_ing_key), ingredients);
        }catch(NullPointerException e){
            e.printStackTrace();
        }
        views.setRemoteAdapter(R.id.listViewWidget, lvIntent);


        Intent intent = new Intent(context, SelectRecipeActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent,0);

        views.setOnClickPendingIntent(R.id.widget_linear_layout, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }


    public static void updateRecipeWidgets(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
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

