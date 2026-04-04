package com.codequest.repository;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import com.codequest.model.Challenge;
import com.codequest.model.DailyChallenge;
import com.codequest.model.TestCaseResult;
import com.codequest.model.dto.SubmissionResponse;
import com.codequest.model.dto.SubmitCodeRequest;
import com.codequest.network.RetrofitClient;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ChallengeRepository {
    private final boolean useMockData = false;

    private static final Map<Long, Challenge> CH = new HashMap<>();
    private static final Map<Long, MockResult> MR = new HashMap<>();
    private static final Map<Long, String> SOLUTIONS = new HashMap<>();
    private static final Map<Long, String> HINTS = new HashMap<>();

    private static final Map<Long, long[]> MODULE_CHALLENGES = new HashMap<>();

    static {

        add(1, "Hello World", "Écrivez un programme qui affiche 'Hello, World!'",
                "EASY", "PYTHON", 30, "", "\"hello\"", "Hello, World!", "", "",
                "print('Hello, World!')", "Utilisez la fonction print() avec des guillemets.",
                "Hello, World!", tc("", "Hello, World!", "Hello, World!", true, 5));
        add(2, "Salutations", "Affichez 'Bonjour CodeQuest!'",
                "EASY", "PYTHON", 30, "", "", "Bonjour CodeQuest!", "", "",
                "print('Bonjour CodeQuest!')", "N'oubliez pas les guillemets autour du texte.",
                "Bonjour CodeQuest!", tc("", "Bonjour CodeQuest!", "Bonjour CodeQuest!", true, 4));
        MODULE_CHALLENGES.put(1L, new long[]{1, 2});

        add(3, "Calculette", "Lisez deux entiers et affichez leur somme.",
                "EASY", "PYTHON", 30, "a = int(input())\nb = int(input())", "3\n5", "8", "10\n20", "30",
                "a = int(input())\nb = int(input())\nprint(a + b)", "Convertissez les entrées avec int().",
                "8", tc("3, 5", "8", "8", true, 5), tc("10, 20", "30", "30", true, 4));
        add(4, "Surface du cercle", "Lisez un rayon et affichez la surface (pi=3.14159), arrondie à 2 décimales.",
                "MEDIUM", "PYTHON", 50, "", "5", "78.54", "10", "314.16",
                "r = int(input())\nprint(round(3.14159 * r * r, 2))", "Surface = π × r². Utilisez round(val, 2).",
                "78.54", tc("5", "78.54", "78.54", true, 6), tc("10", "314.16", "314.16", true, 5));
        add(5, "Celsius → Fahrenheit", "Lisez une température en Celsius et affichez F = C × 9/5 + 32.",
                "MEDIUM", "PYTHON", 50, "", "0", "32.0", "100", "212.0",
                "c = int(input())\nprint(c * 9 / 5 + 32)", "Formule: F = C × 9/5 + 32",
                "32.0", tc("0", "32.0", "32.0", true, 4), tc("100", "212.0", "212.0", true, 4));
        MODULE_CHALLENGES.put(2L, new long[]{3, 4, 5});

        add(6, "Pair ou Impair", "Lisez un entier, affichez 'Pair' ou 'Impair'.",
                "EASY", "PYTHON", 30, "", "4", "Pair", "7", "Impair",
                "n = int(input())\nprint('Pair' if n % 2 == 0 else 'Impair')", "Utilisez l'opérateur modulo %.",
                "Pair", tc("4", "Pair", "Pair", true, 4), tc("7", "Impair", "Impair", true, 3));
        add(7, "Table de multiplication", "Lisez N, affichez sa table de 1 à 10: 'N x i = résultat'.",
                "MEDIUM", "PYTHON", 50, "", "3", "3 x 1 = 3\n...", "", "",
                "n = int(input())\nfor i in range(1, 11):\n    print(f'{n} x {i} = {n*i}')",
                "Utilisez une boucle for avec range(1, 11).",
                "3 x 1 = 3\n3 x 2 = 6\n3 x 3 = 9\n3 x 4 = 12\n3 x 5 = 15\n3 x 6 = 18\n3 x 7 = 21\n3 x 8 = 24\n3 x 9 = 27\n3 x 10 = 30",
                tc("3", "3 x 1 = 3 ...", "3 x 1 = 3 ...", true, 8));
        add(8, "FizzBuzz", "Pour 1 à N: 'FizzBuzz' si /15, 'Fizz' si /3, 'Buzz' si /5, sinon le nombre.",
                "HARD", "PYTHON", 80, "", "5", "1\n2\nFizz\n4\nBuzz", "15", "...FizzBuzz",
                "n = int(input())\nfor i in range(1, n+1):\n    if i % 15 == 0: print('FizzBuzz')\n    elif i % 3 == 0: print('Fizz')\n    elif i % 5 == 0: print('Buzz')\n    else: print(i)",
                "Vérifiez d'abord %15, puis %3, puis %5.",
                "1\n2\nFizz\n4\nBuzz", tc("5", "1,2,Fizz,4,Buzz", "1,2,Fizz,4,Buzz", true, 10),
                tc("15", "...FizzBuzz", "...FizzBuzz", true, 12));
        MODULE_CHALLENGES.put(3L, new long[]{6, 7, 8});

        add(9, "Factorielle", "Lisez N, affichez N!.",
                "EASY", "PYTHON", 30, "", "5", "120", "10", "3628800",
                "n = int(input())\nr = 1\nfor i in range(1, n+1): r *= i\nprint(r)",
                "Multipliez tous les nombres de 1 à N.",
                "120", tc("5", "120", "120", true, 5), tc("10", "3628800", "3628800", true, 7));
        add(10, "Fibonacci", "Lisez N, affichez le N-ème nombre de Fibonacci (F(0)=0, F(1)=1).",
                "MEDIUM", "PYTHON", 50, "", "6", "8", "10", "55",
                "n = int(input())\na, b = 0, 1\nfor _ in range(n): a, b = b, a + b\nprint(a)",
                "Utilisez deux variables a et b, échangez à chaque itération.",
                "8", tc("6", "8", "8", true, 5), tc("10", "55", "55", true, 6));
        add(11, "Nombre premier", "Lisez N, affichez 'True' si premier, 'False' sinon.",
                "HARD", "PYTHON", 80, "", "7", "True", "12", "False",
                "n = int(input())\nif n < 2: print(False)\nelse: print(all(n % i != 0 for i in range(2, int(n**0.5)+1)))",
                "Testez les diviseurs de 2 à √n.",
                "True", tc("7", "True", "True", true, 3), tc("12", "False", "False", true, 2),
                tc("2", "True", "True", true, 2));
        MODULE_CHALLENGES.put(4L, new long[]{9, 10, 11});

        add(12, "Inverser une liste", "Lisez N nombres, affichez-les en ordre inverse.",
                "MEDIUM", "PYTHON", 50, "", "3\n1\n2\n3", "3\n2\n1", "4\n10\n20\n30\n40", "40\n30\n20\n10",
                "n = int(input())\nnums = [int(input()) for _ in range(n)]\nfor x in reversed(nums): print(x)",
                "Utilisez reversed() ou le slicing [::-1].",
                "3\n2\n1", tc("3,1,2,3", "3,2,1", "3,2,1", true, 8));
        add(13, "Maximum sans max()", "Lisez N nombres, affichez le plus grand sans utiliser max().",
                "HARD", "PYTHON", 80, "", "4\n3\n7\n2\n5", "7", "3\n-1\n-5\n-2", "-1",
                "n = int(input())\nnums = [int(input()) for _ in range(n)]\nm = nums[0]\nfor x in nums:\n    if x > m: m = x\nprint(m)",
                "Parcourez et gardez le plus grand dans une variable.",
                "7", tc("4,3,7,2,5", "7", "7", true, 6), tc("3,-1,-5,-2", "-1", "-1", true, 5));
        MODULE_CHALLENGES.put(5L, new long[]{12, 13});

        add(14, "Palindrome", "Lisez un mot, affichez 'True' si palindrome, 'False' sinon.",
                "MEDIUM", "PYTHON", 50, "", "kayak", "True", "hello", "False",
                "s = input().strip().lower()\nprint(s == s[::-1])",
                "Comparez la chaîne avec son inverse.",
                "True", tc("kayak", "True", "True", true, 4), tc("hello", "False", "False", true, 3));
        add(15, "Voyelles", "Lisez une chaîne, affichez le nombre de voyelles (a,e,i,o,u).",
                "HARD", "PYTHON", 80, "", "Hello World", "3", "Python Programming", "4",
                "s = input().lower()\nprint(sum(1 for c in s if c in 'aeiou'))",
                "Comptez les caractères dans 'aeiou'.",
                "3", tc("Hello World", "3", "3", true, 5), tc("Python Programming", "4", "4", true, 5));
        add(16, "Anagramme", "Lisez deux mots, affichez 'True' s'ils sont des anagrammes.",
                "HARD", "PYTHON", 80, "", "listen\nsilent", "True", "hello\nworld", "False",
                "a = sorted(input().strip().lower())\nb = sorted(input().strip().lower())\nprint(a == b)",
                "Triez les lettres des deux mots et comparez.",
                "True", tc("listen/silent", "True", "True", true, 6), tc("hello/world", "False", "False", true, 4));
        MODULE_CHALLENGES.put(6L, new long[]{14, 15, 16});

        add(17, "Hello JS", "Affichez 'Hello, JavaScript!'",
                "EASY", "JAVASCRIPT", 30, "", "", "Hello, JavaScript!", "", "",
                "console.log('Hello, JavaScript!');", "Utilisez console.log() pour afficher.",
                "Hello, JavaScript!", tc("", "Hello, JavaScript!", "Hello, JavaScript!", true, 4));
        add(18, "Somme JS", "Lisez 2 nombres, affichez leur somme.",
                "MEDIUM", "JAVASCRIPT", 50, "", "3\n5", "8", "10\n20", "30",
                "// Lecture avec readline\nconst lines = require('fs').readFileSync('/dev/stdin','utf8').split('\\n');\nconsole.log(parseInt(lines[0])+parseInt(lines[1]));",
                "Utilisez parseInt() pour convertir les strings en nombres.",
                "8", tc("3, 5", "8", "8", true, 5), tc("10, 20", "30", "30", true, 4));
        MODULE_CHALLENGES.put(7L, new long[]{17, 18});

        add(19, "Pair ou Impair JS", "Lisez un entier, affichez 'Pair' ou 'Impair'.",
                "EASY", "JAVASCRIPT", 30, "", "4", "Pair", "7", "Impair",
                "const n = parseInt(readline());\nconsole.log(n % 2 === 0 ? 'Pair' : 'Impair');",
                "Utilisez l'opérateur ternaire avec %.",
                "Pair", tc("4", "Pair", "Pair", true, 4), tc("7", "Impair", "Impair", true, 3));
        add(20, "Somme 1 à N JS", "Lisez N, affichez la somme de 1 à N.",
                "MEDIUM", "JAVASCRIPT", 50, "", "5", "15", "100", "5050",
                "const n = parseInt(readline());\nlet s = 0;\nfor (let i = 1; i <= n; i++) s += i;\nconsole.log(s);",
                "Boucle de 1 à N, accumulez dans une variable.",
                "15", tc("5", "15", "15", true, 5), tc("100", "5050", "5050", true, 6));
        MODULE_CHALLENGES.put(8L, new long[]{19, 20});

        add(21, "Factorielle JS", "Lisez N, affichez N!.",
                "EASY", "JAVASCRIPT", 30, "", "5", "120", "10", "3628800",
                "const n = parseInt(readline());\nlet r = 1;\nfor (let i = 1; i <= n; i++) r *= i;\nconsole.log(r);",
                "Multipliez de 1 à N dans une boucle.",
                "120", tc("5", "120", "120", true, 5), tc("10", "3628800", "3628800", true, 7));
        add(22, "FizzBuzz JS", "Pour 1 à N: FizzBuzz/Fizz/Buzz/nombre.",
                "HARD", "JAVASCRIPT", 80, "", "5", "1\n2\nFizz\n4\nBuzz", "15", "...FizzBuzz",
                "const n = parseInt(readline());\nfor (let i = 1; i <= n; i++) {\n  if (i % 15 === 0) console.log('FizzBuzz');\n  else if (i % 3 === 0) console.log('Fizz');\n  else if (i % 5 === 0) console.log('Buzz');\n  else console.log(i);\n}",
                "Testez d'abord %15, puis %3, puis %5.",
                "1\n2\nFizz\n4\nBuzz", tc("5", "1,2,Fizz,4,Buzz", "1,2,Fizz,4,Buzz", true, 10));
        MODULE_CHALLENGES.put(9L, new long[]{21, 22});

        add(23, "Inverse Array JS", "Lisez N nombres, affichez-les en ordre inverse.",
                "MEDIUM", "JAVASCRIPT", 50, "", "3\n1\n2\n3", "3\n2\n1", "4\n10\n20\n30\n40", "40\n30\n20\n10",
                "const lines = require('fs').readFileSync('/dev/stdin','utf8').split('\\n');\nconst n = parseInt(lines[0]);\nfor (let i = n; i >= 1; i--) console.log(lines[i]);",
                "Parcourez le tableau de la fin vers le début.",
                "3\n2\n1", tc("3,1,2,3", "3,2,1", "3,2,1", true, 7));
        add(24, "Somme des pairs JS", "Lisez N nombres, affichez la somme des pairs.",
                "HARD", "JAVASCRIPT", 80, "", "5\n1\n2\n3\n4\n5", "6", "4\n10\n20\n15\n30", "60",
                "const lines = require('fs').readFileSync('/dev/stdin','utf8').split('\\n');\nconst n = parseInt(lines[0]);\nlet s = 0;\nfor (let i = 1; i <= n; i++) { const v = parseInt(lines[i]); if (v % 2 === 0) s += v; }\nconsole.log(s);",
                "Filtrez avec % 2 === 0 avant d'additionner.",
                "6", tc("5,1,2,3,4,5", "6", "6", true, 8), tc("4,10,20,15,30", "60", "60", true, 7));
        MODULE_CHALLENGES.put(10L, new long[]{23, 24});

        add(25, "Fibonacci JS", "Lisez N, affichez le N-ème nombre de Fibonacci.",
                "MEDIUM", "JAVASCRIPT", 50, "", "6", "8", "10", "55",
                "const n = parseInt(readline());\nlet a = 0, b = 1;\nfor (let i = 0; i < n; i++) [a, b] = [b, a + b];\nconsole.log(a);",
                "Déstructuration: [a, b] = [b, a + b].",
                "8", tc("6", "8", "8", true, 5), tc("10", "55", "55", true, 6));
        add(26, "Palindrome JS", "Lisez un mot, affichez 'true' si palindrome.",
                "HARD", "JAVASCRIPT", 80, "", "kayak", "true", "hello", "false",
                "const s = readline().trim().toLowerCase();\nconsole.log(s === s.split('').reverse().join(''));",
                "split('').reverse().join('') pour inverser une chaîne.",
                "true", tc("kayak", "true", "true", true, 4), tc("hello", "false", "false", true, 3));
        MODULE_CHALLENGES.put(11L, new long[]{25, 26});

        add(27, "Doublons JS", "Lisez N nombres, affichez les uniques dans l'ordre d'apparition.",
                "MEDIUM", "JAVASCRIPT", 50, "", "6\n1\n2\n3\n2\n1\n4", "1\n2\n3\n4", "4\n5\n5\n3\n3", "5\n3",
                "const lines = require('fs').readFileSync('/dev/stdin','utf8').split('\\n');\nconst n = parseInt(lines[0]); const seen = new Set();\nfor (let i = 1; i <= n; i++) { const v = parseInt(lines[i]); if (!seen.has(v)) { console.log(v); seen.add(v); } }",
                "Utilisez un Set pour traquer les valeurs déjà vues.",
                "1\n2\n3\n4", tc("6,1,2,3,2,1,4", "1,2,3,4", "1,2,3,4", true, 8));
        add(28, "Tri croissant JS", "Lisez N nombres, affichez-les triés.",
                "HARD", "JAVASCRIPT", 80, "", "5\n3\n1\n4\n1\n5", "1\n1\n3\n4\n5", "3\n9\n2\n7", "2\n7\n9",
                "const lines = require('fs').readFileSync('/dev/stdin','utf8').split('\\n');\nconst n = parseInt(lines[0]);\nconst a = [];\nfor (let i = 1; i <= n; i++) a.push(parseInt(lines[i]));\na.sort((x, y) => x - y);\na.forEach(x => console.log(x));",
                "Attention: sort() trie par string par défaut ! Utilisez (a,b) => a-b.",
                "1\n1\n3\n4\n5", tc("5,3,1,4,1,5", "1,1,3,4,5", "1,1,3,4,5", true, 10));
        MODULE_CHALLENGES.put(12L, new long[]{27, 28});

        add(29, "Hello Java", "Affichez 'Hello, Java!'",
                "EASY", "JAVA", 30, "", "", "Hello, Java!", "", "",
                "public class Main {\n    public static void main(String[] args) {\n        System.out.println(\"Hello, Java!\");\n    }\n}",
                "Utilisez System.out.println() pour afficher.",
                "Hello, Java!", tc("", "Hello, Java!", "Hello, Java!", true, 5));
        add(30, "Somme Java", "Lisez 2 entiers, affichez leur somme.",
                "MEDIUM", "JAVA", 50, "", "3\n5", "8", "10\n20", "30",
                "import java.util.Scanner;\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        System.out.println(sc.nextInt() + sc.nextInt());\n    }\n}",
                "Utilisez Scanner pour lire les entrées.",
                "8", tc("3, 5", "8", "8", true, 5), tc("10, 20", "30", "30", true, 4));
        MODULE_CHALLENGES.put(13L, new long[]{29, 30});

        add(31, "Pair ou Impair Java", "Lisez un entier, affichez 'Pair' ou 'Impair'.",
                "EASY", "JAVA", 30, "", "4", "Pair", "7", "Impair",
                "import java.util.Scanner;\npublic class Main {\n    public static void main(String[] args) {\n        int n = new Scanner(System.in).nextInt();\n        System.out.println(n % 2 == 0 ? \"Pair\" : \"Impair\");\n    }\n}",
                "Opérateur ternaire: condition ? vrai : faux.",
                "Pair", tc("4", "Pair", "Pair", true, 4), tc("7", "Impair", "Impair", true, 3));
        add(32, "Somme 1 à N Java", "Lisez N, affichez la somme de 1 à N.",
                "MEDIUM", "JAVA", 50, "", "5", "15", "100", "5050",
                "import java.util.Scanner;\npublic class Main {\n    public static void main(String[] args) {\n        int n = new Scanner(System.in).nextInt(), s = 0;\n        for (int i = 1; i <= n; i++) s += i;\n        System.out.println(s);\n    }\n}",
                "Boucle for de 1 à N, accumulez.",
                "15", tc("5", "15", "15", true, 5), tc("100", "5050", "5050", true, 6));
        MODULE_CHALLENGES.put(14L, new long[]{31, 32});

        add(33, "Factorielle Java", "Lisez N, affichez N!.",
                "EASY", "JAVA", 30, "", "5", "120", "10", "3628800",
                "import java.util.Scanner;\npublic class Main {\n    public static void main(String[] args) {\n        int n = new Scanner(System.in).nextInt();\n        long r = 1;\n        for (int i = 1; i <= n; i++) r *= i;\n        System.out.println(r);\n    }\n}",
                "Utilisez long pour éviter le dépassement.",
                "120", tc("5", "120", "120", true, 5), tc("10", "3628800", "3628800", true, 7));
        add(34, "Fibonacci Java", "Lisez N, affichez le N-ème Fibonacci.",
                "MEDIUM", "JAVA", 50, "", "6", "8", "10", "55",
                "import java.util.Scanner;\npublic class Main {\n    public static void main(String[] args) {\n        int n = new Scanner(System.in).nextInt();\n        long x = 0, y = 1;\n        for (int i = 0; i < n; i++) { long t = x + y; x = y; y = t; }\n        System.out.println(x);\n    }\n}",
                "Deux variables et échange à chaque itération.",
                "8", tc("6", "8", "8", true, 5), tc("10", "55", "55", true, 6));
        add(35, "FizzBuzz Java", "Pour 1 à N: FizzBuzz/Fizz/Buzz/nombre.",
                "HARD", "JAVA", 80, "", "5", "1\n2\nFizz\n4\nBuzz", "15", "...FizzBuzz",
                "import java.util.Scanner;\npublic class Main {\n    public static void main(String[] args) {\n        int n = new Scanner(System.in).nextInt();\n        for (int i = 1; i <= n; i++) {\n            if (i % 15 == 0) System.out.println(\"FizzBuzz\");\n            else if (i % 3 == 0) System.out.println(\"Fizz\");\n            else if (i % 5 == 0) System.out.println(\"Buzz\");\n            else System.out.println(i);\n        }\n    }\n}",
                "Testez %15 avant %3 et %5.",
                "1\n2\nFizz\n4\nBuzz", tc("5", "1,2,Fizz,4,Buzz", "1,2,Fizz,4,Buzz", true, 10));
        MODULE_CHALLENGES.put(15L, new long[]{33, 34, 35});

        add(36, "Maximum tableau Java", "Lisez N nombres, affichez le plus grand.",
                "MEDIUM", "JAVA", 50, "", "4\n3\n7\n2\n5", "7", "3\n-1\n-5\n-2", "-1",
                "import java.util.Scanner;\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt(), max = Integer.MIN_VALUE;\n        for (int i = 0; i < n; i++) { int v = sc.nextInt(); if (v > max) max = v; }\n        System.out.println(max);\n    }\n}",
                "Initialisez max à Integer.MIN_VALUE.",
                "7", tc("4,3,7,2,5", "7", "7", true, 6), tc("3,-1,-5,-2", "-1", "-1", true, 5));
        add(37, "Nombre premier Java", "Lisez N, affichez 'true' si premier.",
                "HARD", "JAVA", 80, "", "7", "true", "12", "false",
                "import java.util.Scanner;\npublic class Main {\n    public static void main(String[] args) {\n        int n = new Scanner(System.in).nextInt();\n        if (n < 2) { System.out.println(false); return; }\n        for (int i = 2; i * i <= n; i++) { if (n % i == 0) { System.out.println(false); return; } }\n        System.out.println(true);\n    }\n}",
                "Testez les diviseurs jusqu'à √n (i*i <= n).",
                "true", tc("7", "true", "true", true, 3), tc("12", "false", "false", true, 2));
        MODULE_CHALLENGES.put(16L, new long[]{36, 37});

        add(38, "Tri Java", "Lisez N nombres, affichez-les triés.",
                "MEDIUM", "JAVA", 50, "", "5\n3\n1\n4\n1\n5", "1\n1\n3\n4\n5", "3\n9\n2\n7", "2\n7\n9",
                "import java.util.*;\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt(); int[] arr = new int[n];\n        for (int i = 0; i < n; i++) arr[i] = sc.nextInt();\n        Arrays.sort(arr);\n        for (int x : arr) System.out.println(x);\n    }\n}",
                "Utilisez Arrays.sort() pour trier efficacement.",
                "1\n1\n3\n4\n5", tc("5,3,1,4,1,5", "1,1,3,4,5", "1,1,3,4,5", true, 10));
        add(39, "Palindrome Java", "Lisez un mot, affichez 'true' si palindrome.",
                "HARD", "JAVA", 80, "", "kayak", "true", "hello", "false",
                "import java.util.Scanner;\npublic class Main {\n    public static void main(String[] args) {\n        String s = new Scanner(System.in).nextLine().trim().toLowerCase();\n        System.out.println(s.equals(new StringBuilder(s).reverse().toString()));\n    }\n}",
                "StringBuilder.reverse() pour inverser une chaîne en Java.",
                "true", tc("kayak", "true", "true", true, 5), tc("hello", "false", "false", true, 3));
        add(40, "Anagramme Java", "Lisez deux mots, affichez 'true' si anagrammes.",
                "HARD", "JAVA", 80, "", "listen\nsilent", "true", "hello\nworld", "false",
                "import java.util.*;\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        char[] a = sc.nextLine().trim().toLowerCase().toCharArray();\n        char[] b = sc.nextLine().trim().toLowerCase().toCharArray();\n        Arrays.sort(a); Arrays.sort(b);\n        System.out.println(Arrays.equals(a, b));\n    }\n}",
                "Triez les lettres des deux mots et comparez.",
                "true", tc("listen/silent", "true", "true", true, 6), tc("hello/world", "false", "false", true, 4));
        MODULE_CHALLENGES.put(17L, new long[]{38, 39, 40});

        add(41, "Compteur d'occurrences", "Lisez N mots, affichez le plus fréquent.",
                "MEDIUM", "JAVA", 50, "", "5\napple\nbanana\napple\ncherry\napple", "apple", "4\ncat\ndog\ncat\ndog", "cat",
                "import java.util.*;\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = Integer.parseInt(sc.nextLine().trim());\n        Map<String,Integer> m = new HashMap<>();\n        String best = \"\"; int max = 0;\n        for (int i = 0; i < n; i++) {\n            String w = sc.nextLine().trim();\n            m.merge(w, 1, Integer::sum);\n            if (m.get(w) > max) { max = m.get(w); best = w; }\n        }\n        System.out.println(best);\n    }\n}",
                "Utilisez une HashMap pour compter les occurrences.",
                "apple", tc("5,apple,banana,apple,cherry,apple", "apple", "apple", true, 8));
        add(42, "Nombres uniques Java", "Lisez N nombres, affichez les uniques dans l'ordre d'apparition.",
                "HARD", "JAVA", 80, "", "6\n1\n2\n3\n2\n1\n4", "1\n2\n3\n4", "4\n5\n5\n3\n3", "5\n3",
                "import java.util.*;\npublic class Main {\n    public static void main(String[] args) {\n        Scanner sc = new Scanner(System.in);\n        int n = sc.nextInt();\n        Set<Integer> seen = new LinkedHashSet<>();\n        for (int i = 0; i < n; i++) seen.add(sc.nextInt());\n        for (int v : seen) System.out.println(v);\n    }\n}",
                "LinkedHashSet conserve l'ordre d'insertion et élimine les doublons.",
                "1\n2\n3\n4", tc("6,1,2,3,2,1,4", "1,2,3,4", "1,2,3,4", true, 8));
        MODULE_CHALLENGES.put(18L, new long[]{41, 42});
    }

    private static void add(long id, String title, String desc, String diff, String lang, int xp,
                            String starter, String exIn1, String exOut1, String exIn2, String exOut2,
                            String solution, String hint, String runOutput, TestCaseResult... tests) {
        Challenge c = new Challenge(id, title, desc, diff, lang,
                starter.isEmpty() ? getDefaultStarter(lang, title) : starter, xp);
        c.setHint(hint);
        c.setExampleInput(exIn1);
        c.setExampleOutput(exOut1);
        c.setExampleInput2(exIn2);
        c.setExampleOutput2(exOut2);
        CH.put(id, c);
        MR.put(id, new MockResult(runOutput, xp, Arrays.asList(tests)));
        SOLUTIONS.put(id, solution);
        HINTS.put(id, hint);
    }

    private static String getDefaultStarter(String lang, String title) {
        switch (lang) {
            case "PYTHON": return "# " + title + "\n# Écris ta solution ici\n";
            case "JAVASCRIPT": return "// " + title + "\n// Écris ta solution ici\n";
            case "JAVA": return "// " + title + "\n// Écris ta solution ici\n";
            default: return "";
        }
    }

    private static TestCaseResult tc(String input, String expected, String actual, boolean passed, long timeMs) {
        return new TestCaseResult(input, expected, actual, passed, timeMs);
    }

    private static class MockResult {
        final String output; final int xp; final List<TestCaseResult> testResults;
        MockResult(String o, int x, List<TestCaseResult> t) { output = o; xp = x; testResults = t; }
    }

    public LiveData<DailyChallenge> getDailyChallenge() {
        MutableLiveData<DailyChallenge> data = new MutableLiveData<>();
        if (useMockData) {
            data.setValue(new DailyChallenge(6, "Pair ou Impair", "PYTHON", "EASY", 30));
            return data;
        }
        RetrofitClient.getApi().getDailyChallenge().enqueue(new Callback<DailyChallenge>() {
            @Override public void onResponse(Call<DailyChallenge> call, Response<DailyChallenge> r) { data.setValue(r.body()); }
            @Override public void onFailure(Call<DailyChallenge> call, Throwable t) { data.setValue(null); }
        });
        return data;
    }

    public LiveData<Challenge> getChallenge(long id) {
        MutableLiveData<Challenge> data = new MutableLiveData<>();
        if (useMockData) {
            Challenge c = CH.get(id);
            if (c == null) c = CH.get(1L);
            data.setValue(c);
            return data;
        }
        RetrofitClient.getApi().getChallenge(id).enqueue(new Callback<Challenge>() {
            @Override public void onResponse(Call<Challenge> call, Response<Challenge> r) { data.setValue(r.body()); }
            @Override public void onFailure(Call<Challenge> call, Throwable t) { data.setValue(null); }
        });
        return data;
    }

    public LiveData<SubmissionResponse> submitCode(long challengeId, String code, String language) {
        MutableLiveData<SubmissionResponse> data = new MutableLiveData<>();
        if (useMockData) {
            MockResult mr = MR.get(challengeId);
            if (mr == null) mr = MR.get(1L);
            SubmissionResponse mock = new SubmissionResponse();
            mock.setStatus("ACCEPTED");
            mock.setOutput(mr.output);
            mock.setTestCasesPassed(mr.testResults.size());
            mock.setTestCasesTotal(mr.testResults.size());
            mock.setScore(100);
            mock.setGrade("A+");
            mock.setXpGained(mr.xp);
            mock.setBonusXp(25);
            mock.setTestResults(mr.testResults);
            data.setValue(mock);
            return data;
        }
        RetrofitClient.getApi().submitCode(challengeId, new SubmitCodeRequest(code, language))
                .enqueue(new Callback<SubmissionResponse>() {
                    @Override public void onResponse(Call<SubmissionResponse> call, Response<SubmissionResponse> r) { 
                        if (r.isSuccessful() && r.body() != null) {
                            data.setValue(r.body()); 
                        } else {
                            SubmissionResponse err = new SubmissionResponse();
                            err.setStatus("HTTP_ERROR");
                            try {
                                err.setErrorMessage(r.errorBody() != null ? r.errorBody().string() : "Unknown HTTP Error: " + r.code());
                            } catch (Exception e) {
                                err.setErrorMessage("Error reading body");
                            }
                            data.setValue(err);
                        }
                    }
                    @Override public void onFailure(Call<SubmissionResponse> call, Throwable t) { 
                        SubmissionResponse err = new SubmissionResponse();
                        err.setStatus("NETWORK_ERROR");
                        err.setErrorMessage(t.getMessage());
                        data.setValue(err);
                    }
                });
        return data;
    }

    public LiveData<SubmissionResponse> runCode(long challengeId, String code, String language) {
        MutableLiveData<SubmissionResponse> data = new MutableLiveData<>();
        if (useMockData) {
            MockResult mr = MR.get(challengeId);
            if (mr == null) mr = MR.get(1L);
            SubmissionResponse mock = new SubmissionResponse();
            mock.setStatus("SUCCESS");
            mock.setOutput(mr.output + "\n\nExécution réussie en 15ms");
            data.setValue(mock);
            return data;
        }
        RetrofitClient.getApi().runCode(challengeId, new SubmitCodeRequest(code, language))
                .enqueue(new Callback<SubmissionResponse>() {
                    @Override public void onResponse(Call<SubmissionResponse> call, Response<SubmissionResponse> r) { data.setValue(r.body()); }
                    @Override public void onFailure(Call<SubmissionResponse> call, Throwable t) { data.setValue(null); }
                });
        return data;
    }

    public LiveData<String> getHint(long challengeId) {
        MutableLiveData<String> data = new MutableLiveData<>();
        if (useMockData) {
            String hint = HINTS.get(challengeId);
            data.setValue(hint != null ? hint : "Décomposez le problème en étapes simples !");
            return data;
        }
        RetrofitClient.getApi().getHint(challengeId).enqueue(new Callback<Map<String, String>>() {
            @Override public void onResponse(Call<Map<String, String>> call, Response<Map<String, String>> r) {
                if (r.isSuccessful() && r.body() != null) {
                    data.setValue(r.body().get("hint"));
                } else if (r.errorBody() != null) {
                    try {
                        String errorJson = r.errorBody().string();
                        org.json.JSONObject json = new org.json.JSONObject(errorJson);
                        data.setValue(json.optString("message", "Indice non disponible (3 tentatives requises)."));
                    } catch (Exception e) {
                        data.setValue("Indice non disponible (3 tentatives requises).");
                    }
                } else {
                    data.setValue("Erreur inconnue");
                }
            }
            @Override public void onFailure(Call<Map<String, String>> call, Throwable t) { data.setValue("Erreur réseau"); }
        });
        return data;
    }

    public String getSolution(long challengeId) {
        String sol = SOLUTIONS.get(challengeId);
        return sol != null ? sol : "# Solution non disponible.";
    }

    public LiveData<List<Challenge>> getModuleChallenges(long moduleId) {
        MutableLiveData<List<Challenge>> data = new MutableLiveData<>();
        if (useMockData) {
            long[] ids = MODULE_CHALLENGES.get(moduleId);
            if (ids == null) ids = MODULE_CHALLENGES.get(1L);
            java.util.ArrayList<Challenge> list = new java.util.ArrayList<>();
            for (long id : ids) {
                Challenge c = CH.get(id);
                if (c != null) list.add(c);
            }
            data.setValue(list);
            return data;
        }
        RetrofitClient.getApi().getModuleChallenges(moduleId).enqueue(new Callback<List<Challenge>>() {
            @Override public void onResponse(Call<List<Challenge>> call, Response<List<Challenge>> r) { data.setValue(r.body()); }
            @Override public void onFailure(Call<List<Challenge>> call, Throwable t) { data.setValue(null); }
        });
        return data;
    }
}
