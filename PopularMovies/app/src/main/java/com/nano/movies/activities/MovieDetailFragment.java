package com.nano.movies.activities;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.nano.movies.R;
import com.nano.movies.web.Movie;
import com.nano.movies.web.MovieServiceProxy;
import com.nano.movies.web.Tmdb;
import com.squareup.phrase.Phrase;
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

    private ImageView mImageViewThumbnail;
    private TextView mTextViewTitle;
    private TextView mTextViewReleaseDate;
    private TextView mTextViewRuntime;
    private TextView mTextViewVoteAverage;
    private TextView mTextViewOverview;
    private Button mButtonFavorite;
    //private Movie mMovie;
    private Integer mMovieId;
    private final Tmdb tmdbManager = new Tmdb();

    public MovieDetailFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_detail, container, false);
        mImageViewThumbnail = (ImageView) rootView.findViewById(R.id.img_thumbnail);
        mTextViewTitle = (TextView) rootView.findViewById(R.id.tv_movie_title);
        mTextViewReleaseDate = (TextView) rootView.findViewById(R.id.tv_release_date);
        mTextViewRuntime = (TextView) rootView.findViewById(R.id.tv_runtime);
        mTextViewVoteAverage = (TextView) rootView.findViewById(R.id.tv_vote_average);
        mButtonFavorite = (Button) rootView.findViewById(R.id.btn_mark_fav);
        mTextViewOverview = (TextView) rootView.findViewById(R.id.tv_overview);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        //Call proxy to download the movie
        //Display details
        Log.d(TAG, "Movie id = " + mMovieId);
        super.onActivityCreated(savedInstanceState);
        //This should not be called until MovieGridFragment
        //has finished downloading the movies and
        //tells us which movie to display.  If initializing,
        //then the first movie in the list.  Otherwise,
        //whichever movie user selects.
        //downloadMovieDetails();
    }

    public void downloadMovieDetails(Movie movie) {
        tmdbManager.setIsDebug(false);
        tmdbManager.moviesServiceProxy().summary(mMovieId,
                MovieServiceProxy.TRAILERS,
                new Callback<Movie>() {
                    @Override
                    public void success(Movie movie, Response response) {
                        // here you do stuff with returned tasks
                        //mMovie = movie;
                        displayMovieDetails(movie);
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

    private void displayMovieDetails(Movie movie) {
        mTextViewTitle.setText(movie.getOriginalTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        mTextViewReleaseDate.setText(sdf.format(movie.getReleaseDate()));
        CharSequence runtime = Phrase.from(getActivity(), R.string.text_runtime)
                .put("runtime",movie.getRuntime().toString())
                .format();
        mTextViewRuntime.setText(runtime);
        mTextViewVoteAverage.setText(movie.getVoteAverage().toString() + "/10");
        mTextViewOverview.setText(movie.getOverview().toString());
        Picasso.with(getActivity()).load(movie.getMovieUrl())
                .into(mImageViewThumbnail);
    }

}
