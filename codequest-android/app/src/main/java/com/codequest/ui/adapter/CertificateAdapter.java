package com.codequest.ui.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codequest.R;
import com.codequest.model.Track;

import java.util.List;

public class CertificateAdapter extends RecyclerView.Adapter<CertificateAdapter.ViewHolder> {

    private final List<Track> tracks;

    public CertificateAdapter(List<Track> tracks) {
        this.tracks = tracks;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_certificate, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = tracks.get(position);
        holder.tvTrackTitle.setText(track.getTitle() != null ? track.getTitle() : "Parcours Inconnu");
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrackTitle;

        ViewHolder(View v) {
            super(v);
            tvTrackTitle = v.findViewById(R.id.tvTrackTitle);
        }
    }
}
