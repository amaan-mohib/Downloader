package com.example.downloader;

public class Database {
    public static final String TABLE_NAME="history";

    public static final String COLUMN_ID="id";
    public static final String COLUMN_SONG="song";
    public static final String COLUMN_ARTIST="artist";
    public static final String COLUMN_TIMESTAMP="time";

    private int id;
    private String song, artist,time;

    //Create table SQL Query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_SONG + " TEXT,"
                    + COLUMN_ARTIST + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";
    public Database(){

    }
    public Database(int id, String song,String artist,String time){
        this.id=id;
        this.song=song;
        this.artist=artist;
        this.time=time;
    }
    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getSong(){
        return song;
    }
    public void setSong(String song){
        this.song=song;
    }
    public String getArtist(){
        return artist;
    }
    public void setArtist(String artist){
        this.artist=artist;
    }
    public String getTime(){
        return time;
    }
    public void setTime(String time){
        this.time=time;
    }
}
