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

    private static final String DATABASE_NAME = "Appointments.db";
    private static final int DATABASE_VERSION = 1;

    // Таблица для записей
    private static final String TABLE_APPOINTMENTS = "Appointments";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_PHONE = "phone";
    private static final String COLUMN_SERVICE = "service";
    private static final String COLUMN_DATE = "date";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_SPECIALIST = "specialist";
    private static final String COLUMN_USER_ID = "userId";

    // Таблица для пользователей
    private static final String TABLE_USERS = "Users";
    private static final String COLUMN_USER_ID_USERS = "id";
    private static final String COLUMN_FULL_NAME = "fullName";
    private static final String COLUMN_BIRTH_DATE = "birthDate";
    private static final String COLUMN_GENDER = "gender";
    private static final String COLUMN_USERNAME = "username";
    private static final String COLUMN_PASSWORD = "password";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Создание таблицы записей
        String createAppointmentsTable = "CREATE TABLE " + TABLE_APPOINTMENTS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_NAME + " TEXT, "
                + COLUMN_PHONE + " TEXT, "
                + COLUMN_SERVICE + " TEXT, "
                + COLUMN_DATE + " TEXT, "
                + COLUMN_TIME + " TEXT, "
                + COLUMN_SPECIALIST + " TEXT, "
                + COLUMN_USER_ID + " INTEGER)";
        db.execSQL(createAppointmentsTable);

        // Создание таблицы пользователей
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + "("
                + COLUMN_USER_ID_USERS + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_FULL_NAME + " TEXT, "
                + COLUMN_BIRTH_DATE + " TEXT, "
                + COLUMN_GENDER + " TEXT, "
                + COLUMN_USERNAME + " TEXT, "
                + COLUMN_PASSWORD + " TEXT)";
        db.execSQL(createUsersTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_APPOINTMENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }

    // Метод для вставки записи
    public boolean insertAppointment(String name, String phone, String service, String date, String time, String specialist, int userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME, name);
        contentValues.put(COLUMN_PHONE, phone);
        contentValues.put(COLUMN_SERVICE, service);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_TIME, time);
        contentValues.put(COLUMN_SPECIALIST, specialist);
        contentValues.put(COLUMN_USER_ID, userId);

        long result = db.insert(TABLE_APPOINTMENTS, null, contentValues);
        return result != -1; // Возвращаем true, если запись успешно добавлена
    }

    // Метод для получения всех записей
    public List<Appointment> getAllAppointments() {
        List<Appointment> appointmentList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_APPOINTMENTS, null);

        if (cursor.moveToFirst()) {
            do {
                @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
                @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));
                @SuppressLint("Range") String service = cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
                @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
                @SuppressLint("Range") String specialist = cursor.getString(cursor.getColumnIndex(COLUMN_SPECIALIST));

                Appointment appointment = new Appointment(name, phone, service, date, time, specialist);
                appointmentList.add(appointment);
            } while (cursor.moveToNext());
        }

        cursor.close();
        return appointmentList;
    }

    // Метод для удаления записи
    public boolean deleteAppointment(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_APPOINTMENTS, COLUMN_ID + " = ?", new String[]{String.valueOf(id)});
        return result > 0;
    }

    // Метод для вставки пользователя
    public boolean insertUser(String fullName, String birthDate, String gender, String username, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_FULL_NAME, fullName);
        contentValues.put(COLUMN_BIRTH_DATE, birthDate);
        contentValues.put(COLUMN_GENDER, gender);
        contentValues.put(COLUMN_USERNAME, username);
        contentValues.put(COLUMN_PASSWORD, hashPassword(password)); // Хеширование пароля
        long result = db.insert(TABLE_USERS, null, contentValues);
        return result != -1;
    }

    // Метод для получения пользователя по ID
    public User getUser(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USER_ID_USERS + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String fullName = cursor.getString(cursor.getColumnIndex(COLUMN_FULL_NAME));
            @SuppressLint("Range") String birthDate = cursor.getString(cursor.getColumnIndex(COLUMN_BIRTH_DATE));
            @SuppressLint("Range") String gender = cursor.getString(cursor.getColumnIndex(COLUMN_GENDER));

            // Получение записи пользователя
            Appointment appointment = getAppointmentByUserId(userId);

            cursor.close();
            return new User(fullName, birthDate, gender, appointment);
        }

        cursor.close();
        return null;
    }

    // Метод для получения записи по userId
    private Appointment getAppointmentByUserId(int userId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_APPOINTMENTS + " WHERE " + COLUMN_USER_ID + " = ?", new String[]{String.valueOf(userId)});

        if (cursor.moveToFirst()) {
            @SuppressLint("Range") String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
            @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex(COLUMN_PHONE));
            @SuppressLint("Range") String service = cursor.getString(cursor.getColumnIndex(COLUMN_SERVICE));
            @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex(COLUMN_DATE));
            @SuppressLint("Range") String time = cursor.getString(cursor.getColumnIndex(COLUMN_TIME));
            @SuppressLint("Range") String specialist = cursor.getString(cursor.getColumnIndex(COLUMN_SPECIALIST));

            cursor.close();
            return new Appointment(name, phone, service, date, time, specialist);
        }

        cursor.close();
        return null;
    }

    // Метод для проверки пользователя
    public boolean checkUser(String username, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COLUMN_USERNAME + " = ? AND " + COLUMN_PASSWORD + " = ?", new String[]{username, hashPassword(password)});
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    // Метод для хеширования пароля
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