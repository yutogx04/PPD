package com.codequest.config;

import com.codequest.entity.*;
import com.codequest.entity.Module;
import com.codequest.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor @Slf4j
public class PythonTrackSeeder {

    private final TrackRepository trackRepository;
    private final SeederHelper h;

    public void seed() {
        seedBeginner();
        seedIntermediate();
        seedAdvanced();
        log.info("Seeded 3 Python tracks (Beginner/Intermediate/Advanced)");
    }

    private void seedBeginner() {
        Track t = trackRepository.save(Track.builder()
                .title("Python — Débutant").titleEn("Python — Beginner")
                .description("Premiers pas en programmation avec Python. Apprenez les bases: afficher, stocker et manipuler des données.")
                .descriptionEn("First steps in programming with Python. Learn the basics: display, store and manipulate data.")
                .difficulty(Track.Difficulty.BEGINNER).language(Track.Language.PYTHON)
                .requiredLevel(1).xpPerLesson(20).build());

        Module m1 = h.createModule(t, 0, "Introduction à Python", "Introduction to Python",
                "Découvrez Python et écrivez vos premières lignes", "Discover Python and write your first lines");
        h.seedLesson(m1, 0, "Qu'est-ce que Python ?", "What is Python?", Lesson.LessonType.THEORY,
            "Python est un langage créé par Guido van Rossum en 1991. Il est célèbre pour sa syntaxe claire et sa polyvalence (web, IA, data science).",
            "Python is a language created by Guido van Rossum in 1991. It is famous for its clear syntax and versatility (web, AI, data science).",
            "print(\"Hello, World!\")", "python",
            "La fonction print() affiche du texte dans la console.",
            "The print() function displays text in the console.");
        h.seedLesson(m1, 1, "Premier script", "First script", Lesson.LessonType.THEORY,
            "Créez un fichier .py et exécutez-le avec python dans votre terminal. Chaque ligne est exécutée dans l'ordre.",
            "Create a .py file and run it with python in your terminal. Each line is executed in order.",
            "# Mon premier script\nnom = \"CodeQuest\"\nprint(\"Bienvenue sur\", nom, \"!\")", "python",
            "Les fichiers Python ont l'extension .py.",
            "Python files have the .py extension.");
        h.seedLesson(m1, 2, "Les commentaires", "Comments", Lesson.LessonType.PRACTICE,
            "Les commentaires sont ignorés par Python. # pour une ligne, \"\"\" pour plusieurs.",
            "Comments are ignored by Python. # for a single line, \"\"\" for multiple lines.",
            "# Commentaire simple\n\"\"\"Commentaire\nmulti-lignes\"\"\"\nprint(\"Code exécuté\")", "python",
            "Les commentaires documentent votre code.",
            "Comments document your code.");
        h.seedQuiz(m1, 3, "Quiz: Introduction", "Quiz: Introduction", new String[][] {
            {"Qui a créé Python ?", "Who created Python?", "Linus Torvalds", "Linus Torvalds", "Guido van Rossum", "Guido van Rossum", "James Gosling", "James Gosling", "Brendan Eich", "Brendan Eich", "2", "Guido van Rossum a créé Python en 1991.", "Guido van Rossum created Python in 1991."},
            {"Quelle extension ont les fichiers Python ?", "What extension do Python files have?", ".java", ".java", ".js", ".js", ".py", ".py", ".pt", ".pt", "3", "Les fichiers Python utilisent l'extension .py.", "Python files use the .py extension."},
            {"Quel caractère commence un commentaire ?", "Which character starts a comment?", "//", "//", "#", "#", "/*", "/*", "--", "--", "2", "Le symbole # est utilisé pour les commentaires.", "The # symbol is used for comments."},
        });
        h.seedChallenge(m1, "Hello World", "Hello World",
            "Écrivez un programme qui affiche 'Hello, World!'", "Write a program that prints 'Hello, World!'",
            "PYTHON", "BEGINNER", "", "print('Hello, World!')", "", "Hello, World!", "", "", 30);
        h.seedChallenge(m1, "Salutations", "Greetings",
            "Affichez 'Bonjour CodeQuest!'", "Print 'Bonjour CodeQuest!'",
            "PYTHON", "BEGINNER", "", "print('Bonjour CodeQuest!')", "", "Bonjour CodeQuest!", "", "", 30);

        Module m2 = h.createModule(t, 1, "Variables et Types", "Variables and Types",
                "Stocker et manipuler des données", "Store and manipulate data");
        h.seedLesson(m2, 0, "Les variables", "Variables", Lesson.LessonType.THEORY,
            "Une variable est un espace de stockage nommé. En Python, le type est détecté automatiquement.",
            "A variable is a named storage space. In Python, the type is detected automatically.",
            "nom = \"Alice\"\nage = 25\nprint(nom, age)", "python",
            "Python est dynamiquement typé.",
            "Python is dynamically typed.");
        h.seedLesson(m2, 1, "Types: int, float, str, bool", "Types: int, float, str, bool", Lesson.LessonType.THEORY,
            "4 types de base: int (entier), float (décimal), str (texte), bool (True/False).",
            "4 basic types: int (integer), float (decimal), str (text), bool (True/False).",
            "x = 42        # int\ny = 3.14      # float\nz = \"hello\"   # str\nw = True      # bool", "python",
            "Utilisez type() pour vérifier le type.",
            "Use type() to check the type.");
        h.seedLesson(m2, 2, "Opérations arithmétiques", "Arithmetic operations", Lesson.LessonType.PRACTICE,
            "+, -, *, /, // (division entière), % (modulo), ** (puissance).",
            "+, -, *, /, // (integer division), % (modulo), ** (power).",
            "a = 10\nb = 3\nprint(a + b)   # 13\nprint(a ** b)  # 1000", "python",
            "// donne la division entière, % le reste.",
            "// gives integer division, % the remainder.");
        h.seedLesson(m2, 3, "Entrées utilisateur", "User input", Lesson.LessonType.PRACTICE,
            "input() lit une ligne. Elle retourne toujours un str — convertissez avec int() ou float().",
            "input() reads a line. It always returns a str — convert with int() or float().",
            "nom = input()\nage = int(input())\nprint(f\"{nom} a {age} ans\")", "python",
            "Utilisez les f-strings: f\"{variable}\".",
            "Use f-strings: f\"{variable}\".");
        h.seedQuiz(m2, 4, "Quiz: Variables", "Quiz: Variables", new String[][] {
            {"Quel est le type de 3.14 ?", "What is the type of 3.14?", "int", "int", "float", "float", "str", "str", "double", "double", "2", "3.14 est un float.", "3.14 is a float."},
            {"Que retourne type(True) ?", "What does type(True) return?", "<class 'int'>", "<class 'int'>", "<class 'str'>", "<class 'str'>", "<class 'bool'>", "<class 'bool'>", "<class 'float'>", "<class 'float'>", "3", "True est de type bool.", "True is of type bool."},
            {"Quel opérateur donne la puissance ?", "Which operator gives the power?", "%", "%", "//", "//", "**", "**", "^^", "^^", "3", "** est la puissance: 2**3 = 8.", "** is the power: 2**3 = 8."},
        });
        h.seedChallenge(m2, "Calculette", "Calculator",
            "Lisez deux entiers et affichez leur somme.", "Read two integers and print their sum.",
            "PYTHON", "BEGINNER", "a = int(input())\nb = int(input())", "a = int(input())\nb = int(input())\nprint(a + b)", "3\n5", "8", "10\n20", "30", 30);
        h.seedChallenge(m2, "Surface du cercle", "Area of a circle",
            "Lisez un rayon et affichez la surface (pi=3.14159), arrondie à 2 décimales.", "Read a radius and print the area (pi=3.14159), rounded to 2 decimals.",
            "PYTHON", "INTERMEDIATE", "", "r = int(input())\nprint(round(3.14159 * r * r, 2))", "5", "78.54", "10", "314.16", 50);
        h.seedChallenge(m2, "Celsius → Fahrenheit", "Celsius → Fahrenheit",
            "Lisez une température en Celsius et affichez F = C × 9/5 + 32.", "Read a temperature in Celsius and print F = C × 9/5 + 32.",
            "PYTHON", "INTERMEDIATE", "", "c = int(input())\nprint(c * 9 / 5 + 32)", "0", "32.0", "100", "212.0", 50);
    }

