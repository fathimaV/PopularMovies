package com.example.android.popularmovies.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by icreator on 3/20/18.
 */

public class Movie implements Parcelable{

    public String title;
    public String imageThumbnail;
    public String overview;
    public String vote_average;
    public String releaseDate;
    public String id;

    public String posterPath;

    public Movie(){

    }

    public Movie(String id, String title, String poster, String overview, String userRating,
                 String releaseDate, String backdrop) {
        this.id = id;
        this.title = title;
        this.posterPath = poster;
        this.overview = overview;
        this.vote_average = userRating;
        this.releaseDate = releaseDate;
        this.imageThumbnail = backdrop;
    }

    public Movie(Parcel in) {
        title = in.readString();
        imageThumbnail = in.readString();
        overview = in.readString();
        vote_average = in.readString();
        releaseDate = in.readString();
        id = in.readString();
        posterPath = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getImageThumbnail() {
        return imageThumbnail;
    }

    public String getOverview() {
        return overview;
    }

    public String getVote_average() {
        return vote_average;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setImageThumbnail(String imageThumbnail) {
        this.imageThumbnail = imageThumbnail;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setVote_average(String vote_average) {
        this.vote_average = vote_average;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getTitle() {
        return title;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(imageThumbnail);
        parcel.writeString(overview);
        parcel.writeString(vote_average);
        parcel.writeString(releaseDate);
        parcel.writeString(id);
        parcel.writeString(posterPath);
    }
}
