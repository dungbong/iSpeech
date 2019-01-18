package com.example.dungbong.finalproject;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;

public class AddSotukenPlanFragment extends Fragment {
    public AddSotukenPlanFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_addextra, container, false);
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        final Context context = getActivity();

        Calendar calendar = Calendar.getInstance();//get now date

        String yearspItems[] = new String[6];
        for(int i=0;i<yearspItems.length;i++)yearspItems[i] = ""+ (calendar.get(Calendar.YEAR) -yearspItems.length/2 + 1 + i);
        String monthspItems[] = new String[12];for(int i=0;i<monthspItems.length;i++)monthspItems[i] = ""+(i+1);
        String dayspItems[] = new String[31];for(int i=0;i<dayspItems.length;i++)dayspItems[i]=""+(i+1);
        String hourspItems[] = new String [24];for(int i=0;i<hourspItems.length;i++)hourspItems[i] = ""+i;
        String minutespItems[] = new String[60];for(int i=0;i<minutespItems.length;i++)minutespItems[i] = ""+i;
        String secondspItems[] = new String[60];for(int i=0;i<secondspItems.length;i++)secondspItems[i] = ""+i;
        final Spinner yearsp = view.findViewById(R.id.sotukenyearsp);
        final Spinner monthsp = view.findViewById(R.id.sotukenmonthsp);
        final Spinner daysp = view.findViewById(R.id.sotukendaysp);
        final Spinner hoursp = view.findViewById(R.id.sotukenhoursp);
        final Spinner minutesp = view.findViewById(R.id.sotukenminurtesp);
        final Spinner secondsp = view.findViewById(R.id.sotukensecondsp);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,yearspItems);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,monthspItems);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,dayspItems);
        ArrayAdapter<String> adapter4 = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,hourspItems);
        ArrayAdapter<String> adapter5 = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,minutespItems);
        ArrayAdapter<String> adapter6 = new ArrayAdapter<>(context,android.R.layout.simple_spinner_item,secondspItems);
        yearsp.setAdapter(adapter1);
        monthsp.setAdapter(adapter2);
        daysp.setAdapter(adapter3);
        hoursp.setAdapter(adapter4);
        minutesp.setAdapter(adapter5);
        secondsp.setAdapter(adapter6);

        Button okbt = view.findViewById(R.id.sotukenokbt);
        okbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    FileOutputStream stream = getActivity().openFileOutput("sotukendata.txt", Context.MODE_PRIVATE);
                    String sotukenstr = (String)yearsp.getSelectedItem() + "," + (String)monthsp.getSelectedItem() + "," + (String)daysp.getSelectedItem() +","+ (String)hoursp.getSelectedItem() + "," + (String)minutesp.getSelectedItem() + "," + (String)secondsp.getSelectedItem() +  ",\n" ;
                    stream.write(sotukenstr.getBytes());
                }catch(IOException e) {
                    e.printStackTrace();
                }

                fragmentTransaction.replace(R.id.container, new PlanFragment());
                fragmentTransaction.commit();
            }
        });

        Button cancelbt = view.findViewById(R.id.sotukencancelbt);
        cancelbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentTransaction.replace(R.id.container, new PlanFragment());
                fragmentTransaction.commit();
            }
        });

        return view;
    }
}
