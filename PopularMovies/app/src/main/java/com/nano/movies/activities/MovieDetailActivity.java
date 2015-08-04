package com.nano.movies.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.nano.movies.R;
import com.nano.movies.data.MovieAdapter;
import com.nano.movies.web.Tmdb;

//Sending Extras to Fragments
//http://blog.petrnohejl.cz/handling-bundles-in-activities-and-fragments
//
//Get movie detail data
//https://api.themoviedb.org/3/movie/550?api_key=xxx
public class MovieDetailActivity extends AppCompatActivity {
    public static final String MOVIE_ID_EXTRA = "MOVIE ID EXTRA";

    //Main grid-layout to hold movie posters
    RecyclerView mRvMovies;
    //Layout manager that determines format of list objects (grid vs linear)
    RecyclerView.LayoutManager mLayoutManager;
    //Adapter that retrieves objects from the model (list of Movie)
    //and displays them in the RecyclerView
    MovieAdapter mMovieAdapter;
    //Used to access TheMovieDatabase's various services (movies, TV, etc).
    private final Tmdb tmdbManager = new Tmdb();

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        setupToolbar();;
        setupDetailFragment();
    }

    private void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupDetailFragment() {
        int movieId = getIntent().getIntExtra(MOVIE_ID_EXTRA, 0);
        MovieDetailFragment detailFragment = ((MovieDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_movie_detail));
        detailFragment.downloadMovie(movieId);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        getMenuInflater().inflate(R.menu.menu_movie_detail, menu);
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
}
