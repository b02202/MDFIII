package com.robertbrooks.project_4;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.robertbrooks.project_4.Fragments.FormFragment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Bob on 4/30/2015.
 */
public class FormActivity extends Activity implements LocationListener {

    private static final int REQUEST_TAKE_PICTURE = 0x01001;
    private static final int REQUEST_ENABLE_GPS = 0x03001;

    public static final String TAG = "FormActivity";

    FormFragment fragment;
    ImageView imgView;
    Uri imgUri;

    ArrayList<UserData> userDataList;
    String[] filenames;
    Bundle bundle;
    UserData uData;
    LocationManager locMgr;
    Double locLat;
    Double locLong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        bundle = new Bundle();
        userDataList = new ArrayList<>();
        //filenames = getApplicationContext().fileList();

        // Location


        FormFragment frag = new FormFragment();
        getFragmentManager().beginTransaction().replace(R.id.form_container, frag).commit();

        fragment = (FormFragment) getFragmentManager().findFragmentByTag(FormFragment.TAG);

        // load files
        /*if (filenames.length != 0) {
            for (String file : filenames) {
                uData = UserData.readFile(file, this);
                userDataList.add(uData);
            }

        }*/

    }


    // save button
    public void saveData(View v) {
        TextView input1 = (TextView) findViewById(R.id.editText1);
        TextView input2 = (TextView) findViewById(R.id.editText2);
        locMgr = (LocationManager)getSystemService(LOCATION_SERVICE);

        // get user input
        String text1 = input1.getText().toString();
        String text2 = input2.getText().toString();
        gpsCheck();


        // set UserData object
        UserData userData = new UserData();
        userData.setLocLat(locLat);
        userData.setLocLong(locLong);
        userData.setUserData1(text1);
        userData.setUserData2(text2);
        // Save Data
        userData.saveFile(userDataList, text1, this);
        // reset form
        input1.setText("");
        input2.setText("");

        // Notify User of successful save
        Toast.makeText(this, "Data saved", Toast.LENGTH_SHORT).show();

        Log.d(TAG, "Text1 = " + userData.getUserData1());
        Log.d(TAG, "Text2 = " + userData.getUserData2());
        Log.d(TAG, "LAT = " + userData.getLocLat());
        Log.d(TAG, "LONG = " + userData.getLocLong());

    }

    // GPS Check

    private void gpsCheck() {
        if (locMgr.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locMgr.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, this);

            Location location = locMgr.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (location != null) {
                locLat = location.getLatitude();
                locLong = location.getLongitude();
            }
        } else {
            new AlertDialog.Builder(this).setTitle("GPS is Unavailable").setMessage("GPS must be enabled")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivityForResult(intent, REQUEST_ENABLE_GPS);
                        }
                    })
                    .show();


        }
    }



    public void openView(View v) {

        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
    }

    public void openCamera(View v) {
        Intent camIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        imgUri = getOutputUri();
        camIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
        startActivityForResult(camIntent, REQUEST_TAKE_PICTURE);
    }

    private Uri getOutputUri() {
     String imageName = new SimpleDateFormat("MMddyyy_HHmss")
             .format(new Date(System.currentTimeMillis()));
        File imageDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        // new folder in image directory
        File newDir = new File(imageDirectory, "Project4Images");
        newDir.mkdirs();

        File image = new File(newDir, imageName + ".jpg");

        try {
            image.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        return Uri.fromFile(image);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       // gpsCheck();

        if(requestCode == REQUEST_TAKE_PICTURE && resultCode != RESULT_CANCELED) {
            Log.d(TAG, "requestCode = " + requestCode);
            if (imgUri != null) {
                //imgView.setImageBitmap(BitmapFactory.decodeFile(imgUri.getPath()));
                populateImage(imgUri);
                Log.d(TAG, "Checkpoint popImage");
                addImageToGallery(imgUri);
                Log.d(TAG, "Checkpoint addImageToGallery");
            } else {
                imgView.setImageBitmap((Bitmap)data.getParcelableExtra("data"));
            }
        }
    }

    // add image to gallery

    private void addImageToGallery(Uri imageUri) {
        Intent scanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        scanIntent.setData(imageUri);
        sendBroadcast(scanIntent);
    }



    public void populateImage(Uri imgUri) {

        imgView = (ImageView) findViewById(R.id.image_view);
        imgView.setImageBitmap(BitmapFactory.decodeFile(imgUri.getPath()));
    }

    @Override
    protected void onResume() {
        super.onResume();
        //gpsCheck();
    }

    @Override
    protected void onPause() {
        super.onPause();

       // locMgr.removeUpdates(this);
    }

    @Override
    public void onLocationChanged(Location location) {
        // set location Doubles
        locLat = location.getLatitude();
        locLong = location.getLongitude();
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
