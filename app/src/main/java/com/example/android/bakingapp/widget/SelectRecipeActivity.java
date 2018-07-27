package com.example.android.bakingapp.widget;

import android.app.ProgressDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingapp.MainActivity;
import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeStepsActivity;
import com.example.android.bakingapp.adapter.RecipesAdapter;
import com.example.android.bakingapp.adapter.RecyclerClickListener;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.network.GetDataService;
import com.example.android.bakingapp.network.RetrofitClientInstance;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectRecipeActivity extends AppCompatActivity  implements RecyclerClickListener{

    @BindView(R.id.rv_recipes) RecyclerView mRecipesRV;
    ProgressDialog progressDialog;
    private List<Recipe> recipesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_recipe);

        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(SelectRecipeActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();


        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Recipe>> call = service.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                progressDialog.dismiss();

                recipesList = response.body();
                generateDataList();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(SelectRecipeActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void generateDataList() {

        RecipesAdapter mRecipesAdapter = new RecipesAdapter(this, recipesList, this);

        GridLayoutManager layoutManager = new GridLayoutManager(SelectRecipeActivity.this, getResources().getInteger(R.integer.recipes_col_count));
        mRecipesRV.setLayoutManager(layoutManager);
        mRecipesRV.setHasFixedSize(true);
        mRecipesRV.setAdapter(mRecipesAdapter);
    }


    @Override
    public void onRecyclerItemClicked(int index) {
        Recipe recipe = recipesList.get(index);

        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor prefEditor = sp.edit();
        prefEditor.putString(getString(R.string.widget_title_key), recipe.getName());
        prefEditor.putStringSet(getString(R.string.widget_ing_key), recipe.getIngredientsSet(getString(R.string.recipe_steps_ingredients_format)) );
        prefEditor.apply();

        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, RecipeWidgetProvider.class));
        RecipeWidgetProvider.updateRecipeWidgets(this, appWidgetManager, appWidgetIds);

        this.finish();
    }

}
