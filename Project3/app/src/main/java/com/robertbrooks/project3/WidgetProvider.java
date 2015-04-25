/*WidgetProvider.java
* Robert Brooks */
package com.robertbrooks.project3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.robertbrooks.project3.CustomData.UserData;

/**
 * Created by Bob on 4/22/2015.
 */
public class WidgetProvider extends AppWidgetProvider {

    public static final String ACTION_DETAILS = "com.robertbrooks.ACTION_DETAILS";
    public static final String EXTRA_ITEM = "com.robertbrooks.WidgetProvider";
    public static final String TAG = "WidgetProvider";
    public static final String BTN_CLICK = "com.robertbrooks.BTN_CLK";
    public static final String ADD_BTN = "com.robertbrooks.ADD_BTN";
    int wId;
    RemoteViews wView;




        @Override
        public void onUpdate (Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds)
        {


            for (int i = 0; i < appWidgetIds.length; i++) {
                wId = appWidgetIds[i];

                Intent intent = new Intent(context, CollectionWidgetService.class);
                intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, wId);

                wView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
                wView.setRemoteAdapter(R.id.data_list, intent);
                wView.setEmptyView(R.id.data_list, R.id.empty);

                // set pending intent for list item click
                wView.setPendingIntentTemplate(R.id.data_list, listClick(context));

                // set pending click to pending Button Intent
                wView.setOnClickPendingIntent(R.id.refreshBtn, btnIntent(context ));

                // pending for add
                wView.setOnClickPendingIntent(R.id.addBtn, addBtnIntent(context));

                appWidgetManager.updateAppWidget(wId, wView);
            }
            super.onUpdate(context, appWidgetManager, appWidgetIds);
        }


    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        // Get RemoteViews
        RemoteViews wView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
        // List Click
        if (intent.getAction().equals(ACTION_DETAILS)) {
            UserData userData = (UserData)intent.getSerializableExtra(EXTRA_ITEM);

            if (userData != null) {
                Intent details = new Intent(context, WidgetDetailsActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                details.putExtra(WidgetDetailsActivity.EXTRA_ITEM, userData);
                context.startActivity(details);
            }
        }
        // refresh button
        if (intent.getAction().equals(BTN_CLICK)) {
            AppWidgetManager awm = AppWidgetManager.getInstance(context);
            ComponentName componentName = new ComponentName(context, WidgetProvider.class);
            // get Widget Ids
            int[] ids = awm.getAppWidgetIds(componentName);
            // run WidgetViewFactory onDataSetChanged
            awm.notifyAppWidgetViewDataChanged(ids, R.id.data_list);
            // update Widget
            awm.updateAppWidget(ids, wView);
        }

        // add button
        if (intent.getAction().equals(ADD_BTN)) {
            Intent goToForm = new Intent(context, FormActivity.class);
            goToForm.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(goToForm);
        }


    }

    // set button intent
    public PendingIntent btnIntent(Context context) {
        Intent intent = new Intent(context, getClass());
        intent.setAction(BTN_CLICK);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        return pendingIntent;
    }

    // add button intent
    public PendingIntent addBtnIntent(Context context) {
        Intent intent = new Intent(context, FormActivity.class);
        intent.setAction(ADD_BTN);
        PendingIntent addPending = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return addPending;

    }

    public PendingIntent listClick(Context context) {
        Intent detailIntent = new Intent(ACTION_DETAILS);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, detailIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);
        return pendingIntent;
    }


}
