package com.example.android.popularmovies.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;


import com.example.android.popularmovies.MainActivity;
import com.example.android.popularmovies.R;
import com.example.android.popularmovies.Receiver.NetworkChangeReceiver;
import com.example.android.popularmovies.Receiver.PopularMovies;
import com.example.android.popularmovies.adapters.ReviewAdapter;
import com.example.android.popularmovies.adapters.TrailerAdapter;
import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.ReviewModel;
import com.example.android.popularmovies.data.TrailerModel;
import com.example.android.popularmovies.database.MovieContract;
import com.example.android.popularmovies.utilities.JsonUtil;
import com.example.android.popularmovies.utilities.NetworkUtils;
import com.example.android.popularmovies.utilities.RecyclerClickListener;
import com.squareup.picasso.Picasso;


import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;






public class MovieDetailFragment extends Fragment implements NetworkChangeReceiver.ConnectivityReceiverListener {


    Movie movieModel;

    ImageView mImageView;

    TextView titleView;

    TextView rating;

    RatingBar ratingBar;

    TextView overview;

    TextView releaseText;

    RecyclerView trailersRecyclerView;

    RecyclerView reviewsRecyclerView;

    TextView noReviewView;

    TextView noTrailerView;

    LinearLayout extraLayout;

    ReviewAdapter reviewAdapter;

    TrailerAdapter trailerAdapter;
    NetworkChangeReceiver connectivityReceiver;


    private boolean isConnectedToNetwork;


    public MovieDetailFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (getArguments().containsKey("movie")) {

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = activity.findViewById(R.id.collapsing_toolbar);
            if (appBarLayout != null) {
                appBarLayout.setTitle("");
            }
            movieModel = getArguments().getParcelable("movie");
            assert movieModel != null;
        }

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        connectivityReceiver = new NetworkChangeReceiver();
        getActivity().registerReceiver(connectivityReceiver, intentFilter);


