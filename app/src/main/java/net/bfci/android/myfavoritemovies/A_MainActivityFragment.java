package net.bfci.android.myfavoritemovies;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * A placeholder fragment containing a simple view.
 */
public class A_MainActivityFragment extends Fragment {

    private Y_MovyArrayAdapter movyArrayAdapter;
    public static ArrayList<String> movyArrayList;
    private static final String KEY_ALL_DATA = "movyArrayList";
    private static final String KEY_IMG_FULL_PATH = "movyArrayListForAdapter";
    private static final String KEY_POSITION = "position";
    public static int mPosition =-1;

    Y_MovyArrayAdapter getMovyArrayAdapter(){
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

        movyArrayAdapter = new Y_MovyArrayAdapter(getActivity(), new ArrayList<String>());
        gridView.setAdapter(movyArrayAdapter);

        Log.v("fady", mPosition+"");
        if (mPosition != ListView.INVALID_POSITION) {
            gridView.smoothScrollToPosition(mPosition);
        }
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if(A_MainActivity.isTablet){
                    Bundle bundle = new Bundle();
                    bundle.putString(X_Constants.BUNDLE_DATA, movyArrayList.get(position));

                    B_DetailActivityFragment detailsActivityFragment = new B_DetailActivityFragment();
                    detailsActivityFragment.setArguments(bundle);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.movy_detail_container, detailsActivityFragment).commit();

                }else {
                    Intent intent = new Intent(getActivity(), B_DetailActivity.class)
                            .putExtra(Intent.EXTRA_TEXT, movyArrayList.get(position));
                    startActivity(intent);
                }
                mPosition=position;
            }
        });

        return rootView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(savedInstanceState != null && savedInstanceState.containsKey(KEY_ALL_DATA)) {
            movyArrayList = savedInstanceState.getStringArrayList(KEY_ALL_DATA);
            mPosition = savedInstanceState.getInt(KEY_POSITION);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_main, menu);
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
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sort = preferences.getString(
                getString(R.string.pref_sort_movy_key),
                getString(R.string.pref_sort_movy_default));
        Z_FetchMovyTask fetchMovyTask = new Z_FetchMovyTask(this);
        fetchMovyTask.execute(sort);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putStringArrayList(KEY_ALL_DATA, movyArrayList);
        outState.putInt(KEY_POSITION ,mPosition);
        super.onSaveInstanceState(outState);
    }

}
