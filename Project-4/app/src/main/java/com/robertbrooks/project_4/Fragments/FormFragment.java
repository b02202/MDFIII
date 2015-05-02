/*FormFragment.java
* Robert Brooks*/
package com.robertbrooks.project_4.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.robertbrooks.project_4.R;

/**
 * Created by Bob on 4/30/2015.
 */
public class FormFragment extends Fragment {

    ImageView imgView;

    EditText mUserInput1;
    EditText mUserInput2;

    public static final String TAG = "FormFragment.TAG";

    public static FormFragment newInstance() {
        FormFragment frag = new FormFragment();
        return frag;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.form_frag, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();

        mUserInput1 = (EditText) view.findViewById(R.id.editText1);
        mUserInput2 = (EditText) view.findViewById(R.id.editText2);

        imgView = (ImageView) view.findViewById(R.id.image_view);
    }


}
