package com.example.android.bakingapp;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Debug;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.android.bakingapp.adapter.RecipesAdapter;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.network.GetDataService;
import com.example.android.bakingapp.network.RetrofitClientInstance;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecipesAdapter mRecipesAdapter;
    private RecyclerView mRecipesRV;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);


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

                generateDataList(response.body());

            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(MainActivity.this, "Something went wrong...Please try later!", Toast.LENGTH_SHORT).show();
            }
        });



    }


    private void generateDataList(List<Recipe> recipesList) {

        if(recipesList == null){
            Log.d("Main", "Recipes List is NULL");
        }else{
            Log.d("Main", "Recipes List is NOT NULL");
        }

        mRecipesRV = findViewById(R.id.rv_recipes);
        mRecipesAdapter = new RecipesAdapter(this, recipesList);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        mRecipesRV.setLayoutManager(layoutManager);
        mRecipesRV.setHasFixedSize(true);
        mRecipesRV.setAdapter(mRecipesAdapter);
    }




}
