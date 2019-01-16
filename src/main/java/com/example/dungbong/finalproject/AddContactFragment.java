package com.example.dungbong.finalproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;


/**
 * A simple {@link Fragment} subclass.
 */

public class AddContactFragment extends Fragment {
    public AddContactFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_contact, container, false);

        Button button = view.findViewById(R.id.add_mail_button);
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                EditText text_name = getView().findViewById(R.id.contact_name);
                String string_name = text_name.getText().toString();

                EditText text_mail = getView().findViewById(R.id.contact_mail);
                String string_mail = text_mail.getText().toString();

                FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getActivity());

                // Gets the data repository in write mode
                SQLiteDatabase db = mDbHelper.getWritableDatabase();

// Create a new map of values, where column names are the keys
                ContentValues values = new ContentValues();
                values.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TITLE, string_name);
                values.put(FeedReaderDbHelper.FeedEntry.COLUMN_NAME_SUBTITLE, string_mail);

// Insert the new row, returning the primary key value of the new row
                long newRowId = db.insert(FeedReaderDbHelper.FeedEntry.TABLE_NAME, null, values);
                db.close();
                Toast.makeText(getActivity(), R.string.contact_add, Toast.LENGTH_SHORT).show();

                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                fragmentTransaction.replace(R.id.container, new ContactFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
