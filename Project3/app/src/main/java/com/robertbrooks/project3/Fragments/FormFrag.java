package com.robertbrooks.project3.Fragments;

import android.app.Fragment;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.robertbrooks.project3.R;
import com.robertbrooks.project3.WidgetProvider;

/**
 * Created by Bob on 4/21/2015.
 */
public class FormFrag extends Fragment {

    EditText mUserInput1;
    EditText mUserInput2;
    EditText mUserInput3;
    Button mSaveBtn;

    public static final String TAG = "FormFrag.TAG";

    public static FormFrag newInstance() {
        FormFrag frag = new FormFrag();
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_fragment, container, false);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final View view = getView();
        Log.d(TAG, "FormFrag Created");

        mUserInput1 = (EditText) view.findViewById(R.id.editText1);
        mUserInput2 = (EditText) view.findViewById(R.id.editText2);
        mUserInput3 = (EditText) view.findViewById(R.id.editText3);
        mSaveBtn = (Button) view.findViewById(R.id.save_btn);

    }


}
