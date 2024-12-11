package com.harsh.shah.saavnmp3.activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.harsh.shah.saavnmp3.adapters.ActivityArtistProfileTopAlbumsAdapter;
import com.harsh.shah.saavnmp3.adapters.ActivityArtistProfileTopSongsAdapter;
import com.harsh.shah.saavnmp3.databinding.ActivityArtistProfileBinding;
import com.harsh.shah.saavnmp3.network.ApiManager;
import com.harsh.shah.saavnmp3.network.utility.RequestNetwork;
import com.harsh.shah.saavnmp3.records.AlbumsSearch;
import com.harsh.shah.saavnmp3.records.ArtistSearch;
import com.harsh.shah.saavnmp3.records.ArtistsSearch;
import com.harsh.shah.saavnmp3.records.SongResponse;
import com.harsh.shah.saavnmp3.utils.NetworkUtil;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ArtistProfileActivity extends AppCompatActivity {

    private final String TAG = "ArtistProfileActivity";
    ActivityArtistProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtistProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.collapsingToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        binding.collapsingToolbarLayout.setTitle("Artist Name");

        binding.collapsingToolbarAppbarlayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
            } else {
            }
        });

        binding.topSongsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.topAlbumsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.topSinglesRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        showData();
    }

    void showData() {
        if (getIntent().getExtras() == null) return;
        final String artist = getIntent().getExtras().getString("artist", "null");
        final ArtistsSearch.Data.Results artistItem = new Gson().fromJson(artist, ArtistsSearch.Data.Results.class);
        if (artistItem == null) return;
        final String artistId = artistItem.id();

        Picasso.get().load(Uri.parse(artistItem.image().get(artistItem.image().size() - 1).url())).into(binding.artistImg);
        binding.artistName.setText(artistItem.name());
        binding.collapsingToolbarLayout.setTitle(artistItem.name());

        final List<SongResponse.Song> shimmerData = getShimmerData();
        final List<AlbumsSearch.Data.Results> shimmerDataAlbum = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            shimmerDataAlbum.add(new AlbumsSearch.Data.Results(
                    "<shimmer>",
                    null,
                    null,
                    null,
                    0,
                    null,
                    0,
                    null,
                    false,
                    null,
                    null
            ));
        }


        binding.topSongsRecyclerview.setAdapter(new ActivityArtistProfileTopSongsAdapter(shimmerData));
        binding.topAlbumsRecyclerview.setAdapter(new ActivityArtistProfileTopAlbumsAdapter(shimmerDataAlbum));
        binding.topSinglesRecyclerview.setAdapter(new ActivityArtistProfileTopAlbumsAdapter(shimmerDataAlbum));

        final ApiManager apiManager = new ApiManager(this);
        apiManager.retrieveArtistById(artistId, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                ArtistSearch artistSearch = new Gson().fromJson(response, ArtistSearch.class);
                Log.i(TAG, "onResponse: " + response);
                if (artistSearch.success()) {
                    Picasso.get().load(Uri.parse(artistSearch.data().image().get(artistSearch.data().image().size() - 1).url())).into(binding.artistImg);
                    binding.artistName.setText(artistSearch.data().name());
                    binding.collapsingToolbarLayout.setTitle(artistSearch.data().name());
                    binding.topSongsRecyclerview.setAdapter(new ActivityArtistProfileTopSongsAdapter(artistSearch.data().topSongs()));
                    binding.topAlbumsRecyclerview.setAdapter(new ActivityArtistProfileTopAlbumsAdapter(artistSearch.data().topAlbums()));
                    binding.topSinglesRecyclerview.setAdapter(new ActivityArtistProfileTopAlbumsAdapter(artistSearch.data().singles()));
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                Log.i(TAG, "onErrorResponse: " + message);
                if (!NetworkUtil.isNetworkAvailable(ArtistProfileActivity.this)) {
                    try {
                        Thread.sleep(2000);
                        showData();
                    } catch (InterruptedException e) {
                        Log.e(TAG, "onErrorResponse: ", e);
                    }
                }
            }
        });
    }

    @NonNull
    private static List<SongResponse.Song> getShimmerData() {
        final List<SongResponse.Song> shimmerData = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            shimmerData.add(new SongResponse.Song(
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
                    new SongResponse.Lyrics("", "", ""),
                    "",
                    "",
                    new SongResponse.Album("", "", ""),
                    new SongResponse.Artists(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                    new ArrayList<>(),
                    new ArrayList<>()
            ));
        }
        return shimmerData;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}