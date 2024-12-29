package com.harsh.shah.saavnmp3.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.harsh.shah.saavnmp3.adapters.SavedLibrariesAdapter;
import com.harsh.shah.saavnmp3.databinding.ActivitySavedLibrariesBinding;
import com.harsh.shah.saavnmp3.records.sharedpref.SavedLibraries;
import com.harsh.shah.saavnmp3.utils.SharedPreferenceManager;

public class SavedLibrariesActivity extends AppCompatActivity {

    ActivitySavedLibrariesBinding binding;
    SavedLibraries savedLibraries;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySavedLibrariesBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        showData();
    }

    private void showData() {
        savedLibraries = SharedPreferenceManager.getInstance(this).getSavedLibrariesData();
        binding.emptyListTv.setVisibility(savedLibraries == null ? View.VISIBLE : View.GONE);
        if(savedLibraries != null) binding.recyclerView.setAdapter(new SavedLibrariesAdapter(savedLibraries.lists()));
    }

    public void backPress(View view){
        finish();
    }
}