package com.codequest.repository;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.Module;
import com.codequest.model.Track;
import com.codequest.network.RetrofitClient;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class TrackRepository {
    private final boolean useMockData = false;

    private static final Map<Long, Track> MOCK_TRACKS = new HashMap<>();
    private static final Map<Long, List<Module>> MOCK_MODULES = new HashMap<>();
    private static final List<Track> TRACK_LIST = new ArrayList<>();

    static {

        Track pyBeg = new Track(1, "Python — Débutant",
                "Premiers pas en programmation avec Python. Apprenez les bases: afficher, stocker et manipuler des données.",
                "BEGINNER", "PYTHON", 2, 8, 5, 0, false);
        pyBeg.setXpPerLesson(20); pyBeg.setRequiredLevel(1);
        MOCK_TRACKS.put(1L, pyBeg); TRACK_LIST.add(pyBeg);
        MOCK_MODULES.put(1L, Arrays.asList(
                new Module(1, 1, "Introduction à Python", "Découvrez Python et écrivez vos premières lignes", 1, 4),
                new Module(2, 1, "Variables et Types", "Stocker et manipuler des données", 2, 5)
        ));

        Track pyInt = new Track(2, "Python — Intermédiaire",
                "Maîtrisez les structures de contrôle, les fonctions et les collections en Python.",
                "INTERMEDIATE", "PYTHON", 2, 10, 6, 0, false);
        pyInt.setXpPerLesson(35); pyInt.setRequiredLevel(1);
        MOCK_TRACKS.put(2L, pyInt); TRACK_LIST.add(pyInt);
        MOCK_MODULES.put(2L, Arrays.asList(
                new Module(3, 2, "Structures de contrôle", "if/else, boucles for et while", 1, 5),
                new Module(4, 2, "Les Fonctions", "Créez des fonctions réutilisables", 2, 5)
        ));

        Track pyAdv = new Track(3, "Python — Avancé",
                "Collections, manipulation de texte et algorithmes avancés en Python.",
                "ADVANCED", "PYTHON", 2, 8, 5, 0, false);
        pyAdv.setXpPerLesson(50); pyAdv.setRequiredLevel(1);
        MOCK_TRACKS.put(3L, pyAdv); TRACK_LIST.add(pyAdv);
        MOCK_MODULES.put(3L, Arrays.asList(
                new Module(5, 3, "Listes et Dictionnaires", "Maîtrisez les collections de données", 1, 5),
                new Module(6, 3, "Chaînes de caractères", "Manipulez le texte comme un pro", 2, 4)
        ));

        Track jsBeg = new Track(4, "JavaScript — Débutant",
                "Premiers pas avec JavaScript: variables, types et opérateurs de base.",
                "BEGINNER", "JAVASCRIPT", 2, 10, 4, 0, false);
        jsBeg.setXpPerLesson(20); jsBeg.setRequiredLevel(1);
        MOCK_TRACKS.put(4L, jsBeg); TRACK_LIST.add(jsBeg);
        MOCK_MODULES.put(4L, Arrays.asList(
                new Module(7, 4, "Introduction à JavaScript", "Découvrez le langage du web", 1, 5),
                new Module(8, 4, "Conditions et Boucles", "Structures de contrôle en JS", 2, 4)
        ));

        Track jsInt = new Track(5, "JavaScript — Intermédiaire",
                "Fonctions, closures, tableaux et objets en JavaScript.",
                "INTERMEDIATE", "JAVASCRIPT", 2, 8, 4, 0, false);
        jsInt.setXpPerLesson(35); jsInt.setRequiredLevel(1);
        MOCK_TRACKS.put(5L, jsInt); TRACK_LIST.add(jsInt);
        MOCK_MODULES.put(5L, Arrays.asList(
                new Module(9, 5, "Fonctions", "Fonctions classiques, arrow et closures", 1, 4),
                new Module(10, 5, "Tableaux et Objets", "Collections de données en JS", 2, 4)
        ));

        Track jsAdv = new Track(6, "JavaScript — Avancé",
                "Asynchrone, ES6+ avancé, classes et patterns modernes.",
                "ADVANCED", "JAVASCRIPT", 2, 8, 4, 0, false);
        jsAdv.setXpPerLesson(50); jsAdv.setRequiredLevel(1);
        MOCK_TRACKS.put(6L, jsAdv); TRACK_LIST.add(jsAdv);
        MOCK_MODULES.put(6L, Arrays.asList(
                new Module(11, 6, "Asynchrone", "Promises et async/await", 1, 4),
                new Module(12, 6, "ES6+ Moderne", "Classes, Map/Set et features modernes", 2, 4)
        ));

        Track javBeg = new Track(7, "Java — Débutant",
                "Premiers pas avec Java: syntaxe, types, conditions et boucles.",
                "BEGINNER", "JAVA", 2, 8, 4, 0, false);
        javBeg.setXpPerLesson(20); javBeg.setRequiredLevel(1);
        MOCK_TRACKS.put(7L, javBeg); TRACK_LIST.add(javBeg);
        MOCK_MODULES.put(7L, Arrays.asList(
                new Module(13, 7, "Introduction à Java", "Syntaxe de base et premiers programmes", 1, 5),
                new Module(14, 7, "Conditions et Boucles", "if/else, for, while en Java", 2, 4)
        ));

        Track javInt = new Track(8, "Java — Intermédiaire",
                "Programmation Orientée Objet: classes, héritage, interfaces et encapsulation.",
                "INTERMEDIATE", "JAVA", 2, 10, 5, 0, false);
        javInt.setXpPerLesson(35); javInt.setRequiredLevel(1);
        MOCK_TRACKS.put(8L, javInt); TRACK_LIST.add(javInt);
        MOCK_MODULES.put(8L, Arrays.asList(
                new Module(15, 8, "POO: Classes et Objets", "Fondamentaux de la programmation orientée objet", 1, 5),
                new Module(16, 8, "Collections et Exceptions", "ArrayList, HashMap et gestion d'erreurs", 2, 4)
        ));

        Track javAdv = new Track(9, "Java — Avancé",
                "Algorithmes, Streams, et résolution de problèmes avancés en Java.",
                "ADVANCED", "JAVA", 2, 8, 5, 0, true);
        javAdv.setXpPerLesson(50); javAdv.setRequiredLevel(5);
        MOCK_TRACKS.put(9L, javAdv); TRACK_LIST.add(javAdv);
        MOCK_MODULES.put(9L, Arrays.asList(
                new Module(17, 9, "Algorithmes classiques", "Tri, recherche et récursivité", 1, 5),
                new Module(18, 9, "Patterns et Design avancé", "Patterns de conception et code propre", 2, 4)
        ));
    }

    public LiveData<List<Track>> getTracks() {
        MutableLiveData<List<Track>> data = new MutableLiveData<>();
        if (useMockData) {
            data.setValue(new ArrayList<>(TRACK_LIST));
            return data;
        }
        RetrofitClient.getApi().getTracks().enqueue(new Callback<List<Track>>() {
            @Override public void onResponse(Call<List<Track>> call, Response<List<Track>> response) {
                data.setValue(response.isSuccessful() ? response.body() : null);
            }
            @Override public void onFailure(Call<List<Track>> call, Throwable t) { data.setValue(null); }
        });
        return data;
    }

    public LiveData<Track> getTrack(long trackId) {
        MutableLiveData<Track> data = new MutableLiveData<>();
        if (useMockData) {
            Track t = MOCK_TRACKS.get(trackId);
            if (t == null) t = MOCK_TRACKS.get(1L);
            data.setValue(t);
            return data;
        }
        RetrofitClient.getApi().getTrack(trackId).enqueue(new Callback<Track>() {
            @Override public void onResponse(Call<Track> call, Response<Track> response) {
                data.setValue(response.isSuccessful() ? response.body() : null);
            }
            @Override public void onFailure(Call<Track> call, Throwable t) { data.setValue(null); }
        });
        return data;
    }

    public LiveData<List<Module>> getModules(long trackId) {
        MutableLiveData<List<Module>> data = new MutableLiveData<>();
        if (useMockData) {
            List<Module> modules = MOCK_MODULES.get(trackId);
            if (modules == null) modules = MOCK_MODULES.get(1L);
            data.setValue(modules);
            return data;
        }
        RetrofitClient.getApi().getModules(trackId).enqueue(new Callback<List<Module>>() {
            @Override public void onResponse(Call<List<Module>> call, Response<List<Module>> response) {
                data.setValue(response.isSuccessful() ? response.body() : null);
            }
            @Override public void onFailure(Call<List<Module>> call, Throwable t) { data.setValue(null); }
        });
        return data;
    }
}
