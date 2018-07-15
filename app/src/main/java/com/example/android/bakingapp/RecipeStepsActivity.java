package com.example.android.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.android.bakingapp.adapter.RecipeStepsAdapter;
import com.example.android.bakingapp.adapter.RecyclerClickListener;
import com.example.android.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsActivity extends AppCompatActivity implements RecyclerClickListener{

    private Recipe recipe;
    @BindView(R.id.tv_recipe_steps_ingredients) TextView mIngredientsTV;
    @BindView(R.id.rv_recipe_steps) RecyclerView mStepsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        ButterKnife.bind(this);


        Intent creatingIntent = getIntent();
        if(creatingIntent.hasExtra(getString(R.string.recipe_steps_intent_key))){

            recipe = creatingIntent.getParcelableExtra(getString(R.string.recipe_steps_intent_key));

            mIngredientsTV.setText(recipe.getIngredientsList(this));

            // Set up steps Recycler View
            RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(this, recipe.getSteps(), this);
            LinearLayoutManager layoutManager = new LinearLayoutManager(RecipeStepsActivity.this);
            mStepsRV.setLayoutManager(layoutManager);
            mStepsRV.setHasFixedSize(true);
            mStepsRV.setAdapter(recipeStepsAdapter);

        }
    }

    @Override
    public void onRecyclerItemClicked(int index) {
        Intent stepIntent = new Intent(this, RecipeStepActivity.class);
        stepIntent.putExtra(getString(R.string.recipe_step_recipe_intent_key), recipe);
        stepIntent.putExtra(getString(R.string.recipe_step_selected_step_intent_key), index);
        startActivity(stepIntent);
    }
}
