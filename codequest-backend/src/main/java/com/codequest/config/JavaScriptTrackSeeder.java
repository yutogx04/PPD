package com.codequest.config;

import com.codequest.entity.*;
import com.codequest.entity.Module;
import com.codequest.repository.TrackRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component @RequiredArgsConstructor @Slf4j
public class JavaScriptTrackSeeder {

    private final TrackRepository trackRepository;
    private final SeederHelper h;

    private static final String RL = "const rl=require('readline').createInterface({input:process.stdin});const L=[];rl.on('line',l=>L.push(l));rl.on('close',()=>{";
    private static final String END = "});";

    public void seed() {
        seedBeginner();
        seedIntermediate();
        seedAdvanced();
        log.info("Seeded 3 JavaScript tracks (Beginner/Intermediate/Advanced)");
    }

    private void seedBeginner() {
        Track t = trackRepository.save(Track.builder()
                .title("JavaScript — Débutant").titleEn("JavaScript — Beginner")
                .description("Premiers pas avec JavaScript: variables, types et opérateurs de base.")
                .descriptionEn("First steps with JavaScript: variables, types and basic operators.")
                .difficulty(Track.Difficulty.BEGINNER).language(Track.Language.JAVASCRIPT)
                .requiredLevel(1).xpPerLesson(20).build());

        Module m1 = h.createModule(t, 0, "Introduction à JavaScript", "Introduction to JavaScript",
                "Découvrez le langage du web", "Discover the language of the web");
        h.seedLesson(m1, 0, "Qu'est-ce que JavaScript ?", "What is JavaScript?", Lesson.LessonType.THEORY,
            "JavaScript est LE langage du web. Il tourne dans le navigateur et côté serveur avec Node.js.",
            "JavaScript is THE language of the web. It runs in the browser and server-side with Node.js.",
            "console.log(\"Hello, JavaScript!\");", "javascript",
            "JS est interprété et dynamiquement typé.",
            "JS is interpreted and dynamically typed.");
        h.seedLesson(m1, 1, "Variables: let, const, var", "Variables: let, const, var", Lesson.LessonType.THEORY,
            "let = modifiable, const = constante, var = obsolète. Utilisez const par défaut, let quand nécessaire.",
            "let = mutable, const = constant, var = obsolete. Use const by default, let when needed.",
            "let compteur = 0;\ncompteur = 1; // OK\n\nconst PI = 3.14;\n// PI = 3; // Erreur!", "javascript",
            "const ne peut pas être réassigné après déclaration.",
            "const cannot be reassigned after declaration.");
        h.seedLesson(m1, 2, "Types de données", "Data types", Lesson.LessonType.PRACTICE,
            "7 types primitifs: string, number, boolean, null, undefined, symbol, bigint.",
            "7 primitive types: string, number, boolean, null, undefined, symbol, bigint.",
            "let x = 42;          // number\nlet y = \"hello\";      // string\nlet z = true;         // boolean\nconsole.log(typeof x); // 'number'", "javascript",
            "typeof null retourne 'object' — c'est un bug historique de JS !",
            "typeof null returns 'object' — it's a historic JS bug!");
        h.seedLesson(m1, 3, "Template literals", "Template literals", Lesson.LessonType.PRACTICE,
            "Les backticks `` permettent l'interpolation de variables avec ${expression}.",
            "Backticks `` allow variable interpolation with ${expression}.",
            "const nom = \"Alice\";\nconsole.log(`Bonjour, ${nom}!`);", "javascript",
            "${} pour insérer des expressions dans les backticks.",
            "${} to insert expressions inside backticks.");
        h.seedQuiz(m1, 4, "Quiz: Bases JS", "Quiz: JS Basics", new String[][] {
            {"Quelle déclaration crée une constante ?", "Which declaration creates a constant?", "let", "let", "var", "var", "const", "const", "static", "static", "3", "const crée une variable non réassignable.", "const creates a non-reassignable variable."},
            {"typeof null retourne ?", "typeof null returns?", "'null'", "'null'", "'undefined'", "'undefined'", "'object'", "'object'", "'boolean'", "'boolean'", "3", "Bug historique de JS.", "Historical JS bug."},
            {"=== fait quoi ?", "What does === do?", "Affectation", "Assignment", "Comparaison lâche", "Loose comparison", "Comparaison stricte", "Strict comparison", "Identité", "Identity", "3", "=== compare valeur ET type.", "=== compares value AND type."},
        });
        h.seedChallenge(m1, "Hello JS", "Hello JS",
            "Affichez 'Hello, JavaScript!'", "Print 'Hello, JavaScript!'",
            "JAVASCRIPT", "BEGINNER", "", "console.log('Hello, JavaScript!');", "", "Hello, JavaScript!", "", "", 30);
        h.seedChallenge(m1, "Somme JS", "Sum JS",
            "Lisez 2 nombres, affichez leur somme.", "Read 2 numbers, print their sum.",
            "JAVASCRIPT", "INTERMEDIATE", "", RL+"console.log(parseInt(L[0])+parseInt(L[1]));"+END, "3\n5", "8", "10\n20", "30", 50);

        Module m2 = h.createModule(t, 1, "Conditions et Boucles", "Conditions and Loops",
                "Structures de contrôle en JS", "Control structures in JS");
        h.seedLesson(m2, 0, "if / else", "if / else", Lesson.LessonType.THEORY,
            "if/else avec des accolades {}. L'opérateur ternaire ? : pour les cas simples.",
            "if/else with curly braces {}. The ternary operator ? : for simple cases.",
            "const age = 18;\nif (age >= 18) {\n  console.log(\"Majeur\");\n} else {\n  console.log(\"Mineur\");\n}\n\n// Ternaire\nconsole.log(age >= 18 ? \"Majeur\" : \"Mineur\");", "javascript",
            "Toujours utiliser === au lieu de ==.",
            "Always use === instead of ==.");
        h.seedLesson(m2, 1, "Boucles for et while", "for and while loops", Lesson.LessonType.PRACTICE,
            "for(init; cond; incr) {}, while(cond) {}, for...of pour les valeurs, for...in pour les clés.",
            "for(init; cond; incr) {}, while(cond) {}, for...of for values, for...in for keys.",
            "for (let i = 0; i < 5; i++) {\n  console.log(i);\n}\n\nconst arr = ['a', 'b'];\nfor (const val of arr) console.log(val);", "javascript",
            "for...of pour les valeurs, for...in pour les clés/indices.",
            "for...of for values, for...in for keys/indices.");
        h.seedLesson(m2, 2, "Switch", "Switch", Lesson.LessonType.THEORY,
            "switch compare une valeur à plusieurs cas. N'oubliez pas break!",
            "switch compares a value to multiple cases. Don't forget break!",
            "const jour = 'lundi';\nswitch (jour) {\n  case 'lundi': console.log('Début'); break;\n  case 'vendredi': console.log('Weekend!'); break;\n  default: console.log('Milieu');\n}", "javascript",
            "Sans break, l'exécution 'tombe' dans les cas suivants.",
            "Without break, execution 'falls through' to the next cases.");
        h.seedQuiz(m2, 3, "Quiz: Contrôle JS", "Quiz: JS Control", new String[][] {
            {"Quel opérateur ternaire en JS ?", "Which ternary operator in JS?", "if:else", "if:else", "? :", "? :", "when:then", "when:then", "?:", "?:", "2", "condition ? siVrai : siFaux.", "condition ? ifTrue : ifFalse."},
            {"for...of itère sur ?", "for...of iterates over?", "Les clés", "Keys", "Les valeurs", "Values", "Les index", "Indexes", "Les types", "Types", "2", "for...of donne les valeurs d'un itérable.", "for...of gives the values of an iterable."},
        });
        h.seedChallenge(m2, "Pair ou Impair JS", "Even or Odd JS",
            "Lisez un entier, affichez 'Pair' ou 'Impair'.", "Read an integer, print 'Pair' or 'Impair'.",
            "JAVASCRIPT", "BEGINNER", "", RL+"console.log(parseInt(L[0])%2===0?'Pair':'Impair');"+END, "4", "Pair", "7", "Impair", 30);
        h.seedChallenge(m2, "Somme 1 à N JS", "Sum 1 to N JS",
            "Lisez N, affichez la somme de 1 à N.", "Read N, print the sum from 1 to N.",
            "JAVASCRIPT", "INTERMEDIATE", "", RL+"const n=parseInt(L[0]);let s=0;for(let i=1;i<=n;i++)s+=i;console.log(s);"+END, "5", "15", "100", "5050", 50);
    }

