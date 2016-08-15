package com.example.prateek.moviesappv2;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.example.prateek.moviesappv2.data.MovieContract;

/**
 * Created by Prateek on 13-08-2016.
 */
public class TrailerTab extends Fragment implements LoaderCallbacks<Cursor>{
    public static final String TRAILER_URI = "TRAILER_URI";
    public static final int COL_MOVIE_TRAILER_KEY = 2;
    static final int COL_ID = 0;
    static final int COL_MOVIE_TRAILER_ID = 1;

    private static final String BASE_URL = "https://www.youtube.com/watch?v=";
    private static final int TRAILER_LOADER = 0;

    private static final String[] TRAILER_COLUMNS = {
            MovieContract.MovieTrailerEntry.TABLE_NAME + "." + MovieContract.MovieTrailerEntry._ID,
            MovieContract.MovieTrailerEntry.TABLE_NAME + "." + MovieContract.MovieTrailerEntry.COLUMN_ID,
            MovieContract.MovieTrailerEntry.TABLE_NAME + "." + MovieContract.MovieTrailerEntry.COLUMN_KEY
    };
    private static int height;
    private static int width;
    GridView mTrailers;
    private TrailersAdapter mTrailerAdapter;
    private Uri mUri;

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Bundle arguments = getArguments();
        if (arguments != null) {
            mUri = arguments.getParcelable(TrailerTab.TRAILER_URI);
        }
        final View rootView = inflater.inflate(R.layout.list_trailers, container, false);

        mTrailerAdapter = new TrailersAdapter(getActivity(), null, 0);

        mTrailers = (GridView) rootView.findViewById(R.id.trailer_grid);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                mTrailers.setAdapter(mTrailerAdapter);

                mTrailers.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                        Cursor cursor = (Cursor) adapterView.getItemAtPosition(position);
                        if (cursor != null) {
                            String videoUrl = cursor.getString(COL_MOVIE_TRAILER_KEY);
                            if (videoUrl != null) {
                                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(BASE_URL + videoUrl));
                                startActivity(intent);
                            } else {
                                Toast.makeText(getContext(), "No Trailer Available.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                width = rootView.getMeasuredWidth();
                height = rootView.getMeasuredHeight();

                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
            }
        });



        mTrailers.setOnTouchListener(new View.OnTouchListener() {
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


        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        getLoaderManager().initLoader(TRAILER_LOADER, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        if (mUri != null) {
            long movieId = MovieContract.MovieEntry.getMovieIdFromUri(mUri);
            return new CursorLoader(
                    getActivity(),
                    MovieContract.MovieTrailerEntry.CONTENT_URI,
                    TRAILER_COLUMNS,
                    MovieContract.MovieTrailerEntry.COLUMN_MOVIE_ID + " = ? ",
                    new String[]{Long.toString(movieId)},
                    null
            );
        }
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mTrailerAdapter.swapCursor(data);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mTrailerAdapter.swapCursor(null);
    }

}