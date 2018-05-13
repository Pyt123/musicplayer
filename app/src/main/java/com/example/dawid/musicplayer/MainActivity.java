package com.example.dawid.musicplayer;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LifecycleOwner
{
    //private String storageDirectory;
    private RecyclerView trackList;
    private ImageButton playButton;
    private TextView currentTitleText;
    private LifecycleRegistry lifecycleRegistry;
    private MusicViewModel musicViewModel;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        lifecycleRegistry = new LifecycleRegistry(this);
        PlayerObserver playerObserver = new PlayerObserver(this);
        musicViewModel = ViewModelProviders.of(this).get(MusicViewModel.class);
        setupUi();

        //storageDirectory = Environment.getExternalStorageDirectory().getPath() + "/music/";﻿

        lifecycleRegistry.markState(Lifecycle.State.CREATED);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();

        switch (id)
        {
            case R.id.action_about:
                startActivity(new Intent(this, AboutActivity.class));
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupUi()
    {
        setContentView(R.layout.activity_main);
        setupToolbar();
        setupMusicRecyclerView();
        setupCurrentTrackTitleView();
        setupPlayPauseButton();
    }

    private void setupToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupCurrentTrackTitleView()
    {
        currentTitleText = findViewById(R.id.current_title_text);
        currentTitleText.setText("");
        musicViewModel.getTrackData().getLiveDataCurrentTrack().observe(this, new Observer<Track>()
        {
            @Override
            public void onChanged(@Nullable Track track)
            {
                if(track != null)
                {
                    currentTitleText.setText(track.getTrackTitle());
                }
                else
                {
                    currentTitleText.setText("");
                }
            }
        });
    }

    private void setupMusicRecyclerView()
    {
        trackList = findViewById(R.id.music_list);
        trackList.setLayoutManager(new LinearLayoutManager(this));
        trackList.setAdapter(new TrackAdapter(this, musicViewModel.getTrackData(), trackList));
    }

    private void setupPlayPauseButton()
    {
        playButton = findViewById(R.id.play_pause_button);
        playButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CustomMediaPlayer.getInstance().startPlaying();
            }
        });
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle()
    {
        return lifecycleRegistry;
    }
}