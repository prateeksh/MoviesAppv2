package com.example.prateek.moviesappv2.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Prateek on 01-07-2016.
 */
public class MovieProvider extends ContentProvider {

    static final int MOVIE = 100;
    static final int MOVIE_WITH_ID = 101;
    static final int MOVIE_TOP_RATED = 102;
    static final int MOVIE_POPULAR = 103;
    static final int MOVIE_FAVORITE = 104;
    static final int MOVIE_TRAILER = 300;
    static final int MOVIE_TRAILER_WITH_ID = 301;
    static final int MOVIE_REVIEW = 400;
    static final int MOVIE_REVIEW_WITH_ID = 401;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final String LOG_TAG = MovieProvider.class.getSimpleName();
    private static final String sMovieIsPopularSelection = MovieContract.MovieEntry.TABLE_NAME +
            "." + MovieContract.MovieEntry.COLUMN_IS_POPULAR + " = ? ";
    private static final String sMovieIsFavoriteSelection = MovieContract.MovieEntry.TABLE_NAME +
            "." + MovieContract.MovieEntry.COLUMN_IS_FAVORITE + " = ? ";
    private static final String sMovieIsTopRatedSelection = MovieContract.MovieEntry.TABLE_NAME +
            "." + MovieContract.MovieEntry.COLUMN_IS_TOP_RATED + " = ? ";
    private static final String sMovieByIdSelection = MovieContract.MovieEntry.TABLE_NAME +
            "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
    private static final String sMovieTrailerByIdSelection = MovieContract.MovieTrailerEntry.TABLE_NAME +
            "." + MovieContract.MovieTrailerEntry.COLUMN_ID + " = ? ";
    private static final String sMovieReviewByIdSelection = MovieContract.MovieReviewEntry.TABLE_NAME +
            "." + MovieContract.MovieReviewEntry.COLUMN_ID + " = ? ";
    private MovieDbHelper mOpenHelper;

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/#", MOVIE_WITH_ID); //change "*" with "#" if doesn't work
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/" + MovieContract.MovieEntry.POPULAR, MOVIE_POPULAR);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/" + MovieContract.MovieEntry.TOP_RATED, MOVIE_TOP_RATED);
        matcher.addURI(authority, MovieContract.PATH_MOVIE + "/" + MovieContract.MovieEntry.FAVORITE, MOVIE_FAVORITE);
        matcher.addURI(authority, MovieContract.PATH_TRAILER, MOVIE_TRAILER);
        matcher.addURI(authority, MovieContract.PATH_TRAILER + "/*", MOVIE_TRAILER_WITH_ID);
        matcher.addURI(authority, MovieContract.PATH_REVIEW, MOVIE_REVIEW);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/*", MOVIE_REVIEW_WITH_ID);
        return matcher;
    }

    private Cursor getMovieByPopularity(String[] projection) {
        String selection;
        String[] selectionArgs;
        String sortOrder;

        selection = sMovieIsPopularSelection;
        selectionArgs = new String[]{"1"};
        sortOrder = MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC ";

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovieByFavorites(String[] projection, String sortOrder) {
        String selection;
        String[] selectionArgs;

        selection = sMovieIsFavoriteSelection;
        selectionArgs = new String[]{"1"};

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovieByRating(String[] projection) {
        String selection;
        String[] selectionArgs;
        String sortOrder;

        selection = sMovieIsTopRatedSelection;
        selectionArgs = new String[]{"1"};
        sortOrder = MovieContract.MovieEntry.COLUMN_USER_RATING + " DESC ";

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovieById(Uri uri, String[] projection, String sortOrder) {
        long movieId = MovieContract.MovieEntry.getMovieIdFromUri(uri);

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieEntry.TABLE_NAME,
                projection,
                sMovieByIdSelection,
                new String[]{Long.toString(movieId)},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovieTrailerById(Uri uri, String sortOrder) {
        String movieTrailerId = MovieContract.MovieTrailerEntry.getTrailerIdFromUri(uri);

        return  mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieTrailerEntry.TABLE_NAME,
                new String[]{MovieContract.MovieTrailerEntry.COLUMN_KEY},
                sMovieTrailerByIdSelection,
                new String[]{movieTrailerId},
                null,
                null,
                sortOrder
        );
    }

    private Cursor getMovieReviewById(Uri uri,String[] projection, String sortOrder) {
        String movieReviewId = MovieContract.MovieReviewEntry.getReviewIdFromUri(uri);

        return mOpenHelper.getReadableDatabase().query(
                MovieContract.MovieReviewEntry.TABLE_NAME,
                projection,
                sMovieReviewByIdSelection,
                new String[]{movieReviewId},
                null,
                null,
                sortOrder
        );
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE: {
                return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
            }
            case MOVIE_WITH_ID: {
                return MovieContract.MovieEntry.CONTENT_ITEM_TYPE;
            }
            case MOVIE_POPULAR: {
                return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
            }
            case MOVIE_TOP_RATED: {
                return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
            }
            case MOVIE_FAVORITE:
                return MovieContract.MovieEntry.CONTENT_DIR_TYPE;
            case MOVIE_TRAILER:
                return MovieContract.MovieTrailerEntry.CONTENT_DIR_TYPE;
            case MOVIE_TRAILER_WITH_ID:
                return MovieContract.MovieTrailerEntry.CONTENT_ITEM_TYPE;
            case MOVIE_REVIEW:
                return MovieContract.MovieReviewEntry.CONTENT_DIR_TYPE;
            case MOVIE_REVIEW_WITH_ID:
                return MovieContract.MovieReviewEntry.CONTENT_ITEM_TYPE;
            default: {
                throw new UnsupportedOperationException("unknown uri: " + uri);

            }
        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)) {
            case MOVIE: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null, sortOrder);
                break;
            }

            case MOVIE_WITH_ID: {
                retCursor = getMovieById(uri , projection, sortOrder);
                break;
            }

            case MOVIE_POPULAR: {
                retCursor = getMovieByPopularity(projection);
                break;
            }
            case MOVIE_TOP_RATED: {
                retCursor = getMovieByRating(projection);
                break;
            }

            case MOVIE_FAVORITE: {
                retCursor = getMovieByFavorites(projection, sortOrder);
                break;
            }

            case MOVIE_TRAILER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieTrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case MOVIE_TRAILER_WITH_ID: {
                retCursor = getMovieTrailerById(uri, sortOrder);
                break;
            }

            case MOVIE_REVIEW: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }

            case MOVIE_REVIEW_WITH_ID: {
                retCursor = getMovieReviewById(uri, projection, sortOrder);
                break;
            }

            default: {
                Log.v(LOG_TAG, "In default");
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }


    @Override
    public Uri insert(Uri uri , ContentValues values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri ;
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, values);
                // insert unless it is already contained in the database
                if (_id > 0) {
                    returnUri = MovieContract.MovieEntry.buildMovieUri(_id);
                } else {
                    throw new android.database.SQLException("Failed to insert row into: " + uri);
                }
                break;
            }
            case MOVIE_TRAILER: {
                long _id = db.insert(MovieContract.MovieTrailerEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieTrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case MOVIE_REVIEW: {
                long _id = db.insert(MovieContract.MovieReviewEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            default: {
                throw new UnsupportedOperationException("Unknown uri: " + uri);

            }
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numDeleted;

        if (null == selection){ selection = "1";}
        switch(match){
            case MOVIE: {
                numDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.MovieEntry.TABLE_NAME + "'");
                break;
            }
            case MOVIE_TRAILER: {
                numDeleted = db.delete(
                        MovieContract.MovieTrailerEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            case MOVIE_REVIEW: {
                numDeleted = db.delete(
                        MovieContract.MovieReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numDeleted;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated ;
        int match = sUriMatcher.match(uri);

        if (contentValues == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(match){
            case MOVIE: {
                numUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIE_TRAILER: {
                numUpdated = db.update(MovieContract.MovieTrailerEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            }
            case MOVIE_REVIEW: {
                numUpdated = db.update(MovieContract.MovieReviewEntry.TABLE_NAME, contentValues, selection,
                        selectionArgs);
                break;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case MOVIE: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case MOVIE_TRAILER: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieTrailerEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return returnCount;
            }
            case MOVIE_REVIEW: {
                db.beginTransaction();
                int returnCount = 0;
                try {
                    for (ContentValues value : values) {
                        long _id = db.insert(MovieContract.MovieReviewEntry.TABLE_NAME, null, value);
                        if (_id != -1) {
                            returnCount++;
                        }
                    }
                    db.setTransactionSuccessful();
                } finally {
                    db.endTransaction();
                }
            getContext().getContentResolver().notifyChange(uri, null);
            return returnCount;
            }
            default:
                return super.bulkInsert(uri, values);
        }
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }

}