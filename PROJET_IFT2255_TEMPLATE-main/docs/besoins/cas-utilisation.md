---
title: Analyse des besoins - Cas d'utilisation
---

# Cas d'utilisation

## Vue d’ensemble

Les cas d’utilisation décrivent les principales interactions entre les étudiants, le personnel académique (TGDE, professeurs) et la plateforme de choix de cours.
Ils permettent de valider que les fonctionnalités répondent bien aux besoins exprimés.

## Liste des cas d’utilisation

| ID   | Nom                          | Acteurs principaux | Description |
|------|------------------------------|---------------------|-------------|
| CU01 | Recherche de cours           | Étudiant, TGDE      | Permet de trouver un cours selon code, titre, ou mots-clés et de vérifier l’éligibilité. |
| CU02 | Comparaison de cours         | Étudiant            | Permet de comparer plusieurs cours selon divers critères (charge de travail, résultats, avis). |
| CU03 | Consulter la fiche d’un cours| Étudiant            | Accéder à une fiche détaillée d’un cours avec données officielles et avis. |
| CU04 | Gestion du profil étudiant   | Étudiant            | Modifier les informations personnelles et préférences d’affichage. |
| CU05 | Recommandations personnalisées| Étudiant           | Recevoir des suggestions de cours en fonction du profil académique. |

---

## Détail des cas d’utilisation

### CU01 – Recherche de cours

**Acteurs** : Étudiant (principal), TGDE (secondaire)
**Préconditions** : L’étudiant doit être connecté à l’application et disposer d’une connexion réseau.
**Postconditions** : Une liste de cours correspondant à la recherche est affichée avec l’état d’éligibilité.
**Déclencheur** : L’étudiant saisit un mot-clé, un code ou un titre de cours dans la barre de recherche.
**Dépendances** : API Planifium (cours, prérequis, horaires).
**But** : Trouver efficacement un cours pertinent.

**Scénario nominal :**
1. L’étudiant ouvre la page de recherche.
2. Il saisit un mot-clé ou un code de cours.
3. Le système interroge Planifium et récupère les cours correspondants.
4. Le système vérifie les prérequis et l’éligibilité.
5. La liste des cours s’affiche avec les métadonnées et l’état d’éligibilité.

**Scénario alternatif (TGDE) :**
1. Le TGDE consulte le dossier d’un étudiant.
2. Il effectue une recherche de cours.
3. Le système affiche les résultats.
4. Le TGDE peut forcer une inscription conditionnelle malgré des prérequis manquants (bypass).

---

### CU02 – Comparaison de cours

**Acteurs** : Étudiant
**Préconditions** : L’étudiant doit avoir sélectionné au moins deux cours.
**Postconditions** : Un tableau comparatif est affiché.
**Déclencheur** : L’étudiant clique sur « Ajouter à la comparaison ».
**Dépendances** : Planifium, résultats académiques agrégés, avis Discord.
**But** : Évaluer la compatibilité et la faisabilité d’un ensemble de cours.

**Scénario nominal :**
1. L’étudiant sélectionne deux cours ou plus.
2. Le système récupère données officielles, résultats académiques et avis étudiants.
3. Le système affiche un tableau/graphique comparatif.
4. L’étudiant analyse et peut sauvegarder/partager la comparaison.


**Scénario alternatif :**
1a. Moins de deux cours sélectionnés → 1a.1. Le bouton « Comparer » reste désactivé ;
le système invite à ajouter un cours.



---

### CU03 – Consulter la fiche d’un cours

**Acteurs** : Étudiant
**Préconditions** : Être connecté et avoir une connexion réseau.
**Postconditions** : La fiche détaillée du cours est affichée.
**Déclencheur** : L’étudiant clique sur « Voir détails ».
**Dépendances** : Planifium, résultats académiques agrégés, avis Discord.
**But** : Prendre une décision éclairée sur un cours.

**Scénario nominal :**
1. L’étudiant parcourt la liste des cours.
2. Il clique sur un cours.
3. Le système affiche les données officielles, académiques et avis étudiants.
4. L’étudiant peut ajouter le cours à sa liste ou à une comparaison.

**Scénario alternatif :**
3a. Résultats académiques indisponibles → 3a.1. Afficher « Non disponible pour cette période ».

---

### CU04 – Gestion du profil étudiant

**Acteurs** : Étudiant
**Préconditions** : L’étudiant doit être connecté.
**Postconditions** : Les informations mises à jour sont visibles immédiatement.
**Déclencheur** : L’étudiant accède à « Mon profil ».
**Dépendances** : Base de données utilisateurs.
**But** : Adapter la plateforme aux besoins de l’étudiant.

**Scénario nominal :**
1. L’étudiant ouvre « Mon profil ».
2. Le système affiche les informations actuelles.
3. L’étudiant modifie ses préférences.
4. Le système valide et enregistre.
5. Les modifications apparaissent immédiatement.



---

### CU05 – Recommandations personnalisées

**Acteurs** : Étudiant
**Préconditions** : Profil académique renseigné.
**Postconditions** : Une liste de cours recommandés est affichée.
**Déclencheur** : L’étudiant consulte la section « Recommandations ».
**Dépendances** : Données académiques, historique de cours suivis, préférences.
**But** : Aider l’étudiant à découvrir des cours adaptés à son profil.

**Scénario nominal :**
1. L’étudiant ouvre « Recommandations ».
2. Le système lit le profil et s'il est dispo, l’historique récent.
3. Le système sélectionne des cours correspondant aux préférences et contraintes.
4. Le système génère pour chaque cours une courte justification.
5. Le système affiche la liste triée par pertinence avec accès à « Voir détails » et « Ajouter à la comparaison ».

**Scénario alternatif :**
(1-5)b. Perte réseau. → (1-5)b.1. Notification + « Réessayer » ; 
si possible, suggestions hors-ligne basées sur l’historique local.