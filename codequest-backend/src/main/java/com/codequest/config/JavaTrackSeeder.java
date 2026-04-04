package com.codequest.config;

import com.codequest.entity.*;
import com.codequest.entity.Module;
import com.codequest.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor @Slf4j
public class JavaTrackSeeder {

    private final TrackRepository trackRepository;
    private final SeederHelper h;

    public void seed() {
        seedBeginner();
        seedIntermediate();
        seedAdvanced();
        log.info("Seeded 3 Java tracks (Beginner/Intermediate/Advanced)");
    }

    private void seedBeginner() {
        Track t = trackRepository.save(Track.builder()
                .title("Java — Débutant").titleEn("Java — Beginner")
                .description("Premiers pas avec Java: syntaxe, types, conditions et boucles.")
                .descriptionEn("First steps with Java: syntax, types, conditions and loops.")
                .difficulty(Track.Difficulty.BEGINNER).language(Track.Language.JAVA)
                .requiredLevel(1).xpPerLesson(20).build());

        Module m1 = h.createModule(t, 0, "Introduction à Java", "Introduction to Java",
                "Syntaxe de base et premiers programmes", "Basic syntax and first programs");
        h.seedLesson(m1, 0, "Qu'est-ce que Java ?", "What is Java?", Lesson.LessonType.THEORY,
            "Java est un langage compilé, fortement typé, créé en 1995 par James Gosling. Devise: 'Write Once, Run Anywhere' grâce à la JVM.",
            "Java is a compiled, strongly typed language created in 1995 by James Gosling. Motto: 'Write Once, Run Anywhere' thanks to the JVM.",
            "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, Java!\");\n    }\n}", "java",
            "Chaque programme Java a besoin d'une classe avec main().",
            "Every Java program needs a class with main().");
        h.seedLesson(m1, 1, "Variables et types primitifs", "Variables and primitive types", Lesson.LessonType.THEORY,
            "8 types primitifs: byte, short, int, long, float, double, char, boolean. Les variables doivent être typées.",
            "8 primitive types: byte, short, int, long, float, double, char, boolean. Variables must be typed.",
            "int age = 25;\ndouble pi = 3.14;\nchar lettre = 'A';\nboolean actif = true;\nString nom = \"Alice\";\nSystem.out.println(nom + \" a \" + age + \" ans\");", "java",
            "String est un objet, pas un primitif. Utilisez .equals() pour comparer.",
            "String is an object, not a primitive. Use .equals() to compare.");
        h.seedLesson(m1, 2, "Opérateurs", "Operators", Lesson.LessonType.PRACTICE,
            "+, -, *, /, %. Comparaison: ==, !=, <, >. Logiques: &&, ||, !. Cast: (int), (double).",
            "+, -, *, /, %. Comparison: ==, !=, <, >. Logical: &&, ||, !. Cast: (int), (double).",
            "int a = 10, b = 3;\nSystem.out.println(a / b);         // 3 (division entière!)\nSystem.out.println((double) a / b); // 3.333...\nSystem.out.println(a % b);         // 1", "java",
            "/ entre deux int donne un int. Castez en double pour les décimales.",
            "/ between two ints gives an int. Cast to double for decimals.");
        h.seedLesson(m1, 3, "Scanner: entrées utilisateur", "Scanner: user input", Lesson.LessonType.PRACTICE,
            "Scanner lit les entrées: nextInt(), nextLine(), nextDouble().",
            "Scanner reads input: nextInt(), nextLine(), nextDouble().",
            "import java.util.Scanner;\nScanner sc = new Scanner(System.in);\nint n = sc.nextInt();\nSystem.out.println(n);\nsc.close();", "java",
            "Après nextInt(), appelez sc.nextLine() pour consommer le retour à la ligne.",
            "After nextInt(), call sc.nextLine() to consume the newline.");
        h.seedQuiz(m1, 4, "Quiz: Bases Java", "Quiz: Java Basics", new String[][] {
            {"Qui a créé Java ?", "Who created Java?", "Van Rossum", "Van Rossum", "Eich", "Eich", "Gosling", "Gosling", "Ritchie", "Ritchie", "3", "James Gosling, 1995, Sun Microsystems.", "James Gosling, 1995, Sun Microsystems."},
            {"Point d'entrée d'un programme Java ?", "Entry point of a Java program?", "def main()", "def main()", "func main()", "func main()", "public static void main(String[] args)", "public static void main(String[] args)", "start()", "start()", "3", "main(String[] args) est obligatoire.", "main(String[] args) is required."},
            {"(double) 7 / 2 donne ?", "(double) 7 / 2 gives?", "3", "3", "3.5", "3.5", "3.0", "3.0", "Erreur", "Error", "2", "Le cast convertit 7 en 7.0, donc 7.0/2 = 3.5.", "The cast converts 7 to 7.0, so 7.0/2 = 3.5."},
        });
        h.seedChallenge(m1, "Hello Java", "Hello Java",
            "Affichez 'Hello, Java!'", "Print 'Hello, Java!'",
            "JAVA", "BEGINNER", "", "public class Main{public static void main(String[] a){System.out.println(\"Hello, Java!\");}}", "", "Hello, Java!", "", "", 30);
        h.seedChallenge(m1, "Somme Java", "Sum Java",
            "Lisez 2 entiers, affichez leur somme.", "Read 2 integers, print their sum.",
            "JAVA", "INTERMEDIATE", "", "import java.util.Scanner;public class Main{public static void main(String[] a){Scanner sc=new Scanner(System.in);System.out.println(sc.nextInt()+sc.nextInt());}}", "3\n5", "8", "10\n20", "30", 50);

        Module m2 = h.createModule(t, 1, "Conditions et Boucles", "Conditions and Loops",
                "if/else, for, while en Java", "if/else, for, while in Java");
        h.seedLesson(m2, 0, "if / else / switch", "if / else / switch", Lesson.LessonType.THEORY,
            "if/else avec accolades {}. switch pour de multiples cas.",
            "if/else with curly braces {}. switch for multiple cases.",
            "int note = 15;\nif (note >= 16) {\n    System.out.println(\"TB\");\n} else if (note >= 12) {\n    System.out.println(\"B\");\n} else {\n    System.out.println(\"Insuffisant\");\n}", "java",
            "Toujours utiliser {} même pour une seule instruction.",
            "Always use {} even for a single statement.");
        h.seedLesson(m2, 1, "Boucles for et while", "for and while loops", Lesson.LessonType.PRACTICE,
            "for(init; cond; incr), while(cond), do-while — le do-while s'exécute au moins une fois.",
            "for(init; cond; incr), while(cond), do-while — do-while executes at least once.",
            "for (int i = 0; i < 5; i++) {\n    System.out.println(i);\n}\n\nint j = 10;\nwhile (j > 0) { j -= 3; }", "java",
            "do-while vérifie APRÈS l'exécution.",
            "do-while checks AFTER execution.");
        h.seedLesson(m2, 2, "Tableaux (Arrays)", "Arrays", Lesson.LessonType.THEORY,
            "Taille fixe. Déclaration: type[] nom = new type[taille] ou type[] nom = {valeurs}.",
            "Fixed size. Declaration: type[] name = new type[size] or type[] name = {values}.",
            "int[] nums = {3, 1, 4, 1, 5};\nSystem.out.println(nums.length);\nSystem.out.println(nums[0]);\n\nfor (int n : nums) System.out.print(n + \" \");", "java",
            "Les tableaux Java sont de taille fixe. Utilisez ArrayList pour la taille dynamique.",
            "Java arrays are fixed-size. Use ArrayList for dynamic sizing.");
        h.seedQuiz(m2, 3, "Quiz: Contrôle Java", "Quiz: Java Control", new String[][] {
            {"Quelle boucle s'exécute au moins 1 fois ?", "Which loop executes at least once?", "for", "for", "while", "while", "do-while", "do-while", "for-each", "for-each", "3", "do-while exécute puis vérifie.", "do-while executes then checks."},
            {"nums.length pour {1,2,3} retourne ?", "nums.length for {1,2,3} returns?", "2", "2", "3", "3", "4", "4", "Erreur", "Error", "2", ".length = nombre d'éléments.", ".length = number of elements."},
            {"Les tableaux Java sont de taille:", "Java arrays are:", "Dynamique", "Dynamic", "Fixe", "Fixed", "Variable", "Variable", "Infinie", "Infinite", "2", "Fixée à la création.", "Fixed at creation."},
        });
        h.seedChallenge(m2, "Pair ou Impair Java", "Even or Odd Java",
            "Lisez un entier, affichez 'Pair' ou 'Impair'.", "Read an integer, print 'Pair' or 'Impair'.",
            "JAVA", "BEGINNER", "", "import java.util.Scanner;public class Main{public static void main(String[] a){Scanner sc=new Scanner(System.in);int n=sc.nextInt();System.out.println(n%2==0?\"Pair\":\"Impair\");}}", "4", "Pair", "7", "Impair", 30);
        h.seedChallenge(m2, "Somme 1 à N Java", "Sum 1 to N Java",
            "Lisez N, affichez la somme de 1 à N.", "Read N, print the sum from 1 to N.",
            "JAVA", "INTERMEDIATE", "", "import java.util.Scanner;public class Main{public static void main(String[] a){Scanner sc=new Scanner(System.in);int n=sc.nextInt(),s=0;for(int i=1;i<=n;i++)s+=i;System.out.println(s);}}", "5", "15", "100", "5050", 50);
    }

