package com.example.prateek.moviesappv2;


import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prateek.moviesappv2.data.MovieContract;
import com.squareup.picasso.Picasso;

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
import java.util.Arrays;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    static final String DETAIL_URI = "URI";
    private static final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();
    private static final String[] DETAIL_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.COLUMN_MOVIE_IMG,
            MovieContract.MovieEntry.COLUMN_RATINGS,
            MovieContract.MovieEntry.COLUMN_DATE,
            //MovieContract.MovieEntry.COLUMN_FAVORITE

    };
   /* private static final String[] DETAIL_REVIEW = {
            MovieContract.ReviewEntry.TABLE_NAME + "." + MovieContract.ReviewEntry._ID,
            MovieContract.ReviewEntry.COLUMN_MOVIE_ID,
            MovieContract.ReviewEntry.COLUMN_REVIEW_ID,
            MovieContract.ReviewEntry.COLUMN_CONTENT,
            MovieContract.ReviewEntry.COLUMN_MOVIE_AUTHOR
    };
    private static final String[] TRAILER_REVIEW = {
            MovieContract.TrailerEntry.TABLE_NAME + "." + MovieContract.TrailerEntry._ID,
            MovieContract.TrailerEntry.COLUMN_MOVIE_ID,
            MovieContract.TrailerEntry.COLUMN_TRAILER_ID,
            MovieContract.TrailerEntry.COLUMN_KEY,
            MovieContract.TrailerEntry.COLUMN_NAME
    };*/
    private static final int COL_MOVIE_ID = 0;
    private static final int COL_MOVIE_TITLE = 1;
    private static final int COL_MOVIE_OVERVIEW = 2;
    private static final int COL_MOVIE_IMG = 3;
    private static final int COL_MOVIE_RATING = 4;
    private static final int COL_MOVIE_RELEASE = 5;
    private static final int COL_MOVIE_FAVOURITE = 6;
    //public View rootView;
    private static final int CURSOR_LOADER_ID = 0;
    public static String MovieId;
    public boolean mIsFavourite;
    public ReviewAdapter reviewsadapter;
    public ListView ListReviews;
    private int mPosition;
    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mMovieOverview;
    private TextView mMovieRating;
    private TextView mMovieRelease;
    private Uri mUri;
    public MovieDetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView){
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null)
            return;
        int desiredWidth = View.MeasureSpec.makeMeasureSpec(listView.getWidth(), View.MeasureSpec.UNSPECIFIED);
        int totalHeight = 0;
        View view = null;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            view = listAdapter.getView(i, view, listView);
            if (i == 0)
                view.setLayoutParams(new ViewGroup.LayoutParams(desiredWidth, LinearLayout.LayoutParams.WRAP_CONTENT));

            view.measure(desiredWidth, View.MeasureSpec.UNSPECIFIED);
            totalHeight += view.getMeasuredHeight();
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v(LOG_TAG, "this is detail activity");
        Bundle arguments = getArguments();
        if(arguments != null) {
            mUri = arguments.getParcelable(MovieDetailActivityFragment.DETAIL_URI);
        }
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);



           mMoviePoster = (ImageView) rootView.findViewById(R.id.Movie_Image);
           //Picasso.with(getContext()).load(Image).into(imgView);

           mMovieTitle = (TextView) rootView.findViewById(R.id.Original_Title);
           //textView.setText(display);

           mMovieRelease = (TextView) rootView.findViewById(R.id.release);
           //textView1.setText(display2);

            mMovieOverview = (TextView) rootView.findViewById(R.id.Plot);
           //textView2.setText(synp);

            mMovieRating = (TextView) rootView.findViewById(R.id.rating);
           //textView3.setText(rat);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID,null,this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){

        Intent intent = getActivity().getIntent();
        if (intent == null) {
            return null;
        }

