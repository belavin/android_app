package com.android.gpstest;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

import java.util.Calendar;


public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "value.db";

    public interface DatabaseHandler<T> {
        void onComplete(boolean success, T result);
    }

    private static abstract class DatabaseAsyncTask<T> extends AsyncTask<Void, Void, T> {

        private DatabaseHandler<T> handler;
        private RuntimeException error;

        public DatabaseAsyncTask(DatabaseHandler<T> handler) {
            this.handler = handler;
        }

        @Override
        protected T doInBackground(Void... params) {
            try {
                return executeMethod();
            } catch (RuntimeException error) {
                this.error = error;
                return null;
            }
        }

        protected abstract T executeMethod();

        @Override
        protected void onPostExecute(T result) {
            handler.onComplete(error == null, result);
        }
    }

    private SQLiteDatabase db;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE value (" +
                "time INTEGER PRIMARY KEY AUTOINCREMENT," +
                "value1 REAL," +
                "value2 REAL," +
                "value3 REAL)" );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS value;");
        onCreate(db);
    }

    public void insertValues(String value) {
        ContentValues values = new ContentValues();
        values.put("time", String.valueOf(Calendar.getInstance().getTime()));
        values.put("value1", "");
        values.put("value2", "");
        values.put("value3", "");


        db.insertOrThrow("value", null, values);
    }

    public void insertValuesAsync(final String value, DatabaseHandler<Void> handler) {
        new DatabaseAsyncTask<Void>(handler) {
            @Override
            protected Void executeMethod() {
                insertValues(value);
                return null;
            }
        }.execute();
    }

    public Values selectValues() {
        Values value = new Values();

        Cursor cursor = db.rawQuery("SELECT * FROM value ORDER BY id LIMIT 1", null);
        try {
            if (cursor.getCount() > 0) {

                cursor.moveToFirst();


                value.setTime(String.valueOf(Calendar.getInstance().getTime()));

                value.setValues1(value.getValues1());
                value.setValues2(value.getValues2());
                value.setValues2(value.getValues2());



            } else {
                return null;
            }
        } finally {
            cursor.close();
        }

        return value;
    }

    public void selectValuesAsync(DatabaseHandler<Values> handler) {
        new DatabaseAsyncTask<Values>(handler) {
            @Override
            protected Values executeMethod() {
                return selectValues();
            }
        }.execute();
    }

    public void deleteValues(long time) {
        if (db.delete("value", "time = ?", new String[] { String.valueOf(time) }) != 1) {
            throw new SQLException();
        }
    }

    public void deleteValuesAsync(final long time, DatabaseHandler<Void> handler) {
        new DatabaseAsyncTask<Void>(handler) {
            @Override
            protected Void executeMethod() {
                deleteValues(time);
                return null;
            }
        }.execute();
    }
}
