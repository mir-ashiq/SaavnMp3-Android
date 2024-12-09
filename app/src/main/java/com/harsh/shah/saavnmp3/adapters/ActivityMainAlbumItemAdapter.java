package com.harsh.shah.saavnmp3.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harsh.shah.saavnmp3.R;
import com.harsh.shah.saavnmp3.activities.ListActivity;
import com.harsh.shah.saavnmp3.activities.MusicOverviewActivity;
import com.harsh.shah.saavnmp3.model.AlbumItem;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActivityMainAlbumItemAdapter extends RecyclerView.Adapter<ActivityMainAlbumItemAdapter.ActivityMainAlbumItemAdapterViewHolder> {

    private final List<AlbumItem> data;

    public ActivityMainAlbumItemAdapter(List<AlbumItem> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ActivityMainAlbumItemAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View _v = View.inflate(parent.getContext(), R.layout.activity_main_songs_item, null);
        _v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ActivityMainAlbumItemAdapterViewHolder(_v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityMainAlbumItemAdapterViewHolder holder, int position) {
        ((TextView) holder.itemView.findViewById(R.id.albumTitle)).setText(data.get(position).getalbumTitle());
        ((TextView) holder.itemView.findViewById(R.id.albumSubTitle)).setText(data.get(position).getAlbumSubTitle());

        holder.itemView.findViewById(R.id.albumTitle).setSelected(true);
        holder.itemView.findViewById(R.id.albumSubTitle).setSelected(true);

        holder.itemView.setOnClickListener(view -> {
            holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), MusicOverviewActivity.class));
        });

        ImageView coverImage = holder.itemView.findViewById(R.id.coverImage);
        Picasso.get().load(Uri.parse(data.get(position).albumCover)).into(coverImage);

        holder.itemView.setOnClickListener(v -> {
            v.getContext().startActivity(new Intent(v.getContext(), ListActivity.class));
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ActivityMainAlbumItemAdapterViewHolder extends RecyclerView.ViewHolder {
        public ActivityMainAlbumItemAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
