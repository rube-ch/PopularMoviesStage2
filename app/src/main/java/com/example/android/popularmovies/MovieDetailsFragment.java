package com.example.android.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailsFragment extends Fragment {

//    private String movieID = getMovieID();
    private TextView titleTextview;
    private ImageView posterImageView;
    private TextView releaseYearTextView;
    private TextView runtimeTextView;
    private TextView voteAverageTextView;
    private TextView overviewTextview;


    /**
     * Create a new instance of MovieDetailsFragment, initialized to
     * show the text at 'index'.
     */
    public static MovieDetailsFragment newInstance (String movieID) {
        MovieDetailsFragment f = new MovieDetailsFragment();
        // Supply movie ID as an argument.
        Bundle args = new Bundle();
        args.putString("movieID", movieID);
        f.setArguments(args);
        return f;
    }


    public String getMovieID() {
        return getArguments().getString("movieID", "0");
    }


    public MovieDetailsFragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        updateMovieDetails();

        titleTextview = (TextView) rootView.findViewById(R.id.detail_title);
        posterImageView = (ImageView) rootView.findViewById(R.id.detail_poster);
        posterImageView.setAdjustViewBounds(true);
        posterImageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        releaseYearTextView = (TextView) rootView.findViewById(R.id.detail_release_year);
        runtimeTextView = (TextView) rootView.findViewById(R.id.detail_runtime);
        voteAverageTextView = (TextView) rootView.findViewById(R.id.detail_vote_rate);
        overviewTextview = (TextView) rootView.findViewById(R.id.detail_plot);

        return rootView;
    }


    private void updateMovieDetails() {
        FetchMovieDetailsTask moviesTask = new FetchMovieDetailsTask();
        moviesTask.execute(getMovieID());
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        updateMovieDetails();
//    }

    public class FetchMovieDetailsTask extends AsyncTask<String, Void, List<String>> {

        private final String LOG_TAG = FetchMovieDetailsTask.class.getSimpleName();

        /**
         * Take the String representing the complete movie query in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private List<String> getMovieDetailsFromJson(String moviesJsonStr)
                throws JSONException {

            //String[] resultStrs = new String[22];
            // These are the names of the JSON objects that need to be extracted.
            final String MD_TITLE = "original_title";
            final String MD_POSTER = "poster_path";
            final String MD_YEAR = "release_date";
            final String MD_RUNTIME = "runtime";
            final String MD_AVERAGE = "vote_average";
            final String MD_OVERVIEW = "overview";


            JSONObject moviesJson = new JSONObject(moviesJsonStr);

            // Get the the movie title
            String movieTitle = moviesJson.getString(MD_TITLE);

            //Buil URI of the Poster image
            String posterPath = moviesJson.getString(MD_POSTER);

            final String MOVIES_BASE_URL =
                    "http://image.tmdb.org/t/p/w185/";

            String moviePosterURL = MOVIES_BASE_URL + posterPath;

            // Get the release year of the movie
            String movieReleaseYear = moviesJson.getString(MD_YEAR).substring(0,4);

            // Get the runtime of the movie
            String movieRuntime = moviesJson.getString(MD_RUNTIME) + "min";

            // Get the vote average of the movie
            String movieVoteAverage = moviesJson.getString(MD_AVERAGE) + "/10";

            String movieOverview = moviesJson.getString(MD_OVERVIEW);


            List<String> movieDetails = new ArrayList<>();



            movieDetails.add(movieTitle);
            movieDetails.add(moviePosterURL);
            movieDetails.add(movieReleaseYear);
            movieDetails.add(movieRuntime);
            movieDetails.add(movieVoteAverage);
            movieDetails.add(movieOverview);

            Log.v("Movie Posters", movieDetails.toString());
            return movieDetails;

        }

        @Override
        protected List<String> doInBackground(String... params) {

            //  Verify size of params.
            if (params.length == 0) {
                return null;
            }

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String moviesJsonStr = null;

            String apiKey = "YourAPIKey";

            try {
                // Construct the URL for the Movie Database query

                final String MOVIES_BASE_URL =
                        "http://api.themoviedb.org/3/movie";
                final String KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendPath(params[0])
                        .appendQueryParameter(KEY_PARAM, apiKey)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v("API Details URL", url.toString());

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                Log.v("Movie Details JSON", moviesJsonStr);


            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            try {
                return getMovieDetailsFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(List<String> result) {
            if (result != null) {
                Log.v("Movie Details", result.toString());
                titleTextview.setText(result.get(0));
                Picasso.with(getActivity()).load(result.get(1)).into(posterImageView);
                releaseYearTextView.setText(result.get(2));
                runtimeTextView.setText(result.get(3));
                voteAverageTextView.setText(result.get(4));
                overviewTextview.setText(result.get(5));





            }

            // New data is back from the server.  Hooray!

        }
    }
}
