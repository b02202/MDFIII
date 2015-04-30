package com.robertbrooks.project_4;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.robertbrooks.project_4.Fragments.FormFragment;

/**
 * Created by Bob on 4/30/2015.
 */
public class FormActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);

        FormFragment frag = new FormFragment();
        getFragmentManager().beginTransaction().replace(R.id.form_container, frag).commit();
    }


    // save button
    public void saveData(View v) {


    }

    public void openView(View v) {

        Intent intent = new Intent(this, ViewActivity.class);
        startActivity(intent);
    }

}