        // register connection status listener
        PopularMovies.getInstance().setConnectivityListener(this);


    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if(connectivityReceiver != null){
            getActivity().unregisterReceiver(connectivityReceiver);
        }
    }





    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        mImageView = (ImageView)rootView.findViewById(R.id.imageView);
        titleView = (TextView)rootView.findViewById(R.id.titleView);
        rating = (TextView)rootView.findViewById(R.id.rating);
        overview = (TextView)rootView.findViewById(R.id.overview);
        releaseText = (TextView)rootView.findViewById(R.id.releaseText);
        trailersRecyclerView = (RecyclerView)rootView.findViewById(R.id.trailersRecyclerView);
        reviewsRecyclerView = (RecyclerView)rootView.findViewById(R.id.reviewsRecyclerView);
        System.out.println("moviemodeldata : " +movieModel.getPosterPath());
        titleView.setText(movieModel.getTitle());
        Picasso.with(getActivity().getApplicationContext()).load("http://image.tmdb.org/t/p/w185"+movieModel.getPosterPath()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(mImageView);


        rating.setText((movieModel.getVote_average()).concat("/10"));
        ratingBar = (RatingBar)rootView.findViewById(R.id.ratingBar);
        ratingBar.setMax(5);
        ratingBar.setRating(Float.parseFloat(movieModel.getVote_average()) / 2f);

        overview.setText(movieModel.getOverview());
        releaseText.setText("Release Date: ".concat(movieModel.getReleaseDate()));

//        if (!NetworkUtils.isNetworkAvailable(getActivity()))
//            extraLayout.setVisibility(View.INVISIBLE);

        fetchReviews(movieModel.getId());
        fetchTrailers(movieModel.getId());


        LinearLayoutManager trailerLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false);
        LinearLayoutManager reviewLayoutManager = new LinearLayoutManager(getContext());

        trailersRecyclerView.setLayoutManager(trailerLayoutManager);
        reviewsRecyclerView.setLayoutManager(reviewLayoutManager);

        trailersRecyclerView.setAdapter(trailerAdapter);
        reviewsRecyclerView.setAdapter(reviewAdapter);



        trailersRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getContext(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                String url = "https://www.youtube.com/watch?v=".concat(trailerAdapter.get(position).getKey());
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        }));

        reviewsRecyclerView.addOnItemTouchListener(new RecyclerClickListener(getContext(), new RecyclerClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(reviewAdapter.get(position).getUrl()));
                startActivity(i);
            }
        }));


        return rootView;
    }


    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        menu.findItem(R.id.share).setVisible(true);
        MenuItem item = menu.findItem(R.id.fav);
        item.setVisible(true);
        item.setIcon(!isFavourite() ? R.drawable.fav_remove : R.drawable.fav_add);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.share:
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_SUBJECT, movieModel.getTitle());
                share.putExtra(Intent.EXTRA_TEXT, "https://www.youtube.com/watch?v=".concat(trailerAdapter.get(0).getKey()));
                startActivity(Intent.createChooser(share, "Share Trailer!"));
                break;


            case R.id.fav:
                System.out.println("in fav in detailFragment");
                if (!isFavourite()) {
                    System.out.println("marking as favorite ******");
                    markAsFavorite();

                } else {
                    System.out.println("marking as unfavorite ******");
                    removeFromFavorites();

                }
                break;


        }
        return super.onOptionsItemSelected(item);
    }


    public void markAsFavorite() {

        if (!isFavourite()){
            System.out.println("inserting favourite values in db ******");
            ContentValues movieValues = new ContentValues();
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_ID,
                    movieModel.getId());
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_TITLE,
                    movieModel.getTitle());
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_POSTER_PATH,
                    movieModel.getPosterPath());
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_OVERVIEW,
                    movieModel.getOverview());
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_VOTE_AVERAGE,
                    movieModel.getVote_average());
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_RELEASE_DATE,
                    movieModel.getReleaseDate());
            movieValues.put(MovieContract.MovieEntry.COLUMN_MOVIE_BACKDROP_PATH,
                    movieModel.getPosterPath());
            getActivity().getApplicationContext().getContentResolver().insert(
                    MovieContract.MovieEntry.CONTENT_URI,
                    movieValues
            );
        }
    }

    public void removeFromFavorites() {

        if (isFavourite()) {
            getContext().getContentResolver().delete(MovieContract.MovieEntry.CONTENT_URI,
                    MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + movieModel.getId(), null);

        }
    }


    public boolean isFavourite(){

        Cursor movieCursor = getContext().getContentResolver().query(
                MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.COLUMN_MOVIE_ID},
                MovieContract.MovieEntry.COLUMN_MOVIE_ID + " = " + movieModel.getId(),
                null,
                null);

        if (movieCursor != null && movieCursor.moveToFirst()) {
            movieCursor.close();
            return true;
        } else {
            return false;
        }

    }


    private void fetchReviews(final String id) {

        System.out.println("in fetchreviews isConnectedToNetwork "+isConnectedToNetwork);

        String reviewsUrl = NetworkUtils.Reviews_URL + id + "/reviews?api_key=dcd95a31011445144c8a1a8a5c6dbc50";
        new FetchReviewsTask().execute(reviewsUrl);


    }

    private void fetchTrailers(final String id) {
        System.out.println("in fetchreviews isConnectedToNetwork "+isConnectedToNetwork);

        String trailersUrl = NetworkUtils.Trailers_URL + id + "/videos?api_key=dcd95a31011445144c8a1a8a5c6dbc50";
        new FetchTrailersTask().execute(trailersUrl.trim());

    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        isConnectedToNetwork = isConnected;

        if (isConnected) {
            fetchReviews(movieModel.getId());
            //fetchTrailers(movieModel.getId());
        } else {
            Toast.makeText(getActivity(), "You are not online!!!!", Toast.LENGTH_SHORT).show();
            Log.v("Home", "############################You are not online!!!!");
            //mErrorMessageDisplay.setVisibility(View.VISIBLE);
            //mErrorMessageDisplay.setText("You are not online!");
        }

    }

    public class FetchReviewsTask extends AsyncTask<String, Void, ArrayList<ReviewModel>>{


        @Override
        protected ArrayList<ReviewModel> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String moviesRequestUrl = params[0];
            try {
                String response = NetworkUtils.getResponseFromHttpUrl(new URL(moviesRequestUrl));
                ArrayList<ReviewModel> jsonMovieData = null;
                try {
                    jsonMovieData = JsonUtil.getMovieReviews(getActivity(), response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonMovieData;
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<ReviewModel> reviewsData) {
            super.onPostExecute(reviewsData);
            reviewAdapter = new ReviewAdapter(reviewsData);
            reviewsRecyclerView.setAdapter(reviewAdapter);
        }
    }

    public class FetchTrailersTask extends AsyncTask<String, Void, ArrayList<TrailerModel>>{

        @Override
        protected ArrayList<TrailerModel> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String moviesRequestUrl = params[0];
            try {
                String response = NetworkUtils.getResponseFromHttpUrl(new URL(moviesRequestUrl));
                ArrayList<TrailerModel> jsonMovieData = null;
                try {
                    jsonMovieData = JsonUtil.getMovieTrailers(getActivity(), response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                return jsonMovieData;
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<TrailerModel> trailerModels) {
            super.onPostExecute(trailerModels);
            trailerAdapter = new TrailerAdapter(trailerModels, getActivity().getApplicationContext());
            trailersRecyclerView.setAdapter(trailerAdapter);
        }
    }


}
