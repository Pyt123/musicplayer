package com.example.dawid.musicplayer;

public class Track
{
    private int trackId;
    private String trackTitle;
    private String trackAuthor;
    private String trackLength;

    public Track(int trackId, String trackTitle, String trackAuthor, int trackLength)
    {
        this.trackId = trackId;
        this.trackTitle = trackTitle;
        this.trackAuthor = trackAuthor;
        this.trackLength = makeLengthTextFromSeconds(trackLength);
    }

    private String makeLengthTextFromSeconds(int seconds)
    {
        String min = String.valueOf(seconds / 60);
        String secs = String.valueOf(seconds % 60);
        if(secs.length() == 1) { secs = "0" + secs; }
        return min + ":" + secs;
    }

    public int getTrackId()
    {
        return trackId;
    }

    public String getTrackTitle()
    {
        return trackTitle;
    }

    public String getTrackAuthor()
    {
        return trackAuthor;
    }

    public String getTrackLength()
    {
        return String.valueOf(trackLength);
    }
}
