package com.example.practika;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.textfield.TextInputEditText;

public class AppointmentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private TextInputEditText editName, editPhone;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Spinner spinnerService, spinnerSpecialist;
    private MaterialButton btnSubmit;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

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

        // Инициализация виджетов
        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);
        spinnerService = findViewById(R.id.spinner_service);
        spinnerSpecialist = findViewById(R.id.spinner_specialist);
        btnSubmit = findViewById(R.id.btn_submit);
        dbHelper = new DBHelper(this);

        // Установка обработчика нажатия на кнопку
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAppointment();
            }
        });
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
            // Уже на странице "Записаться на прием"
        } else if (id == R.id.nav_view_appointments) {
            startActivity(new Intent(this, ViewAppointmentsActivity.class));
        }

        drawerLayout.closeDrawers();
        return true;
    }

    private void submitAppointment() {
        String name = editName.getText().toString();
        String phone = editPhone.getText().toString();
        String service = spinnerService.getSelectedItem().toString();
        String specialist = spinnerSpecialist.getSelectedItem().toString();

        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth() + 1;
        int year = datePicker.getYear();
        int hour = timePicker.getHour();
        int minute = timePicker.getMinute();

        // Валидация данных
        if (name.isEmpty()) {
            editName.setError("Введите ваше имя");
        } else if (phone.isEmpty()) {
            editPhone.setError("Введите номер телефона");
        } else if (!phone.matches("\\d{11}")) {
            editPhone.setError("Введите корректный номер телефона (11 цифр)");
        } else {
            // Подтверждение записи
            new AlertDialog.Builder(AppointmentActivity.this)
                    .setTitle("Подтверждение записи")
                    .setMessage("Вы уверены, что хотите записаться на " + service + " к " + specialist + " на дату " + day + "/" + month + "/" + year + " в " + hour + ":" + minute + "?")
                    .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            saveAppointment(name, phone, service, day + "/" + month + "/" + year, hour + ":" + minute, specialist);
                        }
                    })
                    .setNegativeButton("Отмена", null)
                    .show();
        }
    }

    private void saveAppointment(String name, String phone, String service, String date, String time, String specialist) {
        // Получаем userId из Intent
        int userId = getIntent().getIntExtra("userId", -1);

        if (userId == -1) {
            // Если userId не передан, используем фиктивное значение для тестирования
            userId = 1; // Замените на реальный userId после тестирования
            Toast.makeText(this, "Ошибка: ID пользователя не найден. Используем фиктивное значение.", Toast.LENGTH_SHORT).show();
        }

        boolean isInserted = dbHelper.insertAppointment(name, phone, service, date, time, specialist, userId);

        if (isInserted) {
            Toast.makeText(this, "Запись успешно сохранена!", Toast.LENGTH_SHORT).show();

            // Перенаправляем пользователя на страницу "Просмотреть запись"
            Intent intent = new Intent(AppointmentActivity.this, ViewAppointmentsActivity.class);
            intent.putExtra("userId", userId); // Передаем userId
            startActivity(intent);
            finish(); // Закрываем текущую активность
        } else {
            Toast.makeText(this, "Ошибка при записи", Toast.LENGTH_SHORT).show();
        }
    }
}