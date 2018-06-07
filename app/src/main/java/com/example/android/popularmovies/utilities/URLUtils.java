package com.example.android.popularmovies.utilities;



public class URLUtils {

    public static String makeImageURL(String posterPath) {
        return Constants.IMAGE_URL + "/w185" + posterPath + "?api_key?=dcd95a31011445144c8a1a8a5c6dbc50";

        //http://image.tmdb.org/t/p/w185"+singleMovie.getPosterPath()
    }

    public static String makeThumbnailURL(String thumbnailId) {
        return Constants.YT_THUMB_URL.concat(thumbnailId).concat("/hqdefault.jpg");
    }
}
