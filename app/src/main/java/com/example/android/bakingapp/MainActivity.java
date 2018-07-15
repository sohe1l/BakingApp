package com.example.android.bakingapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

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

public class MainActivity extends AppCompatActivity
    implements RecyclerClickListener{

    @BindView(R.id.rv_recipes) RecyclerView mRecipesRV;
    ProgressDialog progressDialog;

    private List<Recipe> recipesList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


        ButterKnife.bind(this);

        progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Loading....");
        progressDialog.show();


        /*Create handle for the RetrofitInstance interface*/
        GetDataService service = RetrofitClientInstance.getRetrofitInstance().create(GetDataService.class);
        Call<List<Recipe>> call = service.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                progressDialog.dismiss();

                Log.d("Main", response.toString());
                recipesList = response.body();
                generateDataList();

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void generateDataList() {

        RecipesAdapter mRecipesAdapter = new RecipesAdapter(this, recipesList, this);

        GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, getResources().getInteger(R.integer.recipes_col_count));
        mRecipesRV.setLayoutManager(layoutManager);
        mRecipesRV.setHasFixedSize(true);
        mRecipesRV.setAdapter(mRecipesAdapter);
    }


    @Override
    public void onRecyclerItemClicked(int index) {
        Intent recipeSteps = new Intent(this, RecipeStepsActivity.class);
        recipeSteps.putExtra(getString(R.string.recipe_steps_intent_key), recipesList.get(index));
        startActivity(recipeSteps);
    }
}
