package com.example.kavanmevada.myapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.constraint.solver.SolverVariable;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class MusicPlaybackServie extends Service {

    String TAG = "MusicPlaybackServie";

    private IBinder myBinder = new MyBinder();
    private MediaPlayer mediaPlayer;
    private ArrayList<Song> tracksarray = new ArrayList<Song>();
    private int pos = 0;
    private ArrayList playlistarray;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer = new MediaPlayer();

        return super.onStartCommand(intent, flags, startId);
    }



    void getFromShared(){
        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        Gson gson = new Gson();

        String get_json_array = appSharedPrefs.getString("tracklist_array", "");
        String get_json_position = appSharedPrefs.getString("track_position", "");

        Type array_type = new TypeToken<ArrayList<Song>>(){}.getType();
        Type int_type = new TypeToken<Integer>(){}.getType();


        if (gson.fromJson(get_json_array, array_type) != null){
            ArrayList<Song> retriveArray = gson.fromJson(get_json_array, array_type);
            playlistarray = retriveArray;
            //playlistarray = Utils.playlist_array(retriveArray, retrive_position);
            tracksarray = playlistarray;
        }

        if (gson.fromJson(get_json_position, int_type) != null) {
            int retrive_position = gson.fromJson(get_json_position, int_type);
            pos = retrive_position;
        }

        //send broadcast to mini player
        send_broadcast();
    }



    void setTrack(ArrayList<Song> al, int track_pos){

        tracksarray = al;
        pos = track_pos;

        SharedPreferences appSharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor prefsEditor = appSharedPrefs.edit();
        Gson gson = new Gson();

        String json_array = gson.toJson(tracksarray);
        String json_position = gson.toJson(track_pos);

        prefsEditor.putString("tracklist_array", json_array);
        prefsEditor.putString("track_position", json_position);

        prefsEditor.commit();


        send_broadcast();
    }

    void send_broadcast(){

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(AlbumListView.mBroadcastStringAction);

        if (tracksarray.isEmpty()){} else {
            String mini_player_title = tracksarray.get(pos).getTitle();
            broadcastIntent.putExtra("title", mini_player_title);

            long mini_player_album_uri = tracksarray.get(pos).getAlbumId();
            broadcastIntent.putExtra("album_id", mini_player_album_uri);

            //Log.d("DAMINI", String.valueOf(tracksarray.get(pos).getAlbumId()));
        }

        sendBroadcast(broadcastIntent);
    }

    void play(){

        if (tracksarray != null && pos >= 0){
            String uripath = tracksarray.get(pos).getUripath();

            mediaPlayer.reset();
            try {
                mediaPlayer.setDataSource(getApplicationContext(), Uri.parse(uripath));

                mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        mp.start();
                    }
                });
                mediaPlayer.prepareAsync();

            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Song is currupted", Toast.LENGTH_SHORT).show();
            }
        }

    }


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return myBinder;
    }

    public class MyBinder extends Binder {
        MusicPlaybackServie getService() {
            return MusicPlaybackServie.this;
        }
    }
}
