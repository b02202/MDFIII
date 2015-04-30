package com.robertbrooks.project_4;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        TheMapFragment frag = new TheMapFragment();
        getFragmentManager().beginTransaction().replace(R.id.map_container, frag).commit();
    }

    public void openForm(View v) {
        Intent intent = new Intent(this, FormActivity.class);
        startActivity(intent);

    }

}
