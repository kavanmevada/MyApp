package com.example.kavanmevada.myapp;

import android.app.Activity;
import android.content.ContentUris;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.View;
import android.widget.ImageView;

import java.io.FileNotFoundException;

/**
 * Created by kavanmevada on 09/08/17.
 */

class MyAsyncTask extends AsyncTask<String, String, Bitmap> {
    private final long AlbumID;
    private final ImageView ImageViewHolder;
    private final int p;
    private final int[] MYPOS;
    private final Activity activity;

    public MyAsyncTask(Activity activity, ImageView imgHolder, long albumid, int[] posArray, int position) {
        this.activity = activity;
        AlbumID = albumid;
        ImageViewHolder = imgHolder;
        p = position;
        MYPOS = posArray;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        super.onPostExecute(bitmap);
        if (MYPOS[p] == p) {
            //ImageViewHolder.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 120, 120, false));
            if (bitmap != null){
                ImageViewHolder.setImageBitmap(bitmap);
                ImageViewHolder.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        if (MYPOS[p] == p) {
            //final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
            Uri albumart = ContentUris.withAppendedId(Uri.parse("content://media/external/audio/albumart"), AlbumID);
            try {
                bitmap = BitmapFactory.decodeStream(activity.getContentResolver().openInputStream(albumart), null, null);
            } catch (FileNotFoundException e) {
                bitmap = null;
            }
        }
        return bitmap;
    }

}
