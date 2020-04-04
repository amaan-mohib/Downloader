package com.example.downloader;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //Version
    private static final int DATABASE_VERSION=3;
    //Name
    private static final String DATABASE_NAME="history_db";
    public DatabaseHelper(Context context){
        super(context, DATABASE_NAME,null,DATABASE_VERSION);
    }

    //Creating Table
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(Database.CREATE_TABLE);
    }
    //Upgrade database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        //Drop older table
        db.execSQL("DROP TABLE IF EXISTS "+Database.TABLE_NAME);
        //Create table again
        onCreate(db);
    }
    public long insertDatabase(String song,String artist){
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and 'time' will be inserted automatically.
        // no need to add them
        values.put(Database.COLUMN_SONG, song);
        values.put(Database.COLUMN_ARTIST,artist);
        // insert row
        long id = db.insert(Database.TABLE_NAME, null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public Database getDatabase(long id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor=db.query(Database.TABLE_NAME,
                new String[]{Database.COLUMN_ID,Database.COLUMN_SONG,Database.COLUMN_ARTIST,Database.COLUMN_TIMESTAMP},
                Database.COLUMN_ID + "=?",
                new String[]{String.valueOf(id)},null,null,null,null);
        if(cursor!=null)
            cursor.moveToFirst();
        Database database=new Database(
                cursor.getInt(cursor.getColumnIndex(Database.COLUMN_ID)),
                cursor.getString(cursor.getColumnIndex(Database.COLUMN_SONG)),
                cursor.getString(cursor.getColumnIndex(Database.COLUMN_ARTIST)),
                cursor.getString(cursor.getColumnIndex(Database.COLUMN_TIMESTAMP)));
        cursor.close();
        return database;
    }
    public List<Database> getAllDatabase(){
        List<Database> history=new ArrayList<>();
        //Select all query
        String selectQuery="SELECT * FROM "+Database.TABLE_NAME+" ORDER BY "+Database.COLUMN_TIMESTAMP+" DESC";
        SQLiteDatabase db=this.getWritableDatabase();
        Cursor cursor=db.rawQuery(selectQuery,null);
        //looping through columns and adding to list
        if(cursor.moveToFirst()){
            do{
                Database database=new Database();
                database.setId(cursor.getInt(cursor.getColumnIndex(Database.COLUMN_ID)));
                database.setSong(cursor.getString(cursor.getColumnIndex(Database.COLUMN_SONG)));
                database.setArtist(cursor.getString(cursor.getColumnIndex(Database.COLUMN_ARTIST)));
                database.setTime(cursor.getString(cursor.getColumnIndex(Database.COLUMN_TIMESTAMP)));

                history.add(database);
            }while (cursor.moveToNext());
        }
        //Close connection
        db.close();
        //return list
        return history;
    }
    public int getDatabaseCount(){
        String countQuery="SELECT * FROM "+Database.TABLE_NAME;
        SQLiteDatabase db=this.getReadableDatabase();
        Cursor cursor=db.rawQuery(countQuery,null);
        int count=cursor.getCount();
        cursor.close();
        return count;
    }
    public int updateDatabase(Database database){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(Database.COLUMN_SONG,database.getSong());
        contentValues.put(Database.COLUMN_ARTIST,database.getArtist());
        //Updating
        return db.update(Database.TABLE_NAME,contentValues,Database.COLUMN_ID+" = ?",new String[]{String.valueOf(database.getId())});
    }
    public void deleteContent(Database database){
        SQLiteDatabase db=this.getWritableDatabase();
        db.delete(Database.TABLE_NAME,Database.COLUMN_ID+" = ?",new String[]{String.valueOf(database.getId())});
        db.close();
    }
    public void deleteAll(Database database){
        SQLiteDatabase db=this.getWritableDatabase();
        db.execSQL("DELETE FROM "+Database.TABLE_NAME);
        db.close();
    }
}
