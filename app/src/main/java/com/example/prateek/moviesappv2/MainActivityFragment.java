package com.example.prateek.moviesappv2;

import android.annotation.TargetApi;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.prateek.moviesappv2.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {


     static final int COL_MOVIE_POSTER = 2;
    private static final String[] DETAIL_COLUMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry.COLUMN_MOVIE_IMG,
    };
    private static final int CURSOR_LOADER_ID = 0;
    private static final int COL_MOVIE_ID = 0;
    private static final int COL_MOVIE_URI_ID = 1;
    private static final String SELECTED_KEY = "selected_position";
    private static int height;
    private static int width;
    private String LOG = MainActivityFragment.class.getName();
    private CustomAdapter customAdapter;
    private GridView gridView;
    private View rootView = null;
    private int mPosition = GridView.INVALID_POSITION;

    public MainActivityFragment() {
    }

    public static int getWidth() {
        return width;
    }

    public static int getHeight() {
        return height;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        customAdapter = new CustomAdapter(getActivity(), null, 0);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(customAdapter);

        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onGlobalLayout() {
                if (mPosition != GridView.INVALID_POSITION) {
                    gridView.smoothScrollToPosition(mPosition);
                }

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    long movieId = cursor.getLong(COL_MOVIE_URI_ID);
                    ((Callback) getActivity())
                            .onItemSelected(MovieContract.MovieEntry
                                    .buildMovieUriWithId(movieId));

                }
                mPosition = position;
            }
        });
                rootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                width = rootView.getMeasuredWidth();
                height = rootView.getMeasuredHeight();
            }
        });
        if (savedInstanceState != null && savedInstanceState.containsKey(SELECTED_KEY)) {
            mPosition = savedInstanceState.getInt(SELECTED_KEY);
        }
        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mPosition != GridView.INVALID_POSITION) {
            outState.putInt(SELECTED_KEY, mPosition);
        }
        super.onSaveInstanceState(outState);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle args) {
        String sortOrder = Utility.getPreferedSorting(getActivity());
        String sortBy = null;
        if(sortOrder.equals(MovieContract.MovieEntry.TOP_RATED)) {
            sortBy = MovieContract.MovieEntry.COLUMN_USER_RATING + " DESC";
        }
        else if (sortOrder.equals(MovieContract.MovieEntry.POPULAR)) {
            sortBy = MovieContract.MovieEntry.COLUMN_POPULARITY + " DESC";
        }
        Uri movieBySortOrder = MovieContract.MovieEntry.buildMovieUriWithSortOrder(sortOrder);

        return new CursorLoader(getActivity(),
                movieBySortOrder,
                DETAIL_COLUMNS,
                null,
                null,
                sortBy);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        Log.v(LOG, "This is onStart");
        updateMovieDetail();
        super.onStart();
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        customAdapter.swapCursor(data);

    }

    public void onSortingOrderChanged(){
        updateMovieDetail();
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
     customAdapter.swapCursor(null);
    }

    private void updateMovieDetail(){
        FetchMovieData fetchMovieData = new FetchMovieData(getActivity());
        String sorting_order = Utility.getPreferedSorting(getActivity());
        fetchMovieData.execute(sorting_order);
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
    }

    public interface Callback{
        public void onItemSelected(Uri idUri);
    }
}