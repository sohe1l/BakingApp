package com.example.android.bakingapp.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.bakingapp.R;
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
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeStepFragment extends Fragment {



    private Recipe recipe;
    private Step step;
    int stepIndex;
    UpdateActionBarTitle mUpdateActionBarTitle;
    private final String RECIPE_BUNDLE_KEY = "recipe_bundle_key";
    private final String RECIPE_STEP_BUNDLE_KEY = "recipe_step_bundle_key";


    @BindView(R.id.player_view)
    PlayerView playerView;

    @BindView(R.id.tv_recipe_step_short_desc_frag)
    TextView mShortDescTV;

    @BindView(R.id.tv_recipe_step_desc)
    TextView mDescTV;

    @BindView(R.id.recipe_step_next)
    Button mNextBtn;

    @BindView(R.id.recipe_step_prev)
    Button mPrevBtn;


    SimpleExoPlayer player;

    public RecipeStepFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(RECIPE_BUNDLE_KEY);
            stepIndex = savedInstanceState.getInt(RECIPE_STEP_BUNDLE_KEY);
        }

        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        ButterKnife.bind(this, view);

        mNextBtn.setOnClickListener(changeStep);
        mPrevBtn.setOnClickListener(changeStep);

        updateInfo();

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            mUpdateActionBarTitle = (UpdateActionBarTitle) context;
        } catch (ClassCastException e) {
            // throw new ClassCastException(context.toString()
            //        + " must implement UpdateActionBarTitle");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
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
        player = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector);

        playerView.setPlayer(player);

        DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getContext(), getString(R.string.app_name)));

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
            mUpdateActionBarTitle.updateTitle(String.format(getString(R.string.recipe_step_title_bar), recipe.getName(), stepIndex+1 ));

        }catch(Exception e){
            e.printStackTrace();
        }
    }

//    public void nextStep(View view) {
//
//
//        if(stepIndex < recipe.getSteps().size() - 1 ){
//            stepIndex++;
//            updateInfo();
//        }
//    }
//    public void prevStep(View view) {
//
//
//        if(stepIndex > 0){
//            stepIndex--;
//            updateInfo();
//        }
//    }

    private View.OnClickListener changeStep = new View.OnClickListener() {
        @Override
        public void onClick(View v){
            switch (v.getId()) {
                case R.id.recipe_step_next:
                    if(stepIndex < recipe.getSteps().size() - 1 ){
                        stepIndex++;
                        updateInfo();
                    }
                    break;
                case R.id.recipe_step_prev:
                    if(stepIndex > 0){
                        stepIndex--;
                        updateInfo();
                    }
                    break;
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(player != null){
            player.release();
        }
    }

    public void setRecipe(Recipe recipe) {
        this.recipe = recipe;
    }

    public void setStepIndex(int stepIndex) {
        this.stepIndex = stepIndex;
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelable(RECIPE_BUNDLE_KEY, recipe);
        currentState.putInt(RECIPE_STEP_BUNDLE_KEY, stepIndex);
    }
}
