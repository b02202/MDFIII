/*DetailActivity.java
* Robert Brooks*/
package com.robertbrooks.project3;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import com.robertbrooks.project3.Fragments.DetailFrag;

/**
 * Created by Bob on 4/21/2015.
 */
public class DetailActivity extends ActionBarActivity {
    public static final String TAG = "DetailActivity";

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
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

}
