package com.nano.movies.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.nano.movies.R;
import com.nano.movies.data.MovieAdapter;
import com.nano.movies.utils.Utils;
import com.nano.movies.web.MovieData;
import com.nano.movies.web.MovieServiceProxy;
import com.nano.movies.web.Tmdb;
import com.nano.movies.web.TmdbResults;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieGridFragment extends Fragment {
    private final String TAG = "[MovieGridFragment]";

    RecyclerView mRvMovies;
    RecyclerView.LayoutManager mLayoutManager;
    MovieAdapter mMovieAdapter;
    private final Tmdb tmdbManager = new Tmdb();

    public MovieGridFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movies, container, false);


        mRvMovies = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRvMovies.setHasFixedSize(true);
        // Grid with 2 columns
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRvMovies.setLayoutManager(mLayoutManager);
        mMovieAdapter = new MovieAdapter(getActivity());
        mRvMovies.setAdapter(mMovieAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        tmdbManager.setIsDebug(true);
        tmdbManager.moviesServiceProxy().discoverMovies(1,MovieServiceProxy.POPULARITY_DESC,new Callback<TmdbResults>() {
            @Override
            public void success(TmdbResults results, Response response) {
                // here you do stuff with returned tasks
                displayPosters(results.results);
                Log.i(TAG, "Success!! total pages = " + results.total_pages);
            }

            @Override
            public void failure(RetrofitError error) {
                // you should handle errors, too
                Log.i(TAG, "discoverMovies Failed!");
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void displayPosters(List<MovieData> movies) {
        mMovieAdapter.addAll(movies);
    }
}
