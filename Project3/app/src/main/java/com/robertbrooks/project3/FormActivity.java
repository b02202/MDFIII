/*FormActivity.java
* Robert Brooks*/
package com.robertbrooks.project3;

import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.robertbrooks.project3.CustomData.UserData;
import com.robertbrooks.project3.Fragments.FormFrag;
import java.util.ArrayList;

/**
 * Created by Bob on 4/21/2015.
 */
public class FormActivity extends ActionBarActivity {

    public static final String TAG = "FormActivity";

    ArrayList<UserData> uDataArray;
    String[] fileNames;
    Bundle bundle;
    UserData data;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        bundle = new Bundle();
        uDataArray = new ArrayList<>();
        fileNames = getApplicationContext().fileList();

        if (savedInstanceState == null) {
            FormFrag frag = FormFrag.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(R.id.formFragContainer, frag, FormFrag.TAG)
                    .commit();
        }

        // load
        if (fileNames.length != 0) {
            for (String file: fileNames) {
                data = UserData.readFile(file, this);
                uDataArray.add(data);
            }
        }


    }

    public void saveData(View v) {
        TextView input1 = (TextView) findViewById(R.id.editText1);
        TextView input2 = (TextView) findViewById(R.id.editText2);
        TextView input3 = (TextView) findViewById(R.id.editText3);

        // set get user input
        UserData userData = new UserData();
        String text1 = input1.getText().toString();
        String text2 = input2.getText().toString();
        String text3 = input3.getText().toString();

        // set UserData object
        userData.setUserData1(text1);
        userData.setUserData2(text2);
        userData.setUserData3(text3);
        // Add to ArrayList
        uDataArray.add(userData);
        // Save file
        userData.saveFile(uDataArray, text1, this);

        // reset form
        input1.setText("");
        input2.setText("");
        input3.setText("");

        Log.d(TAG, "data saved");

        // update widget
        updateWidget();
    }

    // update widget
    public void updateWidget() {
        // get AppWidgetManager Instance
        AppWidgetManager AWP = AppWidgetManager.getInstance(getApplicationContext());
        // Component Name
        ComponentName cn = new ComponentName(getApplication(), WidgetProvider.class);
        // get Widget Ids
        int widgetIds[] = AWP.getAppWidgetIds(cn);
        // loop through ids and call WidgetViewFactory onDataSetChanged()
        for (int widgetID : widgetIds) {
            AWP.notifyAppWidgetViewDataChanged(widgetID, R.id.data_list);
        }

    }
}
