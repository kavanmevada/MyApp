package com.example.kavanmevada.myapp;

import android.app.Activity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;

import java.util.ArrayList;

public class Utils {

    public static ArrayList getAlbumList(Activity activity) {
        ArrayList al = new ArrayList();

        String[] projection = new String[] { MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Albums.ALBUM_ART, MediaStore.Audio.Albums.NUMBER_OF_SONGS };
        String selection = null;
        String[] selectionArgs = null;
        String sortOrder = MediaStore.Audio.Media.ALBUM + " ASC";
        Cursor musicCursor = activity.getContentResolver().query(MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI, projection, selection, selectionArgs, sortOrder);

        if (musicCursor != null && musicCursor.moveToFirst()) {
            //get columns
            int AlbumId = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums._ID);
            int AlbumName = musicCursor.getColumnIndex
                    (MediaStore.Audio.Albums.ALBUM);

            //add songs to list
            do {
                Long thisId = musicCursor.getLong(AlbumId);
                String thisName = musicCursor.getString(AlbumName);
                al.add(new Album(thisId, thisName, null, null));
            }
            while (musicCursor.moveToNext());
        }
        musicCursor.close();
        return al;
    }

    public static ArrayList get_album_info_from_id(Activity activity, Long id) {
        ArrayList<Album> al = new ArrayList<>();
        ArrayList<Song> sl = new ArrayList<>();

        Cursor albumCursor = activity.getContentResolver().query(
                MediaStore.Audio.Albums.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Albums._ID, MediaStore.Audio.Albums.ALBUM, MediaStore.Audio.Albums.ARTIST},
                MediaStore.Audio.Albums._ID + " = ?",
                new String[]{Long.toString(id)},
                null
        );

        if (albumCursor.moveToFirst()){

            String album_name = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ALBUM));
            String album_artist = albumCursor.getString(albumCursor.getColumnIndex(MediaStore.Audio.Albums.ARTIST));

            Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            String[] proj = new String[]{MediaStore.Audio.Media._ID, MediaStore.Audio.Media.TITLE, MediaStore.Audio.Albums.ARTIST, MediaStore.Audio.Media.ALBUM_ID};
            Cursor musicCursor = activity.getContentResolver().query(musicUri, proj, null, null, null);

            if(musicCursor!=null && musicCursor.moveToFirst()){
                do {
                    Long album_id = musicCursor.getLong(musicCursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID));
                    if (album_id == id) {
                        Long song_id = musicCursor.getLong(musicCursor.getColumnIndex(MediaStore.Audio.Media._ID));
                        String song_title = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                        String song_artist = musicCursor.getString(musicCursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                        String uripath = String.valueOf(Uri.parse(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI + "/" + song_id));

                        sl.add(new Song(song_id, song_title, song_artist, uripath, album_id));
                    }
                } while (musicCursor.moveToNext());
            }
            musicCursor.close();

            al.add(new Album(id, album_name, album_artist, sl));
        }

        albumCursor.close();

        return al;
    }

    public static ArrayList<Song> playlist_array(ArrayList<Song> al, int pos) {
        ArrayList<Song> pl = new ArrayList<Song>();
        do {
            pl.add(new Song(al.get(pos).getID(), al.get(pos).getTitle(), al.get(pos).getArtist(), al.get(pos).getUripath(), al.get(pos).getAlbumId()));
            pos++;
        } while (pos < al.size());
        return pl;
    }

}
