package com.example.dungbong.finalproject;



import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment {
    private long timer = 10000;
    private  long timer2;
    private  boolean timestop = true;
    private  boolean mAttached = true;

    private TextView textView ;
    private Button buttonstartstop;
    private Button buttonstop2;
    private Button buttonreset;

    private ImageButton bt1;
    private ImageButton bt2;

    private CountDownTimer countDownTimer;

    private Thread thread;

    private boolean timerun;

    private long timerunning = timer;

    private MediaPlayer mediaPlayer0;
    private MediaPlayer mediaPlayer1;
    private MediaPlayer mediaPlayer2;

    public TimerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_timer, container, false);
        FragmentManager fragmentManager =getActivity().getSupportFragmentManager();
        final FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Context context = getContext();
        final Activity activity = getActivity();

        mediaPlayer0 = MediaPlayer.create(context, R.raw.start);
        mediaPlayer1 = MediaPlayer.create(context, R.raw.finish);
        mediaPlayer2 = MediaPlayer.create(context, R.raw.finished);

        //発表時間
        textView = view.findViewById(R.id.textViewTimer);
        //時間超える
        buttonstartstop =view.findViewById(R.id.buttonStartStop);
        buttonstop2 =view.findViewById(R.id.dungthem);
        buttonreset = view.findViewById(R.id.buttonReset);
        bt1 = view.findViewById(R.id.bttang);
        bt2 = view.findViewById(R.id.btgiam);

        buttonstartstop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer0.start();
                if (timerun){
                    pauseTimer();
                }else{
                    startTimer();

                }
            }
        });


        buttonreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer0.start();
                resetTimer();
                buttonstop2.setText("延長時間");
                buttonstop2.setVisibility(View.INVISIBLE);

            }
        });

        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer0.start();
                increase();

            }

        });

        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer0.start();
                reduce();
            }
        });
        buttonstop2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer0.start();
                timestop = true;
            }
        });
        updateCountDownText();

        thread = new Thread(){
            @Override
            public void run() {
                while (mAttached ) {
                    if(timestop){
                        continue;
                    }

                    try {
                        Thread.sleep(1000);

                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                timer2++;
                                buttonstop2.setText(String.valueOf(timer2));

                            }
                        });

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                }
            }
        };
        thread.start();

        return view;

    }



    private void increase(){
        timerunning = timerunning+1000;
        updateCountDownText();

    }

    private void reduce(){
        timerunning = timerunning-1000;
        updateCountDownText();

    }

    private void startTimer(){
        countDownTimer = new CountDownTimer(timerunning,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                timerunning = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                timerun = false;
                buttonreset.setVisibility(View.VISIBLE);
                buttonstartstop.setText("Start");
            }
        }.start();

        buttonstartstop.setText("Stop");
        timerun = true;
        buttonreset.setVisibility(View.INVISIBLE);
        bt1.setVisibility(View.INVISIBLE);
        bt2.setVisibility(View.INVISIBLE);
    }




    private void pauseTimer(){
        countDownTimer.cancel();
        timerun = false;
        buttonstartstop.setText("Start");
        buttonreset.setVisibility(View.VISIBLE);
        bt1.setVisibility(View.VISIBLE);
        bt2.setVisibility(View.VISIBLE);
    }

    private void resetTimer(){
        timerunning = timer;
        updateCountDownText();
    }
    private void updateCountDownText(){
        int minutes = (int)((timerunning/1000)  / 60);
        int seconds = (int)((timerunning/1000) % 60);

        if (seconds == 5){

            mediaPlayer1.start();
            String timeformat = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            textView.setText(timeformat);

        } else if((minutes == 0) && (seconds == 0)) {

            mediaPlayer2.start();
            String timeformat = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            textView.setText(timeformat);
            buttonstop2.setVisibility(View.VISIBLE);
            bt1.setVisibility(View.VISIBLE);
            bt2.setVisibility(View.VISIBLE);
            timer2 = 0;
            timestop = false;

        }else {
            String timeformat = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            textView.setText(timeformat);
        }
    }

}

