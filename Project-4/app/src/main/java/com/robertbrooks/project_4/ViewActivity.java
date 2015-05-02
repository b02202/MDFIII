/*ViewActivity.java
* Robert Brooks */
package com.robertbrooks.project_4;

import android.app.Activity;
import android.os.Bundle;

import com.robertbrooks.project_4.Fragments.ViewFragment;

/**
 * Created by Bob on 4/30/2015.
 */
public class ViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        ViewFragment frag = new ViewFragment();
        getFragmentManager().beginTransaction().replace(R.id.view_container, frag).commit();
    }
}
