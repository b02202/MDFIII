/*MainActivity.java
* Robert Brooks*/
package com.robertbrooks.project1;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;


import com.robertbrooks.project1.Fragments.MediaLandscapeFrag;
import com.robertbrooks.project1.Fragments.MediaPlayerFragment;

import java.io.IOException;


public class MainActivity extends Activity {
    public static String TAG = "MainActivity";

    MediaPlayerFragment mediaPlayerFragment;
    MediaLandscapeFrag mediaLandscapeFrag;


    PlayerService playerSrv;
    Intent playIntent;
    private boolean playerBound = false;
    private boolean landscape;
    private boolean mPaused;

    TextView titleText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null && landscape ) {
            setLand();
        } else {
            setPortrait();
        }

        // bind to and start PlayerService
        playIntent = new Intent(this, PlayerService.class);
        bindService(playIntent, playerConnect, Context.BIND_AUTO_CREATE);
        startService(playIntent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // check orientation
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setLand();

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
            setPortrait();
        }
    }

    // Connect to PlayerService
    ServiceConnection playerConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.BoundServiceBinder binder = (PlayerService.BoundServiceBinder) service;
            playerSrv = binder.getService();
            Log.d(TAG, "ServiceConnected");
            bindService(playIntent, playerConnect, Context.BIND_AUTO_CREATE);
            // set song title
            //setTitle();

            playerBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            playerBound = false;
        }
    };


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



    @Override
    protected void onDestroy() {
        super.onDestroy();
        // unbind service
        if (playerBound && !mPaused) {
            unbindService(playerConnect);
            stopService(playIntent);
            playerBound = false;
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        mPaused = true;
    }

    // Media Play
    public void playTrack(View view) throws IOException {
        playerSrv.playTrack();
        setTitle();
    }

    public void stopTrack(View view) {
        playerSrv.stopPlayer();
    }

    public void pauseTrack(View view) {
        playerSrv.onPause();
    }

    public void playPrevious(View view) throws IOException {
        playerSrv.playPreviousTrack();
        setTitle();
    }

    public void playNext(View view) throws IOException {
        playerSrv.playNextTrack();
        setTitle();
    }

    // set track title
    public void setTitle() {
        titleText = (TextView) findViewById(R.id.textView);
        String trackString = "";
        int trackIndex = playerSrv.getSongTitle();
        if (trackIndex == 0) {
            trackString = "Gimme Shelter";
        } else if (trackIndex == 1) {
            trackString = "Brown Sugar";
        } else if (trackIndex == 2) {
            trackString = "Doom and Gloom";
        }
        Log.i(TAG, "Track Index (MA) = " + trackIndex);
        //titleText.setText(trackString);
        titleText.setText(trackString);
    }

    // toggle button implementation
    public void toggleClick(View v) {
        // check to see if toggle is on
        boolean on = ((ToggleButton) v).isChecked();

        if (on) {
            playerSrv.loopFunction();
        } else {
            playerSrv.notLooping();
        }
    }



    // set portrait frag
    public void setPortrait() {
        mediaPlayerFragment = MediaPlayerFragment.newInstance();
        getFragmentManager().beginTransaction()
                .replace(R.id.mediaFragContainer, mediaPlayerFragment, MediaPlayerFragment.TAG)
                .commit();
    }

    public void setLand() {
        mediaLandscapeFrag = MediaLandscapeFrag.newInstance();
        getFragmentManager().beginTransaction()
                .replace(R.id.mediaFragContainer, mediaLandscapeFrag, MediaLandscapeFrag.TAG)
                .commit();
    }




}
