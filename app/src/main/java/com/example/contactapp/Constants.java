package com.example.contactapp;

public class Constants
{
    public static final String DATABASE_NAME = "CONTACT_DB02";
    public static final int DATABASE_VERSION = 1;
    public static final String TABLE_NAME = "CONTACT_TABLE";
    public static final String TABLE_NAME_2 = "CONTACT_TABLE_2";
    public static final String C_ID = "ID";
    public static final String C_IMAGE = "IMAGE";
    public static final String C_FNAME = "FNAME";
    public static final String C_LNAME = "LNAME";
    public static final String C_CNAME = "CNAME";
    public static final String C_PNUM = "P_NUM";
    public static final String C_EMAIL = "EMAIL";
    public static final String C_ADDED_TIME = "ADDED_TIME";
    public static final String C_UPDATED_TIME = "UPDATED_TIME";

    public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_IMAGE + " TEXT, "
            + C_FNAME + " TEXT, "
            + C_LNAME + " TEXT, "
            + C_CNAME + " TEXT, "
            + C_PNUM + " TEXT, "
            + C_EMAIL + " TEXT, "
            + C_ADDED_TIME + " TEXT, "
            + C_UPDATED_TIME + " TEXT "
            + " );";

    public static final String CREATE_TABLE_2 = "CREATE TABLE " + TABLE_NAME_2 + "( "
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_IMAGE + " TEXT, "
            + C_FNAME + " TEXT, "
            + C_LNAME + " TEXT, "
            + C_CNAME + " TEXT, "
            + C_PNUM + " TEXT, "
            + C_EMAIL + " TEXT, "
            + C_ADDED_TIME + " TEXT, "
            + C_UPDATED_TIME + " TEXT "
            + " );";
}
