package com.harsh.shah.saavnmp3.activities;

import static com.harsh.shah.saavnmp3.ApplicationClass.mediaPlayerUtil;

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
            if (mediaPlayerUtil.isPlaying()) {
                handler.removeCallbacks(runnable);
                mediaPlayerUtil.pause();
                binding.playPauseImage.setImageResource(com.harsh.shah.saavnmp3.R.drawable.play_arrow_24px);
            } else {
                mediaPlayerUtil.start();
                binding.playPauseImage.setImageResource(R.drawable.baseline_pause_24);
                updateSeekbar();
            }
            showNotification(mediaPlayerUtil.isPlaying() ? R.drawable.baseline_pause_24 : R.drawable.play_arrow_24px);
        });

        binding.seekbar.setMax(100);

        mediaPlayerUtil.setOnBufferingUpdateListener((mediaPlayer, i) -> binding.seekbar.setSecondaryProgress(i));

        binding.seekbar.setOnTouchListener((v, event) -> {
            int playPosition = (mediaPlayerUtil.getDuration() / 100) * binding.seekbar.getProgress();
            mediaPlayerUtil.seekTo(playPosition);
            binding.elapsedDuration.setText(convertDuration(mediaPlayerUtil.getCurrentPosition()));
            return false;
        });

        mediaPlayerUtil.setOnCompletionListener(mediaPlayer -> {
            binding.seekbar.setProgress(0);
            binding.elapsedDuration.setText("00:00");
            binding.playPauseImage.setImageResource(R.drawable.play_arrow_24px);
            handler.removeCallbacks(runnable);
            mediaPlayer.reset();
        });
        //((ApplicationClass)getApplication()).setMusicDetails("","","","");

        final ApplicationClass applicationClass = (ApplicationClass) getApplicationContext();

        binding.nextIcon.setOnClickListener(view -> applicationClass.nextTrack());
        binding.prevIcon.setOnClickListener(view -> applicationClass.prevTrack());

        showData();
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
            if (mediaPlayerUtil.isPlaying())
                binding.playPauseImage.setImageResource(R.drawable.baseline_pause_24);
            else
                binding.playPauseImage.setImageResource(R.drawable.play_arrow_24px);
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
//                        if (mediaPlayerUtil.isPlaying())
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

                    playClicked();
                    binding.playPauseImage.performClick();

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
        binding.totalDuration.setText(convertDuration(mediaPlayerUtil.getDuration()));
        playClicked();
        binding.playPauseImage.performClick();
    }

    private final Runnable runnable = this::updateSeekbar;

    void updateSeekbar() {
        if (mediaPlayerUtil.isPlaying()) {
            binding.seekbar.setProgress((int) (((float) mediaPlayerUtil.getCurrentPosition() / mediaPlayerUtil.getDuration()) * 100));
            long currentDuration = mediaPlayerUtil.getCurrentPosition();
            binding.elapsedDuration.setText(convertDuration(currentDuration));
            handler.postDelayed(runnable, 1000);
        }
    }


    @Override
    public void nextClicked() {
        if (mediaPlayerUtil.isPlaying())
            showNotification(R.drawable.baseline_pause_24);
        else
            showNotification(R.drawable.play_arrow_24px);
    }

    @Override
    public void prevClicked() {
        if (mediaPlayerUtil.isPlaying())
            showNotification(R.drawable.baseline_pause_24);
        else
            showNotification(R.drawable.play_arrow_24px);
    }

    @Override
    public void playClicked() {
        //binding.playPauseImage.performClick();
        if (!mediaPlayerUtil.isPlaying()) {
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
        applicationClass.showNotification(playPauseButton);
    }


}