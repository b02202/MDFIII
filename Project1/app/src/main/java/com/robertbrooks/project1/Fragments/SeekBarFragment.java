/*SeekBarFragment.java
* Robert Brooks*/
package com.robertbrooks.project1.Fragments;

import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import com.robertbrooks.project1.PlayerService;
import com.robertbrooks.project1.R;

/**
 * Created by Bob on 4/17/2015.
 */
public class SeekBarFragment extends Fragment {
    public static String TAG = "SeekBarFragment.TAG";
    SeekBar seekBar;
    PlayerService ps;
    boolean bound = false;
    static boolean active;
    Intent playIntent;

    public static SeekBarFragment newInstance() {
        SeekBarFragment frag = new SeekBarFragment();
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.seekbar_frag, container, false);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);


    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        seekBar = (SeekBar) view.findViewById(R.id.seek_bar);
        /*seekBar.setMax(272144);
        seekBar.setProgress(231254);*/
    }

}
