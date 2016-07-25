package com.example.prateek.moviesappv2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Prateek on 15-06-2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper{
//    private static MovieDbHelper ourInstance = new MovieDbHelper();
//
//    public static MovieDbHelper getInstance() {
//        return ourInstance;
//    }

//    private MovieDbHelper() {
//    }
    private static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "movie.db";

    public MovieDbHelper(Context context){
        super (context, DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME + "(" +

                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.COLUMN_MOVIE_IMG + " INTEGER NOT NULL," +
                MovieContract.MovieEntry.COLUMN_TITLE + " REAL NOT NULL ," +
                MovieContract.MovieEntry.COLUMN_DATE + " REAL NOT NULL," +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " REAL NOT NULL," +
                MovieContract.MovieEntry.COLUMN_RATINGS + " FLOAT NOT NULL" + ");";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + MovieContract.MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
