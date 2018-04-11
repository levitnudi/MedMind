package com.reemind.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.reemind.models.MedData;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Levit Nudi on 2/04/18.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "medmind_db";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {

        // create medlist table
        db.execSQL(MedData.CREATE_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + MedData.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }

    public long insertMedication(String email, String drug, String description, int icon, String start_date,
                                 String end_date, String start_time, String frequency, String interval, String repeat,
                                 String repeatType, String active) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(MedData.COLUMN_ACCOUNT_EMAIL, email);
        values.put(MedData.COLUMN_DRUG_NAME, drug);
        values.put(MedData.COLUMN_DRUG_DESC, description);        
        values.put(MedData.COLUMN_ICON, icon);
        values.put(MedData.COLUMN_START_DATE, start_date);
        values.put(MedData.COLUMN_END_DATE, end_date);
        values.put(MedData.COLUMN_START_TIME, start_time);
        values.put(MedData.COLUMN_FREQUENCY, frequency);
        values.put(MedData.COLUMN_TIME_INTERVAL, interval);
        values.put(MedData.COLUMN_REPEAT, repeat);
        values.put(MedData.COLUMN_REPEAT_TYPE, repeatType);
        values.put(MedData.COLUMN_ACTIVE, active);
        
        // insert row
        long id = db.insert(MedData.TABLE_NAME, null, values);
        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public MedData getMedication(long id) {
        // get readable database as we are not inserting anything
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(MedData.TABLE_NAME,
                new String[]{MedData.COLUMN_ID, MedData.COLUMN_ACCOUNT_EMAIL, 
                        MedData.COLUMN_DRUG_NAME, MedData.COLUMN_DRUG_DESC, 
                        MedData.COLUMN_ICON, MedData.COLUMN_START_DATE,
                        MedData.COLUMN_END_DATE, MedData.COLUMN_START_TIME, MedData.COLUMN_FREQUENCY,
                        MedData.COLUMN_TIME_INTERVAL,MedData.COLUMN_REPEAT,
                        MedData.COLUMN_REPEAT_TYPE, MedData.COLUMN_ACTIVE,
                        MedData.COLUMN_TIMESTAMP},
                MedData.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        // prepare note object
        MedData note = new MedData(
                cursor.getInt(cursor.getColumnIndex(MedData.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(MedData.COLUMN_ACCOUNT_EMAIL)),
                cursor.getString(cursor.getColumnIndex(MedData.COLUMN_DRUG_NAME)),
                cursor.getString(cursor.getColumnIndex(MedData.COLUMN_DRUG_DESC)),
                cursor.getInt(cursor.getColumnIndex(MedData.COLUMN_ICON)),
                cursor.getString(cursor.getColumnIndex(MedData.COLUMN_START_DATE)),
                cursor.getString(cursor.getColumnIndex(MedData.COLUMN_END_DATE)),
                cursor.getString(cursor.getColumnIndex(MedData.COLUMN_START_TIME)),
                cursor.getString(cursor.getColumnIndex(MedData.COLUMN_FREQUENCY)),
                cursor.getString(cursor.getColumnIndex(MedData.COLUMN_TIME_INTERVAL)),
                cursor.getString(cursor.getColumnIndex(MedData.COLUMN_TIMESTAMP)),
                cursor.getString(cursor.getColumnIndex(MedData.COLUMN_REPEAT)),
                cursor.getString(cursor.getColumnIndex(MedData.COLUMN_REPEAT_TYPE)),
                cursor.getString(cursor.getColumnIndex(MedData.COLUMN_ACTIVE)));

        // close the db connection
        cursor.close();

        return note;
    }

    public List<MedData> getAllMedications() {
        List<MedData> medlist = new ArrayList<>();

        // Select All Query
        String selectQuery = "SELECT  * FROM " + MedData.TABLE_NAME + " ORDER BY " +
                MedData.COLUMN_TIMESTAMP + " DESC";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MedData medData = new MedData();
                medData.setId(cursor.getInt(cursor.getColumnIndex(MedData.COLUMN_ID)));
                medData.setColumnAccountEmail(cursor.getString(cursor.getColumnIndex(MedData.COLUMN_ACCOUNT_EMAIL)));
                medData.setColumnDrugName(cursor.getString(cursor.getColumnIndex(MedData.COLUMN_DRUG_NAME)));
                medData.setColumnDrugDesc(cursor.getString(cursor.getColumnIndex(MedData.COLUMN_DRUG_DESC)));
                medData.setColumnIcon(cursor.getInt(cursor.getColumnIndex(MedData.COLUMN_ICON)));
                medData.setColumnStartDate(cursor.getString(cursor.getColumnIndex(MedData.COLUMN_START_DATE)));                
                medData.setColumnEndDate(cursor.getString(cursor.getColumnIndex(MedData.COLUMN_END_DATE)));
                medData.setColumnStartTime(cursor.getString(cursor.getColumnIndex(MedData.COLUMN_START_TIME)));
                medData.setColumnFrequency(cursor.getString(cursor.getColumnIndex(MedData.COLUMN_FREQUENCY)));
                medData.setColumnTimeInterval(cursor.getString(cursor.getColumnIndex(MedData.COLUMN_TIME_INTERVAL)));
                medData.setColumnRepeat(cursor.getString(cursor.getColumnIndex(MedData.COLUMN_REPEAT)));
                medData.setColumnRepeatType(cursor.getString(cursor.getColumnIndex(MedData.COLUMN_REPEAT_TYPE)));
                medData.setColumnActive(cursor.getString(cursor.getColumnIndex(MedData.COLUMN_ACTIVE)));
                medData.setTimestamp(cursor.getString(cursor.getColumnIndex(MedData.COLUMN_TIMESTAMP)));

                medlist.add(medData);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return medlist list
        return medlist;
    }

    public int getmedlistCount() {
        String countQuery = "SELECT  * FROM " + MedData.TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int count = cursor.getCount();
        cursor.close();
        // return count
        return count;
    }

    public int updateMedication(MedData medData) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(MedData.COLUMN_ACCOUNT_EMAIL, medData.getColumnAccountEmail());
        values.put(MedData.COLUMN_DRUG_NAME, medData.getColumnDrugName());
        values.put(MedData.COLUMN_DRUG_DESC, medData.getColumnDrugDesc());
        values.put(MedData.COLUMN_ICON, medData.getColumnIcon());
        values.put(MedData.COLUMN_START_DATE, medData.getColumnStartDate());
        values.put(MedData.COLUMN_END_DATE, medData.getColumnEndDate());
        values.put(MedData.COLUMN_START_TIME, medData.getColumnStartTime());
        values.put(MedData.COLUMN_FREQUENCY, medData.getColumnFrequency());
        values.put(MedData.COLUMN_TIME_INTERVAL, medData.getColumnTimeInterval());
        values.put(MedData.COLUMN_REPEAT, medData.getColumnRepeat());
        values.put(MedData.COLUMN_REPEAT_TYPE, medData.getColumnRepeatType());
        values.put(MedData.COLUMN_ACTIVE, medData.getColumnActive());

        // updating row
        return db.update(MedData.TABLE_NAME, values, MedData.COLUMN_ID + " = ?",
                new String[]{String.valueOf(medData.getId())});
    }

    public void deleteMedication(MedData medData) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(MedData.TABLE_NAME, MedData.COLUMN_ID + " = ?",
                new String[]{String.valueOf(medData.getId())});
        db.close();
    }
}
