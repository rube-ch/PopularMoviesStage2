package com.rubech.udacity.popularmovies.data;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by ruben on 28/12/15.
 */
public class MoviesProvider extends ContentProvider {

    private static final String LOG_TAG = MoviesProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();
    private MoviesDBHelper mOpenHelper;

    // Codes for the UriMatcher //////
    private static final int MOVIE = 100;
    private static final int MOVIE_WITH_ID = 101;
    private static final int TRAILER_WITH_MOVIE = 200;
    private static final int REVIEW_WITH_MOVIE = 300;


    private static UriMatcher buildUriMatcher(){
        // Build a UriMatcher by adding a specific code to return based on a match
        // It's common to use NO_MATCH as the code for this case.
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MoviesContract.CONTENT_AUTHORITY;

        // add a code for each type of URI you want
        matcher.addURI(authority, MoviesContract.MovieEntry.TABLE_NAME, MOVIE);
        matcher.addURI(authority, MoviesContract.MovieEntry.TABLE_NAME + "/#", MOVIE_WITH_ID);
        matcher.addURI(authority, MoviesContract.TrailerEntry.TABLE_NAME + "/*", TRAILER_WITH_MOVIE);
        matcher.addURI(authority, MoviesContract.ReviewEntry.TABLE_NAME + "/*", REVIEW_WITH_MOVIE);

        return matcher;
    }

    @Override
    public boolean onCreate() {

        mOpenHelper = new MoviesDBHelper(getContext());

        return true;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings1, String s1) {
        return null;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE:{
                return MoviesContract.MovieEntry.CONTENT_TYPE;
            }
            case MOVIE_WITH_ID:{
                return MoviesContract.MovieEntry.CONTENT_ITEM_TYPE;
            }
            case TRAILER_WITH_MOVIE:{
                return MoviesContract.TrailerEntry.CONTENT_TYPE;
            }
            case REVIEW_WITH_MOVIE:{
                return MoviesContract.ReviewEntry.CONTENT_TYPE;
            }
            default:{
                throw new UnsupportedOperationException("Unknown uri: " + uri);
            }
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        return 0;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        return 0;
    }
}
