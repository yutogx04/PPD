package com.codequest.ui.catalogue;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.ui.adapter.TrackAdapter;
import com.codequest.util.SharedPrefManager;
import com.codequest.viewmodel.CatalogueViewModel;
public class CatalogueFragment extends Fragment {
    private CatalogueViewModel viewModel;
    private RecyclerView rvTracks;
    private EditText etSearch;
    private TextView chipAll, chipBeginner, chipInter, chipAdvanced;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_catalogue, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(CatalogueViewModel.class);
        rvTracks = view.findViewById(R.id.rvTracks);
        etSearch = view.findViewById(R.id.etSearch);
        chipAll = view.findViewById(R.id.chipAll);
        chipBeginner = view.findViewById(R.id.chipBeginner);
        chipInter = view.findViewById(R.id.chipInter);
        chipAdvanced = view.findViewById(R.id.chipAdvanced);
        setupChips();
        setupSearch();
        setupRecyclerView();
        observeData();
    }
    private void setupChips() {
        TextView[] chips = {chipAll, chipBeginner, chipInter, chipAdvanced};
        String[] filters = {"ALL", "BEGINNER", "INTERMEDIATE", "ADVANCED"};
        View.OnClickListener chipListener = v -> {
            for (TextView chip : chips) {
                chip.setBackgroundResource(R.drawable.tab_inactive);
                chip.setTextColor(requireContext().getColor(R.color.text_secondary));
            }
            ((TextView) v).setBackgroundResource(R.drawable.tab_active);
            ((TextView) v).setTextColor(requireContext().getColor(R.color.white));
            for (int i = 0; i < chips.length; i++) {
                if (v == chips[i]) {
                    viewModel.setFilter(filters[i]);
                    break;
                }
            }
        };
        for (TextView chip : chips) chip.setOnClickListener(chipListener);
    }
    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {
                viewModel.setSearchQuery(s.toString());
            }
        });
    }
    private void setupRecyclerView() {
        rvTracks.setLayoutManager(new LinearLayoutManager(getContext()));
    }
    private void observeData() {
        viewModel.getAllTracks().observe(getViewLifecycleOwner(), tracks -> {
            if (tracks != null) {
                viewModel.setFilter("ALL");
            }
        });
        viewModel.getFilteredTracks().observe(getViewLifecycleOwner(), filteredTracks -> {
            if (filteredTracks != null) {
                int userLevel = SharedPrefManager.getInstance(requireContext()).getUserLevel();
                TrackAdapter adapter = new TrackAdapter(filteredTracks, track -> {
                    if (track.isLocked()) {
                        Toast.makeText(getContext(),
                                String.format(getString(R.string.locked_level), track.getRequiredLevel()),
                                Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Intent intent = new Intent(getContext(), TrackDetailActivity.class);
                    intent.putExtra("trackId", track.getId());
                    intent.putExtra("trackTitle", track.getTitle());
                    startActivity(intent);
                });
                rvTracks.setAdapter(adapter);
            }
        });
    }
}