    private void seedIntermediate() {
        Track t = trackRepository.save(Track.builder()
                .title("Java — Intermédiaire").titleEn("Java — Intermediate")
                .description("Programmation Orientée Objet: classes, héritage, interfaces et encapsulation.")
                .descriptionEn("Object-Oriented Programming: classes, inheritance, interfaces and encapsulation.")
                .difficulty(Track.Difficulty.INTERMEDIATE).language(Track.Language.JAVA)
                .requiredLevel(1).xpPerLesson(35).build());

        Module m1 = h.createModule(t, 0, "POO: Classes et Objets", "OOP: Classes and Objects",
                "Fondamentaux de la programmation orientée objet", "Fundamentals of object-oriented programming");
        h.seedLesson(m1, 0, "Classes et Objets", "Classes and Objects", Lesson.LessonType.THEORY,
            "Classe = modèle, objet = instance. Attributs = données, méthodes = comportements.",
            "Class = template, object = instance. Attributes = data, methods = behaviors.",
            "class Voiture {\n    String marque;\n    int vitesse;\n    void accelerer(int v) { vitesse += v; }\n}\n\nVoiture v = new Voiture();\nv.marque = \"Tesla\";\nv.accelerer(50);", "java",
            "new crée une instance. Le point (.) accède aux membres.",
            "new creates an instance. The dot (.) accesses members.");
        h.seedLesson(m1, 1, "Constructeurs et encapsulation", "Constructors and encapsulation", Lesson.LessonType.THEORY,
            "Le constructeur initialise. private + getters/setters = encapsulation.",
            "The constructor initializes. private + getters/setters = encapsulation.",
            "class Etudiant {\n    private String nom;\n    private int age;\n    public Etudiant(String nom, int age) {\n        this.nom = nom;\n        this.age = age;\n    }\n    public String getNom() { return nom; }\n}", "java",
            "private = caché, public = accessible. C'est l'encapsulation.",
            "private = hidden, public = accessible. That's encapsulation.");
        h.seedLesson(m1, 2, "Héritage et polymorphisme", "Inheritance and polymorphism", Lesson.LessonType.THEORY,
            "extends hérite. @Override redéfinit. super() appelle le parent. Le polymorphisme permet la flexibilité.",
            "extends inherits. @Override redefines. super() calls the parent. Polymorphism allows flexibility.",
            "class Animal {\n    void parler() { System.out.println(\"...\"); }\n}\nclass Chien extends Animal {\n    @Override\n    void parler() { System.out.println(\"Woof!\"); }\n}\nAnimal a = new Chien();\na.parler(); // Woof!", "java",
            "Le polymorphisme: un type parent référence un objet enfant.",
            "Polymorphism: a parent type references a child object.");
        h.seedLesson(m1, 3, "Interfaces", "Interfaces", Lesson.LessonType.THEORY,
            "Interface = contrat. abstract class = classe incomplète. implements pour les interfaces.",
            "Interface = contract. abstract class = incomplete class. implements for interfaces.",
            "interface Volant {\n    void voler();\n}\nclass Drone implements Volant {\n    public void voler() {\n        System.out.println(\"Bzzz\");\n    }\n}", "java",
            "Une classe peut implémenter plusieurs interfaces.",
            "A class can implement multiple interfaces.");
        h.seedQuiz(m1, 4, "Quiz: POO", "Quiz: OOP", new String[][] {
            {"extends fait quoi ?", "What does extends do?", "Allonge un tableau", "Extends an array", "Permet l'héritage", "Enables inheritance", "Exporte", "Exports", "Crée interface", "Creates interface", "2", "extends = héritage entre classes.", "extends = inheritance between classes."},
            {"L'encapsulation c'est:", "Encapsulation is:", "Hériter", "Inheriting", "Cacher les données", "Hiding data", "Créer des objets", "Creating objects", "Utiliser des interfaces", "Using interfaces", "2", "private + getters/setters.", "private + getters/setters."},
            {"Combien d'interfaces implémentables ?", "How many interfaces can be implemented?", "0", "0", "1", "1", "2", "2", "Autant qu'on veut", "As many as you want", "4", "implements Interface1, Interface2, ...", "implements Interface1, Interface2, ..."},
        });
        h.seedChallenge(m1, "Factorielle Java", "Factorial Java",
            "Lisez N, affichez N!.", "Read N, print N!.",
            "JAVA", "BEGINNER", "", "import java.util.Scanner;public class Main{public static void main(String[] a){Scanner sc=new Scanner(System.in);int n=sc.nextInt();long r=1;for(int i=1;i<=n;i++)r*=i;System.out.println(r);}}", "5", "120", "10", "3628800", 30);
        h.seedChallenge(m1, "Fibonacci Java", "Fibonacci Java",
            "Lisez N, affichez le N-ème Fibonacci.", "Read N, print the N-th Fibonacci.",
            "JAVA", "INTERMEDIATE", "", "import java.util.Scanner;public class Main{public static void main(String[] a){Scanner sc=new Scanner(System.in);int n=sc.nextInt();long x=0,y=1;for(int i=0;i<n;i++){long t=x+y;x=y;y=t;}System.out.println(x);}}", "6", "8", "10", "55", 50);
        h.seedChallenge(m1, "FizzBuzz Java", "FizzBuzz Java",
            "Pour 1 à N: FizzBuzz/Fizz/Buzz/nombre.", "For 1 to N: FizzBuzz/Fizz/Buzz/number.",
            "JAVA", "ADVANCED", "", "import java.util.Scanner;public class Main{public static void main(String[] a){Scanner sc=new Scanner(System.in);int n=sc.nextInt();for(int i=1;i<=n;i++){if(i%15==0)System.out.println(\"FizzBuzz\");else if(i%3==0)System.out.println(\"Fizz\");else if(i%5==0)System.out.println(\"Buzz\");else System.out.println(i);}}}", "5", "1\n2\nFizz\n4\nBuzz", "15", "1\n2\nFizz\n4\nBuzz\nFizz\n7\n8\nFizz\nBuzz\n11\nFizz\n13\n14\nFizzBuzz", 80);

        Module m2 = h.createModule(t, 1, "Collections et Exceptions", "Collections and Exceptions",
                "ArrayList, HashMap et gestion d'erreurs", "ArrayList, HashMap and error handling");
        h.seedLesson(m2, 0, "ArrayList", "ArrayList", Lesson.LessonType.THEORY,
            "ArrayList = tableau dynamique. Utilise les generics <Type>.",
            "ArrayList = dynamic array. Uses generics <Type>.",
            "import java.util.ArrayList;\nArrayList<String> noms = new ArrayList<>();\nnoms.add(\"Alice\");\nnoms.add(\"Bob\");\nSystem.out.println(noms.size()); // 2", "java",
            "ArrayList<Integer> pour les int (wrapper).",
            "ArrayList<Integer> for ints (wrapper).");
        h.seedLesson(m2, 1, "HashMap", "HashMap", Lesson.LessonType.THEORY,
            "HashMap = paires clé-valeur. Accès O(1) par clé.",
            "HashMap = key-value pairs. O(1) access by key.",
            "import java.util.HashMap;\nHashMap<String,Integer> scores = new HashMap<>();\nscores.put(\"Alice\", 95);\nSystem.out.println(scores.get(\"Alice\")); // 95", "java",
            "getOrDefault(clé, défaut) évite les NullPointerException.",
            "getOrDefault(key, default) avoids NullPointerException.");
        h.seedLesson(m2, 2, "try / catch / finally", "try / catch / finally", Lesson.LessonType.PRACTICE,
            "try = code risqué, catch = gestion d'erreur, finally = toujours exécuté.",
            "try = risky code, catch = error handling, finally = always executed.",
            "try {\n    int r = 10 / 0;\n} catch (ArithmeticException e) {\n    System.out.println(\"Erreur: \" + e.getMessage());\n} finally {\n    System.out.println(\"Toujours exécuté\");\n}", "java",
            "Checked exceptions doivent être gérées. RuntimeException sont optionnelles.",
            "Checked exceptions must be handled. RuntimeException are optional.");
        h.seedQuiz(m2, 3, "Quiz: Collections", "Quiz: Collections", new String[][] {
            {"Collection à taille dynamique ?", "Dynamic-size collection?", "int[]", "int[]", "String[]", "String[]", "ArrayList", "ArrayList", "Array", "Array", "3", "ArrayList grandit automatiquement.", "ArrayList grows automatically."},
            {"Quel bloc s'exécute toujours ?", "Which block always executes?", "try", "try", "catch", "catch", "finally", "finally", "throw", "throw", "3", "finally s'exécute toujours.", "finally always executes."},
            {"HashMap.get(clé_inexistante) retourne ?", "HashMap.get(missing_key) returns?", "0", "0", "\"\"", "\"\"", "null", "null", "Exception", "Exception", "3", "Retourne null. Utilisez getOrDefault().", "Returns null. Use getOrDefault()."},
        });
        h.seedChallenge(m2, "Maximum tableau Java", "Max array Java",
            "Lisez N nombres, affichez le plus grand.", "Read N numbers, print the largest.",
            "JAVA", "INTERMEDIATE", "", "import java.util.Scanner;public class Main{public static void main(String[] a){Scanner sc=new Scanner(System.in);int n=sc.nextInt(),max=Integer.MIN_VALUE;for(int i=0;i<n;i++){int v=sc.nextInt();if(v>max)max=v;}System.out.println(max);}}", "4\n3\n7\n2\n5", "7", "3\n-1\n-5\n-2", "-1", 50);
        h.seedChallenge(m2, "Nombre premier Java", "Prime number Java",
            "Lisez N, affichez 'true' si premier.", "Read N, print 'true' if prime.",
            "JAVA", "ADVANCED", "", "import java.util.Scanner;public class Main{public static void main(String[] a){Scanner sc=new Scanner(System.in);int n=sc.nextInt();if(n<2){System.out.println(false);return;}for(int i=2;i*i<=n;i++){if(n%i==0){System.out.println(false);return;}}System.out.println(true);}}", "7", "true", "12", "false", 80);
    }

