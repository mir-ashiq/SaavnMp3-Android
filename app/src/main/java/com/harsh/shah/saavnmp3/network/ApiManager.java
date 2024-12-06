package com.harsh.shah.saavnmp3.network;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.harsh.shah.saavnmp3.network.utility.RequestNetwork;
import com.harsh.shah.saavnmp3.network.utility.RequestNetworkController;

import java.util.HashMap;

public class ApiManager {
    private static final String BASE_URL = "https://saavn.dev/api/";
    private static final String SEARCH_URL = BASE_URL + "search";
    private static final String SONGS_URL = BASE_URL + "songs";
    private static final String ALBUMS_URL = BASE_URL + "albums";
    private static final String ARTISTS_URL = BASE_URL + "artists";
    private static final String PLAYLISTS_URL = BASE_URL + "playlists";
    private final Context mContext;
    private RequestNetwork requestNetwork;

    public ApiManager(Context context){
        this.mContext = context;
        requestNetwork = new RequestNetwork((Activity) mContext);
    }

    public void globalSearch(String text, RequestNetwork.RequestListener listener){
        HashMap<String,Object> queryMap = new HashMap<>();
        queryMap.put("query",text);
        requestNetwork.setParams(queryMap, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.GET, SEARCH_URL, "", listener);
    }

    public void searchSongs(@NonNull String query, Integer page, Integer limit, @NonNull RequestNetwork.RequestListener listener){
        HashMap<String,Object> queryMap = new HashMap<>();
        queryMap.put("query",query);
        if(page != null) queryMap.put("page", page);
        if(limit != null)queryMap.put("limit",limit);
        requestNetwork.setParams(queryMap, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.GET, SONGS_URL, "", listener);
    }

    public void searchAlbums(@NonNull String query, Integer page, Integer limit, @NonNull RequestNetwork.RequestListener listener){
        HashMap<String,Object> queryMap = new HashMap<>();
        queryMap.put("query",query);
        if(page != null) queryMap.put("page", page);
        if(limit != null)queryMap.put("limit",limit);
        requestNetwork.setParams(queryMap, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.GET, ALBUMS_URL, "", listener);
    }

    public void searchArtists(@NonNull String query, Integer page, Integer limit, @NonNull RequestNetwork.RequestListener listener){
        HashMap<String,Object> queryMap = new HashMap<>();
        queryMap.put("query",query);
        if(page != null) queryMap.put("page", page);
        if(limit != null)queryMap.put("limit",limit);
        requestNetwork.setParams(queryMap, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.GET, ARTISTS_URL, "", listener);
    }

    public void searchPlaylists(@NonNull String query, Integer page, Integer limit, @NonNull RequestNetwork.RequestListener listener){
        HashMap<String,Object> queryMap = new HashMap<>();
        queryMap.put("query",query);
        if(page != null) queryMap.put("page", page);
        if(limit != null)queryMap.put("limit",limit);
        requestNetwork.setParams(queryMap, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.GET, PLAYLISTS_URL, "", listener);
    }



}
