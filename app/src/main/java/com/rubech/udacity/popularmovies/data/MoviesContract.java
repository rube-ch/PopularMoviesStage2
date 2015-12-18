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
    public static final String PATH_FILTER = "filter";
    public static final String PATH_TRAILER = "trailer";
    public static final String PATH_REVIEW = "review";

    /* Inner class that defines the table contents of the filter table
    * We define this table to have a local representation of the two options of the API:
    * Popularity and highest rated
    * And addtionally to have the list of Favorite movies of the user
    * */
    public static final class FilterEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_FILTER).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILTER;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_FILTER;

        // Table name
        public static final String TABLE_NAME = "filter";

        // The filter name string is what will be sent to movie db
        // as the sorting query.
        public static final String COLUMN_FILTER_NAME = "filter_name";

        public static Uri buildFilterUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }

    /* Inner class that defines the table contents of the movies table
    * There is a reference to the API filter and the order of the movie in this filter
    */
    public static final class MovieEntry implements BaseColumns {

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        // Table name
        public static final String TABLE_NAME = "movie";

        // Column with the foreign key into the filter table.
        public static final String COLUMN_FILTER_KEY = "filter_id";

        // Column with the order of the movie in the filter.
        public static final String COLUMN_FILTER_ORDER = "filter_order";

        //Column with the movie ID of the moviedb API, to query trailers and reviews
        public static final String COLUMN_MOVIE = "movie_id";

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
