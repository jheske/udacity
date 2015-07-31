package com.nano.movies.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.nano.movies.R;
import com.nano.movies.data.MovieAdapter;
import com.nano.movies.web.Movie;
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

    //Main view for holding movie posters
    RecyclerView mRvMovies;

    //Layout for RecyclerView items (linear or grid)
    RecyclerView.LayoutManager mLayoutManager;

    //RecyclerView's adapter that automatically displays movie list
    //updates views on additions and deletions to list
    MovieAdapter mMovieAdapter;

    //List of movies downloaded from themoviedb.org
    List<Movie> mMovies;

    //Manages communication between activities and themoviedb.org service proxies
    private final Tmdb tmdbManager = new Tmdb();

    // Android recommends Fragments always communicate with each other
    // via the container Activity
    // @see https://developer.android.com/training/basics/fragments/communicating.html
    // This callback interface that allows this Fragment to notify MainActivity when
    // user clicks on a List Item so MainActivity can have SelfieImageFragment
    // show the full-sized image.
    // DON'T FORGET TO DESTROY IT WHEN IN onDetach() OR IT WILL LEAK MEMORY
    public interface GridSelectionListener {
        public void onGridSelection(String pathToImage);
    }

    GridSelectionListener mListener = null;

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
        //FIGURE OUT HOW TO GET THE RV ITEM CLICKS TO CALLBACK TO ACTIVITY
        /*mRvMovies.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v,
                                    int position, long pos) {
                Movie selfie = mMovieAdapter.getItem((int) pos);
                //mListener.onListSelection(selfie.getPathToCapturedImage()); */
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
                //TELL DETAIL FRAGMENT TO DISPLAY DETAILS
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

    private void displayPosters(List<Movie> movies) {
        mMovieAdapter.addAll(movies);
    }
}
