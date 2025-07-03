# API Agenda - Documentation

## Vue d'ensemble

L'API Agenda permet de gérer les échéances et événements des doctorants. Elle offre des fonctionnalités complètes pour créer, lire, mettre à jour et supprimer des événements.

## Endpoints disponibles

### 1. Voir les échéances à venir d'un doctorant
```
GET /api/agenda/{doctorantId}
```

**Réponse de succès (200) :**
```json
{
  "doctorantId": 1,
  "upcomingEvents": [
    {
      "id": 1,
      "objet": "Soutenance de thèse",
      "dateEvenement": "2024-06-15T14:00:00",
      "doctorant": null
    }
  ],
  "count": 1
}
```

### 2. Ajouter une échéance (par un encadrant ou Directeur)
```
POST /api/agenda
Content-Type: application/json
```

**Corps de la requête (JSON) :**
```json
{
  "objet": "Soutenance de thèse",
  "dateEvenement": "2024-06-15T14:00:00",
  "doctorantId": 1
}
```

**Réponse de succès (201) :**
```json
{
  "id": 1,
  "objet": "Soutenance de thèse",
  "dateEvenement": "2024-06-15T14:00:00",
  "doctorant": {
    "id": 1,
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@example.com"
  }
}
```

**Réponse d'erreur - Corps manquant (400) :**
```json
{
  "error": "Bad Request",
  "message": "Request body is required. Please provide event data in JSON format.",
  "example": {
    "objet": "Soutenance de thèse",
    "dateEvenement": "2024-06-15T14:00:00",
    "doctorantId": 1
  }
}
```

### 3. Modifier une échéance
```
PUT /api/agenda/{id}
Content-Type: application/json
```

**Corps de la requête (JSON) :**
```json
{
  "objet": "Soutenance de thèse modifiée",
  "dateEvenement": "2024-06-20T15:00:00",
  "doctorantId": 1
}
```

**Réponse de succès (200) :**
```json
{
  "id": 1,
  "objet": "Soutenance de thèse modifiée",
  "dateEvenement": "2024-06-20T15:00:00",
  "doctorant": {
    "id": 1,
    "nom": "Dupont",
    "prenom": "Jean",
    "email": "jean.dupont@example.com"
  }
}
```

### 4. Supprimer une échéance
```
DELETE /api/agenda/{id}
```

**Réponse de succès (200) :**
```json
{
  "message": "Event deleted successfully",
  "deletedId": 1
}
```

**Réponse d'erreur - Événement non trouvé (404) :**
```json
{
  "error": "Not Found",
  "message": "Event not found with id: 999"
}
```

## Endpoints supplémentaires

### 5. Obtenir un événement par ID
```
GET /api/agenda/event/{id}
```

### 6. Voir tous les événements d'un doctorant (passés et futurs)
```
GET /api/agenda/{doctorantId}/all
```

### 7. Voir tous les événements à venir (tous doctorants)
```
GET /api/agenda/all
```

## Validation des données

### Champs obligatoires pour POST/PUT :
- `objet` : String (max 255 caractères)
- `dateEvenement` : LocalDateTime (format ISO: "2024-06-15T14:00:00")
- `doctorantId` : Long (doit exister en base)

### Contraintes de validation :
- La date de l'événement doit être dans le futur (pour POST)
- L'objet ne peut pas être vide
- Le doctorant doit exister

## Exemples d'utilisation

### Créer un événement
```bash
curl -X POST http://localhost:23017/api/agenda \
  -H "Content-Type: application/json" \
  -d '{
    "objet": "Réunion de suivi",
    "dateEvenement": "2024-07-01T10:00:00",
    "doctorantId": 1
  }'
```

### Modifier un événement
```bash
curl -X PUT http://localhost:23017/api/agenda/1 \
  -H "Content-Type: application/json" \
  -d '{
    "objet": "Réunion de suivi reportée",
    "dateEvenement": "2024-07-05T10:00:00"
  }'
```

### Supprimer un événement
```bash
curl -X DELETE http://localhost:23017/api/agenda/1
```

### Voir les échéances d'un doctorant
```bash
curl http://localhost:23017/api/agenda/1
```

## Notes importantes

1. **Mise à jour partielle** : Seuls les champs fournis sont mis à jour
2. **Validation temporelle** : Les nouvelles échéances doivent être dans le futur
3. **Relations sécurisées** : Les doctorants sont liés via leur ID
4. **DTOs** : Évitent les références circulaires dans les réponses JSON
5. **Gestion d'erreurs** : Messages d'erreur explicites pour chaque cas
