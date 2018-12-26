package com.nagizade.popularmoviesstage1;

/**
 * Created by Hasan Nagizade on 26 December 2018
 */


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.nagizade.popularmoviesstage1.utils.RequestHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class MovieDetail extends AppCompatActivity {

    private ImageView movie_poster;
    private TextView  movie_release_date,movie_user_rating,movie_plot,movie_name;
    private JSONObject response;

    private static final String MOVIE_URL_BASE = "http://api.themoviedb.org/3/movie/";
    private static final String MOVIE_URL_PARAMETER = "?api_key=";
    private static final String MOVIE_IMAGE_URL_BASE="http://image.tmdb.org/t/p/w342/";

    // You must put your API KEY in here
    private static final String MOVIE_API_KEY = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_detail);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        bindViews();

        Intent intent = getIntent();
        String movieId = intent.getStringExtra("movie_id");

        if(!isOnline()) {
            showDialog("You don't have internet connection");
        }
        getMovieInfo(movieId);
    }

    private void bindViews() {

        movie_poster        = findViewById(R.id.iv_poster);
        movie_release_date  = findViewById(R.id.tv_release_date);
        movie_user_rating   = findViewById(R.id.tv_user_rating);
        movie_plot          = findViewById(R.id.tv_plot);
        movie_name          = findViewById(R.id.tv_movie_name);

    }

    private void getMovieInfo(final String movieId) {

        final Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    RequestHandler requestHandler = new RequestHandler();
                    response = requestHandler.get(MOVIE_URL_BASE+movieId+MOVIE_URL_PARAMETER+MOVIE_API_KEY);

                    final String posterPath   = response.getString("poster_path");
                    final String releaseDate  = response.getString("release_date");
                    final String userRating   = response.getString("vote_average");
                    final String plot         = response.getString("overview");
                    final String title        = response.getString("title");

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {

                            Picasso.get().load(MOVIE_IMAGE_URL_BASE+posterPath).into(movie_poster);

                            movie_release_date.setText(releaseDate);
                            movie_user_rating.setText(userRating);
                            movie_plot.setText(plot);
                            movie_name.setText(title);
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
        thread.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public boolean isOnline() {
        ConnectivityManager cm =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnected();
    }

    private void showDialog(String dialogMessage) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this,R.style.Theme_AppCompat_Dialog_Alert);
        builder1.setMessage(dialogMessage);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
