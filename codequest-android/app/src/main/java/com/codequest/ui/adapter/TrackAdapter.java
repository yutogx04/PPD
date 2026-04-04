package com.codequest.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.model.Track;
import java.util.List;
public class TrackAdapter extends RecyclerView.Adapter<TrackAdapter.TrackViewHolder> {
    private final List<Track> tracks;
    private final OnTrackClickListener listener;
    public interface OnTrackClickListener {
        void onTrackClick(Track track);
    }
    public TrackAdapter(List<Track> tracks, OnTrackClickListener listener) {
        this.tracks = tracks;
        this.listener = listener;
    }
    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_track_card, parent, false);
        return new TrackViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder holder, int position) {
        Track track = tracks.get(position);
        holder.bind(track);
    }
    @Override
    public int getItemCount() { return tracks.size(); }
    class TrackViewHolder extends RecyclerView.ViewHolder {
        TextView tvTrackName, tvDescription, tvDifficulty;
        ImageView tvLangIcon;
        TextView tvModules, tvLessons, tvChallenges, tvXpPerLesson, tvProgressPercent;
        ProgressBar progressBar;
        Button btnAction;
        TrackViewHolder(View v) {
            super(v);
            tvTrackName = v.findViewById(R.id.tvTrackName);
            tvDescription = v.findViewById(R.id.tvDescription);
            tvLangIcon = v.findViewById(R.id.tvLangIcon);
            tvDifficulty = v.findViewById(R.id.tvDifficulty);
            tvModules = v.findViewById(R.id.tvModules);
            tvLessons = v.findViewById(R.id.tvLessons);
            tvChallenges = v.findViewById(R.id.tvChallenges);
            tvXpPerLesson = v.findViewById(R.id.tvXpPerLesson);
            tvProgressPercent = v.findViewById(R.id.tvProgressPercent);
            progressBar = v.findViewById(R.id.progressBar);
            btnAction = v.findViewById(R.id.btnAction);
        }
        void bind(Track track) {
            tvTrackName.setText(track.getTitle());
            tvDescription.setText(track.getDescription());

            tvModules.setText(itemView.getContext().getString(R.string.format_modules, track.getModuleCount()));
            tvLessons.setText(itemView.getContext().getString(R.string.format_lessons, track.getLessonCount()));
            tvChallenges.setText(itemView.getContext().getString(R.string.format_challenges, track.getChallengeCount()));
            tvXpPerLesson.setText(itemView.getContext().getString(R.string.format_xp_per_lesson, track.getXpPerLesson()));
            progressBar.setProgress(track.getProgressPercent());
            tvProgressPercent.setText(itemView.getContext().getString(R.string.format_percent, track.getProgressPercent()));
            switch (track.getDifficulty()) {
                case "BEGINNER":
                    tvDifficulty.setText(itemView.getContext().getString(R.string.difficulty_beginner));
                    tvDifficulty.setTextColor(tvDifficulty.getContext().getColor(R.color.success));
                    break;
                case "INTERMEDIATE":
                    tvDifficulty.setText(itemView.getContext().getString(R.string.difficulty_intermediate));
                    tvDifficulty.setTextColor(tvDifficulty.getContext().getColor(R.color.warning));
                    break;
                case "ADVANCED":
                    tvDifficulty.setText(itemView.getContext().getString(R.string.difficulty_advanced));
                    tvDifficulty.setTextColor(tvDifficulty.getContext().getColor(R.color.error));
                    break;
            }
            if (track.isLocked()) {
                btnAction.setText(itemView.getContext().getString(R.string.action_level_required, track.getRequiredLevel()));
                btnAction.setEnabled(false);
                btnAction.setAlpha(0.5f);
            } else if (track.getProgressPercent() > 0) {
                btnAction.setText(itemView.getContext().getString(R.string.action_continue));
            } else {
                btnAction.setText(itemView.getContext().getString(R.string.action_start));
            }
            btnAction.setOnClickListener(v -> {
                if (!track.isLocked()) listener.onTrackClick(track);
            });
            itemView.setOnClickListener(v -> {
                if (!track.isLocked()) listener.onTrackClick(track);
            });
        }
    }
}
