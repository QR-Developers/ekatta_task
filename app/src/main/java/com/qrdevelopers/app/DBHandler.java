package com.qrdevelopers.app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;

import java.util.ArrayList;

public class DBHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "ekattadb";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "users";

    public DBHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        Log.d("DBHANDLER", "DBHANDLER onCreate()");

        String query = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, mobile TEXT)";
        db.execSQL(query);
    }


    public void addUser(String name, String mobile) {
        Log.d("DBHANDLER", "DBHANDLER addUser()");
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("mobile", mobile);

        db.insert(TABLE_NAME, null, values);

        db.close();
    }


    public ArrayList<User> getUserList() {
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<User> userList = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME+" ORDER BY id", null);

        Log.d("DBRetrive","Cursor: "+cursor.getCount());


        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                String mobile = cursor.getString(cursor.getColumnIndexOrThrow("mobile"));
//                String name = cursor.getString(1).toString();
//                String mobile = cursor.getString(2).toString();
                Log.d("DBRetrive","Name: "+name);
                Log.d("DBRetrive","Mobile: "+mobile);
                userList.add(new User(name, mobile));
                cursor.moveToNext();
            }
        }
//        cursor.close();
        db.close();
        return userList;
    }

    public void deleteData() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.d("DBHANDLER", "DBHANDLER onUpgrade()");
    }
}
