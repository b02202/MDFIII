package com.robertbrooks.project_4.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.robertbrooks.project_4.R;

/**
 * Created by Bob on 4/30/2015.
 */
public class ViewFragment extends Fragment {

    public static final String TAG = "ViewFragment.TAG";

    public static ViewFragment newInstance() {
        ViewFragment frag = new ViewFragment();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.view_frag, container, false);
        return view;
    }
}
