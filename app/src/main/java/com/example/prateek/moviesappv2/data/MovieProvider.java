package com.example.prateek.moviesappv2.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

/**
 * Created by Prateek on 01-07-2016.
 */
public class MovieProvider extends ContentProvider {

    public static final String sMovieIdSettingSelection =
            MovieContract.MovieEntry.TABLE_NAME +
                    "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
    static final int REVIEW_FOR_MOVIE = 106;
    static final int TRAILER_FOR_MOVIE = 107;
    private static final String LOG_TAG = MovieProvider.class.getSimpleName();
    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 101;
    private static final int REVIEW = 102;
    private static final int TRAILER = 103;
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private static final int FAVORITE_MOVIES = 104;
    private static final int FAVORITE_MOVIE_ID = 105;
    //    public static final SQLiteQueryBuilder sMovieSettingQueryBuilder;
//
//    static {
//        sMovieSettingQueryBuilder = new SQLiteQueryBuilder();
//    }
//
    private static final SQLiteQueryBuilder sMovieWithReview;
    private static final SQLiteQueryBuilder sMovieWithTrailer;
    private static final SQLiteQueryBuilder sFavoriteMovie;

    static{
        sMovieWithReview = new SQLiteQueryBuilder();
        sMovieWithTrailer = new SQLiteQueryBuilder();
        sFavoriteMovie = new SQLiteQueryBuilder();

        // This is an inner join which looks at
        // review INNER JOIN movie on movie.movie_id = review.movie_id
        sMovieWithReview.setTables(
                MovieContract.ReviewEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.ReviewEntry.TABLE_NAME +
                        "." + MovieContract.ReviewEntry.COLUMN_MOVIE_ID
        );

        // This is an inner join which looks at
        // trailer INNER JOIN movie on movie.movie_id = trailer.movie_id
        sMovieWithTrailer.setTables(
                MovieContract.TrailerEntry.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.TrailerEntry.TABLE_NAME +
                        "." + MovieContract.TrailerEntry.COLUMN_MOVIE_ID
        );

        // This is an inner join which looks a
        // favorite_movie INNER JOIN movie on movie.movie_id = favorite_movie.movie_id
        /*sFavoriteMovie.setTables(
                MovieContract.FavoriteMovies.TABLE_NAME + " INNER JOIN " +
                        MovieContract.MovieEntry.TABLE_NAME +
                        " ON " + MovieContract.MovieEntry.TABLE_NAME +
                        "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID +
                        " = " + MovieContract.FavoriteMovies.TABLE_NAME +
                        "." + MovieContract.FavoriteMovies.COLUMN_MOVIE_ID
        );*/
    }

    private MovieDbHelper mOpenHelper;



    // favorite_movie.movie_id = ?
    /*public static final String sFavoriteSettingSelection =
            MovieContract.FavoriteMovies.TABLE_NAME +
                    "." + MovieContract.FavoriteMovies.COLUMN_MOVIE_ID + " = ? ";*/

    /**
     * Get a Cusor using SQLiteQueryBuilder for a join between Favorite and movie
     * @param uri - The Uri location for Favorite
     * @return - Returns a Cursor with favorite movie
     */
    /*private Cursor getFavoriteMovie(Uri uri){
        return sFavoriteMovie.query(
                mOpenHelper.getReadableDatabase(),
                new String[]{"movie.movie_id"}, //null,
                sFavoriteSettingSelection,//null,
                new String[]{MovieContract.MovieEntry.getMovieID(uri)},//null,MovieContract.FavoriteMovies.getFavoriteMovieID(uri)
                null,
                null,
                null);
    }*/

    private static UriMatcher buildUriMatcher() {
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;

        matcher.addURI(authority, MovieContract.MovieEntry.TABLE_NAME, MOVIE);
        matcher.addURI(authority, MovieContract.MovieEntry.TABLE_NAME +
                "/#", MOVIE_WITH_ID); //change "*" with "#" if doesn't work
        matcher.addURI(authority, MovieContract.PATH_TRAILER, TRAILER);
        matcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
//        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIE_ID);
//        matcher.addURI(authority, MovieContract.PATH_FAVORITE_MOVIES, FAVORITE_MOVIES);
        matcher.addURI(authority, MovieContract.PATH_REVIEW + "/*",REVIEW_FOR_MOVIE);
        matcher.addURI(authority, MovieContract.PATH_TRAILER + "/*", TRAILER_FOR_MOVIE);
        return matcher;
    }

