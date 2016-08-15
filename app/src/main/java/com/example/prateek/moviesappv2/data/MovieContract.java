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

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "review";

    public static final class MovieEntry implements BaseColumns {
        public static final String TABLE_NAME = "movie";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_MOVIE_IMG = "poster_path";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_DATE = "release_date";
        public static final String COLUMN_OVERVIEW = "overview";
        public static final String COLUMN_POPULARITY = "popular";
        public static final String COLUMN_USER_RATING = "user_rating";

        public static final String POPULAR = "popular";
        public static final String TOP_RATED = "top_rated";
        public static final String FAVORITE = "favorite";

        public static final String COLUMN_IS_POPULAR = "is_popular";
        public static final String COLUMN_IS_TOP_RATED = "is_top_rated";
        public static final String COLUMN_IS_FAVORITE = "is_favorite";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_MOVIE;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        PATH_MOVIE;

        // Uri for the specific Movie ID in the movie table
        public static long getMovieIdFromUri(Uri uri) {
            return Long.parseLong(uri.getPathSegments().get(1));
        }

        public static Uri buildMovieUriWithId(long id) {
            return CONTENT_URI.buildUpon().appendPath(Long.toString(id)).build();
        }

        public static Uri buildMovieUriWithSortOrder(String sortBy) {
            return CONTENT_URI.buildUpon().appendPath(sortBy).build();
        }

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    public static final class MovieReviewEntry implements BaseColumns {
        public static final String TABLE_NAME = "review";
        public static final String COLUMN_ID = "id";
        public static final String COLUMN_MOVIE_ID = "movie_id";
        public static final String COLUMN_CONTENT = "content";
        public static final String COLUMN_MOVIE_AUTHOR = "movie_author";
        public static final String COLUMN_URL = "url";

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        TABLE_NAME;

        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" +
                        TABLE_NAME;

        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }

        public static String getReviewIdFromUri(Uri uri) {
            return uri.getPathSegments().get(1);
        }

        public static Uri buildReviewUriById(String id) {
            return CONTENT_URI.buildUpon().appendPath(id).build();

        }
    }

    public static final class MovieTrailerEntry implements BaseColumns {
            // Location for the specific table (used to access table data)
            public static final Uri CONTENT_URI =
                    BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

            // The type of data I will be sending if retrieved
            public static final String CONTENT_DIR_TYPE =
                    ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
            public static final String CONTENT_ITEM_TYPE =
                    ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

            // String holding the table name
            public static final String TABLE_NAME = "trailer";

            // Columns in the Trailer DB
            public static final String COLUMN_ID = "id";
            public static final String COLUMN_MOVIE_ID = "movie_id";
            public static final String COLUMN_KEY = "key";

            // Uri for the specific Trailer row in the trailer Table
            public static Uri buildTrailerUri(long id) {
                return ContentUris.withAppendedId(CONTENT_URI, id);
            }
            public static String getTrailerIdFromUri(Uri uri) {
                return uri.getPathSegments().get(1);
            }
    }

}
