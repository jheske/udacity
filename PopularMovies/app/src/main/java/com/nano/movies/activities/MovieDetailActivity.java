package com.nano.movies.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.nano.movies.R;
import com.nano.movies.web.MovieData;

//Sending Extras to Fragments
//http://blog.petrnohejl.cz/handling-bundles-in-activities-and-fragments
//
//Get movie detail data
//https://api.themoviedb.org/3/movie/550?api_key=***REMOVED***
public class MovieDetailActivity extends AppCompatActivity {
    public static final String MOVIE_ID_EXTRA = "MOVIE ID EXTRA";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);
        //Integer movieId = getIntent().getIntExtra(MOVIE_ID_EXTRA,0);
        MovieDetailFragment detailFragment = ((MovieDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_movie_detail));
        detailFragment.setMovieId(getIntent().getIntExtra(MOVIE_ID_EXTRA, 0));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
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
