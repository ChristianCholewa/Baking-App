package com.example.android.bakingapp;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.bakingapp.data.Recipe;
import com.example.android.bakingapp.data.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class StepFragment extends Fragment {

    @BindView(R.id.fragment_step_shortDescription) TextView shortDescription;
    @BindView(R.id.fragment_step_description) TextView description;
    @BindView(R.id.playerView) PlayerView playerView;
    @BindView(R.id.fragment_step_thumbnail) ImageView thumbnail;
    private Unbinder unbinder;

    private static final String LOG_TAG = StepFragment.class.getSimpleName();

    private static final String BUNDLE_POSITION = "BUNDLE_POSITION";
    private static final String BUNDLE_PLAYSTATE = "BUNDLE_PLAYSTATE";

    private SimpleExoPlayer simpleExoPlayer;
    private String videoURLString;
    private boolean playWhenReady;
    private long playerPosition;

    private Context context;

    public StepFragment(){}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        context = getContext();

        //similar to onCreate for an activity
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            Log.d(LOG_TAG, "this.getArguments() != null");

            Recipe recipe = bundle.getParcelable(Recipe.EXTRA_RECIPE_DATA);
            if(recipe != null) {
                int selectedStep = bundle.getInt(Recipe.EXTRA_RECIPE_STEP, 1) - 1;
                Step step = recipe.getStepList().get(selectedStep);

                shortDescription.setText(step.getShortDescription());

                if(selectedStep != 0) {
                    description.setText(step.getDescription());
                } else {
                    description.setVisibility(View.GONE);
                }

                videoURLString = step.getVideoUrl();

                if(TextUtils.isEmpty(videoURLString)) {
                    playerView.setVisibility(View.GONE);
                }

                String imageUrl = step.getThumbnailUrl();
                if(!TextUtils.isEmpty(imageUrl)) {
                    Picasso.with(context).load(imageUrl).into(thumbnail);
                } else {
                    thumbnail.setVisibility(View.GONE);
                }
            }
        }

        if(savedInstanceState != null){
            playWhenReady = savedInstanceState.getBoolean(BUNDLE_PLAYSTATE);
            playerPosition = savedInstanceState.getLong(BUNDLE_POSITION);

        } else {
            playWhenReady = true;
            playerPosition = 0;
        }

        return rootView;
    }

    private void initializePlayer(Uri mediaUri){
        if(simpleExoPlayer == null){
            // create instance of media player
            TrackSelector trackSelector = new DefaultTrackSelector();
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(context, trackSelector);
            playerView.setPlayer(simpleExoPlayer);

            // prepare media source
            String userAgent = Util.getUserAgent(context, "@string/app_name");
            MediaSource mediaSource = new ExtractorMediaSource.Factory(new DefaultDataSourceFactory(context, userAgent)).createMediaSource(mediaUri);

            simpleExoPlayer.prepare(mediaSource);
        }

        simpleExoPlayer.seekTo(playerPosition);
        simpleExoPlayer.setPlayWhenReady(playWhenReady);
    }

    private void releasePlayer() {
        if (simpleExoPlayer != null) {
            simpleExoPlayer.stop();
            simpleExoPlayer.release();
            simpleExoPlayer = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23) {
            if(!TextUtils.isEmpty(videoURLString)) {
                initializePlayer(Uri.parse(videoURLString));
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || simpleExoPlayer == null) {
            if(!TextUtils.isEmpty(videoURLString)) {
                initializePlayer(Uri.parse(videoURLString));
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (Util.SDK_INT <= 23) {
            if(simpleExoPlayer != null) {
                playerPosition = simpleExoPlayer.getCurrentPosition();
                playWhenReady = simpleExoPlayer.getPlayWhenReady();
            }

            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            if(simpleExoPlayer != null) {
                playerPosition = simpleExoPlayer.getCurrentPosition();
                playWhenReady = simpleExoPlayer.getPlayWhenReady();
            }

            releasePlayer();
        }
    }

    @Override public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        releasePlayer();

        Log.d(LOG_TAG, "onDestroyView");
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d(LOG_TAG, "onSaveInstanceState");

        if (simpleExoPlayer != null) {
            outState.putLong(BUNDLE_POSITION, simpleExoPlayer.getCurrentPosition());
            outState.putBoolean(BUNDLE_PLAYSTATE, simpleExoPlayer.getPlayWhenReady());
        }
    }
}
