package com.codequest.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.model.Lesson;
import java.util.List;
public class LessonAdapter extends RecyclerView.Adapter<LessonAdapter.ViewHolder> {
    private final List<Lesson> lessons;
    private final OnLessonClickListener listener;
    public interface OnLessonClickListener {
        void onLessonClick(Lesson lesson);
    }
    public LessonAdapter(List<Lesson> lessons, OnLessonClickListener listener) {
        this.lessons = lessons;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_lesson, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Lesson lesson = lessons.get(position);
        holder.tvLessonTitle.setText(lesson.getTitle());
        holder.tvLessonMeta.setText(String.format("%s · +%d XP · ~%d min",
                lesson.getLessonType(), lesson.getXpReward(), lesson.getEstimatedMinutes()));
        if (lesson.isCompleted()) {
            holder.tvCompleted.setVisibility(View.VISIBLE);
        } else {
            holder.tvCompleted.setVisibility(View.GONE);
        }
        holder.itemView.setOnClickListener(v -> listener.onLessonClick(lesson));
    }
    @Override
    public int getItemCount() { return lessons.size(); }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLessonTitle, tvLessonMeta;
        ImageView tvCompleted;
        ViewHolder(View v) {
            super(v);
            tvLessonTitle = v.findViewById(R.id.tvLessonTitle);
            tvLessonMeta = v.findViewById(R.id.tvLessonMeta);
            tvCompleted = v.findViewById(R.id.tvCompleted);
        }
    }
}
