package com.example.kavanmevada.myapp;

import android.app.Fragment;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

public class AlbumListViewFragment extends Fragment {

    String TAG = "Fragment1";

    MusicPlaybackServie testService;
    boolean isBound = false;
    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlaybackServie.MyBinder binder = (MusicPlaybackServie.MyBinder) service;
            testService = binder.getService();
            isBound = true;

            testService.getFromShared();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            testService = null;
            isBound = false;
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.album_listview_fragment_layout, vg, false);


        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.list);

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        ArrayList musicList = Utils.getAlbumList(getActivity());


        // 3. create an adapter
        MyAdapter mAdapter = new MyAdapter(getActivity(), musicList);

        // 4. set adapter
        recyclerView.setAdapter(mAdapter);

        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());


        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Intent intent = new Intent(getActivity(), MusicPlaybackServie.class);
        getActivity().bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unbindService(serviceConnection);
    }

}
