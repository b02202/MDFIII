package com.robertbrooks.project3;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.robertbrooks.project3.CustomData.UserData;

import java.util.ArrayList;

/**
 * Created by Bob on 4/22/2015.
 */
public class WidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    public static final String TAG = "WidgetViewFactory";
    private static final int ID_CONSTANT = 0x0101010;

    public ArrayList<UserData> uDataList;
    private Context wContext;
    String[] fileNames;
    UserData uData;
    RemoteViews dataView;


    public WidgetViewFactory(Context context) {
        wContext = context;
        uDataList = new ArrayList<>();

    }

    @Override
    public void onCreate() {
        fileNames = wContext.getApplicationContext().fileList();
        if (fileNames.length != 0) {
            for (String file : fileNames) {
                uData = UserData.readFile(file, wContext);
                uDataList.add(uData);

            }
            Log.d(TAG, uDataList.get(1).getUserData1());
        }
    }

    @Override
    public void onDataSetChanged() {
        uDataList.clear();
        fileNames = wContext.getApplicationContext().fileList();
        if (fileNames.length != 0) {
            for (String file : fileNames) {
                uData = UserData.readFile(file, wContext);
                uDataList.add(uData);

            }
            Log.d(TAG, uDataList.get(1).getUserData1());
        }
    }

    @Override
    public void onDestroy() {
        uDataList.clear();
    }

    @Override
    public int getCount() {
        return uDataList.size();
    }

    @Override
    public long getItemId(int position) {
        return ID_CONSTANT + position;
    }

    @Override
    public RemoteViews getViewAt(int position) {

        UserData data = uDataList.get(position);

        dataView = new RemoteViews(wContext.getPackageName(), R.layout.data_item);

        dataView.setTextViewText(R.id.itemTitle, data.getUserData1());
        dataView.setTextViewText(R.id.itemSubTitle, data.getUserData2());
        dataView.setTextViewText(R.id.itemContent, data.getUserData3());

        Intent intent = new Intent();
        intent.putExtra(WidgetProvider.EXTRA_ITEM, data);
        dataView.setOnClickFillInIntent(R.id.data_item, intent);

        return dataView;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }



    @Override
    public boolean hasStableIds() {
        return true;
    }


}
