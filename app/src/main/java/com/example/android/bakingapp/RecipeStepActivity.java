package com.example.android.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
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
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.adapter.RecipeStepsAdapter;
import com.example.android.bakingapp.adapter.RecipesAdapter;
import com.example.android.bakingapp.adapter.RecyclerClickListener;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
import com.example.android.bakingapp.ui.RecipeStepFragment;
import com.example.android.bakingapp.ui.RecipeStepsFragment;
import com.example.android.bakingapp.ui.UpdateActionBarTitle;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeStepActivity extends AppCompatActivity implements UpdateActionBarTitle {

    private Recipe recipe;
    private Step step;
    int stepIndex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        ActionBar actionBar = this.getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        Intent creatingIntent = getIntent();
        if(creatingIntent.hasExtra(getString(R.string.recipe_step_recipe_intent_key))) {
            recipe = creatingIntent.getParcelableExtra(getString(R.string.recipe_step_recipe_intent_key));
            if (creatingIntent.hasExtra(getString(R.string.recipe_step_selected_step_intent_key))) {
                stepIndex = creatingIntent.getIntExtra(getString(R.string.recipe_step_selected_step_intent_key), -1);
            }
        }


        if(savedInstanceState == null) {

            RecipeStepFragment stepFragment = new RecipeStepFragment();

            if(recipe != null){
                stepFragment.setRecipe(recipe);
                stepFragment.setStepIndex(stepIndex);
            }

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.recipe_step_fragment_frame, stepFragment)
                    .commit();
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
    public void updateTitle(String title) {
        setTitle(title);
    }
}
