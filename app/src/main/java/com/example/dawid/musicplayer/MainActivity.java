package com.example.dawid.musicplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
{
    //private String storageDirectory;
    private RecyclerView trackList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolbar();
        //storageDirectory = Environment.getExternalStorageDirectory().getPath() + "/music/";﻿
        setupMusicList();
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
            case R.id.action_settings:
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupMusicList()
    {
        if(Data.getTracks()==null)
        {
            Data.loadTracks();
        }
        trackList = findViewById(R.id.music_list);
        trackList.setLayoutManager(new LinearLayoutManager(this));
        trackList.setAdapter(new TrackAdapter(this, Data.getTracks(), trackList));
    }
}
