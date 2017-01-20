package com.guanzhuli.zestate;

import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.widget.VideoView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashActivity extends AppCompatActivity {
    private VideoView mVideoView;
    private View mControlsView;
    private AlphaAnimation fadeIn = new AlphaAnimation(0.0f , 1.0f ) ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        getSupportActionBar().hide();
        mControlsView = findViewById(R.id.fullscreen_content_controls);
        mControlsView.startAnimation(fadeIn);
        fadeIn.setDuration(5000);
        fadeIn.setFillAfter(true);
        mVideoView = (VideoView) findViewById(R.id.fullscreen_content);
        mVideoView.setVideoPath("android.resource://com.guanzhuli.zestate/raw/" + R.raw.splash);
        // video.setLayoutParams(new RelativeLayout.LayoutParams(-1, -1));
        mVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mediaPlayer) {
                mediaPlayer.setLooping(true);
            }
        });
        mVideoView.start();


        // Set up the user interaction to manually show or hide the system UI.

        // Upon interacting with UI controls, delay any scheduled hide()
        // operations to prevent the jarring behavior of controls going away
        // while interacting with the UI.
        // findViewById(R.id.dummy_button).setOnTouchListener(mDelayHideTouchListener);
    }

}
