package com.example.android.popularmovies;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmovies.Fragments.MovieDetailFragment;
import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.utilities.Constants;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private ImageView mImageView;
    private Movie singleMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        Toolbar toolbar = findViewById(R.id.detail_toolbar);
        mImageView = (ImageView)findViewById(R.id.toolImage);

        Intent intentThatStartedThisActivity = getIntent();
        singleMovie = intentThatStartedThisActivity.getParcelableExtra("movieData");
        if(getSupportActionBar() != null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("");
            Picasso.with(this).load("http://image.tmdb.org/t/p/w185"+singleMovie.getPosterPath()).placeholder(R.drawable.placeholder).error(R.drawable.placeholder).into(mImageView);

        }

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putParcelable("movie", singleMovie);
            //arguments.put
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.movie_detail_container, fragment)
                    .commit();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_detail, menu);

        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                supportFinishAfterTransition();
                super.onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
