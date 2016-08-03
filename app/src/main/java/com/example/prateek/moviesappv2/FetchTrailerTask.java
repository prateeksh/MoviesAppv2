package com.example.prateek.moviesappv2;

import android.content.ContentValues;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.GridView;

import com.example.prateek.moviesappv2.data.MovieContract;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.Vector;

/**
 * Created by Prateek on 02-08-2016.
 */
public class FetchTrailerTask extends AsyncTask<String,Void,Void> {

    public static String MovieId;
    private final String LOG_TAG = FetchTrailerTask.class.getSimpleName();
    public GridView ListTrailers;
    public TrailersAdapter trailersadapter;
    Context mContext;
    public FetchTrailerTask (Context context) {
        this.mContext = context;
    }

    private void getTrailerFromJSON(String trailerJsonStr)throws JSONException {
        final String RESULTS = "results";
        final String TRAILER_NAME = "name";
        final String TRAILER_KEY = "key";
        final String ID = "id";
        try {
            JSONObject trailerJson = new JSONObject(trailerJsonStr);
            JSONArray trailerArray = trailerJson.getJSONArray(RESULTS);
            Vector<ContentValues> cVVector = new Vector<ContentValues>(trailerArray.length());

            //Here statement to obtent the id of the movie!!!
            //Extract movie review data and build movie objects
            for (int i = 0; i < trailerArray.length(); i++) {

                String trailer_name;
                String trailer_key;
                String id;

                JSONObject trailerdata = trailerArray.getJSONObject(i);

                trailer_name = trailerdata.getString(TRAILER_NAME);
                trailer_key = trailerdata.getString(TRAILER_KEY);
                id = trailerdata.getString(ID);
                ContentValues trailerValues = new ContentValues();
                trailerValues.put(MovieContract.TrailerEntry.COLUMN_TRAILER_ID, id);
                trailerValues.put(MovieContract.TrailerEntry.COLUMN_KEY, trailer_key);
                trailerValues.put(MovieContract.TrailerEntry.COLUMN_NAME, trailer_name);
                cVVector.add(trailerValues);
            }
            int inserted = 0;
            if (cVVector.size() > 0) {
                ContentValues[] cvvArray = new ContentValues[cVVector.size()];
                cVVector.toArray(cvvArray);
                inserted = mContext.getContentResolver().bulkInsert(MovieContract.MovieEntry.CONTENT_URI, cvvArray);
            }
            Log.d(LOG_TAG, "Fetching Done " + inserted + "Inserted");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @Override
    protected Void doInBackground(String... params) {
        //String MovieId = params[0];
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        String trailerJsonStr = null;

        try{
            //Construct the url
            String URLString = null;
            URLString = "http://api.themoviedb.org/3/movie/" + MovieId + "/videos?api_key=e8e92e67635d148d3fc74a61aa393eec";
            //URLString = "http://api.themoviedb.org/3/movie/" + "269149" + "/videos?api_key=" + BuildConfig.OPEN_POPULAR_MOVIES_API_KEY;

            URL url = new URL(URLString);
            Log.v(LOG_TAG,"Trailer "+ URLString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            //Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();

            if (inputStream == null){
                trailerJsonStr = null;
            }else{
                reader = new BufferedReader(new InputStreamReader(inputStream));

            }
            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }
            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                trailerJsonStr = null;
            }
            trailerJsonStr = buffer.toString();

        } catch (MalformedURLException e1) {
            e1.printStackTrace();
        } catch (ProtocolException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        try{
            getTrailerFromJSON(trailerJsonStr);
        }catch (JSONException e){
            e.printStackTrace();
        }

        return null;

    }
    //postexecute
    /*@Override
    protected void onPostExecute(MovieTrailer[] result){
        if (result!=null){
            try{
                trailersadapter = new TrailersAdapter(getActivity(), Arrays.asList(result));
                ListTrailers = (GridView) getView().findViewById(R.id.listview_trailers);
                ListTrailers.setAdapter(trailersadapter);
                //setListViewHeightBasedOnChildren(ListTrailers);

                //Code to be able to insert listview inside scrollview from http://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view
                ListTrailers.setOnTouchListener(new View.OnTouchListener() {
                    // Setting on Touch Listener for handling the touch inside ScrollView
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        // Disallow the touch request for parent scroll on touch of child view
                        v.getParent().requestDisallowInterceptTouchEvent(true);
                        return false;
                    }
                });

                ListTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Context context = getActivity();
                        MovieTrailer result = trailersadapter.getItem(position);

                        String trailer_key = result.key;

                        //Intent to start the youtube video

                        Intent youtubeIntent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + trailer_key));
                        Log.v(LOG_TAG, "Trailer Key "+trailer_key);
                        startActivity(youtubeIntent);

                    }
                });

            }catch(NullPointerException e){
                e.printStackTrace();
            }

        }else{
            Toast.makeText(getContext(), "Nothing to show", Toast.LENGTH_SHORT).show();
        }
    }

*/
}