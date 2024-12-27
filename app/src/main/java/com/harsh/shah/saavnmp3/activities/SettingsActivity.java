package com.harsh.shah.saavnmp3.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.harsh.shah.saavnmp3.databinding.ActivitySettingsBinding;

public class SettingsActivity extends AppCompatActivity {

    ActivitySettingsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }

    public void backPress(View view) {
        finish();
    }
}