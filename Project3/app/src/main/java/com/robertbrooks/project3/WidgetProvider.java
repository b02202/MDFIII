package com.robertbrooks.project3;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
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

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {

        for (int i = 0; i < appWidgetIds.length; i++) {
            int wId = appWidgetIds[i];

            Intent intent = new Intent(context, CollectionWidgetService.class);
            intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, wId);

            RemoteViews wView = new RemoteViews(context.getPackageName(), R.layout.widget_layout);
            wView.setRemoteAdapter(R.id.data_list, intent);
            wView.setEmptyView(R.id.data_list, R.id.empty);

            Intent detailIntent = new Intent(ACTION_DETAILS);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, detailIntent,
                    PendingIntent.FLAG_UPDATE_CURRENT);

            wView.setPendingIntentTemplate(R.id.data_list, pendingIntent);

            appWidgetManager.updateAppWidget(wId, wView);
        }

        super.onUpdate(context, appWidgetManager, appWidgetIds);
    }

    @Override
    public void onReceive(Context context, Intent intent) {

        // onRecieve action

        if (intent.getAction().equals(ACTION_DETAILS)) {
            UserData userData = (UserData)intent.getSerializableExtra(EXTRA_ITEM);
            if (userData != null) {
                Intent details = new Intent(context, WidgetDetailsActivity.class);
                details.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                details.putExtra(WidgetDetailsActivity.EXTRA_ITEM, userData);
                context.startActivity(details);
            }
        }

        super.onReceive(context, intent);
    }
}
