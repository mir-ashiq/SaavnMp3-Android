package com.harsh.shah.saavnmp3.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.harsh.shah.saavnmp3.records.AlbumsSearch;
import com.harsh.shah.saavnmp3.records.ArtistsSearch;
import com.harsh.shah.saavnmp3.records.PlaylistsSearch;
import com.harsh.shah.saavnmp3.records.SongResponse;
import com.harsh.shah.saavnmp3.records.SongSearch;

public class SharedPreferenceManager {

    public SharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

    private final SharedPreferences sharedPreferences;

    private static SharedPreferenceManager instance;

    public static SharedPreferenceManager getInstance(Context context) {
        return instance == null ? new SharedPreferenceManager(context) : instance;
    }

    private SharedPreferenceManager(Context context) {
        sharedPreferences = context.getSharedPreferences("cache", Context.MODE_PRIVATE);
    }

    public void setHomeSongsRecommended(SongSearch songSearch) {
        sharedPreferences.edit().putString("home_songs_recommended", new Gson().toJson(songSearch)).apply();
    }

    public SongSearch getHomeSongsRecommended() {
        return new Gson().fromJson(sharedPreferences.getString("home_songs_recommended", ""), SongSearch.class);
    }

    public void setHomeArtistsRecommended(ArtistsSearch artistsRecommended){
        sharedPreferences.edit().putString("home_artists_recommended", new Gson().toJson(artistsRecommended)).apply();
    }

    public ArtistsSearch getHomeArtistsRecommended(){
        return new Gson().fromJson(sharedPreferences.getString("home_artists_recommended", ""), ArtistsSearch.class);
    }

    public void setHomeAlbumsRecommended(AlbumsSearch albumsSearch){
        sharedPreferences.edit().putString("home_albums_recommended", new Gson().toJson(albumsSearch)).apply();
    }

    public AlbumsSearch getHomeAlbumsRecommended(){
        return new Gson().fromJson(sharedPreferences.getString("home_albums_recommended", ""), AlbumsSearch.class);
    }

    public void setHomePlaylistRecommended(PlaylistsSearch playlistsSearch){
        sharedPreferences.edit().putString("home_playlists_recommended", new Gson().toJson(playlistsSearch)).apply();
    }
    public PlaylistsSearch getHomePlaylistRecommended(){
        return new Gson().fromJson(sharedPreferences.getString("home_playlists_recommended", ""), PlaylistsSearch.class);
    }

    public void setSongResponseById(String id, SongResponse songSearch){
        sharedPreferences.edit().putString(id, new Gson().toJson(songSearch)).apply();
    }
    public SongResponse getSongResponseById(String id){
        return new Gson().fromJson(sharedPreferences.getString(id, ""), SongResponse.class);
    }

    public boolean isSongResponseById(String id){
        return sharedPreferences.contains(id);
    }

}
