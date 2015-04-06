package com.robertbrooks.dev_audio;

import android.app.Activity;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;


public class MainActivity extends Activity implements MediaPlayer.OnPreparedListener {

    private static final String SAVE_POSITION = "MainActivity.SAVE_POSITION";

    MediaPlayer mPlayer;
    boolean mActivityResumed;
    boolean mPrepared;
    int mAudioPosition;
    String track1;
    String track2;
    String track3;
    ArrayList<String> trackList;
    final int[] songs = {R.raw.gimme_shelter, R.raw.brown_sugar, R.raw.doom_and_gloom};




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create list of audio tracks from raw folder
        trackList = new ArrayList<>();
        track1 = "android.resource://" + getPackageName() + "/raw/gimme_shelter";
        track2 = "android.resource://" + getPackageName() + "/raw/brown_sugar";
        track3 = "android.resource://" + getPackageName() + "/raw/doom_and_gloom";
        trackList.add(track1);
        trackList.add(track2);
        trackList.add(track3);
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
        Intent intent = new Intent(this, playerService.class);
        startService(intent);
        mPlayer.start();
    }

    public void pause(View v) {
        mPlayer.pause();
    }

    public void next(View v) throws IOException {
        mPlayer.reset();
        mPlayer.setDataSource(this, Uri.parse(track2));
        mPlayer.prepare();
        mPlayer.start();
    }

    public void previous(View v) throws IOException {
        mPlayer.reset();
        mPlayer.setDataSource(this, Uri.parse(track1));
        mPlayer.prepare();
        mPlayer.start();
    }

    public void stop(View v) throws IOException {
        Intent intent = new Intent(this, playerService.class);
        stopService(intent);
        mPlayer.stop();

    }

}
