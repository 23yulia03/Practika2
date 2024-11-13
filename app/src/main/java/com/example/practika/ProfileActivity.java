package com.example.practika;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ProfileActivity extends AppCompatActivity {

    private TextView textFullName, textBirthDate, textGender, textAppointment;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        textFullName = findViewById(R.id.text_full_name);
        textBirthDate = findViewById(R.id.text_birth_date);
        textGender = findViewById(R.id.text_gender);
        textAppointment = findViewById(R.id.text_appointment);
        dbHelper = new DBHelper(this);

        // Получение ID пользователя из Intent
        int userId = getIntent().getIntExtra("userId", -1);
        if (userId != -1) {
            // Получение данных пользователя и отображение их
            User user = dbHelper.getUser(userId);
            if (user != null) {
                textFullName.setText(user.getFullName());
                textBirthDate.setText(user.getBirthDate());
                textGender.setText(user.getGender());
                if (user.getAppointment() != null) {
                    textAppointment.setText("Запись на услугу: " + user.getAppointment().getService() + " на " + user.getAppointment().getDate() + " в " + user.getAppointment().getTime());
                } else {
                    textAppointment.setText("Нет записи на услугу");
                }
            } else {
                Toast.makeText(this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Ошибка: ID пользователя не найден", Toast.LENGTH_SHORT).show();
        }
    }
}