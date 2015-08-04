package com.nano.movies.web;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;
import retrofit.http.Query;

/**
 * Retrofit Service to send requests to TheMovieDatabase (Tmdb) web service and
 * convert the Json response to POJO class.
 *
 * For now put everything in here I think I might need for P1, P2, etc.
 *
 */
public interface MovieServiceProxy {
    public final String POPULARITY_DESC = "popularity.desc";
    public final String VOTE_AVERAGE_DESC = "vote_average.desc";
    //For later use
    public static final String PARAM_APPEND = "append_to_response";
    public static final String RELEASES = "releases";
    public static final String TRAILERS = "trailers";
    public static final String RELEASES_AND_TRAILERS = "releases,trailers";

    /**
     * Get basic movie information for specific movie.
     *
     * @param tmdbId Tmdb-assigned Unique movie id
     */
    @GET("/movie/{id}")
    void summary(
            @Path("id") int tmdbId,
            Callback<Movie> resultsCallback
    );

    /**
     * Get basic movie information for specific movie.
     *
     * @param tmdbId Tmdb-assigned Unique movie id
     */
    @GET("/movie/{id}")
    void summary(
            @Path("id") int tmdbId,
            @Query("append_to_response") String appendToResponse,
            Callback<Movie> resultsCallback
    );

    /**
     * Get movie information for specific movie.
     *
     * @param tmdbId   TMDb movie id.
     * @param language optional language code
     */
    @GET("/movie/{id}")
    Movie summary(
            @Path("id") int tmdbId,
            @Query("language") String language
    );

    /**
     * Get list of popular movies.
     */
    @GET("/movie/popular")
    TmdbResults popular();

    /**
     * Get list of popular movies, paged.
     *
     * @param page    optional integer >= 1
     * @param language optional language code
     */
    @GET("/movie/popular")
    TmdbResults popular(
            @Query("page") Integer page,
            @Query("language") String language
    );

    /**
     * Get list of top-rated movies (10 or more votes)
     */
    @GET("/movie/top_rated")
    TmdbResults topRated();

    /**
     * Get list of movies that have 10
     * or more votes.
     *
     * @param page     integer >= 1
     * @param language optional (use language code)
     */
    @GET("/movie/top_rated")
    TmdbResults topRated(
            @Query("page") Integer page,
            @Query("language") String language
    );

    @GET("/discover/movie")
    void discoverMovies(@Query("page") int page,
                        @Query("sort_by") String sort_by,
                   Callback<TmdbResults> resultsCallback);
}