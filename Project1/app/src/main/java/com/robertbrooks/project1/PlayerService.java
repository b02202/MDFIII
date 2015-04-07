package com.robertbrooks.project1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import java.io.IOException;

/**
 * Created by Bob on 4/6/2015.
 */
public class PlayerService extends Service implements MediaPlayer.OnCompletionListener {
    public static final int STANDARD_NOTIFICATION = 0x01001;
    public static final int EXPANDED_NOTIFICATION = 0x01002;




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
            NotificationManager nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
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

            startForeground(STANDARD_NOTIFICATION, builder.build());
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer.isPlaying()) {
            mPlayer.stop();
        }

        stopForeground(true);

        mPlayer.release();
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        stopSelf();

    }
}
