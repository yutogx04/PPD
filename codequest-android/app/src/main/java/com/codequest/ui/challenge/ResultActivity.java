package com.codequest.ui.challenge;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.codequest.R;
import com.codequest.ui.adapter.TestResultAdapter;
import com.codequest.util.Constants;
import java.util.ArrayList;
public class ResultActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        int score = getIntent().getIntExtra("score", 0);
        String grade = getIntent().getStringExtra("grade");
        int testsPassed = getIntent().getIntExtra("testsPassed", 0);
        int testsTotal = getIntent().getIntExtra("testsTotal", 0);
        int xpGained = getIntent().getIntExtra("xpGained", 0);
        int bonusXp = getIntent().getIntExtra("bonusXp", 0);

        if (grade == null) grade = Constants.getGrade(score);

        TextView tvResultTitle = findViewById(R.id.tvResultTitle);
        TextView tvResultSubtitle = findViewById(R.id.tvResultSubtitle);
        TextView tvBaseXp = findViewById(R.id.tvBaseXp);
        TextView tvBonusXp = findViewById(R.id.tvBonusXp);
        TextView tvTotalXp = findViewById(R.id.tvTotalXp);
        Button btnNextChallenge = findViewById(R.id.btnNextChallenge);
        Button btnSolution = findViewById(R.id.btnSolution);

        if (testsPassed == testsTotal && testsTotal > 0) {
            tvResultTitle.setText("Challenge Completed!");
            tvResultSubtitle.setText(String.format("All %d tests passed!", testsTotal));
        } else if (testsPassed > 0) {
            tvResultTitle.setText("Presque !");
            tvResultSubtitle.setText(String.format("%d / %d tests passed", testsPassed, testsTotal));
        } else {
            tvResultTitle.setText("Result");
            tvResultSubtitle.setText(String.format("%d / %d tests passed", testsPassed, testsTotal));
        }

        tvBaseXp.setText(String.format("+%d XP", xpGained));
        if (tvBonusXp != null) {
            tvBonusXp.setText(String.format("+%d XP", bonusXp));
        }
        int totalXp = xpGained + bonusXp;
        tvTotalXp.setText(String.format("+%d XP", totalXp));

        String badgeName = getIntent().getStringExtra("badgeName");
        LinearLayout badgeUnlockedCard = findViewById(R.id.badgeUnlockedCard);
        TextView tvBadgeName = findViewById(R.id.tvBadgeName);
        if (badgeName != null && badgeUnlockedCard != null && tvBadgeName != null) {
            badgeUnlockedCard.setVisibility(View.VISIBLE);
            tvBadgeName.setText(badgeName);
        }

        RecyclerView rvTestResults = findViewById(R.id.rvTestResults);
        if (rvTestResults != null) {
            rvTestResults.setLayoutManager(new LinearLayoutManager(this));
            
            ArrayList<String> testInputs = getIntent().getStringArrayListExtra("testInputs");
            ArrayList<String> testExpected = getIntent().getStringArrayListExtra("testExpected");
            ArrayList<String> testActual = getIntent().getStringArrayListExtra("testActual");
            ArrayList<Boolean> testPassed = (ArrayList<Boolean>) getIntent().getSerializableExtra("testPassed");

            if (testInputs != null && testExpected != null) {
                java.util.List<com.codequest.model.TestCaseResult> results = new java.util.ArrayList<>();
                for (int i = 0; i < testInputs.size(); i++) {
                    boolean passed = testPassed != null && i < testPassed.size() && testPassed.get(i);
                    String actual = (testActual != null && i < testActual.size()) ? testActual.get(i) : "";
                    results.add(new com.codequest.model.TestCaseResult(
                            testInputs.get(i), testExpected.get(i), actual, passed, 0));
                }
                rvTestResults.setAdapter(new TestResultAdapter(results));
            }
        }

        long challengeId = getIntent().getLongExtra("challengeId", 1);
        btnSolution.setOnClickListener(v -> {
            com.codequest.repository.ChallengeRepository repo = new com.codequest.repository.ChallengeRepository();
            String solution = repo.getSolution(challengeId);
            new AlertDialog.Builder(this)
                    .setTitle("Solution")
                    .setMessage(solution)
                    .setPositiveButton("Compris", null)
                    .show();
        });

        btnNextChallenge.setOnClickListener(v -> {
            setResult(RESULT_OK);
            finish();
        });
    }
}

