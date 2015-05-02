/*ViewFragment.java
* Robert Brooks*/
package com.robertbrooks.project_4.Fragments;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.robertbrooks.project_4.R;
import com.robertbrooks.project_4.TheMapFragment;
import com.robertbrooks.project_4.UserData;


/**
 * Created by Bob on 4/30/2015.
 */
public class ViewFragment extends Fragment {

    public static final String TAG = "ViewFragment.TAG";

    TextView mDetailText;
    ImageView imageView;
    UserData userData;

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

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        View view = getView();
        imageView = (ImageView)view.findViewById(R.id.detail_image);
        mDetailText = (TextView)getActivity().findViewById(R.id.detail_text);



        Intent intent = getActivity().getIntent();
        String fromMap = intent.getStringExtra(TheMapFragment.EXTRA_MESSAGE);
        String itemText = intent.getExtras().getString("item_text", fromMap);
        if (fromMap != null) {
            userData = UserData.readFile(fromMap, getActivity());
            mDetailText.setText(userData.getUserData1() + "\n\n"
                    + userData.getUserData2());

            Uri imagePath = Uri.parse(userData.getImageUriString());
            imageView.setImageBitmap(BitmapFactory.decodeFile(imagePath.getPath()));
        } else if (itemText != null) {
            Uri imgUri = Uri.parse(intent.getExtras().getString("imgUri"));

            imageView.setImageBitmap(BitmapFactory.decodeFile(imgUri.getPath()));
            mDetailText.setText(intent.getExtras().getString("item_text"));
        }

    }
}
