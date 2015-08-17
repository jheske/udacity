package com.nano.movies.web;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.nano.movies.utils.ApiKey;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by jill on 7/25/2015.
 */
public class Tmdb {

    /**
     * Tmdb API URL.
     */
    private static final String MOVIE_SERVICE_URL = "https://api.themoviedb.org/3";

    /**
     * API key query parameter name.
     * This has to be appended to every request.
     */
    private static final String PARAM_API_KEY = "api_key";
    private boolean isDebug;
    private RestAdapter restAdapter;


    /**
     * A few image size constants selected from
     * https://api.themoviedb.org/3/configuration?api_key=xxx
     * They're kind of arbitrary,
     * but I don't know how else to pick them.
     * Put them here vs MovieServiceProxy in case
     * other service might need them.
     */
    public final String IMAGE_POSTER_ORIGINAL = "original";
    public static final String IMAGE_POSTER_XSMALL = "w92";
    public static final String IMAGE_POSTER_SMALL = "w185";
    public static final String IMAGE_POSTER_MED = "w342";
    public final String IMAGE_POSTER_LARGE = "w500";
    public final String IMAGE_POSTER_XLARGE = "w780";

    /**
     * Constructor to create a new manager instance.
     */
    public Tmdb() {
    }

    public static String getMovieImageUrl(String mImagePath, String imageSize) {
        return ("http://image.tmdb.org/t/p/" + imageSize + "/" + mImagePath);
    }

    /**
     * Set RestAdapter log level.
     *
     * @param isDebug true = LogLevel set to FULL
     */
    public void setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        if (restAdapter != null) {
            restAdapter.setLogLevel(isDebug ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE);
        }
    }

    /**
     * Create a RestAdapterBuilder to help build the adapter
     */
    private RestAdapter.Builder restAdapterBuilder() {
        return new RestAdapter.Builder();
    }

    /**
     * Create the RestAdapter
     */
    private RestAdapter getRestAdapter() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd")
                .create();

        if (restAdapter == null) {
            RestAdapter.Builder builder = restAdapterBuilder();
            builder.setConverter(new GsonConverter(gson));
            builder.setEndpoint(MOVIE_SERVICE_URL);
            builder.setRequestInterceptor(new RequestInterceptor() {
                // Add API_KEY to every API request
                // Always request releases and trailers

                /**********
                 *
                 *  My ApiKey Class is in .gitignore so it is
                 *  excluded from   my repo.
                 *  REPLACE ApiKey.getApiKey() WITH YOUR OWN KEY STRING
                 *
                 */
                @Override
                public void intercept(RequestInterceptor.RequestFacade requestFacade) {
                    requestFacade.addQueryParam(PARAM_API_KEY, ApiKey.getApiKey());
                }
            });
            if (isDebug) {
                builder.setLogLevel(RestAdapter.LogLevel.FULL);
            }
            restAdapter = builder.build();
        }
        return restAdapter;
    }

    /**
     *  Proxy to the Tmdb Movie service.
     *  There are other proxies I can add later,
     *  like Search and TV.
     */
    public MovieServiceProxy moviesServiceProxy() {
        return getRestAdapter().create(MovieServiceProxy.class);
    }
}
