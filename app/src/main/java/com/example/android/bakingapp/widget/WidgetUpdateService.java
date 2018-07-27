package com.example.android.bakingapp.widget;

import android.app.IntentService;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;

import com.example.android.bakingapp.R;


public class WidgetUpdateService extends IntentService {

    public static final String ACTION_UPDATE_LIST_VIEW = "learn.tasha.widgetexample.widgetupdateservice.update_app_widget_list";

    public WidgetUpdateService() {
        super("WidgetUpdateService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if(ACTION_UPDATE_LIST_VIEW.equals(action)){

                AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
                int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));

                RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds);

                appWidgetManager.notifyAppWidgetViewDataChanged(appWidgetIds, R.id.listViewWidget);

            }
        }
    }




}