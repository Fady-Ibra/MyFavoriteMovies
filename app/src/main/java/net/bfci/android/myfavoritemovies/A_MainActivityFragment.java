package net.bfci.android.myfavoritemovies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class A_MainActivityFragment extends Fragment {

    private Z_MovyArrayAdapter movyArrayAdapter;
    public static ArrayList<String> movyArrayList;

    Z_MovyArrayAdapter getMovyArrayAdapter(){
        return movyArrayAdapter;
    }

    public A_MainActivityFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.a_fragment_main, container, false);
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_poster);

        movyArrayAdapter = new Z_MovyArrayAdapter(getActivity(), new ArrayList<String>());
        gridView.setAdapter(movyArrayAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), B_DetailActivity.class)
                        .putExtra(Intent.EXTRA_TEXT, movyArrayList.get(position));
                startActivity(intent);
            }
        });

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

    @Override
    public void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = preferences.getString(
                getString(R.string.pref_sort_movy_key),
                getString(R.string.pref_sort_movy_default));
        Z_FetchMovyTask fetchMovyTask = new Z_FetchMovyTask(this);
        fetchMovyTask.execute(sort);
    }

}
