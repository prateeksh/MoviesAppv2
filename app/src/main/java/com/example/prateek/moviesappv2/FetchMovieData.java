package com.example.prateek.moviesappv2;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import com.example.prateek.moviesappv2.data.MovieContract;

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
    private Context mContext;



    private void getMoviesDatafromJason(String movieJsonStr)
            throws JSONException {

//        MovieMain[] movies ;
//        movies = new MovieMain[0];
        final String POSTER = "poster_path";
        final String TITLE = "original_title";
        final String RELEASE_DATE = "release_date";
        final String SYNOPSIS = "overview";
        final String RATING = "popularity";
        final String MOVIE_ID = "id";

        try {

            JSONObject imageJson = new JSONObject(movieJsonStr);
            JSONArray arrayResult = imageJson.getJSONArray("results");
            Vector<ContentValues>cVVector = new Vector<ContentValues>(arrayResult.length());

           // movies = new MovieMain[arrayResult.length()];

            for (int i = 0; i < arrayResult.length(); i++) {

                String img_path;
                String title;
                String sypnosis;
                String movie_release;
                String rating;
                String id;

                JSONObject imgDisplay = arrayResult.getJSONObject(i);

                img_path = "http://image.tmdb.org/t/p/w500" + imgDisplay.getString(POSTER);
                title = imgDisplay.getString(TITLE);
                movie_release = imgDisplay.getString(RELEASE_DATE);
                sypnosis = imgDisplay.getString(SYNOPSIS);
                rating = imgDisplay.getString(RATING);
                id = imgDisplay.getString(MOVIE_ID);

//                MovieMain element = new MovieMain(img_path, title, sypnosis, movie_release, rating);
//                movies[i] = element;

                ContentValues movieValues = new ContentValues();
                movieValues.put(MovieContract.MovieEntry._ID, id);
                movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, title);
                movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMG, img_path);
                movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, sypnosis);
                movieValues.put(MovieContract.MovieEntry.COLUMN_DATE, movie_release);
                movieValues.put(MovieContract.MovieEntry.COLUMN_RATINGS, rating);

                cVVector.add(movieValues);
                Log.v(LOG_TAG,img_path);
            }
            int inserted = 0;
            if (cVVector.size() > 0){
                ContentValues[] cvvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvvArray);
                inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvvArray);
            }
            Log.d(LOG_TAG, "Fetching Done " + inserted + "Inserted");

        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    protected Void doInBackground(String... params) {

        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String Url = params[0];

        String movieJsonStr = null;

        //int index = 20;

        try {


            final String Image_Base = "http://api.themoviedb.org/3/movie/"+Url+"?api_key=e8e92e67635d148d3fc74a61aa393eec";
            Log.v(LOG_TAG, "IN Trry");

            Uri builtUri = Uri.parse(Image_Base).buildUpon()

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
            getMoviesDatafromJason(movieJsonStr);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;

    }
}