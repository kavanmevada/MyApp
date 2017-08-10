package com.example.kavanmevada.myapp;

public class Song {

    private Long album_id;
    private String uripath;
    private long id;
    private String title;
    private String artist;

    public Song(long songID, String songTitle, String songArtist, String uriPath, Long albumID) {
        id = songID;
        title = songTitle;
        artist = songArtist;
        uripath = uriPath;
        album_id = albumID;
    }

    public long getID() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getUripath() {
        return uripath;
    }

    public Long getAlbumId() {
        return album_id;
    }
}