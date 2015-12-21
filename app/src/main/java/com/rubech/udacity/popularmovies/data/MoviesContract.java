package com.rubech.udacity.popularmovies.data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by ruben on 18/12/15.
 * Defines table and column names for the movies database.
 */
public class MoviesContract {

    public static final String CONTENT_AUTHORITY = "com.rubech.udacity.popularmovies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String PATH_MOVIE = "movie";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "review";


    /* Inner class that defines the table contents of the movies table
    * The table _ID refers to the API ID
    **/
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Table name
        public static final String TABLE_NAME = "movie";

        // Column with the URL of the movie poster.
        public static final String COLUMN_POSTER = "poster";

        // Title of the movie
        public static final String COLUMN_TITLE = "title";

        // Release year of the movie
        public static final String COLUMN_YEAR = "year";

        // Run time of the movie
        public static final String COLUMN_RUNTIME = "runtime";

        // Vote average of the movie
        public static final String COLUMN_VOTES = "vote_average";

        // Overview of the movie
        public static final String COLUMN_OVERVIEW = "overview";

        // Column with index of the movies by popularity.
        public static final String COLUMN_POPULARITY = "popularity_index";

        // Column with index of the movies by user rating
        public static final String COLUMN_RATING = "rating_index";

        // Column with index of the movies by user rating
        public static final String COLUMN_FAVORITES = "favorite";

        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the trailers table */
    public static final class TrailerEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_TRAILER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_TRAILER;

        // Table name
        public static final String TABLE_NAME = "trailer";

        // Column with the foreign key into the movies table.
        public static final String COLUMN_MOVIE_KEY = "movie_id";

        // website hosting the trailer
        public static final String COLUMN_WEBSITE = "website";

        // Name of the trailer
        public static final String COLUMN_NAME = "name";

        // URL of the trailer
        public static final String COLUMN_URL = "url";


        public static Uri buildTrailerUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the reviews table */
    public static final class ReviewEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_REVIEW).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_REVIEW;

        // Table name
        public static final String TABLE_NAME = "review";

        // Column with the foreign key into the movies table.
        public static final String COLUMN_MOVIE_KEY = "movie_id";

        // Author of the review
        public static final String COLUMN_AUTHOR = "website";

        // Content of the review
        public static final String COLUMN_CONTENT = "content";

        // URL of the review
        public static final String COLUMN_URL = "url";


        public static Uri buildReviewUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

}
