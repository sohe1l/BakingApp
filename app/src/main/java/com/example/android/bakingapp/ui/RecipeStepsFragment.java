package com.example.android.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.bakingapp.R;
import com.example.android.bakingapp.RecipeStepActivity;
import com.example.android.bakingapp.RecipeStepsActivity;
import com.example.android.bakingapp.adapter.RecipeStepsAdapter;
import com.example.android.bakingapp.adapter.RecyclerClickListener;
import com.example.android.bakingapp.model.Recipe;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeStepsFragment extends Fragment{

// implements RecyclerClickListener {


    @BindView(R.id.tv_recipe_steps_ingredients)  TextView mIngredientsTV;

    @BindView(R.id.rv_recipe_steps) RecyclerView mStepsRV;

    private Recipe recipe;

    private RecyclerClickListener recyclerCallback;

    private final String RECIPE_BUNDLE_KEY = "recipe_bundle_key";

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;

    }

    @Override
    public void onAttach(Context context){
        super.onAttach(context);
        try{
            recyclerCallback = (RecyclerClickListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(context.toString() + " RecyclerClickListener not implemented.");
        }

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(RECIPE_BUNDLE_KEY);
        }



        View view = inflater.inflate(R.layout.fragment_recipe_steps, container, false);

        ButterKnife.bind(this, view);

        updateView();

        return view;
    }


    private void updateView(){

        mIngredientsTV.setText(recipe.getIngredientsList(getContext().getString(R.string.recipe_steps_ingredients_format)));

        // Set up steps Recycler View
        RecipeStepsAdapter recipeStepsAdapter = new RecipeStepsAdapter(getContext(), recipe.getSteps(), recyclerCallback);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        mStepsRV.setLayoutManager(layoutManager);
        mStepsRV.setHasFixedSize(true);
        mStepsRV.setAdapter(recipeStepsAdapter);
    }


    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable(RECIPE_BUNDLE_KEY, recipe);
    }

}
