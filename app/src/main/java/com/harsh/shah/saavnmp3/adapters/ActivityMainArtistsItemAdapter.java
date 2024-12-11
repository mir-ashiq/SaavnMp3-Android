package com.harsh.shah.saavnmp3.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.harsh.shah.saavnmp3.R;
import com.harsh.shah.saavnmp3.activities.ArtistProfileActivity;
import com.harsh.shah.saavnmp3.records.ArtistsSearch;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActivityMainArtistsItemAdapter extends RecyclerView.Adapter<ActivityMainArtistsItemAdapter.ActivityMainArtistsItemAdapterViewHolder> {

    private final List<ArtistsSearch.Data.Results> data;

    public ActivityMainArtistsItemAdapter(List<ArtistsSearch.Data.Results> data) {
        this.data = data;
    }

    @NonNull
    @Override
    public ActivityMainArtistsItemAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View _v = View.inflate(parent.getContext(), R.layout.activity_main_artists_item, null);
        _v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ActivityMainArtistsItemAdapterViewHolder(_v);
    }

    @Override
    public void onBindViewHolder(@NonNull ActivityMainArtistsItemAdapterViewHolder holder, int position) {
        holder.itemView.findViewById(R.id.artist_name).setSelected(true);
        ((TextView) holder.itemView.findViewById(R.id.artist_name)).setText(data.get(position).name());
        ImageView imageView = holder.itemView.findViewById(R.id.artist_img);
        Picasso.get().load(Uri.parse(data.get(position).image().get(data.get(position).image().size() - 1).url())).into(imageView);

        holder.itemView.setOnClickListener(v -> {
            v.getContext().startActivity(new Intent(v.getContext(), ArtistProfileActivity.class).putExtra("artist", new Gson().toJson(data.get(position))));
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public static class ActivityMainArtistsItemAdapterViewHolder extends RecyclerView.ViewHolder {
        public ActivityMainArtistsItemAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}