package com.example.prateek.moviesappv2.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Prateek on 15-06-2016.
 */
public class MovieContract {


    public static final String CONTENT_AUTHORITY = "com.example.prateek.moviesappv2";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);



    public static final class MovieEntry implements BaseColumns{
        public static final String TABLE_NAME = "movie12";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(TABLE_NAME).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        TABLE_NAME;

        //public static final String TABLE_NAME = "moviedetail";
        public static final String _ID = "_id";
        public static final String COLUMN_MOVIE_IMG = "poster_path";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATINGS = "rating";

        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }
}
