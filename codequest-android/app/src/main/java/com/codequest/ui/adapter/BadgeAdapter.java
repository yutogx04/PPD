package com.codequest.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.model.Badge;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class BadgeAdapter extends RecyclerView.Adapter<BadgeAdapter.ViewHolder> {
    private final List<Badge> badges;
    private static final Map<String, Integer> ICON_MAP = new HashMap<>();
    static {
        ICON_MAP.put("badge_first_lesson", R.drawable.ic_badge_first_step);
        ICON_MAP.put("badge_first_challenge", R.drawable.ic_badge_code);
        ICON_MAP.put("badge_first_attempt", R.drawable.ic_badge_bullseye);
        ICON_MAP.put("badge_streak_7", R.drawable.ic_badge_fire);
        ICON_MAP.put("badge_streak_30", R.drawable.ic_badge_heart);
        ICON_MAP.put("badge_10_challenges", R.drawable.ic_badge_sword);
        ICON_MAP.put("badge_2_tracks", R.drawable.ic_badge_compass);
        ICON_MAP.put("badge_night", R.drawable.ic_badge_moon);
        ICON_MAP.put("badge_python_complete", R.drawable.ic_badge_python);
        ICON_MAP.put("badge_level_6", R.drawable.ic_badge_trophy);
    }
    public BadgeAdapter(List<Badge> badges) {
        this.badges = badges;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_badge, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Badge badge = badges.get(position);
        holder.tvBadgeName.setText(badge.getName());
        
        Integer iconRes = ICON_MAP.get(badge.getIcon());
        if (iconRes != null) {
            holder.ivBadgeIcon.setImageResource(iconRes);
        } else {
            holder.ivBadgeIcon.setImageResource(R.drawable.ic_badge);
        }
        if (!badge.isEarned()) {
            holder.itemView.setAlpha(0.4f);
            holder.ivLockOverlay.setVisibility(View.VISIBLE);
        } else {
            holder.itemView.setAlpha(1.0f);
            holder.ivLockOverlay.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() { return badges.size(); }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvBadgeName;
        ImageView ivBadgeIcon, ivLockOverlay;
        ViewHolder(View v) {
            super(v);
            tvBadgeName = v.findViewById(R.id.tvBadgeName);
            ivBadgeIcon = v.findViewById(R.id.ivBadgeIcon);
            ivLockOverlay = v.findViewById(R.id.ivLockOverlay);
        }
    }
}
