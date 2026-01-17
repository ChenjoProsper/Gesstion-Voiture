# 🧪 Exemples d'Utilisation - API Recherche Alignée DB

## 🚀 Démarrage Rapide

### Installation & Lancement
```bash
# Compiler
mvn clean package -DskipTests

# Lancer l'application
java -jar target/gestionnaire.jar

# L'API sera disponible à : http://localhost:8080
```

---

## 📡 Exemples cURL

### 1️⃣ Recherche AUTOMOBILE Simple
```bash
curl -X POST http://localhost:8080/api/recherche/automobiles \
  -H "Content-Type: application/json" \
  -d '{
    "motsCles": ["toyota"]
  }'
```

**Réponse:**
```json
[
  {
    "id": 1,
    "marque": "Toyota",
    "modele": "Corolla",
    "prixBase": 25000000,
    "typeVehicule": "AUTO_ESSENCE",
    "description": "Berline compacte fiable"
  }
]
```

---

### 2️⃣ Recherche AUTOMOBILE Avancée (ET)
```bash
curl -X POST http://localhost:8080/api/recherche/automobiles \
  -H "Content-Type: application/json" \
  -d '{
    "motsCles": ["bmw", "sport"],
    "operateur": "ET"
  }'
```

**Explique:** Retourne les automobiles qui contiennent **BMW** ET **SPORT**

---

### 3️⃣ Recherche SCOOTER (OU)
```bash
curl -X POST http://localhost:8080/api/recherche/scooters \
  -H "Content-Type: application/json" \
  -d '{
    "motsCles": ["vespa", "piaggio"],
    "operateur": "OU"
  }'
```

**Explique:** Retourne les scooters qui contiennent **VESPA** OU **PIAGGIO**

---

### 4️⃣ Recherche Routée par Type (RECOMMANDÉ)
```bash
curl -X POST http://localhost:8080/api/recherche \
  -H "Content-Type: application/json" \
  -d '{
    "typeVehicule": "AUTOMOBILE",
    "motsCles": ["luxury", "premium"],
    "operateur": "OU"
  }'
```

---

### 5️⃣ Recherche avec Query Parameter
```bash
curl -X POST 'http://localhost:8080/api/recherche/type?type=SCOOTER' \
  -H "Content-Type: application/json" \
  -d '{
    "motsCles": ["electrique"],
    "operateur": "ET"
  }'
```

---

## 🔨 Exemples JavaScript/Axios

### Setup
```javascript
const axios = require('axios');

const API_BASE = 'http://localhost:8080/api';

// Fonction utilitaire
async function search(endpoint, data) {
  try {
    const response = await axios.post(`${API_BASE}${endpoint}`, data);
    return response.data;
  } catch (error) {
    console.error('Erreur:', error.response?.data || error.message);
    throw error;
  }
}
```

### Exemple 1: Recherche Automobile
```javascript
// Chercher toutes les Toyota
const toyotas = await search('/recherche/automobiles', {
  motsCles: ['toyota']
});

console.log(`Trouvé ${toyotas.length} Toyota(s)`);
toyotas.forEach(auto => {
  console.log(`- ${auto.marque} ${auto.modele} (${auto.prixBase}€)`);
});
```

### Exemple 2: Recherche Scooter avec Filtre
```javascript
// Chercher scooters électriques de marques spécifiques
const scooters = await search('/recherche/scooters', {
  motsCles: ['electrique', 'moderne'],
  operateur: 'OU'
});

console.log(`Trouvé ${scooters.length} scooter(s) électrique(s)`);
```

### Exemple 3: Fonction Générique
```javascript
async function searchByType(type, keywords, operator = 'OU') {
  const data = {
    typeVehicule: type,
    motsCles: keywords,
    operateur: operator
  };
  
  return await search('/recherche', data);
}

// Utilisation
const autos = await searchByType('AUTOMOBILE', ['sport', 'rapide'], 'ET');
const scooters = await searchByType('SCOOTER', ['vespa']);
```

### Exemple 4: Interface Utilisateur Simple
```javascript
class VehicleSearchUI {
  constructor() {
    this.typeSelect = document.getElementById('vehicleType');
    this.keywordsInput = document.getElementById('keywords');
    this.operatorSelect = document.getElementById('operator');
    this.resultsDiv = document.getElementById('results');
  }

  async handleSearch() {
    const type = this.typeSelect.value;
    const keywords = this.keywordsInput.value.split(',').map(k => k.trim());
    const operator = this.operatorSelect.value;

    try {
      const results = await search('/recherche', {
        typeVehicule: type,
        motsCles: keywords,
        operateur: operator
      });

      this.displayResults(results);
    } catch (error) {
      this.showError(error.message);
    }
  }

  displayResults(results) {
    this.resultsDiv.innerHTML = `<p>Trouvé ${results.length} résultat(s)</p>`;
    results.forEach(item => {
      const html = `
        <div class="result-item">
          <h3>${item.marque} ${item.modele}</h3>
          <p>Type: ${item.typeVehicule}</p>
          <p>Prix: ${item.prixBase}€</p>
          <p>${item.description}</p>
        </div>
      `;
      this.resultsDiv.innerHTML += html;
    });
  }

  showError(message) {
    this.resultsDiv.innerHTML = `<div class="error">Erreur: ${message}</div>`;
  }
}

// Utilisation
const ui = new VehicleSearchUI();
document.getElementById('searchBtn').addEventListener('click', () => ui.handleSearch());
```

