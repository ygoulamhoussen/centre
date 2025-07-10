-- DROP TABLES EXISTANTS
-- DROP TABLES dans l'ordre des dépendances inversées
DROP TABLE IF EXISTS document CASCADE;
DROP TABLE IF EXISTS cloture_exercice CASCADE;
DROP TABLE IF EXISTS charges CASCADE;
DROP TABLE IF EXISTS recettes CASCADE;
-- ECRITURE COMPTABLE (nouvelle version simplifiée)
DROP TABLE IF EXISTS ligne_ecriture CASCADE;
DROP TABLE IF EXISTS ecriture_comptable CASCADE;
DROP TABLE IF EXISTS echeance_credit CASCADE;
DROP TABLE IF EXISTS credit CASCADE;
DROP TABLE IF EXISTS amortissement CASCADE;
DROP TABLE IF EXISTS immobilisation CASCADE;
DROP TABLE IF EXISTS paiement CASCADE;
DROP TABLE IF EXISTS quittance CASCADE;
DROP TABLE IF EXISTS location CASCADE;
DROP TABLE IF EXISTS locataire CASCADE;
DROP TABLE IF EXISTS composition_acquisition CASCADE;
DROP TABLE IF EXISTS propriete CASCADE;

DROP TABLE IF EXISTS utilisateur_role CASCADE;
DROP TABLE IF EXISTS utilisateur_roles CASCADE;
DROP TABLE IF EXISTS roles CASCADE;
DROP TABLE IF EXISTS role CASCADE;

DROP TABLE IF EXISTS utilisateur CASCADE;

DROP TABLE IF EXISTS compte_comptable;

