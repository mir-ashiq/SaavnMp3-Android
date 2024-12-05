package com.harsh.shah.saavnmp3.utils;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;
import com.harsh.shah.saavnmp3.modals.apimodel.GlobalSearchModel;
import com.harsh.shah.saavnmp3.network.RequestNetwork;
import com.harsh.shah.saavnmp3.network.RequestNetworkController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class testFetchSongs {

    private final String TAG = "testFetchSongs";
    private final Context mContext;

    public testFetchSongs(Context context){
        mContext = context;
    }

    public void searchSongs(String text){
        RequestNetwork requestNetwork = new RequestNetwork((Activity) mContext);
        HashMap<String, Object> data = new HashMap<>();
        data.put("query",text);
        requestNetwork.setParams(data, RequestNetworkController.REQUEST_PARAM);
        requestNetwork.startRequestNetwork(RequestNetworkController.GET, "https://saavn.dev/api/search", "", new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                GlobalSearchModel globalSearchModel = new Gson().fromJson(response, GlobalSearchModel.class);
                Log.i(TAG, "onResponse: " + response);
                try {
                    JSONObject data = new JSONObject(response);
                    Log.i(TAG, "onResponse: " + data.getJSONObject("data").getJSONObject("songs").getJSONArray("results").getJSONObject(0).get("id"));
                } catch (JSONException e) {
                    Log.e(TAG, "onResponse: ", e);
                }
                if(globalSearchModel.isSuccess())
                    Log.i(TAG, "onResponse: " + globalSearchModel.getData().getSongs().getResults().get(0).getSongIds());
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                Log.i(TAG, "onErrorResponse: " + message);
            }
        });
    }
}
