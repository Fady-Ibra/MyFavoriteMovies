/**
 * Created by fady on 5/24/16.
 */

package net.bfci.android.myfavoritemovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.Arrays;

public class Z_FetchMovyExtraTask extends AsyncTask<String, Void, String[][]> {

    private static final String LOG_TAG = Z_FetchMovyExtraTask.class.getSimpleName();
    private B_DetailActivityFragment detailActivityFragment;
    private String movyId;

    public Z_FetchMovyExtraTask(B_DetailActivityFragment b_detailActivityFragment) {
        super();
        detailActivityFragment= b_detailActivityFragment;
    }

    @Override
    protected String[][] doInBackground(String... params) {

        movyId = params[0]!=null ? params[0] : X_Constants.DEFAULT_MOVY_ID;

        // Get array of strings for Movy Extra Data
        String movyVideosJsonString = getMovyDataAsStr(X_Constants.VIDEOS);
        String movyReviewsJsonString = getMovyDataAsStr(X_Constants.REVIEWS);
        try {
            return new String[][]{
                    getMoviesDataExtraFromJson(new JSONObject(movyVideosJsonString)
                            .getJSONArray(X_Constants.RESULTS_PARAM_NAME)),
                    getMoviesDataExtraFromJson(new JSONObject(movyReviewsJsonString)
                            .getJSONArray(X_Constants.RESULTS_PARAM_NAME))
            };
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage(), e);
            e.printStackTrace();
        }
        // This will only happen if there was an error getting or parsing the forecast.
        return null;
    }

    private String getMovyDataAsStr(String relativePath){
        // These two need to be declared outside the try/catch
        // so that they can be closed in the finally block.
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        try {
            // Construct the URL for the themoviedb.org query
            String apiUrlUsed = X_Constants.API_BASE_URL+movyId+ relativePath;
            Uri builtUri = Uri.parse(apiUrlUsed).buildUpon()
                    .appendQueryParameter(X_Constants.APP_KEY_PARAM_NAME, X_Constants.API_KEY)
                    .build();
            URL url = new URL(builtUri.toString());

            // Create the request to TheMovieDb, and open the connection
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

            return buffer.toString();

        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the moviedb data, there's no point in attempting
            // to parse it.
            return null;
        } finally{
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
    }

    private String[] getMoviesDataExtraFromJson(JSONArray movyExtraJsonArray) throws JSONException{
        String[] movyStrArray = new String[movyExtraJsonArray.length()];
        for (int i=0;i<movyExtraJsonArray.length();i++){
            movyStrArray[i] = movyExtraJsonArray.getJSONObject(i).toString();
        }
        return movyStrArray;
    }

    @Override
    protected void onPostExecute(String[][] movyExtraStrArray) {
        //super.onPostExecute(strings);
        if (movyExtraStrArray != null) {

            // Prepares videos data as array of strings
            String[] videoFullPathStrArray = new String[movyExtraStrArray[0].length];
            for(int i = 0; i<movyExtraStrArray[0].length; i++){
                try {
                    videoFullPathStrArray[i]=Uri.parse(X_Constants.YOUTUBE_BASE_URL).buildUpon()
                            .appendQueryParameter(
                                    X_Constants.YOUTUBE_VIDEO_PARAM_NAME,
                                    new JSONObject(movyExtraStrArray[0][i]).getString(X_Constants.VIDEOS_KEY_PARAM_NAME))
                            .build().toString();
                }  catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
            }
            B_DetailActivityFragment.movyExtraArrayString = videoFullPathStrArray;

            // Prepares reviews data as array of strings
            String[] ReviewsStrArray = new String[movyExtraStrArray[1].length];
            for(int i = 0; i<movyExtraStrArray[1].length; i++){
                try {
                    ReviewsStrArray[i]= new JSONObject(movyExtraStrArray[1][i])
                            .getString(X_Constants.REVIEWS_CONTENT_PARAM_NAME);
                }  catch (JSONException e) {
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }

            }
            C_ReviewActivityFragment.reviewArrayList = new ArrayList<String>(Arrays.asList(ReviewsStrArray));

        }
        // New data is back from the server.
    }

}