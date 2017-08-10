package com.example.kavanmevada.myapp;

import java.util.ArrayList;

/**
 * Created by kavanmevada on 03/08/17.
 */

public class Album {

    private ArrayList<Song> al;
    private long id;
    private String title;
    private String artist;

    public Album(long songID, String songTitle, String songArtist, ArrayList<Song> songArray) {
        id = songID;
        title = songTitle;
        artist = songArtist;
        al = songArray;
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

    public ArrayList<Song> getSongs() {
        return al;
    }
}
