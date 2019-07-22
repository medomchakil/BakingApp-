package com.example.medo.bakingapp.ui.details;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.medo.bakingapp.R;
import com.example.medo.bakingapp.data.model.Step;
import com.example.medo.bakingapp.databinding.StepDetailBinding;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;

import java.util.Objects;


public class StepDetailFragment extends Fragment implements Player.EventListener, View.OnClickListener {
    public static final String LOG_TAG = StepDetailFragment.class.getSimpleName();
    public static final String EXTRA_STEP = "com.example.medo.bakingapp.ui.details.step";
    public static final String EXTRA_STEP_POSITION = "com.example.medo.bakingapp.ui.details.page.position";
    public static final int CURRENT_PAGE_POSITION = 0;
    OnStepClickListener mListener;
    private ImageView mImageView;
    private SimpleExoPlayer mExoPlayer;
    private PlayerView mPlayerView;
    private TextView mDescTv, mDescHeaderTv;
    private long mPlaybackPosition;
    private int orientation;
    private static final String ARG_POSITION = "com.example.medo.bakingapp.ui.details.playback.position";
    private boolean isTablet;
    private int mLastPage;
    Context mContext;
    String videoUrl;
    Step step;

    public static StepDetailFragment newInstance(Step step, int position) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle b = new Bundle();
        b.putParcelable(EXTRA_STEP, step);
        b.putInt(EXTRA_STEP_POSITION, position);
        fragment.setArguments(b);
        return fragment;
    }

    public static StepDetailFragment newInstance(Step step) {
        StepDetailFragment fragment = new StepDetailFragment();
        Bundle b = new Bundle();
        b.putParcelable(EXTRA_STEP, step);
        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            step = getArguments().getParcelable(EXTRA_STEP);
            mLastPage = getArguments().getInt(EXTRA_STEP_POSITION);
        }
        videoUrl = step != null ? step.getVideoURL() : null;
        if (savedInstanceState != null) {
            mPlaybackPosition = savedInstanceState.getLong( ARG_POSITION );
            if (mExoPlayer != null && mExoPlayer.getPlayWhenReady()) {
                mPlaybackPosition = mExoPlayer.getCurrentPosition();
                mExoPlayer.setPlayWhenReady(false);
            }
        }else {
            mPlaybackPosition = 0;
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        StepDetailBinding binding = StepDetailBinding.inflate(inflater, container, false);
        binding.setSteps(step);
        int stepId = step != null ? step.getId() : 0;
        mContext = getActivity();
        mPlayerView = binding.playerView;
        mImageView = binding.errorImage;
        mDescTv = binding.stepDetail;
        mDescHeaderTv = binding.header;
        isTablet = getResources().getBoolean(R.bool.isTablet);
        orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            fullScreen();
        } else {
            Button mNextBtn = binding.nextButton;
            mNextBtn.setOnClickListener(this);
            Button mPrevBtn = binding.previousButton;
            mPrevBtn.setOnClickListener(this);
            TextView mStepIndicator = binding.stepId;
            String currentStep = "Step  " + String.valueOf(stepId);
            mStepIndicator.setText(currentStep);
            if (stepId == CURRENT_PAGE_POSITION) {
                mPrevBtn.setVisibility(View.INVISIBLE);
            } else if (stepId == mLastPage) {
                mNextBtn.setText(getString(R.string.finish));
            }
        }
        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.next_button:
                mListener.onNextStepClick(step);
                break;
            case R.id.previous_button:
                mListener.onPrevStepClick(step);
                break;
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnStepClickListener) {
            mListener = (OnStepClickListener) context;
        } else {
            throw new ClassCastException(context.toString()
                    + getString(R.string.error_interface));
        }
    }

    // initialise exo player
    public void initPlayer() {
        if (mExoPlayer == null && !(videoUrl.isEmpty())) {
            mPlayerView.setVisibility(View.VISIBLE);
            TrackSelector trackSelector = new DefaultTrackSelector();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, trackSelector);
            mPlayerView.setPlayer(mExoPlayer);
            mExoPlayer.addListener(this);
            String userAgent = Util.getUserAgent(mContext, getString(R.string.app_name));
            DataSource.Factory dataSourceFactory =
                    new DefaultDataSourceFactory(mContext, userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(Uri.parse(videoUrl));
            mExoPlayer.prepare(mediaSource);
            mExoPlayer.seekTo(mPlaybackPosition);
            mExoPlayer.setPlayWhenReady(true);
        } else {
            if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
                mPlayerView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
                mDescTv.setVisibility(View.VISIBLE);
                mDescHeaderTv.setVisibility(View.VISIBLE);
            } else {
                mPlayerView.setVisibility(View.GONE);
                mImageView.setVisibility(View.VISIBLE);
            }
        }
    }

    void releasePlayer() {
        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }


    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void hideSystemUI() {
        Objects.requireNonNull(((AppCompatActivity) Objects.requireNonNull(getActivity())).getSupportActionBar()).hide();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN

                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE);
        }
        getActivity().getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onResume() {
        super.onResume();
        initPlayer();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null)
            mPlaybackPosition = mExoPlayer.getContentPosition();
        if (Util.SDK_INT <= 23) {
            releasePlayer();
        }
    }
    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23) {
            releasePlayer();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }


    public boolean isPause() {
        return mExoPlayer != null && !mExoPlayer.getPlayWhenReady();
    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playWhenReady && playbackState == Player.STATE_READY) {
            Log.d(LOG_TAG, "Player is playing");
        } else if (playbackState == Player.STATE_READY) {
            Log.d(LOG_TAG, "Player is paused");
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putLong(ARG_POSITION, mPlaybackPosition);
        super.onSaveInstanceState(outState);
    }

    private void fullScreen() {
        if (!videoUrl.isEmpty() && !isTablet) {
            hideSystemUI();
            mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        }
    }


    public interface OnStepClickListener {
        void onPrevStepClick(Step step);

        void onNextStepClick(Step step);
    }
}
