/*MediaLandscapeFrag.java
* Robert Brooks*/
package com.robertbrooks.project1.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.robertbrooks.project1.R;

/**
 * Created by Bob on 4/17/2015.
 */
public class MediaLandscapeFrag extends Fragment {

    // Buttons
    Button mPlayButton;
    Button mStopButton;
    Button mPauseButton;
    Button mNextButton;
    Button mPreviousButton;

    // Track Title TextView
    TextView titleText;

    public static final String TAG = "MediaLandscapeFrag.TAG";

    public static MediaLandscapeFrag newInstance() {
        MediaLandscapeFrag frag = new MediaLandscapeFrag();
        return frag;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // retain fragment
        setRetainInstance(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.media_land_frag, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        // UI References
        mPlayButton = (Button) view.findViewById(R.id.play_btn);
        mStopButton = (Button) view.findViewById(R.id.stop_btn);
        mPauseButton = (Button) view.findViewById(R.id.pause_btn);
        mNextButton = (Button) view.findViewById(R.id.next_btn);
        mPreviousButton = (Button) view.findViewById(R.id.previous_btn);
        titleText = (TextView) view.findViewById(R.id.textView);


    }
}
