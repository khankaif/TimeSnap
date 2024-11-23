package com.example.instagramlimiter;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Start the AppUsageService when the app is launched
        Intent serviceIntent = new Intent(this, AppUsageService.class);
        startService(serviceIntent);

        // Close the MainActivity as it serves only to start the service
        finish();
    }
}
