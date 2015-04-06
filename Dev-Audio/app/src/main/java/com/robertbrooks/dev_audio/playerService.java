package com.robertbrooks.dev_audio;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.widget.Toast;

/**
 * Created by Bob on 4/2/2015.
 */
public class playerService extends Service implements MediaPlayer.OnPreparedListener {

    MediaPlayer mPlayer;
    boolean mActivityResumed;
    boolean mPrepared;
    int mAudioPosition;

    @Override
    public void onCreate() {
        super.onCreate();
        mPrepared = mActivityResumed = false;
        mAudioPosition = 0;

        mPlayer = MediaPlayer.create(this, R.raw.gimme_shelter);
        Toast.makeText(this, "Player created", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Player Started", Toast.LENGTH_SHORT).show();
        return Service.START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Player Stopped", Toast.LENGTH_SHORT).show();
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {

        // no bind
        return null;
    }

    @Override
    public void onPrepared(MediaPlayer mp) {

    }
}
