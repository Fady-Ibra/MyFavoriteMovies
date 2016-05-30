package net.bfci.android.myfavoritemovies;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A placeholder fragment containing a simple view.
 */
public class B_DetailActivityFragment extends Fragment {

    public B_DetailActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.b_fragment_detail, container, false);
        Intent intent = getActivity().getIntent();
        if (intent != null && intent.hasExtra(Intent.EXTRA_TEXT)) {
            String movyInfoStr = intent.getStringExtra(Intent.EXTRA_TEXT);

            try {
                JSONObject movyInfoJson = new JSONObject(movyInfoStr);
                String movieId = movyInfoJson.getString(Z_Constants.MOVY_ID_PARAM_NAME);

                ((TextView) rootView.findViewById(R.id.movy_detail_title_textview))
                        .setText(movyInfoJson.getString(Z_Constants.ORIGINAL_TITLE_PARAM_NAME));
                ((TextView) rootView.findViewById(R.id.movy_detail_overview_textview))
                        .setText(movyInfoJson.getString(Z_Constants.OVERVIEW_PARAM_NAME));
                ((TextView) rootView.findViewById(R.id.movy_detail_date_textview))
                        .setText(movyInfoJson.getString(Z_Constants.RELEASE_DATE_PARAM_NAME));
                ((TextView) rootView.findViewById(R.id.movy_detail_rating_textview))
                        .setText(movyInfoJson.getString(Z_Constants.VOTE_AVERAGE_PARAM_NAME) + " from "
                                + movyInfoJson.getString(Z_Constants.VOTE_COUNT_PARAM_NAME) + " voters");

                Picasso
                        .with(getActivity())
                        .load(Uri
                                .parse(Z_Constants.IMG_BASE_URL)
                                .buildUpon().
                                appendEncodedPath(movyInfoJson.getString(Z_Constants.IMG_PATH_PARAM_NAME))
                                .build()
                                .toString())
                        .into((ImageView) rootView.findViewById(R.id.movy_detail_thumbnail_imageview));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return rootView;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_settings) {
            startActivity(new Intent(getActivity(), C_SettingsActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);

    }
}