    private void seedIntermediate() {
        Track t = trackRepository.save(Track.builder()
                .title("Python — Intermédiaire").titleEn("Python — Intermediate")
                .description("Maîtrisez les structures de contrôle, les fonctions et les collections en Python.")
                .descriptionEn("Master control structures, functions and collections in Python.")
                .difficulty(Track.Difficulty.INTERMEDIATE).language(Track.Language.PYTHON)
                .requiredLevel(1).xpPerLesson(35).build());

        Module m1 = h.createModule(t, 0, "Structures de contrôle", "Control structures",
                "if/else, boucles for et while", "if/else, for and while loops");
        h.seedLesson(m1, 0, "Conditions: if, elif, else", "Conditions: if, elif, else", Lesson.LessonType.THEORY,
            "Les conditions exécutent du code selon une situation. Python utilise if, elif et else.",
            "Conditions execute code based on a situation. Python uses if, elif and else.",
            "age = 18\nif age >= 18:\n    print(\"Majeur\")\nelif age >= 13:\n    print(\"Adolescent\")\nelse:\n    print(\"Enfant\")", "python",
            "N'oubliez pas les : et l'indentation !",
            "Don't forget the : and indentation!");
        h.seedLesson(m1, 1, "Boucle for", "For loop", Lesson.LessonType.PRACTICE,
            "for parcourt une séquence. range(n) génère 0 à n-1.",
            "for iterates over a sequence. range(n) generates 0 to n-1.",
            "for i in range(5):\n    print(i)\n\nfor lettre in \"Python\":\n    print(lettre)", "python",
            "range(start, stop, step) pour plus de contrôle.",
            "range(start, stop, step) for more control.");
        h.seedLesson(m1, 2, "Boucle while", "While loop", Lesson.LessonType.PRACTICE,
            "while s'exécute tant que sa condition est True.",
            "while runs as long as its condition is True.",
            "compteur = 0\nwhile compteur < 5:\n    print(compteur)\n    compteur += 1", "python",
            "Modifiez la condition pour éviter les boucles infinies.",
            "Modify the condition to avoid infinite loops.");
        h.seedLesson(m1, 3, "break et continue", "break and continue", Lesson.LessonType.THEORY,
            "break sort de la boucle. continue passe à l'itération suivante.",
            "break exits the loop. continue skips to the next iteration.",
            "for i in range(10):\n    if i == 3: continue\n    if i == 7: break\n    print(i)", "python",
            "break et continue marchent dans for et while.",
            "break and continue work in for and while.");
        h.seedQuiz(m1, 4, "Quiz: Contrôle", "Quiz: Control", new String[][] {
            {"Que fait break ?", "What does break do?", "Passe à l'itération suivante", "Skips to next iteration", "Sort de la boucle", "Exits the loop", "Redémarre la boucle", "Restarts the loop", "Termine le programme", "Ends the program", "2", "break sort immédiatement de la boucle.", "break immediately exits the loop."},
            {"range(3) produit combien de valeurs ?", "How many values does range(3) produce?", "2", "2", "3", "3", "4", "4", "1", "1", "2", "range(3) → 0, 1, 2 soit 3 valeurs.", "range(3) → 0, 1, 2 which is 3 values."},
            {"Résultat de 10 % 3 ?", "Result of 10 % 3?", "3", "3", "1", "1", "3.33", "3.33", "0", "0", "2", "% = modulo (reste): 10 ÷ 3 = 3 reste 1.", "% = modulo (remainder): 10 ÷ 3 = 3 remainder 1."},
        });
        h.seedChallenge(m1, "Pair ou Impair", "Even or Odd",
            "Lisez un entier, affichez 'Pair' ou 'Impair'.", "Read an integer, print 'Pair' or 'Impair'.",
            "PYTHON", "BEGINNER", "", "n = int(input())\nprint('Pair' if n % 2 == 0 else 'Impair')", "4", "Pair", "7", "Impair", 30);
        h.seedChallenge(m1, "Table de multiplication", "Multiplication table",
            "Lisez N, affichez sa table de 1 à 10: 'N x i = résultat'.", "Read N, print its table from 1 to 10: 'N x i = result'.",
            "PYTHON", "INTERMEDIATE", "", "n = int(input())\nfor i in range(1, 11):\n    print(f'{n} x {i} = {n*i}')", "3", "3 x 1 = 3\n3 x 2 = 6\n3 x 3 = 9\n3 x 4 = 12\n3 x 5 = 15\n3 x 6 = 18\n3 x 7 = 21\n3 x 8 = 24\n3 x 9 = 27\n3 x 10 = 30", "", "", 50);
        h.seedChallenge(m1, "FizzBuzz", "FizzBuzz",
            "Pour 1 à N: 'FizzBuzz' si /15, 'Fizz' si /3, 'Buzz' si /5, sinon le nombre.", "For 1 to N: 'FizzBuzz' if /15, 'Fizz' if /3, 'Buzz' if /5, else the number.",
            "PYTHON", "ADVANCED", "", "n = int(input())\nfor i in range(1, n+1):\n    if i % 15 == 0: print('FizzBuzz')\n    elif i % 3 == 0: print('Fizz')\n    elif i % 5 == 0: print('Buzz')\n    else: print(i)", "5", "1\n2\nFizz\n4\nBuzz", "15", "1\n2\nFizz\n4\nBuzz\nFizz\n7\n8\nFizz\nBuzz\n11\nFizz\n13\n14\nFizzBuzz", 80);

        Module m2 = h.createModule(t, 1, "Les Fonctions", "Functions",
                "Créez des fonctions réutilisables", "Create reusable functions");
        h.seedLesson(m2, 0, "Définir une fonction", "Defining a function", Lesson.LessonType.THEORY,
            "def définit une fonction, return renvoie une valeur.",
            "def defines a function, return sends back a value.",
            "def saluer(nom):\n    return f\"Bonjour, {nom}!\"\n\nprint(saluer(\"Alice\"))", "python",
            "def nom(paramètres): ... return valeur",
            "def name(parameters): ... return value");
        h.seedLesson(m2, 1, "Paramètres par défaut", "Default parameters", Lesson.LessonType.THEORY,
            "Les paramètres peuvent avoir des valeurs par défaut — ils deviennent optionnels.",
            "Parameters can have default values — they become optional.",
            "def info(nom, age=18):\n    print(f\"{nom}, {age} ans\")\n\ninfo(\"Alice\")     # age=18\ninfo(\"Bob\", 25)   # age=25", "python",
            "Les paramètres avec défaut viennent après les paramètres obligatoires.",
            "Default parameters come after required parameters.");
        h.seedLesson(m2, 2, "Fonctions lambda", "Lambda functions", Lesson.LessonType.PRACTICE,
            "lambda crée une fonction anonyme en une ligne. Utile pour sorted(), map(), filter().",
            "lambda creates an anonymous function in one line. Useful for sorted(), map(), filter().",
            "carre = lambda x: x ** 2\nprint(carre(5))  # 25\n\nnums = [3, 1, 4, 1, 5]\nprint(sorted(nums, key=lambda n: -n))", "python",
            "lambda params: expression — pour des opérations simples.",
            "lambda params: expression — for simple operations.");
        h.seedLesson(m2, 3, "Récursivité", "Recursion", Lesson.LessonType.THEORY,
            "Une fonction récursive s'appelle elle-même. Elle a toujours un cas de base.",
            "A recursive function calls itself. It always has a base case.",
            "def factorielle(n):\n    if n <= 1: return 1\n    return n * factorielle(n - 1)\n\nprint(factorielle(5))  # 120", "python",
            "Sans cas de base → RecursionError.",
            "Without a base case → RecursionError.");
        h.seedQuiz(m2, 4, "Quiz: Fonctions", "Quiz: Functions", new String[][] {
            {"Quel mot-clé définit une fonction ?", "Which keyword defines a function?", "function", "function", "func", "func", "def", "def", "fn", "fn", "3", "def est le mot-clé Python.", "def is the Python keyword."},
            {"Que retourne une fonction sans return ?", "What does a function without return give?", "0", "0", "None", "None", "False", "False", "Erreur", "Error", "2", "Sans return explicite → None.", "Without explicit return → None."},
            {"lambda x: x*2 appliqué à 5 donne ?", "lambda x: x*2 applied to 5 gives?", "5", "5", "7", "7", "10", "10", "25", "25", "3", "5*2 = 10.", "5*2 = 10."},
        });
        h.seedChallenge(m2, "Factorielle", "Factorial",
            "Lisez N, affichez N!.", "Read N, print N!.",
            "PYTHON", "BEGINNER", "", "n = int(input())\nr = 1\nfor i in range(1, n+1): r *= i\nprint(r)", "5", "120", "10", "3628800", 30);
        h.seedChallenge(m2, "Fibonacci", "Fibonacci",
            "Lisez N, affichez le N-ème nombre de Fibonacci (F(0)=0, F(1)=1).", "Read N, print the N-th Fibonacci number (F(0)=0, F(1)=1).",
            "PYTHON", "INTERMEDIATE", "", "n = int(input())\na, b = 0, 1\nfor _ in range(n): a, b = b, a + b\nprint(a)", "6", "8", "10", "55", 50);
        h.seedChallenge(m2, "Nombre premier", "Prime number",
            "Lisez N, affichez 'True' si premier, 'False' sinon.", "Read N, print 'True' if prime, 'False' otherwise.",
            "PYTHON", "ADVANCED", "", "n = int(input())\nif n < 2: print(False)\nelse: print(all(n % i != 0 for i in range(2, int(n**0.5)+1)))", "7", "True", "12", "False", 80);
    }

