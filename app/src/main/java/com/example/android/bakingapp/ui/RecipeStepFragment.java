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
import android.widget.ImageView;
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
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;


public class RecipeStepFragment extends Fragment {



    private Recipe recipe;
    private Step step;
    int stepIndex;
    UpdateActionBarTitle mUpdateActionBarTitle;
    private final String RECIPE_BUNDLE_KEY = "recipe_bundle_key";
    private final String RECIPE_STEP_BUNDLE_KEY = "recipe_step_bundle_key";

    private final String EXO_POSITION_BUNDLE_KEY = "exo_position_bundle_key";
    private final String EXO_PLAY_WHEN_READY_BUNDLE_KEY = "exo_play_when_ready_bundle_key";

    private long exo_position;
    private boolean exo_play_when_ready;



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

    @BindView(R.id.iv_recipe_step_thumbnail)
    ImageView mThumbnail;


    SimpleExoPlayer player;

    public RecipeStepFragment() {

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        if(savedInstanceState != null) {
            recipe = savedInstanceState.getParcelable(RECIPE_BUNDLE_KEY);
            stepIndex = savedInstanceState.getInt(RECIPE_STEP_BUNDLE_KEY);
            exo_position = savedInstanceState.getLong(EXO_POSITION_BUNDLE_KEY);
            exo_play_when_ready = savedInstanceState.getBoolean(EXO_PLAY_WHEN_READY_BUNDLE_KEY);
        }

        View view = inflater.inflate(R.layout.fragment_recipe_step, container, false);

        ButterKnife.bind(this, view);

        mNextBtn.setOnClickListener(changeStep);
        mPrevBtn.setOnClickListener(changeStep);

        updateInfo();

        return view;
    }


    private void updateInfo() {
        try{
            step = recipe.getSteps().get(stepIndex);
            mShortDescTV.setText(step.getShortDescription());
            mDescTV.setText(step.getDescription());

            if(step.getThumbnailURL().equals("")){
                mThumbnail.setVisibility(View.INVISIBLE);
            }else{
                Picasso.with(getContext()).load(step.getThumbnailURL())
                        .placeholder(R.drawable.ic_local_pizza_black_24dp)
                        .error(R.drawable.ic_error_outline_black_24dp)
                        .into(mThumbnail);
                mThumbnail.setVisibility(View.VISIBLE);
            }

            //Update Action Bar Text
            mUpdateActionBarTitle.updateTitle(String.format(getString(R.string.recipe_step_title_bar), recipe.getName(), stepIndex+1 ));
        }catch(Exception e){
            e.printStackTrace();
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || player == null)) {
            showVideo();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            showVideo();
        }
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


    private void showVideo(){
        if(player != null){
            player.release();
        }

        if(step == null){
            return;
        }

        String url = step.getVideoURL();

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

        player.seekTo(exo_position);
        player.setPlayWhenReady(exo_play_when_ready); // when enough data is buffered
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

            exo_position = 0;
            exo_play_when_ready = false;

            switch (v.getId()) {
                case R.id.recipe_step_next:
                    if(stepIndex < recipe.getSteps().size() - 1 ){
                        stepIndex++;
                        updateInfo();
                        showVideo();
                    }
                    break;
                case R.id.recipe_step_prev:
                    if(stepIndex > 0){
                        stepIndex--;
                        updateInfo();
                        showVideo();
                    }
                    break;
            }
        }
    };


    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
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
        currentState.putLong(EXO_POSITION_BUNDLE_KEY, exo_position);
        currentState.putBoolean(EXO_PLAY_WHEN_READY_BUNDLE_KEY, exo_play_when_ready);
    }

    @Override
    public void onPause() {
        super.onPause();
        if(player != null){
            exo_position = player.getCurrentPosition();
            exo_play_when_ready = player.getPlayWhenReady();
            if (Util.SDK_INT <= 23) {
                releasePlayer();
            }
        }


    }


    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    private void releasePlayer(){
        if(player != null){
            player.release();
        }
    }




}
