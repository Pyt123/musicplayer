package com.example.dawid.musicplayer;

import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.Observer;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

public class TrackAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener
{
    private Context context = null;
    private TrackData data = null;

    public TrackAdapter(Context context, TrackData data, RecyclerView recyclerView)
    {
        super();
        this.context = context;
        this.data = data;
        setSwipeToDeleteListener(recyclerView);
    }

    private void setSwipeToDeleteListener(RecyclerView recyclerView)
    {
        ItemTouchHelper.SimpleCallback itemTouchHelperCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.RIGHT | ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        final LayoutInflater inflater = LayoutInflater.from(context);
        final View row = inflater.inflate(R.layout.track_row, parent, false);
        final Item item = new Item(row);

        return item;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position)
    {
        final Track track = data.getTracks().get(position);
        final Item item = (Item)holder;
        item.titleView.setText(track.getTrackTitle());
        item.authorView.setText(track.getTrackAuthor());
        item.lengthView.setText(track.getTrackLength());
        item.playButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                CustomMediaPlayer.getInstance().startNewTrack(context, track.getTrackId());
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return data.getTracks().size();
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position)
    {
        if (viewHolder instanceof TrackAdapter.Item)
        {
            removeItemOnPosition(position);
        }
    }

    private void removeItemOnPosition(int position)
    {
        data.getTracks().remove(position);
        notifyItemRemoved(position);
    }

    public class Item extends RecyclerView.ViewHolder
    {
        private TextView titleView;
        private TextView authorView;
        private TextView lengthView;
        private ImageButton playButton;

        public Item(View itemView)
        {
            super(itemView);
            titleView = itemView.findViewById(R.id.track_title);
            authorView = itemView.findViewById(R.id.track_author);
            lengthView = itemView.findViewById(R.id.track_length);
            playButton = itemView.findViewById(R.id.play_track_button);
        }
    }
}