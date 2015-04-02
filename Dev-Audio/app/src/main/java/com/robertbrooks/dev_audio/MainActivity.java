package com.robertbrooks.dev_audio;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;


public class MainActivity extends ActionBarActivity implements MediaPlayer.OnPreparedListener {

    private static final String SAVE_POSITION = "MainActivity.SAVE_POSITION";

    MediaPlayer mPlayer;
    boolean mActivityResumed;
    boolean mPrepared;
    int mAudioPosition;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrepared = mActivityResumed = false;
        mAudioPosition = 0;

        if(savedInstanceState != null && savedInstanceState.containsKey(SAVE_POSITION)) {
            mAudioPosition = savedInstanceState.getInt(SAVE_POSITION, 0);
        }

    }



        @Override
        protected void onStart () {
            super.onStart();

            mPlayer = new MediaPlayer();
            mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mPlayer.setOnPreparedListener(this);

            try {
                mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/gimme_shelter"));

            } catch (IOException e) {
                e.printStackTrace();
                mPlayer.release();
                mPlayer = null;
            }

        }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPlayer != null) {
            outState.putInt(SAVE_POSITION, mPlayer.getCurrentPosition());
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mActivityResumed = true;
        if (mPlayer != null && !mPrepared) {
            mPlayer.prepareAsync();
        } else if (mPlayer != null && mPrepared) {
            mPlayer.seekTo(mAudioPosition);
            mPlayer.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mActivityResumed = false;
        if (mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.pause();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mPlayer != null && mPlayer.isPlaying()) {
            mPlayer.stop();
            mPrepared = false;

        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mPlayer != null) {
            mPlayer.release();
        }
    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        mPrepared = true;

        if (mPlayer != null && mActivityResumed) {
            mPlayer.seekTo(mAudioPosition);
            mPlayer.start();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void play(View v) {
        mPlayer.start();
    }

    public void stop(View v) {
        mPlayer.pause();
    }

}
