package com.codequest.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.model.Module;
import java.util.List;
public class ModuleAdapter extends RecyclerView.Adapter<ModuleAdapter.ViewHolder> {
    private final List<Module> modules;
    private final OnModuleClickListener listener;
    public interface OnModuleClickListener {
        void onModuleClick(Module module);
    }
    public ModuleAdapter(List<Module> modules, OnModuleClickListener listener) {
        this.modules = modules;
        this.listener = listener;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_module, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Module module = modules.get(position);
        holder.tvModuleTitle.setText(module.getTitle());
        holder.tvModuleMeta.setText(String.format("%d leçons · %d défis",
                module.getLessonCount(), module.getChallengeCount()));
        holder.moduleProgress.setProgress(module.getProgressPercent());
        holder.tvModuleProgress.setText(module.getProgressPercent() + "%");
        holder.itemView.setOnClickListener(v -> listener.onModuleClick(module));
    }
    @Override
    public int getItemCount() { return modules.size(); }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvModuleTitle, tvModuleMeta, tvModuleProgress;
        ProgressBar moduleProgress;
        ViewHolder(View v) {
            super(v);
            tvModuleTitle = v.findViewById(R.id.tvModuleTitle);
            tvModuleMeta = v.findViewById(R.id.tvModuleMeta);
            moduleProgress = v.findViewById(R.id.moduleProgress);
            tvModuleProgress = v.findViewById(R.id.tvModuleProgress);
        }
    }
}
