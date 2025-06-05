# API Ressources - Documentation

## Vue d'ensemble

L'API Ressources permet de gérer les documents et liens utiles pour les doctorants. Elle offre des fonctionnalités pour lister, consulter, ajouter et supprimer des ressources.

## Endpoints disponibles

### 1. Liste de tous les documents ou liens utiles
```
GET /api/ressources
```

**Réponse de succès (200) :**
```json
{
  "ressources": [
    {
      "id": 1,
      "titre": "Guide de rédaction de thèse",
      "lien": "https://example.com/guide.pdf",
      "description": "Guide complet pour la rédaction d'une thèse",
      "ajoutePar": null
    },
    {
      "id": 2,
      "titre": "Modèle de présentation",
      "lien": "https://example.com/template.pptx",
      "description": "Modèle PowerPoint pour les soutenances",
      "ajoutePar": null
    }
  ],
  "count": 2
}
```

### 2. Détail d'une ressource
```
GET /api/ressources/{id}
```

**Réponse de succès (200) :**
```json
{
  "id": 1,
  "titre": "Guide de rédaction de thèse",
  "lien": "https://example.com/guide.pdf",
  "description": "Guide complet pour la rédaction d'une thèse",
  "ajoutePar": null
}
```

**Réponse d'erreur - Ressource non trouvée (404) :**
```json
{
  "error": "Not Found",
  "message": "Ressource not found with id: 999"
}
```

### 3. Ajouter une ressource (admin)
```
POST /api/ressources
Content-Type: application/json
```

**Corps de la requête (JSON) :**
```json
{
  "titre": "Guide de rédaction de thèse",
  "lien": "https://example.com/guide.pdf",
  "description": "Guide complet pour la rédaction d'une thèse",
  "ajouteParId": 1
}
```

**Réponse de succès (201) :**
```json
{
  "id": 3,
  "titre": "Guide de rédaction de thèse",
  "lien": "https://example.com/guide.pdf",
  "description": "Guide complet pour la rédaction d'une thèse",
  "ajoutePar": null
}
```

**Réponse d'erreur - Corps manquant (400) :**
```json
{
  "error": "Bad Request",
  "message": "Request body is required. Please provide resource data in JSON format.",
  "example": {
    "titre": "Guide de rédaction de thèse",
    "lien": "https://example.com/guide.pdf",
    "description": "Guide complet pour la rédaction d'une thèse",
    "ajouteParId": 1
  }
}
```

### 4. Supprimer une ressource (admin)
```
DELETE /api/ressources/{id}
```

**Réponse de succès (200) :**
```json
{
  "message": "Ressource deleted successfully",
  "deletedId": 1
}
```

**Réponse d'erreur - Ressource non trouvée (404) :**
```json
{
  "error": "Not Found",
  "message": "Ressource not found with id: 999"
}
```

## Endpoints supplémentaires

### 5. Rechercher des ressources
```
GET /api/ressources/search?titre=guide
GET /api/ressources/search?description=rédaction
```

**Réponse de succès (200) :**
```json
{
  "ressources": [
    {
      "id": 1,
      "titre": "Guide de rédaction de thèse",
      "lien": "https://example.com/guide.pdf",
      "description": "Guide complet pour la rédaction d'une thèse",
      "ajoutePar": null
    }
  ],
  "count": 1,
  "searchCriteria": {
    "titre": "guide",
    "description": ""
  }
}
```

### 6. Ressources ajoutées par un utilisateur
```
GET /api/ressources/user/{userId}
```

## Validation des données

### Champs obligatoires pour POST :
- `titre` : String (obligatoire, non vide)
- `ajouteParId` : Long (obligatoire, utilisateur doit exister)

### Champs optionnels :
- `lien` : String (URL vers la ressource)
- `description` : String (description de la ressource)

## Exemples d'utilisation

### Lister toutes les ressources
```bash
curl http://localhost:23017/api/ressources
```

### Obtenir une ressource spécifique
```bash
curl http://localhost:23017/api/ressources/1
```

### Ajouter une nouvelle ressource
```bash
curl -X POST http://localhost:23017/api/ressources \
  -H "Content-Type: application/json" \
  -d '{
    "titre": "Bibliographie LaTeX",
    "lien": "https://example.com/biblio.pdf",
    "description": "Guide pour gérer les références avec LaTeX",
    "ajouteParId": 1
  }'
```

### Supprimer une ressource
```bash
curl -X DELETE http://localhost:23017/api/ressources/1
```

### Rechercher des ressources
```bash
curl "http://localhost:23017/api/ressources/search?titre=guide"
curl "http://localhost:23017/api/ressources/search?description=latex"
```

### Voir les ressources d'un utilisateur
```bash
curl http://localhost:23017/api/ressources/user/1
```

## Notes importantes

1. **Accès admin** : Les opérations POST et DELETE sont réservées aux administrateurs
2. **Validation** : Le titre est obligatoire et l'utilisateur doit exister
3. **Recherche flexible** : Recherche par titre ou description avec correspondance partielle
4. **DTOs** : Évitent les références circulaires dans les réponses JSON
5. **Gestion d'erreurs** : Messages d'erreur explicites pour chaque cas
6. **Tri automatique** : Les ressources sont triées par titre par défaut
