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

    ArrayList<Integer> songIdList;
    ArrayList<String> songArrayList;

    MediaPlayer mPlayer;

    /*public static PlayerService getInstance() {
        if (ref == null) {
            ref = new PlayerService();
        }
        return ref;
    }*/

    @Override
    public void onCompletion(MediaPlayer mp) {

        try {
            playTrack();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*trackIndex = mPlayer.getSelectedTrack(MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO);
        int nextIndex = trackIndex + 1;

        if (nextIndex < songIdList.size()) {
            mPlayer.selectTrack(nextIndex);

        } else {

            mPlayer.stop();
            mPlayer.release();
        }
*/
        /*if (trackIndex < songIdList.size()) {
            trackIndex ++;
            index ++;
        } else {
            trackIndex = 0;
            index = 0;
        }
        trackIndex = index;

        try {
            mPlayer.setDataSource(this, Uri.parse(songArrayList.get(trackIndex)));
        } catch (IOException e) {
            e.printStackTrace();
        }*/
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
        mPlayer.stop();
        mPlayer.release();
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

        mPlayer.start();

    }

    // play song
    public void playTrack() throws IOException {
        trackIndex = (trackIndex + 1)% 3;
        AssetFileDescriptor aFD = this.getResources().openRawResourceFd(songs[trackIndex]);

        mPlayer.reset();
        mPlayer.setDataSource(aFD.getFileDescriptor(), aFD.getStartOffset(), aFD.getDeclaredLength());
        mPlayer.prepareAsync();
        aFD.close();

        /*Log.i(TAG, "trackIndex = " + trackIndex);
        mPlayer.reset();
        //setTrackList();
        String Track1 = "android.resource://" + getPackageName() + "/raw/gimme_shelter";
        String Track2 = "android.resource://" + getPackageName() + "/raw/brown_sugar";
        String Track3 = "android.resource://" + getPackageName() + "/raw/doom_and_gloom";

        mPlayer.setDataSource(this, Uri.parse(songArrayList.get(0)));
        Log.d(TAG, "Track = " + songArrayList.get(trackIndex));
        mPlayer.prepareAsync();*/
        //int resString =


        /*int index;
        for (index = 0; index < songArrayList.size(); index++) {
            mPlayer.setDataSource(this, Uri.parse(songArrayList.get(index)));

        }*/
        /*if (index < songArrayList.size())
            index ++;*/

        /*else {
            index = 0;
        }*/
        //Log.i(TAG, "index = " + index);


        /*try {
            mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/gimme_shelter"));
        } catch (IOException e) {
            Log.e("PLAYER SERVICE", "Error setting data source", e);
        }*/

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
        Log.d(TAG, "Track info: " + mPlayer.getTrackInfo().toString());
       // start mPlayer
        mp.start();

       /* mPrepared = true;

        if (mPlayer != null) {
            mPlayer.seekTo(mAudioPosition);
            mp.start();
        }*/
    }

    // Binder toast
    public void showToast() {
        Toast.makeText(this, "Text goes here", Toast.LENGTH_SHORT).show();
    }



    // resume playback after pause
    public void onResume() {
        onPlayerResume(mAudioPosition);
    }






    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {


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
            mPlayer.stop();
            mAudioPosition = mPlayer.getCurrentPosition();
            mPrepared = false;
        }

        stopForeground(true);


       mPlayer.release();
    }









    // Start Media Player
    public void onPlayerResume(int currentPosition) {

        if (mPlayer != null) {
            mPlayer.seekTo(currentPosition);
            mPlayer.start();
        }
    }

    // pause media player and get current position
    public void onPause() {
        mActivityResumed = false;
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
            mActivityResumed = true;

        }
    }

    // Initialize player
    public void initPlayer() throws IOException {
        // Set wake lock property
        //mPlayer.setWakeMode(getApplicationContext(), PowerManager.PARTIAL_WAKE_LOCK);
        //songs = new int[] {songIdList.get(0), songIdList.get(1), songIdList.get(2)};
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
       // trackIndex = mPlayer.getSelectedTrack(MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO);
        /*mPlayer.selectTrack(trackIndex + 1);
        mPlayer.start();*/
        playTrack();
    }

    // skip forward button
    public void skipback() throws IOException {
        // trackIndex = mPlayer.getSelectedTrack(MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_AUDIO);
        /*mPlayer.selectTrack(trackIndex + 1);
        mPlayer.start();*/
        playPreviousTrack();
    }

    // Update Track Title
    public void updateTitle(String currentSongTitle){

    }

}
