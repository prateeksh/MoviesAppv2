package com.example.prateek.moviesappv2;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import com.example.prateek.moviesappv2.data.MovieContract;
import com.example.prateek.moviesappv2.data.MovieDbHelper;

import java.util.HashSet;

/**
 * Created by Prateek on 16-06-2016.
 */
public class TestDb extends AndroidTestCase {

    public static final String LOG_TAG = TestDb.class.getSimpleName();

    void deleteTheDatabase(){
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    public void setUp(){
        deleteTheDatabase();
    }

    public void testCreateDb() throws Throwable{

        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.MovieEntry.TABLE_NAME);

        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
        SQLiteDatabase db = new MovieDbHelper(
                this.mContext).getWritableDatabase();
        assertEquals(true,db.isOpen());

        //Have we created the table we want?
        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type = 'table'", null);
        assertTrue("Error: This means that databse has not been created correctly", c.moveToFirst());

        //Verify that the table have been created
        do{
            tableNameHashSet.remove(c.getString(0));
        }while (c.moveToNext());

        //if this fails, it neans that your databse doesn't contains entry in the table

        assertTrue("Error: Your database is created without any entry in the tables", tableNameHashSet.isEmpty());

        //now, do our tables contain the correct columns?

        c = db.rawQuery("PRAGMA table_info("+ MovieContract.MovieEntry.TABLE_NAME +")", null);

        assertTrue("Error: This means that we were unable to query the databse for table information." ,c.moveToFirst());

        //Build a HashSet of all the column names we want to look for
        final HashSet<String> movieColumnHashSet = new HashSet<String>();
        movieColumnHashSet.add(MovieContract.MovieEntry._ID);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_DATE);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_MOVIE_IMG);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_OVERVIEW);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_RATINGS);
        movieColumnHashSet.add(MovieContract.MovieEntry.COLUMN_TITLE);

        int columnNameIndex = c.getColumnIndex("name");
        do{
            String columnName = c.getString(columnNameIndex);
            movieColumnHashSet.remove(columnName);
        }while(c.moveToNext());

        // if this fails, it means that your database doesn't contain all of the required location
        // entry columns
        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                movieColumnHashSet.isEmpty());
        db.close();
    }
//    public testMovieTable(){
//
//    }
}
