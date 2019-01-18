package com.example.dungbong.finalproject;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */

public class ContactFragment extends Fragment {

    public static String str;
    private GestureDetector mGestureDetector;

    public ContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            fragmentTransaction.replace(R.id.container, new AddContactFragment());
            fragmentTransaction.commit();
            }
        });


        Context context = getActivity();

        final ArrayList<String> mail_list = new ArrayList<>();
        final ArrayList<Integer> id_list = new ArrayList<>();

        FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(context);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        try {
            db.execSQL(FeedReaderDbHelper.SQL_CREATE_ENTRIES);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        ListView listview = view.findViewById(R.id.listView);


        Cursor cursor = db.rawQuery("SELECT * FROM contact", null);


        while (cursor.moveToNext()) {
            id_list.add(cursor.getInt(0));
            mail_list.add(cursor.getString(2));
        }

        final MyCursorAdapter arrayAdapter = new MyCursorAdapter(context, cursor);
        listview.setAdapter(arrayAdapter);

        db.close();

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                str = mail_list.get(position);

                fragmentTransaction.replace(R.id.container, new SendMailFragment());
                fragmentTransaction.commit();
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getActivity());

                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                db.delete("contact", FeedReaderDbHelper.FeedEntry._ID + "=" + id, null);
                id_list.remove(position);
                mail_list.remove(position);

                Toast.makeText(getActivity(), R.string.contact_delete, Toast.LENGTH_SHORT).show();

                arrayAdapter.notifyDataSetChanged();
                db.close();
                fragmentTransaction.replace(R.id.container, new ContactFragment());
                fragmentTransaction.commit();

                return false;
            }
        });
        //cursor.close();

        return view;
    }
}
