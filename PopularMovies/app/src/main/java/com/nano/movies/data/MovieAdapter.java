package com.nano.movies.data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nano.movies.R;
import com.nano.movies.activities.MovieDetailActivity;
import com.nano.movies.web.Movie;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jill on 7/27/2015.
 * <p/>
 * http://www.101apps.co.za/index.php/articles/android-recyclerview-and-picasso-tutorial.html
 * https://github.com/antoniolg/RecyclerViewExtensions/blob/4ce42029e1577de07f9cc38f420e1e9790f838d2/app/src/main/java/com/antonioleiva/recyclerviewextensions/example/MyRecyclerAdapter.java
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {
    private final String TAG="[MovieAdapter]";
    private List<Movie> mMovies;
    private Context mContext;

    public class MovieViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPoster;

        public MovieViewHolder(View itemView) {
            super(itemView);
            imgPoster = (ImageView) itemView.findViewById(R.id.img_poster);
        }
    }

    public MovieAdapter(Context context) {
        super();
        mContext = context;
        mMovies = new ArrayList<Movie>();
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_grid_item, viewGroup, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MovieViewHolder holder, int i) {
        Movie movie = mMovies.get(holder.getAdapterPosition());
        Picasso.with(mContext).load(movie.getMovieUrl())
                .into(holder.imgPoster);
        holder.imgPoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //WOULD BE BETTER TO CREATE A CALLBACK INTO THE Fragment SO
                //IT COULD START THE NEW ACTIVITY
                startDetailActivity(mMovies.get(holder.getAdapterPosition()));
            }
        });
    }

    private void startDetailActivity(Movie movie) {
        Log.d(TAG,"Viewing movie " + movie.getOriginalTitle() );
        Intent intent = new Intent(mContext,MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_ID_EXTRA, movie.getId());
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void addAll(List<Movie> movies) {
        for (int position = 0; position < movies.size(); position++) {
            addItem(movies.get(position), position);
        }
    }

    public void addItem(Movie movie, int position) {
        mMovies.add(position, movie);
        notifyItemInserted(position);
    }

    public void remove(Movie movie) {
        int position = mMovies.indexOf(movie);
        mMovies.remove(position);
        notifyItemRemoved(position);
    }

}
