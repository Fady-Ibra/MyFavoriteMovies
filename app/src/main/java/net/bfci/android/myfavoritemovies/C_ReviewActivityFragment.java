package net.bfci.android.myfavoritemovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class C_ReviewActivityFragment extends Fragment {

    public static ArrayList<String> reviewArrayList;
    public static ArrayAdapter<String> reviewArrayAdapter;
    private static final String KEY = "reviewArrayList";

    public C_ReviewActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.c_fragment_review, container, false);
        ListView listView = (ListView) rootView.findViewById(R.id.listview_review);
        reviewArrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                R.layout.c_list_item,
                R.id.review_content_textView,
                reviewArrayList);
        listView.setAdapter(reviewArrayAdapter);
        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY)) {
            reviewArrayList = savedInstanceState.getStringArrayList(KEY);
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
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(KEY, reviewArrayList);
        super.onSaveInstanceState(outState);
    }
}
