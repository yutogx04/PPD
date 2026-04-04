package com.codequest.viewmodel;
import android.app.Application;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.Challenge;
import com.codequest.model.dto.SubmissionResponse;
import com.codequest.repository.ChallengeRepository;
import com.codequest.util.Constants;
public class EditorViewModel extends AndroidViewModel {
    private final ChallengeRepository challengeRepository;
    private LiveData<Challenge> challenge;
    private final MutableLiveData<Boolean> isLoading = new MutableLiveData<>(false);
    private final MutableLiveData<Boolean> isSubmitEnabled = new MutableLiveData<>(true);
    private final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    private final MutableLiveData<Integer> attemptCount = new MutableLiveData<>(0);
    private final MutableLiveData<String> consoleOutput = new MutableLiveData<>("");
    private long challengeId;
    private String currentCode = "";
    private final Handler handler = new Handler(Looper.getMainLooper());
    public EditorViewModel(@NonNull Application application) {
        super(application);
        challengeRepository = new ChallengeRepository();
    }
    public void loadChallenge(long challengeId) {
        this.challengeId = challengeId;
        challenge = challengeRepository.getChallenge(challengeId);
    }
    public LiveData<Challenge> getChallenge() { return challenge; }
    public LiveData<Boolean> getIsLoading() { return isLoading; }
    public LiveData<Boolean> getIsSubmitEnabled() { return isSubmitEnabled; }
    public LiveData<String> getErrorMessage() { return errorMessage; }
    public LiveData<Integer> getAttemptCount() { return attemptCount; }
    public LiveData<String> getConsoleOutput() { return consoleOutput; }
    public void setCurrentCode(String code) { this.currentCode = code; }
    public String getCurrentCode() { return currentCode; }
    public LiveData<SubmissionResponse> runCode(String language) {
        if (currentCode.length() > Constants.MAX_CODE_LENGTH) {
            errorMessage.setValue("Le code ne doit pas dépasser 10 000 caractères");
            return new MutableLiveData<>(null);
        }
        isLoading.setValue(true);
        consoleOutput.setValue("Exécution en cours...");
        LiveData<SubmissionResponse> result = challengeRepository.runCode(challengeId, currentCode, language);
        isLoading.setValue(false);
        return result;
    }
    public LiveData<SubmissionResponse> submitCode(String language) {
        if (currentCode.length() > Constants.MAX_CODE_LENGTH) {
            errorMessage.setValue("Le code ne doit pas dépasser 10 000 caractères");
            return new MutableLiveData<>(null);
        }
        isSubmitEnabled.setValue(false);
        isLoading.setValue(true);
        int attempts = attemptCount.getValue() != null ? attemptCount.getValue() : 0;
        attemptCount.setValue(attempts + 1);
        LiveData<SubmissionResponse> result = challengeRepository.submitCode(challengeId, currentCode, language);
        isLoading.setValue(false);
        handler.postDelayed(() -> isSubmitEnabled.setValue(true), Constants.SUBMIT_COOLDOWN_MS);
        return result;
    }
    public boolean isHintAvailable() {
        int attempts = attemptCount.getValue() != null ? attemptCount.getValue() : 0;
        return attempts >= Constants.HINT_THRESHOLD;
    }
    public boolean isSolutionAvailable() {
        int attempts = attemptCount.getValue() != null ? attemptCount.getValue() : 0;
        return attempts >= Constants.SOLUTION_THRESHOLD;
    }
    public LiveData<String> getHint() {
        return challengeRepository.getHint(challengeId);
    }
}
