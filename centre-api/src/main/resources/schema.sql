
-- DROP TABLES EXISTANTS
-- DROP TABLES dans l'ordre des dépendances inversées
DROP TABLE IF EXISTS document;
DROP TABLE IF EXISTS cloture_exercice;
DROP TABLE IF EXISTS ecriture_comptable;
DROP TABLE IF EXISTS echeance_credit;
DROP TABLE IF EXISTS credit;
DROP TABLE IF EXISTS amortissement;
DROP TABLE IF EXISTS immobilisation;
DROP TABLE IF EXISTS paiement;
DROP TABLE IF EXISTS quittance;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS locataire;
DROP TABLE IF EXISTS composition_acquisition;
DROP TABLE IF EXISTS propriete;
DROP TABLE IF EXISTS utilisateur;

DROP TABLE IF EXISTS inscription;
DROP TABLE IF EXISTS utilisateur;
DROP TABLE IF EXISTS etudiant;
DROP TABLE IF EXISTS session;
DROP TABLE IF EXISTS formation;
DROP TABLE IF EXISTS formateur;
DROP TABLE IF EXISTS salle;

-- TYPES ENUM EN MAJUSCULE
DROP TYPE IF EXISTS type_bien;
DROP TYPE IF EXISTS type_operation;
DROP TYPE IF EXISTS code_journal;
DROP TYPE IF EXISTS categorie_composition;
DROP TYPE IF EXISTS categorie_immobilisation;
DROP TYPE IF EXISTS moyen_paiement;
DROP TYPE IF EXISTS statut_quittance;
DROP TYPE IF EXISTS type_document;
DROP TYPE IF EXISTS frequence_loyer;

CREATE TYPE type_bien AS ENUM ('APPARTEMENT', 'MAISON', 'BOX', 'PARKING');
CREATE TYPE type_operation AS ENUM ('RECETTE', 'CHARGE', 'ACHAT IMMOBILISATION', 'AMORTISSEMENT', 'INTÉRÊT EMPRUNT', 'ASSURANCE CRÉDIT', 'REMBOURSEMENT CRÉDIT', 'AUTRE');
CREATE TYPE code_journal AS ENUM ('AC', 'VE', 'BQ', 'OD', 'AN');
CREATE TYPE categorie_composition AS ENUM ('TERRAIN', 'CONSTRUCTION', 'TRAVAUX', 'MOBILIER', 'FRAIS DE NOTAIRE', 'FRAIS D''AGENCE', 'AUTRE');
CREATE TYPE categorie_immobilisation AS ENUM ('TERRAIN', 'STRUCTURE', 'FAÇADES', 'TOITURE', 'INSTALLATIONS ÉLECTRIQUES', 'PLOMBERIE', 'CHAUFFAGE', 'AGENCEMENTS INTÉRIEURS', 'CUISINE ÉQUIPÉE', 'MOBILIER', 'ASCENSEUR', 'AUTRE');
CREATE TYPE moyen_paiement AS ENUM ('VIREMENT', 'CHÈQUE', 'ESPÈCES', 'PRÉLÈVEMENT', 'CARTE BANCAIRE', 'AUTRE');
CREATE TYPE statut_quittance AS ENUM ('PAYÉE', 'PARTIELLE', 'IMPAYÉE');
CREATE TYPE type_document AS ENUM ('FACTURE', 'QUITTANCE', 'BAIL', 'JUSTIFICATIF', 'CONTRAT DE CRÉDIT', 'AUTRE');
CREATE TYPE frequence_loyer AS ENUM ('MENSUEL', 'TRIMESTRIEL', 'ANNUEL');

-- UTILISATEUR
CREATE TABLE utilisateur (
    id UUID PRIMARY KEY,
    nom TEXT,
    prenom TEXT,
    email TEXT UNIQUE,
    mot_de_passe_hash TEXT,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- PROPRIETE
CREATE TABLE propriete (
    id UUID PRIMARY KEY,
    utilisateur_id UUID REFERENCES utilisateur(id),
    type_bien type_bien,
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
    categorie categorie_composition,
    montant DECIMAL,
    description TEXT,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- LOCATAIRE
CREATE TABLE locataire (
    id UUID PRIMARY KEY,
    propriete_id UUID REFERENCES propriete(id),
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
    frequence_loyer frequence_loyer,
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
    statut statut_quittance,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- PAIEMENT
CREATE TABLE paiement (
    id UUID PRIMARY KEY,
    quittance_id UUID REFERENCES quittance(id),
    date_paiement DATE,
    montant DECIMAL,
    moyen_paiement moyen_paiement,
    reference TEXT,
    commentaire TEXT,
    est_valide BOOLEAN,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- IMMOBILISATION
CREATE TABLE immobilisation (
    id UUID PRIMARY KEY,
    propriete_id UUID REFERENCES propriete(id),
    nom TEXT,
    categorie categorie_immobilisation,
    valeur DECIMAL,
    duree_amortissement INT,
    date_mise_en_service DATE,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);

-- AMORTISSEMENT
CREATE TABLE amortissement (
    id UUID PRIMARY KEY,
    immobilisation_id UUID REFERENCES immobilisation(id),
    annee INT,
    montant_amorti DECIMAL,
    valeur_residuelle DECIMAL,
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

-- ECRITURE COMPTABLE
CREATE TABLE ecriture_comptable (
    id UUID PRIMARY KEY,
    propriete_id UUID REFERENCES propriete(id),
    date_ecriture DATE,
    libelle TEXT,
    montant DECIMAL,
    type_operation type_operation,
    categorie_comptable TEXT,
    code_journal code_journal,
    tva DECIMAL,
    justificatif_url TEXT,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
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

-- DOCUMENTS
CREATE TABLE document (
    id UUID PRIMARY KEY,
    utilisateur_id UUID REFERENCES utilisateur(id),
    propriete_id UUID REFERENCES propriete(id),
    locataire_id UUID REFERENCES locataire(id),
    immobilisation_id UUID REFERENCES immobilisation(id),
    ecriture_id UUID REFERENCES ecriture_comptable(id),
    type_document type_document,
    titre TEXT,
    url_fichier TEXT,
    date_document DATE,
    cree_le TIMESTAMP,
    modifie_le TIMESTAMP
);
