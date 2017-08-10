package com.example.kavanmevada.myapp;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Set font to whole app
        TypefaceUtil.overrideFont(getApplicationContext(), "SERIF", "fonts/HammersmithOne-Regular.ttf");


        setContentView(R.layout.activity_main);

        TextView album_btn = (TextView) findViewById(R.id.album_btn);
        album_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AlbumListView.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //Check service is running or not
        if (isMyServiceRunning() == false) {
            Log.d("INFO", "Service not Running");
            Intent serviceIntent = new Intent(this, MusicPlaybackServie.class);
            startService(serviceIntent);
        }
    }

    //Service function..
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if ("com.example.kavanmevada.myapp.MusicPlaybackServie".equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
