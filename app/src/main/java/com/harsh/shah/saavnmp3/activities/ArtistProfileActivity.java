package com.harsh.shah.saavnmp3.activities;

import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.harsh.shah.saavnmp3.adapters.ActivityArtistProfileTopAlbumsAdapter;
import com.harsh.shah.saavnmp3.adapters.ActivityArtistProfileTopSongsAdapter;
import com.harsh.shah.saavnmp3.databinding.ActivityArtistProfileBinding;
import com.harsh.shah.saavnmp3.network.ApiManager;
import com.harsh.shah.saavnmp3.network.utility.RequestNetwork;
import com.harsh.shah.saavnmp3.records.ArtistSearch;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

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
        final String artistId = getIntent().getExtras().getString("artist_id", "null");
        final ApiManager apiManager = new ApiManager(this);
        apiManager.retrieveArtistById(artistId, null, null, null, null, null, new RequestNetwork.RequestListener() {
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
            }
        });
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