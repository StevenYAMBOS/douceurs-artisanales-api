## Douceurs artisanales

Modifié le : 09 novembre 2025

Par : [Steven YAMBOS](www.linkedin.com/in/steven-yambos)

[![Postgres](https://img.shields.io/badge/Postgres-%23316192.svg?logo=postgresql&logoColor=white)](#)

---

## Architecture générale

### Schéma relationnel

```
users (1) ----< (N) favorites
users (1) ----< (N) recommendations
users (1) ----< (N) contact_messages
districts (1) ----< (N) pastry_shops
pastry_shops (1) ----< (N) favorites
pastry_shops (1) ----< (N) recommendations
```

---

## Tables

### Table `districts`

Référentiel des 20 arrondissements parisiens pour la classification géographique.

```sql
-- Extension UUID requise
CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE districts (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    number SMALLINT NOT NULL UNIQUE CHECK (number BETWEEN 1 AND 20),
    name VARCHAR(50) NOT NULL,
    postal_code VARCHAR(5) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index pour optimiser les recherches par numéro
CREATE INDEX idx_districts_number ON districts(number);

-- Commentaires
COMMENT ON TABLE districts IS 'Référentiel des arrondissements parisiens';
COMMENT ON COLUMN districts.number IS 'Numéro de l''arrondissement (1-20)';
COMMENT ON COLUMN districts.postal_code IS 'Code postal principal (750XX)';
```

**Explication des champs:**

- `id`: Clé primaire UUID v4
- `number`: Numéro d'arrondissement avec contrainte de validité
- `name`: Libellé complet (ex: "1er Arrondissement")
- `postal_code`: Code postal principal au format 750XX
- `created_at`: Horodatage de création
- `updated_at`: Horodatage de dernière modification

---

### Table `pastry_shops`

Catalogue des établissements pâtissiers avec leurs informations détaillées.

```sql
CREATE TABLE pastry_shops (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    name VARCHAR(100) NOT NULL,
    description TEXT,
    address VARCHAR(200) NOT NULL,
    district_id UUID NOT NULL REFERENCES districts(id) ON DELETE RESTRICT,
    phone VARCHAR(15),
    email VARCHAR(100),
    website VARCHAR(255),
    opening_hours JSONB,
    specialties TEXT[],
    average_rating NUMERIC(2,1) CHECK (average_rating BETWEEN 0 AND 5),
    price_range VARCHAR(10) CHECK (price_range IN ('€', '€€', '€€€')),
    photo_url VARCHAR(255),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index pour optimiser les recherches
CREATE INDEX idx_pastry_shops_district ON pastry_shops(district_id);
CREATE INDEX idx_pastry_shops_name ON pastry_shops(name);
CREATE INDEX idx_pastry_shops_active ON pastry_shops(is_active) WHERE is_active = true;
CREATE INDEX idx_pastry_shops_specialties ON pastry_shops USING GIN(specialties);

-- Commentaires
COMMENT ON TABLE pastry_shops IS 'Catalogue des pâtisseries artisanales parisiennes';
COMMENT ON COLUMN pastry_shops.opening_hours IS 'Horaires d''ouverture au format JSON: {"monday": "9h-19h", ...}';
COMMENT ON COLUMN pastry_shops.specialties IS 'Liste des spécialités principales';
COMMENT ON COLUMN pastry_shops.average_rating IS 'Note moyenne sur 5';
```

**Explication des champs:**

- `id`: Clé primaire UUID v4
- `name`: Nom commercial de l'établissement
- `description`: Présentation détaillée de la pâtisserie
- `address`: Adresse postale complète
- `district_id`: Clé étrangère vers `districts`
- `phone`: Numéro de téléphone au format national
- `email`: Adresse email de contact
- `website`: URL du site officiel
- `opening_hours`: Plages horaires au format JSON flexible
- `specialties`: Array PostgreSQL des produits phares
- `average_rating`: Évaluation globale (0.0 à 5.0)
- `price_range`: Indicateur de gamme de prix
- `photo_url`: URL de l'image principale
- `is_active`: Flag pour désactivation logique
- `created_at`: Horodatage de création
- `updated_at`: Horodatage de dernière modification

---

### Table `users`

Gestion des comptes utilisateurs avec authentification sécurisée.

```sql
CREATE TABLE users (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    phone VARCHAR(15),
    role VARCHAR(20) DEFAULT 'user' CHECK (role IN ('user', 'admin')),
    is_verified BOOLEAN DEFAULT false,
    last_login TIMESTAMP,
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index pour optimiser les authentifications
CREATE UNIQUE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);

-- Commentaires
COMMENT ON TABLE users IS 'Comptes utilisateurs avec gestion des droits';
COMMENT ON COLUMN users.password IS 'Hash bcrypt du mot de passe (coût 10+)';
COMMENT ON COLUMN users.role IS 'Niveau d''autorisation: user ou admin';
```

**Explication des champs:**

- `id`: Clé primaire UUID v4
- `email`: Identifiant unique de connexion
- `password`: Hash sécurisé du mot de passe
- `first_name`: Prénom de l'utilisateur
- `last_name`: Nom de famille
- `phone`: Numéro de contact optionnel
- `role`: Niveau d'autorisation (RBAC)
- `is_verified`: Statut de validation email
- `last_login`: Horodatage de dernière session
- `is_active`: Flag pour désactivation compte
- `created_at`: Horodatage de création
- `updated_at`: Horodatage de dernière modification

---

### Table `favorites`

Table de liaison pour les pâtisseries favorites des utilisateurs.

```sql
CREATE TABLE favorites (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    pastry_shop_id UUID NOT NULL REFERENCES pastry_shops(id) ON DELETE CASCADE,
    personal_rating NUMERIC(2,1) CHECK (personal_rating BETWEEN 0 AND 5),
    comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(user_id, pastry_shop_id)
);

-- Index pour optimiser les requêtes
CREATE INDEX idx_favorites_user ON favorites(user_id);
CREATE INDEX idx_favorites_pastry_shop ON favorites(pastry_shop_id);

-- Commentaires
COMMENT ON TABLE favorites IS 'Association users-pastry_shops pour les favoris';
COMMENT ON COLUMN favorites.personal_rating IS 'Évaluation individuelle (0.0 à 5.0)';
```

**Explication des champs:**

- `id`: Clé primaire UUID v4
- `user_id`: Clé étrangère vers `users` (cascade delete)
- `pastry_shop_id`: Clé étrangère vers `pastry_shops` (cascade delete)
- `personal_rating`: Note optionnelle de l'utilisateur
- `comment`: Avis personnel optionnel
- `created_at`: Horodatage d'ajout au favoris
- Contrainte `UNIQUE`: Empêche les doublons par utilisateur

---

### Table `recommendations`

Suggestions de pâtisseries soumises par les utilisateurs.

```sql
CREATE TABLE recommendations (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    pastry_shop_id UUID REFERENCES pastry_shops(id) ON DELETE SET NULL,
    pastry_shop_name VARCHAR(100) NOT NULL,
    address VARCHAR(200),
    district_number SMALLINT CHECK (district_number BETWEEN 1 AND 20),
    reason TEXT NOT NULL,
    status VARCHAR(20) DEFAULT 'pending' CHECK (status IN ('pending', 'approved', 'rejected')),
    admin_comment TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index pour gestion administrative
CREATE INDEX idx_recommendations_user ON recommendations(user_id);
CREATE INDEX idx_recommendations_status ON recommendations(status);

-- Commentaires
COMMENT ON TABLE recommendations IS 'Suggestions de nouvelles pâtisseries par les users';
COMMENT ON COLUMN recommendations.status IS 'Workflow de validation: pending, approved, rejected';
COMMENT ON COLUMN recommendations.pastry_shop_id IS 'NULL si non encore créée, ID si approuvée';
```

**Explication des champs:**

- `id`: Clé primaire UUID v4
- `user_id`: Clé étrangère vers l'auteur (cascade delete)
- `pastry_shop_id`: Lien optionnel vers pâtisserie créée
- `pastry_shop_name`: Nom de l'établissement suggéré
- `address`: Localisation proposée
- `district_number`: Numéro d'arrondissement (1-20)
- `reason`: Justification de la recommandation
- `status`: État du workflow de validation
- `admin_comment`: Feedback administrateur
- `created_at`: Horodatage de soumission
- `updated_at`: Horodatage de dernière modification

---

### Table `contact_messages`

Stockage des messages du formulaire de contact.

```sql
CREATE TABLE contact_messages (
    id UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id UUID REFERENCES users(id) ON DELETE SET NULL,
    name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL,
    subject VARCHAR(200) NOT NULL,
    message TEXT NOT NULL,
    is_processed BOOLEAN DEFAULT false,
    response TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Index pour gestion des tickets
CREATE INDEX idx_contact_messages_processed ON contact_messages(is_processed) WHERE is_processed = false;
CREATE INDEX idx_contact_messages_user ON contact_messages(user_id);

-- Commentaires
COMMENT ON TABLE contact_messages IS 'Messages du formulaire de contact';
COMMENT ON COLUMN contact_messages.user_id IS 'NULL si utilisateur non authentifié';
COMMENT ON COLUMN contact_messages.is_processed IS 'Flag de gestion des tickets support';
```

**Explication des champs:**

- `id`: Clé primaire UUID v4
- `user_id`: Clé étrangère optionnelle (null pour anonymes)
- `name`: Nom du contact (requis même si authentifié)
- `email`: Email de réponse
- `subject`: Objet du message
- `message`: Contenu du message
- `is_processed`: Statut de traitement par support
- `response`: Réponse administrative optionnelle
- `created_at`: Horodatage de soumission
- `updated_at`: Horodatage de traitement

---

## Changelog

| Version | Date       | Modifications    |
| ------- | ---------- | ---------------- |
| 1.0     | 09-11-2025 | Version initiale |

---

## Annexes

### Conventions de nommage

- **Tables:** Pluriel, snake_case (`contact_messages`)
- **Colonnes:** Singulier, snake_case (`nom`, `arrondissement_id`)
- **Index:** Préfixe `idx_`, format `idx_table_colonne`
- **Vues:** Préfixe `v_`, format `v_description`
- **Contraintes FK:** Format `fk_table_colonne`
