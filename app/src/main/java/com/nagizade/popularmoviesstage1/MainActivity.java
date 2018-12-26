package com.nagizade.popularmoviesstage1;

/**
 * Created by Hasan Nagizade on 26 December 2018
 */

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.nagizade.popularmoviesstage1.adapters.MovieAdapter;
import com.nagizade.popularmoviesstage1.model.Movie;
import com.nagizade.popularmoviesstage1.utils.ItemClickListener;
import com.nagizade.popularmoviesstage1.utils.RequestHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView_movies;
    private  MovieAdapter movieAdapter;
    private ArrayList<Movie> movies;
    private JSONObject response;
    private final static String URL_POPULAR="http://api.themoviedb.org/3/movie/popular?api_key=";
    private final static String URL_HIGH_RATE = "http://api.themoviedb.org/3/movie/top_rated?api_key=";

    // You must put your API KEY in here
    private final static String API_KEY="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView_movies = findViewById(R.id.rv_movies);

        if(isOnline()) {
            fetchTheMovies(URL_POPULAR);
        } else {
            showDialog("You don't have internet connection",true);
        }

    }

    private void fetchTheMovies(final String url_adress) {
        movies = new ArrayList<>();

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    RequestHandler requestHandler = new RequestHandler();
                    response = requestHandler.get(url_adress+API_KEY);

                    JSONArray results = response.getJSONArray("results");
                    movies = new ArrayList<>();

                    for(int i = 0; i < results.length(); i++) {

                        JSONObject movie    = results.getJSONObject(i);
                        String poster       = movie.getString("poster_path");
                        String movieId      = movie.getString("id");
                        movies.add(new Movie(movieId,poster));
                    }

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                                ItemClickListener listener = new ItemClickListener() {
                                    @Override
                                    public void onItemClick(View view, int position) {

                                        if(isOnline()) {
                                            Intent intent = new Intent(MainActivity.this, MovieDetail.class);
                                            intent.putExtra("movie_id", movies.get(position).getMovie_id());
                                            startActivity(intent);
                                        } else {
                                            showDialog("You don't have internet connection",false);
                                        }
                                    }
                                };
                                movieAdapter = new MovieAdapter(movies, listener);
                                GridLayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 2);
                                recyclerView_movies.setLayoutManager(layoutManager);
                                recyclerView_movies.setAdapter(movieAdapter);

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
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_high_rated:
                movieAdapter.notifyDataSetChanged();
               if(isOnline()) {
                   fetchTheMovies(URL_HIGH_RATE);
               } else {
                   showDialog("You don't have internet connection",false);
               }
                return true;
            case R.id.action_popular:
                movieAdapter.notifyDataSetChanged();
                if(isOnline()) {
                    fetchTheMovies(URL_POPULAR);
                } else {
                    showDialog("You don't have internet connection",false);
                }
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

    private void showDialog(String dialogMessage, final Boolean mustFinished) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this,R.style.Theme_AppCompat_Dialog_Alert);
        builder1.setMessage(dialogMessage);
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        if(mustFinished) finish();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
