package com.example.dungbong.finalproject;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class QandaFragment extends Fragment {


    public QandaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_qanda, container, false);

        Context context = getActivity();
        DBHelper DbHelper = new DBHelper(context);
        SQLiteDatabase db = DbHelper.getWritableDatabase();

        ListView listview = view.findViewById(R.id.listview2);
        try {
            db.execSQL(DbHelper.SQL_CREATE_ENTRIES);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        Cursor cursor = db.rawQuery("SELECT * FROM comment", null);

        final MyCursorAdapter2 arrayAdapter = new MyCursorAdapter2(context, cursor);
        listview.setAdapter(arrayAdapter);
        listview.setSelection(listview.getAdapter().getCount()-1);

        db.close();

        Button bt_cmt = view.findViewById(R.id.bt_cmt);
        bt_cmt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                EditText text = view.findViewById(R.id.cmt);
                String string_cmt = text.getText().toString();

                EditText text2 = view.findViewById(R.id.et_name);
                String string_name = text2.getText().toString();

                DBHelper DbHelper = new DBHelper(getActivity());
                SQLiteDatabase db = DbHelper.getWritableDatabase();

                ContentValues values = new ContentValues();
                values.put(DBHelper.FeedEntry.COLUMN_NAME_TITLE, string_name);
                values.put(DBHelper.FeedEntry.COLUMN_NAME_SUBTITLE, string_cmt);
                long newRowId = db.insert(DBHelper.FeedEntry.TABLE_NAME, null, values);

                Toast.makeText(getActivity(), R.string.cmt_add, Toast.LENGTH_SHORT).show();
                db.close();

                fragmentTransaction.replace(R.id.container, new QandaFragment());
                fragmentTransaction.commit();
            }
        });

        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                FeedReaderDbHelper mDbHelper = new FeedReaderDbHelper(getActivity());

                SQLiteDatabase db = mDbHelper.getWritableDatabase();
                db.delete("comment", FeedReaderDbHelper.FeedEntry._ID + "=" + id, null);


                Toast.makeText(getActivity(), R.string.cmt_delete, Toast.LENGTH_SHORT).show();

                arrayAdapter.notifyDataSetChanged();
                db.close();
                fragmentTransaction.replace(R.id.container, new QandaFragment());
                fragmentTransaction.commit();

                return false;
            }
        });

        return view;
    }

}
