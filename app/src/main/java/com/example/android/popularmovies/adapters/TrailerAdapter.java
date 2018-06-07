package com.example.android.popularmovies.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.android.popularmovies.R;
import com.example.android.popularmovies.data.TrailerModel;
import com.example.android.popularmovies.utilities.URLUtils;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;



public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<TrailerModel> mData;
    //private Picasso picasso;
    Context mContext;


    public TrailerAdapter(ArrayList<TrailerModel> trailersData, Context context) {
        //this.picasso = picasso;
        mContext = context;
        this.mData = trailersData;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        View v;
        v = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.trailer_list_item, parent, false);
        viewHolder = new MyItemHolder(v);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        String thumbnailURL = URLUtils.makeThumbnailURL(mData.get(position).getKey());
        Picasso.with(mContext).load(thumbnailURL).placeholder(R.drawable.thumbnail).into(((MyItemHolder) holder).imageView);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void addAll(ArrayList<TrailerModel> data) {
        this.mData.addAll(data);
        notifyDataSetChanged();
    }

    public TrailerModel get(int position) {
        return mData.get(position);
    }

    public ArrayList<TrailerModel> getData() {
        return mData;
    }

    public static class MyItemHolder extends RecyclerView.ViewHolder {
        ImageView imageView;


        MyItemHolder(View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.trailerImage);
        }

    }


}