package com.example.android.popularmovies.data;


import android.os.Parcel;
import android.os.Parcelable;

public class ReviewModel  implements Parcelable {


    public String getId() {
        return id;
    }


    private String id;

    public String getcontent() {
        return content;
    }


    private String content;

    public String getAuthor() {
        return author;
    }


    private String author;

    public String getUrl() {
        return url;
    }

    private String url;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(id);
        dest.writeString(author);
        dest.writeString(content);


    }

    public ReviewModel(){

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ReviewModel(Parcel in) {
        id = in.readString();
        author = in.readString();
        content = in.readString();
    }

    public static final Creator<ReviewModel> CREATOR = new Creator<ReviewModel>() {
        @Override
        public ReviewModel createFromParcel(Parcel in) {
            return new ReviewModel(in);
        }

        @Override
        public ReviewModel[] newArray(int size) {
            return new ReviewModel[size];
        }
    };
}