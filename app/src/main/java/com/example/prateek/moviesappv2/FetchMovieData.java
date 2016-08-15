package com.example.prateek.moviesappv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.prateek.moviesappv2.data.MovieContract;
import com.example.prateek.moviesappv2.data.MovieDbHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Prateek on 11-07-2016.
 */
public class FetchMovieData extends AsyncTask<String, Void, Void> {

    private final String LOG_TAG = FetchMovieData.class.getSimpleName();
    public String sortValue;
    private Context mContext;
    private MovieDbHelper mOpenHelper;
    private String mSchemeName;
    private String mAuthorityName;
    private String mUrlNumber;
    private String mMovieUrl;

    public FetchMovieData (Context context) {
        this.mContext = context;
        mOpenHelper = new MovieDbHelper(mContext);
        mSchemeName = "http";
        mAuthorityName = "api.themoviedb.org";
        mUrlNumber = "3";
        mMovieUrl = "movie";
    }

    @Override
    public void onPreExecute(){
        super.onPreExecute();
    }

    private void getMoviesDatafromJason(String movieJsonStr, String orderBy)
            throws JSONException {


        if (movieJsonStr != null) {

            final String POSTER = "poster_path";
            final String TITLE = "original_title";
            final String RELEASE_DATE = "release_date";
            final String SYNOPSIS = "overview";
            final String USER_RATING = "vote_average";
            final String POPULARITY = "popularity";
            final String MOVIE_ID = "id";

            String categoryType = null;

            try {
                JSONObject imageJson = new JSONObject(movieJsonStr);
                JSONArray arrayResult = imageJson.getJSONArray("results");

                Vector<ContentValues> cVVector = new Vector<ContentValues>(arrayResult.length());

                switch (orderBy){
                    case MovieContract.MovieEntry.POPULAR:{
                        categoryType = MovieContract.MovieEntry.COLUMN_IS_POPULAR;
                        break;
                    }
                    /*case MovieEntry.FAVORITE: {
                        categoryType = MovieEntry.COLUMN_IS_FAVORITE;
                        break;
                    }*/
                    case MovieContract.MovieEntry.TOP_RATED: {
                        categoryType = MovieContract.MovieEntry.COLUMN_IS_TOP_RATED;
                        break;
                    }
                    default: {
                        // By default, the category for ordering the movies is popular
                        categoryType = MovieContract.MovieEntry.COLUMN_IS_POPULAR;
                        break;
                    }
                }
                Long[] movId = new Long[arrayResult.length()];
                for (int i = 0; i < arrayResult.length(); i++) {

                    String img_path;
                    String title;
                    String sypnosis;
                    String movie_release;
                    double user_rating;
                    long id;
                    double popularity;
                    int movieIdFlag;

                    JSONObject imgDisplay = arrayResult.getJSONObject(i);

                    img_path = imgDisplay.getString(POSTER).substring(0);
                    title = imgDisplay.getString(TITLE);
                    movie_release = imgDisplay.getString(RELEASE_DATE);
                    sypnosis = imgDisplay.getString(SYNOPSIS);
                    user_rating = imgDisplay.getDouble(USER_RATING);
                    id = imgDisplay.getLong(MOVIE_ID);
                    popularity = imgDisplay.getDouble(POPULARITY);

                    movId[i] = id;
                    movieIdFlag = checkMovieById(id, categoryType);

                    ContentValues movieValues = new ContentValues();
                    if (movieIdFlag == 2) {
                        movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, popularity);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, user_rating);
                        int updatedRow = 0;
                        String selection = MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
                        updatedRow = mContext.getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI,
                                movieValues, selection, new String[]{Long.toString(id)});
                    } else if (movieIdFlag == 1) {
                        movieValues.put(categoryType, 1);
                        int updatedRow = 0;
                        String selection = MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ? ";
                        updatedRow = mContext.getContentResolver().update(MovieContract.MovieEntry.CONTENT_URI, movieValues, selection, new String[]{Long.toString(id)});
                    } else if (movieIdFlag == 0) {
                        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID, id);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMG, img_path);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, sypnosis);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_DATE, movie_release);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING, user_rating);
                        movieValues.put(MovieContract.MovieEntry.COLUMN_POPULARITY, popularity);
                        movieValues.put(categoryType, 1);

                        cVVector.add(movieValues);
                        Log.v(LOG_TAG, movie_release);
                    }
                }
                int inserted = 0;
                if (cVVector.size() > 0) {
                    ContentValues[] cvvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvvArray);
                    inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvvArray);
                }
                deleteUnnecessaryData(movId, categoryType);

                Log.d(LOG_TAG, "Fetching Done " + inserted + "Inserted");

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
    @Override
    protected Void doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String categortyOrder = params[0];

        String movieJsonStr = null;

        try {


            final String Image_Base = "http://api.themoviedb.org/3/discover/movie";
            final String SORT_BY = "sort_by";
            final String API_KEY_PARAM = "api_key";
            Log.v(LOG_TAG, "IN Trry");

            Uri builtUri = Uri.parse(Image_Base).buildUpon()
                    .appendQueryParameter(SORT_BY,categortyOrder)
                    .appendQueryParameter(API_KEY_PARAM, BuildConfig.OPEN_MOVIE_API_KEY)
                    .build();

            URL url = new URL(builtUri.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();
            Log.v(LOG_TAG,"URL " +url);
            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                movieJsonStr = null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                movieJsonStr = null;
            }

            movieJsonStr = buffer.toString();


        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);

            movieJsonStr = null;
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        try {
            Log.v(LOG_TAG, "JSON "+ movieJsonStr);
            getMoviesDatafromJason(movieJsonStr, categortyOrder);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
    int checkMovieById (long id, String categoryType) {
        long movieId = 0;
        int categoryFlag = 0;
        int returnFlag = 0;
        final String [] projection = {Long.toString(id), categoryType};

        final int COL_ID = 0;
        final int COL_ORDER_BY = 1;

        Cursor movieCursor = mContext.getContentResolver().query(
                MovieContract.MovieEntry.buildMovieUriWithId(id),
                projection,
                null,
                null,
                null);
        if (movieCursor != null) {
            if (movieCursor.moveToFirst()) {
                movieId = movieCursor.getLong(COL_ID);
                returnFlag = 1;
                categoryFlag = movieCursor.getInt(COL_ORDER_BY);
                if (categoryFlag == 1 && movieId != 0) {
                    returnFlag = 2;
                }

            }
            movieCursor.close();
        }

        return returnFlag;
    }
    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);}

    String makePlaceHolder(int length) {
        if (length < 1) {
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(length * 2 -1);
            sb.append("?");
            for (int i = 1; i < length; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

    void deleteUnnecessaryData(Long[] movId, String category) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        String[] movieIds = new String[movId.length];
        for (int i = 0; i < movId.length; i++) {
            movieIds[i] = movId[i].toString();
        }

        String subQuery = "SELECT " + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " FROM " + MovieContract.MovieEntry.TABLE_NAME + " WHERE " +
                category + " = 1 AND " + MovieContract.MovieEntry.COLUMN_MOVIE_ID + " NOT IN (" + makePlaceHolder(movId.length) + " );";
        Cursor cursor = db.rawQuery(subQuery, movieIds);
        if(cursor != null) {
            long id;
            cursor.moveToFirst();
            for (int i = 0; i < cursor.getCount(); i++, cursor.moveToNext()) {
                id = cursor.getLong(0);
                int updatedRow = db.delete(
                        MovieContract.MovieEntry.TABLE_NAME,
                        MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[] {Long.toString(id)});
            }
            cursor.close();
        }
        db.close();

    }
    private void getMovieReviewsDetailsFromId(long id) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //Will contain the raw JSON response as a string.
        String movieReviewDetailsJsonStr = null;
        try {
            // Construct the URL for the TheMovieDb query
            final String APP_KEY_PARAM = "api_key";
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(mSchemeName)
                    .authority(mAuthorityName)
                    .appendPath(mUrlNumber)
                    .appendPath(mMovieUrl)
                    .appendPath(Long.toString(id))
                    .appendPath("reviews")
                    .appendQueryParameter(APP_KEY_PARAM, BuildConfig.OPEN_MOVIE_API_KEY)
                    .build();

            URL url = new URL(builder.toString());

            // Create the request to TheMovieDb, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0) {
                // Stream is empty. No need in parsing.
                return;
            }

            movieReviewDetailsJsonStr = buffer.toString();
            getMovieReviewsDetailsFromJson(movieReviewDetailsJsonStr, id);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    private void getMovieReviewsDetailsFromJson(String movieReviewDetailsJsonStr, long movieId) throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String TMD_RESULTS = "results";
        final String TMD_AUTHOR = "author";
        final String TMD_ID = "id";
        final String TMD_CONTENT = "content";
        final String TMD_URL = "url";

        try {
            JSONObject movieReviewDetails = new JSONObject(movieReviewDetailsJsonStr);
            JSONArray movieReviewsArray = movieReviewDetails.getJSONArray(TMD_RESULTS);

            if(movieReviewsArray.length() != 0) {
                Vector<ContentValues> cVVector = new Vector<ContentValues>(movieReviewsArray.length());
                for (int i = 0; i < movieReviewsArray.length(); i++) {
                    String author;
                    String id;
                    String content;
                    String url;

                    JSONObject movieReviewData = movieReviewsArray.getJSONObject(i);
                    author = movieReviewData.getString(TMD_AUTHOR);
                    id = movieReviewData.getString(TMD_ID);
                    content = movieReviewData.getString(TMD_CONTENT);
                    url = movieReviewData.getString(TMD_URL);

                    ContentValues movieReviewValues = new ContentValues();

                    movieReviewValues.put(MovieContract.MovieReviewEntry.COLUMN_ID, id);
                    movieReviewValues.put(MovieContract.MovieReviewEntry.COLUMN_MOVIE_AUTHOR, author);
                    movieReviewValues.put(MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID, movieId);
                    movieReviewValues.put(MovieContract.MovieReviewEntry.COLUMN_CONTENT, content);
                    movieReviewValues.put(MovieContract.MovieReviewEntry.COLUMN_URL, url);

                    cVVector.add(movieReviewValues);
                }

                int inserted = 0;
                if (cVVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieReviewEntry.CONTENT_URI, cvArray);
                }

            }
            else {
                ContentValues movieReviewValue = new ContentValues();
                movieReviewValue.put(MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID, movieId);
                Uri insertedId = mContext.getContentResolver().insert(MovieContract.MovieReviewEntry.CONTENT_URI, movieReviewValue);

            }

        } catch(JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }

    private void getMovieTrailerDetailsFromId(long id) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        //Will contain the raw JSON response as a string.
        String movieTrailerDetailsJsonStr = null;
        try {
            // Construct the URL for the TheMovieDb query
            //final String MOVIE_DETAILS_BASE_URL = "http://api.themoviedb.org/3/movie/" + id +"/videos?";
            final String APP_KEY_PARAM = "api_key";
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(mSchemeName)
                    .authority(mAuthorityName)
                    .appendPath(mUrlNumber)
                    .appendPath(mMovieUrl)
                    .appendPath(Long.toString(id))
                    .appendPath("videos")
                    .appendQueryParameter(APP_KEY_PARAM, BuildConfig.OPEN_MOVIE_API_KEY)
                    .build();

            URL url = new URL(builder.toString());

            // Create the request to TheMovieDb, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));

            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line + "\n");
            }

            if(buffer.length() == 0) {
                // Stream is empty. No need in parsing.
                return;
            }

            movieTrailerDetailsJsonStr = buffer.toString();
            getMovieTrailerDetailsFromJson(movieTrailerDetailsJsonStr, id);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e(LOG_TAG, "Error closing stream", e);
                }
            }
        }
    }

    private void getMovieTrailerDetailsFromJson(String movieTrailerDetailsJsonStr, long movieId) throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String TMD_RESULTS = "results";
        final String TMD_KEY = "key";
        final String TMD_ID = "id";
        final String TMD_TYPE = "type";

        try {
            JSONObject movieTrailerDetails = new JSONObject(movieTrailerDetailsJsonStr);
            JSONArray movieTrailersArray = movieTrailerDetails.getJSONArray(TMD_RESULTS);

            if(movieTrailersArray.length() != 0) {
                Vector<ContentValues> cVVector = new Vector<ContentValues>(movieTrailersArray.length());
                for (int i = 0; i < movieTrailersArray.length(); i++) {
                    String key;
                    String id;
                    String type;

                    JSONObject movieTrailerData = movieTrailersArray.getJSONObject(i);
                    key = movieTrailerData.getString(TMD_KEY);
                    id = movieTrailerData.getString(TMD_ID);
                    type = movieTrailerData.getString(TMD_TYPE);

                    ContentValues movieTrailerValues = new ContentValues();

                    movieTrailerValues.put(MovieContract.MovieTrailerEntry.COLUMN_ID, id);
                    movieTrailerValues.put(MovieContract.MovieTrailerEntry.COLUMN_KEY, key);
                    movieTrailerValues.put(MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID, movieId);

                    cVVector.add(movieTrailerValues);
                }

                int inserted = 0;
                if (cVVector.size() > 0) {
                    ContentValues[] cvArray = new ContentValues[cVVector.size()];
                    cVVector.toArray(cvArray);
                    inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieTrailerEntry.CONTENT_URI, cvArray);
                }

            }
            else {
                ContentValues movieTrailerValue = new ContentValues();
                movieTrailerValue.put(MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID, movieId);
                Uri insertedId = mContext.getContentResolver().insert(MovieContract.MovieTrailerEntry.CONTENT_URI, movieTrailerValue);

            }

        } catch(JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }

    }
}