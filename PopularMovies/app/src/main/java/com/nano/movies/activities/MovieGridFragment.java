package com.nano.movies.activities;

import android.app.Activity;
import android.content.Context;
import android.os.Parcelable;
import android.support.annotation.Nullable;
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
    RecyclerView mRecyclerView;

    //Layout for RecyclerView items (linear or grid)
    RecyclerView.LayoutManager mLayoutManager;

    //RecyclerView's adapter that automatically displays movie list
    //updates views on additions and deletions to list
    MovieAdapter mMovieAdapter;

    //List of movies downloaded from themoviedb.org
    List<Movie> mMovies;

    //Manages communication between activities and themoviedb.org service proxies
    private final Tmdb tmdbManager = new Tmdb();

    /**
     * State vars that must survive a config change.
     */
    Parcelable mLayoutManagerSavedState;
    int mLastPosition=0;
    String mSortBy=MovieServiceProxy.POPULARITY_DESC;

    /**
     * Keys for storing/retrieving state on config change.
     */
    private final String BUNDLE_RECYCLER_LAYOUT = "SaveLayoutState";
    private final String BUNDLE_LAST_POSITION = "SaveLastPosition";
    private final String BUNDLE_SORT_BY = "SaveSortBy";


    // Android recommends Fragments always communicate with each other
    // via the container Activity
    // @see https://developer.android.com/training/basics/fragments/communicating.html
    // This callback interface that allows this Fragment to notify MainActivity when
    // user clicks on a List Item so MainActivity can have SelfieImageFragment
    // show the full-sized image.
    // DON'T FORGET TO DESTROY IT WHEN IN onDetach() OR IT WILL LEAK MEMORY
    public interface MovieSelectionListener {
        public void onMovieSelected(int movieId, boolean isUserSelected);
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
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        // Grid with 2 columns
        mLayoutManager = new GridLayoutManager(getActivity(), 2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mMovieAdapter = new MovieAdapter(getActivity());
        mRecyclerView.setAdapter(mMovieAdapter);
        mRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(),
                mRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {
                Movie movie = mMovieAdapter.getItemAtPosition(position);
                //Call back to MainActivity to handle the click event
                //True = Movie selected by user
                mLastPosition = position;
                mCallback.onMovieSelected(movie.getId(), true);
            }
        }));
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            mLayoutManagerSavedState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mSortBy = savedInstanceState.getString(BUNDLE_SORT_BY);
            mLastPosition = savedInstanceState.getInt(BUNDLE_LAST_POSITION);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
        }
        downloadMovies();
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_SORT_BY, mSortBy);
        outState.putInt(BUNDLE_LAST_POSITION, mLastPosition);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT,
                mRecyclerView.getLayoutManager().onSaveInstanceState());
    }

    /**
     *
     * The state was saved on the most recent config change.
     * Don't restore it quite yet.  Wait til the next downloadMovies
     * to finish up and redisplay all the most recent movies,
     * then scroll the RecyclerView back to its pre-config position.
     *
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
/**
        if (savedInstanceState != null) {
            mLayoutManagerSavedState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            mSortBy = savedInstanceState.getString(BUNDLE_SORT_BY);
            mLastPosition = savedInstanceState.getInt(BUNDLE_LAST_POSITION);
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
        } **/
    }


    /**
     *
     * If this is called after a recent config change,
     * mLayoutManagerSavedState will hold pre-config state,
     * including the most recently viewed movie position
     * and the LayoutManager's state.
     *
     * Retrieve that information and reinitialize the
     * saved states.
     */
    private void restoreLayoutManagerPosition() {
        if (mLayoutManagerSavedState != null) {
            mRecyclerView.getLayoutManager().onRestoreInstanceState(mLayoutManagerSavedState);
        }
        else {
            mLastPosition = 0;
            mLayoutManagerSavedState = null;
        }
    }

    public void downloadMovies() {
        tmdbManager.setIsDebug(false);
        tmdbManager.moviesServiceProxy().discoverMovies(1, mSortBy, new Callback<TmdbResults>() {
            @Override
            public void success(TmdbResults results, Response response) {
                // here you do stuff with returned tasks
                displayPosters(results.results);
                //Tell main activity it can display the first movie in the list
                //if MovieDetailFragment exists (two-pane mode).
                //False = Movie not selected by user
                restoreLayoutManagerPosition();
                Movie movie = mMovieAdapter.getItemAtPosition(mLastPosition);
                mCallback.onMovieSelected(movie.getId(), false);
                //Movie movie = mMovieAdapter.getItemAtPosition(0);
                //mCallback.onMovieSelected(movie.getId(), false);
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
        mMovieAdapter.clear();
        mMovieAdapter.addAll(movies);
    }

    public void setSortBy(String sortBy) {
        mSortBy = sortBy;
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
