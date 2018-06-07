package com.example.android.popularmovies.Receiver;

import android.app.Application;

/**
 * Created by icreator on 3/21/18.
 */

public class PopularMovies extends Application {
    private static PopularMovies mInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("in Popular movies********");
        //mInstance = new PopularMovies();
    }

    public static synchronized PopularMovies getInstance() {
        if(mInstance == null){
            mInstance = new PopularMovies();
        }
        return mInstance;
    }

//    public  static synchronized PopularMovies getInstance() {
//        return new PopularMovies();
//    }

    public void setConnectivityListener(NetworkChangeReceiver.ConnectivityReceiverListener listener) {
        NetworkChangeReceiver.connectivityReceiverListener = listener;
    }
}
