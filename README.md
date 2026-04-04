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
mvn -version        # 3.8+
docker --version    # 20+
```

### 2. Lancer le backend

```powershell
# Ouvrir PowerShell dans le dossier codequest-backend

# Démarrer PostgreSQL + Redis via Docker
docker compose up -d postgres redis

# Construire les images sandbox (une seule fois)
docker build -t codequest-python docker/python/
docker build -t codequest-node docker/node/
docker build -t codequest-java docker/java/

# Lancer le backend
mvn spring-boot:run
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

Ouvre [http://localhost:5173](http://localhost:5173) — Login : `admin@codequest.com` / `Admin123!`

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
java -version && mvn -version && docker --version
```

### 2. Lancer le backend

```bash
cd codequest-backend

docker compose up -d postgres redis

docker build -t codequest-python docker/python/
docker build -t codequest-node docker/node/
docker build -t codequest-java docker/java/

mvn spring-boot:run
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
# Java 17 + Maven
sudo pacman -S jdk17-openjdk maven

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
java -version && mvn -version && docker --version
```

### 2. Lancer le backend

```bash
cd codequest-backend

docker compose up -d postgres redis

docker build -t codequest-python docker/python/
docker build -t codequest-node docker/node/
docker build -t codequest-java docker/java/

mvn spring-boot:run
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
| `docker compose up -d postgres redis` | Containers démarrés sans erreur |
| `mvn spring-boot:run` | `Started CodequestBackendApplication` dans les logs |
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
| `google-services.json not found` | Le fichier est déjà inclus dans `codequest-android/app/`. Si manquant, demande-le |
| Emails OTP non reçus | Normal en local — les identifiants email ne sont pas configurés. L'inscription fonctionne quand même |

---

##  Technologies

- **Backend :** Spring Boot 3, PostgreSQL, Redis, Docker (sandboxing)
- **Mobile :** Android natif (Java), Material Design 3
- **Admin :** React + Vite
- **Langages supportés :** Python, JavaScript, Java
