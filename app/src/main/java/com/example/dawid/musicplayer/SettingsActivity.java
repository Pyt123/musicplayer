package com.example.dawid.musicplayer;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.LifecycleRegistry;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity implements LifecycleOwner
{
    public static final String MODE_KEY = "mode_key";
    public static final String TIME_KEY = "time_key";
    public static final String PREFS_NAME = "player_prefs";

    private Button loopAllButton;
    private Button loopOneButton;
    private Button playAllOnceButton;
    private Button playOneOnceButton;
    private Button playRandomButton;
    private SeekBar timeSeekbar;
    private TextView timeText;

    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor prefsEditor;

    private LifecycleRegistry lifecycleRegistry;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        lifecycleRegistry = new LifecycleRegistry(this);

        setupToolbar();
        findViews();
        setupSharedPreferences();
        setPreferencesToView();
        setupButtons();
        setupTimeText();

        getLifecycle().addObserver(new SettingsActivityObserver(this));

        lifecycleRegistry.markState(Lifecycle.State.CREATED);
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED);
    }

    private void setupToolbar()
    {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void findViews()
    {
        loopAllButton = findViewById(R.id.looped_all_button);
        loopOneButton = findViewById(R.id.looped_one_button);
        playAllOnceButton = findViewById(R.id.play_all_once_button);
        playOneOnceButton = findViewById(R.id.play_one_once_button);
        playRandomButton = findViewById(R.id.play_random_button);
        timeSeekbar = findViewById(R.id.time_seekbar);
        timeText = findViewById(R.id.time_text);
    }

    private void setupSharedPreferences()
    {
        sharedPrefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        prefsEditor = sharedPrefs.edit();
    }

    private void setPreferencesToView()
    {
        //int indexOfMode = sharedPrefs.getInt(MODE_KEY, 0);
        int time = sharedPrefs.getInt(TIME_KEY, 30);
        timeSeekbar.setProgress(time);
        timeText.setText(String.valueOf(time) + " min");
        //CustomMediaPlayer.Mode mode = CustomMediaPlayer.Mode.values()[indexOfMode];

    }

    private void setupButtons()
    {
        loopAllButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                prefsEditor.putInt(MODE_KEY, 0);
                saveAndBack();
            }
        });

        loopOneButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                prefsEditor.putInt(MODE_KEY, 1);
                saveAndBack();
            }
        });

        playAllOnceButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                prefsEditor.putInt(MODE_KEY, 2);
                saveAndBack();
            }
        });

        playOneOnceButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                prefsEditor.putInt(MODE_KEY, 3);
                saveAndBack();
            }
        });

        playRandomButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                prefsEditor.putInt(MODE_KEY, 4);
                saveAndBack();
            }
        });
    }

    private void setupTimeText()
    {
        timeSeekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b)
            {
                timeText.setText(String.valueOf(i) + " min");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });
    }

    private void saveAndBack()
    {
        prefsEditor.putInt(TIME_KEY, timeSeekbar.getProgress());
        prefsEditor.apply();
        finish();
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle()
    {
        return lifecycleRegistry;
    }

    public SharedPreferences getSharedPrefs()
    {
        return sharedPrefs;
    }
}
