package com.codequest.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.model.Track;
import java.util.List;
public class TrackMiniAdapter extends RecyclerView.Adapter<TrackMiniAdapter.ViewHolder> {
    private final List<Track> tracks;
    private final OnTrackClickListener listener;
    public interface OnTrackClickListener {
        void onTrackClick(Track track);
    }
    public TrackMiniAdapter(List<Track> tracks, OnTrackClickListener listener) {
        this.tracks = tracks;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_track_mini, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Track track = tracks.get(position);
        holder.tvTitle.setText(track.getTitle());
        holder.tvLanguage.setText(track.getLanguageShort());
        holder.progressBar.setProgress(track.getProgressPercent());
        holder.tvProgress.setText(track.getProgressPercent() + "%");
        holder.itemView.setOnClickListener(v -> listener.onTrackClick(track));
    }
    @Override
    public int getItemCount() { return tracks.size(); }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvLanguage, tvProgress;
        ProgressBar progressBar;
        ViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvTitle);
            tvLanguage = v.findViewById(R.id.tvLanguage);
            tvProgress = v.findViewById(R.id.tvProgress);
            progressBar = v.findViewById(R.id.progressBar);
        }
    }
}
