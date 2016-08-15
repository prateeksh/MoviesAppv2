package com.example.prateek.moviesappv2.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Prateek on 15-06-2016.
 */
public class MovieDbHelper extends SQLiteOpenHelper{


    public static final String DATABASE_NAME = "popmovie.db";

    private static final int DATABASE_VERSION = 13;

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME + "(" +

                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_MOVIE_IMG + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_TITLE + " TEXT NOT NULL , " +
                MovieContract.MovieEntry.COLUMN_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_USER_RATING + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieContract.MovieEntry.COLUMN_IS_POPULAR + " INTEGER DEFAULT 0, " +
                MovieContract.MovieEntry.COLUMN_IS_TOP_RATED + " INTEGER DEFAULT 0, " +
                MovieContract.MovieEntry.COLUMN_IS_FAVORITE + " INTEGER DEFAULT 0, " +
                "UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ", " +
                MovieContract.MovieEntry.COLUMN_IS_POPULAR + ", " +
                MovieContract.MovieEntry.COLUMN_IS_FAVORITE + ", " +
                MovieContract.MovieEntry.COLUMN_IS_TOP_RATED + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MovieContract.MovieTrailerEntry.TABLE_NAME + " (" +
                MovieContract.MovieTrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieTrailerEntry.COLUMN_ID + " TEXT, " +
                MovieContract.MovieTrailerEntry.COLUMN_KEY + " TEXT, " +
                " FOREIGN KEY (" + MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + "), " +
                " UNIQUE (" + MovieContract.MovieTrailerEntry.COLUMN_ID + ", " +
                MovieContract.MovieTrailerEntry.COLUMN_KEY + ") ON CONFLICT REPLACE);";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + MovieContract.MovieReviewEntry.TABLE_NAME + " (" +
                MovieContract.MovieReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieReviewEntry.COLUMN_ID + " TEXT, " +
                MovieContract.MovieReviewEntry.COLUMN_MOVIE_AUTHOR + " TEXT, " +
                MovieContract.MovieReviewEntry.COLUMN_CONTENT + " TEXT, " +
                MovieContract.MovieReviewEntry.COLUMN_URL + " TEXT, " +
                " FOREIGN KEY (" + MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + "), " +
                " UNIQUE (" + MovieContract.MovieReviewEntry.COLUMN_ID + ") ON CONFLICT REPLACE);";

        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + MovieContract.MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + MovieContract.MovieReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + MovieContract.MovieTrailerEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
