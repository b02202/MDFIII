/*WidgetDetailsActivity.java
* Robert Brooks */
package com.robertbrooks.project3;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.robertbrooks.project3.CustomData.UserData;

/**
 * Created by Bob on 4/22/2015.
 */
public class WidgetDetailsActivity extends Activity {


    public static final String EXTRA_ITEM = "com.robertbrooks.WidgetDetailsActivity.EXTRA_ITEM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.widget_details);

        Intent intent = getIntent();
        UserData userData = (UserData)intent.getSerializableExtra(EXTRA_ITEM);
        if (userData == null) {
            finish();
            return;
        }

        TextView title = (TextView)findViewById(R.id.title);
        title.setText(userData.getUserData1());

        TextView subTitle = (TextView)findViewById(R.id.itemSub);
        subTitle.setText(userData.getUserData2());

        TextView content = (TextView)findViewById(R.id.itemDetails);
        content.setText(userData.getUserData3());

    }

}
