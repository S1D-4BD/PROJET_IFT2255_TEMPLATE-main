---
title: Analyse des besoins - Exigences
---

# Exigences

## Exigences fonctionnelles

- [ ] EF01 : Recherche de cours par code, nom, session ou mot-clé (professeur à voir)
- [ ] EF02 : Affichage des prérequis et de l’éligibilité d’un étudiant.
- [ ] EF03 : Consultation des résultats académiques agrégés.
- [ ] EF04 : Intégration et affichage d’avis étudiants (Discord).
- [ ] EF05 : Comparaison multi-cours (au moins 2)
- [ ] EF06 : Gestion du profil étudiant (programme, cycle, préférences)
- [ ] EF07 : Recommandations personnalisées selon le profil.
- [ ] EF08 : Accessibilité via API REST et interface web

## Exigences non fonctionnelles

- [ ] ENF01 : Performance – la recherche doit répondre en < 2s.
- [ ] ENF02 : Sécurité – données chiffrées dans la base
- [ ] ENF03 : Confidentialité – anonymisation des avis (Loi 25).
- [ ] ENF04 : Internationalisation – interface FR/EN.
- [ ] ENF05 : Accessibilité – respect des handicaps audio-visuels
- [ ] ENF06 : Fiabilité – disponibilité 99,5 % en période de pointe

## Priorisation

- Critiques : EF01, EF02, EF05, ENF01, ENF03
- Importants : EF03, EF04, EF06
- Secondaires : EF07, ENF04, ENF05

## Types d’utilisateurs

| Type d’utilisateur        | Description | Exemples de fonctionnalités |
|---------------------------|-------------|-----------------------------|
| Étudiant (authentifié)    | Profil personnel, cours suivis, préférences | Recherche, comparaison, profil, recommandations |
| TGDE / Professeur         | Vue admin, gestion d’inscriptions | Consultation de dossier étudiant, bypass de prérequis |
| Utilisateur invité        | Accès limité | Consultation d’informations publiques (description cours) |


## Besoins matériels
Infrastructure serveur

- Serveurs (si beosin): Minimum un ordinateur à 8 go (mémoire SSD pas nécéssaire il faut être performant pour les taches)
Système de sauvegarde automatique quotidien (mise en cache afin d'éviter les multiples recherches fréquentes)

## Postes de développement (notre équipe)

Git + GitHub/GitLab
IDE (VS Code, IntelliJ)
Mac/Windows

## Solution de stockage
Base de données principale : Si simple alors JSON sinon MONGODB (bonne option noSQL)

**Attributs de documents/JSON principaux :**

- cours : Code, nom, crédits, cycle, prérequis
- plans_cours : Versionnés par trimestre et professeur
- etudiants : Profils, préférences, programmes
- avis : Commentaires anonymisés par sécurité des utilisateurs
- resultats_academiques : Moyennes, taux d'échec
- horaires : Sessions, plages horaires

# Infrastructures et solutions d'intégration
- Architecture : **API REST**
- Backend (*si en parallèle avec une DB*) : Node.js + Express OU Python + FastAPI
- Format des payloads : JSON
- Documentation : MKDOCS

## Intégrations externes
1. Planifium (API REST) fourni par Louis-Édouard
2. Synchronisation quotidienne (cron jobs)
3. Cache local pour performance
4. Données : cours, prérequis, horaires
5. Discord (Bot + export JSON) pour les commentaires
6. Résultats académiques UdeM (Import CSV)

##  Services à implémenter

Authentification sécurisée : JWT tokens (repsecter la loi 25)
