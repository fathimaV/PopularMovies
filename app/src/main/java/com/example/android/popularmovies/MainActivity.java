package com.example.android.popularmovies;


import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.popularmovies.Receiver.NetworkChangeReceiver;
import com.example.android.popularmovies.Receiver.PopularMovies;
import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.database.MovieContract;
import com.example.android.popularmovies.utilities.JsonUtil;
import com.example.android.popularmovies.utilities.NetworkUtils;

import org.json.JSONException;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MoviesAdapter.ItemClickListener,NetworkChangeReceiver.ConnectivityReceiverListener, LoaderManager.LoaderCallbacks<Cursor> {
    private RecyclerView mRecyclerView;
    private TextView mErrorMessageDisplay;
    private ProgressBar mLoadingIndicator;
    private MoviesAdapter mMoviesAdapter;
    private boolean isConnectedToNetwork;
    private static final int FAVORITE_MOVIES_LOADER = 0;
    NetworkChangeReceiver connectivityReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView)findViewById(R.id.recylerview_movies);

        /* This TextView is used to display errors and will be hidden if there are no errors */
        mErrorMessageDisplay = (TextView) findViewById(R.id.tv_error_message_display);
        int numberOfColumns = 2;

        GridLayoutManager layoutManager = new GridLayoutManager(this, numberOfColumns, GridLayoutManager.VERTICAL, false );
        mRecyclerView.setLayoutManager(layoutManager);


        mLoadingIndicator = (ProgressBar) findViewById(R.id.pb_loading_indicator);
        if(isConnectedToNetwork){
            loadMoviesData(NetworkUtils.POPULAR_MOVIES_URL);

        }else{
            Toast.makeText(this,"You are not online!!!!",Toast.LENGTH_SHORT).show();
        }

        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);

        connectivityReceiver = new NetworkChangeReceiver();
        this.registerReceiver(connectivityReceiver, intentFilter);


        // register connection status listener
        PopularMovies.getInstance().setConnectivityListener(this);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(connectivityReceiver != null){
            this.unregisterReceiver(connectivityReceiver);
        }
    }

    private void loadMoviesData(String url) {
        showMovieDataView();

        if(isConnectedToNetwork){
            new FetchMoviesTask().execute(url);
        }else{
            Toast.makeText(this,"You are not online!!!!",Toast.LENGTH_SHORT).show();
            Log.v("Home", "############################You are not online!!!!");
            //mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mErrorMessageDisplay.setText("You are not online!");
        }

    }

    private void showMovieDataView() {
        /* First, make sure the error is invisible */
        mErrorMessageDisplay.setVisibility(View.INVISIBLE);

        /* Then, make sure the weather data is visible */
        mRecyclerView.setVisibility(View.VISIBLE);
    }

    /**
     * This method will make the error message visible and hide the weather
     * View.
     * <p>
     * Since it is okay to redundantly set the visibility of a View, we don't
     * need to check whether each view is currently visible or invisible.
     */
    private void showErrorMessage() {

        /* First, hide the currently visible data */
        mRecyclerView.setVisibility(View.INVISIBLE);
        /* Then, show the error */
        mErrorMessageDisplay.setVisibility(View.VISIBLE);
    }

    @Override
    public void onItemClick(Movie movieData) {
        if(movieData != null){
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra("movieData", movieData);
            startActivity(detailIntent);
        }
    }

//    private boolean checkConnection() {
//        boolean isConnected = NetworkChangeReceiver.isConnected();
//        //showSnack(isConnected);
//        return isConnected;
//    }

    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {

        isConnectedToNetwork = isConnected;

        if(isConnected){
            loadMoviesData(NetworkUtils.POPULAR_MOVIES_URL);
        }else{
            Toast.makeText(this,"You are not online!!!!",Toast.LENGTH_SHORT).show();
            Log.v("Home", "############################You are not online!!!!");
            //mErrorMessageDisplay.setVisibility(View.VISIBLE);
            mErrorMessageDisplay.setText("You are not online!");
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        return new CursorLoader(this,
                MovieContract.MovieEntry.CONTENT_URI,
                MovieContract.MovieEntry.MOVIE_COLUMNS,
                null,
                null,
                null);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        System.out.println("the cursor size is: "+cursor.getCount());

        mRecyclerView.setAdapter(mMoviesAdapter);
        mMoviesAdapter.add(cursor);

//        updateEmptyState();
//        findViewById(R.id.progress).setVisibility(View.GONE);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }


    public class FetchMoviesTask extends AsyncTask<String, Void, ArrayList<Movie>>{

        @Override
        protected ArrayList<Movie> doInBackground(String... params) {

            if (params.length == 0) {
                return null;
            }

            String moviesRequestUrl = params[0];
            try {
                String moviesResponse = NetworkUtils.getResponseFromHttpUrl(new URL(moviesRequestUrl));
                ArrayList<Movie> jsonMovieData = JsonUtil.getMoviesPosterPathFromJson(MainActivity.this, moviesResponse);
                return jsonMovieData;
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<Movie> movieData) {
            mLoadingIndicator.setVisibility(View.INVISIBLE);
            if (movieData != null) {
                showMovieDataView();

                mMoviesAdapter = new MoviesAdapter(movieData, MainActivity.this, MainActivity.this);
                mRecyclerView.setAdapter(mMoviesAdapter);
                //mMoviesAdapter.setmMoviesData(movieData);
            } else {
                showErrorMessage();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.movies, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }

    private void fetchFavouriteMovies() {
        getSupportLoaderManager().initLoader(FAVORITE_MOVIES_LOADER, null, this);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_popular) {

            mMoviesAdapter.setmMoviesData(null);
            if(isConnectedToNetwork){
                loadMoviesData(NetworkUtils.POPULAR_MOVIES_URL);
            }else{
                Toast.makeText(this,"You are not online!!!!",Toast.LENGTH_SHORT).show();
                Log.v("Home", "############################You are not online!!!!");
                //mErrorMessageDisplay.setVisibility(View.VISIBLE);
                mErrorMessageDisplay.setText("You are not online!");
            }

            return true;
        }
        if (id == R.id.action_top_rated) {

            mMoviesAdapter.setmMoviesData(null);
            if(isConnectedToNetwork){
                loadMoviesData(NetworkUtils.TOP_RATED_URL);
            }else{
                Toast.makeText(this,"You are not online!!!!",Toast.LENGTH_SHORT).show();
                Log.v("Home", "############################You are not online!!!!");
                //mErrorMessageDisplay.setVisibility(View.VISIBLE);
                mErrorMessageDisplay.setText("You are not online!");
            }

            return true;
        }

        if (id == R.id.action_fav) {
            mMoviesAdapter.setmMoviesData(null);

            item.setChecked(true);
            fetchFavouriteMovies();

        }

        return super.onOptionsItemSelected(item);
    }
}
