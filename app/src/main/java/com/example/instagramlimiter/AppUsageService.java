//package com.example.instagramlimiter;
//
//import android.accessibilityservice.AccessibilityService;
//import android.content.Intent;
//import android.os.Handler;
//import android.os.Looper;
//import android.util.Log; // Import Log class
//import android.view.accessibility.AccessibilityEvent;
//import android.widget.Toast;
//
//public class AppUsageService extends AccessibilityService {
//
//    private static final long USAGE_LIMIT = 5 * 1000; // 5 seconds for testing
//    private static final long BLOCK_TIME = 60 * 60 * 1000; // 1 hour
//    private static final String TAG = "AppUsageService"; // Tag for logging
//
//    private long startTime = 0;
//    private boolean isBlocked = false;
//
//    private Handler handler = new Handler(Looper.getMainLooper());
//
//    @Override
//    public void onAccessibilityEvent(AccessibilityEvent event) {
//        if (event.getPackageName() != null && event.getPackageName().equals("com.instagram.android")) {
//            Log.d(TAG, "Instagram is opened"); // Log when Instagram is opened
//            if (!isBlocked) {
//                if (startTime == 0) {
//                    startTime = System.currentTimeMillis();
//                    Log.d(TAG, "Start time set: " + startTime); // Log the start time
//                } else if (System.currentTimeMillis() - startTime > USAGE_LIMIT) {
//                    Log.d(TAG, "Usage limit reached. Blocking app."); // Log when usage limit is reached
//                    blockApp();
//                } else {
//                    Log.d(TAG, "Time used: " + (System.currentTimeMillis() - startTime) + " ms"); // Log time used
//                }
//            } else {
//                Toast.makeText(this, "Instagram is blocked for an hour", Toast.LENGTH_SHORT).show();
//                Log.d(TAG, "Instagram is currently blocked."); // Log when Instagram is blocked
//                openHomeScreen();
//            }
//        }
//    }
//
//    @Override
//    public void onInterrupt() {
//        // You can add logging here if necessary
//        Log.d(TAG, "Accessibility Service interrupted");
//    }
//
////    private void blockApp() {
////        isBlocked = true;
////        Toast.makeText(this, "Instagram usage limit reached. Blocked for an hour.", Toast.LENGTH_LONG).show();
////        openHomeScreen();
////
////        // Schedule unblock after the block time
////        handler.postDelayed(() -> {
////            isBlocked = false;
////            startTime = 0; // Reset startTime after unblock
////            Toast.makeText(this, "Instagram is now accessible.", Toast.LENGTH_LONG).show();
////            Log.d(TAG, "Instagram is now accessible."); // Log when Instagram is unblocked
////        }, BLOCK_TIME);
////    }
//
//    private void blockApp() {
//        isBlocked = true;
//        Toast.makeText(this, "Instagram usage limit reached. Blocked for an hour.", Toast.LENGTH_LONG).show();
//        openHomeScreen();
//
//        // Start BlockedActivity with time remaining as an extra
//        Intent intent = new Intent(this, BlockedActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        intent.putExtra("timeRemaining", BLOCK_TIME); // Pass the block time
//        startActivity(intent);
//
//        handler.postDelayed(() -> {
//            isBlocked = false;
//            startTime = 0;
//            Toast.makeText(this, "Instagram is now accessible.", Toast.LENGTH_LONG).show();
//        }, BLOCK_TIME);
//    }
//
//
//    private void openHomeScreen() {
//        Intent startMain = new Intent(Intent.ACTION_MAIN);
//        startMain.addCategory(Intent.CATEGORY_HOME);
//        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(startMain);
//    }
//}


// NEW CODE

package com.example.instagramlimiter;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.view.accessibility.AccessibilityEvent;
import android.widget.Toast;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class AppUsageService extends AccessibilityService {

    private static final long USAGE_LIMIT = 5 * 1000; // 5 minutes
    private static final long BLOCK_TIME = 60 * 60 * 1000; // 1 hour

    private long startTime = 0;
    private boolean isBlocked = false;

    private Handler handler = new Handler(Looper.getMainLooper());

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (event.getPackageName() != null && event.getPackageName().equals("com.instagram.android")) {
            if (!isBlocked) {
                if (startTime == 0) {
                    startTime = System.currentTimeMillis();
                } else if (System.currentTimeMillis() - startTime > USAGE_LIMIT) {
                    blockApp();
                }
            } else {
                showBreakScreen();
            }
        }
    }

    @Override
    public void onInterrupt() {
    }


    // Add this variable to your service
    private long remainingBreakTime;

    // Call this method to save remaining time
    private void saveRemainingTime(long time) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong("remainingBreakTime", time);
        editor.apply();
    }

    // Call this method to retrieve remaining time
    private long getRemainingTime() {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        return preferences.getLong("remainingBreakTime", 0);
    }

    private void blockApp() {
        isBlocked = true;
        Toast.makeText(this, "Instagram usage limit reached. Blocked for an hour.", Toast.LENGTH_LONG).show();
        showBreakScreen();

        handler.postDelayed(() -> {
            isBlocked = false;
            startTime = 0;
            Toast.makeText(this, "Instagram is now accessible.", Toast.LENGTH_LONG).show();
        }, BLOCK_TIME);
    }

//    private void showBreakScreen() {
//        Intent intent = new Intent(this, BlockedActivity.class); // Replace with your break screen activity
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(intent);
//    }

    private void showBreakScreen() {
        Intent intent = new Intent(this, BlockedActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP); // Prevents multiple instances
        startActivity(intent);
    }

}
