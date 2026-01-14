# reclamation-service

Le **service de gestion des réclamations** est un microservice métier chargé du **cycle de vie des réclamations** au sein de la plateforme.

Il n’est **pas exposé directement** aux clients et est accessible **uniquement via l’API Gateway** (auth-gateway-service).

Ce service a été développé avec **Spring Boot**

---

## Rôles et responsabilités

- Création des réclamations
- Suivi et traitement des réclamations
-Gestion du cycle de vie des reclamations 

---

## Architecture

- **Type** : Microservice métier
- **Exposition** : Via API Gateway uniquement
- **Communication** : REST
- **Sécurité** : JWT (validé par la Gateway)
- **Découverte de services** : JHipster Registry
- **Base de données** : PostgreSQL

---

## Dépendances

- **auth-gateway-service**
- **JHipster Registry**
- Base de données relationnelle

---

## Sécurité

- Toutes les requêtes entrantes proviennent de la Gateway
- Le token JWT est déjà validé
- Les autorisations sont contrôlées par rôles et règles métier

---

## API principales (internes)

- `POST /api/reclamations`
- `GET /api/reclamations`
- `GET /api/reclamations/{id}`
- `PUT /api/reclamations/{id}`
- `DELETE /api/reclamations/{id}`

---

## Lancement
./mvnw

---

## Tests

./mvnw verify

---

## Construction production

./mvnw -Pprod clean verify
java -jar target/*.jar


---

## Remarque

Ce microservice implémente la **logique métier principale** et dépend entièrement :
- de la Gateway pour l’exposition,
- du service d’authentification pour la sécurité.
