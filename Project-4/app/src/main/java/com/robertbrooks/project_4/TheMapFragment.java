package com.robertbrooks.project_4;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.InfoWindowAdapter;
import com.google.android.gms.maps.GoogleMap.OnInfoWindowClickListener;
import com.google.android.gms.maps.GoogleMap.OnMapClickListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Bob on 4/27/2015.
 */
public class TheMapFragment extends MapFragment implements OnInfoWindowClickListener, OnMapClickListener {

    private static final String TAG = "TheMapFragment.TAG";
    GoogleMap mMap;
    Double latitude;
    Double longitude;
    String[] fileNames;
    ArrayList<UserData> userDataList;
    UserData uData;

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // Google Map
        mMap = getMap();

        uData = new UserData();
        userDataList = new ArrayList<>();


        fileNames = getActivity().getApplicationContext().getDir("p4Dir", Context.MODE_PRIVATE).list();


            Log.d(TAG, "File Names = " + fileNames);
            // load files


        if (fileNames.length != 0) {
            for (String file : fileNames) {
                uData = UserData.readFile(file, getActivity());

                userDataList.add(uData);
                Log.d(TAG, "LAT TEST = " + userDataList.get(0));

            }

            for (int i = 0; i < userDataList.size(); i++) {
                Log.d(TAG, "LAt = " + userDataList.get(i).getLocLat());
                latitude = userDataList.get(i).getLocLat();
                longitude = userDataList.get(i).getLocLong();
                String title = userDataList.get(i).getUserData1();
                mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(title));
            }
        }




        mMap.setInfoWindowAdapter(new MarkerAdapter());
        mMap.setOnInfoWindowClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 16));
    }

    @Override
    public void onInfoWindowClick(final Marker marker) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Marker Clicked")
                .setMessage("You clicked on: " + marker.getTitle())
                .setPositiveButton("Close", null)
                .setNegativeButton("Remove", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        marker.remove();
                    }
                })
                .show();
    }

    @Override
    public void onMapClick(final LatLng location) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Map Clicked")
                .setMessage("Add new marker here?")
                .setNegativeButton("No", null)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mMap.addMarker(new MarkerOptions().position(location).title("New Marker"));
                    }
                })
                .show();
    }

    private class MarkerAdapter implements InfoWindowAdapter {

        TextView mText;

        public MarkerAdapter() {
            mText = new TextView(getActivity());
        }

        @Override
        public View getInfoContents(Marker marker) {
            mText.setText(marker.getTitle());
            return mText;
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }
    }
}
