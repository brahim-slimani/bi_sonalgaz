package com.slimani.bi_sonalgaz.paramsSQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db_server extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "paramServer";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "configServer";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "ip_address";
    private static final String COLUMN_NAME2 = "port_number";


    public Db_server( Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (\n" +
                " " + COLUMN_ID + " INTEGER NOT NULL,\n" +
                " " + COLUMN_NAME + "  varchar(200) NOT NULL,\n" +
                " " + COLUMN_NAME2 + " varchar(200) NOT NULL\n" + ");";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }


    public boolean initServer(int id, String ip_address, String port_number){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, ip_address);
        contentValues.put(COLUMN_NAME2, port_number);

        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME, null, contentValues) != -1;
    }

    public boolean refreshServer(int id, String ip_address, String port_number){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, ip_address);
        contentValues.put(COLUMN_NAME2, port_number);

        SQLiteDatabase db = getWritableDatabase();
        return db.update(TABLE_NAME, contentValues, "id="+id,null) != -1;
    }



    public Cursor getParamServer(int id){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT ip_address, port_number FROM configServer WHERE id = '"+id+"' ";
        return db.rawQuery(sql, null);
    }

    public Cursor getServer(){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }
}
