package com.harsh.shah.saavnmp3.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harsh.shah.saavnmp3.R;
import com.harsh.shah.saavnmp3.model.AlbumItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActivityMainPlaylistAdapter extends RecyclerView.Adapter<ActivityMainPlaylistAdapter.PlaylistAdapterViewHolder> {

    private final List<AlbumItem> data;

    public ActivityMainPlaylistAdapter(List<AlbumItem> data) {
        this.data = data;
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
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapterViewHolder holder, int position) {
        ((TextView) holder.itemView.findViewById(R.id.title)).setText(data.get(position).getalbumTitle());
        ImageView imageView = holder.itemView.findViewById(R.id.imageView);
        Picasso.get().load(Uri.parse(data.get(position).getAlbumCover())).into(imageView);
    }

    static class PlaylistAdapterViewHolder extends RecyclerView.ViewHolder {
        public PlaylistAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}