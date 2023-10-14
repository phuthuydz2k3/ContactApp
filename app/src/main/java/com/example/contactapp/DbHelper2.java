package com.example.contactapp;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

//class for database helper
public class DbHelper2 extends SQLiteOpenHelper {

    public DbHelper2(@Nullable Context context) {
        super(context, Constants.DATABASE_NAME, null, Constants.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create table on database
        db.execSQL(Constants.CREATE_TABLE_2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //upgrade table if any structure change in db

        // drop table if exists
        db.execSQL("DROP TABLE IF EXISTS " + Constants.TABLE_NAME_2);

        // create table again
        onCreate(db);
    }

    // Insert Function to insert data in database
    public long insertContact(String image, String fName, String lName, String cName,
                              String pNum, String email, String addedTime, String updatedTime)
    {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.C_IMAGE, image);
        contentValues.put(Constants.C_FNAME, fName);
        contentValues.put(Constants.C_LNAME, lName);
        contentValues.put(Constants.C_CNAME, cName);
        contentValues.put(Constants.C_PNUM, pNum);
        contentValues.put(Constants.C_EMAIL, email);
        contentValues.put(Constants.C_ADDED_TIME, addedTime);
        contentValues.put(Constants.C_UPDATED_TIME, updatedTime);

        long id = db.insert(Constants.TABLE_NAME_2, null, contentValues);

        db.close();

        return id;
    }

    // Update Function to update data in database
    public void updateContact(String id,String image, String fName, String lName, String cName,
                              String pNum, String email, String addedTime, String updatedTime)
    {

        //get writable database to write data on db
        SQLiteDatabase db = this.getWritableDatabase();

        // create ContentValue class object to save data
        ContentValues contentValues = new ContentValues();

        contentValues.put(Constants.C_IMAGE, image);
        contentValues.put(Constants.C_FNAME, fName);
        contentValues.put(Constants.C_LNAME, lName);
        contentValues.put(Constants.C_CNAME, cName);
        contentValues.put(Constants.C_PNUM, pNum);
        contentValues.put(Constants.C_EMAIL, email);
        contentValues.put(Constants.C_ADDED_TIME, addedTime);
        contentValues.put(Constants.C_UPDATED_TIME, updatedTime);

        //update data in row, It will return id of record
        db.update(Constants.TABLE_NAME_2,contentValues,Constants.C_ID+" =? ",new String[]{id} );

        // close db
        db.close();

    }


    // get data
    public ArrayList<ModelContact> getAllData(){
        //create arrayList
        ArrayList<ModelContact> arrayList = new ArrayList<>();
        //sql command query
        String selectQuery = "SELECT * FROM " + Constants.TABLE_NAME_2;

        //get readable db
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        // looping through all record and add to list
        if (cursor.moveToFirst()){
            do {
                ModelContact modelContact = new ModelContact(
                        // only id is integer type
                        "" + cursor.getInt(cursor.getColumnIndexOrThrow(Constants.C_ID)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_FNAME)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_LNAME)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_CNAME)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_IMAGE)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_PNUM)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_EMAIL)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_ADDED_TIME)),
                        "" + cursor.getString(cursor.getColumnIndexOrThrow(Constants.C_UPDATED_TIME))
                );
                arrayList.add(modelContact);
            }while (cursor.moveToNext());
        }
        db.close();
        return arrayList;
    }
}
