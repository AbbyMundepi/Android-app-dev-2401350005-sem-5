package com.example.testproject;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Re-enable EdgeToEdge to push content under system bars
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        // Add padding to the root layout to avoid overlapping with the status bar (notch area)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.drawerLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(0, systemBars.top, 0, systemBars.bottom);
            return insets;
        });

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup Navigation Drawer
        drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navView = findViewById(R.id.navView);
        navView.setNavigationItemSelectedListener(this);

        // Setup Hamburger Icon Toggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Standard App Buttons
        findViewById(R.id.btn_open_calculator).setOnClickListener(v -> startActivity(new Intent(this, CalculatorActivity.class)));
        findViewById(R.id.btn_open_second_activity).setOnClickListener(v -> startActivity(new Intent(this, Second_activity.class)));
        findViewById(R.id.btn_open_checkbox).setOnClickListener(v -> startActivity(new Intent(this, CheckboxActivity.class)));
        findViewById(R.id.btn_open_listview).setOnClickListener(v -> startActivity(new Intent(this, ListViewActivity.class)));
        findViewById(R.id.btn_open_login_page).setOnClickListener(v -> startActivity(new Intent(this, Login_page.class)));
        findViewById(R.id.btn_open_select_color).setOnClickListener(v -> startActivity(new Intent(this, Select_color.class)));
        findViewById(R.id.btn_open_hello_page).setOnClickListener(v -> startActivity(new Intent(this, HelloActivity.class)));
        findViewById(R.id.btn_open_google).setOnClickListener(v -> startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"))));
        findViewById(R.id.btn_open_caller).setOnClickListener(v -> startActivity(new Intent(this, CallerActivity.class)));
        findViewById(R.id.btn_open_location).setOnClickListener(v -> startActivity(new Intent(this, LocationActivity.class)));
        findViewById(R.id.btn_open_file_manager).setOnClickListener(v -> startActivity(new Intent(this, FileManagerActivity.class)));
        findViewById(R.id.btn_open_grid_layout).setOnClickListener(v -> startActivity(new Intent(this, GridLayoutDemoActivity.class)));
        findViewById(R.id.btn_open_frame_layout).setOnClickListener(v -> startActivity(new Intent(this, FrameLayoutActivity.class)));
        findViewById(R.id.btn_open_linear_layout).setOnClickListener(v -> startActivity(new Intent(this, LinearLayoutDemoActivity.class)));
        findViewById(R.id.btn_open_food_order).setOnClickListener(v -> startActivity(new Intent(this, FoodOrderActivity.class)));
        findViewById(R.id.btn_open_splash_screen).setOnClickListener(v -> startActivity(new Intent(this, SplashActivity.class)));
        findViewById(R.id.btn_open_fragment_demo).setOnClickListener(v -> startActivity(new Intent(this, FragmentDemoActivity.class)));
        findViewById(R.id.btn_open_localization).setOnClickListener(v -> startActivity(new Intent(this, LocalizationActivity.class)));
        findViewById(R.id.btn_open_fab_demo).setOnClickListener(v -> startActivity(new Intent(this, FabDemoActivity.class)));
        findViewById(R.id.btn_open_pdf_demo).setOnClickListener(v -> startActivity(new Intent(this, PdfDemoActivity.class)));
        findViewById(R.id.btnAbout).setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AboutActivity.class)));
        findViewById(R.id.btn_open_battery_demo).setOnClickListener(v -> startActivity(new Intent(this, BatteryDemoActivity.class)));
        findViewById(R.id.btn_open_airplane_mode_demo).setOnClickListener(v -> startActivity(new Intent(this, AirplaneModeActivity.class)));
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        
        if (id == R.id.nav_home) {
            Toast.makeText(this, "Home Selected", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_profile) {
            Toast.makeText(this, "Profile Selected", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_settings) {
            Toast.makeText(this, "Settings Selected", Toast.LENGTH_SHORT).show();
        } else if (id == R.id.nav_about) {
            Toast.makeText(this, "About Selected", Toast.LENGTH_SHORT).show();
        }

        drawerLayout.closeDrawers();
        return true;
    }
}
