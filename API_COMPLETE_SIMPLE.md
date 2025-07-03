# 🚀 API Complete et Simple - Documentation

## 📌 **Tous les Endpoints Fonctionnels**

### **🎓 THESES API**
```
GET    /api/theses/all                    - Liste toutes les thèses
GET    /api/theses/{id}                   - Détail d'une thèse
GET    /api/theses/doctorant/{doctorantId} - Thèse d'un doctorant
PUT    /api/theses/update/{id}            - Modifier une thèse
GET    /api/theses/statut/{statut}        - Thèses par statut
```

### **📅 AGENDA API**
```
GET    /api/agenda/{doctorantId}          - Échéances à venir d'un doctorant
POST   /api/agenda                        - Ajouter une échéance
PUT    /api/agenda/{id}                   - Modifier une échéance
DELETE /api/agenda/{id}                   - Supprimer une échéance
GET    /api/agenda/all                    - Toutes les échéances à venir
```

### **📚 RESSOURCES API**
```
GET    /api/ressources                    - Liste toutes les ressources
GET    /api/ressources/{id}               - Détail d'une ressource
POST   /api/ressources                    - Ajouter une ressource (admin)
DELETE /api/ressources/{id}               - Supprimer une ressource (admin)
GET    /api/ressources/search             - Rechercher des ressources
```

### **👨‍🎓 DOCTORANTS API**
```
GET    /api/doctorants/dashboard/{userId} - Dashboard d'un doctorant
GET    /api/doctorants/all                - Liste tous les doctorants
```

---

## 🔧 **Exemples d'Utilisation**

### **1. Modifier une thèse**
```bash
PUT http://localhost:23017/api/theses/update/1
Content-Type: application/json

{
  "titre": "Nouveau titre",
  "statut": "EN_COURS"
}
```

### **2. Ajouter une échéance**
```bash
POST http://localhost:23017/api/agenda
Content-Type: application/json

{
  "objet": "Soutenance de thèse",
  "dateEvenement": "2024-06-15T14:00:00",
  "doctorantId": 1
}
```

### **3. Ajouter une ressource**
```bash
POST http://localhost:23017/api/ressources
Content-Type: application/json

{
  "titre": "Guide de rédaction",
  "lien": "https://example.com/guide.pdf",
  "description": "Guide complet",
  "ajouteParId": 1
}
```

---

## ✅ **Statuts de Thèse Disponibles**
- `SOUMISE`
- `EN_COURS` 
- `VALIDEE`
- `ARCHIVEE`

---

## 🎯 **Code Simplifié**

### **Caractéristiques :**
- ✅ **DTOs simples** sans relations circulaires
- ✅ **Gestion d'erreurs claire**
- ✅ **Pas de validations complexes**
- ✅ **Réponses JSON propres**
- ✅ **Code facile à maintenir**

### **Structure des DTOs :**
- `TheseDTO` : Données de base seulement
- `AgendaDTO` : Données de base seulement  
- `RessourceDTO` : Données de base seulement
- `DoctorantDTO` : Données de base seulement

### **Réponses d'erreur simples :**
```json
"Error: Message d'erreur simple"
```

---

## 🚀 **Prêt pour le Développement**

Tous les endpoints sont **testés** et **fonctionnels**. Le code est **simple**, **propre** et **sans bouclages JSON**.

**Base URL :** `http://localhost:23017`

**Prêt à coder ! 🎉**
