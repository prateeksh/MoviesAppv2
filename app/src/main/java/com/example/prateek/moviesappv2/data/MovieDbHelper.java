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

    public static final String DATABASE_NAME = "movie.db";
//    private MovieDbHelper() {
//    }
    private static final int DATABASE_VERSION = 3;

    public MovieDbHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase){
        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MovieContract.MovieEntry.TABLE_NAME + "(" +

                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL," +
                MovieContract.MovieEntry.COLUMN_MOVIE_IMG + " INTEGER NOT NULL," +
                MovieContract.MovieEntry.COLUMN_TITLE + " REAL NOT NULL ," +
                MovieContract.MovieEntry.COLUMN_DATE + " REAL NOT NULL," +
                MovieContract.MovieEntry.COLUMN_OVERVIEW + " REAL NOT NULL," +
                MovieContract.MovieEntry.COLUMN_RATINGS + " FLOAT NOT NULL," +
                "UNIQUE (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE);";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " +
                MovieContract.ReviewEntry.TABLE_NAME +" ( " +
                MovieContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.ReviewEntry.COLUMN_REVIEW_ID + " INTEGER NOT NULL, " +

                MovieContract.ReviewEntry.COLUMN_MOVIE_AUTHOR + " TEXT NOT NULL, " +
                MovieContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                MovieContract.ReviewEntry.COLUMN_URL + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + MovieContract.ReviewEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ")); ";
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " + MovieContract.TrailerEntry.TABLE_NAME + " ( "+
                MovieContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.TrailerEntry.COLUMN_TRAILER_ID + " INTEGER NOT NULL, "+

                MovieContract.TrailerEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                MovieContract.TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                " FOREIGN KEY (" + MovieContract.TrailerEntry.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ")); ";
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);

        /*final String SQL_CREATE_FAVORITE_MOVIE_TABLE = "CREATE TABLE " + MovieContract.FavoriteMovies.TABLE_NAME + " ( "+
                MovieContract.FavoriteMovies._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.FavoriteMovies.COLUMN_MOVIE_ID + " INTEGER NOT NULL, "+
                MovieContract.FavoriteMovies.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                //FavoriteMovies.COLUMN_ADULT + " BLOB NULL, " +
                MovieContract.FavoriteMovies.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovies.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
               // FavoriteMovies.COLUMN_GENRE_IDS + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovies.COLUMN_ORIGINAL_TITLE + " TEXT NULL, " +
                //FavoriteMovies.COLUMN_ORIGINAL_LANGUAGE + " TEXT NULL, " +
                MovieContract.FavoriteMovies.COLUMN_TITLE + " TEXT NOT NULL, " +
                //FavoriteMovies.COLUMN_BACKDROP_PATH + " TEXT NOT NULL, " +
                MovieContract.FavoriteMovies.COLUMN_POPULARITY + " REAL NULL, " +
                //FavoriteMovies.COLUMN_VOTE_COUNT + " REAL NULL, " +
               // FavoriteMovies.COLUMN_VIDEO + " BLOB NULL, " +
                MovieContract.FavoriteMovies.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +

                " FOREIGN KEY (" + MovieContract.FavoriteMovies.COLUMN_MOVIE_ID + ") REFERENCES " +
                MovieContract.MovieEntry.TABLE_NAME + " (" + MovieContract.MovieEntry.COLUMN_MOVIE_ID + ")); ";
        sqLiteDatabase.execSQL(SQL_CREATE_FAVORITE_MOVIE_TABLE);*/
    }

//    public void addFavorite(String favorite, SQLiteDatabase db){
//        ContentValues contentValues = new ContentValues();
//        contentValues.put(MovieContract.MovieEntry.COLUMN_FAVORITE, 1);
//
//        String selection = MovieContract.MovieEntry._ID + " LIKE ?";
//        String[] selectionArgs = {String.valueOf(favorite)};
//        db.update(MovieContract.MovieEntry.TABLE_NAME, contentValues, selection, selectionArgs);
//    }
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion){
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + MovieContract.MovieEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + MovieContract.ReviewEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS" + MovieContract.TrailerEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
