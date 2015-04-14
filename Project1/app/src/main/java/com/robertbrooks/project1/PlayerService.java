package com.robertbrooks.project1;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by Bob on 4/6/2015.
 */
public class PlayerService extends Service  implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    public String TAG = "PLAYER SERVICE";
    public static final int STANDARD_NOTIFICATION = 0x01001;
    public static final int EXPANDED_NOTIFICATION = 0x01002;
    private static final String SAVE_POSITION = "PlayerService.SAVE_POSITION";
    private final IBinder mBinder = new BoundServiceBinder();
    boolean mActivityResumed;
    boolean mPrepared;
    private int mAudioPosition;
    private static PlayerService ref = null;
    public int index;
    int trackIndex = 0;
    int [] songs;
    int currentPosition = 0;
    String[] trackNames;
    String songName;

    ArrayList<Integer> songIdList;
    ArrayList<String> songArrayList;

    MediaPlayer mPlayer;


    @Override
    public void onCompletion(MediaPlayer mp) {

        try {
            playTrack();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }


    // create binder
    public class BoundServiceBinder extends Binder {
         PlayerService getService() {
            // return instance of Player Service
            return PlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    // onUnbind Method
    @Override
    public boolean onUnbind(Intent intent) {
        //mPlayer.stop();
        //mPlayer.release();

        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        songIdList = new ArrayList<Integer>();
        //get raw resource id's and add to songIDList
        try {
            getRawTracks(songIdList);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        trackNames = new String[] {"Gimme Shelter", "Brown Sugar", "Doom and Gloom"};


        // initialize Audio Position
        mAudioPosition = 0;
        trackIndex = 0;
        //mPlayer = MediaPlayer.create(this, R.raw.gimme_shelter);
        mPlayer = new MediaPlayer();
        //mPrepared = mActivityResumed = false;

        // initialize MediaPlayer
        try {
            initPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }

       // mPlayer.start();

    }

    // play song
    public void playTrack() throws IOException {
        if (!mPlayer.isPlaying()) {
            if (mActivityResumed) {
                mPlayer.seekTo(currentPosition);
                mPlayer.start();
                mActivityResumed = false;
            } else {
                trackIndex = (trackIndex + 1) % 3;
                AssetFileDescriptor aFD = this.getResources().openRawResourceFd(songs[trackIndex]);

                mPlayer.reset();
                mPlayer.setDataSource(aFD.getFileDescriptor(), aFD.getStartOffset(), aFD.getDeclaredLength());
                mPlayer.prepareAsync();
                aFD.close();
            }
        }

    }

    // play next track
    public void playNextTrack() throws IOException {
        trackIndex = (trackIndex + 1) % 3;
        AssetFileDescriptor aFD = this.getResources().openRawResourceFd(songs[trackIndex]);

        mPlayer.reset();
        mPlayer.setDataSource(aFD.getFileDescriptor(), aFD.getStartOffset(), aFD.getDeclaredLength());
        mPlayer.prepareAsync();
        aFD.close();
    }

    // PlayPrevious
    public void playPreviousTrack() throws IOException {

        if (trackIndex > 0 ) {
            trackIndex = (trackIndex - 1) % 3;
            AssetFileDescriptor aFD = this.getResources().openRawResourceFd(songs[trackIndex]);

            mPlayer.reset();
            mPlayer.setDataSource(aFD.getFileDescriptor(), aFD.getStartOffset(), aFD.getDeclaredLength());
            mPlayer.prepareAsync();
            aFD.close();

        } else if (trackIndex == 0){
            trackIndex = 2;
            AssetFileDescriptor assetFileDescriptor = this.getResources().openRawResourceFd(songs[trackIndex]);

            mPlayer.reset();
            mPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),
                    assetFileDescriptor.getStartOffset(), assetFileDescriptor.getDeclaredLength());
            mPlayer.prepareAsync();
            assetFileDescriptor.close();
        }

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "Track info: " + mPlayer.getCurrentPosition());
        Log.d(TAG, "TrackIndex = " + trackIndex);
        Log.d(TAG, "Activity Resumed = " + mActivityResumed);
        songName = updateTitle();
        Log.d(TAG, songName);
       // start mPlayer
       /* if (mActivityResumed) {
            mp.seekTo(currentPosition);
           // mp.start();
            mActivityResumed = false;
        }*/

        mp.start();
    }

    // Binder toast
    public void showToast() {
        Toast.makeText(this, "Text goes here", Toast.LENGTH_SHORT).show();

    }










    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this, "Player Started", Toast.LENGTH_SHORT).show();
        updateTitle();
            //mPlayer.start();
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
       // }

        return START_NOT_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            //mPlayer.stop();
            //mPlayer.stop();
           // mAudioPosition = mPlayer.getCurrentPosition();
            mPrepared = false;
            Log.d(TAG, "mPrepared = " + mPrepared);
        }

        stopForeground(true);


       mPlayer.release();
    }

    // pause media player and get current position
    public void onPause() {
        mActivityResumed = false;
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            currentPosition = mPlayer.getCurrentPosition();
            mActivityResumed = true;
        }
    }

    // Initialize player
    public void initPlayer() throws IOException {
        // Set wake lock property

        songs = new int[] {R.raw.gimme_shelter, R.raw.brown_sugar, R.raw.doom_and_gloom};
        mPlayer = MediaPlayer.create(this, songs[0]);
        Log.i(TAG, "Song Track = " + songs[0]);
       // mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/gimme_shelter"));
        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        //mPlayer.start();
    }

    // get songs from raw folder
    public void getRawTracks(ArrayList<Integer> trackArray) throws IllegalAccessException {
        Field[] fields = R.raw.class.getFields();
        for (Field field : fields) {
            int resID = field.getInt(field);
            trackArray.add(resID);
        }

    }

    // set Track
    public void setTrackList() {
        String Track1 = "android.resource://" + getPackageName() + "/raw/gimme_shelter";
        String Track2 = "android.resource://" + getPackageName() + "/raw/brown_sugar";
        String Track3 = "android.resource://" + getPackageName() + "/raw/doom_and_gloom";
        songArrayList = new ArrayList<>();
        songArrayList.add(Track1);
        songArrayList.add(Track2);
        songArrayList.add(Track3);
    }

    // skip forward button
    public void skipForward() throws IOException {
        mActivityResumed = false;
        playNextTrack();
    }

    // skip forward button
    public void skipBack() throws IOException {
        mActivityResumed = false;
        playPreviousTrack();
    }

    // Update Track Title
    public String updateTitle(){
        String trackTitle = "";
        if (trackIndex == 0) {
            trackTitle = trackNames[0];
        } else if (trackIndex == 1) {
            trackTitle = trackNames[1];
        } else if (trackIndex == 2) {
            trackTitle = trackNames[2];
        }
        /*String trackTitle = trackNames[trackIndx];
        Log.d(TAG, "Track Title (PS)  = " + trackTitle);*/

        return trackTitle;

    }

    public int getSongTitle() {
        return trackIndex;
    }

    // stop track and service
    public void stopTrack() {
        mPlayer.stop();

        //stopSelf();
    }


}
