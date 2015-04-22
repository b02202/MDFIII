package com.robertbrooks.project3;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by Bob on 4/22/2015.
 */
public class CollectionWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
       //return new CollectionWidgetViewFactory(getApplicationContext());

        return null;
    }
}