    private void seedAdvanced() {
        Track t = trackRepository.save(Track.builder()
                .title("Python — Avancé").titleEn("Python — Advanced")
                .description("Collections, manipulation de texte et algorithmes avancés en Python.")
                .descriptionEn("Collections, text manipulation and advanced algorithms in Python.")
                .difficulty(Track.Difficulty.ADVANCED).language(Track.Language.PYTHON)
                .requiredLevel(1).xpPerLesson(50).build());

        Module m1 = h.createModule(t, 0, "Listes et Dictionnaires", "Lists and Dictionaries",
                "Maîtrisez les collections de données", "Master data collections");
        h.seedLesson(m1, 0, "Les listes", "Lists", Lesson.LessonType.THEORY,
            "Liste = collection ordonnée, modifiable. Créée avec [].",
            "List = ordered, mutable collection. Created with [].",
            "fruits = [\"pomme\", \"banane\", \"cerise\"]\nfruits.append(\"orange\")\nprint(fruits[0])   # pomme\nprint(len(fruits)) # 4", "python",
            "Les index commencent à 0. -1 = dernier élément.",
            "Indexes start at 0. -1 = last element.");
        h.seedLesson(m1, 1, "Compréhensions de listes", "List comprehensions", Lesson.LessonType.PRACTICE,
            "[expression for item in iterable if condition] — crée des listes de manière concise.",
            "[expression for item in iterable if condition] — creates lists concisely.",
            "carres = [x**2 for x in range(10)]\npairs = [x for x in range(20) if x % 2 == 0]\nprint(carres)\nprint(pairs)", "python",
            "Plus rapide et lisible que les boucles for équivalentes.",
            "Faster and more readable than equivalent for loops.");
        h.seedLesson(m1, 2, "Les dictionnaires", "Dictionaries", Lesson.LessonType.THEORY,
            "Dict = paires clé-valeur. Accès par clé O(1).",
            "Dict = key-value pairs. Access by key O(1).",
            "etudiant = {\"nom\": \"Alice\", \"age\": 20}\nprint(etudiant[\"nom\"])\netudiant[\"ville\"] = \"Paris\"\nfor k, v in etudiant.items():\n    print(f\"{k}: {v}\")", "python",
            "Utilisez .get(clé, défaut) pour éviter KeyError.",
            "Use .get(key, default) to avoid KeyError.");
        h.seedLesson(m1, 3, "Tuples et Sets", "Tuples and Sets", Lesson.LessonType.THEORY,
            "Tuple = immuable (non modifiable). Set = sans doublons.",
            "Tuple = immutable (cannot be modified). Set = no duplicates.",
            "coords = (48.8, 2.3)   # tuple\nensemble = {1, 2, 3, 2, 1}\nprint(ensemble)  # {1, 2, 3}", "python",
            "Tuple quand les données ne doivent pas changer.",
            "Tuple when the data should not change.");
        h.seedQuiz(m1, 4, "Quiz: Collections", "Quiz: Collections", new String[][] {
            {"Comment ajouter à une liste ?", "How to add to a list?", "list.add()", "list.add()", "list.append()", "list.append()", "list.push()", "list.push()", "list += item", "list += item", "2", "append() ajoute à la fin.", "append() adds to the end."},
            {"Liste vs tuple ?", "List vs tuple?", "Le tuple est plus rapide", "Tuple is faster", "Le tuple est immuable", "Tuple is immutable", "La liste n'a pas d'index", "List has no index", "Aucune différence", "No difference", "2", "Un tuple ne peut pas être modifié.", "A tuple cannot be modified."},
            {"{1, 2, 2, 3} donne ?", "{1, 2, 2, 3} gives?", "{1, 2, 2, 3}", "{1, 2, 2, 3}", "{1, 2, 3}", "{1, 2, 3}", "[1, 2, 3]", "[1, 2, 3]", "Erreur", "Error", "2", "Un set élimine les doublons.", "A set eliminates duplicates."},
        });
        h.seedChallenge(m1, "Inverser une liste", "Reverse a list",
            "Lisez N nombres, affichez-les en ordre inverse.", "Read N numbers, print them in reverse order.",
            "PYTHON", "INTERMEDIATE", "", "n = int(input())\nnums = [int(input()) for _ in range(n)]\nfor x in reversed(nums): print(x)", "3\n1\n2\n3", "3\n2\n1", "4\n10\n20\n30\n40", "40\n30\n20\n10", 50);
        h.seedChallenge(m1, "Maximum sans max()", "Maximum without max()",
            "Lisez N nombres, affichez le plus grand sans utiliser max().", "Read N numbers, print the largest without using max().",
            "PYTHON", "ADVANCED", "", "n = int(input())\nnums = [int(input()) for _ in range(n)]\nm = nums[0]\nfor x in nums:\n    if x > m: m = x\nprint(m)", "4\n3\n7\n2\n5", "7", "3\n-1\n-5\n-2", "-1", 80);

        Module m2 = h.createModule(t, 1, "Chaînes de caractères", "Strings",
                "Manipulez le texte comme un pro", "Manipulate text like a pro");
        h.seedLesson(m2, 0, "Slicing et méthodes", "Slicing and methods", Lesson.LessonType.THEORY,
            "Slicing: s[start:end]. Méthodes: upper(), lower(), strip(), split(), join(), replace().",
            "Slicing: s[start:end]. Methods: upper(), lower(), strip(), split(), join(), replace().",
            "s = \"Hello, Python!\"\nprint(s[0:5])    # Hello\nprint(s.upper()) # HELLO, PYTHON!\nprint(\"a-b-c\".split(\"-\"))  # ['a','b','c']", "python",
            "s[::-1] inverse la chaîne.",
            "s[::-1] reverses the string.");
        h.seedLesson(m2, 1, "F-strings et formatage", "F-strings and formatting", Lesson.LessonType.PRACTICE,
            "f-strings: f\"{expression}\". Le formatage le plus moderne et lisible.",
            "f-strings: f\"{expression}\". The most modern and readable formatting.",
            "nom = \"Alice\"\nage = 25\nprint(f\"{nom} a {age} ans\")\nprint(f\"Pi = {3.14159:.2f}\")", "python",
            "f\"...{expr:format}...\" pour le formatage avancé.",
            "f\"...{expr:format}...\" for advanced formatting.");
        h.seedLesson(m2, 2, "Expressions régulières", "Regular expressions", Lesson.LessonType.THEORY,
            "Le module re: re.findall(), re.search(), re.sub() pour chercher des motifs.",
            "The re module: re.findall(), re.search(), re.sub() to search for patterns.",
            "import re\ntexte = \"Contact: alice@mail.com\"\nemail = re.findall(r'\\w+@\\w+\\.\\w+', texte)\nprint(email)", "python",
            "Les regex sont puissantes pour la validation de données.",
            "Regex are powerful for data validation.");
        h.seedQuiz(m2, 3, "Quiz: Chaînes", "Quiz: Strings", new String[][] {
            {"'hello'.upper() retourne ?", "'hello'.upper() returns?", "Hello", "Hello", "hello", "hello", "HELLO", "HELLO", "hELLO", "hELLO", "3", "upper() met tout en majuscules.", "upper() converts everything to uppercase."},
            {"'a,b,c'.split(',') retourne ?", "'a,b,c'.split(',') returns?", "['a,b,c']", "['a,b,c']", "['a', 'b', 'c']", "['a', 'b', 'c']", "'abc'", "'abc'", "Erreur", "Error", "2", "split découpe au niveau des virgules.", "split cuts at the commas."},
            {"Les chaînes Python sont:", "Python strings are:", "Mutables", "Mutable", "Immuables", "Immutable", "Seulement ASCII", "ASCII only", "Des listes", "Lists", "2", "On ne peut pas modifier un caractère directement.", "You cannot modify a character directly."},
        });
        h.seedChallenge(m2, "Palindrome", "Palindrome",
            "Lisez un mot, affichez 'True' si palindrome, 'False' sinon.", "Read a word, print 'True' if palindrome, 'False' otherwise.",
            "PYTHON", "INTERMEDIATE", "", "s = input().strip().lower()\nprint(s == s[::-1])", "kayak", "True", "hello", "False", 50);
        h.seedChallenge(m2, "Voyelles", "Vowels",
            "Lisez une chaîne, affichez le nombre de voyelles (a,e,i,o,u).", "Read a string, print the number of vowels (a,e,i,o,u).",
            "PYTHON", "ADVANCED", "", "s = input().lower()\nprint(sum(1 for c in s if c in 'aeiou'))", "Hello World", "3", "Python Programming", "4", 80);
        h.seedChallenge(m2, "Anagramme", "Anagram",
            "Lisez deux mots, affichez 'True' s'ils sont des anagrammes.", "Read two words, print 'True' if they are anagrams.",
            "PYTHON", "ADVANCED", "", "a = sorted(input().strip().lower())\nb = sorted(input().strip().lower())\nprint(a == b)", "listen\nsilent", "True", "hello\nworld", "False", 80);
    }
}
