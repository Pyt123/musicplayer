package com.example.dawid.musicplayer;

import android.arch.lifecycle.LiveData;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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

        setClickListenerForItem(item);

        return item;
    }

    private void setClickListenerForItem(final Item item)
    {
        item.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                int idOfTrackInData = item.getLayoutPosition();
                int trackId = data.getTracks().get(idOfTrackInData).getTrackId();
                data.setCurrentTrack(idOfTrackInData);
                CustomMediaPlayer.getInstance().startNewTrack(context, trackId);
            }
        });
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        Track track = data.getTracks().get(position);
        Item item = (Item)holder;
        item.titleView.setText(track.getTrackTitle());
        item.authorView.setText(track.getTrackAuthor());
        item.lengthView.setText(track.getTrackLength());
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