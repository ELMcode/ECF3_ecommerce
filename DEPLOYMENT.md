# Documentation déploiement

Ce document fournit les étapes nécessaires pour déployer l'ensemble de l'application e-commerce à l'aide de Docker et Docker Compose.

## Prérequis

- Docker Engine (version 20.10 ou supérieure)
- Docker Compose (version 2.0 ou supérieure)
- Git

## Architecture de l'application

L'application est composée des services suivants :

1. **Frontend React** - Interface utilisateur de l'application (port 3000)
2. **Microservices Spring Boot** :
   - `authentication-service` - Gestion de l'authentification (port 7001)
   - `common-data-service` - Gestion des données communes (port 9000)
   - `search-suggestion-service` - Service de suggestions de recherche (port 10000)
   - `payment-service` - Gestion des paiements (port 9050)
3. **Services d'infrastructure** :
   - Base de données MySQL (port 3306) pour la persistance des données
   - Cache Redis (port 6379) pour la gestion des sessions et du cache

## Étapes de déploiement

### 1. Clone du dépôt et configuration de l'environnement

```bash
git clone [URL_DU_DEPOT]
cd [NOM_DU_DOSSIER_CLONE]
```

#### Configuration des variables d'environnement

L'application utilise des fichiers `.env` pour la configuration. Des fichiers d'exemple sont fournis :

1. **Fichier `.env.example` principal** (à la racine du projet) : Contient les configurations globales

   ```bash
   # Copier et configurer le fichier d'environnement principal
   cp .env.example .env
   # Modifier les valeurs si nécessaire
   nano .env
   ```

2. **Fichier `.env.example` du client** (dans le dossier `/client`) : Configure le frontend React
   ```bash
   # Copier et configurer le fichier d'environnement du client
   cp client/.env.example client/.env
   # Modifier les valeurs si nécessaire
   nano client/.env
   ```

### 2. Construction et démarrage des conteneurs

Pour déployer tous les services en une seule commande :

```bash
docker compose up -d --build
```

Cette commande va :

1. Construire toutes les images Docker définies dans le docker-compose.yml
2. Créer et démarrer tous les conteneurs
3. Configurer les réseaux et volumes nécessaires

Si vous avez besoin de reconstruire uniquement un service spécifique (par exemple, après des modifications du code) :

```bash
docker compose build [nom-du-service]
docker compose up -d
```

Exemple pour reconstruire le client frontend :

```bash
docker compose build client
docker compose up -d
```

### 3. Vérification de l'état des services

Vous pouvez vérifier que tous les services sont en cours d'exécution avec la commande :

```bash
docker compose ps
```

### 4. Accès à l'application

Une fois tous les services démarrés :

- **Interface utilisateur** : Accédez à l'application via votre navigateur à l'adresse http://localhost:3000

- **API des microservices** (pour les tests directs) :
  - Service d'authentification : http://localhost:7001
  - Service de données communes : http://localhost:9000
  - Service de suggestions de recherche : http://localhost:10000
  - Service de paiement : http://localhost:9050

> **Note** : Les adresses et ports indiqués ci-dessus correspondent aux valeurs par défaut définies dans le fichier `.env.example`. Si vous avez modifié les variables de port dans votre fichier `.env`, vous devez adapter les URLs en fonction de vos configurations personnalisées.

## Notes sur la configuration

### Variables d'environnement

Les variables d'environnement sont configurées dans deux endroits principaux :

1. **Fichier .env à la racine du projet** : Contient les variables utilisées par Docker Compose pour configurer tous les services. Ce fichier définit notamment :

   - Les ports des services
   - Les identifiants de base de données
   - Le mot de passe Redis
   - Les profils d'environnement (dev, prod)

2. **Fichier .env dans le répertoire client** : Configure les variables d'environnement pour le frontend React. Attention : pour que ces variables soient incluses dans le build, le script `build_dev` est utilisé au lieu de `build`.

### Configuration de Redis

Redis est configuré avec un mot de passe dans ce projet. Assurez-vous que :

1. La variable `REDIS_PASSWORD` est définie dans le fichier `.env`
2. Le service Redis dans `docker-compose.yml` est configuré avec l'argument `--requirepass ${REDIS_PASSWORD}`
3. La commande healthcheck de Redis utilise le paramètre `--pass ${REDIS_PASSWORD}`

Cette configuration est essentielle car les services Java tentent de s'authentifier auprès de Redis avec ce mot de passe.

### Persistance des données

Les données sont persistées à l'aide de volumes Docker :

- `mysql-data` : Stocke les données MySQL
- `redis-data` : Stocke les données Redis

Ces volumes persistent même après l'arrêt des conteneurs.

### Redemarrage et arrêt des services

```bash
# Redemarrer un service spécifique
docker compose restart [nom-du-service]

# Arrêter tous les services
docker compose down
```

## Infos techniques

### Frontend React

- Le frontend utilise un build en deux étapes avec Node.js pour la construction et Nginx pour servir l'application
- **Yarn** est utilisé comme gestionnaire de paquets au lieu de npm
- Le Dockerfile exécute `yarn build_dev` pour incorporer les variables d'environnement du fichier `.env` au moment de la compilation

#### Déploiement en mode production

Pour déployer l'application en mode production (au lieu du mode développement) :

1. Modifiez la variable `REACT_APP_ENVIRONMENT=prod` dans les fichiers `.env`
2. Modifiez le Dockerfile du client pour utiliser `yarn build` au lieu de `yarn build_dev`

**Note importante** : En mode production, les variables d'environnement React sont "figées" au moment de la compilation et ne peuvent pas être changées sans recompiler l'application. Pour des environnements variables, vous devrez :

- Soit utiliser un script de substitution de variables au démarrage du conteneur
- Soit utiliser une bibliothèque comme `react-env` qui permet d'injecter des variables d'environnement au runtime

### Microservices Spring Boot

- Les microservices utilisent Java 11 avec Spring Boot 2.3.0
- Le common-data-service utilise Redis pour la mise en cache des données fréquemment accédées
- L'authentication-service gère les JWT pour l'authentification
- Le nouveau search-suggestion-service fournit des suggestions de recherche à partir de la base de données

### Infrastructure

- MySQL 8.0 stocke toutes les données de l'application
- Redis 6.2-alpine sert de cache et de gestionnaire de sessions
