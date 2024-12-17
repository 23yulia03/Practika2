package com.example.practika;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Настройка Navigation Drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_services) {
            startActivity(new Intent(this, ServicesActivity.class));
        } else if (id == R.id.nav_appointment) {
            Intent intent = new Intent(this, AppointmentActivity.class);
            intent.putExtra("userId", getUserId()); // Передаем userId
            startActivity(intent);
        } else if (id == R.id.nav_view_appointments) {
            Intent intent = new Intent(this, ViewAppointmentsActivity.class);
            intent.putExtra("userId", getUserId()); // Передаем userId
            startActivity(intent);
        }

        drawerLayout.closeDrawers();
        return true;
    }

    private int getUserId() {
        // Здесь должен быть код для получения текущего userId пользователя
        // Например, из SharedPreferences или другого источника
        return 1; // Пример: возвращаем фиксированный userId
    }
}