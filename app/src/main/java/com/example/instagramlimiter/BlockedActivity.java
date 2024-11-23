//package com.example.instagramlimiter;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Looper;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.TextView;
//import android.content.SharedPreferences;
//import android.preference.PreferenceManager; // Use androidx.preference.PreferenceManager if you're using AndroidX
//
//
//import java.util.Locale;
////public class BlockedActivity extends Activity {
////    private TextView timerText;
////    private Handler handler;
////    private long timeRemaining; // Time remaining in milliseconds
////
////    @Override
////    protected void onCreate(Bundle savedInstanceState) {
////        super.onCreate(savedInstanceState);
////        setContentView(R.layout.activity_break_screen);
////
////
////
//////        TextView timerText = findViewById(R.id.timer_text);
//////        TextView breakText = findViewById(R.id.break_text);
//////        ImageView timerIcon = findViewById(R.id.timer_icon);
//////        Button closeButton = findViewById(R.id.close_button);
////
////        timerText = findViewById(R.id.timer_text);
////        Button closeAppButton = findViewById(R.id.close_button);
////
////        // Retrieve time remaining from Intent
////        timeRemaining = getIntent().getLongExtra("timeRemaining", 0);
////
////        // Start the countdown
////        handler = new Handler(Looper.getMainLooper());
////        startCountdown();
////
////
////
////        closeAppButton.setOnClickListener(v -> finishAffinity()); // Close app completely
////    }
////
////    private void startCountdown() {
////        handler.postDelayed(new Runnable() {
////            @Override
////            public void run() {
////                if (timeRemaining > 0) {
////                    updateTimerDisplay();
////                    timeRemaining -= 1000;
////                    handler.postDelayed(this, 1000);
////                } else {
////                    finish(); // Close this screen when time is up
////                }
////            }
////        }, 1000);
////    }
////
////    private void updateTimerDisplay() {
////        int minutes = (int) (timeRemaining / 1000) / 60;
////        int seconds = (int) (timeRemaining / 1000) % 60;
////        timerText.setText(String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds));
////    }
////}
//
//
//
//
//import android.os.CountDownTimer;
//
//
//public class BlockedActivity extends Activity {
//
//    private TextView timerText;
//    private Button closeAppButton;
//    private long breakTime; // Total break time (1 hour)
//    private long remainingBreakTime; // Remaining break time
//    private CountDownTimer countDownTimer;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_break_screen);
//
//        timerText = findViewById(R.id.timer_text);
//        closeAppButton = findViewById(R.id.close_button);
//
//        breakTime = 3600000; // 1 hour in milliseconds
//        remainingBreakTime = getRemainingTime(); // Get remaining time
//
//        // Start the timer with remaining time or total break time
//        startTimer(remainingBreakTime > 0 ? remainingBreakTime : breakTime);
//
//        closeAppButton.setOnClickListener(view -> finishAffinity()); // Close the app completely
//    }
//
//    private void startTimer(long time) {
//        countDownTimer = new CountDownTimer(time, 1000) { // 1 second interval
//            @Override
//            public void onTick(long millisUntilFinished) {
//                timerText.setText(String.format("You can come back in %02d:%02d",
//                        (millisUntilFinished / 1000) / 60,
//                        (millisUntilFinished / 1000) % 60));
//                saveRemainingTime(millisUntilFinished); // Save remaining time
//            }
//
//            @Override
//            public void onFinish() {
//                saveRemainingTime(0); // Reset stored time when finished
//                finish(); // Closes this activity once the time is up
//            }
//        }.start();
//    }
//
//    @Override
//    protected void onDestroy() {
//        if (countDownTimer != null) {
//            countDownTimer.cancel(); // Cancel the timer if the activity is destroyed
//        }
//        super.onDestroy();
//    }
//
//    // Save remaining time when the activity is paused or destroyed
//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (countDownTimer != null) {
//            countDownTimer.cancel(); // Stop the timer if the activity is paused
//        }
//    }
//
//    // Reset timer on activity resume
//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Get remaining time when resuming
//        remainingBreakTime = getRemainingTime();
//        if (remainingBreakTime > 0) {
//            startTimer(remainingBreakTime);
//        }
//    }
//}
// NEW CODE

package com.example.instagramlimiter;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class BlockedActivity extends AppCompatActivity {

    private TextView timerText;
    private Button closeAppButton;
    private long breakTime; // Total break time (1 hour)
    private long remainingBreakTime; // Remaining break time
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_break_screen); // Ensure this matches your layout file

        timerText = findViewById(R.id.timer_text);
        closeAppButton = findViewById(R.id.close_button);

        breakTime = 3600000; // 1 hour in milliseconds
        remainingBreakTime = getRemainingTime(); // Get remaining time from SharedPreferences

        // Start the timer with remaining time or total break time
        startTimer(remainingBreakTime > 0 ? remainingBreakTime : breakTime);

        closeAppButton.setOnClickListener(view -> finishAffinity()); // Close the app completely
    }

    private void startTimer(long time) {
        countDownTimer = new CountDownTimer(time, 1000) { // 1 second interval
            @Override
            public void onTick(long millisUntilFinished) {
                timerText.setText(String.format("%02d:%02d",
                        (millisUntilFinished / 1000) / 60,
                        (millisUntilFinished / 1000) % 60));
                saveRemainingTime(millisUntilFinished); // Save remaining time
            }

            @Override
            public void onFinish() {
                saveRemainingTime(0); // Reset stored time when finished
                finish(); // Closes this activity once the time is up
            }
        }.start();
    }

    private void saveRemainingTime(long time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("remainingBreakTime", time);
        editor.apply();
    }

    private long getRemainingTime() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getLong("remainingBreakTime", 0);
    }

    @Override
    protected void onDestroy() {
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Cancel the timer if the activity is destroyed
        }
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (countDownTimer != null) {
            countDownTimer.cancel(); // Stop the timer if the activity is paused
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Get remaining time when resuming
        remainingBreakTime = getRemainingTime();
        if (remainingBreakTime > 0) {
            startTimer(remainingBreakTime);
        }
    }
}

