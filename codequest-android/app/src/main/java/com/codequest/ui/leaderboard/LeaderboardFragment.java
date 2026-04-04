package com.codequest.ui.leaderboard;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.model.LeaderboardEntry;
import com.codequest.ui.adapter.LeaderboardAdapter;
import com.codequest.viewmodel.LeaderboardViewModel;
import java.util.ArrayList;
import java.util.List;
public class LeaderboardFragment extends Fragment {
    private LeaderboardViewModel viewModel;
    private TextView chipWeekly, chipMonthly, chipAllTime;
    private RecyclerView rvLeaderboard;
    private TextView tvName1, tvXp1, tvName2, tvXp2, tvName3, tvXp3;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_leaderboard, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(LeaderboardViewModel.class);
        initViews(view);
        setupChips();
        observeData(0);
    }
    private void initViews(View view) {
        chipWeekly = view.findViewById(R.id.chipWeekly);
        chipMonthly = view.findViewById(R.id.chipMonthly);
        chipAllTime = view.findViewById(R.id.chipAllTime);
        rvLeaderboard = view.findViewById(R.id.rvLeaderboard);
        tvName1 = view.findViewById(R.id.tvName1);
        tvXp1 = view.findViewById(R.id.tvXp1);
        tvName2 = view.findViewById(R.id.tvName2);
        tvXp2 = view.findViewById(R.id.tvXp2);
        tvName3 = view.findViewById(R.id.tvName3);
        tvXp3 = view.findViewById(R.id.tvXp3);
        rvLeaderboard.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private void setupChips() {
        TextView[] chips = {chipWeekly, chipMonthly, chipAllTime};
        View.OnClickListener chipListener = v -> {
            for (TextView chip : chips) {
                chip.setBackgroundResource(R.drawable.tab_inactive);
                chip.setTextColor(requireContext().getColor(R.color.text_secondary));
            }
            ((TextView) v).setBackgroundResource(R.drawable.tab_active);
            ((TextView) v).setTextColor(requireContext().getColor(R.color.white));
            if (v == chipWeekly) observeData(0);
            else if (v == chipMonthly) observeData(1);
            else observeData(2);
        };
        for (TextView chip : chips) chip.setOnClickListener(chipListener);
    }
    private void observeData(int tab) {
        switch (tab) {
            case 0:
                viewModel.getGlobalBoard().observe(getViewLifecycleOwner(), this::updateUI);
                break;
            case 1:
                viewModel.getWeeklyBoard().observe(getViewLifecycleOwner(), this::updateUI);
                break;
            case 2:
                viewModel.getFriendsBoard().observe(getViewLifecycleOwner(), this::updateUI);
                break;
        }
    }
    private void updateUI(List<LeaderboardEntry> entries) {
        if (entries == null || entries.isEmpty()) return;
        if (entries.size() >= 1) {
            tvName1.setText(entries.get(0).getPseudo());
            tvXp1.setText(entries.get(0).getXpFormatted());
        }
        if (entries.size() >= 2) {
            tvName2.setText(entries.get(1).getPseudo());
            tvXp2.setText(entries.get(1).getXpFormatted());
        } else {
            tvName2.setText("-");
            tvXp2.setText("- XP");
        }
        if (entries.size() >= 3) {
            tvName3.setText(entries.get(2).getPseudo());
            tvXp3.setText(entries.get(2).getXpFormatted());
        } else {
            tvName3.setText("-");
            tvXp3.setText("- XP");
        }
        List<LeaderboardEntry> rest = new ArrayList<>();
        for (int i = 3; i < entries.size(); i++) {
            rest.add(entries.get(i));
        }
        rvLeaderboard.setAdapter(new LeaderboardAdapter(rest));
    }
}