//        if (null!= mUri){
            return new CursorLoader(getActivity(),
                    intent.getData(),
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null);
//        }
//        return null;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        if (data != null && data.moveToFirst()) {
            mMovieTitle.setText(data.getString(COL_MOVIE_TITLE));
            mMovieRelease.setText(data.getString(COL_MOVIE_RELEASE));
            mMovieRating.setText(data.getString(COL_MOVIE_RATING) + "/10");
            mMovieOverview.setText(data.getString(COL_MOVIE_OVERVIEW));
            String movieposter = data.getString(COL_MOVIE_IMG);
            Picasso.with(getContext()).load(movieposter).into(mMoviePoster);
            Log.v(LOG_TAG, movieposter);

            //mIsFavourite = data.getInt(COL_MOVIE_FAVOURITE) > 0;

            MovieId = data.getString(COL_MOVIE_ID);
            //this.updateReviews();
            //this.updateTrailers();

        }
    }
//    public static String getFavouriteId(){
//        return MovieId;
//    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        //mDetailCursor = null;
    }

    private void updateTrailers(){
        FetchTrailerTask trailersTask = new FetchTrailerTask(getContext());
        trailersTask.execute(MovieId);
    }

    private void updateReviews(){
        FetchReviewsTask reviewsTask = new FetchReviewsTask();
        reviewsTask.execute(MovieId);
    }


    public class FetchReviewsTask extends AsyncTask<String, Void, MovieReview[]>{

        private MovieReview[] getReviewsDataFromJson(String reviewJsonStr)throws JSONException{

            //these are the names of the JSON object that need to be extracted

            final String RESULTS = "results";
            final String REVIEW_ID = "id";
            final String REVIEW_AUTHOR = "author";
            final String REVIEW_CONTENT = "content";

            JSONObject reviewJson = new JSONObject(reviewJsonStr);
            JSONArray reviewArray = reviewJson.getJSONArray(RESULTS);

            MovieReview[] resultStr = new MovieReview[reviewArray.length()];

            //Here statement to obtent the id of the movie!!!
            //Extract movie review data and build movie objects
            for (int i = 0; i<reviewArray.length();i++){

                String review_id;
                String review_author;
                String review_content;

                JSONObject reviewdata = reviewArray.getJSONObject(i);

                review_id = reviewdata.getString(REVIEW_ID);
                review_author = reviewdata.getString(REVIEW_AUTHOR);
                review_content = reviewdata.getString(REVIEW_CONTENT);

                MovieReview element = new MovieReview(review_id,review_author,review_content);
                resultStr[i]= element;


            }
            return resultStr;

        }


        @Override
        protected MovieReview[] doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String reviewJsonStr = null;

            try{
                //Construct the url
                String URLString = null;
                URLString = "http://api.themoviedb.org/3/movie/" + MovieId + "/reviews?api_key=e8e92e67635d148d3fc74a61aa393eec";
                URL url = new URL(URLString);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                //Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();

                if (inputStream == null){
                    reviewJsonStr = null;
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
                    reviewJsonStr = null;
                }
                reviewJsonStr = buffer.toString();

            } catch (MalformedURLException e1) {
                e1.printStackTrace();
            } catch (ProtocolException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            }

            try{
                return getReviewsDataFromJson(reviewJsonStr);
            }catch (JSONException e){
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(MovieReview[] result){
            if (result!=null){
                try{
                    reviewsadapter = new ReviewAdapter(getActivity(), Arrays.asList(result));
                    ListReviews = (ListView) getView().findViewById(R.id.listview_reviews);
                    ListReviews.setAdapter(reviewsadapter);
                    setListViewHeightBasedOnChildren(ListReviews);

                    //Code to be able to insert listview inside scrollview from http://stackoverflow.com/questions/18367522/android-list-view-inside-a-scroll-view
                    ListReviews.setOnTouchListener(new View.OnTouchListener() {
                        // Setting on Touch Listener for handling the touch inside ScrollView
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            // Disallow the touch request for parent scroll on touch of child view
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                            return false;
                        }
                    });

                }catch(NullPointerException e){
                    e.printStackTrace();
                }

            }else{
                Toast.makeText(getContext(), "Nothing to show", Toast.LENGTH_SHORT).show();
            }
        }
    }




}
