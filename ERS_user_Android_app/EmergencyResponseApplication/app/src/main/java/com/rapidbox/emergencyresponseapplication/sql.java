package com.rapidbox.emergencyresponseapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
class sql extends SQLiteOpenHelper {

    static String db="fyp";
    static String table1="routes";
    static String table2="notifications";
    static String col1="id";
    static String col2="email";
    static String col3="latitude";
    static String col4="longitude";
    String q1 = "drop table if exists ";


    public sql(Context context) {
        super(context, db, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table "+table1+" (id INTEGER PRIMARY KEY AUTOINCREMENT,email TEXT,latitude TEXT,longitude TEXT)");
        sqLiteDatabase.execSQL("create table "+table2+" (id integer primary key AUTOINCREMENT,nid text,email text)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(q1+table1);
        sqLiteDatabase.execSQL(q1+table2);
        onCreate(sqLiteDatabase);
    }

    public boolean insert(String email,String lat,String lng)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("email",email);
        contentValues.put("latitude",lat);
        contentValues.put("longitude",lng);

        long check= db.insert(table1,null,contentValues);
        if (check==-1)
            return false;
        else
            return  true;
    }

    public boolean insert(String nid,String email)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put("email",email);
        contentValues.put("nid",nid);

        long check= db.insert(table2,null,contentValues);
        if (check==-1)
            return false;
        else
            return  true;
    }


    public Cursor fetch(String query)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor result=db.rawQuery(query,null);
        return  result;
    }


    public void exec(String query)
    {
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL(query);
    }





}