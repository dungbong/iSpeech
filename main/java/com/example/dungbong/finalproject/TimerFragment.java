package com.example.dungbong.finalproject;



import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class TimerFragment extends Fragment {
    //発表時間
    private long timer = 190000;
    //延長時間をはかる。
    private  long timer2;
    //スレッドの有効/無効
    private  boolean timestop = true;
    private  boolean mAttached = true;

    //発表時間を表示する
    private TextView textView ;
    //発表時間をスタート/ストップ
    private Button buttonstartstop;
    //延長時間をストップ
    private Button buttonstop2;
    //発表時間をリセット
    private Button buttonreset;

    //時間を増やす
    private ImageButton bt1;
    //時間を減らす
    private ImageButton bt2;

    //分か秒か変更
   private RadioButton radioButtonmin;
   private RadioButton radioButtonsec;

   //発表時間を1秒ずつ減らす。
    private CountDownTimer countDownTimer;

    //延長時間を0から1秒ずつ増える。
    private Thread thread;

    //発表時間を動くか止めるか
    private boolean timerun;

    //
    private long timerunning = timer;

    //音声
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
        final Context context = getContext();
        final Activity activity = getActivity();

        mediaPlayer0 = MediaPlayer.create(context, R.raw.start);
        mediaPlayer1 = MediaPlayer.create(context, R.raw.finish);
        mediaPlayer2 = MediaPlayer.create(context, R.raw.finished);

        //textView-発表時間
        textView = view.findViewById(R.id.textViewTimer);

        //各button
        buttonstartstop =view.findViewById(R.id.buttonStartStop);
        buttonstop2 =view.findViewById(R.id.dungthem);
        buttonreset = view.findViewById(R.id.buttonReset);
        bt1 = view.findViewById(R.id.bttang);
        bt2 = view.findViewById(R.id.btgiam);
        radioButtonmin = view.findViewById(R.id.minute);
        radioButtonsec = view.findViewById(R.id.second);

        //各buttonのクリックするイベント

        // 1 - スタート/ストップ
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


        // 2 - リセット
        buttonreset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer0.start();
                resetTimer();
                buttonstop2.setText(R.string.reset);
                buttonstop2.setVisibility(View.INVISIBLE);

            }
        });

        // 3 - " + "
        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer0.start();
                increase();

            }

        });

        // 4 - " - "
        bt2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaPlayer0.start();
                reduce();
            }
        });

        // 5 - 延長時間をストップ
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
                                String timeformat1 = String.format(Locale.getDefault(), "%02d:%02d",
                                        (int)(timer2/60), (int)(timer2%60));
                                buttonstop2.setText(timeformat1);

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



    //
    private void increase(){
        if (radioButtonmin.isChecked()) {
            timerunning = timerunning + 60000;
        }else {
            timerunning = timerunning + 1000;
        }
        updateCountDownText();

    }

    //
    private void reduce(){
        if (radioButtonmin.isChecked()) {
            if ((int)(timerunning /6000) != 0){
                timerunning = timerunning - 60000;
            }else{
                timerunning = timerunning;
            }
        }else {
            if(((int)timerunning % 6000 == 0)&&((int)(timerunning / 6000) == 0)){

            }else{
            timerunning = timerunning - 1000;}
        }
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
                buttonstartstop.setText(R.string.start);
            }
        }.start();

        buttonstartstop.setText(R.string.stop);
        timerun = true;
        buttonreset.setVisibility(View.INVISIBLE);
        bt1.setVisibility(View.INVISIBLE);
        bt2.setVisibility(View.INVISIBLE);
        radioButtonmin.setVisibility(View.INVISIBLE);
        radioButtonsec.setVisibility(View.INVISIBLE);
    }




    private void pauseTimer(){
        countDownTimer.cancel();
        timerun = false;
        buttonstartstop.setText(R.string.start);
        buttonreset.setVisibility(View.VISIBLE);
        bt1.setVisibility(View.VISIBLE);
        bt2.setVisibility(View.VISIBLE);
        radioButtonmin.setVisibility(View.VISIBLE);
        radioButtonsec.setVisibility(View.VISIBLE);
    }

    private void resetTimer(){
        timerunning = timer;
        updateCountDownText();
    }
    private void updateCountDownText(){
        int minutes = (int)((timerunning/1000)  / 60);
        int seconds = (int)((timerunning/1000) % 60);

        if ((minutes == 1)&&(seconds == 0)){

            mediaPlayer1.start();
            String timeformat = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            textView.setText(timeformat);

        } else if((minutes == 00)&&(seconds == 0)) {

            mediaPlayer2.start();
            String timeformat = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
            textView.setText(timeformat);
            buttonstop2.setVisibility(View.VISIBLE);
            bt1.setVisibility(View.VISIBLE);
            bt2.setVisibility(View.VISIBLE);
            radioButtonmin.setVisibility(View.VISIBLE);
            radioButtonsec.setVisibility(View.VISIBLE);
            timer2 = 0;
            timestop = false;

        }else {
            String timeformat = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);

            textView.setText(timeformat);
        }
    }

}

