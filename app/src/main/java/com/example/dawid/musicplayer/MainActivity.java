package com.example.dawid.musicplayer;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.media.MediaPlayer;
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
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity implements LifecycleOwner
{
    private LifecycleRegistry lifecycleRegistry;
    private MusicViewModel musicViewModel;
    private SeekBar seekBar;
    private ImageButton playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        lifecycleRegistry = new LifecycleRegistry(this);
        //EventObserver eventObserver = new EventObserver(this);
        musicViewModel = ViewModelProviders.of(this).get(MusicViewModel.class);
        setupUi();

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
        setupProgressBar();
        setupPlayPauseButton();
        setupForwardButton();
        setupBackwardButton();
        setupPlayerStateObserving();
    }

    private void setupToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    private void setupForwardButton()
    {
        ImageButton imageButton = findViewById(R.id.further_button);
        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                musicViewModel.handleForwardButtonClicked();
            }
        });
    }

    private void setupBackwardButton()
    {
        ImageButton imageButton = findViewById(R.id.rev_button);
        imageButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                musicViewModel.handleBackwardButtonClicked();
            }
        });
    }

    private void setupPlayerStateObserving()
    {
        CustomMediaPlayer.getInstance().getPlayerStateLiveData().observe(this, new Observer<CustomMediaPlayer.PlayerState>()
        {
            @Override
            public void onChanged(@Nullable CustomMediaPlayer.PlayerState playerState)
            {
                if(playerState == CustomMediaPlayer.PlayerState.Playing)
                {
                    playButton.setImageResource(android.R.drawable.ic_media_pause);
                }
                else
                {
                    playButton.setImageResource(android.R.drawable.ic_media_play);
                    if(playerState == CustomMediaPlayer.PlayerState.Prepared ||
                            playerState == CustomMediaPlayer.PlayerState.TrackNotSet)
                    {
                        seekBar.setProgress(0);
                    }
                }
            }
        });
    }

    private void setupCurrentTrackTitleView()
    {
        final TextView currentTitleText = findViewById(R.id.current_title_text);
        currentTitleText.setText("");
        CustomMediaPlayer.getInstance().getTrackData().getLiveDataCurrentTrack().observe(this, new Observer<Track>()
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
        RecyclerView trackList = findViewById(R.id.music_list);
        trackList.setLayoutManager(new LinearLayoutManager(this));
        trackList.setAdapter(new TrackAdapter(this, CustomMediaPlayer.getInstance().getTrackData(), trackList));
    }

    private void setupPlayPauseButton()
    {
        playButton = findViewById(R.id.play_pause_button);
        playButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                musicViewModel.handleStartPauseButtonClicked();
            }
        });
    }

    private void setupProgressBar()
    {
        seekBar = findViewById(R.id.progress_bar);
        seekBar.setMax(500);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progressPoints, boolean isMovedByUser)
            {
                if(isMovedByUser)
                {
                    if(CustomMediaPlayer.getInstance().getPlayerStateLiveData().getValue()
                            != CustomMediaPlayer.PlayerState.TrackNotSet)
                    {
                        int percent = (int) ((((float) (progressPoints)) / seekBar.getMax()) * 100);
                        CustomMediaPlayer.getInstance().moveToPercentOfTrack(percent);
                    }
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        new Thread()
        {
            private MediaPlayer mediaPlayer;
            @Override
            public void run()
            {
                while(true)
                {
                    mediaPlayer = CustomMediaPlayer.getInstance().getMediaPlayer();
                    if(CustomMediaPlayer.getInstance().getPlayerStateLiveData().getValue() == CustomMediaPlayer.PlayerState.Playing)
                    {
                        float timeElapsed = mediaPlayer.getCurrentPosition();
                        float duration = mediaPlayer.getDuration();
                        seekBar.setProgress((int)(timeElapsed / duration * seekBar.getMax()));
                    }

                    synchronized (this)
                    {
                        try { wait(50); }
                        catch (InterruptedException e) { e.printStackTrace(); }
                    }
                }
            }
        }.start();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle()
    {
        return lifecycleRegistry;
    }
}