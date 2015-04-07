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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;



public class MainActivity extends Activity implements ServiceConnection,View.OnClickListener {
    private static final int FOREGROUND_NOTIFICATION = 0x01001;
    // Buttons
    Button mPlayButton;
    Button mStopButton;
    Intent intent;
    Intent bindIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPlayButton = (Button) this.findViewById(R.id.play_btn);
        mStopButton = (Button) this.findViewById(R.id.stop_btn);

        mPlayButton.setOnClickListener(this);
        mStopButton.setOnClickListener(this);

        intent = new Intent(this, PlayerService.class);

        Intent bindIntent = new Intent(this, PlayerService.class);


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

    @Override
    public void onClick(View v) {
        //TODO: CHANGE TO SWITCH CASE WHEN FORWARD AND BACK BUTTONS ARE ADDED
        if (v == mPlayButton) {
            bindService(bindIntent, this, Context.BIND_AUTO_CREATE);
            startService(intent);

        } else if (v == mStopButton) {
            unbindService(this);
            stopService(intent);

        }
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {

        PlayerService.BoundServiceBinder binder = (PlayerService.BoundServiceBinder) service;
        PlayerService theService = binder.getService();
        theService.showToast();
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {

    }


        @Override
        protected void onStart () {
            super.onStart();

            bindIntent = new Intent(this, PlayerService.class);
            bindService(bindIntent, this, Context.BIND_AUTO_CREATE);
        }


    @Override
    protected void onStop() {
        super.onStop();

        unbindService(this);
    }


}
