/*PlayerService.java
* Robert Brooks*/
package com.robertbrooks.project1;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by Bob on 4/6/2015.
 */
public class PlayerService extends Service  implements MediaPlayer.OnPreparedListener,
        MediaPlayer.OnErrorListener, MediaPlayer.OnCompletionListener {
    public String TAG = "PLAYER SERVICE";
    public static final int STANDARD_NOTIFICATION = 0x01001;
    public static final int EXPANDED_NOTIFICATION = 0x01002;
    private final IBinder mBinder = new BoundServiceBinder();
    boolean mActivityResumed;
    boolean mPrepared;
    int trackIndex = 0;
    int currentPosition = 0;
    boolean trackLooping;
    String[] trackNames;
    NotificationManager nManager;
    NotificationCompat.Builder builder;
    NotificationCompat.BigPictureStyle bigTextStyle;
    ArrayList<Integer> songIdList;
    MediaPlayer mPlayer;
    Bitmap bigPicture;
    private final android.os.Handler handler = new android.os.Handler();
    boolean songEnd;





    @Override
    public void onCompletion(MediaPlayer mp) {

        if (trackLooping) {
            try {
                selectTrack(trackIndex);
                mPlayer.prepareAsync();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else  {

            try {
                playNextTrack();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onError(MediaPlayer mp, int what, int extra) {
        return false;
    }



    // create binder
    public class BoundServiceBinder extends Binder {
         public PlayerService getService() {
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
        this.stopSelf();

        return false;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        songIdList = new ArrayList<>();
        //get raw resource id's and add to songIDList
        try {
            getRawTracks(songIdList);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        // array of track titles
        trackNames = new String[] {"Gimme Shelter", "Brown Sugar", "Doom and Gloom"};

        // initialize Audio Position
        trackIndex = 0;
        mPlayer = new MediaPlayer();

        // initialize MediaPlayer
        try {
            initPlayer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



    // play song
    public void playTrack() throws IOException {
        if (!mPlayer.isPlaying()) {
            if (!mActivityResumed) {
                mPlayer.prepareAsync();
                createNot();
            } else if (mActivityResumed) {
                mPlayer.seekTo(currentPosition);
                mPlayer.start();
                mActivityResumed = false;
            }
            else  {
                trackIndex = (trackIndex + 1) % 3;
                if (trackIndex == 0) {
                    mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/gimme_shelter"));

                } else if (trackIndex == 1) {
                    mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/brown_sugar"));

                } else if (trackIndex == 2) {
                    mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/doom_and_gloom"));
                }

                mPlayer.reset();

                mPlayer.prepareAsync();

            }
        }
        Toast.makeText(this, "Music Playing", Toast.LENGTH_SHORT).show();

    }

    // play next track
    public void playNextTrack() throws IOException {
        if (trackLooping && mPlayer.isPlaying() && !mActivityResumed) {
            selectTrack(trackIndex);
            mPlayer.prepareAsync();
        } else if (mPlayer.isPlaying() && !mActivityResumed && !trackLooping) {
            trackIndex = (trackIndex + 1) % 3;
            // select and play track
            selectTrack(trackIndex);
            mPlayer.prepareAsync();
        }
    }

    // Play Previous Track
    public void playPreviousTrack() throws IOException {

        if (mPlayer.isPlaying() && !mActivityResumed && trackLooping) {
            selectTrack(trackIndex);
            mPlayer.prepareAsync();

        } else if (trackIndex > 0 && mPlayer.isPlaying() && !mActivityResumed && !trackLooping ) {
            trackIndex = (trackIndex - 1) % 3;
            selectTrack(trackIndex);
            mPlayer.prepareAsync();

        } else if (trackIndex == 0 && mPlayer.isPlaying() && !mActivityResumed){
            trackIndex = 2;
            selectTrack(trackIndex);
            mPlayer.prepareAsync();
        }

        // start handler

    }

    @Override
    public void onPrepared(MediaPlayer mp) {
        Log.d(TAG, "Track info: " + mPlayer.getCurrentPosition());
        Log.d(TAG, "TrackIndex = " + trackIndex);
        Log.d(TAG, "Activity Resumed = " + mActivityResumed);

        //songName = updateTitle();
        updateNot();
        mp.start();

    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_NOT_STICKY;
    }

    // create notification
    public void createNot(){
        // Notification implementation
        nManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        builder = new NotificationCompat.Builder(this);

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
        builder.setContentText("The Rolling Stones");


        bigTextStyle = new NotificationCompat.BigPictureStyle();
        bigTextStyle.bigPicture(BitmapFactory.decodeResource(getResources(), R.drawable.rolling_stones_logo));
        bigTextStyle.setBigContentTitle("The Rolling Stones");

        builder.setStyle(bigTextStyle);

        // update bigTextStyle with current image
        bigTextStyle.bigPicture(updateImage());

        nManager.notify(EXPANDED_NOTIFICATION, builder.build());
        builder.setAutoCancel(false);
        builder.setOngoing(true);

        startForeground(EXPANDED_NOTIFICATION, builder.build());
    }
    // update notification
    public void updateNot() {
        bigTextStyle.bigPicture(updateImage());
        nManager.notify(EXPANDED_NOTIFICATION, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPlayer != null) {
            mPrepared = false;
            Log.d(TAG, "mPrepared = " + mPrepared);
        }
        stopForeground(true);

       mPlayer.release();
        this.stopSelf();

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

    // stop media player
    public void stopPlayer() {
        if(mPlayer.isPlaying()) {
            mPlayer.stop();
            mActivityResumed = false;
            stopForeground(true);
            //mPlayer.prepareAsync();
        }
    }

    // Initialize player
    public void initPlayer() throws IOException {

        mPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        mPlayer.setOnPreparedListener(this);
        mPlayer.setOnCompletionListener(this);
        mPlayer.setOnErrorListener(this);
        mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/gimme_shelter"));
    }

    // get songs from raw resources
    public void getRawTracks(ArrayList<Integer> trackArray) throws IllegalAccessException {
        int rawFiles[] = {R.raw.gimme_shelter, R.raw.brown_sugar, R.raw.doom_and_gloom};
        for (int i = 0; i < rawFiles.length; i++) {
            int track = rawFiles[i];
            trackArray.add(track);

        }
    }

    // Select track to be played

    public void selectTrack(int trackIndx) throws IOException {
        if (trackIndx == 0) {
            mPlayer.reset();
            mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/gimme_shelter"));

        } else if (trackIndx == 1) {
            mPlayer.reset();
            mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/brown_sugar"));

        } else if (trackIndx == 2) {
            mPlayer.reset();
            mPlayer.setDataSource(this, Uri.parse("android.resource://" + getPackageName() + "/raw/doom_and_gloom"));
        }
    }


    // Update Notification image
    public Bitmap updateImage(){
       // String trackTitle = "";
        if (trackIndex == 0) {
            bigPicture = BitmapFactory.decodeResource(getResources(), R.drawable.rolling_stones_logo);
         //   trackTitle = trackNames[0];
        } else if (trackIndex == 1) {
            bigPicture = BitmapFactory.decodeResource(getResources(), R.drawable.rolling_stones_two);
         //   trackTitle = trackNames[1];
        } else if (trackIndex == 2) {
            bigPicture = BitmapFactory.decodeResource(getResources(), R.drawable.rolling_stones_three);
         //   trackTitle = trackNames[2];
        }
        return bigPicture;
    }

    // return trackIndex to use in MainActivity
    public int getSongTitle() {
        return trackIndex;
    }

    // set Looping
    public void loopFunction() {
        mPlayer.setLooping(true);
        trackLooping = true;
    }
    // set no looping
    public void notLooping() {
        mPlayer.setLooping(false);
        trackLooping = false;
    }


    // get current track position
    public int getPositition() {
        return mPlayer.getCurrentPosition();

    }


    // get track duration
    public int getDur() {

        return mPlayer.getDuration();
    }











}