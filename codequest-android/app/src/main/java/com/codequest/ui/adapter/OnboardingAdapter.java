package com.codequest.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
public class OnboardingAdapter extends RecyclerView.Adapter<OnboardingAdapter.SlideViewHolder> {
    private final String[] titles;
    private final String[] descriptions;
    private final int[] icons;
    public OnboardingAdapter(String[] titles, String[] descriptions, int[] icons) {
        this.titles = titles;
        this.descriptions = descriptions;
        this.icons = icons;
    }
    @NonNull
    @Override
    public SlideViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_onboarding_slide, parent, false);
        return new SlideViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull SlideViewHolder holder, int position) {
        holder.tvTitle.setText(titles[position]);
        holder.tvDesc.setText(descriptions[position]);
        holder.ivIcon.setImageResource(icons[position]);
    }
    @Override
    public int getItemCount() { return titles.length; }
    static class SlideViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvDesc;
        android.widget.ImageView ivIcon;
        SlideViewHolder(View v) {
            super(v);
            tvTitle = v.findViewById(R.id.tvSlideTitle);
            tvDesc = v.findViewById(R.id.tvSlideDesc);
            ivIcon = v.findViewById(R.id.ivSlideIcon);
        }
    }
}