    private void seedAdvanced() {
        Track t = trackRepository.save(Track.builder()
                .title("Java — Avancé").titleEn("Java — Advanced")
                .description("Algorithmes, Streams, et résolution de problèmes avancés en Java.")
                .descriptionEn("Algorithms, Streams, and advanced problem solving in Java.")
                .difficulty(Track.Difficulty.ADVANCED).language(Track.Language.JAVA)
                .requiredLevel(1).xpPerLesson(50).build());

        Module m1 = h.createModule(t, 0, "Algorithmes classiques", "Classic algorithms",
                "Tri, recherche et récursivité", "Sorting, searching and recursion");
        h.seedLesson(m1, 0, "Tri par sélection", "Selection sort", Lesson.LessonType.THEORY,
            "Trouve le min, le place au début, recommence. O(n²).",
            "Find the min, place it at the beginning, repeat. O(n²).",
            "int[] arr = {64, 25, 12, 22, 11};\nfor (int i = 0; i < arr.length - 1; i++) {\n    int minIdx = i;\n    for (int j = i + 1; j < arr.length; j++)\n        if (arr[j] < arr[minIdx]) minIdx = j;\n    int tmp = arr[minIdx]; arr[minIdx] = arr[i]; arr[i] = tmp;\n}", "java",
            "O(n²) — inefficace. Utilisez Arrays.sort() en pratique.",
            "O(n²) — inefficient. Use Arrays.sort() in practice.");
        h.seedLesson(m1, 1, "Recherche binaire", "Binary search", Lesson.LessonType.THEORY,
            "Cherche dans un tableau TRIÉ en divisant en deux. O(log n).",
            "Searches in a SORTED array by dividing in half. O(log n).",
            "int[] arr = {2, 5, 8, 12, 16, 23, 38};\nint target = 23, lo = 0, hi = arr.length - 1;\nwhile (lo <= hi) {\n    int mid = (lo + hi) / 2;\n    if (arr[mid] == target) { System.out.println(mid); break; }\n    else if (arr[mid] < target) lo = mid + 1;\n    else hi = mid - 1;\n}", "java",
            "Prérequis: le tableau DOIT être trié.",
            "Prerequisite: the array MUST be sorted.");
        h.seedLesson(m1, 2, "Récursivité", "Recursion", Lesson.LessonType.PRACTICE,
            "Fonction qui s'appelle elle-même. Toujours un cas de base.",
            "Function that calls itself. Always has a base case.",
            "static int fib(int n) {\n    if (n <= 1) return n;\n    return fib(n-1) + fib(n-2);\n}\n// Version itérative O(n) recommandée", "java",
            "Récursion naive de Fibonacci = O(2^n). Itératif = O(n).",
            "Naive Fibonacci recursion = O(2^n). Iterative = O(n).");
        h.seedLesson(m1, 3, "Streams Java 8+", "Java 8+ Streams", Lesson.LessonType.PRACTICE,
            "Les Streams permettent la programmation fonctionnelle: filter, map, reduce, collect.",
            "Streams enable functional programming: filter, map, reduce, collect.",
            "import java.util.*;\nimport java.util.stream.*;\n\nList<Integer> nums = List.of(1,2,3,4,5);\nint sum = nums.stream()\n    .filter(n -> n % 2 == 0)\n    .mapToInt(n -> n)\n    .sum();\nSystem.out.println(sum); // 6", "java",
            "Les Streams ne modifient pas la collection d'origine.",
            "Streams don't modify the original collection.");
        h.seedQuiz(m1, 4, "Quiz: Algorithmes", "Quiz: Algorithms", new String[][] {
            {"Complexité du tri par sélection ?", "Selection sort complexity?", "O(n)", "O(n)", "O(n log n)", "O(n log n)", "O(n²)", "O(n²)", "O(1)", "O(1)", "3", "Double boucle = O(n²).", "Double loop = O(n²)."},
            {"La recherche binaire nécessite:", "Binary search requires:", "Tableau vide", "Empty array", "Tableau trié", "Sorted array", "ArrayList", "ArrayList", "HashMap", "HashMap", "2", "Ne fonctionne que sur un tableau trié.", "Only works on a sorted array."},
            {"Complexité de la recherche binaire ?", "Binary search complexity?", "O(n)", "O(n)", "O(n²)", "O(n²)", "O(log n)", "O(log n)", "O(1)", "O(1)", "3", "On divise par 2 à chaque étape.", "We divide by 2 at each step."},
        });
        h.seedChallenge(m1, "Tri Java", "Sort Java",
            "Lisez N nombres, affichez-les triés.", "Read N numbers, print them sorted.",
            "JAVA", "INTERMEDIATE", "", "import java.util.*;public class Main{public static void main(String[] a){Scanner sc=new Scanner(System.in);int n=sc.nextInt();int[] arr=new int[n];for(int i=0;i<n;i++)arr[i]=sc.nextInt();Arrays.sort(arr);for(int x:arr)System.out.println(x);}}", "5\n3\n1\n4\n1\n5", "1\n1\n3\n4\n5", "3\n9\n2\n7", "2\n7\n9", 50);
        h.seedChallenge(m1, "Palindrome Java", "Palindrome Java",
            "Lisez un mot, affichez 'true' si palindrome.", "Read a word, print 'true' if palindrome.",
            "JAVA", "ADVANCED", "", "import java.util.Scanner;public class Main{public static void main(String[] a){Scanner sc=new Scanner(System.in);String s=sc.nextLine().trim().toLowerCase();System.out.println(s.equals(new StringBuilder(s).reverse().toString()));}}", "kayak", "true", "hello", "false", 80);
        h.seedChallenge(m1, "Anagramme Java", "Anagram Java",
            "Lisez deux mots, affichez 'true' si anagrammes.", "Read two words, print 'true' if anagrams.",
            "JAVA", "ADVANCED", "", "import java.util.*;public class Main{public static void main(String[] a){Scanner sc=new Scanner(System.in);char[] a1=sc.nextLine().trim().toLowerCase().toCharArray();char[] a2=sc.nextLine().trim().toLowerCase().toCharArray();Arrays.sort(a1);Arrays.sort(a2);System.out.println(Arrays.equals(a1,a2));}}", "listen\nsilent", "true", "hello\nworld", "false", 80);

        Module m2 = h.createModule(t, 1, "Patterns et Design avancé", "Patterns and Advanced Design",
                "Patterns de conception et code propre", "Design patterns and clean code");
        h.seedLesson(m2, 0, "Generics", "Generics", Lesson.LessonType.THEORY,
            "Les generics permettent d'écrire du code réutilisable pour différents types. <T> est un paramètre de type.",
            "Generics allow writing reusable code for different types. <T> is a type parameter.",
            "class Boite<T> {\n    private T contenu;\n    public Boite(T c) { this.contenu = c; }\n    public T getContenu() { return contenu; }\n}\n\nBoite<String> b = new Boite<>(\"Hello\");\nSystem.out.println(b.getContenu());", "java",
            "Les generics détectent les erreurs de type à la compilation.",
            "Generics detect type errors at compile time.");
        h.seedLesson(m2, 1, "Pattern Singleton", "Singleton Pattern", Lesson.LessonType.THEORY,
            "Le Singleton garantit qu'une classe n'a qu'une seule instance. Utile pour les configs, caches, connexions.",
            "The Singleton ensures a class has only one instance. Useful for configs, caches, connections.",
            "class Config {\n    private static Config instance;\n    private Config() {} // constructeur privé\n    public static Config getInstance() {\n        if (instance == null) instance = new Config();\n        return instance;\n    }\n}", "java",
            "Le constructeur est private — on ne peut pas utiliser new.",
            "The constructor is private — you can't use new.");
        h.seedLesson(m2, 2, "Pattern Builder", "Builder Pattern", Lesson.LessonType.PRACTICE,
            "Le Builder construit des objets complexes étape par étape. Lisible et flexible.",
            "The Builder constructs complex objects step by step. Readable and flexible.",
            "class User {\n    String nom; int age;\n    static class Builder {\n        String nom; int age;\n        Builder nom(String n) { this.nom = n; return this; }\n        Builder age(int a) { this.age = a; return this; }\n        User build() { User u = new User(); u.nom = nom; u.age = age; return u; }\n    }\n}", "java",
            "Le Builder évite les constructeurs avec trop de paramètres.",
            "The Builder avoids constructors with too many parameters.");
        h.seedQuiz(m2, 3, "Quiz: Design Patterns", "Quiz: Design Patterns", new String[][] {
            {"Le Singleton garantit:", "The Singleton guarantees:", "Héritage unique", "Single inheritance", "Une seule instance", "A single instance", "Immutabilité", "Immutability", "Thread safety", "Thread safety", "2", "Une seule instance de la classe existe.", "Only one instance of the class exists."},
            {"Les generics servent à:", "Generics are used for:", "Accélérer le code", "Speeding up code", "Écrire du code réutilisable typé", "Writing typed reusable code", "Créer des threads", "Creating threads", "Gérer les exceptions", "Handling exceptions", "2", "Code réutilisable avec vérification de type à la compilation.", "Reusable code with compile-time type checking."},
        });
        h.seedChallenge(m2, "Compteur d'occurrences", "Occurrence counter",
            "Lisez N mots, affichez le plus fréquent.", "Read N words, print the most frequent.",
            "JAVA", "INTERMEDIATE", "", "import java.util.*;public class Main{public static void main(String[] a){Scanner sc=new Scanner(System.in);int n=Integer.parseInt(sc.nextLine().trim());Map<String,Integer> m=new HashMap<>();String best=\"\";int max=0;for(int i=0;i<n;i++){String w=sc.nextLine().trim();m.merge(w,1,Integer::sum);if(m.get(w)>max){max=m.get(w);best=w;}}System.out.println(best);}}", "5\napple\nbanana\napple\ncherry\napple", "apple", "4\ncat\ndog\ncat\ndog", "cat", 50);
        h.seedChallenge(m2, "Nombres uniques Java", "Unique numbers Java",
            "Lisez N nombres, affichez les uniques dans l'ordre d'apparition.", "Read N numbers, print unique ones in order of appearance.",
            "JAVA", "ADVANCED", "", "import java.util.*;public class Main{public static void main(String[] a){Scanner sc=new Scanner(System.in);int n=sc.nextInt();Set<Integer> seen=new LinkedHashSet<>();for(int i=0;i<n;i++)seen.add(sc.nextInt());for(int v:seen)System.out.println(v);}}", "6\n1\n2\n3\n2\n1\n4", "1\n2\n3\n4", "4\n5\n5\n3\n3", "5\n3", 80);
    }
}
