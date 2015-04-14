package com.robertbrooks.project1;

import android.app.Activity;
import android.app.NotificationManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import com.robertbrooks.project1.PlayerService.BoundServiceBinder;

import java.io.IOException;


public class MainActivity extends Activity implements View.OnClickListener {
    public static String TAG = "MainActivity";
    private static final int FOREGROUND_NOTIFICATION = 0x01001;
    // Buttons
    Button mPlayButton;
    Button mStopButton;
    Button mPauseButton;
    Button mNextButton;
    Button mPreviousButton;

    Intent intent;
    Intent bindIntent;
    PlayerService playerSrv;
    Intent playIntent;
    private boolean playerBound = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayButton = (Button) this.findViewById(R.id.play_btn);
        mStopButton = (Button) this.findViewById(R.id.stop_btn);
        mPauseButton = (Button) this.findViewById(R.id.pause_btn);
        mNextButton = (Button) this.findViewById(R.id.next_btn);
        mPreviousButton = (Button) this.findViewById(R.id.previous_btn);

        mPlayButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);
        mPauseButton.setOnClickListener(this);
        mNextButton.setOnClickListener(this);
        mPreviousButton.setOnClickListener(this);


        //intent = new Intent(this, PlayerService.class);


        //playIntent = new Intent(this, PlayerService.class);
        playIntent = new Intent(this, PlayerService.class);
        //Intent intent = new Intent(this, PlayerService.class);
       bindService(playIntent, playerConnect, Context.BIND_AUTO_CREATE);
        startService(playIntent);


    }
     // Connect to PlayerService
    ServiceConnection playerConnect = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            PlayerService.BoundServiceBinder binder = (PlayerService.BoundServiceBinder) service;
            playerSrv = binder.getService();
            Log.d(TAG, "ServiceConnected");
            bindService(playIntent, playerConnect, Context.BIND_AUTO_CREATE);

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
    public void onClick(View v) {


        switch (v.getId()) {
            case R.id.play_btn:
                // play track

                try {
                    playerSrv.playTrack();
                } catch (IOException e) {
                    e.printStackTrace();
                }


                break;

            case R.id.stop_btn:

                // Stop track at current position
                if (playerBound) {
                    playerSrv.onPause();
                    Log.d(TAG, "Player is bound and is pausing");
                }
                break;

            case R.id.pause_btn:
                if (playerBound) {
                    playerSrv.onPause();

                }
                break;

            case R.id.previous_btn:
                try {
                    playerSrv.skipback();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.next_btn:
                if (playerBound) {
                    try {
                        playerSrv.skipForward();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.d(TAG, "Player is bound skipping to next song.");
                }
                Log.d(TAG, "Next Button Clicked");

                break;
        }


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (playerBound) {
            unbindService(playerConnect);
            playerBound = false;
        }
    }


}
