package com.example.practika;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class AppointmentActivity extends AppCompatActivity {

    TextInputEditText editName, editPhone;
    DatePicker datePicker;
    TimePicker timePicker;
    Spinner spinnerService, spinnerSpecialist;
    MaterialButton btnSubmit;
    DBHelper dbHelper;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment);

        // Инициализация виджетов
        editName = findViewById(R.id.edit_name);
        editPhone = findViewById(R.id.edit_phone);
        datePicker = findViewById(R.id.date_picker);
        timePicker = findViewById(R.id.time_picker);
        spinnerService = findViewById(R.id.spinner_service);
        spinnerSpecialist = findViewById(R.id.spinner_specialist);
        btnSubmit = findViewById(R.id.btn_submit);
        dbHelper = new DBHelper(this);

        // Заполнение Spinner данными
        ArrayAdapter<CharSequence> serviceAdapter = ArrayAdapter.createFromResource(this,
                R.array.services_array, android.R.layout.simple_spinner_item);
        serviceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService.setAdapter(serviceAdapter);

        ArrayAdapter<CharSequence> specialistAdapter = ArrayAdapter.createFromResource(this,
                R.array.specialists_array, android.R.layout.simple_spinner_item);
        specialistAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSpecialist.setAdapter(specialistAdapter);

        // Установка обработчика нажатия на кнопку
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitAppointment();
            }
        });
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
        boolean isInserted = dbHelper.insertAppointment(name, phone, service, date, time, specialist);

        if (isInserted) {
            Toast.makeText(this, "Запись успешно сохранена!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Ошибка при записи", Toast.LENGTH_SHORT).show();
        }
    }
}