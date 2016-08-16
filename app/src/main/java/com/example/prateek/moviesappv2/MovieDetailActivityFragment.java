package com.example.prateek.moviesappv2;


import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.view.menu.ActionMenuItemView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prateek.moviesappv2.data.MovieContract;
import com.squareup.picasso.Picasso;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailActivityFragment extends Fragment implements
        LoaderManager.LoaderCallbacks<Cursor>, View.OnClickListener{


    public static final String DETAIL_URI = "DETAIL_URI";
    private static final String LOG_TAG = MovieDetailActivityFragment.class.getSimpleName();
    private static final String[] DETAIL_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_IMG,
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_USER_RATING,
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_DATE,
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_OVERVIEW,
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_IS_FAVORITE
     };
    private static final int COL_MOVIE_ID = 1;
    private static final int COL_MOVIE_URI_ID = 2;
    private static final int COL_MOVIE_IMG = 3;
    private static final int COL_MOVIE_TITLE = 4;
    private static final int COL_MOVIE_RELEASE = 5;
    private static final int COL_MOVIE_RATING = 6;
    private static final int COL_MOVIE_OVERVIEW = 7;
    private static final int COL_IS_FAVORITE = 8;
    private static final int CURSOR_LOADER_ID = 0;

    private Uri mUri;
    private long MovieId;
    private ImageButton mImageButton;
    private TextView mHeader;
    private View mDivider;
    private Button mTrailerButton;
    private Button mReviewButton;

    private int mIsFavorite;
    private ImageView mMoviePoster;
    private TextView mMovieTitle;
    private TextView mMovieOverview;
    private TextView mMovieRating;
    private TextView mMovieRelease;

    public MovieDetailActivityFragment() {
        setHasOptionsMenu(true);
    }



    public static int getState(long movieId, Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                "Favorite", Context.MODE_PRIVATE
        );
        String key = "State" + Long.toString(movieId);
        Log.v(LOG_TAG, key);
        return sharedPreferences.getInt(key, 0);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.favorite_button:{
                changeFavorites(MainActivity.getPaneMode());
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if(arguments != null){
            mUri = arguments.getParcelable(MovieDetailActivityFragment.DETAIL_URI);
            Log.v(LOG_TAG, "argumnts not null");
        }
        Log.v(LOG_TAG, "arguments null");
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);

        //mMovieAdapter = new CustomAdapter(getActivity(), null, 0);
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
        NestedScrollView scrollView = (NestedScrollView) rootView.findViewById(R.id.detail_scrollview);
        scrollView.setOnTouchListener(new View.OnTouchListener() {
            int dragThreshold = 30;
            int downX;
            int downY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        downX = (int) event.getRawX();
                        downY = (int) event.getRawY();
                        break;
                    }
                    case MotionEvent.ACTION_MOVE: {
                        int distanceX = Math.abs((int) event.getRawX() - downX);
                        int distanceY = Math.abs((int) event.getRawY() - downY);

                        if (distanceY > distanceX) {
                            v.getParent().requestDisallowInterceptTouchEvent(true);
                        } else if (distanceX > distanceY && distanceX > dragThreshold) {
                            v.getParent().requestDisallowInterceptTouchEvent(false);
                        }
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        v.getParent().requestDisallowInterceptTouchEvent(false);
                        break;
                    }
                }
                return false;
            }
        });

        if(MainActivity.getPaneMode()){
            mImageButton = (ImageButton) rootView.findViewById(R.id.favorite_button_tab);
            mImageButton.setOnClickListener(this);

            mDivider = rootView.findViewById(R.id.divider);
            mHeader = (TextView) rootView.findViewById(R.id.header);

            mTrailerButton = (Button) rootView.findViewById(R.id.trailer_button);
            mTrailerButton.setOnClickListener(this);

            mReviewButton = (Button) rootView.findViewById(R.id.review_button);
            mReviewButton.setOnClickListener(this);
        }
        return rootView;
    }

    @Override
    public void onClick(View v){
        Fragment fragment = null;
        Bundle args = new Bundle();
        switch (v.getId()){
            case R.id.trailer_button: {
                args.putParcelable(TrailerTab.TRAILER_URI, mUri);
                fragment = new TrailerTab();
                break;
            }
            case R.id.review_button: {
                args.putParcelable(ReviewTab.REVIEW_URI, mUri);
                fragment = new ReviewTab();
                break;
            }
            case R.id.favorite_button_tab:{
                changeFavorites(MainActivity.getPaneMode());
            }
        }
        if (fragment != null) {
            fragment.setArguments(args);
            replaceFragment(fragment);
        }
    }

    public void replaceFragment (Fragment fragment){
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.movie_detail_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    public void onSortingOrderChanged(){
        Uri uri = mUri;
        if(null != uri){
            long id = MovieContract.MovieEntry.getMovieIdFromUri(uri);
            mUri = MovieContract.MovieEntry.buildMovieUriWithId(id);
            getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args){
        Log.v(LOG_TAG, "loader created");

        if (null!= mUri){
            return new CursorLoader(getActivity(),
                    mUri,
                    DETAIL_COLUMNS,
                    null,
                    null,
                    null);
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        String baseUrl = "http://image.tmdb.org/t/p/w185/";
        Log.v(LOG_TAG, data.toString());

        if (!data.moveToFirst()) {return;}

        mMovieTitle.setText(data.getString(COL_MOVIE_TITLE));
        mMovieRelease.setText(data.getString(COL_MOVIE_RELEASE));
        double userRating = data.getDouble(COL_MOVIE_RATING);
        String userReviews = String.format("%.1f", userRating) + "/10 ";
        mMovieRating.setText(userReviews);
        String overView = data.getString(COL_MOVIE_OVERVIEW);
        mMovieOverview.setText(overView);
        String movieposter = data.getString(COL_MOVIE_IMG);
        Picasso.with(getContext()).load(baseUrl + movieposter).into(mMoviePoster);

        //mIsFavorite = data.getInt(COL_IS_FAVORITE);

        MovieId = data.getLong(COL_MOVIE_URI_ID);

        if(MainActivity.getPaneMode()){
            int iconType = (getState(MovieId, getContext()) == 1) ? R.drawable.ic_favorite_black_48dp : R.drawable.ic_favorite_border_black_48dp;
            mImageButton.setImageResource(iconType);

            int[] attr = new int[] {android.R.attr.listDivider};
            TypedArray ta = getContext().obtainStyledAttributes(attr);
            mDivider.setBackground(ta.getDrawable(0));
            ta.recycle();

            mHeader.setText("TRAILERS AND REVIEWS");

            mTrailerButton.setText("TRAILERS");
            mTrailerButton.setVisibility(View.VISIBLE);

            mReviewButton.setText("REVIEWS");
            mReviewButton.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
        //mDetailCursor = null;
    }

    private void changeFavorites(boolean paneType) {
        ActionMenuItemView item = (ActionMenuItemView) getActivity().findViewById(R.id.favorite_button);
        ImageButton imageButton = (ImageButton) getActivity().findViewById(R.id.favorite_button_tab);
        ContentValues contentValues = new ContentValues();
        if (mIsFavorite == 1) {
            contentValues.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, 0);
            getContext().getContentResolver().update(
                    MovieContract.MovieEntry.CONTENT_URI,
                    contentValues,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{Long.toString(MovieId)}
            );
            if(paneType) {
                imageButton.setImageResource(R.drawable.ic_favorite_border_black_48dp);
            } else {
                item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_border_white_36dp));
            }
            setState(0, MovieId);
            Toast.makeText(getContext(), "Remove from Favorite", Toast.LENGTH_SHORT).show();
        }
        else {
            contentValues.put(MovieContract.MovieEntry.COLUMN_IS_FAVORITE, 1);
            getContext().getContentResolver().update(
                    MovieContract.MovieEntry.CONTENT_URI,
                    contentValues,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = ?",
                    new String[]{Long.toString(MovieId)}
            );
            Log.v(LOG_TAG, MovieContract.MovieEntry.CONTENT_URI.toString());
            if(paneType) {
                imageButton.setImageResource(R.drawable.ic_favorite_black_48dp);
            } else {
                item.setIcon(getResources().getDrawable(R.drawable.ic_favorite_white_36dp));
            }
            setState(1, MovieId);
            Toast.makeText(getContext(), "Set as Favorite", Toast.LENGTH_SHORT).show();
        }
    }

    private void setState(int isFavorite, long movieId){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                "Favorite", Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String key = "State" +Long.toString(movieId);
        editor.putInt(key, isFavorite);
        editor.apply();
    }
}
