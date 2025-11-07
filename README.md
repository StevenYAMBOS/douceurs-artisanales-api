# Douceurs artisanales

Bienvenue sur le dépôt GitHub de l'API **Douceurs Artisanales**, une application permettant de découvrir les délices sucrés proposés par les artisans pâtissiers à Paris, classés par arrondissement.

\
[![NestJS](https://img.shields.io/badge/NestJS-EA2845?logo=nestjs&logoColor=fff)](#)
[![TypeScript](https://img.shields.io/badge/TypeScript-3178C6?logo=typescript&logoColor=fff)](#)
[![Node.js](https://img.shields.io/badge/Node.js-339933?logo=node.js&logoColor=fff)](#)
[![pnpm](https://img.shields.io/badge/pnpm-F69220?logo=pnpm&logoColor=fff)](#)
[![Jest](https://img.shields.io/badge/Jest-C21325?logo=jest&logoColor=fff)](#)
[![ESLint](https://img.shields.io/badge/ESLint-4B32C3?logo=eslint&logoColor=fff)](#)
[![Prettier](https://img.shields.io/badge/Prettier-1A2C34?logo=prettier&logoColor=fff)](#)
[![Statut licence](https://img.shields.io/badge/Licence-UNLICENSED-808080)](#)

> Les badges sont sélectionnés depuis la collection de références de badges. Voir la section « références ».

<p align="center">
  <img src="https://savourschool.com.au/wp-content/uploads/2023/08/BLOG-IMAGE-Hazelnut-Paris-Brest.jpg" alt="Douceurs artisanales" width="738">
</p>

## Aperçu du projet

- API REST focalisée sur la découverte des pâtisseries par arrondissement
- Architecture modulaire Nest (modules, contrôleurs, services)
- Outillage standardisé pour le développement, le test, le lint et le formatage

## Fonctionnalités

- Recherche de pâtisseries par arrondissement
- Authentification pour gérer des favoris et des recommandations
- Formulaire de contact pour les questions et suggestions

## Architecture et organisation

Arborescence minimale du projet pour repères rapides.

```text
src/
  app.controller.ts
  app.module.ts
  app.service.ts
  main.ts
test/
  app.e2e-spec.ts
  jest-e2e.json
```

Principes structurants:

- `app.module.ts`: module racine agrégeant les dépendances applicatives
- `app.controller.ts`: exposé HTTP (contrats REST)
- `app.service.ts`: logique de domaine applicative
- `main.ts`: bootstrap de l'application (initialisation NestFactory)

## Prérequis

- Node.js 18+ recommandé
- pnpm 8+
- git

## Installation

```bash
git clone https://github.com/StevenYAMBOS/douceurs-artisanales-api
cd douceurs-artisanales-api
pnpm install
```

## Configuration

Le projet ne requiert pas de configuration externe par défaut. Pour ajouter des variables d'environnement, créez un fichier `.env` à la racine et chargez-les dans `main.ts` ou via le module `@nestjs/config` si vous l'ajoutez au projet.

Exemple minimal de variables d'environnement:

```bash
PORT=3000
NODE_ENV=development
```

## Scripts de développement

Scripts disponibles (extraits de `package.json`).

```bash
pnpm start         # démarrage (mode standard)
pnpm start:dev     # démarrage en watch
pnpm start:debug   # démarrage avec debugger
pnpm build         # build de production (transpile TypeScript -> dist)
pnpm start:prod    # exécution du build (node dist/main)
pnpm test          # tests unitaires (jest)
pnpm test:watch    # tests en watch
pnpm test:cov      # couverture de test
pnpm test:e2e      # tests end-to-end
pnpm lint          # lint (eslint)
pnpm format        # formatage (prettier)
```

## Exécution locale

Développement avec rechargement:

```bash
pnpm start:dev
```

Build et exécution de production:

```bash
pnpm build
pnpm start:prod
```

Par défaut, l'application écoute sur le port `3000` et expose un endpoint de santé minimal à la racine (`GET /`).

## Tests et qualité

- Tests unitaires via `jest`
- Tests e2e via `supertest` et configuration `test/jest-e2e.json`
- Normes de code via `eslint` et formatage via `prettier`

## Documentation API

La documentation OpenAPI/Swagger n'est pas activée par défaut dans ce dépôt. Pour l'activer, ajoutez `@nestjs/swagger`, configurez l'initialisation dans `main.ts` et exposez l'UI sous `/api`.

## Liens utiles

Dépôt Front-End (pas encore créé) : <https://github.com/StevenYAMBOS/douceurs-artisanales-app>

## Contacts

[![LinkedIn](https://custom-icon-badges.demolab.com/badge/LinkedIn-0A66C2?logo=linkedin-white&logoColor=fff)](https://www.linkedin.com/in/steven-yambos)

[![X](https://img.shields.io/badge/X-%23000000.svg?logo=X&logoColor=white)](https://x.com/StevenYambos)

## Références

Références de badges: <https://github.com/inttter/md-badges>
