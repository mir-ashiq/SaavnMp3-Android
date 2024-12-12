package com.harsh.shah.saavnmp3.activities;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.harsh.shah.saavnmp3.adapters.ActivityListSongsItemAdapter;
import com.harsh.shah.saavnmp3.databinding.ActivityListBinding;
import com.harsh.shah.saavnmp3.model.AlbumItem;
import com.harsh.shah.saavnmp3.network.ApiManager;
import com.harsh.shah.saavnmp3.network.utility.RequestNetwork;
import com.harsh.shah.saavnmp3.records.PlaylistSearch;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class ListActivity extends AppCompatActivity {

    ActivityListBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        //binding.recyclerView.setAdapter(new ActivityListSongsItemAdapter());

        showData();
    }

    private void showData() {
        if (getIntent().getExtras() == null) return;
        final AlbumItem albumItem = new Gson().fromJson(getIntent().getExtras().getString("data"), AlbumItem.class);
        binding.albumTitle.setText(albumItem.albumTitle());
        binding.albumSubTitle.setText(albumItem.albumSubTitle());
        Picasso.get().load(Uri.parse(albumItem.albumCover())).into(binding.albumCover);
        final ApiManager apiManager = new ApiManager(this);
        apiManager.retrievePlaylistById(albumItem.id(), 0, 50, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                PlaylistSearch playlistSearch = new Gson().fromJson(response, PlaylistSearch.class);
                if (playlistSearch.success()) {
                    binding.recyclerView.setAdapter(new ActivityListSongsItemAdapter(playlistSearch.data().songs()));
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {

            }
        });
    }

    public void backPress(View view) {
        finish();
    }

}