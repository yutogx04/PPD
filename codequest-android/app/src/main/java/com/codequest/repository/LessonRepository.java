package com.codequest.repository;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.Lesson;
import com.codequest.model.LessonSlide;
import com.codequest.model.QuizQuestion;
import com.codequest.model.dto.GamificationResult;
import com.codequest.network.RetrofitClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class LessonRepository {
    private final boolean useMockData = false;
    public LiveData<List<Lesson>> getLessons(long moduleId) {
        MutableLiveData<List<Lesson>> data = new MutableLiveData<>();
        if (useMockData) {
            List<Lesson> lessons = Arrays.asList(
                    new Lesson(1, moduleId, "Introduction aux Fonctions", "THEORY", 20, 5),
                    new Lesson(2, moduleId, "Les Fonctions en Python", "THEORY", 20, 5),
                    new Lesson(3, moduleId, "Paramètres et Arguments", "THEORY", 20, 5),
                    new Lesson(4, moduleId, "Valeurs de Retour", "PRACTICE", 20, 5),
                    new Lesson(5, moduleId, "Fonctions Avancées", "THEORY", 20, 5)
            );
            lessons.get(0).setCompleted(true);
            lessons.get(1).setCompleted(true);
            lessons.get(1).setSavedSlideIndex(3);
            data.setValue(lessons);
            return data;
        }
        RetrofitClient.getApi().getLessons(moduleId).enqueue(new Callback<List<Lesson>>() {
            @Override
            public void onResponse(Call<List<Lesson>> call, Response<List<Lesson>> response) {
                data.setValue(response.isSuccessful() ? response.body() : null);
            }
            @Override
            public void onFailure(Call<List<Lesson>> call, Throwable t) { data.setValue(null); }
        });
        return data;
    }
    public LiveData<List<LessonSlide>> getLessonSlides(long lessonId) {
        MutableLiveData<List<LessonSlide>> data = new MutableLiveData<>();
        if (useMockData) {
            List<LessonSlide> slides = new ArrayList<>();
            LessonSlide s1 = new LessonSlide();
            s1.setId(1);
            s1.setOrderIndex(1);
            s1.setContentType("TEXT");
            s1.setContentText("Une **fonction** est un bloc de code réutilisable qui effectue une tâche précise.\nOn la définit avec le mot-clé `def`.");
            slides.add(s1);
            LessonSlide s2 = new LessonSlide();
            s2.setId(2);
            s2.setOrderIndex(2);
            s2.setContentType("CODE");
            s2.setContentText("Voici comment définir et appeler une fonction :");
            s2.setCodeSnippet("def saluer(nom):\n    return f\"Bonjour, {nom} !\"\n\n# Appeler la fonction\nmessage = saluer(\"Alice\")\nprint(message)  # Bonjour, Alice !");
            s2.setCodeLanguage("Python 3");
            s2.setExplanation("Les paramètres sont les variables entre parenthèses. `return` renvoie un résultat.");
            slides.add(s2);
            LessonSlide s3 = new LessonSlide();
            s3.setId(3);
            s3.setOrderIndex(3);
            s3.setContentType("TEXT");
            s3.setContentText("Les fonctions peuvent avoir **plusieurs paramètres** et des **valeurs par défaut** :");
            slides.add(s3);
            LessonSlide s4 = new LessonSlide();
            s4.setId(4);
            s4.setOrderIndex(4);
            s4.setContentType("QCM");
            QuizQuestion q = new QuizQuestion();
            q.setId(1);
            q.setQuestion("Que retourne la fonction suivante quand on appelle double(5) ?");
            q.setCodeSnippet("def double(x):\n    return x * 2");
            q.setCorrectAnswer("10");
            q.setWrongAnswer1("5");
            q.setWrongAnswer2("double5");
            q.setWrongAnswer3("None");
            q.setExplanation("double(5) retourne 5 × 2 = 10. La fonction multiplie son paramètre par 2.");
            s4.setQuizQuestion(q);
            slides.add(s4);
            LessonSlide s5 = new LessonSlide();
            s5.setId(5);
            s5.setOrderIndex(5);
            s5.setContentType("CODE");
            s5.setContentText("Fonctions avec valeur par défaut :");
            s5.setCodeSnippet("def puissance(base, exp=2):\n    return base ** exp\n\nprint(puissance(3))    # 9\nprint(puissance(3, 3)) # 27");
            s5.setCodeLanguage("Python 3");
            slides.add(s5);
            data.setValue(slides);
            return data;
        }
        RetrofitClient.getApi().getLessonSlides(lessonId).enqueue(new Callback<List<LessonSlide>>() {
            @Override
            public void onResponse(Call<List<LessonSlide>> call, Response<List<LessonSlide>> response) {
                data.setValue(response.isSuccessful() ? response.body() : null);
            }
            @Override
            public void onFailure(Call<List<LessonSlide>> call, Throwable t) { data.setValue(null); }
        });
        return data;
    }
    public LiveData<GamificationResult> completeLesson(long lessonId) {
        MutableLiveData<GamificationResult> data = new MutableLiveData<>();
        if (useMockData) {
            GamificationResult mock = new GamificationResult();
            data.setValue(mock);
            return data;
        }
        RetrofitClient.getApi().completeLesson(lessonId).enqueue(new Callback<GamificationResult>() {
            @Override
            public void onResponse(Call<GamificationResult> call, Response<GamificationResult> response) {
                data.setValue(response.isSuccessful() ? response.body() : null);
            }
            @Override
            public void onFailure(Call<GamificationResult> call, Throwable t) { data.setValue(null); }
        });
        return data;
    }
}
