package com.example.android.bakingapp;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.android.bakingapp.adapter.RecipeStepsAdapter;
import com.example.android.bakingapp.adapter.RecyclerClickListener;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.ui.RecipeStepFragment;
import com.example.android.bakingapp.ui.RecipeStepsFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepsActivity extends AppCompatActivity implements RecyclerClickListener{

    Recipe recipe;
    boolean mTwoPage = false;
    int stepIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_steps);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        Intent creatingIntent = getIntent();
        if(creatingIntent.hasExtra(getString(R.string.recipe_steps_intent_key))){
            recipe = creatingIntent.getParcelableExtra(getString(R.string.recipe_steps_intent_key));
        }

        if(creatingIntent.hasExtra(getString(R.string.recipe_step_selected_step_intent_key))) {
            stepIndex = creatingIntent.getIntExtra(getString(R.string.recipe_step_selected_step_intent_key), -1);
        }


        if(findViewById(R.id.recipe_step_fragment_frame) != null){
            mTwoPage = true;
        }

        if(savedInstanceState == null) {

            RecipeStepsFragment stepsFragment = new RecipeStepsFragment();

            if(recipe != null){
                stepsFragment.setRecipe(recipe);

                //Update Action Bar Text
                setTitle(recipe.getName());
            }


            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_steps_fragment_frame, stepsFragment)
                    .commit();


            if(mTwoPage) {

                RecipeStepFragment stepFragment = new RecipeStepFragment();

                if(recipe != null){
                    stepFragment.setRecipe(recipe);
                    stepFragment.setStepIndex(stepIndex);
                }

                fragmentManager.beginTransaction()
                        .add(R.id.recipe_step_fragment_frame, stepFragment)
                        .commit();
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onRecyclerItemClicked(int index) {
        if(mTwoPage){
            stepIndex = index;
            RecipeStepFragment stepFragment = new RecipeStepFragment();
             stepFragment.setRecipe(recipe);
             stepFragment.setStepIndex(stepIndex);

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .replace(R.id.recipe_step_fragment_frame, stepFragment)
                    .commit();
        }else{
            Intent stepIntent = new Intent(this, RecipeStepActivity.class);
            stepIntent.putExtra(getString(R.string.recipe_step_recipe_intent_key), recipe);
            stepIntent.putExtra(getString(R.string.recipe_step_selected_step_intent_key), index);
            startActivity(stepIntent);
        }
    }


}
