package com.robertbrooks.project3;

import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.robertbrooks.project3.CustomData.UserData;

import java.util.ArrayList;

/**
 * Created by Bob on 4/22/2015.
 */
public class WidgetViewFactory implements RemoteViewsService.RemoteViewsFactory {

    private static final int ID_CONSTANT = 0x0101010;

    public ArrayList<UserData> uDataList;
    private Context wConext;
    String[] fileNames;
    UserData uData;

    public WidgetViewFactory(Context context) {
        wConext = context;
        uDataList = new ArrayList<UserData>();

    }

    @Override
    public void onCreate() {
        fileNames = wConext.getApplicationContext().fileList();
        if (fileNames.length != 0) {
            for (String file : fileNames) {
                uData = UserData.readFile(file, wConext);
                uDataList.add(uData);
            }
        }
    }

    @Override
    public void onDataSetChanged() {

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

        RemoteViews dataView = new RemoteViews(wConext.getPackageName(), R.layout.data_item);

        String title = data.getUserData1();
        String subTitle = data.getUserData2();
        String content = data.getUserData3();

        dataView.setTextViewText(R.id.itemTitle, title);
        dataView.setTextViewText(R.id.itemSubTitle, subTitle);
        dataView.setTextViewText(R.id.itemContent, content);

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