    private Cursor getMovieReview(Uri uri){
        return sMovieWithReview.query(mOpenHelper.getReadableDatabase(),
                new String[]{"review._ID,review_id","movie.movie_id","author","content","url"},
                sMovieIdSettingSelection,
                new String[]{MovieContract.MovieEntry.getMovieID(uri)},
                null,
                null,
                null);
    }

    /**
     * Get a Cursor using SQLiteQueryBuilder for a join between Trailer and movie
     * @param uri - The Uri location for Trailer
     * @return - Returns a Cursor with movie trailer
     */
    private Cursor getMovieTrailer(Uri uri){
        return sMovieWithTrailer.query(
                mOpenHelper.getReadableDatabase(),
                new String[]{"trailer._ID","trailer_id","movie.movie_id","key","name"},
                sMovieIdSettingSelection,
                new String[]{MovieContract.MovieEntry.getMovieID(uri)},
                null,
                null,
                null);
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
            case REVIEW:
                return MovieContract.ReviewEntry.CONTENT_DIR_TYPE;
            case TRAILER:
                return MovieContract.TrailerEntry.CONTENT_TYPE;
            case REVIEW_FOR_MOVIE:
                return MovieContract.ReviewEntry.CONTENT_ITEM_TYPE;
            case TRAILER_FOR_MOVIE:
                return MovieContract.TrailerEntry.CONTENT_ITEM_TYPE;
            /*case FAVORITE_MOVIES:
                return MovieContract.FavoriteMovies.CONTENT_ITEM_TYPE;
            case FAVORITE_MOVIE_ID:
                return MovieContract.FavoriteMovies.CONTENT_ITEM_TYPE;*/
            default: {
                throw new UnsupportedOperationException("unknown uri: " + uri);

            }
        }
    }


    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;
        switch (sUriMatcher.match(uri)){
            case MOVIE : {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection, selection, selectionArgs,
                        null, null , sortOrder);
                return retCursor;
            }

            case MOVIE_WITH_ID : {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.MovieEntry.TABLE_NAME,
                        projection, MovieContract.MovieEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))},
                        null, null, sortOrder
                );
                return retCursor;
            }
            case REVIEW: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.ReviewEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case TRAILER: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.TrailerEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
          /*  case FAVORITE_MOVIES: {
                retCursor = mOpenHelper.getReadableDatabase().query(
                        MovieContract.FavoriteMovies.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;
            }
            case FAVORITE_MOVIE_ID:{
                retCursor = getFavoriteMovie(uri);
                break;
            }*/
            default:{
                Log.v(LOG_TAG, "In default");
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
        retCursor.setNotificationUri(getContext().getContentResolver(),uri);
        return retCursor;
    }

    @Override
    public Uri insert(Uri uri , ContentValues values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        Uri returnUri;
        switch (sUriMatcher.match(uri)) {
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
            case TRAILER:{
                long _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.TrailerEntry.buildTrailerUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            case REVIEW:{
                long _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.ReviewEntry.buildReviewUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
          /*  case FAVORITE_MOVIE_ID:{
                long _id = db.insert(MovieContract.FavoriteMovies.TABLE_NAME, null, values);
                if(_id > 0)
                    returnUri = MovieContract.FavoriteMovies.buildFavoriteMovieUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }*/

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
        switch(match){
            case MOVIE: {
                numDeleted = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME, selection, selectionArgs);
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.MovieEntry.TABLE_NAME + "'");
                break;
            }
            case MOVIE_WITH_ID: {
                numDeleted = db.delete(MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry._ID + " = ?",
                        new String[]{String.valueOf(ContentUris.parseId(uri))});
                // reset _ID
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.MovieEntry.TABLE_NAME + "'");

                break;
            }
            case REVIEW: {
                numDeleted = db.delete(
                        MovieContract.ReviewEntry.TABLE_NAME,selection,selectionArgs
                );
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.ReviewEntry.TABLE_NAME + "'");
                break;
            }
            case TRAILER: {
                numDeleted = db.delete(
                        MovieContract.TrailerEntry.TABLE_NAME,selection,selectionArgs
                );
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.TrailerEntry.TABLE_NAME + "'");
                break;
            }
        /*    case FAVORITE_MOVIES: {
                numDeleted = db.delete(
                        MovieContract.FavoriteMovies.TABLE_NAME, selection, selectionArgs
                );
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.FavoriteMovies.TABLE_NAME + "'");
                break;
            }
            case FAVORITE_MOVIE_ID: {
                numDeleted = db.delete(
                        MovieContract.FavoriteMovies.TABLE_NAME, selection, selectionArgs
                );
                db.execSQL("DELETE FROM SQLITE_SEQUENCE WHERE NAME = '" +
                        MovieContract.FavoriteMovies.TABLE_NAME + "'");
                break;
            }*/
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        if (numDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return numDeleted;
    }
  /*  @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        int numInserted;
        switch (match) {
            case MOVIE:
                // allows for multiple transactions
                db.beginTransaction();

                // keep track of successful inserts
                numInserted = 0;
                long _id;
                try {
                    for (ContentValues value : values) {
                        if (value != null) {
                            //throw new IllegalArgumentException("Cannot have null content values");
                            switch (match) {
                                case MOVIE: {
                                    _id = db.insertOrThrow(MovieContract.MovieEntry.TABLE_NAME,
                                            null, value);

//                            Log.w(LOG_TAG, "Attempting to insert " +
//                                    value.getAsString(
//                                            MovieContract.MovieEntry.COLUMN_VERSION_NAME)
//                                    + " but value is already in database.");
                                    if (_id != -1) {
                                        numInserted++;
                                    }
                                    break;
                                }
                                case MOVIE_WITH_ID: {
                                    _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                                    if (_id != -1) {
                                        numInserted++;
                                    }
                                    break;
                                }
                                case TRAILER: {
                                    _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, value);
                                    if (_id != -1) {
                                        numInserted++;
                                    }
                                    break;
                                }
                                case REVIEW: {
                                    _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, value);
                                    if (_id != -1) {
                                        numInserted++;
                                    }
                                    break;
                                }
                                case FAVORITE_MOVIES: {
                                    _id = db.insert(MovieContract.FavoriteMovies.TABLE_NAME, null, value);
                                    if (_id != -1) {
                                        numInserted++;
                                    }
                                    break;
                                }
                                case FAVORITE_MOVIE_ID: {
                                    _id = db.insert(MovieContract.FavoriteMovies.TABLE_NAME, null, value);
                                    if (_id != -1) {
                                        numInserted++;
                                    }
                                    break;
                                }
                                default:
                                    return super.bulkInsert(uri, values);
                            }
                            if (numInserted > 0) {
                                // If no errors, declare a successful transaction.
                                // database will not populate if this is not called
                                db.setTransactionSuccessful();
                            }
                        }
                    }
                } finally {
                    // all transactions occur at once
                    db.endTransaction();
                }
                getContext().getContentResolver().notifyChange(uri, null);
                return numInserted;

        }
    }*/
    @Override
    public int bulkInsert(Uri uri, ContentValues[] values){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        db.beginTransaction();
        int returnCount = 0;
        long _id;

        try {
            for (ContentValues value : values) {
                if(value != null) {
                    switch (match) {
                        case MOVIE: {
                            _id = db.insertOrThrow(MovieContract.MovieEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case MOVIE_WITH_ID: {
                            _id = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case TRAILER: {
                            _id = db.insert(MovieContract.TrailerEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case REVIEW: {
                            _id = db.insert(MovieContract.ReviewEntry.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                       /* case FAVORITE_MOVIES: {
                            _id = db.insert(MovieContract.FavoriteMovies.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }
                        case FAVORITE_MOVIE_ID: {
                            _id = db.insert(MovieContract.FavoriteMovies.TABLE_NAME, null, value);
                            if (_id != -1) {
                                returnCount++;
                            }
                            break;
                        }*/
                        default:
                            return super.bulkInsert(uri, values);
                    }
                }
            }

            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnCount;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs){
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        int numUpdated = 0;

        if (contentValues == null){
            throw new IllegalArgumentException("Cannot have null content values");
        }

        switch(sUriMatcher.match(uri)){
            case MOVIE:{
                numUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME ,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case MOVIE_WITH_ID: {
                numUpdated = db.update(MovieContract.MovieEntry.TABLE_NAME ,
                        contentValues,
                        MovieContract.MovieEntry._ID + " = ?",
                        new String[] {String.valueOf(ContentUris.parseId(uri))});
                break;
            }
            case REVIEW:{
                numUpdated = db.update(MovieContract.ReviewEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case TRAILER:{
                numUpdated = db.update(MovieContract.TrailerEntry.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            /*case FAVORITE_MOVIE_ID:{
                numUpdated = db.update(MovieContract.FavoriteMovies.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }
            case FAVORITE_MOVIES:{
                numUpdated = db.update(MovieContract.FavoriteMovies.TABLE_NAME,
                        contentValues,
                        selection,
                        selectionArgs);
                break;
            }*/
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }

        if (numUpdated > 0){
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return numUpdated;
    }

}