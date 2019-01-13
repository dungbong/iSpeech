package com.example.dungbong.finalproject;


import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.media.tv.TvView;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.zip.Inflater;


/**
 * A simple {@link Fragment} subclass.
 */
public class PlanFragment extends Fragment {

    static String selectedlist[] = {"", "", "", "", "", ""};
    private Calendar calendar = Calendar.getInstance();

    public PlanFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        //Inflate the layout for the fragment
        final View view = inflater.inflate(R.layout.fragment_plan, container, false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final Activity activity = getActivity();

        //Add plan button settting
        Button addtodo = view.findViewById(R.id.addtodo_bt);
        addtodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //state change
                fragmentTransaction.replace(R.id.container, new AddPlanFragment());
                fragmentTransaction.commit();
            }
        });

        //sotuken plan button setting
        FloatingActionButton sotukenbt = view.findViewById(R.id.sotuken_fabt);
        sotukenbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //state change
                fragmentTransaction.replace(R.id.container, new AddSotukenPlanFragment());
                fragmentTransaction.commit();
            }
        });
        //Open DB
        PlanDBHelper helper = new PlanDBHelper(getActivity());
        SQLiteDatabase db = helper.getWritableDatabase();

        try {
            db.execSQL(PlanDBHelper.SQL_CREATE_ENTRIES);
        } catch (SQLiteException e) {
            Log.e("ERROR", e.toString());
        }
        //List View setting
        final ListView list = view.findViewById(R.id.planlsit_lv);
        final ArrayList<Integer> idarray = new ArrayList<>();
        final ArrayList<String> todoarray = new ArrayList<>();
        final ArrayList yeararray = new ArrayList<>();
        final ArrayList montharray = new ArrayList<>();
        final ArrayList dayarray = new ArrayList<>();
        final ArrayList clearfarray = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + PlanDBHelper.FeedEntry.TABLE_NAME + " ORDER BY " + PlanDBHelper.FeedEntry.COLUMN_PLAN_CLEARFLAG + "," + PlanDBHelper.FeedEntry.COLUMN_PLAN_YEAR + "," + PlanDBHelper.FeedEntry.COLUMN_PLAN_MONTH + "," + PlanDBHelper.FeedEntry.COLUMN_PLAN_DAY + " DESC;", null);
        ArrayList<PlanListItem> listItem = new ArrayList<>();
        //get every record from DB
        int i = 0;
        while (cursor.moveToNext()) {
            idarray.add(cursor.getInt(0));
            todoarray.add(cursor.getString(1));
            yeararray.add(cursor.getString(2));
            montharray.add(cursor.getString(3));
            dayarray.add(cursor.getString(4));
            clearfarray.add(cursor.getInt(5));

            PlanListItem item = new PlanListItem(todoarray.get(i),
                    yeararray.get(i) + "." + montharray.get(i) + "." + dayarray.get(i),
                    (int) clearfarray.get(i));
            listItem.add(item);
            i++;
        }
        final PlanListAdapter adapter = new PlanListAdapter(getContext(), R.layout.planlistview, listItem);
        //final SimpleAdapter adapter = new SimpleAdapter(context,data , android.R.layout.simple_list_item_2, new String[] { "PLANNAME", "PLANDATE" }, new int[] { android.R.id.text1, android.R.id.text2});
        list.setAdapter(adapter);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            Activity activity = getActivity();

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                PlanDBHelper helper = new PlanDBHelper(getContext());
                SQLiteDatabase db = helper.getWritableDatabase();
                //db.delete("plantable",PlanDBHelper.FeedEntry._ID + "=" + idarray.get((int)id) ,null);
                //db.delete(PlanDBHelper.FeedEntry.TABLE_NAME,PlanDBHelper.FeedEntry.COLUMN_PLAN_YEAR + "=2017",null);
                //Toast.makeText(context,""+id,Toast.LENGTH_SHORT).show();
                list.getSelectedItem();
                adapter.notifyDataSetChanged();
                fragmentTransaction.replace(R.id.container, new AddPlanFragment());
                fragmentTransaction.commit();
                selectedlist[0] = "" + idarray.get((int) id - 1);
                selectedlist[1] = todoarray.get((int) id - 1);
                selectedlist[2] = "" + yeararray.get((int) id - 1);
                selectedlist[3] = "" + montharray.get((int) id - 1);
                selectedlist[4] = "" + dayarray.get((int) id - 1);
                selectedlist[5] = "" + clearfarray.get((int) id - 1);
            }
        });

        //remaining for Intermediate anouncement
        final int timedate[] = OpenTimedate();
        final int nowdate[] = {0, 0, 0};
        nowdate[0] = timedate[0] - calendar.get(Calendar.YEAR);
        nowdate[1] = timedate[1] - (calendar.get(Calendar.MONTH) + 1);
        nowdate[2] = timedate[2] - calendar.get(Calendar.DATE);
        TextView untiltheday = view.findViewById(R.id.textuntilday);
        untiltheday.setText("" + (nowdate[0] * 365 + nowdate[1] * 30 + nowdate[2]));
        final TextView untiltime = view.findViewById(R.id.TimeCountingtxt);

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            int untilhour = 0;
            int untilminute = 0;
            int untilsecond = 0;

            @Override
            public void run() {
                activity.runOnUiThread(new Runnable() {
                    Calendar nowtime = Calendar.getInstance();

                    @Override
                    public void run() {
                        untilhour = timedate[3] - nowtime.get(Calendar.HOUR_OF_DAY);
                        untilhour = (untilhour >= 0) ? untilhour : 12 + untilhour;
                        untilminute = timedate[4] - nowtime.get(Calendar.MINUTE);
                        untilminute = (untilminute >= 0) ? untilminute : 60 + untilminute;
                        untilsecond = timedate[5] - nowtime.get(Calendar.SECOND) - 1;
                        untilsecond = (untilsecond >= 0) ? untilsecond : 60 + untilsecond;
                        untiltime.setText(untilhour + ":" + untilminute + ":" + untilsecond);
                    }
                });
            }
        }, 0, 10);

        return view;
    }


    //calc until import schedule
    private int[] OpenTimedate() {

        int remainday = 0;
        int timedate[] = {0, 0, 0, 0, 0, 0};

        //try opening sotuken date;
        try {
            FileInputStream stream = getActivity().openFileInput("sotukendata.txt");
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(streamReader);
            String line;
            while ((line = br.readLine()) != null) {
                String date[] = line.split(",");
                timedate[0] = Integer.parseInt(date[0]);
                timedate[1] = Integer.parseInt(date[1]);
                timedate[2] = Integer.parseInt(date[2]);
                timedate[3] = Integer.parseInt(date[3]);
                timedate[4] = Integer.parseInt(date[4]);
                timedate[5] = Integer.parseInt(date[5]);

            }
            br.close();
            stream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return timedate;
    }
}
