package com.example.prateek.moviesappv2;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Prateek on 13-08-2016.
 */
public class PagerAdapter extends FragmentStatePagerAdapter {

    int mNumOfTabs;
    Uri mUri;

    public PagerAdapter(FragmentManager fm, int numOfTabs, Uri data) {
        super(fm);
        this.mNumOfTabs = numOfTabs;
        this.mUri = data;
    }

    @Override
    public Fragment getItem(int position) {
        Bundle arguments = new Bundle();
        switch (position) {
            case 0: {
                arguments.putParcelable(MovieDetailActivityFragment.DETAIL_URI,  mUri);
                MovieDetailActivityFragment descTab = new MovieDetailActivityFragment();
                descTab.setArguments(arguments);
                return descTab;
            }
            case 1: {
                arguments.putParcelable(TrailerTab.TRAILER_URI, mUri);
                TrailerTab trailerTab = new TrailerTab();
                trailerTab.setArguments(arguments);
                return trailerTab;
            }
            case 2: {
                arguments.putParcelable(ReviewTab.REVIEW_URI, mUri);
                ReviewTab reviewTab = new ReviewTab();
                reviewTab.setArguments(arguments);
                return reviewTab;
            }
            default: {
                return null;
            }
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
