# API Theses - Documentation

## Problèmes résolus

L'API `/api/theses/update/{id}` a été corrigée pour résoudre les problèmes suivants :

1. **Références circulaires** : Utilisation de DTOs pour éviter les bouclages JSON
2. **Corps de requête manquant** : Meilleure validation et messages d'erreur explicites
3. **Gestion des erreurs** : Réponses d'erreur structurées avec des messages clairs
4. **Sécurité des relations** : Mise à jour des relations via IDs plutôt que par objets complets

## Endpoints disponibles

### 1. Mettre à jour une thèse (RECOMMANDÉ)
```
PUT /api/theses/update/{id}
Content-Type: application/json
```

**Corps de la requête (JSON) :**
```json
{
  "titre": "Nouveau titre de la thèse",
  "resume": "Nouveau résumé de la thèse",
  "dateSoumission": "2024-01-15",
  "dateInscription": "2023-09-01",
  "dateFinVisee": "2026-12-31",
  "dateSoutenance": "2026-06-15",
  "langue": "Français",
  "urlPdf": "https://example.com/thesis.pdf",
  "motCles": "intelligence artificielle, machine learning",
  "fichierThese": "thesis_v2.pdf",
  "etatThese": "En cours",
  "statut": "EN_COURS",
  "doctorantId": 1,
  "chercheurId": 2
}
```

**Réponse de succès (200) :**
```json
{
  "id": 1,
  "titre": "Nouveau titre de la thèse",
  "resume": "Nouveau résumé de la thèse",
  "dateSoumission": "2024-01-15",
  "dateInscription": "2023-09-01",
  "dateFinVisee": "2026-12-31",
  "dateSoutenance": "2026-06-15",
  "langue": "Français",
  "urlPdf": "https://example.com/thesis.pdf",
  "motCles": "intelligence artificielle, machine learning",
  "fichierThese": "thesis_v2.pdf",
  "etatThese": "En cours",
  "statut": "EN_COURS",
  "doctorant": null,
  "chercheur": null
}
```

**Réponse d'erreur - Corps manquant (400) :**
```json
{
  "error": "Bad Request",
  "message": "Request body is required. Please provide thesis data in JSON format.",
  "example": {
    "titre": "Titre de la thèse",
    "resume": "Résumé de la thèse",
    "statut": "EN_COURS"
  }
}
```

**Réponse d'erreur - Thèse non trouvée (404) :**
```json
{
  "error": "Not Found",
  "message": "Thesis not found with id: 999"
}
```

### 2. Obtenir toutes les thèses
```
GET /api/theses/all
```

### 3. Obtenir une thèse par ID
```
GET /api/theses/{id}
```

### 4. Obtenir une thèse par ID de doctorant
```
GET /api/theses/doctorant/{doctorantId}
```

### 5. Obtenir les thèses par statut
```
GET /api/theses/statut/{statut}
```

Statuts disponibles : `SOUMISE`, `EN_COURS`, `VALIDEE`, `ARCHIVEE`

## Notes importantes

1. **Tous les champs sont optionnels** dans la requête de mise à jour
2. **Seuls les champs fournis seront mis à jour** (mise à jour partielle)
3. **Les relations sont mises à jour via IDs** pour éviter les problèmes de sérialisation
4. **Les DTOs évitent les références circulaires** dans les réponses JSON
5. **L'ancienne API est toujours disponible** à `/api/theses/update-legacy/{id}` mais dépréciée

## Exemples d'utilisation

### Mise à jour partielle (seulement le titre)
```json
{
  "titre": "Nouveau titre seulement"
}
```

### Mise à jour du statut seulement
```json
{
  "statut": "VALIDEE"
}
```

### Changer le directeur de thèse
```json
{
  "chercheurId": 5
}
```
