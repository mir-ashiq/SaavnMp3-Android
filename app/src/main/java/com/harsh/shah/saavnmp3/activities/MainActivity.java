package com.harsh.shah.saavnmp3.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.harsh.shah.saavnmp3.adapters.ActivityMainAlbumItemAdapter;
import com.harsh.shah.saavnmp3.adapters.ActivityMainArtistsItemAdapter;
import com.harsh.shah.saavnmp3.adapters.ActivityMainPlaylistAdapter;
import com.harsh.shah.saavnmp3.databinding.ActivityMainBinding;
import com.harsh.shah.saavnmp3.modals.AlbumItem;
import com.harsh.shah.saavnmp3.utils.testFetchSongs;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        int span = calculateNoOfColumns(this, 200);
        binding.playlistRecyclerView.setLayoutManager(new GridLayoutManager(this,span));
        binding.playlistRecyclerView.setAdapter(new ActivityMainPlaylistAdapter());

        binding.popularSongsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.popularArtistsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.popularAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        List<AlbumItem> data = new ArrayList<>();
        data.add(new AlbumItem("Album 1", "Sub Title 1", ""));
        data.add(new AlbumItem("Album 2", "Sub Title 2", ""));
        data.add(new AlbumItem("Album 3", "Sub Title 3", ""));
        data.add(new AlbumItem("Album 4", "Sub Title 4", ""));
        data.add(new AlbumItem("Album 5", "Sub Title 5", ""));
        data.add(new AlbumItem("Album 6", "Sub Title 6", ""));
        data.add(new AlbumItem("Album 7", "Sub Title 7", ""));
        data.add(new AlbumItem("Album 8", "Sub Title 8", ""));
        data.add(new AlbumItem("Album 9", "Sub Title 9", ""));
        data.add(new AlbumItem("Album 10", "Sub Title 10", ""));
        binding.popularSongsRecyclerView.setAdapter(new ActivityMainAlbumItemAdapter(data));
        binding.popularAlbumsRecyclerView.setAdapter(new ActivityMainAlbumItemAdapter(data));

        List<String> str_data = new ArrayList<>();
        str_data.add("Artist 1");
        str_data.add("Artist 2");
        str_data.add("Artist 3");
        str_data.add("Artist 4");
        str_data.add("Artist 5");
        str_data.add("Artist 6");
        str_data.add("Artist 7");
        str_data.add("Artist 8");
        str_data.add("Artist 9");
        str_data.add("Artist 10");
        binding.popularArtistsRecyclerView.setAdapter(new ActivityMainArtistsItemAdapter(str_data));

        new testFetchSongs(this).searchSongs("Hindi Songs");

    }

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthDp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        return  (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
    }

}