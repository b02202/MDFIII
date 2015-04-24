package com.robertbrooks.project3.CustomData;

import android.content.Context;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Bob on 4/21/2015.
 */
public class UserData implements Serializable {

    private static long serialVersionUID = 8585654672565839584L;
    public static final String TAG = "UserData";
    private String userData1;
    private String userData2;
    private String userData3;



    // Getters / Setters
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

    public String getUserData3() {
        return userData3;
    }

    public void setUserData3(String userData3) {
        this.userData3 = userData3;
    }

    // write to file
    public void saveFile(ArrayList arrayList, String fileName, Context context) {
        try {
            FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
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
            FileInputStream fis = context.openFileInput(fileName);
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
