---
title: Analyse des besoins - Présentation générale
---

# Présentation du projet

## Méthodologie pour la cueillette des données

Les besoins ont été collectés de plusieurs façons :

**Discussion en équipe** pour définir les fonctionnalités principales et identifier les problèmes rencontrés par les étudiants lors du choix de cours.

**Analyse documentaire** : consultation de Planifium, des règlements académiques de l’UdeM et des directives liées à la Loi 25.

**Observation et retours informels** : échanges avec des étudiants du DIRO et expériences personnelles

Cette combinaison d’approches à permis de bâtir une base de besoins réaliste et réalisable

---

## Description du domaine

### Fonctionnement

La plateforme est une application web (avec API REST) permettant aux étudiants de l’UdeM de choisir leurs cours en combinant :

- **Données officielles** : horaires, prérequis, crédits, cycle, résultats académiques agrégés (moyennes, taux d’échec, nombre d’inscrits).
- **Données non officielles** : avis étudiants issus de Discord, incluant la charge de travail perçue, le niveau de difficulté et les commentaires qualitatifs.

Les étudiants peuvent rechercher des cours, consulter des fiches détaillées, comparer plusieurs cours et obtenir des recommandations personnalisées selon leur profil académique.

les professeurs peuvent modifier leur plan de cours (menant à une modification de la fiche de cours), les TGDE peuvent récolter des informations pour aider un étudiant dans sa prise de décision /fournir un plan de parcours idéal

---

### Acteurs

- **Étudiants** : principaux utilisateurs (tous cycles confondus).
- **TGDE** (Techniciens en gestion de dossiers étudiants) : consultation des dossiers, validation, possibilité de bypasser des prérequis.
- **Professeurs / chargés de cours** : indirectement concernés, leurs plans de cours et méthodes d’enseignement influencent les données.
- **Systèmes externes** :
  - *Planifium* : source des données officielles (horaires, prérequis).
  - *Discord* : source des avis étudiants
  - *Données académiques agrégées* (CSV fournis par l’université).

---

### Dépendances

- **API Planifium** : fournit les cours, horaires et prérequis.
- **Bot Discord** : collecte les avis étudiants (export JSON si on y arrive).
- **Résultats académiques agrégés** : statistiques officielles fournies par l’UdeM/ Louis-Édouard
- **Serveur central si nécéssaire** : la base de données et l’application web si on fait une page web

---

## Hypothèses et contraintes

### Hypothèses
- Les avis publiés sur Discord sont représentatifs de l’expérience réelle des étudiants.
- Les cours sont relativement stables d’une session à l’autre (prérequis, contenu, cycle).
- Les professeurs ne changent pas radicalement la structure d’un cours entre deux trimestres.
- Les étudiants utiliseront la plateforme en complément de synchro et planifium et non en remplacement complet.

### Contraintes
- **Légales** : respect de la Loi 25 (protection des renseignements personnels, anonymisation des avis).
- **Techniques** : assurer une recherche fluide , disponibilité élevée malgré le traffic de requetes, encryption des données sensibles
- **Organisation** : limites de l’équipe de projet (temps, ressources), dépendance aux données externes (Planifium, Discord, CSV).
- **Évolutives** : nécessité de versionner les plans de cours par trimestre et par professeur afin d’éviter l’obsolescence des données
