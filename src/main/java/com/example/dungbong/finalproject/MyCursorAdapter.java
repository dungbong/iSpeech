package com.example.dungbong.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter extends CursorAdapter {
    private LayoutInflater cursorInflater;

    // Default constructor
    public MyCursorAdapter(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name);
        TextView tv_mail = (TextView) view.findViewById(R.id.tv_mail);
        String name = cursor.getString( cursor.getColumnIndex( FeedReaderDbHelper.FeedEntry.COLUMN_NAME_TITLE ));
        String mail = cursor.getString( cursor.getColumnIndex( FeedReaderDbHelper.FeedEntry.COLUMN_NAME_SUBTITLE ));
        tv_name.setText(name);
        tv_mail.setText(mail);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // R.layout.list_row is your xml layout for each row
        return LayoutInflater.from(context).inflate(R.layout.items, parent, false);
    }
}
