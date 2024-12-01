package com.harsh.shah.saavnmp3.adapters;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.harsh.shah.saavnmp3.R;
import com.harsh.shah.saavnmp3.activities.ListActivity;
import com.harsh.shah.saavnmp3.modals.AlbumItem;

import java.util.HashMap;
import java.util.List;

public class ActivityMainArtistsItemAdapter extends RecyclerView.Adapter<ActivityMainArtistsItemAdapter.ActivityMainArtistsItemAdapterViewHolder> {

    private final List<String> data;

    public ActivityMainArtistsItemAdapter(List<String> data) {
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
        holder.itemView.setOnClickListener(v ->{
            v.getContext().startActivity(new Intent(v.getContext(), ListActivity.class));
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