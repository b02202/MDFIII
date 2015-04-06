package com.robertbrooks.project1;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;

import java.io.IOException;

/**
 * Created by Bob on 4/6/2015.
 */
public class PlayerService extends Service implements MediaPlayer.OnCompletionListener {

    MediaPlayer mPlayer;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        mPlayer = MediaPlayer.create(this, R.raw.gimme_shelter);

        mPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        if (!mPlayer.isPlaying()) {
            mPlayer.start();
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }

        mPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopSelf();

    }
}
