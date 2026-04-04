package com.codequest.ui.adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.model.TestCaseResult;
import java.util.List;
public class TestResultAdapter extends RecyclerView.Adapter<TestResultAdapter.ViewHolder> {
    private final List<TestCaseResult> results;
    public TestResultAdapter(List<TestCaseResult> results) {
        this.results = results;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_test_result, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TestCaseResult result = results.get(position);
        holder.tvTestName.setText("Test " + (position + 1));
        holder.tvTestDetail.setText("Entrée: " + result.getInput() + " → Attendu: " + result.getExpectedOutput());
        holder.tvTestTime.setText(result.getTimeMs() + "ms");
        if (result.isPassed()) {
            holder.ivResultIcon.setImageResource(R.drawable.ic_check);
            holder.ivResultIcon.setColorFilter(holder.itemView.getContext().getColor(R.color.success));
            holder.tvTestName.setTextColor(holder.itemView.getContext().getColor(R.color.success));
        } else {
            holder.ivResultIcon.setImageResource(R.drawable.ic_close);
            holder.ivResultIcon.setColorFilter(holder.itemView.getContext().getColor(R.color.error));
            holder.tvTestName.setTextColor(holder.itemView.getContext().getColor(R.color.error));
        }
    }
    @Override
    public int getItemCount() { return results.size(); }
    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTestName, tvTestDetail, tvTestTime;
        ImageView ivResultIcon;
        ViewHolder(View v) {
            super(v);
            tvTestName = v.findViewById(R.id.tvTestName);
            tvTestDetail = v.findViewById(R.id.tvTestDetail);
            tvTestTime = v.findViewById(R.id.tvTestTime);
            ivResultIcon = v.findViewById(R.id.ivResultIcon);
        }
    }
}
