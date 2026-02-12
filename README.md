# SoChief API — README

Ce dépôt contient l’API **SoChief** (Spring Boot + Kotlin), avec une stack locale Docker (PostgreSQL + MinIO + API).
L’API dépend d’un package Maven hébergé sur **GitHub Packages** : `com.vlegall:contracts-desktop:${contracts.version}`.

> ⚠️ Important : GitHub Packages (registry Maven) **nécessite une authentification** pour télécharger des dépendances.
> C’est pour ça qu’on configure soit `~/.m2/settings.xml`, soit `maven.settings.xml` (utilisé pendant le build Docker).

---

## 1) Prérequis

### Outils nécessaires (local)
- **Git**
- **Java 21** (Temurin recommandé) — nécessaire uniquement si tu builds en local avec Maven
- **Maven 3.9+** — si tu builds en local (hors Docker)
- **Docker Desktop** (avec BuildKit / buildx) + **docker compose**
- **Make**
  - Windows : via Git Bash / MSYS2 / WSL (ou `make` installé)

### Accès GitHub Packages
Tu as besoin d’un **GitHub Personal Access Token** (PAT) avec au minimum :
- `read:packages`

> Si tu utilises un “fine-grained token”, il faut en plus lui donner accès au repo qui contient les packages, et le droit “Packages: Read”.
> Le plus simple : un **PAT classic** avec `read:packages`.

---

## 2) Récupérer le code

```bash
git clone <URL_DU_DEPOT>
cd SoChief
```

---

## 3) Build Maven en local (sans Docker) — via `~/.m2/settings.xml`

Si tu veux faire :

```bash
mvn clean package
```

Alors il faut configurer Maven pour accéder à GitHub Packages.

### 3.1 Créer `~/.m2/settings.xml`

#### Windows
Chemin : `C:\Users\<TonUser>\.m2\settings.xml`

#### Linux / macOS
Chemin : `~/.m2/settings.xml`

Contenu :

```xml
<settings xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <!-- Doit correspondre au <repository><id>github</id> dans le pom.xml -->
      <id>github</id>
      <username>GITHUB_USERNAME</username>
      <password>GITHUB_READ_TOKEN</password>
    </server>
  </servers>
</settings>
```

### 3.2 Build
```bash
mvn -B clean package
```

Le jar sera généré dans :
- `target/*.jar`

---

## 4) Lancer en local avec Docker (stack complète) — via `make`

La stack locale utilise :
- PostgreSQL 16
- MinIO
- SoChief API (build depuis le Dockerfile)

### 4.1 Créer `.env.local`
À partir de `.env.local.example`, crée un fichier `.env.local` et remplis-le :

```env
POSTGRES_USER=
POSTGRES_PASSWORD=
POSTGRES_DB=
POSTGRES_PORT=

MINIO_ROOT_USER=
MINIO_ROOT_PASSWORD=
MINIO_BUCKET=
#Uniquement local
MINIO_PORT=
MINIO_INTERFACE_PORT=

API_KEYS="clé_1, clé_2, etc."

APP_PUBLIC_API_EXPOSED_PORT=
```

Exemple minimal (à adapter) :

```env
POSTGRES_USER=sochief
POSTGRES_PASSWORD=sochief
POSTGRES_DB=sochief
POSTGRES_PORT=5432

MINIO_ROOT_USER=minio
MINIO_ROOT_PASSWORD=minio123456
MINIO_BUCKET=sochief
MINIO_PORT=9000
MINIO_INTERFACE_PORT=9001

API_KEYS="dev_key_1,dev_key_2"

APP_PUBLIC_API_EXPOSED_PORT=8080
```

### 4.2 Créer `maven.settings.xml` (utilisé par le build Docker)
À la racine du projet, crée (ou copie) le fichier `maven.settings.xml` :

