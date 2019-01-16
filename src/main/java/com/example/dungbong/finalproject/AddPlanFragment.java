package com.example.dungbong.finalproject;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Calendar;

public class AddPlanFragment extends Fragment {

    public AddPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragmente_add_plan, container, false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        final Context context =getActivity();

        Calendar calendar = Calendar.getInstance();//get now date

        //some spinner's settings
        String yearspItems[] = new String[6];
        for(int i=0;i<yearspItems.length;i++){
            yearspItems[i] = ""+ (calendar.get(Calendar.YEAR) -yearspItems.length/2 + 1 + i);
        }
        String monthspItems[] = {"1","2","3","4","5","6","7","8","9","10","11","12"};
        String dayspItems[] ={"1","2","3","4","5","6","7","8","9","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26","27","28","29","30","31"};
        final Spinner yearsp = view.findViewById(R.id.planyear_spinner);
        final Spinner monthsp = view.findViewById(R.id.planmonth_spinner);
        final Spinner daysp = view.findViewById(R.id.planday_spinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,yearspItems);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,monthspItems);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,dayspItems);
        yearsp.setAdapter(adapter1);
        monthsp.setAdapter(adapter2);
        daysp.setAdapter(adapter3);
        EditText planname = view.findViewById(R.id.planname_edtxt);
        final CheckBox checkBox = view.findViewById(R.id.checkBox);
        final int plandbid,yearnum ,monthnum,daynum,clearfnum;

        if(PlanFragment.selectedlist[1]!="") {
            plandbid =Integer.parseInt(PlanFragment.selectedlist[0]);
            planname.setText(PlanFragment.selectedlist[1]);
            yearnum =Integer.parseInt(PlanFragment.selectedlist[2]);
            monthnum = Integer.parseInt(PlanFragment.selectedlist[3]);
            daynum = Integer.parseInt(PlanFragment.selectedlist[4]);
            if(Integer.parseInt(PlanFragment.selectedlist[5])==1)checkBox.setChecked(true);
            else checkBox.setChecked(false);
            yearsp.setSelection(yearnum - (calendar.get(Calendar.YEAR) - yearspItems.length/2 + 1));
            monthsp.setSelection(monthnum - 1);
            daysp.setSelection(daynum - 1);
        }
        else{
            plandbid = -1;
            planname.setText("");
            yearsp.setSelection(0);
            monthsp.setSelection(0);
            daysp.setSelection(0);
            checkBox.setChecked(false);

        }
        //Plan Confirm button setting
        Button confirmbt = view.findViewById(R.id.planconf_bt);
        confirmbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get every gadjet data
                EditText editText =getView().findViewById(R.id.planname_edtxt);
                String planname = editText.getText().toString();
                String yeardata = (String)yearsp.getSelectedItem();
                String monthdata = (String)monthsp.getSelectedItem();
                String daydata = (String)daysp.getSelectedItem();
                //if plannname is empty,
                if(planname.isEmpty() == true) {
                    //Alert
                    new AlertDialog
                            .Builder(context).setTitle("EMPTY ALERT!")
                            .setMessage("PLAN NAME IS EMPTY!! SET NAME!!!")
                            .setPositiveButton("OK", null)
                            .show();
                }
                else{
                    //opendb
                    PlanDBHelper helper = new PlanDBHelper(getContext());
                    SQLiteDatabase db = helper.getWritableDatabase();


                    if(plandbid != -1) {
                        db.delete(PlanDBHelper.FeedEntry.TABLE_NAME,PlanDBHelper.FeedEntry._ID + "=" + plandbid,null);
                    }
                        ContentValues values = new ContentValues();
                        values.put(PlanDBHelper.FeedEntry.COLUMN_PLAN_NAME, planname);
                        values.put(PlanDBHelper.FeedEntry.COLUMN_PLAN_YEAR, yeardata);
                        values.put(PlanDBHelper.FeedEntry.COLUMN_PLAN_MONTH, monthdata);
                        values.put(PlanDBHelper.FeedEntry.COLUMN_PLAN_DAY, daydata);
                        if(checkBox.isChecked() == true)values.put(PlanDBHelper.FeedEntry.COLUMN_PLAN_CLEARFLAG, 1);
                        else values.put(PlanDBHelper.FeedEntry.COLUMN_PLAN_CLEARFLAG, 0);
                        //insert the record
                        db.insert(PlanDBHelper.FeedEntry.TABLE_NAME, null, values);
                        db.close();
                        //Move state :Plan page
                        fragmentTransaction.replace(R.id.container, new PlanFragment());
                        fragmentTransaction.commit();

                }


            }
        });

        Button delete = view.findViewById(R.id.plancancel_bt);
        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                PlanDBHelper helper = new PlanDBHelper(getContext());
                SQLiteDatabase db = helper.getWritableDatabase();

                if(plandbid != -1) {
                    db.delete(PlanDBHelper.FeedEntry.TABLE_NAME,PlanDBHelper.FeedEntry._ID + "=" + plandbid,null);
                }
                //move state : Plan page
                fragmentTransaction.replace(R.id.container, new PlanFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
