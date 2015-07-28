package com.nano.movies.web;

import retrofit.RequestInterceptor;
import retrofit.RestAdapter;

/**
 * Created by jill on 7/25/2015.
 */
public class Tmdb {

    /**
     * Tmdb API URL.
     */
    public static final String MOVIE_SERVICE_URL = "https://api.themoviedb.org/3";

    /**
     * API key query parameter name.
     * This has to be appended to every request.
     */
    public static final String PARAM_API_KEY = "api_key";

    private String apiKey = "***REMOVED***";
    private boolean isDebug=true;
    private RestAdapter restAdapter;

    /**
     * Create a new manager instance.
     */
    public Tmdb() {
    }

    /**
     * Set the TMDB API key.
     * <p>
     * The next service method call will trigger a rebuild of the {@link retrofit.RestAdapter}. If you have cached any
     * service instances, get a new one from its service method.
     *
     * @param value Your TMDB API key.
     */
    public Tmdb setApiKey(String value) {
        this.apiKey = value;
        restAdapter = null;
        return this;
    }

    /**
     * Set RestAdapter log level.
     *
     * @param isDebug true = LogLevel set to FULL
     */
    public Tmdb setIsDebug(boolean isDebug) {
        this.isDebug = isDebug;
        if (restAdapter != null) {
            restAdapter.setLogLevel(isDebug ? RestAdapter.LogLevel.FULL : RestAdapter.LogLevel.NONE);
        }
        return this;
    }

    /**
     * Create a RestAdapterBuilder to help build the adapter
     */
    protected RestAdapter.Builder restAdapterBuilder() {
        return new RestAdapter.Builder();
    }

    /**
     * Create the RestAdapter
     */
    protected RestAdapter getRestAdapter() {
        if (restAdapter == null) {
            RestAdapter.Builder builder = restAdapterBuilder();

            builder.setEndpoint(MOVIE_SERVICE_URL);
            builder.setRequestInterceptor(new RequestInterceptor() {
                // Add API_KEY to every API request
                @Override
                public void intercept(RequestInterceptor.RequestFacade requestFacade) {
                    requestFacade.addQueryParam(PARAM_API_KEY, apiKey);
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
