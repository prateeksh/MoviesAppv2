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

    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "review";
    public static final String PATH_FAVORITE_MOVIES = "favorite_movies";

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
        //public static final String _ID = "_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_IMG = "poster_path";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RATINGS = "rating";
        public static final String COLUMN_FAVORITE = "favorite";

        // Uri for the specific Movie ID in the movie table
        public static Uri buildMovieIDUri(int MovieID){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(MovieID)).build();
        }

        // Uri for getting the movie id
        public static String getMovieID(Uri uri){
            return uri.getPathSegments().get(1);
        }

        // Uri for getting the movie id
        public static int getIntegerMovieID(Uri uri){
            return Integer.parseInt(uri.getPathSegments().get(1));
        }
        public static Uri buildMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    public static final class ReviewEntry implements BaseColumns{
        public static final String TABLE_NAME = "review";
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        TABLE_NAME;

        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_REVIEW_ID = "review_id";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_MOVIE_AUTHOR = "movie_author";
        public static final String COLUMN_URL = "url";

        public static Uri buildReviewUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }
    public static final class TrailerEntry implements BaseColumns{
        // Location for the specific table (used to access table data)
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        // The type of data I will be sending if retrieved
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +PATH_TRAILER;
        // String holding the table name
        public static final String TABLE_NAME = "trailer";

        // Uri all trailer for a Movie in the movie table
//        public static Uri buildTrailerMovieIDUri(int MovieID){
//            return CONTENT_URI.buildUpon().appendPath(Integer.toString(MovieID)).build();
//        }
        // Columns in the Trailer DB
        public static final String COLUMN_TRAILER_ID = "trailer_id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_KEY = "key";
        public static final String COLUMN_NAME = "name";

        // Uri for the specific Trailer row in the trailer Table
        public static Uri buildTrailerUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }
    }

    // Class that creates the Favorite Movie (FavoriteMovies) table
    /*public static final class FavoriteMovies implements BaseColumns{
        // Location for the specific table (used to access table data)
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FAVORITE_MOVIES).build();

        // The type of data I will be sending if retrieved
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIES;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FAVORITE_MOVIES;

        // Uri for the specific favorite movie row in the favorite movie table
        public static Uri buildFavoriteMovieUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI,id);
        }

        // Uri for the specific Movie ID in the movie table
        public static Uri buildFavoriteMovieIDUri(int MovieID){
            return CONTENT_URI.buildUpon().appendPath(Integer.toString(MovieID)).build();
        }

        // Uri for getting the movie id
        public static String getFavoriteMovieID(Uri uri){
            return uri.getPathSegments().get(1);
        }

        // String hold the table name
        public static final String TABLE_NAME = "favorite_movies";

        // Columns in the favoriteMovies DB
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POPULARITY = "popularity";
        public static final String COLUMN_VOTE_AVERAGE = "vote_average";
    }*/
}
