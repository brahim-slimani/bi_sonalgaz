package com.slimani.bi_sonalgaz.param;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Db_manager extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "configDB";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "param";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "cube_olap";


    public Db_manager(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "CREATE TABLE " + TABLE_NAME + " (\n" +
                " " + COLUMN_ID + " INTEGER NOT NULL,\n" +
                " " + COLUMN_NAME + " varchar(200) NOT NULL\n" + ");";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}


    public boolean initParam(int id, String cube_olap){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, cube_olap);

        SQLiteDatabase db = getWritableDatabase();
        return db.insert(TABLE_NAME, null, contentValues) != -1;
    }

    public boolean refresh(int id, String cube_olap){
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ID, id);
        contentValues.put(COLUMN_NAME, cube_olap);

        SQLiteDatabase db = getWritableDatabase();
        return db.update(TABLE_NAME, contentValues, "id="+id,null) != -1;
    }



    public Cursor getCube(int id){
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT cube_olap FROM param WHERE id = '"+id+"' ";
        return db.rawQuery(sql, null);
    }

    public Cursor getAll(){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
    }


}
