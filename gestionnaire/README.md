# Gestion Véhicule - Guide Démarrage & Test

## Prérequis

- **Java 21** ou supérieur
- **PostgreSQL 14** ou supérieur (base de données `gestion_vehicule`)
- **Maven 3.8+** (ou utiliser `./mvnw` inclus)
- **curl** ou **Postman** pour tester les APIs

## Étape 1: Préparation de la Base de Données

```bash
# Créer la base de données PostgreSQL
psql -U postgres -c "CREATE DATABASE gestion_vehicule ENCODING 'UTF8';"

# (Optionnel) Créer un utilisateur dédié
psql -U postgres -c "CREATE USER vehicule_user WITH PASSWORD 'password';"
psql -U postgres -c "GRANT ALL PRIVILEGES ON DATABASE gestion_vehicule TO vehicule_user;"
```

## Étape 2: Configuration

Éditer `src/main/resources/application.properties`:

```properties
# DataSource
spring.datasource.url=jdbc:postgresql://localhost:5432/gestion_vehicule
spring.datasource.username=postgres
spring.datasource.password=YOUR_PASSWORD

# JPA/Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Server
server.port=8000
```

## Étape 3: Compiler et Lancer

```bash
cd /path/to/gestionnaire

# Compiler
./mvnw clean compile

# Tests unitaires
./mvnw test

# Package (build JAR)
./mvnw package

# Lancer l'application
java -jar target/gestionnaire.jar
```

Le serveur démarre sur `http://localhost:8000`

## Étape 4: Test d'Intégration Complet (Panier → Commande)

### 4.1 Créer un Client

```bash
curl -X POST http://localhost:8000/api/client \
  -H 'Content-Type: application/json' \
  -d '{
    "nom":"Jean Dupont",
    "email":"jean@example.com",
    "telephone":"06 12 34 56 78",
    "isSociete":false
  }'
```

**Réponse**: `{"id":1, "nom":"Jean Dupont", ...}`

**→ Noter l'ID du client: `1`**

### 4.2 Créer un Véhicule

```bash
curl -X POST http://localhost:8000/api/vehicules \
  -H 'Content-Type: application/json' \
  -d '{
    "reference":"PEUGEOT-208-001",
    "marque":"Peugeot",
    "modele":"208",
    "prixBase":5000000,
    "nombrePortes":4,
    "typeVehicule":"AUTO_ESSENCE"
  }'
```

**Réponse**: `{"id":1, "marque":"Peugeot", "modele":"208", "prixBase":5000000, ...}`

**→ Noter l'ID du véhicule: `1`**

### 4.3 Créer des Options

```bash
# Option 1: Sieges Sport
curl -X POST http://localhost:8000/api/options \
  -H 'Content-Type: application/json' \
  -d '{"nom":"Sieges Sport","prix":250000}'

# Réponse: {"id":1, "nom":"Sieges Sport", "prix":250000}

# Option 2: Sieges Cuir (incompatible avec Sport)
curl -X POST http://localhost:8000/api/options \
  -H 'Content-Type: application/json' \
  -d '{"nom":"Sieges Cuir","prix":350000}'

# Réponse: {"id":2, "nom":"Sieges Cuir", "prix":350000}

# Option 3: Toit Panoramique (compatible)
curl -X POST http://localhost:8000/api/options \
  -H 'Content-Type: application/json' \
  -d '{"nom":"Toit Panoramique","prix":150000}'

# Réponse: {"id":3, "nom":"Toit Panoramique", "prix":150000}
```

### 4.4 Ajouter un Item au Panier

```bash
# Ajouter Peugeot 208 avec Sieges Sport (id=1)
curl -X POST "http://localhost:8000/api/paniers/client/1/items?vehiculeId=1&quantite=1" \
  -H 'Content-Type: application/json' \
  -d '[1]'
```

**Réponse**: 
```json
{
  "id": 1,
  "clientId": 1,
  "items": [
    {
      "id": 1,
      "vehicule": {"id": 1, "marque": "Peugeot", "modele": "208", "prixBase": 5000000, ...},
      "quantite": 1,
      "options": [{"id": 1, "nom": "Sieges Sport", "prix": 250000}]
    }
  ],
  "updatedAt": "2026-01-29T13:00:00Z"
}
```

### 4.5 Consulter le Panier

```bash
curl http://localhost:8000/api/paniers/client/1
```

### 4.6 Passer une Commande

```bash
curl -X POST "http://localhost:8000/api/commande/from-panier/1?payment=COMPTANT&pays=Cameroun"
```

**Réponse**:
```json
{
  "id": 1,
  "dateCommande": "2026-01-29T14:30:00.123456",
  "etat": "EN_COURS",
  "montantTotal": 6258000,
  "paysLivraison": "Cameroun",
  "typePaiement": "COMPTANT",
  "client": {
    "id": 1,
    "nom": "Jean Dupont",
    "email": "jean@example.com"
  },
  "vehicules": [
    {
      "id": 1,
      "marque": "Peugeot",
      "modele": "208",
      "prixBase": 5000000,
      "description": "Automobile Essence Peugeot 208 - 4 portes. Prix de base: 5000000.00 FCFA"
    }
  ]
}
```

