package com.nagizade.popularmoviesstage1.model;

public class Movie {
    private String poster,movie_id;

    public Movie() {}

    public Movie(String movie_id,String poster) {
        this.movie_id = movie_id;
        this.poster = poster;
    }

    public String getPoster() {
        return poster;
    }

    public void setPoster(String poster) {
        this.poster = poster;
    }

    public String getMovie_id() {
        return movie_id;
    }

    public void setMovie_id(String movie_id) {
        this.movie_id = movie_id;
    }
}
