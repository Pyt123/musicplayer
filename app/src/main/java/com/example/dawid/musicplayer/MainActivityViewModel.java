package com.example.dawid.musicplayer;

import android.arch.lifecycle.ViewModel;

public class MainActivityViewModel extends ViewModel
{
    public MainActivityViewModel()
    {
        super();
    }

    public void handleStartPauseButtonClicked()
    {
        CustomMediaPlayer.getInstance().handleStartPauseButtonClicked();
    }

    public void handleForwardButtonClicked()
    {
        CustomMediaPlayer.getInstance().moveTime(10000);
    }

    public void handleBackwardButtonClicked()
    {
        CustomMediaPlayer.getInstance().moveTime(-10000);
    }
}
