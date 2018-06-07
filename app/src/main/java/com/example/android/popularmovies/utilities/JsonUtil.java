package com.example.android.popularmovies.utilities;


import android.content.Context;

import com.example.android.popularmovies.data.Movie;
import com.example.android.popularmovies.data.ReviewModel;
import com.example.android.popularmovies.data.TrailerModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by icreator on 3/20/18.
 */

public class JsonUtil {

    public static ArrayList<Movie> getMoviesPosterPathFromJson(Context context, String moviesJsonString)
            throws JSONException {
        String[] parsedMoviesData = null;
        final String MOVIE_LIST = "results";
        final String POSTER_PATH = "poster_path";
        final String OVERVIEW = "overview";
        final String RELEASE_DATE = "release_date";
        final String TITLE = "title";
        final String VOTE_AVERAGE = "vote_average";
        final String ID = "id";
        System.out.println("in jsoon util");
        JSONObject movieJson = new JSONObject(moviesJsonString);
        JSONArray movieArray = movieJson.getJSONArray(MOVIE_LIST);
        ArrayList<Movie> list = new ArrayList<>();
        for (int i = 0; i < movieArray.length(); i++) {
            Movie singleMoview = new Movie();
            JSONObject movie = movieArray.getJSONObject(i);
            if(movie.getString(POSTER_PATH) != null){
                singleMoview.setPosterPath(movie.getString(POSTER_PATH));
                singleMoview.setId(movie.getString(ID));
                singleMoview.setTitle(movie.getString(TITLE));
                singleMoview.setOverview(movie.getString(OVERVIEW));
                singleMoview.setReleaseDate(movie.getString(RELEASE_DATE));
                singleMoview.setVote_average(movie.getString(VOTE_AVERAGE));
                list.add(singleMoview);
                System.out.println(movie.getString(POSTER_PATH) + " " + movie.getString(ID));

            }
        }

        return list;
    }

    public static ArrayList<ReviewModel> getMovieReviews(Context context, String reviewsJsonString) throws JSONException {

        System.out.println("trailers and reives response: " +reviewsJsonString);
//        [{"author":"Screen-Space","content":"\"It is a bold undertaking, to readjust what is expected of
//            the MCU/Avengers formula, and there are moments when the sheer scale and momentum match the narrative ambition...\"\r\n\r\nRead
//            the full review here: http://screen-space.squarespace.com/reviews/2018/4/25/avengers-infinity-war.html",
//            "id":"5adff809c3a3683daa00ad3d","url":"https://www.themoviedb.org/review/5adff809c3a3683daa00ad3d"}


        final String MOVIE_LIST = "results";
        JSONObject movieJson = new JSONObject(reviewsJsonString);
        JSONArray reviewsArray = movieJson.getJSONArray(MOVIE_LIST);
        ArrayList<ReviewModel> list = new ArrayList<>();
        for (int i = 0; i < reviewsArray.length(); i++) {
            ReviewModel reviews = new ReviewModel();
            JSONObject review = reviewsArray.getJSONObject(i);
            reviews.setAuthor(review.getString("author"));
            reviews.setId(review.getString("id"));
            reviews.setUrl(review.getString("url"));
            reviews.setContent(review.getString("content"));
            list.add(reviews);
        }


        return list;
    }

    public static ArrayList<TrailerModel> getMovieTrailers(Context context, String trailerJsonString) throws JSONException {
        final String MOVIE_LIST = "results";
        JSONObject movieJson = new JSONObject(trailerJsonString);
        JSONArray reviewsArray = movieJson.getJSONArray(MOVIE_LIST);
        ArrayList<TrailerModel> list = new ArrayList<>();
        for (int i = 0; i < reviewsArray.length(); i++) {
            TrailerModel trailers = new TrailerModel();
            JSONObject trailer = reviewsArray.getJSONObject(i);
            trailers.setKey(trailer.getString("key"));
            trailers.setId(trailer.getString("id"));

            list.add(trailers);
        }
        return list;
    }

}
