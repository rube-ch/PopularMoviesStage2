package com.rubech.udacity.popularmovies.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * Created by ruben on 21/12/15.
 */
public class MoviesDBHelper extends SQLiteOpenHelper {

    public static final String LOG_TAG = MoviesDBHelper.class.getSimpleName();

    //name & version
    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String SQL_CREATE_MOVIE_TABLE = "CREATE TABLE " +
                MoviesContract.MovieEntry.TABLE_NAME + "(" +
                MoviesContract.MovieEntry._ID + " INTEGER PRIMARY KEY, " +
                MoviesContract.MovieEntry.COLUMN_POSTER + " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_TITLE +  " TEXT NOT NULL, " +
                MoviesContract.MovieEntry.COLUMN_YEAR +  " TEXT NOT NULL " +
                MoviesContract.MovieEntry.COLUMN_RUNTIME +  " TEXT NOT NULL " +
                MoviesContract.MovieEntry.COLUMN_VOTES +  " TEXT NOT NULL " +
                MoviesContract.MovieEntry.COLUMN_RUNTIME +  " TEXT NOT NULL " +
                MoviesContract.MovieEntry.COLUMN_OVERVIEW +  " TEXT NOT NULL " +
                MoviesContract.MovieEntry.COLUMN_POPULARITY +  " INT NOT NULL " +
                MoviesContract.MovieEntry.COLUMN_RATING +  " INT NOT NULL " +
                MoviesContract.MovieEntry.COLUMN_FAVORITES +  " INT " +
                ");";

        final String SQL_CREATE_TRAILER_TABLE = "CREATE TABLE " +
                MoviesContract.TrailerEntry.TABLE_NAME + "(" +
                MoviesContract.TrailerEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesContract.TrailerEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL " +
                MoviesContract.TrailerEntry.COLUMN_WEBSITE + " TEXT NOT NULL, " +
                MoviesContract.TrailerEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MoviesContract.TrailerEntry.COLUMN_URL + " TEXT NOT NULL, " +

                // Set up the movie column as a foreign key to movies table.
                " FOREIGN KEY (" + MoviesContract.TrailerEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MoviesContract.MovieEntry.TABLE_NAME + " (" + MoviesContract.MovieEntry._ID + "), " +

                ");";

        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " +
                MoviesContract.ReviewEntry.TABLE_NAME + "(" +
                MoviesContract.ReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MoviesContract.ReviewEntry.COLUMN_MOVIE_KEY + " INTEGER NOT NULL " +
                MoviesContract.ReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                MoviesContract.ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                MoviesContract.ReviewEntry.COLUMN_URL + " TEXT NOT NULL, " +

                // Set up the movie column as a foreign key to movies table.
                " FOREIGN KEY (" + MoviesContract.ReviewEntry.COLUMN_MOVIE_KEY + ") REFERENCES " +
                MoviesContract.MovieEntry.TABLE_NAME + " (" + MoviesContract.MovieEntry._ID + "), " +

                ");";


        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_TRAILER_TABLE);
        sqLiteDatabase.execSQL(SQL_CREATE_REVIEW_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {

        Log.w(LOG_TAG, "Upgrading database from version " + oldVersion + " to " +
                newVersion + ". OLD DATA WILL BE DESTROYED");

        sqLiteDatabase.execSQL("DELETE FROM " + MoviesContract.MovieEntry.TABLE_NAME +
                " WHERE favorite IS NULL;");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.TrailerEntry.TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MoviesContract.ReviewEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
}
