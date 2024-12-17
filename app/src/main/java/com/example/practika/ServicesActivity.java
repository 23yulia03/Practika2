package com.example.practika;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ServicesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private RecyclerView recyclerView;
    private ServiceAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_services);

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

        // Инициализация RecyclerView
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Создание списка услуг
        List<ServiceItem> serviceList = new ArrayList<>();
        serviceList.add(new ServiceItem("Чистка зубов", "Профессиональная чистка зубов с использованием современного оборудования.", "2000 руб."));
        serviceList.add(new ServiceItem("Лечение кариеса", "Лечение кариеса с применением современных технологий.", "3000 руб."));
        serviceList.add(new ServiceItem("Имплантация зубов", "Установка имплантатов высокого качества.", "25000 руб."));
        serviceList.add(new ServiceItem("Отбеливание зубов", "Профессиональное отбеливание зубов с гарантией результата.", "5000 руб."));

        // Установка адаптера
        adapter = new ServiceAdapter(serviceList, this);
        recyclerView.setAdapter(adapter);
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
            // Уже на странице "Услуги"
        } else if (id == R.id.nav_appointment) {
            Intent intent = new Intent(this, AppointmentActivity.class);
            startActivity(intent);
            finish(); // Закрываем текущую активность
        } else if (id == R.id.nav_view_appointments) {
            Intent intent = new Intent(this, ViewAppointmentsActivity.class);
            startActivity(intent);
            finish(); // Закрываем текущую активность
        }

        drawerLayout.closeDrawers();
        return true;
    }
}