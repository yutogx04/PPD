package com.codequest.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.model.LeaderboardEntry;
import java.util.List;
public class LeaderboardAdapter extends RecyclerView.Adapter<LeaderboardAdapter.ViewHolder> {
    private final List<LeaderboardEntry> entries;
    public LeaderboardAdapter(List<LeaderboardEntry> entries) {
        this.entries = entries;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_leaderboard_row, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaderboardEntry entry = entries.get(position);
        holder.tvRank.setText("#" + entry.getRank());
        holder.tvName.setText(entry.getPseudo());
        holder.tvLevel.setText("Niveau " + entry.getLevel());
        holder.tvXp.setText(entry.getXpFormatted());

        if (holder.ivAvatar != null) {
            holder.ivAvatar.setImageResource(R.drawable.ic_default_avatar);
        }

        if (entry.isCurrentUser()) {
            
            holder.itemView.setBackgroundColor(
                    holder.itemView.getContext().getColor(R.color.primary));
            holder.tvName.setTextColor(holder.itemView.getContext().getColor(R.color.white));
            holder.tvRank.setTextColor(holder.itemView.getContext().getColor(R.color.white));
            holder.tvLevel.setTextColor(holder.itemView.getContext().getColor(R.color.white));
            holder.tvXp.setTextColor(holder.itemView.getContext().getColor(R.color.white));
        } else {
            
            holder.itemView.setBackgroundColor(0); 
            holder.tvName.setTextColor(holder.itemView.getContext().getColor(R.color.text_primary));
            holder.tvRank.setTextColor(holder.itemView.getContext().getColor(R.color.text_muted));
            holder.tvLevel.setTextColor(holder.itemView.getContext().getColor(R.color.text_secondary));
            holder.tvXp.setTextColor(holder.itemView.getContext().getColor(R.color.primary));
        }
    }
    @Override
    public int getItemCount() { return entries.size(); }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvRank, tvName, tvLevel, tvXp;
        ImageView ivAvatar;
        ViewHolder(View v) {
            super(v);
            tvRank = v.findViewById(R.id.tvRank);
            tvName = v.findViewById(R.id.tvName);
            tvLevel = v.findViewById(R.id.tvLevel);
            tvXp = v.findViewById(R.id.tvXp);
            ivAvatar = v.findViewById(R.id.ivAvatar);
        }
    }
}
