package com.yalantis.multiselectdemo.demo.model;

import android.support.annotation.DrawableRes;
import android.support.v4.app.Fragment;

import java.io.Serializable;

/**
 * Created by Artem Kholodnyi on 9/6/16.
 */
public class Track implements Comparable<Track>, Serializable {
    private String trackName;
    private @DrawableRes int album;
    private String artist;

    public Track(String trackName, String artist, @DrawableRes int album) {
        this.trackName = trackName;
        this.artist = artist;
        this.album = album;
    }

    public String getTrackName() {
        return trackName;
    }

    public void setTrackName(String trackName) {
        this.trackName = trackName;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public int getAlbum() {
        return album;
    }

    public void setAlbum(int album) {
        this.album = album;
    }

    @Override
    public int compareTo(Track track) {
        return trackName.compareTo(track.trackName);
    }

}
