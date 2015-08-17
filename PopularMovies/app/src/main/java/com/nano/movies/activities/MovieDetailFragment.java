package com.nano.movies.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.nano.movies.R;
import com.nano.movies.utils.Utils;
import com.nano.movies.web.Movie;
import com.nano.movies.web.MovieServiceProxy;
import com.nano.movies.web.Tmdb;
import com.squareup.phrase.Phrase;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * A placeholder fragment containing a simple view.
 */
public class MovieDetailFragment extends Fragment {
    private final String TAG = "[MovieDetailFragment]";

    private ImageView mImageViewThumbnail;
    private TextView mTextViewTitle;
    private TextView mTextViewReleaseDate;
    private TextView mTextViewRuntime;
    private TextView mTextViewOverview;
    private RatingBar mRatingVoteAverage;

    //Will be hooked up in P2
    private Button mButtonFavorite;
    private int mMovieId;
    private final Tmdb tmdbManager = new Tmdb();


    public MovieDetailFragment() {
        setRetainInstance(true);
    }

    /**
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     *
     * @TODO Hook up Favorite button
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mImageViewThumbnail = (ImageView) rootView.findViewById(R.id.img_thumbnail);
        mTextViewTitle = (TextView) rootView.findViewById(R.id.tv_movie_title);
        mTextViewReleaseDate = (TextView) rootView.findViewById(R.id.tv_release_date);
        mTextViewRuntime = (TextView) rootView.findViewById(R.id.tv_runtime);
        mRatingVoteAverage = (RatingBar) rootView.findViewById(R.id.rating_bar_vote_average);
        mButtonFavorite = (Button) rootView.findViewById(R.id.btn_mark_fav);
        mTextViewOverview = (TextView) rootView.findViewById(R.id.tv_overview);
        return rootView;
    }

    /**
     * Called by MainActivity if Fragment already exists (two-pane mode),
     * or when the Fragment is created by its own separate activity
     * (MovieDetailActivity), in single-pane mode.
     *
     * @param
     */
    public void downloadMovie(int movieId) {
        //Member var so it's available in callback for error handling
        mMovieId = movieId;
        tmdbManager.setIsDebug(false);
        tmdbManager.moviesServiceProxy().summary(movieId,
                MovieServiceProxy.TRAILERS,
                new Callback<Movie>() {
                    @Override
                    public void success(Movie movie, Response response) {
                        displayMovieDetails(movie);
                        Log.i(TAG, "Success!! Movie title = " + movie.getOriginalTitle());
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        // Handle errors here.
                        Utils.showToast(getActivity(),"Failed to download movie " + mMovieId);
                    }
                });
    }

    private void displayMovieDetails(Movie movie) {
        mTextViewTitle.setText(movie.getOriginalTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy", Locale.ENGLISH);
        mTextViewReleaseDate.setText(sdf.format(movie.getReleaseDate()));
        CharSequence runtime = Phrase.from(getActivity(), R.string.text_runtime)
                .put("runtime", movie.getRuntime().toString())
                .format();
        mTextViewRuntime.setText(runtime);
        mRatingVoteAverage.setRating(movie.getVoteAverage().floatValue());
        mTextViewOverview.setText(movie.getOverview());
        String movieImageUrl = Tmdb.getMovieImageUrl(movie.getPosterPath(),
                Tmdb.IMAGE_POSTER_SMALL);
        Picasso.with(getActivity()).load(movieImageUrl)
                .into(mImageViewThumbnail);
    }

}
