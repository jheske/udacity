package com.nano.movies.activities;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
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
import com.nano.movies.web.MovieData;
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
    private MovieData mMovie;
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
        mTextViewTitle.setText(mMovie.getOriginalTitle());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        mTextViewReleaseDate.setText(sdf.format(mMovie.getReleaseDate()));
        CharSequence runtime = Phrase.from(getActivity(), R.string.text_runtime)
                .put("runtime",mMovie.getRuntime().toString())
                .format();
        mTextViewRuntime.setText(runtime);
        mTextViewVoteAverage.setText(mMovie.getVoteAverage().toString() + "/10");
        mTextViewOverview.setText(mMovie.getOverview().toString());
        Drawable drawable = mButtonFavorite.getBackground();
        drawable.setColorFilter(Color.BLUE, PorterDuff.Mode.MULTIPLY);
        //mButtonFavorite.
        Picasso.with(getActivity()).load(mMovie.getMovieUrl())
                .into(mImageViewThumbnail);

    }

}
