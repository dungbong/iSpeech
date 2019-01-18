package com.example.dungbong.finalproject;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

public class MyCursorAdapter2 extends CursorAdapter {
    private LayoutInflater cursorInflater;

    // Default constructor
    public MyCursorAdapter2(Context context, Cursor cursor) {
        super(context, cursor, 0);
    }

    public void bindView(View view, Context context, Cursor cursor) {
        TextView tv_name = (TextView) view.findViewById(R.id.tv_name2);
        TextView tv_content = (TextView) view.findViewById(R.id.tv_content);
        String name = cursor.getString( cursor.getColumnIndex( DBHelper.FeedEntry.COLUMN_NAME_TITLE ));
        String content = cursor.getString( cursor.getColumnIndex( DBHelper.FeedEntry.COLUMN_NAME_SUBTITLE ));
        tv_name.setText(name);
        tv_content.setText(content);
    }

    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        // R.layout.list_row is your xml layout for each row
        return LayoutInflater.from(context).inflate(R.layout.items2, parent, false);
    }
}
