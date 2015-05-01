package com.robertbrooks.project_4.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
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
    Uri imgUri;

    EditText mUserInput1;
    EditText mUserInput2;

    public static final String TAG = "FormFragment.TAG";
    private static final int REQUEST_TAKE_PICTURE = 0x01001;
    private static final int RESULT_CANCELED = 0x00001;

    public static FormFragment newInstance() {
        FormFragment frag = new FormFragment();
        return frag;
    }



    /*public interface fragInterface {
        public void populateImage (Uri imageUri);


    }*/

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

    // populate imageView

    public void populateImage(Uri imgUri) {

        imgView.setImageBitmap(BitmapFactory.decodeFile(imgUri.getPath()));
    }



}
