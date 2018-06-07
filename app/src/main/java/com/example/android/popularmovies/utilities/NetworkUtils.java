package com.example.android.popularmovies.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by icreator on 3/20/18.
 */

public class NetworkUtils {

    public static final String TAG = NetworkUtils.class.getSimpleName();
    public static int TYPE_WIFI = 1;
    public static int TYPE_MOBILE = 2;
    public static int TYPE_NOT_CONNECTED = 0;
    public static final int NETWORK_STATUS_NOT_CONNECTED=0,NETWORK_STAUS_WIFI=1,NETWORK_STATUS_MOBILE=2;

    public static final String POPULAR_MOVIES_URL =
            "https://api.themoviedb.org/3/movie/popular?api_key=dcd95a31011445144c8a1a8a5c6dbc50";

    public static final String TOP_RATED_URL =
            "https://api.themoviedb.org/3/movie/top_rated?api_key=dcd95a31011445144c8a1a8a5c6dbc50";

    public static final String Reviews_URL = "https://api.themoviedb.org/3/movie/";

    public static final String Trailers_URL = "https://api.themoviedb.org/3/movie/";


    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if (hasInput) {
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }



    public static int getConnectivityStatus(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (null != activeNetwork) {
            if(activeNetwork.getType() == ConnectivityManager.TYPE_WIFI)
                return TYPE_WIFI;

            if(activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE)
                return TYPE_MOBILE;
        }
        return TYPE_NOT_CONNECTED;
    }

    public static int getConnectivityStatusString(Context context) {
        int conn = NetworkUtils.getConnectivityStatus(context);
        int status = 0;
        if (conn == NetworkUtils.TYPE_WIFI) {
            status = NETWORK_STAUS_WIFI;
        } else if (conn == NetworkUtils.TYPE_MOBILE) {
            status =NETWORK_STATUS_MOBILE;
        } else if (conn == NetworkUtils.TYPE_NOT_CONNECTED) {
            status = NETWORK_STATUS_NOT_CONNECTED;
        }
        return status;
    }


}
