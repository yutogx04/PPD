package com.codequest.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.model.Challenge;
import java.util.List;
public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ViewHolder> {
    private final List<Challenge> challenges;
    private final OnChallengeClickListener listener;
    public interface OnChallengeClickListener {
        void onChallengeClick(Challenge challenge);
    }
    public ChallengeAdapter(List<Challenge> challenges, OnChallengeClickListener listener) {
        this.challenges = challenges;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_challenge, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Challenge challenge = challenges.get(position);
        holder.tvChallengeTitle.setText(challenge.getTitle());

        String diffLabel;
        switch (challenge.getDifficulty() != null ? challenge.getDifficulty() : "") {
            case "EASY": diffLabel = holder.itemView.getContext().getString(R.string.difficulty_beginner); break;
            case "MEDIUM": diffLabel = holder.itemView.getContext().getString(R.string.difficulty_intermediate); break;
            case "HARD": diffLabel = holder.itemView.getContext().getString(R.string.difficulty_advanced); break;
            default: diffLabel = holder.itemView.getContext().getString(R.string.difficulty_beginner); break;
        }
        String lang = challenge.getLanguage() != null ? challenge.getLanguage() : "Python";
        holder.tvChallengeMeta.setText(String.format("%s · %s · +%d XP", diffLabel, lang, challenge.getXpReward()));

        if (challenge.isSolved()) {
            holder.tvStatus.setText("✓");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.success));
        } else {
            holder.tvStatus.setText("→");
            holder.tvStatus.setTextColor(holder.itemView.getContext().getColor(R.color.primary));
        }

        holder.itemView.setOnClickListener(v -> listener.onChallengeClick(challenge));
    }
    @Override
    public int getItemCount() { return challenges.size(); }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvChallengeTitle, tvChallengeMeta, tvStatus;
        View tvDifficultyIcon;
        ViewHolder(View v) {
            super(v);
            tvChallengeTitle = v.findViewById(R.id.tvChallengeTitle);
            tvChallengeMeta = v.findViewById(R.id.tvChallengeMeta);
            tvDifficultyIcon = v.findViewById(R.id.tvDifficultyIcon);
            tvStatus = v.findViewById(R.id.tvStatus);
        }
    }
}
