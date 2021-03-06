package com.kktran.cis2323_week14_lab;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DB_Name = "TASKS";
    private static final String DB_Column = "TASKNAME";
    private static final int DB_VER = 1;

    public DBHelper(Context context) {
        super(context,DB_Name,null, DB_VER);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String q = String.format("CREATE TABLE %s " +
                 "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
                 "%s TEXT NOT NULL);", DB_Name, DB_Column);
        db.execSQL(q);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String q = String.format("DELETE TABLE IF EXIST %s:", DB_Name);
        db.execSQL(q);
        onCreate(db);
    }

    public void insertTask(String task) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DB_Column,task);
        db.insertWithOnConflict(DB_Name, null,values, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public ArrayList<String> getTaskList (){
        ArrayList<String> taskList = new ArrayList<>();
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(DB_Name, new String[]{DB_Column}, null, null, null, null, null);

        while(cursor.moveToNext()){
            int index  = cursor.getColumnIndex(DB_Column);
            taskList.add(cursor.getString(index));
        }
        cursor.close();
        db.close();
        return taskList;
    }

    public void deleteTask(String task){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_Name, DB_Column + " =d ?", new String[] {task});
        db.close();
    }
}

