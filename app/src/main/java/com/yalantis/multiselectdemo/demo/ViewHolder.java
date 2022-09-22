package com.yalantis.multiselectdemo.demo;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.yalantis.multiselectdemo.R;
import com.yalantis.multiselectdemo.demo.model.Track;

/**
 * Created by Artem Kholodnyi on 9/6/16.
 */
class ViewHolder extends RecyclerView.ViewHolder {
    TextView track;
    TextView artist;
    ImageView avatar;

    public ViewHolder(View view) {
        super(view);
        track = view.findViewById(R.id.track);
        artist = view.findViewById(R.id.artist);
        avatar = view.findViewById(R.id.yal_ms_avatar);
    }

    public static void bind(ViewHolder viewHolder, Track track) {
        viewHolder.track.setText(track.getTrackName());
        viewHolder.artist.setText(track.getArtist());
        viewHolder.avatar.setImageResource(track.getAlbum());
    }
}
