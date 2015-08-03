package com.nano.movies.activities;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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
    public interface MovieSelectionListener {
        public void onMovieSelected(int movieId,boolean isUserSelected);
    }

    MovieSelectionListener mCallback = null;

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
        mRvMovies.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                mRvMovies, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = mMovieAdapter.getItemAtPosition(position);
                //Call back to MainActivity to handle the click event
                //True = Movie selected by user
                mCallback.onMovieSelected(movie.getId(),true);
            }
        }));
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
        downloadMovies();
        super.onActivityCreated(savedInstanceState);
    }

    private void downloadMovies() {
        tmdbManager.setIsDebug(false);
        tmdbManager.moviesServiceProxy().discoverMovies(1, MovieServiceProxy.POPULARITY_DESC, new Callback<TmdbResults>() {
            @Override
            public void success(TmdbResults results, Response response) {
                // here you do stuff with returned tasks
                displayPosters(results.results);
                //Tell main activity it can display the first movie in the list
                //if MovieDetailFragment exists (two-pane mode).
                //False = Movie not selected by user
                Movie movie = mMovieAdapter.getItemAtPosition(0);
                mCallback.onMovieSelected(movie.getId(),false);
            }

            @Override
            public void failure(RetrofitError error) {
                // Handle errors here
                Log.i(TAG, "discoverMovies Failed!");
            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Make sure that the hosting Activity implements
        // the MovieSelectionListener callback interface.
        try {
            mCallback = (MovieSelectionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement MovieSelectionListener");
        }
    }

    private void displayPosters(List<Movie> movies) {
        mMovieAdapter.addAll(movies);
    }


    /**
     * Set up interface to handle onClick
     * This could also handle have methods to handle
     * onLongPress, or other gestures.
     */
    public static interface ClickListener {
        public void onClick(View view, int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {
        private GestureDetector mGestureDetector;
        private ClickListener mClickListener;

        public RecyclerTouchListener(Context context, RecyclerView recyclerView, ClickListener clickListener) {
            //Set up Simple listener to detect singleTapUp (can add
            // additional gestures, like onLongPress if I want)
            mClickListener = clickListener;
            mGestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    //True = this view has handled the event so
                    //it won't be propagated any further.
                    return true;
                }
            });
        }

        /**
         * Required method called only for ViewGroups (like RecyclerView),
         * not for plain Views (like TextViews).
         * Handle the RecyclerView's grid item click event here.
         *
         * @param rv
         * @param e
         * @return
         */
        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            Log.d(TAG, "onInterceptTouchEvent " + mGestureDetector.onTouchEvent(e));

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && mClickListener != null && mGestureDetector.onTouchEvent(e)) {
                mClickListener.onClick(child, rv.getChildAdapterPosition(child));
            }
            return false;
        }

        /**
         * Required method called on View where very first touch occurs.
         *
         * @param rv
         * @param e
         */
        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        /**
         * Required method called when a child of RecyclerView does not want
         * RecyclerView and its ancestors to intercept touch events.
         */
        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
        }
    }
}
