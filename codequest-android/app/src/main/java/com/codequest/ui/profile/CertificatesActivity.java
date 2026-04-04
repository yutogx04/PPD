package com.codequest.ui.profile;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.codequest.R;
import com.codequest.model.Track;
import com.codequest.ui.adapter.CertificateAdapter;
import com.codequest.viewmodel.CertificatesViewModel;

import java.util.ArrayList;
import java.util.List;

public class CertificatesActivity extends BaseActivity {

    private CertificatesViewModel viewModel;
    private ProgressBar progressBar;
    private RecyclerView rvCertificates;
    private LinearLayout emptyState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certificates);

        viewModel = new ViewModelProvider(this).get(CertificatesViewModel.class);

        ImageView ivBack = findViewById(R.id.ivBack);
        progressBar = findViewById(R.id.progressBar);
        rvCertificates = findViewById(R.id.rvCertificates);
        emptyState = findViewById(R.id.emptyState);

        ivBack.setOnClickListener(v -> finish());
        
        rvCertificates.setLayoutManager(new LinearLayoutManager(this));

        progressBar.setVisibility(View.VISIBLE);
        viewModel.getTracks().observe(this, tracks -> {
            progressBar.setVisibility(View.GONE);
            if (tracks != null) {
                
                List<Track> completedTracks = new ArrayList<>();
                for (Track t : tracks) {
                    if (t.getProgressPercent() >= 100) {
                        completedTracks.add(t);
                    }
                }
                
                if (completedTracks.isEmpty()) {
                    emptyState.setVisibility(View.VISIBLE);
                    rvCertificates.setVisibility(View.GONE);
                } else {
                    emptyState.setVisibility(View.GONE);
                    rvCertificates.setVisibility(View.VISIBLE);
                    rvCertificates.setAdapter(new CertificateAdapter(completedTracks));
                }
            } else {
                Toast.makeText(this, "Erreur lors du chargement des certificats", Toast.LENGTH_SHORT).show();
                emptyState.setVisibility(View.VISIBLE);
            }
        });
    }
}