```xml
<settings xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
          xmlns="http://maven.apache.org/SETTINGS/1.0.0"
          xsi:schemaLocation="http://maven.apache.org/SETTINGS/1.0.0
                              https://maven.apache.org/xsd/settings-1.0.0.xsd">
  <servers>
    <server>
      <id>github</id>
      <username>GITHUB_USERNAME</username>
      <password>GITHUB_READ_TOKEN</password>
    </server>
  </servers>
</settings>
```

> ✅ Ce fichier sert uniquement à permettre au **Dockerfile** de télécharger `contracts-desktop` pendant le build.
> ⚠️ Ne le commit pas : ajoute-le à ton `.gitignore` :
>
> ```
> maven.settings.xml
> ```

### 4.3 Commandes Make utiles

#### Démarrer la stack (sans forcer rebuild)
```bash
make up-local
```

#### Rebuild complet + reset volumes + rebuild image avec secret Maven (recommandé)
```bash
make nuke-local
```

#### Logs
```bash
make logs-local
```

#### Stop / Down
```bash
make stop-local
make down-local
```

### 4.4 URLs utiles (local)
- API : `http://localhost:<APP_PUBLIC_API_EXPOSED_PORT>`
- MinIO Console : `http://localhost:<MINIO_INTERFACE_PORT>`
- MinIO API : `http://localhost:<MINIO_PORT>`
- PostgreSQL : `localhost:<POSTGRES_PORT>`

---

## 5) Exécuter l’image Docker générée par la CI/CD (sans builder le code)

La CI/CD publie une image Docker (exemple) sur GHCR :

- `ghcr.io/<ORG>/sochief-api:<VERSION>`
- `ghcr.io/<ORG>/sochief-api:latest`

> Remplace `<ORG>` par le owner GitHub (organisation ou user) qui héberge le dépôt.

### 5.1 Pull de l’image
```bash
docker pull ghcr.io/<ORG>/sochief-api:latest
```

#### Si l’image est privée
Tu dois te connecter à GHCR :

```bash
echo <TOKEN_GITHUB> | docker login ghcr.io -u <GITHUB_USERNAME> --password-stdin
```

Le token doit avoir `read:packages`.

### 5.2 Lancer l’image avec Docker Compose (recommandé)
Le plus simple : utiliser **la même stack compose** mais remplacer `build:` par `image:`.

#### Option A — fichier compose dédié “runtime image”
Crée un fichier `docker-compose.runtime.yml` :

```yaml
services:
  sochief-api:
    image: ghcr.io/<ORG>/sochief-api:latest
    build: null
```

Puis lance :

```bash
docker compose \
  -p sochief-local \
  -f docker-compose.base.yml \
  -f docker-compose.local.yml \
  -f docker-compose.runtime.yml \
  --env-file .env.local \
  up -d
```

✅ Avantage : tu réutilises PostgreSQL + MinIO + toutes les variables `.env.local`  
✅ Tu n’as pas besoin de `maven.settings.xml` (puisqu’on ne build pas)

---

## 6) Dépannage

### 401 Unauthorized pendant un build (Maven / Docker)
Cause : Maven n’a pas eu accès à GitHub Packages.

Vérifie :
- Le token a `read:packages`
- Le `<id>github</id>` match bien dans :
  - `pom.xml` (`<repository><id>github</id>`)
  - `~/.m2/settings.xml` (build local Maven)
  - `maven.settings.xml` (build Docker)
- Si tu utilises `make nuke-local`, il faut que `maven.settings.xml` existe à la racine.

### Docker build très lent
C’est normal au premier build. Les builds suivants seront plus rapides grâce aux caches Docker/BuildKit (sauf si `--no-cache`).

---

## 7) Fichiers importants (rappel)

- `.env.local` : variables runtime (DB, MinIO, ports, API keys)
- `maven.settings.xml` : credentials Maven **uniquement pour builder l’image Docker**
- `~/.m2/settings.xml` : credentials Maven **pour build Maven en local**
- `docker-compose.base.yml` + `docker-compose.local.yml` : stack locale
- `Dockerfile` : build + packaging jar dans l’image