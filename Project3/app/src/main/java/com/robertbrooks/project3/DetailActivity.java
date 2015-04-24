package com.robertbrooks.project3;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.robertbrooks.project3.Fragments.DetailFrag;
import com.robertbrooks.project3.Fragments.FormFrag;

/**
 * Created by Bob on 4/21/2015.
 */
public class DetailActivity extends ActionBarActivity {
    public static final String TAG = "DetailActivity";
    public static final String EXTRA_ITEM = "com.robertbrooks.WidgetProvider";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            DetailFrag frag = DetailFrag.newInstance();
            getFragmentManager().beginTransaction()
                    .replace(R.id.detail_frag_container, frag, DetailFrag.TAG)
                    .commit();
        }


        //tv.setText("working");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            /*Intent returnIntent = new Intent(this, MainActivity.class);
            boolean reload = true;

            returnIntent.putExtra("reload", true);
            setResult(RESULT_OK);
            startActivity(returnIntent);*/
            finish();
        }

        return super.onOptionsItemSelected(item);
    }
}
