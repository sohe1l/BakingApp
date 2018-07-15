package com.example.android.bakingapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.android.bakingapp.adapter.RecipeStepsAdapter;
import com.example.android.bakingapp.adapter.RecipesAdapter;
import com.example.android.bakingapp.adapter.RecyclerClickListener;
import com.example.android.bakingapp.model.Recipe;
import com.example.android.bakingapp.model.Step;
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

public class RecipeStepActivity extends AppCompatActivity{


    private Recipe recipe;
    private Step step;
    int stepIndex;

    @BindView(R.id.player_view)
    PlayerView playerView;

    @BindView(R.id.tv_recipe_step_short_desc)
    TextView mShortDescTV;

    @BindView(R.id.tv_recipe_step_desc)
    TextView mDescTV;

    SimpleExoPlayer player;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_step);

        ButterKnife.bind(this);

        Intent creatingIntent = getIntent();
        if(creatingIntent.hasExtra(getString(R.string.recipe_step_recipe_intent_key))) {
            recipe = creatingIntent.getParcelableExtra(getString(R.string.recipe_step_recipe_intent_key));
            if (creatingIntent.hasExtra(getString(R.string.recipe_step_selected_step_intent_key))) {
                stepIndex = creatingIntent.getIntExtra(getString(R.string.recipe_step_selected_step_intent_key), -1);
                updateInfo();
            }
        }








    }

    private void showVideo(String url){
        if(player != null){
            player.release();
        }

        if(url.equals("")){
            playerView.setVisibility(View.GONE);
            return;
        }

        playerView.setVisibility(View.VISIBLE);

        // 1. Create a default TrackSelector
        Handler mainHandler = new Handler();
        BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();
        TrackSelection.Factory videoTrackSelectionFactory =
                new AdaptiveTrackSelection.Factory(bandwidthMeter);
        DefaultTrackSelector trackSelector =
                new DefaultTrackSelector(videoTrackSelectionFactory);

        // 2. Create the player
        player =
                ExoPlayerFactory.newSimpleInstance(this, trackSelector);

        playerView.setPlayer(player);




        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(this, Util.getUserAgent(this, "yourApplicationName"));


        Uri uri = Uri.parse(url);

        MediaSource videoSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(uri);
        player.prepare(videoSource);
    }


    private void updateInfo() {
        try{
            step = recipe.getSteps().get(stepIndex);
            mShortDescTV.setText(step.getShortDescription());
            mDescTV.setText(step.getDescription());
            showVideo(step.getVideoURL());

            //Update Action Bar Text
            setTitle(String.format(getString(R.string.recipe_step_title_bar), recipe.getName(), stepIndex+1 ));

        }catch(Exception e){
            e.printStackTrace();
        }


    }

    public void nextStep(View view) {


        if(stepIndex < recipe.getSteps().size() - 1 ){
            stepIndex++;
            updateInfo();
        }
    }

    public void prevStep(View view) {


        if(stepIndex > 0){
            stepIndex--;
            updateInfo();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        player.release();
    }
}