-- UTILISATEUR
CREATE TABLE utilisateur (
    id UUID PRIMARY KEY,
    user_name TEXT UNIQUE,
    nom TEXT,
    prenom TEXT,
    email TEXT UNIQUE,
    mot_de_passe_hash TEXT,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

CREATE TABLE role (
  id UUID PRIMARY KEY,
  name VARCHAR(50) UNIQUE NOT NULL
);

CREATE TABLE utilisateur_role (
  utilisateur_id UUID REFERENCES utilisateur(id),
  role_id UUID REFERENCES role(id),
  PRIMARY KEY (utilisateur_id, role_id)
);

-- PROPRIETE
CREATE TABLE propriete (
    id UUID PRIMARY KEY,
    utilisateur_id UUID REFERENCES utilisateur(id),
    type_bien TEXT,
    nom TEXT,
    adresse TEXT,
    complement_adresse TEXT,
    code_postal TEXT,
    ville TEXT,
    date_acquisition DATE,
    date_livraison DATE,
    montant_acquisition DECIMAL,
    tantieme DECIMAL,
    frais_notaire DECIMAL,
    frais_agence DECIMAL,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- COMPOSITION D'ACQUISITION
CREATE TABLE composition_acquisition (
    id UUID PRIMARY KEY,
    propriete_id UUID REFERENCES propriete(id),
    categorie TEXT,
    montant DECIMAL,
    description TEXT,
    duree INT,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- LOCATAIRE
CREATE TABLE locataire (
    id UUID PRIMARY KEY,
    utilisateur_id UUID REFERENCES utilisateur(id),
    nom TEXT,
    telephone TEXT,
    email TEXT,
    adresse TEXT,
    complement_adresse TEXT,
    code_postal TEXT,
    ville TEXT,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- LOCATION
CREATE TABLE location (
    id UUID PRIMARY KEY,
    propriete_id UUID REFERENCES propriete(id),
    locataire_id UUID REFERENCES locataire(id),
    date_debut DATE,
    date_fin DATE,
    loyer_mensuel DECIMAL,
    charges_mensuelles DECIMAL,
    depot_garantie DECIMAL,
    frequence_loyer TEXT,
    jour_echeance INTEGER,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- QUITTANCE
CREATE TABLE quittance (
    id UUID PRIMARY KEY,
    location_id UUID REFERENCES location(id),
    date_debut DATE,
    date_fin DATE,
    date_emission DATE,
    montant_loyer DECIMAL,
    montant_charges DECIMAL,
    montant_total DECIMAL,
    statut TEXT,
    date_echeance DATE,
    depot_garantie DECIMAL,
    montant_caution DECIMAL,
    inclure_caution BOOLEAN NOT NULL DEFAULT FALSE,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- PAIEMENT
CREATE TABLE paiement (
    id UUID PRIMARY KEY,
    quittance_id UUID REFERENCES quittance(id),
    date_paiement DATE,
    montant DECIMAL,
    moyen_paiement TEXT,
    reference TEXT,
    commentaire TEXT,
    est_valide BOOLEAN,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- IMMOBILISATION
CREATE TABLE immobilisation (
    id UUID PRIMARY KEY,
    utilisateur_id UUID REFERENCES utilisateur(id) NOT NULL,
    propriete_id UUID REFERENCES propriete(id) NOT NULL,
    intitule TEXT NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    date_acquisition DATE NOT NULL,
    duree_amortissement INTEGER NOT NULL,
    type_immobilisation TEXT NOT NULL,
    categorie_fiscale TEXT NOT NULL,
    valeur_terrain DECIMAL(10,2),
    commentaire TEXT,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- AMORTISSEMENT
CREATE TABLE amortissement (
    id UUID PRIMARY KEY,
    immobilisation_id UUID REFERENCES immobilisation(id) NOT NULL,
    annee INTEGER NOT NULL,
    montant_amortissement DECIMAL(10,2) NOT NULL,
    valeur_residuelle DECIMAL(10,2) NOT NULL,
    cumul_amortissements DECIMAL(10,2) NOT NULL,
    taux_amortissement DECIMAL(5,2) NOT NULL,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- CREDIT
CREATE TABLE credit (
    id UUID PRIMARY KEY,
    propriete_id UUID REFERENCES propriete(id),
    banque TEXT,
    montant_emprunte DECIMAL,
    date_debut DATE,
    date_fin DATE,
    duree_mois INT,
    taux_interet_annuel DECIMAL,
    mensualite DECIMAL,
    assurance_mensuelle DECIMAL,
    frais_dossier DECIMAL,
    frais_garantie DECIMAL,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- ECHEANCE DE CREDIT
CREATE TABLE echeance_credit (
    id UUID PRIMARY KEY,
    credit_id UUID REFERENCES credit(id),
    date_echeance DATE,
    interet DECIMAL,
    capital_rembourse DECIMAL,
    assurance DECIMAL,
    total_echeance DECIMAL
);

-- DOCUMENT
CREATE TABLE document (
    id UUID PRIMARY KEY,
    utilisateur_id UUID REFERENCES utilisateur(id),
    propriete_id UUID REFERENCES propriete(id),
    locataire_id UUID REFERENCES locataire(id),
    immobilisation_id UUID REFERENCES immobilisation(id),
    type_document TEXT,
    titre TEXT,
    url_fichier TEXT,
    contenu TEXT,
    mime_type VARCHAR(255),
    nom_fichier VARCHAR(255),
    taille BIGINT,
    date_document DATE,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- ECRITURE COMPTABLE (doit être créée AVANT charges, recettes, ligne_ecriture)
CREATE TABLE ecriture_comptable (
    id UUID PRIMARY KEY,
    date_ecriture DATE NOT NULL,
    libelle TEXT NOT NULL,
    journal_code VARCHAR(10) NOT NULL,
    numero_piece VARCHAR(50),
    utilisateur_id UUID REFERENCES utilisateur(id) NOT NULL,
    created_at TIMESTAMP DEFAULT NOW()
);

-- CHARGE
CREATE TABLE charges (
    id UUID PRIMARY KEY,
    intitule TEXT NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    date_charge DATE NOT NULL,
    propriete_id UUID REFERENCES propriete(id) NOT NULL,
    nature TEXT NOT NULL,
    commentaire TEXT,
    utilisateur_id UUID REFERENCES utilisateur(id) NOT NULL,
    document_id UUID REFERENCES document(id),
    ecriture_comptable_id UUID REFERENCES ecriture_comptable(id),
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- RECETTE
CREATE TABLE recettes (
    id UUID PRIMARY KEY,
    intitule TEXT NOT NULL,
    montant DECIMAL(10,2) NOT NULL,
    date_recette DATE NOT NULL,
    propriete_id UUID REFERENCES propriete(id) NOT NULL,
    type TEXT NOT NULL,
    quittance_id UUID REFERENCES quittance(id),
    commentaire TEXT,
    utilisateur_id UUID REFERENCES utilisateur(id) NOT NULL,
    document_id UUID REFERENCES document(id),
    ecriture_comptable_id UUID REFERENCES ecriture_comptable(id),
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- LIGNE ECRITURE (doit être après ecriture_comptable)
CREATE TABLE ligne_ecriture (
    id UUID PRIMARY KEY,
    ecriture_id UUID REFERENCES ecriture_comptable(id) ON DELETE CASCADE,
    compte_num VARCHAR(20) NOT NULL,
    compte_libelle VARCHAR(255) NOT NULL,
    debit DECIMAL(12,2) DEFAULT 0,
    credit DECIMAL(12,2) DEFAULT 0,
    tiers VARCHAR(255),
    commentaire TEXT
);

-- CLOTURE D'EXERCICE
CREATE TABLE cloture_exercice (
    id UUID PRIMARY KEY,
    annee_fiscale INT,
    utilisateur_id UUID REFERENCES utilisateur(id),
    total_loyers DECIMAL,
    total_charges DECIMAL,
    total_amortissements DECIMAL,
    resultat_comptable DECIMAL,
    revenu_imposable DECIMAL,
    date_cloture DATE,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

CREATE TABLE IF NOT EXISTS compte_comptable (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(20) NOT NULL UNIQUE,
    libelle VARCHAR(255) NOT NULL,
    type VARCHAR(50) NOT NULL,
    description VARCHAR(255)
);