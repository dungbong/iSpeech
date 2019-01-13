package com.example.dungbong.finalproject;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_plan:
                    //fragmentTransaction.add(R.id.container, new PlanFragment());
                    fragmentTransaction.replace(R.id.container, new PlanFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_contact:
                    fragmentTransaction.replace(R.id.container, new ContactFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_timer:
                    fragmentTransaction.replace(R.id.container, new TimerFragment());
                    fragmentTransaction.commit();
                    return true;
                case R.id.navigation_qanda:
                    fragmentTransaction.replace(R.id.container, new QandaFragment());
                    fragmentTransaction.commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        if (savedInstanceState == null) {
//            FragmentManager fragmentManager = getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//            fragmentTransaction.replace(R.id.container, new );
//            fragmentTransaction.commit();
//        }

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

}
