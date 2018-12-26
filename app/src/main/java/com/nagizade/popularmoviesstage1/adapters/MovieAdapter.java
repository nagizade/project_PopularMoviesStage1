package com.nagizade.popularmoviesstage1.adapters;

/**
 * Created by Hasan Nagizade on 26 December 2018
 */


import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nagizade.popularmoviesstage1.R;
import com.nagizade.popularmoviesstage1.model.Movie;
import com.nagizade.popularmoviesstage1.utils.ItemClickListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {

    private ArrayList<Movie> movies;
    private static final String IMAGE_URL_BASE="http://image.tmdb.org/t/p/w342/";
    private ItemClickListener mListener;

    public void setClickListener(ItemClickListener itemClickListener) {
        this.mListener = itemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private ImageView iv_poster;
        private ItemClickListener mListener;


        ViewHolder(View itemView,ItemClickListener listener) {
            super(itemView);
            mListener = listener;
            iv_poster = itemView.findViewById(R.id.img_poster);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mListener.onItemClick(v, getAdapterPosition());
        }
    }

    public MovieAdapter(ArrayList<Movie> movies,ItemClickListener listener) {
        this.movies = movies;
        this.mListener = listener;
    }

    @Override
    public MovieAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View itemView = inflater.inflate(R.layout.movie_row,parent,false);

        return new MovieAdapter.ViewHolder(itemView,mListener);
    }

    @Override
    public void onBindViewHolder(MovieAdapter.ViewHolder holder,int position) {
        Movie movie = movies.get(position);
        String imagepath = movie.getPoster();

        Picasso.get().load(IMAGE_URL_BASE+imagepath).into(holder.iv_poster);

    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}
