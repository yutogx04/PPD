package com.codequest.ui.challenge;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import com.codequest.ui.base.BaseActivity;
import androidx.lifecycle.ViewModelProvider;
import com.codequest.R;
import com.codequest.util.LocaleHelper;
import com.codequest.viewmodel.EditorViewModel;
public class EditorActivity extends BaseActivity {
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.setLocale(newBase, LocaleHelper.getLanguage(newBase)));
    }
    private EditorViewModel viewModel;
    private EditText etCode;
    private Button btnRun, btnSubmit;
    private TextView tvChallengeTitle, tvDescription, tvConsole, tvEditorLang;
    private LinearLayout consoleContainer, statementSection, codeSection;
    private TextView tabStatement, tabCode, tabResult;
    private ActivityResultLauncher<Intent> resultLauncher;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
        viewModel = new ViewModelProvider(this).get(EditorViewModel.class);
        long challengeId = getIntent().getLongExtra("challengeId", 1);
        
        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK) {
                        
                        com.codequest.repository.UserRepository userRepo =
                                new com.codequest.repository.UserRepository(getApplication());
                        userRepo.getProfile(); 
                        finish(); 
                    }
                });
        initViews();
        viewModel.loadChallenge(challengeId);
        observeData();
        setupListeners();
        setupTabs();
    }
    private void initViews() {
        etCode = findViewById(R.id.etCode);
        btnRun = findViewById(R.id.btnRun);
        btnSubmit = findViewById(R.id.btnSubmit);
        tvChallengeTitle = findViewById(R.id.tvChallengeTitle);
        tvEditorLang = findViewById(R.id.tvEditorLang);
        tvDescription = findViewById(R.id.tvStatement);
        tvConsole = findViewById(R.id.tvConsoleOutput);
        consoleContainer = findViewById(R.id.consoleSection);
        statementSection = findViewById(R.id.statementSection);
        codeSection = findViewById(R.id.codeSection);
        tabStatement = findViewById(R.id.tabStatement);
        tabCode = findViewById(R.id.tabCode);
        tabResult = findViewById(R.id.tabResult);

        View btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        View btnHint = findViewById(R.id.btnHint);
        if (btnHint != null) {
            btnHint.setOnClickListener(v -> {
                viewModel.getHint().observe(this, hint -> {
                    String hintText = (hint != null) ? hint : "Aucun indice disponible.";
                    new AlertDialog.Builder(this)
                            .setTitle("Indice")
                            .setMessage(hintText)
                            .setPositiveButton("Compris", null)
                            .show();
                });
            });
        }
    }
    private void setupTabs() {
        TextView[] tabs = {tabStatement, tabCode, tabResult};
        View.OnClickListener tabListener = v -> {
            
            for (TextView tab : tabs) {
                tab.setBackgroundResource(0); 
                tab.setTextColor(getColor(R.color.text_secondary));
            }
            
            ((TextView) v).setBackgroundResource(R.drawable.tab_active);
            ((TextView) v).setTextColor(getColor(R.color.white));

            if (v == tabStatement) {
                statementSection.setVisibility(View.VISIBLE);
                codeSection.setVisibility(View.GONE);
                consoleContainer.setVisibility(View.GONE);
            } else if (v == tabCode) {
                statementSection.setVisibility(View.GONE);
                codeSection.setVisibility(View.VISIBLE);
                consoleContainer.setVisibility(View.GONE);
            } else if (v == tabResult) {
                statementSection.setVisibility(View.GONE);
                codeSection.setVisibility(View.GONE);
                consoleContainer.setVisibility(View.VISIBLE);
                if (tvConsole.getText().toString().isEmpty()) {
                    tvConsole.setText("Execute your code to see the results here.");
                }
            }
        };
        for (TextView tab : tabs) {
            if (tab != null) tab.setOnClickListener(tabListener);
        }
    }
    private void observeData() {
        viewModel.getChallenge().observe(this, challenge -> {
            if (challenge != null) {
                tvChallengeTitle.setText(challenge.getTitle());
                tvDescription.setText(challenge.getDescription());
                if (tvEditorLang != null && challenge.getLanguage() != null) {
                    tvEditorLang.setText(challenge.getLanguage());
                }
                
                TextView tvInput = findViewById(R.id.tvExampleInput);
                TextView tvOutput = findViewById(R.id.tvExampleOutput);
                if (tvInput != null) {
                    tvInput.setText(challenge.getExampleInput() != null ? challenge.getExampleInput() : "\"hello\"");
                }
                if (tvOutput != null) {
                    tvOutput.setText(challenge.getExampleOutput() != null ? challenge.getExampleOutput() : "\"olleh\"");
                }

                if (challenge.getStarterCode() != null) {
                    etCode.setText(challenge.getStarterCode());
                    viewModel.setCurrentCode(challenge.getStarterCode());
                }
            }
        });
        viewModel.getIsSubmitEnabled().observe(this, enabled -> {
            btnSubmit.setEnabled(enabled);
            btnSubmit.setAlpha(enabled ? 1f : 0.5f);
        });
        viewModel.getErrorMessage().observe(this, error -> {
            if (error != null && !error.isEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show();
            }
        });

        etCode.addTextChangedListener(new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(android.text.Editable s) {
                viewModel.setCurrentCode(s.toString());
                updateLineNumbers();
            }
        });
    }
    private void setupListeners() {
        btnRun.setOnClickListener(v -> {
            
            if (tabResult != null) tabResult.performClick();
            String lang = (viewModel.getChallenge().getValue() != null && viewModel.getChallenge().getValue().getLanguage() != null) 
                          ? viewModel.getChallenge().getValue().getLanguage() 
                          : "PYTHON";
            viewModel.runCode(lang).observe(this, result -> {
                consoleContainer.setVisibility(View.VISIBLE);
                if (result != null) {
                    tvConsole.setText(result.getOutput() != null ? result.getOutput() : "Execution finished");
                } else {
                    tvConsole.setText("Erreur de connexion");
                }
            });
        });
        btnSubmit.setOnClickListener(v -> {
            String lang = (viewModel.getChallenge().getValue() != null && viewModel.getChallenge().getValue().getLanguage() != null) 
                          ? viewModel.getChallenge().getValue().getLanguage() 
                          : "PYTHON";
            viewModel.submitCode(lang).observe(this, result -> {
                consoleContainer.setVisibility(View.VISIBLE);
                if (result != null) {
                    if ("HTTP_ERROR".equals(result.getStatus()) || "NETWORK_ERROR".equals(result.getStatus())) {
                        tvConsole.setText("Erreur: " + result.getErrorMessage());
                        return;
                    }
                    
                    Intent intent = new Intent(this, ResultActivity.class);
                    intent.putExtra("challengeId", getIntent().getLongExtra("challengeId", 1));
                    intent.putExtra("score", result.getScore());
                    intent.putExtra("grade", result.getGrade());
                    intent.putExtra("testsPassed", result.getTestCasesPassed());
                    intent.putExtra("testsTotal", result.getTestCasesTotal());
                    intent.putExtra("xpGained", result.getXpGained());
                    intent.putExtra("bonusXp", result.getBonusXp());

                    if (result.getTestResults() != null) {
                        java.util.ArrayList<String> inputs = new java.util.ArrayList<>();
                        java.util.ArrayList<String> expected = new java.util.ArrayList<>();
                        java.util.ArrayList<String> actual = new java.util.ArrayList<>();
                        java.util.ArrayList<Boolean> passed = new java.util.ArrayList<>();
                        for (com.codequest.model.TestCaseResult tc : result.getTestResults()) {
                            inputs.add(tc.getInput());
                            expected.add(tc.getExpectedOutput());
                            actual.add(tc.getActualOutput());
                            passed.add(tc.isPassed());
                        }
                        intent.putStringArrayListExtra("testInputs", inputs);
                        intent.putStringArrayListExtra("testExpected", expected);
                        intent.putStringArrayListExtra("testActual", actual);
                        intent.putExtra("testPassed", passed);
                    }
                    
                    if (result.hasBadges()) {
                        intent.putExtra("badgeName", result.getBadgesUnlocked().get(0).getName());
                    }
                    
                    resultLauncher.launch(intent);
                } else {
                    tvConsole.setText("Erreur de soumission");
                }
            });
        });
    }

    private void updateLineNumbers() {
        TextView tvLineNumbers = findViewById(R.id.tvLineNumbers);
        if (tvLineNumbers != null && etCode != null) {
            int lines = etCode.getLineCount();
            if (lines == 0) lines = 1;
            StringBuilder sb = new StringBuilder();
            for (int i = 1; i <= lines; i++) {
                sb.append(i).append("\n");
            }
            tvLineNumbers.setText(sb.toString());
        }
    }
}

