package com.example.android.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;


/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private static LinkedHashMap<String,String> movieIDPostersMap = new LinkedHashMap<>();
    // references to poster images
    private static List<String> posterImagesURIS = new ArrayList<>();
    private static List<String> moviesIDs = new ArrayList<>();
    private GridView gridview;
    public static final String KEY_PREF_SORT_ORDER = "pref_sortOrder";


    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridview = (GridView) rootView.findViewById(R.id.movie_poster_grid);


        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long id) {
                String movieID = moviesIDs.get(position);
                Intent detailIntent = new Intent(getActivity(),MovieDetails.class)
                                .putExtra("movieID", movieID);
                startActivity(detailIntent);
            }
        });


        return rootView;

    }


    public class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return posterImagesURIS.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView; //= (ImageView) mContext.findViewById(R.id.picture);
            if (convertView == null) {


                imageView = new ImageView(mContext);
/*                imageView.setLayoutParams(new GridView.LayoutParams(
                        (int)mContext.getResources().getDimension(R.dimen.poster_width),
                        (int)mContext.getResources().getDimension(R.dimen.poster_height)));*/
                imageView.setAdjustViewBounds(true);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            } else {
                imageView = (ImageView) convertView;
            }

            String url = posterImagesURIS.get(position);

            Picasso.with(getActivity()).load(url).into(imageView);
            return imageView;
        }

    }


    private void updateMovies() {
        FetchMoviesTask moviesTask = new FetchMoviesTask();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sort_movies_key),
                "");
        Log.v("Sort Order", sortOrder);
        moviesTask.execute(sortOrder);
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovies();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateMovies();
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, LinkedHashMap> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();
        private Uri.Builder builtUri = new Uri.Builder();

         /**
         * Take the String representing the complete movie query in JSON Format and
         * pull out the data we need to construct the Strings needed for the wireframes.
         *
         * Fortunately parsing is easy:  constructor takes the JSON string and converts it
         * into an Object hierarchy for us.
         */
        private LinkedHashMap getMovieIDFromJson(String moviesJsonStr)
                throws JSONException {

            //String[] resultStrs = new String[22];
            // These are the names of the JSON objects that need to be extracted.
            final String MD_RESULTS = "results";
            final String MD_ID = "id";
            final String MD_POSTER = "poster_path";

            JSONObject moviesJson = new JSONObject(moviesJsonStr);
            JSONArray resultsArray = moviesJson.getJSONArray(MD_RESULTS);

            movieIDPostersMap.clear();


            for(int i = 0; i < resultsArray.length(); i++) {

                // Get the JSON object representing the movie
                JSONObject movieData = resultsArray.getJSONObject(i);


                // Get the ID of the movie
                String movieID = movieData.getString(MD_ID);

                //Buil URI of the Poster image
                String posterPath = movieData.getString(MD_POSTER);

                final String MOVIES_BASE_URL =
                        "http://image.tmdb.org/t/p/w185/";

                String moviePosterURL = MOVIES_BASE_URL + posterPath;


                movieIDPostersMap.put(movieID, moviePosterURL);
            }
            Log.v("Movie Posters", movieIDPostersMap.toString());
            return movieIDPostersMap;

        }

        @Override
        protected LinkedHashMap doInBackground(String... params) {

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
                        "http://api.themoviedb.org/3/discover/movie?";
                final String SORT_PARAM = "sort_by";
                final String KEY_PARAM = "api_key";

                Uri builtUri = Uri.parse(MOVIES_BASE_URL).buildUpon()
                        .appendQueryParameter(SORT_PARAM, params[0])
                        .appendQueryParameter(KEY_PARAM, apiKey)
                        .build();

                URL url = new URL(builtUri.toString());
                Log.v("API URL", url.toString());

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
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                moviesJsonStr = buffer.toString();
                Log.v("Movies JSON", moviesJsonStr);


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
                return getMovieIDFromJson(moviesJsonStr);
            } catch (JSONException e) {
                Log.e(LOG_TAG, e.getMessage(), e);
                e.printStackTrace();
            }

            // This will only happen if there was an error getting or parsing the forecast.
            return null;
        }

        @Override
        protected void onPostExecute(LinkedHashMap result) {
            if (result != null) {
                moviesIDs.clear();
                moviesIDs.addAll(movieIDPostersMap.keySet());
                posterImagesURIS.clear();
                posterImagesURIS.addAll(movieIDPostersMap.values());
                Log.v("Movies IDs", moviesIDs.toString());
                gridview.setAdapter(new ImageAdapter(getActivity()));
                }

                // New data is back from the server.  Hooray!

        }
    }

}