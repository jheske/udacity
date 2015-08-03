/**
 * Created by Jill E Heske on 2015-07-29.
 * Copyright(c) 2015.
 */
package com.nano.movies.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.nano.movies.R;
import com.nano.movies.data.MovieAdapter;
import com.nano.movies.web.Movie;

/**
 * Multi-pane Layout
 * The app is designed to adapt the layout to the device, specifically a
 * phone, or a tablet either in portrait or landscape mode.
 * Phones and tablets in portrait mode will use a one-pane layout,
 * while tablets in landscape will use a two-pane layout.
 * In order to determine which layout to use, Android looks for
 * layouts.xml in res/values (a one-pane layout) for phones,
 * res/values-large-land (two-pane), or res/values-large-port (one-pane).
 * Layout.xml will direct Android to the correct one-pane or two-pane layout
 * in the res/layouts directory.
 * <p/>
 * Organization
 * MainActivity contains 2 Fragments:
 * MovieGridFragment handles the
 * Main UI maintains the list of
 * downloaded movies and corresponding view (in the adapter).
 * This Fragment communicates with MainActivity through callback
 * Interface ListSelectionListener, which the Activity
 * Implements.  When the user selects an item on the grid,
 * the Fragment calls the Listener's onListSelection callback
 * method to pass the path to the selected image to MainActivity,
 * which will the pass it on to either
 * MovieDetailFragment (in two-pane layout) or MovieViewActivity
 * (one-pane layout)
 * MovieDetailFragment displays movie details when
 * user selects a poster from the grid.
 * The fragment is only available in a two-pane layout,
 * and displays on the same screen and to the right
 * as MovieGridFragment, so it is not used at all on a phone or on a
 * tablet in portrait mode.  In that case, we use MovieDetailActivity
 * to load its fragment show the details on a separate screen instead.
 */
//http://stackoverflow.com/questions/30487700/recyclerview-onitemclicked-callback
public class MainActivity extends AppCompatActivity
        implements MovieGridFragment.MovieSelectionListener {
    private final String TAG = getClass().getSimpleName();

    private MovieGridFragment mMovieGridFragment;
    private MovieDetailFragment mMovieDetailFragment;
    private boolean mIsTwoPane = false;

    /**
     * layout_main is defined in 3 different
     * versions of layouts.xml:
     * values/layouts.xml
     * values-large-port/layouts.xml
     * values-large-land/layouts.xml
     * Android decides which one to load
     * depending on device size.
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_main);
        setupFragments();
        // This determines whether to load details into the
        // details fragment, which exists already in two-pane mode,
        // or to launch a separate Activity to view details
        mIsTwoPane = checkForDualPane();
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupFragments() {
        // Retrieve Fragments
        // If we happen to be in a one-pane layout, mMovieDetailFragment
        // isn't in the layout and will be null.
        mMovieGridFragment = (MovieGridFragment) getSupportFragmentManager().findFragmentById(
                R.id.fragment_movie_grid);
        mMovieDetailFragment = (MovieDetailFragment) getSupportFragmentManager().findFragmentById(
                R.id.fragment_movie_detail);
    }

    // Determine whether we are in single-pane or dual-pane mode by testing the visibility
    // of the selfie_image_fragment view.
    private boolean checkForDualPane() {
        if (getResources().getBoolean(R.bool.has_two_panes)) {
            Log.i(TAG, "Two-pane layout");
            return true;
        } else {
            Log.i(TAG, "One-pane layout");
            return false;
        }
    }

    public void onMovieSelected(int movieId,boolean isUserSelected) {
        if (mIsTwoPane) {
            mMovieDetailFragment.downloadMovie(movieId);
        } else {
            if (isUserSelected)
              startMovieDetailActivity(movieId);
        }
    }

    private void startMovieDetailActivity(int movieId) {
        Intent intent = new Intent(this, MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_ID_EXTRA, movieId);
        this.startActivity(intent);
    }
}
