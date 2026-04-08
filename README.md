# CodeQuest — Guide d'Installation

Application d'apprentissage de programmation (Python, JavaScript, Java) avec gamification.

> **Prérequis :** Docker Desktop (Windows) ou Docker Engine (Linux), Android Studio, Java 17+, Maven 3.8+

---

##  Structure du Projet

```
codequest/
├── codequest-backend/    ← API Spring Boot + Docker Compose (Postgres, Redis)
├── codequest-android/    ← Application mobile Android
└── codequest-admin/      ← Dashboard admin React (optionnel)
```

---

##  Installation — Windows

### 1. Installer les outils

| Outil | Téléchargement |
|---|---|
| **Java JDK 17+** | [adoptium.net](https://adoptium.net/) |
| **Maven 3.8+** | [maven.apache.org](https://maven.apache.org/download.cgi) |
| **Docker Desktop** | [docker.com](https://www.docker.com/products/docker-desktop/) |
| **Android Studio** | [developer.android.com](https://developer.android.com/studio) |
| **Node.js 18+** | [nodejs.org](https://nodejs.org/) *(uniquement pour le dashboard admin)* |

Vérifier :
```powershell
java -version       # 17+ 
docker --version    # 20+
```

### 2. Lancer le backend

```powershell
# Ouvrir PowerShell dans le dossier codequest-backend

# Construire les images sandbox (une seule fois)
docker build -t codequest-python docker/python/
docker build -t codequest-node docker/node/
docker build -t codequest-java docker/java/

# Lancer tout (Postgres + Redis + Backend) en une seule commande
docker compose up --build
```

Tu devrais voir : `Started CodequestBackendApplication in X seconds`

### 3. Lancer l'app Android

1. Ouvre le dossier `codequest-android` dans **Android Studio**
2. Attends la fin du **Gradle Sync**
3. Sélectionne un **émulateur** (recommandé) ou un téléphone physique
4. Clique **▶ Run**

> *** Téléphone physique ?** L'émulateur fonctionne directement. Pour un téléphone physique connecté au même WiFi, modifie `BASE_URL` dans `codequest-android/app/src/main/java/com/codequest/util/Constants.java` avec l'IP de ton PC (tapez `ipconfig` dans PowerShell pour la trouver) :
> ```java
> public static final String BASE_URL = "http://192.168.X.X:8080/";
> ```

### 4. Dashboard admin (optionnel)

```powershell
cd codequest-admin
npm install
npm run dev
```

Ouvre [http://localhost:5173](http://localhost:5173) — Login : `admin@codequest.com` / `admin123`

---

##  Installation — Debian / Ubuntu

### 1. Installer les outils

```bash
# Java 17 + Maven
sudo apt update
sudo apt install -y openjdk-17-jdk maven

# Docker
sudo apt install -y ca-certificates curl gnupg
sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/debian/gpg | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] https://download.docker.com/linux/debian $(. /etc/os-release && echo "$VERSION_CODENAME") stable" | sudo tee /etc/apt/sources.list.d/docker.list
sudo apt update
sudo apt install -y docker-ce docker-ce-cli containerd.io docker-compose-plugin

# Ajouter ton user au groupe docker (évite sudo)
sudo usermod -aG docker $USER
newgrp docker

# Node.js (pour le dashboard admin uniquement)
curl -fsSL https://deb.nodesource.com/setup_18.x | sudo -E bash -
sudo apt install -y nodejs

# Android Studio — télécharger depuis developer.android.com
```

> **Ubuntu :** Remplacer `debian` par `ubuntu` dans l'URL Docker ci-dessus.

Vérifier :
```bash
java -version && docker --version
```


### 2. Lancer le backend

```bash
cd codequest-backend

# Construire les images sandbox (une seule fois)
docker build -t codequest-python docker/python/
docker build -t codequest-node docker/node/
docker build -t codequest-java docker/java/

# Lancer tout (Postgres + Redis + Backend) en une seule commande
docker compose up --build
```

### 3. Lancer l'app Android

Même procédure que Windows — ouvrir `codequest-android` dans Android Studio → Run.

### 4. Dashboard admin (optionnel)

```bash
cd codequest-admin && npm install && npm run dev
```

---

##  Installation — Arch Linux

### 1. Installer les outils

```bash
# Java 17 (pour Android Studio uniquement)
sudo pacman -S jdk17-openjdk

# Docker
sudo pacman -S docker docker-compose
sudo systemctl enable --now docker
sudo usermod -aG docker $USER
newgrp docker

# Node.js (pour le dashboard admin uniquement)
sudo pacman -S nodejs npm

# Android Studio
yay -S android-studio   # ou depuis le AUR manuellement
```

Vérifier :
```bash
java -version && docker --version
```

### 2. Lancer le backend

```bash
cd codequest-backend

# Construire les images sandbox (une seule fois)
docker build -t codequest-python docker/python/
docker build -t codequest-node docker/node/
docker build -t codequest-java docker/java/

# Lancer tout (Postgres + Redis + Backend) en une seule commande
docker compose up --build
```

### 3. Lancer l'app Android

Même procédure — ouvrir `codequest-android` dans Android Studio → Run.

### 4. Dashboard admin (optionnel)

```bash
cd codequest-admin && npm install && npm run dev
```

---

##  Checklist de Vérification

| Test | Résultat attendu |
|---|---|
| `docker compose up --build` | Postgres + Redis + Backend démarrés sans erreur |
| `curl http://localhost:8080/actuator/health` | `{"status":"UP"}` |
| App Android → écran de login | S'affiche sans erreur |
| Inscription → les tracks s'affichent | 9 parcours (3 langages × 3 difficultés) |

---

##  Comptes par Défaut

| Compte | Email | Mot de passe | Utilisation |
|---|---|---|---|
| **Admin** | `admin@codequest.com` | `admin123` | Dashboard admin |
| **Nouveau compte** | *ton email* | *ton choix* | Inscription depuis l'app mobile |

---

##  Problèmes Courants

| Problème | Solution |
|---|---|
| `Connection refused` sur le mobile | Backend pas lancé, ou mauvaise IP dans `Constants.java` |
| `Port 5432 already in use` | PostgreSQL déjà installé localement — arrête-le ou change le port Docker |
| `Port 8080 already in use` | Un autre serveur tourne sur 8080 — arrête-le |
| Gradle sync échoue | Vérifie ta connexion internet — Android Studio télécharge les dépendances |
| `COPY failed: no source files` | Erreur Docker résolue — le Dockerfile compile Maven lui-même, aucune action requise |
| `google-services.json not found` | Demande le fichier au propriétaire du projet |
| `Emails OTP non reçus` | Normal en local — les identifiants email ne sont pas configurés. L'inscription fonctionne quand même |

---

##  Fichiers Sensibles & Configuration Google Sign-In

> Ces fichiers contiennent des clés privées et ne sont **pas** dans le dépôt GitHub. Le propriétaire du projet doit les envoyer manuellement 

### Fichiers à demander au propriétaire

| Fichier | Où le placer |
|---|---|
| `firebase-service-account.json` | `codequest-backend/src/main/resources/` |
| `google-services.json` (mis à jour) | `codequest-android/app/` |

### Activer Google Sign-In sur votre machine

Le bouton "Continuer avec Google" nécessite que l'empreinte de **votre PC** soit enregistrée dans Firebase. Sans cela, le reste de l'app fonctionne normalement .

**Étape 1 — Obtenir vos empreintes SHA :**

```bash
# Linux / Mac — depuis le dossier codequest-android
./gradlew signingReport

# Ou via keytool directement
keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android -keypass android
```

```powershell
# Windows PowerShell — depuis le dossier codequest-android
.\gradlew signingReport

# Ou via keytool
keytool -list -v -keystore $env:USERPROFILE\.android\debug.keystore -alias androiddebugkey -storepass android -keypass android
```

Notez les deux lignes suivantes dans l'output :
```
SHA1:   XX:XX:XX:XX:...
SHA-256: XX:XX:XX:XX:...
```

**Étape 2 — Les envoyer au propriétaire du projet** qui les ajoutera dans la [Firebase Console](https://console.firebase.google.com/) → Paramètres du projet → votre app Android.

**Étape 3 — Récupérer le `google-services.json` mis à jour** et le placer dans `codequest-android/app/`.

---

##  Technologies

- **Backend :** Spring Boot 3, PostgreSQL, Redis, Docker (sandboxing)
- **Mobile :** Android natif (Java), Material Design 3
- **Admin :** React + Vite
- **Langages supportés :** Python, JavaScript, Java
