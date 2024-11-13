package com.example.practika;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context) {
        super(context, "Appointments.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Appointments(id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone TEXT, service TEXT, date TEXT, time TEXT, specialist TEXT)");
        db.execSQL("CREATE TABLE Users(id INTEGER PRIMARY KEY AUTOINCREMENT, fullName TEXT, birthDate TEXT, gender TEXT, username TEXT, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Appointments");
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }

    public boolean insertAppointment(String name, String phone, String service, String date, String time, String specialist) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("phone", phone);
        contentValues.put("service", service);
        contentValues.put("date", date);
        contentValues.put("time", time);
        contentValues.put("specialist", specialist);
        long result = db.insert("Appointments", null, contentValues);
        return result != -1;
    }

    public List<Appointment> getAllAppointments() {
        List<Appointment> appointmentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Appointments", null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex("name"));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));
                @SuppressLint("Range") String service = cursor.getString(cursor.getColumnIndex("service"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex("time"));
                @SuppressLint("Range") String specialist = cursor.getString(cursor.getColumnIndex("specialist"));

                Appointment appointment = new Appointment(name, phone, service, date, time, specialist);
                appointmentList.add(appointment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return appointmentList;
    }

    public boolean insertUser(String fullName, String birthDate, String gender, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("fullName", fullName);
        contentValues.put("birthDate", birthDate);
        contentValues.put("gender", gender);
        contentValues.put("username", username);
        contentValues.put("password", hashPassword(password)); // Хеширование пароля
        long result = db.insert("Users", null, contentValues);
        return result != -1;
    }

    public User getUser(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE id = ?", new String[]{String.valueOf(id)});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String fullName = cursor.getString(cursor.getColumnIndex("fullName"));
            @SuppressLint("Range") String birthDate = cursor.getString(cursor.getColumnIndex("birthDate"));
            @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex("gender"));

            // Получение записи пользователя
            Appointment appointment = getAppointmentByUserId(id);

            return new User(fullName, birthDate, gender, appointment);
        }

        cursor.close();
        return null;
    }

    private Appointment getAppointmentByUserId(int userId) {
        // Логика получения записи пользователя
        return null;
    }

    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM Users WHERE username = ? AND password = ?", new String[]{username, hashPassword(password)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    private String hashPassword(String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(password.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(hash);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return password; // Возвращаем пароль в незашифрованном виде в случае ошибки
        }
    }

    private String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}