package com.harsh.shah.saavnmp3.activities;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.harsh.shah.saavnmp3.R;
import com.harsh.shah.saavnmp3.databinding.ActivityMusicOverviewBinding;

public class MusicOverviewActivity extends AppCompatActivity {

    ActivityMusicOverviewBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
    }

    public void backPress(View view) {
        finish();
    }
}