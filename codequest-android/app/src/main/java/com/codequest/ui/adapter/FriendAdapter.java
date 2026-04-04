package com.codequest.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.model.Friend;
import java.util.List;
public class FriendAdapter extends RecyclerView.Adapter<FriendAdapter.ViewHolder> {
    private final List<Friend> friends;
    private final boolean showActions; 
    private final OnFriendActionListener listener;
    public interface OnFriendActionListener {
        void onAction(long friendId);
    }
    public FriendAdapter(List<Friend> friends, boolean showActions, OnFriendActionListener listener) {
        this.friends = friends;
        this.showActions = showActions;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_friend, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Friend friend = friends.get(position);
        holder.tvPseudo.setText(friend.getPseudo());
        holder.tvStatus.setText(String.format("Niveau %d · %s",
                friend.getLevel(), friend.getLastActivity()));

        if (holder.ivAvatar != null) {
            holder.ivAvatar.setImageResource(R.drawable.ic_default_avatar);
        }

        if (showActions) {
            holder.btnAccept.setVisibility(View.VISIBLE);
            holder.btnReject.setVisibility(View.VISIBLE);
            holder.btnAccept.setOnClickListener(v -> {
                listener.onAction(friend.getId());
                
                Toast.makeText(v.getContext(), "Demande acceptée !", Toast.LENGTH_SHORT).show();
            });
            holder.btnReject.setOnClickListener(v -> {
                
                int pos = holder.getAdapterPosition();
                if (pos != RecyclerView.NO_POSITION && pos < friends.size()) {
                    friends.remove(pos);
                    notifyItemRemoved(pos);
                    Toast.makeText(v.getContext(), "Demande refusée", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            holder.btnAccept.setVisibility(View.GONE);
            holder.btnReject.setVisibility(View.GONE);
        }
    }
    @Override
    public int getItemCount() { return friends.size(); }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvPseudo, tvStatus;
        Button btnAccept, btnReject;
        ImageView ivAvatar;
        ViewHolder(View v) {
            super(v);
            tvPseudo = v.findViewById(R.id.tvPseudo);
            tvStatus = v.findViewById(R.id.tvStatus);
            btnAccept = v.findViewById(R.id.btnAccept);
            btnReject = v.findViewById(R.id.btnReject);
            ivAvatar = v.findViewById(R.id.ivAvatar);
        }
    }
}
