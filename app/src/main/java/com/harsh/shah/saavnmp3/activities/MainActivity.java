package com.harsh.shah.saavnmp3.activities;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.harsh.shah.saavnmp3.ApplicationClass;
import com.harsh.shah.saavnmp3.R;
import com.harsh.shah.saavnmp3.adapters.ActivityMainAlbumItemAdapter;
import com.harsh.shah.saavnmp3.adapters.ActivityMainArtistsItemAdapter;
import com.harsh.shah.saavnmp3.adapters.ActivityMainPlaylistAdapter;
import com.harsh.shah.saavnmp3.adapters.ActivityMainPopularSongs;
import com.harsh.shah.saavnmp3.databinding.ActivityMainBinding;
import com.harsh.shah.saavnmp3.model.AlbumItem;
import com.harsh.shah.saavnmp3.network.ApiManager;
import com.harsh.shah.saavnmp3.network.NetworkChangeReceiver;
import com.harsh.shah.saavnmp3.network.utility.RequestNetwork;
import com.harsh.shah.saavnmp3.records.AlbumsSearch;
import com.harsh.shah.saavnmp3.records.ArtistsSearch;
import com.harsh.shah.saavnmp3.records.PlaylistsSearch;
import com.harsh.shah.saavnmp3.records.SongSearch;
import com.harsh.shah.saavnmp3.utils.NetworkUtil;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "MainActivity";
    ActivityMainBinding binding;
    ApplicationClass applicationClass;
    final List<AlbumItem> songs = new ArrayList<>();
    final List<ArtistsSearch.Data.Results> artists = new ArrayList<>();
    final List<AlbumItem> albums = new ArrayList<>();
    final List<AlbumItem> playlists = new ArrayList<>();

    NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver(new NetworkChangeReceiver.NetworkStatusListener() {
        @Override
        public void onNetworkConnected() {
            if (songs.isEmpty() || artists.isEmpty() || albums.isEmpty() || playlists.isEmpty())
                showData();
        }

        @Override
        public void onNetworkDisconnected() {
            if (songs.isEmpty() || artists.isEmpty() || albums.isEmpty() || playlists.isEmpty())
                showOfflineData();
            Snackbar.make(binding.getRoot(), "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    });

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthDp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        applicationClass = (ApplicationClass) getApplicationContext();

        ApplicationClass.setCurrentActivity(this);

        int span = calculateNoOfColumns(this, 200);
        binding.playlistRecyclerView.setLayoutManager(new GridLayoutManager(this, span));

        binding.popularSongsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.popularArtistsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.popularAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        OverScrollDecoratorHelper.setUpOverScroll(binding.popularSongsRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        OverScrollDecoratorHelper.setUpOverScroll(binding.popularArtistsRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        OverScrollDecoratorHelper.setUpOverScroll(binding.popularAlbumsRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);


//        startActivity(new Intent(this, ArtistProfileActivity.class));
//        finish();

        binding.refreshLayout.setOnRefreshListener(() -> {
            showShimmerData();
            showData();
            binding.refreshLayout.setRefreshing(false);
        });

        binding.playBarPlayPauseIcon.setOnClickListener(view -> {
            if(ApplicationClass.mediaPlayerUtil.isPlaying())
                ApplicationClass.mediaPlayerUtil.pause();
            else
                ApplicationClass.mediaPlayerUtil.start();

            ApplicationClass applicationClass = (ApplicationClass) getApplicationContext();
            applicationClass.showNotification(ApplicationClass.mediaPlayerUtil.isPlaying() ? R.drawable.baseline_pause_24 : R.drawable.play_arrow_24px);
            binding.playBarPlayPauseIcon.setImageResource(ApplicationClass.mediaPlayerUtil.isPlaying() ? R.drawable.baseline_pause_24 : R.drawable.play_arrow_24px);
        });

        showShimmerData();
        showOfflineData();

        //showData();
        showPlayBarData();

    }

    Handler handler = new Handler();
    Runnable runnable = this::showPlayBarData;

    void showPlayBarData(){
        binding.playBarMusicTitle.setText(ApplicationClass.MUSIC_TITLE);
        binding.playBarMusicDesc.setText(ApplicationClass.MUSIC_DESCRIPTION);
        Picasso.get().load(Uri.parse(ApplicationClass.IMAGE_URL)).into(binding.playBarCoverImage);
        if(ApplicationClass.mediaPlayerUtil.isPlaying()){
            binding.playBarPlayPauseIcon.setImageResource(R.drawable.baseline_pause_24);
        }else{
            binding.playBarPlayPauseIcon.setImageResource(R.drawable.play_arrow_24px);
        }

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(ApplicationClass.IMAGE_BG_COLOR);
        gradientDrawable.setCornerRadius(18);

        binding.playBarBackground.setBackground(gradientDrawable);

        binding.playBarMusicTitle.setTextColor(ApplicationClass.TEXT_ON_IMAGE_COLOR);
        binding.playBarMusicDesc.setTextColor(ApplicationClass.TEXT_ON_IMAGE_COLOR);

        binding.playBarPlayPauseIcon.setImageTintList(ColorStateList.valueOf(ApplicationClass.TEXT_ON_IMAGE_COLOR));

        OverScrollDecoratorHelper.setUpStaticOverScroll(binding.getRoot(), OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        handler.postDelayed(runnable, 1000);
    }


    @Override
    protected void onResume() {
        super.onResume();
        NetworkChangeReceiver.registerReceiver(this, networkChangeReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetworkChangeReceiver.unregisterReceiver(this, networkChangeReceiver);
    }

    private void showData() {

        songs.clear();
        artists.clear();
        albums.clear();
        playlists.clear();

        final ApiManager apiManager = new ApiManager(this);

        apiManager.searchSongs(" ", 0, 10, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                SongSearch songSearch = new Gson().fromJson(response, SongSearch.class);
                Log.i(TAG, "onResponse: " + response);
                if (songSearch.success()) {
                    songSearch.data().results().forEach(results -> {
                        songs.add(new AlbumItem(results.name(), results.language() + " " + results.year(), results.image().get(results.image().size() - 1).url(), results.id()));
                        ActivityMainPopularSongs adapter = new ActivityMainPopularSongs(songs);
                        binding.popularSongsRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    });
                    applicationClass.sharedPreferenceManager.setHomeSongsRecommended(songSearch);
                } else {
                    try {
                        showOfflineData();
                        Toast.makeText(MainActivity.this, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                showOfflineData();
            }
        });

        apiManager.searchArtists(" ", 0, 15, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                ArtistsSearch artistSearch = new Gson().fromJson(response, ArtistsSearch.class);
                Log.i(TAG, "onResponse: " + response);
                if (artistSearch.success()) {
                    artistSearch.data().results().forEach(results -> {
                        artists.add(results);
                        ActivityMainArtistsItemAdapter adapter = new ActivityMainArtistsItemAdapter(artists);
                        binding.popularArtistsRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    });
                    applicationClass.sharedPreferenceManager.setHomeArtistsRecommended(artistSearch);
                } else {
                    try {
                        showOfflineData();
                        Toast.makeText(MainActivity.this, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                showOfflineData();
            }
        });

        apiManager.searchAlbums(" ", 0, 15, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                AlbumsSearch albumsSearch = new Gson().fromJson(response, AlbumsSearch.class);
                Log.i(TAG, "onResponse: " + response);
                if (albumsSearch.success()) {
                    albumsSearch.data().results().forEach(results -> {
                        albums.add(new AlbumItem(results.name(), results.language() + " " + results.year(), results.image().get(results.image().size() - 1).url(), results.id()));
                        ActivityMainAlbumItemAdapter adapter = new ActivityMainAlbumItemAdapter(albums);
                        binding.popularAlbumsRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    });
                    applicationClass.sharedPreferenceManager.setHomeAlbumsRecommended(albumsSearch);
                } else {
                    try {
                        Toast.makeText(MainActivity.this, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                        showOfflineData();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                showOfflineData();
            }
        });

        apiManager.searchPlaylists(String.valueOf(Calendar.getInstance().get(Calendar.YEAR)), 0, 15, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                PlaylistsSearch playlistsSearch = new Gson().fromJson(response, PlaylistsSearch.class);
                Log.i(TAG, "onResponse: " + response);
                if (playlistsSearch.success()) {
                    playlistsSearch.data().results().forEach(results -> {
                        playlists.add(new AlbumItem(results.name(), "", results.image().get(results.image().size() - 1).url(), results.id()));
                        //binding.playlistRecyclerView.setAdapter(new ActivityMainPlaylistAdapter(playlists));

                        ActivityMainPlaylistAdapter adapter = new ActivityMainPlaylistAdapter(playlists);
                        binding.playlistRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    });
                    applicationClass.sharedPreferenceManager.setHomePlaylistRecommended(playlistsSearch);
                } else {
                    try {
                        Toast.makeText(MainActivity.this, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                        showOfflineData();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                showOfflineData();
            }
        });
    }

    private void showShimmerData() {
        final List<AlbumItem> data_shimmer = new ArrayList<>();
        final List<ArtistsSearch.Data.Results> artists_shimmer = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            data_shimmer.add(new AlbumItem("<shimmer>", "<shimmer>", "<shimmer>", "<shimmer>"));
            artists_shimmer.add(new ArtistsSearch.Data.Results(
                    "<shimmer>",
                    "<shimmer>",
                    "<shimmer>",
                    "<shimmer>",
                    "<shimmer>",
                    null
            ));
        }
        binding.popularSongsRecyclerView.setAdapter(new ActivityMainAlbumItemAdapter(data_shimmer));
        binding.popularAlbumsRecyclerView.setAdapter(new ActivityMainAlbumItemAdapter(data_shimmer));
        binding.popularArtistsRecyclerView.setAdapter(new ActivityMainArtistsItemAdapter(artists_shimmer));
        binding.playlistRecyclerView.setAdapter(new ActivityMainPlaylistAdapter(data_shimmer));

    }

    void tryConnect() {
        if (!NetworkUtil.isNetworkAvailable(MainActivity.this)) {
            try {
                Thread.sleep(2000);
                //showData();
            } catch (Exception e) {
                Log.e(TAG, "onErrorResponse: ", e);
            }
        }
    }

    private void showOfflineData(){
        if(applicationClass.sharedPreferenceManager.getHomeSongsRecommended()!=null){
            SongSearch songSearch = applicationClass.sharedPreferenceManager.getHomeSongsRecommended();
            songSearch.data().results().forEach(results -> {
                songs.add(new AlbumItem(results.name(), results.language() + " " + results.year(), results.image().get(results.image().size() - 1).url(), results.id()));
                ActivityMainPopularSongs adapter = new ActivityMainPopularSongs(songs);
                binding.popularSongsRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
        }

        if(applicationClass.sharedPreferenceManager.getHomeArtistsRecommended()!=null){
            ArtistsSearch artistsSearch = applicationClass.sharedPreferenceManager.getHomeArtistsRecommended();
            artistsSearch.data().results().forEach(results -> {
                artists.add(results);
                ActivityMainArtistsItemAdapter adapter = new ActivityMainArtistsItemAdapter(artists);
                binding.popularArtistsRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
        }

        if(applicationClass.sharedPreferenceManager.getHomeAlbumsRecommended()!=null){
            AlbumsSearch albumsSearch = applicationClass.sharedPreferenceManager.getHomeAlbumsRecommended();
            albumsSearch.data().results().forEach(results -> {
                albums.add(new AlbumItem(results.name(), results.language() + " " + results.year(), results.image().get(results.image().size() - 1).url(), results.id()));
                ActivityMainAlbumItemAdapter adapter = new ActivityMainAlbumItemAdapter(albums);
                binding.popularAlbumsRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
        }

        if(applicationClass.sharedPreferenceManager.getHomePlaylistRecommended()!=null){
            PlaylistsSearch playlistsSearch = applicationClass.sharedPreferenceManager.getHomePlaylistRecommended();
            playlistsSearch.data().results().forEach(results -> {
                playlists.add(new AlbumItem(results.name(), "", results.image().get(results.image().size() - 1).url(), results.id()));
                //binding.playlistRecyclerView.setAdapter(new ActivityMainPlaylistAdapter(playlists));

                ActivityMainPlaylistAdapter adapter = new ActivityMainPlaylistAdapter(playlists);
                binding.playlistRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
        }

    }

    public void openSearch(View view) {
        startActivity(new Intent(this, SearchActivity.class));
    }
}