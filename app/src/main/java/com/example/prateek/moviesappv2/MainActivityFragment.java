package com.example.prateek.moviesappv2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.prateek.moviesappv2.data.MovieContract;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    //private MovieMain movieMain;
    private static final int CURSOR_LOADER_ID = 0;
    private static final String[] MOVIE_COLMNS = {
            MovieContract.MovieEntry.TABLE_NAME + "." + MovieContract.MovieEntry._ID,
            MovieContract.MovieEntry.COLUMN_TITLE,
            MovieContract.MovieEntry.COLUMN_MOVIE_IMG
    };
    private static final int COL_MOVIE_ID = 0;
    private static final int COL_MOVIE_TITLE = 1;
    private static final int COL_MOVIE_POSTER = 2;
    private String LOG = MainActivityFragment.class.getName();
    private CustomAdapter customAdapter;
    private GridView gridView;
    private int mPostiton = GridView.INVALID_POSITION;

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        final View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        customAdapter = new CustomAdapter(getActivity(), null, 0 , CURSOR_LOADER_ID);
        gridView = (GridView) rootView.findViewById(R.id.gridview);
        gridView.setAdapter(customAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Cursor cursor = (Cursor) parent.getItemAtPosition(position);
                if (cursor != null) {
                    Intent intent = new Intent(getActivity(),MovieDetailActivity.class)
                            .setData(MovieContract.MovieEntry.buildMovieUri(cursor.getInt(COL_MOVIE_ID)));
                    startActivity(intent);
//                    ((Callback) getActivity()).onItemSelected(MovieContract.MovieEntry
//                            .buildMovieUri(cursor.getInt(COL_MOVIE_ID)));
                }
                mPostiton = position;
            }
        });

    return rootView;
    }

    @Override
    public void onStart() {
        Log.v(LOG, "This is onStart");
        super.onStart();

        FetchMovieData fetchMovieData = new FetchMovieData(getContext());
        getLoaderManager().restartLoader(CURSOR_LOADER_ID, null, this);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String url = prefs.getString(getString(R.string.pref_key),
                getString(R.string.pref_default_name));
        Log.v(LOG,"DATA "+url);
        fetchMovieData.execute(url);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState){
//        Cursor c =
//                getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
//                        new String[]{MovieContract.MovieEntry._ID},
//                        null,
//                        null,
//                        null);
//        if (c.getCount() == 0){
//            insertMovieData();
//        }
        super.onActivityCreated(savedInstanceState);
        getLoaderManager().initLoader(CURSOR_LOADER_ID, null, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle){
      return new CursorLoader(getActivity(), MovieContract.MovieEntry.CONTENT_URI,
              MOVIE_COLMNS,
              null,
              null,
              null);
    }

//    public void insertMovieData(){
//
//        ContentValues movieVlaues = new ContentValues();
//        movieVlaues.put(MovieContract.MovieEntry.COLUMN_DATE, movieMain.movie_release );
//        movieVlaues.put(MovieContract.MovieEntry.COLUMN_MOVIE_IMG, movieMain.img_url);
//        movieVlaues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, movieMain.synopsis);
//        movieVlaues.put(MovieContract.MovieEntry.COLUMN_RATINGS,movieMain.rating);
//        movieVlaues.put(MovieContract.MovieEntry.COLUMN_TITLE, movieMain.O_title);
//
//        Uri insertedUri = getContext().getContentResolver().insert(
//                MovieContract.MovieEntry.CONTENT_URI, movieVlaues
//        );
//        ContentUris.parseId(insertedUri);
//
//    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data){
        customAdapter.swapCursor(data);
        if(mPostiton != GridView.INVALID_POSITION){
            gridView.smoothScrollToPosition(mPostiton);
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader){
     customAdapter.swapCursor(null);
    }

    public interface Callback{
        public void onItemSelected(Uri dateUri);
    }

//        protected void onPostExecute(MovieMain[] result) {
//
//
//            if (result != null) {
//
//                customAdapter.clear();
//
//                   for (MovieMain show : result) {
//                       customAdapter.add(show);
//                       customAdapter.notifyDataSetChanged();
//                    }
//            }
//        }
//    }
}