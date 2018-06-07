package com.example.android.popularmovies.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.ReviewModel;

import java.util.ArrayList;


public class ReviewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ReviewModel> data;


    public ReviewAdapter(ArrayList<ReviewModel> reviewsData) {
        this.data = reviewsData;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.review_list_item, parent, false);
        viewHolder = new MyItemHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        ((MyItemHolder) holder).authorText.setText(data.get(position).getAuthor());
        ((MyItemHolder) holder).reviewText.setText(data.get(position).getcontent());

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void addAll(ArrayList<ReviewModel> data) {
        this.data.addAll(data);
        notifyDataSetChanged();
    }

    public ReviewModel get(int position) {
        return data.get(position);
    }

    public ArrayList<ReviewModel> getData() {
        return data;
    }

    static class MyItemHolder extends RecyclerView.ViewHolder {

        TextView authorText;

        TextView reviewText;


        MyItemHolder(View itemView) {
            super(itemView);

            authorText = (TextView)itemView.findViewById(R.id.authorText);
            reviewText = (TextView)itemView.findViewById(R.id.reviewText);

        }

    }


}