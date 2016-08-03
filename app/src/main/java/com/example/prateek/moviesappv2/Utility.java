package com.example.prateek.moviesappv2;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by Prateek on 03-08-2016.
 */
public class Utility {
    public static String getPreferedSorting(Context context){
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_sort_key),(context.getString(R.string.pref_default_value)));
    }
}
