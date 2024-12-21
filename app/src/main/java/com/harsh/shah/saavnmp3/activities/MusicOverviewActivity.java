package com.harsh.shah.saavnmp3.activities;

import android.annotation.SuppressLint;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.harsh.shah.saavnmp3.ApplicationClass;
import com.harsh.shah.saavnmp3.R;
import com.harsh.shah.saavnmp3.databinding.ActivityMusicOverviewBinding;
import com.harsh.shah.saavnmp3.network.ApiManager;
import com.harsh.shah.saavnmp3.network.utility.RequestNetwork;
import com.harsh.shah.saavnmp3.records.SongResponse;
import com.harsh.shah.saavnmp3.services.ActionPlaying;
import com.harsh.shah.saavnmp3.services.MusicService;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class MusicOverviewActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {

    private final String TAG = "MusicOverviewActivity";
    //private final MediaPlayer mediaPlayer = new MediaPlayer();
    private final Handler handler = new Handler();
    ActivityMusicOverviewBinding binding;
    private String SONG_URL = "";
    private String IMAGE_URL = "";
    MusicService musicService;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.title.setSelected(true);
        binding.description.setSelected(true);

        binding.playPauseImage.setOnClickListener(view -> {
            if (ApplicationClass.player.isPlaying()) {
                handler.removeCallbacks(runnable);
                ApplicationClass.player.pause();
                binding.playPauseImage.setImageResource(com.harsh.shah.saavnmp3.R.drawable.play_arrow_24px);
            } else {
                ApplicationClass.player.play();
                binding.playPauseImage.setImageResource(R.drawable.baseline_pause_24);
                updateSeekbar();
            }
            showNotification(ApplicationClass.player.isPlaying() ? R.drawable.baseline_pause_24 : R.drawable.play_arrow_24px);
        });

        binding.seekbar.setMax(100);

        //ApplicationClass.player.setOnBufferingUpdateListener((mediaPlayer, i) -> binding.seekbar.setSecondaryProgress(i));


        binding.seekbar.setOnTouchListener((v, event) -> {
            int playPosition = (int) ((ApplicationClass.player.getDuration() / 100) * binding.seekbar.getProgress());
            ApplicationClass.player.seekTo(playPosition);
            binding.elapsedDuration.setText(convertDuration(ApplicationClass.player.getCurrentPosition()));
            return false;
        });

//        ApplicationClass.player.setOnCompletionListener(mediaPlayer -> {
//            binding.seekbar.setProgress(0);
//            binding.elapsedDuration.setText("00:00");
//            binding.playPauseImage.setImageResource(R.drawable.play_arrow_24px);
//            handler.removeCallbacks(runnable);
//            mediaPlayer.seekTo(0);
//            mediaPlayer.reset();
//            ((ApplicationClass)getApplication()).nextTrack();
//        });
        //((ApplicationClass)getApplication()).setMusicDetails("","","","");

        final ApplicationClass applicationClass = (ApplicationClass) getApplicationContext();

        binding.nextIcon.setOnClickListener(view -> applicationClass.nextTrack());
        binding.prevIcon.setOnClickListener(view -> applicationClass.prevTrack());

        showData();

        updateTrackInfo();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder binder = (MusicService.MyBinder) service;
        musicService = binder.getService();
        musicService.setCallback(MusicOverviewActivity.this);
        Log.e(TAG, "onServiceConnected: ");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.e(TAG, "onServiceDisconnected: ");
        musicService = null;
    }

    void showData() {
        if (getIntent().getExtras() == null) return;
        final ApiManager apiManager = new ApiManager(this);
        final String ID = getIntent().getExtras().getString("id", "");
        //((ApplicationClass)getApplicationContext()).setMusicDetails(null,null,null,ID);
        if (ApplicationClass.MUSIC_ID.equals(ID)) {
            updateSeekbar();
            if (ApplicationClass.player.isPlaying())
                binding.playPauseImage.setImageResource(R.drawable.baseline_pause_24);
            else
                binding.playPauseImage.setImageResource(R.drawable.play_arrow_24px);
        }

        if(getIntent().getExtras().getString("type", "").equals("clear")){
            ApplicationClass applicationClass = (ApplicationClass) getApplicationContext();
            applicationClass.setTrackQueue(new ArrayList<>(Collections.singletonList(ID)));
        }

        apiManager.retrieveSongById(ID, null, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                SongResponse songResponse = new Gson().fromJson(response, SongResponse.class);
                if (songResponse.success()) {
                    binding.title.setText(songResponse.data().get(0).name());
                    binding.description.setText(
                            String.format("%s plays | %s | %s",
                                    convertPlayCount(songResponse.data().get(0).playCount()),
                                    songResponse.data().get(0).year(),
                                    songResponse.data().get(0).copyright())
                    );
                    List<SongResponse.Image> image = songResponse.data().get(0).image();
                    IMAGE_URL = image.get(image.size() - 1).url();
                    Picasso.get().load(Uri.parse(image.get(image.size() - 1).url())).into(binding.coverImage);
                    List<SongResponse.DownloadUrl> downloadUrls = songResponse.data().get(0).downloadUrl();

                    //Log.i(TAG, "onResponse: " + downloadUrls.get(downloadUrls.size() - 1).url());
                    SONG_URL = downloadUrls.get(downloadUrls.size() - 1).url();
//                    if (ApplicationClass.MUSIC_ID.equals(ID)) {
//                        updateSeekbar();
//                        if (ApplicationClass.player.isPlaying())
//                            binding.playPauseImage.setImageResource(R.drawable.baseline_pause_24);
//                        else
//                            binding.playPauseImage.setImageResource(R.drawable.play_arrow_24px);
//                    } else
//                        prepareMediaPLayer();

                    if(!ApplicationClass.MUSIC_ID.equals(ID)){
                        ApplicationClass applicationClass = (ApplicationClass) getApplicationContext();
                        applicationClass.setMusicDetails(IMAGE_URL, binding.title.getText().toString(), binding.description.getText().toString(), ID);
                        applicationClass.setSongUrl(SONG_URL);
                        prepareMediaPLayer();
                    }

                    if(!ApplicationClass.player.isPlaying()){
                        playClicked();
                        binding.playPauseImage.performClick();
                    }

                    //binding.main.setBackgroundColor(ApplicationClass.IMAGE_BG_COLOR);

                } else
                    finish();
            }

            @Override
            public void onErrorResponse(String tag, String message) {

            }
        });
    }

    public void backPress(View view) {
        finish();
    }

    public static String convertPlayCount(int playCount) {
        if (playCount < 1000) return playCount + "";
        if (playCount < 1000000) return playCount / 1000 + "K";
        return playCount / 1000000 + "M";
    }

    public static String convertDuration(long duration) {
        String timeString = "";
        String secondString;

        int hours = (int) (duration / (1000 * 60 * 60));
        int minutes = (int) (duration % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((duration % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            timeString = hours + ":";
        }
        if (seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }
        timeString = timeString + minutes + ":" + secondString;
        return timeString;
    }

    void prepareMediaPLayer() {
        ((ApplicationClass)getApplicationContext()).prepareMediaPlayer();
        binding.totalDuration.setText(convertDuration(ApplicationClass.player.getDuration()));
        playClicked();
        binding.playPauseImage.performClick();
    }

    private final Runnable runnable = this::updateSeekbar;

    void updateSeekbar() {
        if (ApplicationClass.player.isPlaying()) {
            binding.seekbar.setProgress((int) (((float) ApplicationClass.player.getCurrentPosition() / ApplicationClass.player.getDuration()) * 100));
            long currentDuration = ApplicationClass.player.getCurrentPosition();
            binding.elapsedDuration.setText(convertDuration(currentDuration));
            handler.postDelayed(runnable, 1000);
        }
    }

    private final Handler mHandler = new Handler();
    private final Runnable mUpdateTimeTask = this::updateTrackInfo;

    private void updateTrackInfo() {
        if(!binding.title.getText().toString().equals(ApplicationClass.MUSIC_TITLE)) binding.title.setText(ApplicationClass.MUSIC_TITLE);
        if(!binding.title.getText().toString().equals(ApplicationClass.MUSIC_TITLE)) binding.description.setText(ApplicationClass.MUSIC_DESCRIPTION);
        Picasso.get().load(Uri.parse(ApplicationClass.IMAGE_URL)).into(binding.coverImage);
        binding.seekbar.setProgress((int) (((float) ApplicationClass.player.getCurrentPosition() / ApplicationClass.player.getDuration()) * 100));
        long currentDuration = ApplicationClass.player.getCurrentPosition();
        binding.elapsedDuration.setText(convertDuration(currentDuration));

        if(!binding.totalDuration.getText().toString().equals(convertDuration(ApplicationClass.player.getDuration())))
            binding.totalDuration.setText(convertDuration(ApplicationClass.player.getDuration()));

        if(ApplicationClass.player.isPlaying())
            binding.playPauseImage.setImageResource(R.drawable.baseline_pause_24);
        else
            binding.playPauseImage.setImageResource(R.drawable.play_arrow_24px);

        ((ApplicationClass)getApplicationContext()).showNotification();

        mHandler.postDelayed(mUpdateTimeTask, 1000);
    }


    @Override
    public void nextClicked() {
    }

    @Override
    public void prevClicked() {
    }

    @Override
    public void playClicked() {
        //binding.playPauseImage.performClick();
        if (!ApplicationClass.player.isPlaying()) {
            binding.playPauseImage.setImageResource(R.drawable.play_arrow_24px);
        } else {
            binding.playPauseImage.setImageResource(R.drawable.baseline_pause_24);
        }
    }

    @Override
    public void onProgressChanged(int progress) {

    }

    public void showNotification(int playPauseButton) {
        ApplicationClass applicationClass = (ApplicationClass) getApplicationContext();
        applicationClass.setMusicDetails(IMAGE_URL, binding.title.getText().toString(), binding.description.getText().toString(), getIntent().getExtras().getString("id", ""));
        applicationClass.showNotification();
    }


}