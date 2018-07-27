package com.example.android.bakingapp.widget;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;
import com.example.android.bakingapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ListViewWidgetService extends RemoteViewsService {

    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        String ingKey = getApplicationContext().getString(R.string.widget_ing_key);
        String[] ingredients = {};
        if(intent.hasExtra(ingKey)){
            ingredients = intent.getStringArrayExtra(ingKey);
        }

        return new AppWidgetListView (this.getApplicationContext(), ingredients);
    }
}

class AppWidgetListView implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    String[] ingredients;

    public AppWidgetListView(Context applicationContext, String[] ingredients ) {
        this.context=applicationContext;
        this.ingredients = ingredients;
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredients.length;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.recipe_widget_list_item);
        views.setTextViewText(R.id.widget_ing_text, ingredients[position]);
        return views;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
