/*UserData.java
* Robert Brooks*/
package com.robertbrooks.project_4;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bob on 4/30/2015.
 */
public class UserData implements Serializable {

    private static long serialVersionUID = 8585654672565839584L;
    public static final String TAG = "UserData";

    Double locLat;
    Double locLong;
    String userData1;
    String userData2;
    String imageUriString;


    // Getters / Setters




    public String getImageUriString() {
        return imageUriString;
    }

    public void setImageUriString(String imageUriString) {
        this.imageUriString = imageUriString;
    }

    public Double getLocLong() {
        return locLong;
    }

    public void setLocLong(Double locLong) {
        this.locLong = locLong;
    }

    public Double getLocLat() {
        return locLat;
    }

    public void setLocLat(Double locLat) {
        this.locLat = locLat;
    }


    public String getUserData1() {
        return userData1;
    }

    public void setUserData1(String userData1) {
        this.userData1 = userData1;
    }

    public String getUserData2() {
        return userData2;
    }

    public void setUserData2(String userData2) {
        this.userData2 = userData2;
    }

    public void saveFile(ArrayList arrayList, String fileName, Context context) {

        try {
            File p4Dir = context.getDir("p4Dir", Context.MODE_PRIVATE);
            File newFile = new File(p4Dir, fileName);
            FileOutputStream fos = new FileOutputStream(newFile);
            ObjectOutputStream outStream = new ObjectOutputStream(fos);
            outStream.writeObject(this);
            outStream.close();
            //fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static UserData readFile(String fileName, Context context) {
        UserData userData = null;



        try {
            File p4dir = context.getDir("p4Dir", Context.MODE_PRIVATE);
            File readFile = new File(p4dir, fileName);
            FileInputStream fis = new FileInputStream(readFile);
            ObjectInputStream inputStream = new ObjectInputStream(fis);
            userData = (UserData) inputStream.readObject();
            Log.d(TAG, userData.getUserData1());
            inputStream.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return userData;
    }


}


