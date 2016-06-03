package net.bfci.android.myfavoritemovies;

import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class B_DetailActivityFragment extends Fragment {

    public static String[] movyExtraArrayString;
    private static final String LOG_TAG = B_DetailActivity.class.getSimpleName();
    private static final String KEY = "movyExtraArrayString";
    private String movieId;

    public B_DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.b_fragment_detail, container, false);

        String movyInfoStr = "{}";
        /*Bundle bundle = getArguments();
        Intent intent = getActivity().getIntent();
        if(A_MainActivity.isTablet && bundle!=null){
            movyInfoStr = bundle.getString(X_Constants.BUNDLE_DATA);
        }else if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            movyInfoStr = intent.getStringExtra(Intent.EXTRA_TEXT);
        }*/
        movyInfoStr = A_MainActivityFragment.movyArrayList.get(A_MainActivityFragment.mPosition);
        try {
            JSONObject movyInfoJson = new JSONObject(movyInfoStr);
            movieId = movyInfoJson.getString(X_Constants.MOVY_ID_PARAM_NAME);

            ((TextView) rootView.findViewById(R.id.movy_detail_title_textview))
                    .setText(movyInfoJson.getString(X_Constants.ORIGINAL_TITLE_PARAM_NAME));
            ((TextView) rootView.findViewById(R.id.movy_detail_overview_textview))
                    .setText(movyInfoJson.getString(X_Constants.OVERVIEW_PARAM_NAME));
            ((TextView) rootView.findViewById(R.id.movy_detail_date_textview))
                    .setText(movyInfoJson.getString(X_Constants.RELEASE_DATE_PARAM_NAME));
            ((TextView) rootView.findViewById(R.id.movy_detail_rating_textview))
                    .setText(movyInfoJson.getString(X_Constants.VOTE_AVERAGE_PARAM_NAME) + "/10 ("
                            + movyInfoJson.getString(X_Constants.VOTE_COUNT_PARAM_NAME) + " voters)");

            Picasso
                    .with(getActivity())
                    .load(Uri
                            .parse(X_Constants.IMG_BASE_URL)
                            .buildUpon().
                                    appendEncodedPath(movyInfoJson.getString(X_Constants.IMG_PATH_PARAM_NAME))
                            .build()
                            .toString())
                    .into((ImageView) rootView.findViewById(R.id.movy_detail_thumbnail_imageview));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Button trailerBtn = (Button) rootView.findViewById(R.id.movy_detail_trailer_button);
        trailerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTrailer(movieId);
            }
        });

        Button reviewBtn = (Button) rootView.findViewById(R.id.movy_detail_review_button);
        reviewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showReview(movieId);
            }
        });

        Button favoriteBtn = (Button) rootView.findViewById(R.id.movy_detail_favorite_button);
        favoriteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    addToFavorite(movieId);
                }catch (JSONException e){
                    Log.e(LOG_TAG, e.getMessage(), e);
                    e.printStackTrace();
                }
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY)) {
            movyExtraArrayString = savedInstanceState.getStringArray(KEY);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        if(!A_MainActivity.isTablet){
            inflater.inflate(R.menu.menu_main, menu);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(getActivity(), E_SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public void onStart() {
        super.onStart();
        Z_FetchMovyExtraTask fetchMovyExtraTask = new Z_FetchMovyExtraTask(this);
        fetchMovyExtraTask.execute(movieId);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArray(KEY, movyExtraArrayString);
        super.onSaveInstanceState(outState);
    }

    private void showTrailer(String movieId){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        intent.setData(Uri.parse(movyExtraArrayString[0]));
        if (intent.resolveActivity(getActivity().getPackageManager()) != null
                && movyExtraArrayString[0] != null) {
            startActivity(intent);
        } else {
            Log.d(LOG_TAG, "Couldn't view " + movyExtraArrayString[0]);
        }
    }

    private void showReview(String movieId){
        startActivity(new Intent(getActivity(), C_ReviewActivity.class));
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void addToFavorite(String movieId) throws JSONException{

        // Get Favorite Preferences and create favorite array and object
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String favorite = preferences.getString(getString(R.string.pref_favorite_key), "[]");
        JSONArray favoriteJsonArray= new JSONArray(favorite);
        JSONObject favoriteJsonObject = new JSONObject(new String("{\"id\": \"" +movieId+ "\"}"));

        // Add or Remove Favorite Movy
        boolean addNewId=true;
        for(int i=0; i < favoriteJsonArray.length(); i++){
            if(favoriteJsonArray.getJSONObject(i).toString().equals(favoriteJsonObject.toString())){
                favoriteJsonArray.remove(i);
                addNewId=false;
            }
        }
        if(addNewId) favoriteJsonArray.put(favoriteJsonObject);

        // Save Favorite Preferences
        SharedPreferences.Editor edit = preferences.edit();
        edit.putString(getString(R.string.pref_favorite_key), favoriteJsonArray.toString());
        edit.commit();

    }
}
