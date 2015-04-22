package com.robertbrooks.project3.Fragments;

import android.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.app.ListFragment;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.robertbrooks.project3.CustomData.UserData;
import com.robertbrooks.project3.DetailActivity;
import com.robertbrooks.project3.R;

import java.util.ArrayList;


/**
 * Created by Bob on 4/21/2015.
 */
public class ListFrag extends Fragment {

    ListView fListView;
    ArrayList<UserData> uList;
    ArrayList<UserData> itemList;



    public static final String TAG = "ListFrag.TAG";

    public static ListFrag newInstance() {
       ListFrag frag = new ListFrag();
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_fragment, container, false);
        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        fListView = (ListView) getActivity().findViewById(R.id.listView);

        popListView();

        fListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // text to pass to detailView
                String dataString = itemList.get(position).getUserData1()
                        + "\n\n" + itemList.get(position).getUserData2()
                        + "\n\n" + itemList.get(position).getUserData3();

                Intent detailIntent = new Intent(getActivity(), DetailActivity.class);
                detailIntent.putExtra("itemText", dataString);
                startActivity(detailIntent);

            }
        });
    }

    // populate listView
    public void popListView() {


        String[] fileNames = getActivity().getApplicationContext().fileList();
        ArrayAdapter listAD = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        itemList = new ArrayList<>();

        UserData uData;
        if (fileNames.length != 0 ) {
            for (String file : fileNames) {
                uData = UserData.readFile(file, getActivity());
                itemList.add(uData);
            }

            for (int i = 0; i < itemList.size(); i++) {
                String dataString = itemList.get(i).getUserData1();
                listAD.add(dataString);
            }
            fListView.setAdapter(listAD);
        }



    }

    @Override
    public void onStart() {

        popListView();
        super.onStart();
    }
}