    private void seedIntermediate() {
        Track t = trackRepository.save(Track.builder()
                .title("JavaScript — Intermédiaire").titleEn("JavaScript — Intermediate")
                .description("Fonctions, closures, tableaux et objets en JavaScript.")
                .descriptionEn("Functions, closures, arrays and objects in JavaScript.")
                .difficulty(Track.Difficulty.INTERMEDIATE).language(Track.Language.JAVASCRIPT)
                .requiredLevel(1).xpPerLesson(35).build());

        Module m1 = h.createModule(t, 0, "Fonctions", "Functions",
                "Fonctions classiques, arrow et closures", "Classic functions, arrow and closures");
        h.seedLesson(m1, 0, "Fonctions et arrow functions", "Functions and arrow functions", Lesson.LessonType.THEORY,
            "function classique vs arrow (=>) — les arrow n'ont pas leur propre this.",
            "Classic function vs arrow (=>) — arrows don't have their own this.",
            "function add(a, b) { return a + b; }\nconst mult = (a, b) => a * b;\nconsole.log(add(3, 5)); // 8\nconsole.log(mult(3, 5)); // 15", "javascript",
            "Arrow en une ligne: pas besoin de return ni {}.",
            "One-line arrow: no need for return or {}.");
        h.seedLesson(m1, 1, "Closures", "Closures", Lesson.LessonType.THEORY,
            "Une closure capture les variables de son scope parent, même après sa fin.",
            "A closure captures variables from its parent scope, even after it ends.",
            "function compteur() {\n  let count = 0;\n  return () => ++count;\n}\nconst c = compteur();\nconsole.log(c()); // 1\nconsole.log(c()); // 2", "javascript",
            "Les closures permettent l'encapsulation de données privées.",
            "Closures allow encapsulation of private data.");
        h.seedLesson(m1, 2, "Callback functions", "Callback functions", Lesson.LessonType.PRACTICE,
            "Un callback est une fonction passée en argument, exécutée plus tard.",
            "A callback is a function passed as an argument, executed later.",
            "function traiter(arr, callback) {\n  return arr.map(callback);\n}\nconst doubles = traiter([1,2,3], x => x * 2);\nconsole.log(doubles); // [2, 4, 6]", "javascript",
            "Les callbacks sont la base de l'asynchrone en JS.",
            "Callbacks are the foundation of async in JS.");
        h.seedQuiz(m1, 3, "Quiz: Fonctions JS", "Quiz: JS Functions", new String[][] {
            {"Arrow function syntax ?", "Arrow function syntax?", "function()", "function()", "=> {}", "=> {}", "(a, b) => a + b", "(a, b) => a + b", "lambda a, b: a + b", "lambda a, b: a + b", "3", "(params) => expression.", "(params) => expression."},
            {"Qu'est-ce qu'une closure ?", "What is a closure?", "Une boucle", "A loop", "Fonction avec accès au scope parent", "Function with access to parent scope", "Un objet", "An object", "Un type", "A type", "2", "Capture les variables du scope englobant.", "Captures variables from the enclosing scope."},
        });
        h.seedChallenge(m1, "Factorielle JS", "Factorial JS",
            "Lisez N, affichez N!.", "Read N, print N!.",
            "JAVASCRIPT", "BEGINNER", "", RL+"let n=parseInt(L[0]),r=1;for(let i=1;i<=n;i++)r*=i;console.log(r);"+END, "5", "120", "10", "3628800", 30);
        h.seedChallenge(m1, "FizzBuzz JS", "FizzBuzz JS",
            "Pour 1 à N: FizzBuzz/Fizz/Buzz/nombre.", "For 1 to N: FizzBuzz/Fizz/Buzz/number.",
            "JAVASCRIPT", "ADVANCED", "", RL+"const n=parseInt(L[0]);for(let i=1;i<=n;i++){if(i%15===0)console.log('FizzBuzz');else if(i%3===0)console.log('Fizz');else if(i%5===0)console.log('Buzz');else console.log(i);}"+END,
            "5", "1\n2\nFizz\n4\nBuzz", "15", "1\n2\nFizz\n4\nBuzz\nFizz\n7\n8\nFizz\nBuzz\n11\nFizz\n13\n14\nFizzBuzz", 80);

        Module m2 = h.createModule(t, 1, "Tableaux et Objets", "Arrays and Objects",
                "Collections de données en JS", "Data collections in JS");
        h.seedLesson(m2, 0, "Arrays", "Arrays", Lesson.LessonType.THEORY,
            "push(), pop(), shift(), unshift(). Parcourez avec for...of ou méthodes fonctionnelles.",
            "push(), pop(), shift(), unshift(). Iterate with for...of or functional methods.",
            "const arr = [1, 2, 3];\narr.push(4); // [1,2,3,4]\narr.pop();   // [1,2,3]\nconsole.log(arr.length);", "javascript",
            "Les arrays JS sont dynamiques.",
            "JS arrays are dynamic.");
        h.seedLesson(m2, 1, "map, filter, reduce", "map, filter, reduce", Lesson.LessonType.PRACTICE,
            "map transforme, filter sélectionne, reduce accumule.",
            "map transforms, filter selects, reduce accumulates.",
            "const nums = [1, 2, 3, 4, 5];\nconsole.log(nums.map(n => n * 2));\nconsole.log(nums.filter(n => n % 2 === 0));\nconsole.log(nums.reduce((acc, n) => acc + n, 0));", "javascript",
            "Toutes retournent un nouveau résultat sans modifier l'original.",
            "All return a new result without modifying the original.");
        h.seedLesson(m2, 2, "Objets et destructuration", "Objects and destructuring", Lesson.LessonType.PRACTICE,
            "Objets = paires clé-valeur. Destructuration extrait des valeurs facilement.",
            "Objects = key-value pairs. Destructuring extracts values easily.",
            "const user = { nom: 'Alice', age: 25 };\nconst { nom, age } = user;\nconsole.log(nom); // Alice\n\nconst copie = { ...user, ville: 'Paris' };", "javascript",
            "... (spread) copie/fusionne des objets et arrays.",
            "... (spread) copies/merges objects and arrays.");
        h.seedQuiz(m2, 3, "Quiz: Collections JS", "Quiz: JS Collections", new String[][] {
            {"array.map(fn) fait quoi ?", "What does array.map(fn) do?", "Modifie en place", "Modifies in place", "Crée un nouveau tableau transformé", "Creates a new transformed array", "Filtre", "Filters", "Trie", "Sorts", "2", "map() crée un NOUVEAU tableau.", "map() creates a NEW array."},
            {"filter(n => n > 1) sur [1,2,3] ?", "filter(n => n > 1) on [1,2,3]?", "[1,2,3]", "[1,2,3]", "[2,3]", "[2,3]", "[1]", "[1]", "3", "3", "2", "filter garde les éléments où la condition est true.", "filter keeps elements where the condition is true."},
        });
        h.seedChallenge(m2, "Inverse Array JS", "Reverse Array JS",
            "Lisez N nombres, affichez-les en ordre inverse.", "Read N numbers, print them in reverse order.",
            "JAVASCRIPT", "INTERMEDIATE", "", RL+"const n=parseInt(L[0]);for(let i=n;i>=1;i--)console.log(L[i]);"+END, "3\n1\n2\n3", "3\n2\n1", "4\n10\n20\n30\n40", "40\n30\n20\n10", 50);
        h.seedChallenge(m2, "Somme des pairs JS", "Sum of evens JS",
            "Lisez N nombres, affichez la somme des pairs.", "Read N numbers, print the sum of even numbers.",
            "JAVASCRIPT", "ADVANCED", "", RL+"const n=parseInt(L[0]);let s=0;for(let i=1;i<=n;i++){const v=parseInt(L[i]);if(v%2===0)s+=v;}console.log(s);"+END,
            "5\n1\n2\n3\n4\n5", "6", "4\n10\n20\n15\n30", "60", 80);
    }

