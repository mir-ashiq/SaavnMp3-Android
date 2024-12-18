package com.harsh.shah.saavnmp3.activities;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.harsh.shah.saavnmp3.ApplicationClass;
import com.harsh.shah.saavnmp3.adapters.ActivityListSongsItemAdapter;
import com.harsh.shah.saavnmp3.databinding.ActivityListBinding;
import com.harsh.shah.saavnmp3.model.AlbumItem;
import com.harsh.shah.saavnmp3.network.ApiManager;
import com.harsh.shah.saavnmp3.network.utility.RequestNetwork;
import com.harsh.shah.saavnmp3.records.AlbumSearch;
import com.harsh.shah.saavnmp3.records.PlaylistSearch;
import com.harsh.shah.saavnmp3.records.SongResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListActivity extends AppCompatActivity {

    ActivityListBinding binding;

    private List<String> trackQueue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<SongResponse.Song> data = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            data.add(new SongResponse.Song(
                    "<shimmer>",
                    "",
                    "",
                    "",
                    "",
                    0.0,
                    "",
                    false,
                    0,
                    "",
                    false,
                    "",
                    null,
                    "",
                    "",
                    null,
                    null, null, null
            ));
        }
        binding.recyclerView.setAdapter(new ActivityListSongsItemAdapter(data));

        binding.playAllBtn.setOnClickListener(view -> {
            if (!trackQueue.isEmpty()) {
                ((ApplicationClass)getApplicationContext()).setTrackQueue(trackQueue);
                ((ApplicationClass)getApplicationContext()).nextTrack();
            }
        });

        showData();
    }

    private void showData() {
        if (getIntent().getExtras() == null) return;
        final AlbumItem albumItem = new Gson().fromJson(getIntent().getExtras().getString("data"), AlbumItem.class);
        binding.albumTitle.setText(albumItem.albumTitle());
        binding.albumSubTitle.setText(albumItem.albumSubTitle());
        Picasso.get().load(Uri.parse(albumItem.albumCover())).into(binding.albumCover);

        final ApiManager apiManager = new ApiManager(this);
        if (getIntent().getExtras().getString("type", "").equals("album")) {
            apiManager.retrieveAlbumById(albumItem.id(), new RequestNetwork.RequestListener() {
                @Override
                public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                    AlbumSearch albumSearch = new Gson().fromJson(response, AlbumSearch.class);
                    if (albumSearch.success()) {
                        binding.recyclerView.setAdapter(new ActivityListSongsItemAdapter(albumSearch.data().songs()));
                        for (SongResponse.Song song : albumSearch.data().songs())
                            trackQueue.add(song.id());
                    }
                }

                @Override
                public void onErrorResponse(String tag, String message) {

                }
            });
            return;
        }
        apiManager.retrievePlaylistById(albumItem.id(), 0, 50, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                Log.i("API_RESPONSE", "onResponse: " + response);
                PlaylistSearch playlistSearch = new Gson().fromJson(response, PlaylistSearch.class);
                if (playlistSearch.success()) {
                    binding.recyclerView.setAdapter(new ActivityListSongsItemAdapter(playlistSearch.data().songs()));
                    for (SongResponse.Song song : playlistSearch.data().songs())
                        trackQueue.add(song.id());
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