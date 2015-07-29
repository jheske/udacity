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
import com.nano.movies.web.MovieData;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jill on 7/27/2015.
 * <p/>
 * http://www.101apps.co.za/index.php/articles/android-recyclerview-and-picasso-tutorial.html
 * https://github.com/antoniolg/RecyclerViewExtensions/blob/4ce42029e1577de07f9cc38f420e1e9790f838d2/app/src/main/java/com/antonioleiva/recyclerviewextensions/example/MyRecyclerAdapter.java
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    private final String TAG="[MovieAdapter]";
    private List<MovieData> mMovies;
    private Context mContext;

    class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPoster;

        public ViewHolder(View itemView) {
            super(itemView);
            imgPoster = (ImageView) itemView.findViewById(R.id.img_poster);
        }
    }

    public MovieAdapter(Context context) {
        super();
        mContext = context;
        mMovies = new ArrayList<MovieData>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_grid_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        MovieData movie = mMovies.get(holder.getAdapterPosition());
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

    private void startDetailActivity(MovieData movie) {
        Log.d(TAG,"Viewing movie " + movie.getOriginalTitle() );
        Intent intent = new Intent(mContext,MovieDetailActivity.class);
        intent.putExtra(MovieDetailActivity.MOVIE_ID_EXTRA, movie.getId());
        mContext.startActivity(intent);
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    public void addAll(List<MovieData> movies) {
        for (int position = 0; position < movies.size(); position++) {
            addItem(movies.get(position), position);
        }
    }

    public void addItem(MovieData movie, int position) {
        mMovies.add(position, movie);
        notifyItemInserted(position);
    }

    public void remove(MovieData movie) {
        int position = mMovies.indexOf(movie);
        mMovies.remove(position);
        notifyItemRemoved(position);
    }

}
