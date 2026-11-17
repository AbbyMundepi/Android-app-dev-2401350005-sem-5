package com.example.booknest;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

/**
 * AboutActivity displaying BookNest application details,
 * core capabilities, technologies used, and academic project information.
 * Includes lifecycle logging and state callbacks.
 */
public class AboutActivity extends AppCompatActivity {
    private static final String LIFECYCLE_TAG = "BookNestLifecycle";

    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(LIFECYCLE_TAG, "AboutActivity: onCreate");
        setContentView(R.layout.activity_about);

        btnBack = findViewById(R.id.btnBack);
        View rootLayout = findViewById(R.id.rootLayout);
        View headerBar = findViewById(R.id.headerBar);
        View bottomNav = findViewById(R.id.bottomNavigationContainer);

        // Setup Edge-to-Edge System Bar Insets & Notch Protection
        EdgeToEdgeUtils.applyEdgeToEdge(this, rootLayout, headerBar, bottomNav);
        if (btnBack != null) {
            btnBack.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        // Setup Bottom Navigation
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        View navHome = findViewById(R.id.navHome);
        if (navHome != null) {
            navHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AboutActivity.this, MainActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }

        View navMyBooks = findViewById(R.id.navMyBooks);
        if (navMyBooks != null) {
            navMyBooks.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(AboutActivity.this, BorrowedBooksActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(LIFECYCLE_TAG, "AboutActivity: onStart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d(LIFECYCLE_TAG, "AboutActivity: onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(LIFECYCLE_TAG, "AboutActivity: onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(LIFECYCLE_TAG, "AboutActivity: onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LIFECYCLE_TAG, "AboutActivity: onDestroy");
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        Log.d(LIFECYCLE_TAG, "AboutActivity: onSaveInstanceState");
    }
}
