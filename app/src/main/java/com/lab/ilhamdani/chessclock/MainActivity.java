package com.lab.ilhamdani.chessclock;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView tvWhiteTimer, tvBlackTimer;
    ColorStateList enableColor, disableColor;
    Button btnPause, btnReset;
    LinearLayout whiteContainer, blackContainer;

    private Handler handler;

    int updateThread = 1000;
    int currentPlayer;
    long whiteTimeLeft = 0, blackTimeLeft = 0;
    int whiteTotalTime = 0, blackTotalTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        handler = new Handler();

        //container
        whiteContainer = (LinearLayout)findViewById(R.id.WhiteContainer);
        blackContainer = (LinearLayout)findViewById(R.id.BlackContainer);

        //textView
        tvWhiteTimer = (TextView)findViewById(R.id.tvWhiteTimer);
        tvBlackTimer = (TextView)findViewById(R.id.tvBlackTimer);

        //button
        btnPause = (Button)findViewById(R.id.btnPause);
        btnReset = (Button)findViewById(R.id.btnReset);

        //set time
        whiteTotalTime = 20000;
        blackTotalTime = 20000;
        tvWhiteTimer.setText(getString(R.string.set_time));
        tvBlackTimer.setText(getString(R.string.set_time));

        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimer();
            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetTimer();
            }
        });

        btnPause.setEnabled(false);
        btnReset.setEnabled(false);

    }

    public void whitePlayer(View v) {
        currentPlayer = 1;
        switchPlayer (currentPlayer);
        btnPause.setEnabled(true);
        btnReset.setEnabled(true);
    }

    public void blackPlayer(View v) {
        currentPlayer = 2;
        switchPlayer(currentPlayer);
        btnPause.setEnabled(true);
        btnReset.setEnabled(true);
    }

    private void switchPlayer(int currentPlayer) {

        if (currentPlayer == 1) {
            whiteTimer.run();
            handler.removeCallbacks(blackTimer);
        }

        if (currentPlayer == 2) {
            blackTimer.run();
            handler.removeCallbacks(whiteTimer);
        }

    }

    Runnable whiteTimer = new Runnable() {
        @Override
        public void run() {
            if (blackTimeLeft > 0) {
                long seconds = (blackTotalTime / 1000) % 60;
                long minutes = (blackTotalTime / 1000) / 60;
                tvBlackTimer.setText(String.format("%02d:%02d", minutes, seconds));

                if (blackTimeLeft < 10000) {
                    updateThread = 0;
                    long milliseconds = getMilliSecond(blackTotalTime);
                    tvBlackTimer.setText(String.format("%02d:%02d:%02d", minutes, seconds, milliseconds));
                    tvBlackTimer.setTextColor(Color.RED);
                    tvBlackTimer.setTextSize(50);
                    blackTotalTime = blackTotalTime - 10;
                } else {
                    blackTotalTime = blackTotalTime - 1000;
                }

            }

            blackTimeLeft = blackTotalTime;
            if (blackTimeLeft == 0) {
                stopTimer();
            }

            Log.i("CheesClock", " blackTimer" + blackTimeLeft);
            handler.postDelayed(whiteTimer, updateThread);


        }
    };

    Runnable blackTimer = new Runnable() {
        @Override
        public void run() {
            if (whiteTimeLeft > 0) {
                long seconds = (whiteTotalTime / 1000) % 60;
                long minutes = (whiteTotalTime / 1000) / 60;
                tvWhiteTimer.setText(String.format("%02d:%02d", minutes, seconds));

                if (whiteTimeLeft < 10000) {
                    updateThread = 0;
                    long milliseconds = getMilliSecond(whiteTimeLeft);
                    tvWhiteTimer.setText(String.format("%02d:%02d:%02d", minutes, seconds, milliseconds));
                    tvWhiteTimer.setTextColor(Color.RED);
                    tvWhiteTimer.setTextSize(50);
                    whiteTotalTime = whiteTotalTime - 10;
                } else {
                    whiteTotalTime = whiteTotalTime - 1000;
                }
            }

            whiteTimeLeft = whiteTotalTime;

            if (whiteTimeLeft == 0) {
                stopTimer();
            }

            Log.i("CheesClock", "whiteTimer" + whiteTimeLeft);
            handler.postDelayed(blackTimer, updateThread);
        }
    };

    public long getMilliSecond(long timeLeft){
        timeLeft = (timeLeft % 1000);
        timeLeft = timeLeft - 10;
        return timeLeft;
    }

    public void stopTimer() {
        handler.removeCallbacks(whiteTimer);
        Log.i("CheesClock", "white timer stop");
        handler.removeCallbacks(blackTimer);
        Log.i("CheesClock", "black timer stop");
        btnPause.setEnabled(false);
        btnReset.setEnabled(false);
    }

    public void resetTimer(){
        stopTimer();
        whiteTotalTime = 180000;
        blackTotalTime = 180000;
        tvWhiteTimer.setText(getString(R.string.set_time));
        tvBlackTimer.setText(getString(R.string.set_time));
    }

}