### 4.7 Vérifier Panier Vide (après commande)

```bash
curl http://localhost:8000/api/paniers/client/1
```

**Réponse**: `{"items": []}` (panier vidé automatiquement)

---

## Test : Options Incompatibles

```bash
# Essayer ajouter Sieges Sport (id=1) ET Sieges Cuir (id=2)
curl -X POST "http://localhost:8000/api/paniers/client/1/items?vehiculeId=1&quantite=1" \
  -H 'Content-Type: application/json' \
  -d '[1,2]'
```

**Réponse Erreur (500)**:
```json
{
  "timestamp": "2026-01-29T14:35:00Z",
  "status": 500,
  "error": "Internal Server Error",
  "message": "Options incompatibles sélectionnées: Sieges Sport et Sieges Cuir"
}
```

---

## Test : Options Compatibles

```bash
# Ajouter Sieges Sport (id=1) + Toit Panoramique (id=3)
curl -X POST "http://localhost:8000/api/paniers/client/2/items?vehiculeId=1&quantite=1" \
  -H 'Content-Type: application/json' \
  -d '[1,3]'
```

**Réponse (200 OK)**: Panier mis à jour avec 2 options compatibles.

---

## Endpoints Disponibles

### Clients
- `POST /api/client` - Créer un client
- `GET /api/client` - Lister tous les clients

### Véhicules
- `POST /api/vehicules` - Ajouter un véhicule
- `GET /api/vehicules/catalogue` - Afficher catalogue (iterator pattern)
- `GET /api/vehicules/{id}/calculer-prix?optionsIds=1,2` - Calculer prix avec options (decorator)
- `GET /api/vehicules/soldes` - Véhicules en solde
- `PUT /api/vehicules/{id}/solder?pourcentage=10` - Solder un véhicule

### Options
- `POST /api/options` - Créer une option
- `GET /api/options` - Lister toutes les options

### Panier
- `GET /api/paniers/client/{clientId}` - Consulter panier
- `POST /api/paniers/client/{clientId}/items?vehiculeId={id}&quantite=1` - Ajouter item (body: liste d'IDs options)
- `DELETE /api/paniers/client/{clientId}/items/{itemId}` - Supprimer item
- `POST /api/paniers/client/{clientId}/clear` - Vider panier
- `POST /api/paniers/client/{clientId}/undo` - Annuler dernière action

### Commandes
- `POST /api/commande` - Passer commande (manuelle)
- `POST /api/commande/from-panier/{clientId}?payment=COMPTANT|CREDIT&pays=COUNTRY` - **Commande depuis panier**
- `PUT /api/commande/{id}/valider` - Valider commande (génère liasse de documents)

---

## Calcul du Montant

**Formula**:
```
Montant = (PrixBase + ∑OptionsPrix) × Quantité × (1 + TauxInteret) + Taxes

Taxes = Montant × (0.192 si Cameroun, sinon 0.20)
```

**Exemple**:
- Véhicule: 5,000,000 FCFA
- Option (Sieges Sport): +250,000 FCFA
- **Total décoré**: 5,250,000 FCFA
- Paiement: **Comptant** (pas de taux intérêt)
- Taxes (Cameroun 19.2%): 5,250,000 × 0.192 = 1,008,000 FCFA
- **Montant Final**: 5,250,000 + 1,008,000 = **6,258,000 FCFA**

---

## Troubleshooting

### Port 8000 déjà utilisé
```bash
lsof -i :8000
kill <PID>
```

### Erreur de connexion BD
- Vérifier PostgreSQL est lancé: `psql -U postgres`
- Vérifier connexion params dans `application.properties`
- Tables créées automatiquement par Hibernate (ddl-auto=update)

### Erreur "No JTA platform available"
- Normal, information seulement, ne bloque pas les tests

### Swagger/OpenAPI
- Accès: `http://localhost:8000/swagger-ui.html`
- Spec: `http://localhost:8000/v3/api-docs`

---

## Fichiers Clés

- **Panier**: `models/Panier.java`, `models/PanierItem.java`, `services/PanierService.java`, `controller/PanierController.java`
- **Commande**: `services/CommandeService.java`, `controller/CommandeController.java`, `mapper/CommandeMapper.java`
- **Options**: `models/Option.java` (méthode `estCompatible()`)
- **Recherche**: `repository/VehiculeRepository.java` (requête `searchByKeyword()`)

---

## Status Build & Tests

✅ **Maven Build**: SUCCESS  
✅ **Unit Tests**: 1 PASSED (GestionnaireApplicationTests)  
✅ **Integration Tests**: PANIER → COMMANDE flux OK  
✅ **Incompatibility Checks**: Options Sport/Cuir correctement rejetées  

---

**Version**: 0.0.1-SNAPSHOT  
**Date**: 2026-01-29  
**Auteur**: Implémentation Patrons Conception INF4067
