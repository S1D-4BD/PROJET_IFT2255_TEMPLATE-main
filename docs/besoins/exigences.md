---
title: Analyse des besoins - Exigences
---

# Exigences

## Exigences fonctionnelles

- [ ] EF01 : Recherche de cours par code, nom ou mot-clé.
- [ ] EF02 : Affichage des prérequis et de l’éligibilité d’un étudiant.
- [ ] EF03 : Consultation des résultats académiques agrégés.
- [ ] EF04 : Intégration et affichage d’avis étudiants (Discord).
- [ ] EF05 : Comparaison multi-cours (au moins 2).
- [ ] EF06 : Gestion du profil étudiant (programme, cycle, préférences).
- [ ] EF07 : Recommandations personnalisées selon le profil.
- [ ] EF08 : Accessibilité via API REST et interface web.

## Exigences non fonctionnelles

- [ ] ENF01 : Performance – la recherche doit répondre en < 2s.
- [ ] ENF02 : Sécurité – données chiffrées dans la base.
- [ ] ENF03 : Confidentialité – anonymisation des avis (Loi 25).
- [ ] ENF04 : Internationalisation – interface FR/EN.
- [ ] ENF05 : Accessibilité – respect WCAG 2.1 AA.
- [ ] ENF06 : Fiabilité – disponibilité 99,5 % en période de pointe.

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

## Infrastructures

... on va ajouter on the go

- API : REST + documentation OpenAPI
- Stockage externe : CSV académiques, intégration Planifium, bot Discord JSON
