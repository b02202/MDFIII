package com.robertbrooks.project1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import java.io.IOException;

/**
 * Created by Bob on 4/6/2015.
 */
public class PlayerService extends Service  implements MediaPlayer.OnPreparedListener {
    public static final int STANDARD_NOTIFICATION = 0x01001;
    public static final int EXPANDED_NOTIFICATION = 0x01002;
    private static final String SAVE_POSITION = "PlayerService.SAVE_POSITION";
    boolean mActivityResumed;
    boolean mPrepared;
    int mAudioPosition;

    MediaPlayer mPlayer;

    // create binder
    public class BoundServiceBinder extends Binder {
        public PlayerService getService() {
            return PlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new BoundServiceBinder();
    }

    // Binder toast
    public void showToast() {
        Toast.makeText(this, "Text goes here", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onCreate() {

        //mPlayer = MediaPlayer.create(this, R.raw.gimme_shelter);
        mPlayer = new MediaPlayer();
        mPrepared = mActivityResumed = false;
        mAudioPosition = 0;

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // new mPlayer
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

        if (mPlayer != null) {

                mPlayer.prepareAsync();
                mPlayer.start();

            // Notification implementation
            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);

            // Intent
            Intent nIntent = new Intent(this, MainActivity.class);

            // Pending Intent
            PendingIntent pendingIntent = PendingIntent.getActivity(this, STANDARD_NOTIFICATION, nIntent, 0);
            // Create default action intent
            // Set the default action

            // Set ContentIntent
            builder.setContentIntent(pendingIntent);

            builder.setSmallIcon(R.drawable.ic_av_play_arrow);
            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_av_play_arrow));
            builder.setContentTitle("Playing Music");
            builder.setContentText("Stones Playing");
            NotificationCompat.BigTextStyle bigTextStyle = new NotificationCompat.BigTextStyle();
            bigTextStyle.bigText("The Rolling Stones are the greatest Rock n' Roll band in the world!");
            bigTextStyle.setBigContentTitle("The Rolling Stones");
            bigTextStyle.setSummaryText("The Rolling Stones are the greatest Rock n' Roll band in the world! The band formed in 1962" +
                    " and are considered the best in the business!");
            builder.setStyle(bigTextStyle);
            nManager.notify(EXPANDED_NOTIFICATION, builder.build());
            builder.setAutoCancel(false);
            builder.setOngoing(true);

            startForeground(EXPANDED_NOTIFICATION, builder.build());
        }

        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPlayer.stop();
            mPrepared = false;
        }

        stopForeground(true);

        mPlayer.release();
    }


    @Override
    public void onPrepared(MediaPlayer mp) {
        mPrepared = true;

        if (mPlayer != null && mActivityResumed) {
            mPlayer.seekTo(mAudioPosition);
            mPlayer.start();
        }
    }

}
