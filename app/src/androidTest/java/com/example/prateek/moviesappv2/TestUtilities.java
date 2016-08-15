package com.example.prateek.moviesappv2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.prateek.moviesappv2.data.MovieContract;
import com.example.prateek.moviesappv2.data.MovieDbHelper;

import java.util.Map;
import java.util.Set;

/**
 * Created by Prateek on 20-06-2016.
 */
public class TestUtilities extends AndroidTestCase{

    //static final ;
    static void validateCursor(String error, Cursor valueCursor, ContentValues expectedValues){
        assertTrue("Emoty cursor returned " + error, valueCursor.moveToFirst());
        validateCurrentRecord(error, valueCursor, expectedValues);
        valueCursor.close();
    }
    static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues){
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "'not found. " +error,idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() + "' did not march the expected vlaue '"
            + expectedValue + "'." + error, expectedValue, valueCursor.getString(idx));
        }
    }

   static ContentValues createMovieValues(){
       ContentValues movieValues = new ContentValues();
       movieValues.put(MovieContract.MovieEntry.COLUMN_DATE,2016-06-12);
       movieValues.put(MovieContract.MovieEntry.COLUMN_OVERVIEW, "Good Movies");
       movieValues.put(MovieContract.MovieEntry.COLUMN_USER_RATING,21.001);
       movieValues.put(MovieContract.MovieEntry.COLUMN_TITLE, "Caption America");
       return movieValues;
   }
    static long insetValues(Context context){

        MovieDbHelper dbHelper = new MovieDbHelper(context);
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues testValues = TestUtilities.createMovieValues();
        long locId = db.insert(MovieContract.MovieEntry.TABLE_NAME, null, testValues);
        assertTrue("Error: Failiure to insert values", locId != -1);
        return locId;
    }
}
