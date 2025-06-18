
SUPNUM ARCHIVES - GUIDE D'INSTALLATION RAPIDE
=============================================

📱 Projet Android complet pour les étudiants de SupNum.

------------------------------------------
📦 1. OUVERTURE DU PROJET DANS ANDROID STUDIO
------------------------------------------
- Ouvrir le dossier "SupArch" avec Android Studio.
- Si demandé, cliquer sur "Trust project" / "Gradle sync".
- Ajouter le fichier `google-services.json` dans : app/

------------------------------------------
🔥 2. CONFIGURATION FIREBASE CONSOLE
------------------------------------------
1. Crée un projet sur https://console.firebase.google.com
2. Active :
   - Authentication > Méthode Email/Mot de passe
   - Firestore Database (en mode test)
   - Storage
3. Télécharge `google-services.json` et place-le dans /app

------------------------------------------
🛡 3. RÈGLES FIRESTORE (dev/test)
------------------------------------------
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /{document=**} {
      allow read, write: if request.auth != null;
    }
  }
}

------------------------------------------
🛡 4. RÈGLES STORAGE (dev/test)
------------------------------------------
rules_version = '2';
service firebase.storage {
  match /b/{bucket}/o {
    match /{allPaths=**} {
      allow read, write: if request.auth != null;
    }
  }
}

------------------------------------------
🧪 5. CRÉER DES UTILISATEURS TEST
------------------------------------------
Collection : users

- users/{UID}
  - name: "Admin"
  - email: "admin@supnum.com"
  - role: "admin"

- users/{UID}
  - name: "Ancien"
  - email: "ancien@supnum.com"
  - role: "ancien"

- users/{UID}
  - name: "Étudiant"
  - email: "etudiant@supnum.com"
  - role: "etudiant"

------------------------------------------
🎯 MODULES INCLUS DANS L'APP
------------------------------------------
✅ Authentification + gestion des rôles
✅ Upload de fichiers (par anciens)
✅ Validation admin des documents
✅ Téléchargement depuis Firebase
✅ Questions / Réponses entre étudiants
✅ Générateur de planning intelligent
✅ Navigation fluide + design cohérent

Bonne utilisation !
