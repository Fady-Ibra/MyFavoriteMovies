package net.bfci.android.myfavoritemovies;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class A_MainActivity extends AppCompatActivity {

    private static final String DETAIL_FRAGMENT_TAG = "DFTAG";
    public static boolean isTablet =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.a_activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar
                        .make(view, getString(R.string.snackbar), Snackbar.LENGTH_LONG)
                        //.setAction("Action", null)
                        .show();
            }
        });

        if (findViewById(R.id.movy_detail_container) != null) {
            isTablet = true;
            if (savedInstanceState == null) {
                if(A_MainActivityFragment.movyArrayList!=null){
                    getSupportFragmentManager().beginTransaction()
                            .replace(
                                    R.id.movy_detail_container,
                                    new B_DetailActivityFragment(),
                                    DETAIL_FRAGMENT_TAG)
                            .commit();
                }else{
                    getSupportFragmentManager().beginTransaction()
                            .replace(
                                    R.id.movy_detail_container,
                                    new D_EmptyActivityFragment()
                            ).commit();
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            startActivity(new Intent(this, E_SettingsActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