    private void seedAdvanced() {
        Track t = trackRepository.save(Track.builder()
                .title("JavaScript — Avancé").titleEn("JavaScript — Advanced")
                .description("Asynchrone, ES6+ avancé, classes et patterns modernes.")
                .descriptionEn("Async, advanced ES6+, classes and modern patterns.")
                .difficulty(Track.Difficulty.ADVANCED).language(Track.Language.JAVASCRIPT)
                .requiredLevel(1).xpPerLesson(50).build());

        Module m1 = h.createModule(t, 0, "Asynchrone", "Asynchronous",
                "Promises et async/await", "Promises and async/await");
        h.seedLesson(m1, 0, "Promises", "Promises", Lesson.LessonType.THEORY,
            "Promise = valeur future. .then() pour le succès, .catch() pour l'erreur.",
            "Promise = future value. .then() for success, .catch() for errors.",
            "const p = new Promise((resolve, reject) => {\n  setTimeout(() => resolve('OK'), 1000);\n});\np.then(data => console.log(data))\n .catch(err => console.error(err));", "javascript",
            "Une Promise est pending, fulfilled ou rejected.",
            "A Promise is pending, fulfilled or rejected.");
        h.seedLesson(m1, 1, "async/await", "async/await", Lesson.LessonType.PRACTICE,
            "async/await = sucre syntaxique sur les Promises. Le code semble synchrone.",
            "async/await = syntactic sugar over Promises. The code looks synchronous.",
            "async function charger() {\n  try {\n    const res = await fetch('/api');\n    const data = await res.json();\n    console.log(data);\n  } catch (err) {\n    console.error(err);\n  }\n}", "javascript",
            "await ne peut être utilisé que dans une fonction async.",
            "await can only be used inside an async function.");
        h.seedLesson(m1, 2, "JSON et APIs", "JSON and APIs", Lesson.LessonType.PRACTICE,
            "JSON.parse() → objet, JSON.stringify() → chaîne. Essentiel pour les API REST.",
            "JSON.parse() → object, JSON.stringify() → string. Essential for REST APIs.",
            "const obj = { nom: 'Alice' };\nconst json = JSON.stringify(obj);\nconst parsed = JSON.parse(json);\nconsole.log(parsed.nom);", "javascript",
            "fetch() retourne une Promise — utilisez .json() pour les données.",
            "fetch() returns a Promise — use .json() for the data.");
        h.seedQuiz(m1, 3, "Quiz: Async", "Quiz: Async", new String[][] {
            {"Qu'est-ce qu'une Promise ?", "What is a Promise?", "Variable", "Variable", "Valeur future", "Future value", "Boucle", "Loop", "Erreur", "Error", "2", "Représente une opération asynchrone.", "Represents an asynchronous operation."},
            {"Où utiliser await ?", "Where to use await?", "N'importe où", "Anywhere", "Dans une fonction async", "In an async function", "Dans une boucle", "In a loop", "Dans un callback", "In a callback", "2", "await est valide dans une fonction async.", "await is valid in an async function."},
        });
        h.seedChallenge(m1, "Fibonacci JS", "Fibonacci JS",
            "Lisez N, affichez le N-ème nombre de Fibonacci.", "Read N, print the N-th Fibonacci number.",
            "JAVASCRIPT", "INTERMEDIATE", "", RL+"let n=parseInt(L[0]),a=0,b=1;for(let i=0;i<n;i++){[a,b]=[b,a+b];}console.log(a);"+END, "6", "8", "10", "55", 50);
        h.seedChallenge(m1, "Palindrome JS", "Palindrome JS",
            "Lisez un mot, affichez 'true' si palindrome.", "Read a word, print 'true' if palindrome.",
            "JAVASCRIPT", "ADVANCED", "", RL+"const s=L[0].trim().toLowerCase();console.log(s===s.split('').reverse().join(''));"+END, "kayak", "true", "hello", "false", 80);

        Module m2 = h.createModule(t, 1, "ES6+ Moderne", "Modern ES6+",
                "Classes, Map/Set et features modernes", "Classes, Map/Set and modern features");
        h.seedLesson(m2, 0, "Classes", "Classes", Lesson.LessonType.THEORY,
            "class = sucre syntaxique sur les prototypes. constructor, méthodes, extends pour l'héritage.",
            "class = syntactic sugar over prototypes. constructor, methods, extends for inheritance.",
            "class Animal {\n  constructor(nom) { this.nom = nom; }\n  parler() { return `${this.nom} fait du bruit`; }\n}\nclass Chien extends Animal {\n  parler() { return `${this.nom} aboie`; }\n}", "javascript",
            "extends pour hériter, super() pour le constructeur parent.",
            "extends to inherit, super() for the parent constructor.");
        h.seedLesson(m2, 1, "Map et Set", "Map and Set", Lesson.LessonType.PRACTICE,
            "Map = clé-valeur (n'importe quel type de clé). Set = valeurs uniques.",
            "Map = key-value (any key type). Set = unique values.",
            "const m = new Map();\nm.set('a', 1);\nconsole.log(m.get('a')); // 1\n\nconst s = new Set([1, 2, 2, 3]);\nconsole.log(s.size); // 3", "javascript",
            "Set élimine les doublons. Map est plus flexible que {}.",
            "Set eliminates duplicates. Map is more flexible than {}.");
        h.seedLesson(m2, 2, "Optional chaining et nullish coalescing", "Optional chaining and nullish coalescing", Lesson.LessonType.PRACTICE,
            "?. évite les erreurs sur null. ?? donne un défaut seulement pour null/undefined.",
            "?. avoids errors on null. ?? gives a default only for null/undefined.",
            "const user = { nom: 'Alice', adr: null };\nconsole.log(user.adr?.ville);     // undefined\nconsole.log(user.bio ?? 'N/A');  // 'N/A'", "javascript",
            "?? est différent de || : 0 et '' sont valides avec ??.",
            "?? is different from || : 0 and '' are valid with ??.");
        h.seedQuiz(m2, 3, "Quiz: ES6+", "Quiz: ES6+", new String[][] {
            {"extends fait quoi ?", "What does extends do?", "Allonge un string", "Extends a string", "Héritage de classes", "Class inheritance", "Étend un array", "Extends an array", "Exporte", "Exports", "2", "extends établit l'héritage.", "extends establishes inheritance."},
            {"new Set([1,1,2,3]) donne ?", "new Set([1,1,2,3]) gives?", "Erreur", "Error", "{1, 2, 3}", "{1, 2, 3}", "[1,1,2,3]", "[1,1,2,3]", "{1:1, 2:2}", "{1:1, 2:2}", "2", "Set élimine les doublons.", "Set eliminates duplicates."},
            {"null ?? 'défaut' retourne ?", "null ?? 'default' returns?", "null", "null", "'défaut'", "'default'", "undefined", "undefined", "false", "false", "2", "?? retourne le côté droit si gauche est null/undefined.", "?? returns the right side if left is null/undefined."},
        });
        h.seedChallenge(m2, "Doublons JS", "Duplicates JS",
            "Lisez N nombres, affichez les uniques dans l'ordre d'apparition.", "Read N numbers, print unique ones in order of appearance.",
            "JAVASCRIPT", "INTERMEDIATE", "", RL+"const n=parseInt(L[0]);const seen=new Set();for(let i=1;i<=n;i++){const v=parseInt(L[i]);if(!seen.has(v)){console.log(v);seen.add(v);}}"+END,
            "6\n1\n2\n3\n2\n1\n4", "1\n2\n3\n4", "4\n5\n5\n3\n3", "5\n3", 50);
        h.seedChallenge(m2, "Tri croissant JS", "Ascending sort JS",
            "Lisez N nombres, affichez-les triés.", "Read N numbers, print them sorted.",
            "JAVASCRIPT", "ADVANCED", "", RL+"const n=parseInt(L[0]);const a=[];for(let i=1;i<=n;i++)a.push(parseInt(L[i]));a.sort((x,y)=>x-y);a.forEach(x=>console.log(x));"+END,
            "5\n3\n1\n4\n1\n5", "1\n1\n3\n4\n5", "3\n9\n2\n7", "2\n7\n9", 80);
    }
}
