package com.example.kavanmevada.myapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by kavanmevada on 09/08/17.
 */

class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    private final Activity activity;
    ArrayList<Album> listData;
    int[] MYPOS;

    public MyAdapter(Activity activity, ArrayList itemsData) {
        this.activity = activity;
        listData = itemsData;
        MYPOS = new int[itemsData.size()];
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View iteamView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.album_listview_recycler_layout, parent, false);
        return new MyViewHolder(iteamView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tracklist_albumart.setVisibility(View.INVISIBLE);
        holder.tv.setText(listData.get(position).getTitle());
        MYPOS[position] = position;
        new MyAsyncTask(activity, holder.tracklist_albumart, listData.get(position).getID(), MYPOS, position).execute();
    }

    @Override
    public void onViewDetachedFromWindow(MyViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        MYPOS[holder.getAdapterPosition()] = 0;
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private final TextView tv;
        ImageView tracklist_albumart;

        public MyViewHolder(View itemView) {
            super(itemView);

            tv = (TextView) itemView.findViewById(R.id.textView);
            tracklist_albumart = (ImageView) itemView.findViewById(R.id.imageView);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int pos = getAdapterPosition();

                    //ArrayList<Song> songInfo = Utils.getMeta_from_id(getActivity(), (Long) listData.get(pos));

                    android.app.FragmentManager fm = activity.getFragmentManager();
                    android.app.FragmentTransaction fragmentTransaction = fm.beginTransaction();


                    Bundle bundle = new Bundle();
                    bundle.putLong("album_id", listData.get(pos).getID());

                    AlbumDetailViewFragment f2 = new AlbumDetailViewFragment();

                    f2.setArguments(bundle);

                    fragmentTransaction.replace(R.id.main_fragment_wrapper, f2);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                }
            });
        }
    }
}
