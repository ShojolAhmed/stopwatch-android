package com.shojolahmed.stopwatch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView txtTimer, txtHint;
    private ImageButton btnReset;
    private boolean isRunning = false;

    Handler customHandler = new Handler();

    int mins, secs, millisecs;

    long startTime = 0L,
        timeInMS = 0L,
        timeSwapBuff = 0L,
        updateTime = 0L;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); //will hide the title
        getSupportActionBar().hide(); // hide the title bar
        setContentView(R.layout.activity_main);

        txtTimer = findViewById(R.id.timerTextView);
        txtHint = findViewById(R.id.hintTextView);
        btnReset = findViewById(R.id.resetButtonView);
    }

    Runnable updateTimerThread = new Runnable() {
        @Override
        public void run() {
            timeInMS = SystemClock.uptimeMillis() - startTime;
            updateTime = timeSwapBuff + timeInMS;

            secs = (int) (updateTime/1000);
            mins = secs/60;
            secs %= 60;
            millisecs = (int) (updateTime/10)%100;

            String timeFormat = String.format("%02d:%02d.%02d", mins, secs, millisecs);
            txtTimer.setText(timeFormat);
            customHandler.postDelayed(this, 0);
        }
    };

    public void startTimer(View v) {
        if (!isRunning){
            isRunning = true;
            btnReset.setVisibility(View.GONE);
            txtHint.setVisibility(View.GONE);
            startTime = SystemClock.uptimeMillis();
            customHandler.postDelayed(updateTimerThread, 0);
        }
        else if (isRunning){
            isRunning = false;
            btnReset.setVisibility(View.VISIBLE);
            timeSwapBuff += timeInMS;
            customHandler.removeCallbacks(updateTimerThread);
        }
    }

    public void resetTimer(View v) {
        startTime = 0L;
        timeInMS = 0L;
        timeSwapBuff = 0L;
        updateTime = 0L;

        txtTimer.setText(R.string.time_format);
        txtHint.setVisibility(View.VISIBLE);
        btnReset.setVisibility(View.GONE);

    }
}