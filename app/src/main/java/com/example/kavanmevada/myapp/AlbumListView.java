package com.example.kavanmevada.myapp;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AlbumListView extends AppCompatActivity {

    public static final String mBroadcastStringAction = "com.kavanmevada.broadcast.string";
    //Create service connection..
    private IntentFilter mIntentFilter;

    //Create broadcast reciever [update miniplayer]
    private BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(mBroadcastStringAction)) {

                TextView main_text = (TextView) findViewById(R.id.textView2);
                ImageView main_album_art = (ImageView) findViewById(R.id.imageView2);

                String title = intent.getStringExtra("title");
                Long album_id = intent.getLongExtra("album_id", 0);
                Uri albumart = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), album_id);

                try {
                    Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(albumart), null, null);
                    main_album_art.setImageBitmap(bitmap);
                } catch (FileNotFoundException e) { }

                main_text.setText(title);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_list_view);

        //Create broadcast
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction(mBroadcastStringAction);

        Button play_btn = (Button) findViewById(R.id.play_btn);
        play_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                testService.play();
            }
        });


        //Create fragment wrapper
        android.app.FragmentManager fm = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();

        //Trigger first wrapper
        AlbumListViewFragment f1 = new AlbumListViewFragment();
        fragmentTransaction.replace(R.id.main_fragment_wrapper, f1);
        fragmentTransaction.commit();

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

    @Override
    public void onResume() {
        super.onResume();
        registerReceiver(mReceiver, mIntentFilter);

        Intent intent = new Intent(this, MusicPlaybackServie.class);
        this.bindService(intent, serviceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    public void onPause() {
        super.onPause();
        unregisterReceiver(mReceiver);

        this.unbindService(serviceConnection);
    }


}
