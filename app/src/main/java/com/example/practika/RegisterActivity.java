package com.example.practika;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {

    private EditText editFullName, editBirthDate, editGender, editUsername, editPassword;
    private Button btnRegister;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        editFullName = findViewById(R.id.edit_full_name);
        editBirthDate = findViewById(R.id.edit_birth_date);
        editGender = findViewById(R.id.edit_gender);
        editUsername = findViewById(R.id.edit_username);
        editPassword = findViewById(R.id.edit_password);
        btnRegister = findViewById(R.id.btn_register);
        dbHelper = new DBHelper(this);

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fullName = editFullName.getText().toString().trim();
                String birthDate = editBirthDate.getText().toString().trim();
                String gender = editGender.getText().toString().trim();
                String username = editUsername.getText().toString().trim();
                String password = editPassword.getText().toString().trim();

                if (fullName.isEmpty() || birthDate.isEmpty() || gender.isEmpty() || username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "Заполните все поля", Toast.LENGTH_SHORT).show();
                } else {
                    boolean isInserted = dbHelper.insertUser(fullName, birthDate, gender, username, password);
                    if (isInserted) {
                        Toast.makeText(RegisterActivity.this, "Регистрация успешна", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Ошибка при регистрации", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}