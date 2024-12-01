package com.harsh.shah.saavnmp3.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harsh.shah.saavnmp3.R;

public class ActivityMainPlaylistAdapter extends RecyclerView.Adapter<ActivityMainPlaylistAdapter.PlaylistAdapterViewHolder> {

    public ActivityMainPlaylistAdapter() {

    }

    @NonNull
    @Override
    public PlaylistAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View _v = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_main_playlist_item, null, false);
        _v.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new PlaylistAdapterViewHolder(_v);
    }

    @Override
    public int getItemCount() {
        return 10;
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapterViewHolder holder, int position) {

    }

    static class PlaylistAdapterViewHolder extends RecyclerView.ViewHolder {
        public PlaylistAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}