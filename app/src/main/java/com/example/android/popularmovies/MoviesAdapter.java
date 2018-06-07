package com.example.android.popularmovies;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.database.MovieContract;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by icreator on 3/20/18.
 */

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesAdapterViewHolder> {

    private ArrayList<Movie> mMoviesData;
    private Context mContext;
    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185";
    private ItemClickListener mClickHandler;

    public interface ItemClickListener{
        public void onItemClick(Movie movieData);
    }



    public MoviesAdapter(ArrayList<Movie> movieList, Context context, ItemClickListener clickHandler ){

        mMoviesData = movieList;
        mContext = context;
        mClickHandler = clickHandler;
    }


    @Override
    public MoviesAdapterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


        View view = LayoutInflater.from(mContext).inflate(R.layout.movies_list_item, parent, false);

        return new MoviesAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MoviesAdapterViewHolder holder, int position) {
            Movie moviesUrl = mMoviesData.get(position);

        String url = IMAGE_URL + moviesUrl.getPosterPath();

        Picasso.with(mContext).load("http://image.tmdb.org/t/p/w185"+moviesUrl.getPosterPath()).into(holder.mMovieImageView);


    }

    public void add(List<Movie> movies) {
        mMoviesData.clear();
        mMoviesData.addAll(movies);
        notifyDataSetChanged();
    }

    public void add(Cursor cursor) {
        mMoviesData = new ArrayList<Movie>();
        if (cursor != null && cursor.moveToFirst()) {
            do {
                long id = cursor.getLong(MovieContract.MovieEntry.COL_MOVIE_ID);
                String title = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_TITLE);
                String posterPath = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_POSTER_PATH);
                String overview = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_OVERVIEW);
                String rating = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_VOTE_AVERAGE);
                String releaseDate = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_RELEASE_DATE);
                String backdropPath = cursor.getString(MovieContract.MovieEntry.COL_MOVIE_BACKDROP_PATH);
                Movie movie = new Movie(Long.toString(id), title, posterPath, overview, rating, releaseDate, backdropPath);
                mMoviesData.add(movie);
            } while (cursor.moveToNext());
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if(null == mMoviesData) return 0;
        return mMoviesData.size();
    }

    public void setmMoviesData(ArrayList<Movie> moviesData)
    {
        mMoviesData = moviesData;
        notifyDataSetChanged();
    }

    public class MoviesAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final ImageView mMovieImageView;


        public MoviesAdapterViewHolder(View itemView) {
            super(itemView);
            mMovieImageView = (ImageView) itemView.findViewById(R.id.movies_imageview);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPostion = getAdapterPosition();
            Movie movie = mMoviesData.get(adapterPostion);
            mClickHandler.onItemClick(movie);
        }
    }
}
