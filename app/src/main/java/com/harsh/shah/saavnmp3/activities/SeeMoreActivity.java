package com.harsh.shah.saavnmp3.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.harsh.shah.saavnmp3.adapters.ActivitySeeMoreListAdapter;
import com.harsh.shah.saavnmp3.databinding.ActivitySeeMoreBinding;
import com.harsh.shah.saavnmp3.network.ApiManager;
import com.harsh.shah.saavnmp3.network.utility.RequestNetwork;
import com.harsh.shah.saavnmp3.records.ArtistAllSongs;
import com.paginate.Paginate;

import java.util.HashMap;

public class SeeMoreActivity extends AppCompatActivity {

    ActivitySeeMoreBinding binding;
    private int totalItems = 0;
    private int currentPage = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySeeMoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerView.setAdapter(activitySeeMoreListAdapter);

        Paginate.Callbacks callbacks = new Paginate.Callbacks() {
            @Override
            public void onLoadMore() {
                requestDataNext();
            }

            @Override
            public boolean isLoading() {
                return isLoading;
            }

            @Override
            public boolean hasLoadedAllItems() {
                return currentPage == totalItems/10;
            }
        };

        Paginate.with(binding.recyclerView, callbacks)
                .setLoadingTriggerThreshold(2)
                .addLoadingListItem(true)
                .build();

        showData();

    }

    private void showData() {
        if(getIntent().getExtras() == null) finish();
        binding.toolbarText.setText(getIntent().getExtras().getString("artist_name"));
        artistId = getIntent().getExtras().getString("id");
        requestDataFirst();
    }

    private String artistId = "";
    private final ActivitySeeMoreListAdapter activitySeeMoreListAdapter = new ActivitySeeMoreListAdapter(ActivitySeeMoreListAdapter.Mode.TOP_SONGS);

    private void requestDataFirst() {
        final ApiManager apiManager = new ApiManager(this);
        apiManager.retrieveArtistSongs(artistId, 0,null,null, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                ArtistAllSongs artistAllSongs = new Gson().fromJson(response, ArtistAllSongs.class);
                if(!artistAllSongs.success()) finish();
                isLoading = false;
                currentPage = 0;
                totalItems = artistAllSongs.data().total();
                activitySeeMoreListAdapter.addAll(artistAllSongs.data().songs());
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                isLoading = false;
                Toast.makeText(SeeMoreActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void requestDataNext() {
        currentPage++;
        final ApiManager apiManager = new ApiManager(this);
        apiManager.retrieveArtistSongs(artistId, currentPage,null,null, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                ArtistAllSongs artistAllSongs = new Gson().fromJson(response, ArtistAllSongs.class);
                if(!artistAllSongs.success()) finish();
                isLoading = false;
                activitySeeMoreListAdapter.addAll(artistAllSongs.data().songs());
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                isLoading = false;
                Toast.makeText(SeeMoreActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void backPress(View view) {
        finish();
    }
}