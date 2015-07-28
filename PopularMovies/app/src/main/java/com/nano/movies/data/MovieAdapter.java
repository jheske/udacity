package com.nano.movies.data;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nano.movies.R;
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
    private List<MovieData> mItems;
    private Context mContext;
    private ClickListener mClickListener;

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
        mItems = new ArrayList<MovieData>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.movie_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int i) {
        MovieData movie = mItems.get(i);
        Picasso.with(mContext).load(movie.getMovieUrl())
                .into(holder.imgPoster);
    }

    public void setClickListener(ClickListener clickListener) {
        this.mClickListener = clickListener;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addAll(List<MovieData> movies) {
        for (int position = 0; position < movies.size(); position++) {
            addItem(movies.get(position), position);
        }
    }

    public void addItem(MovieData movie, int position) {
        mItems.add(position, movie);
        notifyItemInserted(position);
    }

    public void remove(MovieData movie) {
        int position = mItems.indexOf(movie);
        mItems.remove(position);
        notifyItemRemoved(position);
    }

    public interface ClickListener {
        public void itemClicked (View view, int position);
    }
}
