package com.robertbrooks.project3.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.robertbrooks.project3.R;

/**
 * Created by Bob on 4/21/2015.
 */
public class DetailFrag extends Fragment {

    public static final String TAG = "DetailFrag.TAG";

    TextView mDetailText;
    String recievedText;

    public static DetailFrag newInstance() {
        DetailFrag frag = new DetailFrag();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.detail_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mDetailText = (TextView) getActivity().findViewById(R.id.detailText);

        Intent intent = getActivity().getIntent();
        recievedText = intent.getExtras().getString("itemText");
        mDetailText.setText(intent.getExtras().getString("itemText"));


    }
}
