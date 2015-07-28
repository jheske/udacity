package com.nano.movies.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
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
public class MovieGridFragment extends Fragment implements MovieAdapter.ClickListener {
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
        mMovieAdapter.setClickListener(this);
        mRvMovies.setAdapter(mMovieAdapter);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //tmdbManager.moviesServiceProxy().discoverMovies(1, "popularity.desc", resultsCallback);
        tmdbManager.setIsDebug(true);
        tmdbManager.moviesServiceProxy().discoverMovies(1,"popularity.desc",new Callback<TmdbResults>() {
            @Override
            public void success(TmdbResults results, Response response) {
                // here you do stuff with returned tasks
                displayPosters(results.results);
                Log.i(TAG, "Success!! total pages = " + results.total_pages);
            }

            @Override
            public void failure(RetrofitError error) {
                // you should handle errors, too
            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void displayPosters(List<MovieData> movies) {
        mMovieAdapter.addAll(movies);
    }

    @Override
    public void itemClicked(View view, int position) {
        //Start MovieDetailActivity
        Utils.showToast(getActivity(),"DetailActivity Click!!!");
    }
}