---

## 🧮 Cas d'Usage Métier

### Cas 1: Recherche Client Simple
```javascript
// Client cherche une Toyota
async function findToyota() {
  return await search('/recherche/automobiles', {
    motsCles: ['toyota']
  });
}
```

### Cas 2: Filtre Combiné
```javascript
// Client cherche automobile sport ET rapide
async function findSportsCar() {
  return await search('/recherche/automobiles', {
    motsCles: ['sport', 'performance'],
    operateur: 'ET'
  });
}
```

### Cas 3: Alternative Produits
```javascript
// Client hésite entre scooters Vespa OU Piaggio
async function compareScooters() {
  return await search('/recherche/scooters', {
    motsCles: ['vespa', 'piaggio'],
    operateur: 'OU'
  });
}
```

### Cas 4: Catalogue Complet
```javascript
// Afficher tout le catalogue automobiles
async function showFullCatalog() {
  return await search('/recherche/automobiles', {
    motsCles: []  // Mots-clés vides = retourne tous
  });
}
```

---

## 📄 HTML Template

```html
<!DOCTYPE html>
<html>
<head>
  <title>Recherche Véhicules</title>
  <style>
    body { font-family: Arial; margin: 20px; }
    .search-form { margin: 20px 0; padding: 15px; background: #f5f5f5; }
    .result-item { border: 1px solid #ddd; padding: 10px; margin: 5px 0; }
    .error { color: red; }
    .success { color: green; }
  </style>
</head>
<body>
  <h1>Recherche Véhicules</h1>
  
  <div class="search-form">
    <label>Type:</label>
    <select id="vehicleType">
      <option value="AUTOMOBILE">Automobile</option>
      <option value="SCOOTER">Scooter</option>
    </select>
    
    <br/>
    
    <label>Mots-clés (séparés par virgule):</label>
    <input type="text" id="keywords" placeholder="ex: toyota, sport"/>
    
    <br/>
    
    <label>Opérateur:</label>
    <select id="operator">
      <option value="OU">OU (au moins un)</option>
      <option value="ET">ET (tous)</option>
    </select>
    
    <br/>
    
    <button id="searchBtn" onclick="handleSearch()">Rechercher</button>
  </div>
  
  <div id="results"></div>
  
  <script src="search.js"></script>
</body>
</html>
```

---

## 🔗 Autres Endpoints Utiles

### Documents d'un Client
```bash
# Récupérer tous les documents du client #1
curl http://localhost:8080/api/documents/client/1
```

### Itérateur - Catalogue Complet
```bash
# Afficher le catalogue avec iterator
curl http://localhost:8080/api/iterator/catalogue
```

### Template Method - Calcul Montant
```bash
# Calculer montant avec template method
curl -X POST http://localhost:8080/api/template-method/calcul-comptant \
  -H "Content-Type: application/json" \
  -d '{"vehicules": [1, 2]}'
```

### Composite - Hiérarchie Clients
```bash
# Voir structure hiérarchique d'une société
curl http://localhost:8080/api/composite/societe/5
```

---

## ⚠️ Erreurs Courantes

### Erreur: Type invalide
```json
{
  "error": "Type de véhicule invalide: VOITURE. Doit être AUTOMOBILE ou SCOOTER"
}
```
**Solution:** Utiliser AUTOMOBILE ou SCOOTER uniquement

### Erreur: Type obligatoire
```json
{
  "error": "Type de véhicule obligatoire (AUTOMOBILE ou SCOOTER)"
}
```
**Solution:** Ajouter `"typeVehicule"` dans le body

### Erreur: Aucun résultat
```json
[]
```
**Solution:** Les mots-clés ne correspondent à rien. Essayer d'autres termes ou laisser `motsCles` vide pour tous les voir.

---

## 🧪 Teste Complète

### Script de Test Bash
```bash
#!/bin/bash

API="http://localhost:8080/api"

echo "🧪 Test 1: Recherche Automobile simple"
curl -X POST "$API/recherche/automobiles" \
  -H "Content-Type: application/json" \
  -d '{"motsCles": ["toyota"]}'

echo -e "\n🧪 Test 2: Recherche Scooter avec ET"
curl -X POST "$API/recherche/scooters" \
  -H "Content-Type: application/json" \
  -d '{"motsCles": ["vespa", "electrique"], "operateur": "ET"}'

echo -e "\n🧪 Test 3: Recherche routée par type"
curl -X POST "$API/recherche" \
  -H "Content-Type: application/json" \
  -d '{"typeVehicule": "AUTOMOBILE", "motsCles": ["sport"]}'

echo -e "\n✅ Tests terminés!"
```

---

## 📊 Performance Notes

- Recherche en **tables spécifiques** → Plus rapide
- Pas de **mélange de types** → Requêtes optimisées
- **Index sur marque/modèle** recommandé pour production

---

**Dernière mise à jour:** 17 Janvier 2026

