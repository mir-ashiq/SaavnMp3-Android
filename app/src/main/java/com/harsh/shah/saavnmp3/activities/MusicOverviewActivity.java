package com.harsh.shah.saavnmp3.activities;

import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.harsh.shah.saavnmp3.R;
import com.harsh.shah.saavnmp3.databinding.ActivityMusicOverviewBinding;
import com.harsh.shah.saavnmp3.network.ApiManager;
import com.harsh.shah.saavnmp3.network.utility.RequestNetwork;
import com.harsh.shah.saavnmp3.records.SongResponse;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class MusicOverviewActivity extends AppCompatActivity {

    private final String TAG = "MusicOverviewActivity";
    private final MediaPlayer mediaPlayer = new MediaPlayer();
    private final Handler handler = new Handler();
    ActivityMusicOverviewBinding binding;
    private String SONG_URL = "";

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.title.setSelected(true);
        binding.description.setSelected(true);

        binding.playPauseImage.setOnClickListener(view -> {
            if (mediaPlayer.isPlaying()) {
                handler.removeCallbacks(runnable);
                mediaPlayer.pause();
                binding.playPauseImage.setImageResource(com.harsh.shah.saavnmp3.R.drawable.play_arrow_24px);
            } else {
                mediaPlayer.start();
                binding.playPauseImage.setImageResource(R.drawable.baseline_pause_24);
                updateSeekbar();
            }
        });

        binding.seekbar.setMax(100);

        mediaPlayer.setOnBufferingUpdateListener((mediaPlayer, i) -> {
            binding.seekbar.setSecondaryProgress(i);
        });

        binding.seekbar.setOnTouchListener((v, event) -> {
            int playPosition = (mediaPlayer.getDuration() / 100) * binding.seekbar.getProgress();
            mediaPlayer.seekTo(playPosition);
            binding.elapsedDuration.setText(convertDuration(mediaPlayer.getCurrentPosition()));
            return false;
        });

        mediaPlayer.setOnCompletionListener(mediaPlayer -> {
            binding.seekbar.setProgress(0);
            binding.elapsedDuration.setText("00:00");
            binding.playPauseImage.setImageResource(R.drawable.play_arrow_24px);
            handler.removeCallbacks(runnable);
            mediaPlayer.reset();
        });

        showData();
    }

    void showData() {
        if (getIntent().getExtras() == null) return;
        final ApiManager apiManager = new ApiManager(this);
        apiManager.retrieveSongById(getIntent().getExtras().getString("id", "3IoDK8qI"), null, new RequestNetwork.RequestListener() {
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
                    Picasso.get().load(Uri.parse(image.get(image.size() - 1).url())).into(binding.coverImage);
                    List<SongResponse.DownloadUrl> downloadUrls = songResponse.data().get(0).downloadUrl();

                    Log.i(TAG, "onResponse: " + downloadUrls.get(downloadUrls.size() - 1).url());
                    SONG_URL = downloadUrls.get(downloadUrls.size() - 1).url();
                    prepareMediaPLayer();

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

    String convertPlayCount(int playCount) {
        if (playCount < 1000) return playCount + "";
        if (playCount < 1000000) return playCount / 1000 + "K";
        return playCount / 1000000 + "M";
    }

    String convertDuration(long duration) {
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
        try {
            mediaPlayer.setDataSource(SONG_URL);
            mediaPlayer.prepare();
            binding.totalDuration.setText(convertDuration(mediaPlayer.getDuration()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private final Runnable runnable = this::updateSeekbar;

    void updateSeekbar() {
        if (mediaPlayer.isPlaying()) {
            binding.seekbar.setProgress((int) (((float) mediaPlayer.getCurrentPosition() / mediaPlayer.getDuration()) * 100));
            long currentDuration = mediaPlayer.getCurrentPosition();
            binding.elapsedDuration.setText(convertDuration(currentDuration));
            handler.postDelayed(runnable, 1000);
        }
    }


}