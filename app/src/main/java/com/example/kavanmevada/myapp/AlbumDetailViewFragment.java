package com.example.kavanmevada.myapp;

import android.app.Activity;
import android.app.Fragment;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AlbumDetailViewFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup vg,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.album_detailview_fragment_layout, vg, false);

        Long strtext = getArguments().getLong("album_id");

        //Log.d("KAVAN", String.valueOf(strtext));
        ArrayList<Album> album_meta = Utils.get_album_info_from_id(getActivity(), strtext);

        //ArrayList<Album> album_meta = Utils.get_album_info_from_id(getActivity(), strtext);

        ImageView album_detail_list_albumart = (ImageView) rootView.findViewById(R.id.album_detail_view_art);
        final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri albumart = ContentUris.withAppendedId(sArtworkUri, album_meta.get(0).getID());
        try {
            Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(albumart), null, null);
            album_detail_list_albumart.setImageBitmap(bitmap);
        } catch (FileNotFoundException e) {
        }


        // 1. get a reference to recyclerView
        RecyclerView recyclerView = (RecyclerView) rootView.findViewById(R.id.albumtracklist);

        // 2. set layoutManger
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));



        ArrayList musicList = album_meta.get(0).getSongs();


        // 3. create an adapter
        AlbumDetailViewFragment.MyAlbumAdapter mAdapter = new AlbumDetailViewFragment.MyAlbumAdapter(musicList);

        // 4. set adapter
        recyclerView.setAdapter(mAdapter);

        // 5. set item animator to DefaultAnimator
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        return rootView;
    }


    private class MyAlbumAdapter extends RecyclerView.Adapter<MyAlbumAdapter.MyViewHolder> {

        ArrayList<Song> listData;

        public MyAlbumAdapter(ArrayList itemsData) {
            listData = itemsData;
        }

        @Override
        public MyAlbumAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View iteamView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.album_detail_track_listview_recycler_layout, parent, false);
            return new MyAlbumAdapter.MyViewHolder(iteamView);
        }

        @Override
        public void onBindViewHolder(MyAlbumAdapter.MyViewHolder holder, int position) {
            holder.tv.setText(listData.get(position).getTitle());
        }

        @Override
        public int getItemCount() {
            return listData.size();
        }


        public class MyViewHolder extends RecyclerView.ViewHolder {
            private final TextView tv;

            public MyViewHolder(View itemView) {
                super(itemView);

                tv = (TextView) itemView.findViewById(R.id.album_track_list_textview);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = getAdapterPosition();

                        testService.setTrack(listData, pos);
                        testService.play();

                    }
                });
            }
        }
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


    MusicPlaybackServie testService;
    boolean isBound = false;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicPlaybackServie.MyBinder binder = (MusicPlaybackServie.MyBinder) service;
            testService = binder.getService();
            isBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            testService = null;
            isBound = false;
        }
    };
}