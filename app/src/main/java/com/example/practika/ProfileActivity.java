package com.example.practika;

import android.os.Bundle;
import android.widget.TextView;

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

        // Получение данных пользователя и отображение их
        User user = dbHelper.getUser(1); // Здесь нужно передать реальный ID пользователя
        if (user != null) {
            textFullName.setText(user.getFullName());
            textBirthDate.setText(user.getBirthDate());
            textGender.setText(user.getGender());
            if (user.getAppointment() != null) {
                textAppointment.setText("Запись на услугу: " + user.getAppointment().getService() + " на " + user.getAppointment().getDate() + " в " + user.getAppointment().getTime());
            } else {
                textAppointment.setText("Нет записи на услугу");
            }
        }
    }
}