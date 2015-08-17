package com.nano.movies.web;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * This "Plain Ol' Java Object" (POJO) class represents data of
 * interest downloaded in Json from the MovieServiceProxy.  We
 * don't care about all the data, just the fields defined in this
 * class.
 */
public class Movie implements Parcelable {
    /*
     * These fields store the Tmdb state.
     * Typically we would use
     * the @SerializedName annotation to make an explicit mapping
     * between each member variable and its Json name, as in.
     * However, because we named these fields the same as the Json
     * names we won't need to use this annotation.
     */
    @SerializedName("id")
    private Integer id;
    @SerializedName("homepage")
    private String mHomePage;
    @SerializedName("original_title")
    private String mOriginalTitle;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("popularity")
    private Double mPopularity;
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("release_date")
    private Date mReleaseDate;
    @SerializedName("runtime")
    private Integer mRuntime;
    @SerializedName("tagline")
    private String mTagline;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("vote_average")
    private Double mVoteAverage;
    @SerializedName("vote_count")
    private Integer mVoteCount;
    // Used with append_to_response=trailers
    private Trailers mTrailers;

    public Movie(String originalTitle) {
        mOriginalTitle = originalTitle;
    }

    /**
     * Getters and Setters
     *
     */
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getHomePage() {
        return mHomePage;
    }

    public void setHomePage(String mHomePage) {
        this.mHomePage = mHomePage;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public void setOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public String getOverview() {
        return mOverview;
    }

    public void setOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public Double getPopularity() {
        return mPopularity;
    }

    public void setPopularity(Double mPopularity) {
        this.mPopularity = mPopularity;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public void setPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public Date getReleaseDate() {
        return mReleaseDate;
    }

    public void setReleaseDate(Date mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public Integer getRuntime() {
        return mRuntime;
    }

    public void setRuntime(Integer mRuntime) {
        this.mRuntime = mRuntime;
    }

    public String getTagline() {
        return mTagline;
    }

    public void setTagline(String mTagline) {
        this.mTagline = mTagline;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Double getVoteAverage() {
        return mVoteAverage;
    }

    public void setVoteAverage(Double mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public Integer getVoteCount() {
        return mVoteCount;
    }

    public void setVoteCount(Integer mVoteCount) {
        this.mVoteCount = mVoteCount;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(mHomePage);
        dest.writeString(mOriginalTitle);
        dest.writeString(mOverview);
        dest.writeDouble(mPopularity);
        dest.writeString(mPosterPath);
        dest.writeLong(mReleaseDate.getTime());
        dest.writeInt(mRuntime);
        dest.writeString(mTagline);
        dest.writeString(mTitle);
        dest.writeDouble(mVoteAverage);
        dest.writeInt(mVoteCount);
        dest.writeParcelable(mTrailers, flags);
    }

    /**
     * Private constructor provided for the CREATOR interface, which
     * is used to de-marshal an WeatherData from the Parcel of data.
     * <p/>
     * The order of reading in variables HAS TO MATCH the order in
     * writeToParcel(Parcel, int)
     *
     * NOTE Use getClassLoader to load mTrailers, which parses itself in
     * the Trailers class.
     *
     * @param in
     */
    private Movie(Parcel in) {
        id = in.readInt();
        mHomePage = in.readString();
        mOriginalTitle = in.readString();
        mOverview = in.readString();
        mPopularity = in.readDouble();
        mPosterPath = in.readString();
        mReleaseDate = new Date(in.readLong());
        mRuntime = in.readInt();
        mTagline = in.readString();
        mTitle = in.readString();
        mVoteAverage = in.readDouble();
        mVoteCount = in.readInt();
        mTrailers = Trailers.CREATOR.createFromParcel(in);
    }

    /**
     * A bitmask indicating the set of special object types marshaled
     * by the Parcelable.
     */
    @Override
    public int describeContents() {
        return 0;
    }

    /**
     * public Parcelable.Creator for WeatherData, which is an
     * interface that must be implemented and provided as a public
     * CREATOR field that generates instances of your Parcelable class
     * from a Parcel.
     */
    public static final Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>() {
                public Movie createFromParcel(Parcel in) {
                    return new Movie(in);
                }

                public Movie[] newArray(int size) {
                    return new Movie[size];
                }
            };
}



