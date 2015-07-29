package com.nano.movies.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nano.movies.R;
import com.nano.movies.web.MovieData;
import com.nano.movies.web.MovieServiceProxy;
import com.nano.movies.web.Tmdb;
import com.nano.movies.web.TmdbResults;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {
    private final String TAG = "[MovieDetailFragment]";

    private ImageView mImageView_Thumbnail;
    private TextView mTextView_Title;
    private TextView mTextView_ReleaseDate;
    private TextView mTextView_Runtime;
    private TextView mTextView_ViewerRating;
    private TextView mTextView_Overview;
    private MovieData mMovie;
    private Integer mMovieId;
    private final Tmdb tmdbManager = new Tmdb();

    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mImageView_Thumbnail = (ImageView) rootView.findViewById(R.id.img_thumbnail);
        mTextView_Title = (TextView) rootView.findViewById(R.id.tv_movie_title);
        mTextView_ReleaseDate = (TextView) rootView.findViewById(R.id.tv_release_date);
        mTextView_Runtime = (TextView) rootView.findViewById(R.id.tv_runtime);
        mTextView_ViewerRating = (TextView) rootView.findViewById(R.id.tv_viewer_rating);
        mTextView_Overview = (TextView) rootView.findViewById(R.id.tv_overview);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //Call proxy to download the movie
        //Display details
        Log.d(TAG, "Movie id = " + mMovieId);
        super.onActivityCreated(savedInstanceState);
        tmdbManager.setIsDebug(true);
        tmdbManager.moviesServiceProxy().summary(mMovieId, new Callback<MovieData>() {
            @Override
            public void success(MovieData movie, Response response) {
                // here you do stuff with returned tasks
                mMovie = movie;
                displayMovieDetails();
                Log.i(TAG, "Success!! Movie title = " + movie.getOriginalTitle());
            }

            @Override
            public void failure(RetrofitError error) {
                // you should handle errors, too
            }
        });

    }

    /**
     * Called by calling Activity to set the movie's id
     *
     * @param movieId
     */
    public void setMovieId(Integer movieId) {
        mMovieId = movieId;
    }

    private void displayMovieDetails() {
        mTextView_Title.setText(mMovie.getOriginalTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        mTextView_ReleaseDate.setText(sdf.format(mMovie.getReleaseDate()));
        mTextView_Runtime.setText(mMovie.getRuntime().toString());
        mTextView_ViewerRating.setText(mMovie.getVoteAverage().toString());
        mTextView_Overview.setText(mMovie.getOverview().toString());
        Picasso.with(getActivity()).load(mMovie.getMovieUrl())
                .into(mImageView_Thumbnail);

    }

}
