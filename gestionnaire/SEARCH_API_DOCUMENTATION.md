# 🔍 API Recherche Véhicules - Documentation

## Architecture
La recherche est **routée par type de véhicule** selon la structure de base de données :
- **AUTOMOBILE** → Recherche dans `automobile` table et ses sous-types
- **SCOOTER** → Recherche dans `scooter` table et ses sous-types

## ✅ Avantages
✅ Alignée avec la base de données  
✅ Aucun mélange de types  
✅ Recherche rapide et fiable  
✅ Opérateurs ET/OU logiques  

---

## 📡 Endpoints REST

### 1️⃣ Recherche AUTOMOBILE
**URL:** `POST /api/recherche/automobiles`

**Body:**
```json
{
  "motsCles": ["toyota", "corolla"],
  "operateur": "ET"
}
```

**Réponse:** Liste d'automobiles avec `typeVehicule` basé sur le type réel (AUTO_ESSENCE, AUTO_ELECTRIQUE)

---

### 2️⃣ Recherche SCOOTER
**URL:** `POST /api/recherche/scooters`

**Body:**
```json
{
  "motsCles": ["piaggio", "vespa"],
  "operateur": "OU"
}
```

**Réponse:** Liste de scooters avec `typeVehicule` basé sur le type réel (SCOOTER_ESSENCE, SCOOTER_ELECTRIQUE)

---

### 3️⃣ Recherche Routée par Type (Recommandé)
**URL:** `POST /api/recherche`

**Body:**
```json
{
  "typeVehicule": "AUTOMOBILE",
  "motsCles": ["bmw"],
  "operateur": "ET"
}
```

**Alternative avec query param:**
```
POST /api/recherche/type?type=AUTOMOBILE
```

**Erreurs possibles:**
- `400 Bad Request` si `typeVehicule` est vide
- `400 Bad Request` si `typeVehicule` n'est pas AUTOMOBILE ou SCOOTER

---

## 📊 Format de Réponse

### ✅ Réponse Automobile
```json
[
  {
    "id": 1,
    "reference": "AUTO-2025-001",
    "marque": "Toyota",
    "modele": "Corolla",
    "prixBase": 25000000,
    "nombrePortes": 4,
    "espaceCoffre": 400.0,
    "typeVehicule": "AUTO_ESSENCE",
    "description": "Berline compacte efficace",
    "imageLink": "http://localhost:8080/uploads/..."
  }
]
```

### ✅ Réponse Scooter
```json
[
  {
    "id": 5,
    "reference": "SCOOTER-2025-001",
    "marque": "Piaggio",
    "modele": "Vespa",
    "prixBase": 5000000,
    "cylindree": 300,
    "typeVehicule": "SCOOTER_ESSENCE",
    "description": "Scooter classique mythique",
    "imageLink": "http://localhost:8080/uploads/..."
  }
]
```

---

## 🔄 Opérateurs Logiques

### **ET** (AND)
Retourne les véhicules contenant **TOUS** les mots-clés

```json
{
  "typeVehicule": "AUTOMOBILE",
  "motsCles": ["toyota", "corolla"],
  "operateur": "ET"
}
// Retourne: Véhicules qui contiennent "toyota" ET "corolla"
```

### **OU** (OR) - Défaut
Retourne les véhicules contenant **AU MOINS UN** mot-clé

```json
{
  "typeVehicule": "AUTOMOBILE",
  "motsCles": ["bmw", "audi"],
  "operateur": "OU"
}
// Retourne: Véhicules qui contiennent "bmw" OU "audi"
```

---

## 🚫 CE QU'IL NE FAUT PAS FAIRE

❌ Mélanger AUTOMOBILE et SCOOTER dans une seule requête  
❌ Recherche globale sans type spécifié  
❌ Fusionner les résultats après coup  
❌ Déduire le type côté frontend  

---

## 🔧 Implémentation Côté Frontend

### Exemple avec Axios

```javascript
// Recherche Automobile
async function searchAutomobiles(keywords, operator = "OU") {
  try {
    const response = await axios.post('/api/recherche/automobiles', {
      motsCles: keywords,
      operateur: operator
    });
    console.log("Automobiles trouvées:", response.data);
  } catch (error) {
    console.error("Erreur recherche:", error.response.data);
  }
}

// Recherche Scooter
async function searchScooters(keywords, operator = "OU") {
  try {
    const response = await axios.post('/api/recherche/scooters', {
      motsCles: keywords,
      operateur: operator
    });
    console.log("Scooters trouvés:", response.data);
  } catch (error) {
    console.error("Erreur recherche:", error.response.data);
  }
}

// Recherche Routée (Recommandé)
async function searchByType(type, keywords, operator = "OU") {
  try {
    const response = await axios.post('/api/recherche', {
      typeVehicule: type,
      motsCles: keywords,
      operateur: operator
    });
    console.log("Résultats:", response.data);
  } catch (error) {
    console.error("Erreur:", error.response.data);
  }
}

// Utilisation
searchByType("AUTOMOBILE", ["toyota"]);
searchByType("SCOOTER", ["vespa", "piaggio"], "OU");
```

---

## 📝 Endpoints Hérités (Toujours Disponibles)

Ces endpoints fonctionnent mais ne routent pas par type :

- `GET /api/recherche/simple?motCle=toyota` → Recherche globale
- `POST /api/recherche/avancee` → Recherche globale avancée
- `GET /api/recherche/marque/{marque}` → Par marque globale
- `POST /api/recherche/filtres` → Filtres globaux

---

## 🎯 Cas d'Usage Recommandés

### 1. Recherche Automobile Simple
```
POST /api/recherche/automobiles
{ "motsCles": ["toyota"] }
```

### 2. Recherche Multi-critères Automobile
```
POST /api/recherche
{
  "typeVehicule": "AUTOMOBILE",
  "motsCles": ["sport"],
  "operateur": "ET",
  "marque": "Ferrari"
}
```

### 3. Recherche Scooter Économique
```
POST /api/recherche/scooters
{
  "motsCles": ["electrique"],
  "operateur": "OU"
}
```

---

## 🔐 Validations

| Champ | Obligatoire | Format | Notes |
|-------|-------------|--------|-------|
| `typeVehicule` | Oui (dans /api/recherche) | AUTOMOBILE \| SCOOTER | Sensible à la casse |
| `motsCles` | Non | Array[String] | Vide = retourne tous |
| `operateur` | Non | ET \| OU | Défaut: OU |
| `marque` | Non | String | Optionnel, insensible à la casse |
| `modele` | Non | String | Optionnel, insensible à la casse |

---

## 📊 Exemples cURL

### Automobile
```bash
curl -X POST http://localhost:8080/api/recherche/automobiles \
  -H "Content-Type: application/json" \
  -d '{
    "motsCles": ["toyota", "corolla"],
    "operateur": "ET"
  }'
```

### Scooter
```bash
curl -X POST http://localhost:8080/api/recherche/scooters \
  -H "Content-Type: application/json" \
  -d '{
    "motsCles": ["piaggio"],
    "operateur": "OU"
  }'
```

### Routé par Type
```bash
curl -X POST http://localhost:8080/api/recherche \
  -H "Content-Type: application/json" \
  -d '{
    "typeVehicule": "AUTOMOBILE",
    "motsCles": ["bmw"],
    "operateur": "ET"
  }'
```
