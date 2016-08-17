package com.example.prateek.moviesappv2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.facebook.stetho.Stetho;

public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback {

    private static final String MAINACTIVITYFRAGMENT_TAG = "MAFTAG";
    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static boolean sTwoPane;
    private String mSortOrder;

    public static boolean getPaneMode() {
        return sTwoPane;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mSortOrder = Utility.getPreferedSorting(this);
        super.onCreate(savedInstanceState);
        Stetho.initializeWithDefaults(this);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.movie_detail_container) != null){
            sTwoPane = true;
            if (savedInstanceState == null){
                getSupportFragmentManager().beginTransaction()
                        .replace(R.id.movie_detail_container, new MovieDetailActivityFragment(), MAINACTIVITYFRAGMENT_TAG)
                        .commit();
            }
        }else{
            Log.v(LOG_TAG, "no twopane");
            sTwoPane = false;
        }
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        String sortOrder = Utility.getPreferedSorting(this);
        //update the sorting order in our second pane using the fragment manager
        if (sortOrder != null && !sortOrder.equals(mSortOrder)) {
            MainActivityFragment maf = (MainActivityFragment)getSupportFragmentManager().findFragmentById(R.id.fragment);
            if ( null != maf ) {
                maf.onSortingOrderChanged();
            }
            MovieDetailActivityFragment df = (MovieDetailActivityFragment)getSupportFragmentManager().findFragmentByTag(MAINACTIVITYFRAGMENT_TAG);
            if ( null != df ) {
                df.onSortingOrderChanged();
            }
        }
        mSortOrder = sortOrder;
        super.onResume();
    }

    @Override
    public void onItemSelected(Uri contentUri){

        if(sTwoPane){
            Bundle args = new Bundle();
            Log.v(LOG_TAG,args.toString());
            args.putParcelable(MovieDetailActivityFragment.DETAIL_URI, contentUri);

            MovieDetailActivityFragment fragment = new MovieDetailActivityFragment();
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment, MAINACTIVITYFRAGMENT_TAG)
                    .commit();
        }else{
            Intent intent = new Intent(this, MovieDetailActivity.class)
                    .setData(contentUri);
            startActivity(intent);
        }
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }
    }
}
