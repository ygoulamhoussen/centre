-- =============================
-- 📘 Plan comptable simplifié LMNP – par catégories
-- =============================
--
-- 🟦 Recettes (classe 7)
-- Numéro de compte | Intitulé                        | À reporter dans 2033-B
-- 706000           | Loyers meublés                  | Recettes locatives
-- 708000           | Produits accessoires            | Produits accessoires
--
-- 🟥 Charges d'exploitation (classe 6)
-- Numéro de compte | Intitulé                        | À reporter dans 2033-B
-- 606000           | Achats non stockés de petits matériels | Achats
-- 615000           | Entretien et réparations        | Charges externes
-- 616000           | Primes d'assurances             | Charges externes
-- 618000           | Frais de gestion (honoraires, assistance...) | Charges externes
-- 622000           | Frais de gestion locative, conciergerie | Charges externes
-- 606300           | Fournitures administratives     | Charges externes
-- 635100           | Taxe foncière                   | Impôts et taxes
-- 637000           | Cotisations CFE ou autres       | Impôts et taxes
--
-- 🟧 Charges financières
-- Numéro de compte | Intitulé                        | À reporter dans 2033-B
-- 661100           | Intérêts des emprunts           | Charges financières
--
-- 🟨 Amortissements (via 2033-C)
-- Numéro de compte | Intitulé                        | À reporter dans 2033-B
-- 681100           | Dotations aux amortissements    | Dotations aux amortissements
-- =============================

-- Jeux de données pour LMNP - PostgreSQL

-- Utilisateurs
INSERT INTO utilisateur (id, user_name, nom, prenom, email, mot_de_passe_hash, cree_le, modifie_le) VALUES
('00000000-0000-0000-0000-000000000001', 'jdupont', 'Dupont', 'Jean', 'jean.dupont@example.com', 'hash1', now(), now()),
('00000000-0000-0000-0000-000000000002', 'cmartin', 'Martin', 'Claire', 'claire.martin@example.com', 'hash2', now(), now()),
('00000000-0000-0000-0000-000000000003', 'Yussouf', 'Martin', 'Claire', 'yussouf.goulamhoussen@gmail.com', '123456', now(), now()),
('00000000-0000-0000-0000-000000000004', 'Taha', 'Martin', 'Claire', 'taha.goulamhoussen@gmail.com', '123456', now(), now()),
('00000000-0000-0000-0000-000000000005', 'bmorel', 'Morel', 'Benoit', 'benoit.morel@example.com', 'hash3', now(), now()),
('00000000-0000-0000-0000-000000000006', 'lperrin', 'Perrin', 'Laura', 'laura.perrin@example.com', 'hash4', now(), now());


-- Création des rôles
INSERT INTO role (id, name) VALUES
('80000000-0000-0000-0000-000000000001', 'USER'),
('80000000-0000-0000-0000-000000000002', 'ADMIN');

-- Association utilisateur ↔ rôle
INSERT INTO utilisateur_role (utilisateur_id, role_id) VALUES
('00000000-0000-0000-0000-000000000003', '80000000-0000-0000-0000-000000000001'), -- Yussouf → USER
('00000000-0000-0000-0000-000000000004', '80000000-0000-0000-0000-000000000002'), -- Taha → ADMIN
('00000000-0000-0000-0000-000000000005', '80000000-0000-0000-0000-000000000001'), -- Benoit → USER
('00000000-0000-0000-0000-000000000006', '80000000-0000-0000-0000-000000000002'); -- Laura → ADMIN


-- Propriétés
INSERT INTO propriete (id, utilisateur_id, type_bien, nom, adresse, complement_adresse, code_postal, ville,
    date_acquisition, date_livraison, montant_acquisition, tantieme, frais_notaire, frais_agence, cree_le, modifie_le) VALUES
('10000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003', 'APPARTEMENT', 'Studio Paris 15e',
    '10 rue de Vaugirard', '', '75015', 'Paris', '2022-06-15', '2022-07-01', 220000, 100, 15000, 8000, now(), now()),
('10000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000003', 'MAISON', 'Maison Bordeaux',
    '15 avenue des Vins', '', '33000', 'Bordeaux', '2023-01-10', '2023-03-01', 350000, 100, 25000, 12000, now(), now()),
('10000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000005', 'APPARTEMENT', 'Appartement Nice',
    '25 avenue Jean Médecin', '', '06000', 'Nice', '2021-05-10', '2021-06-01', 180000, 100, 12000, 6000, now(), now()),
('10000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000006', 'MAISON', 'Villa Lyon',
    '8 rue des Fleurs', '', '69000', 'Lyon', '2020-03-15', '2020-05-01', 400000, 100, 20000, 15000, now(), now());

-- LOCATAIRES (mise à jour : utilisateur_id au lieu de propriete_id)
INSERT INTO locataire (id, utilisateur_id, nom, telephone, email, adresse, complement_adresse, code_postal, ville, cree_le, modifie_le) VALUES
('20000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003', 'Alice Laurent', '0601010101', 'alice@example.com', '1 rue des Lilas', '', '75015', 'Paris', now(), now()),
('20000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000003', 'Marc Durant', '0602020202', 'marc@example.com', '2 rue des Vignes', '', '33000', 'Bordeaux', now(), now()),
('20000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000005', 'Sophie Martin', '0603030303', 'sophie.martin@example.com', '3 rue des Oliviers', '', '06000', 'Nice', now(), now()),
('20000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000006', 'Julien Petit', '0604040404', 'julien.petit@example.com', '4 rue des Roses', '', '69000', 'Lyon', now(), now());

-- Location
INSERT INTO location (id, propriete_id, locataire_id, date_debut, date_fin, loyer_mensuel, charges_mensuelles, depot_garantie, frequence_loyer, jour_echeance, cree_le, modifie_le) VALUES
('30000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000001',
    '2023-01-01', null, 900, 100, 1000, 'MENSUEL', 5, now(), now()),
('30000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000002',
    '2023-02-01', null, 1200, 150, 1500, 'MENSUEL', 10, now(), now()),
('30000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000003', '20000000-0000-0000-0000-000000000003',
    '2022-01-01', null, 750, 80, 800, 'MENSUEL', 3, now(), now()),
('30000000-0000-0000-0000-000000000004', '10000000-0000-0000-0000-000000000004', '20000000-0000-0000-0000-000000000004',
    '2021-04-01', '2023-04-01', 1500, 200, 2000, 'MENSUEL', 7, now(), now());

-- Composition Acquisition
INSERT INTO composition_acquisition (id, propriete_id, categorie, montant, description, cree_le, modifie_le) VALUES
('40000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', 'TERRAIN', 80000, 'Terrain brut', now(), now()),
('40000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000001', 'CONSTRUCTION', 120000, 'Construction neuve', now(), now()),
('40000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000003', 'TERRAIN', 60000, 'Terrain centre-ville', now(), now()),
('40000000-0000-0000-0000-000000000004', '10000000-0000-0000-0000-000000000003', 'CONSTRUCTION', 120000, 'Construction ancienne', now(), now()),
('40000000-0000-0000-0000-000000000005', '10000000-0000-0000-0000-000000000004', 'TERRAIN', 100000, 'Grand terrain', now(), now()),
('40000000-0000-0000-0000-000000000006', '10000000-0000-0000-0000-000000000004', 'CONSTRUCTION', 300000, 'Villa moderne', now(), now());

-- Crédits
INSERT INTO credit (id, propriete_id, banque, montant_emprunte, date_debut, date_fin, duree_mois, taux_interet_annuel, mensualite, assurance_mensuelle, frais_dossier, frais_garantie, cree_le, modifie_le) VALUES
('50000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', 'Banque A', 200000, '2022-06-01', '2037-06-01', 180, 1.2, 1000, 30, 500, 1000, now(), now()),
('50000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000003', 'Banque B', 150000, '2021-05-01', '2036-05-01', 180, 1.5, 800, 25, 400, 900, now(), now()),
('50000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000004', 'Banque C', 350000, '2020-03-01', '2040-03-01', 240, 1.1, 1400, 40, 600, 1200, now(), now()),
('50000000-0000-0000-0000-000000000200', '10000000-0000-0000-0000-000000000002', 'Credit Foncier', 320000, '2023-01-01', '2043-01-01', 240, 2.1, 1530.55, 65.00, 1000, 2500, now(), now());

-- QUITTANCE
INSERT INTO quittance (id, location_id, date_debut, date_fin, date_emission, montant_loyer, montant_charges, montant_total, statut, cree_le, modifie_le) VALUES
('40000000-0000-0000-0000-000000000001', '30000000-0000-0000-0000-000000000001', '2024-04-01', '2024-04-30', '2024-04-01', 900.00, 100.00, 1000.00, 'PAYEE', NOW(), NOW()),
('40000000-0000-0000-0000-000000000002', '30000000-0000-0000-0000-000000000003', '2024-03-01', '2024-03-31', '2024-03-01', 750.00, 80.00, 830.00, 'PAYEE', NOW(), NOW()),
('40000000-0000-0000-0000-000000000003', '30000000-0000-0000-0000-000000000004', '2023-03-01', '2023-03-31', '2023-03-01', 1500.00, 200.00, 1700.00, 'IMPAYEE', NOW(), NOW());

-- PAIEMENT
INSERT INTO paiement (id, quittance_id, date_paiement, montant, moyen_paiement, reference, commentaire, est_valide, cree_le, modifie_le) VALUES
('50000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', '2024-04-03', 1000.00, 'VIREMENT', 'VIR123456', 'Paiement complet', true, NOW(), NOW()),
('50000000-0000-0000-0000-000000000002', '40000000-0000-0000-0000-000000000002', '2024-03-05', 830.00, 'CARTE', 'CB202403', 'Paiement par carte', true, NOW(), NOW()),
('50000000-0000-0000-0000-000000000003', '40000000-0000-0000-0000-000000000003', '2023-03-10', 1700.00, 'CHEQUE', 'CHQ202303', 'Chèque en attente', false, NOW(), NOW());

-- Propriétés supplémentaires pour Yussouf
INSERT INTO propriete (id, utilisateur_id, type_bien, nom, adresse, complement_adresse, code_postal, ville, date_acquisition, date_livraison, montant_acquisition, tantieme, frais_notaire, frais_agence, cree_le, modifie_le) VALUES
('10000000-0000-0000-0000-000000000101', '00000000-0000-0000-0000-000000000003', 'APPARTEMENT', 'Appartement Marseille', '12 rue Paradis', '', '13001', 'Marseille', '2021-02-10', '2021-03-01', 160000, 100, 10000, 5000, now(), now()),
('10000000-0000-0000-0000-000000000102', '00000000-0000-0000-0000-000000000003', 'MAISON', 'Maison Lille', '7 rue Nationale', '', '59000', 'Lille', '2020-09-15', '2020-10-01', 300000, 100, 18000, 9000, now(), now());

-- Locataires supplémentaires pour Yussouf
INSERT INTO locataire (id, utilisateur_id, nom, telephone, email, adresse, complement_adresse, code_postal, ville, cree_le, modifie_le) VALUES
('20000000-0000-0000-0000-000000000101', '00000000-0000-0000-0000-000000000003', 'Paul Dubois', '0605050505', 'paul.dubois@example.com', '5 rue des Pins', '', '13001', 'Marseille', now(), now()),
('20000000-0000-0000-0000-000000000102', '00000000-0000-0000-0000-000000000003', 'Emma Leroy', '0606060606', 'emma.leroy@example.com', '6 rue des Peupliers', '', '59000', 'Lille', now(), now());

-- Locations supplémentaires pour Yussouf
INSERT INTO location (id, propriete_id, locataire_id, date_debut, date_fin, loyer_mensuel, charges_mensuelles, depot_garantie, frequence_loyer, jour_echeance, cree_le, modifie_le) VALUES
('30000000-0000-0000-0000-000000000101', '10000000-0000-0000-0000-000000000101', '20000000-0000-0000-0000-000000000101', '2022-05-01', null, 700, 70, 700, 'MENSUEL', 2, now(), now()),
('30000000-0000-0000-0000-000000000102', '10000000-0000-0000-0000-000000000102', '20000000-0000-0000-0000-000000000102', '2021-11-01', null, 1100, 120, 1200, 'MENSUEL', 8, now(), now());

-- Quittances pour Yussouf
INSERT INTO quittance (id, location_id, date_debut, date_fin, date_emission, montant_loyer, montant_charges, montant_total, statut, cree_le, modifie_le) VALUES
('40000000-0000-0000-0000-000000000101', '30000000-0000-0000-0000-000000000101', '2024-05-01', '2024-05-31', '2024-05-01', 700.00, 70.00, 770.00, 'PAYEE', NOW(), NOW()),
('40000000-0000-0000-0000-000000000102', '30000000-0000-0000-0000-000000000101', '2024-04-01', '2024-04-30', '2024-04-01', 700.00, 70.00, 770.00, 'IMPAYEE', NOW(), NOW()),
('40000000-0000-0000-0000-000000000103', '30000000-0000-0000-0000-000000000102', '2024-05-01', '2024-05-31', '2024-05-01', 1100.00, 120.00, 1220.00, 'PAYEE', NOW(), NOW()),
('40000000-0000-0000-0000-000000000104', '30000000-0000-0000-0000-000000000102', '2024-04-01', '2024-04-30', '2024-04-01', 1100.00, 120.00, 1220.00, 'PAYEE', NOW(), NOW());

-- Paiements pour Yussouf (divers statuts et moyens)
INSERT INTO paiement (id, quittance_id, date_paiement, montant, moyen_paiement, reference, commentaire, est_valide, cree_le, modifie_le) VALUES
('50000000-0000-0000-0000-000000000101', '40000000-0000-0000-0000-000000000101', '2024-05-05', 770.00, 'VIREMENT', 'VIR202405', 'Loyer payé en virement', true, NOW(), NOW()),
('50000000-0000-0000-0000-000000000102', '40000000-0000-0000-0000-000000000102', '2024-04-10', 400.00, 'ESPECES', 'ESP202404', 'Paiement partiel en espèces', false, NOW(), NOW()),
('50000000-0000-0000-0000-000000000103', '40000000-0000-0000-0000-000000000103', '2024-05-07', 1220.00, 'CARTE', 'CB202405', 'Loyer payé par carte', true, NOW(), NOW()),
('50000000-0000-0000-0000-000000000104', '40000000-0000-0000-0000-000000000104', '2024-04-10', 1220.00, 'CHEQUE', 'CHQ202404', 'Chèque reçu', true, NOW(), NOW());

-- Crédits pour les nouvelles propriétés de Yussouf
INSERT INTO credit (id, propriete_id, banque, montant_emprunte, date_debut, date_fin, duree_mois, taux_interet_annuel, mensualite, assurance_mensuelle, frais_dossier, frais_garantie, cree_le, modifie_le) VALUES
('50000000-0000-0000-0000-000000000101', '10000000-0000-0000-0000-000000000101', 'Banque D', 120000, '2021-02-01', '2036-02-01', 180, 1.3, 650, 20, 300, 700, now(), now()),
('50000000-0000-0000-0000-000000000102', '10000000-0000-0000-0000-000000000102', 'Banque E', 250000, '2020-09-01', '2040-09-01', 240, 1.0, 1100, 35, 500, 1000, now(), now());

-- Compositions d'acquisition pour les nouvelles propriétés de Yussouf
INSERT INTO composition_acquisition (id, propriete_id, categorie, montant, description, cree_le, modifie_le) VALUES
('40000000-0000-0000-0000-000000000101', '10000000-0000-0000-0000-000000000101', 'TERRAIN', 50000, 'Terrain centre Marseille', now(), now()),
('40000000-0000-0000-0000-000000000102', '10000000-0000-0000-0000-000000000101', 'CONSTRUCTION', 110000, 'Immeuble ancien rénové', now(), now()),
('40000000-0000-0000-0000-000000000103', '10000000-0000-0000-0000-000000000102', 'TERRAIN', 90000, 'Terrain Lille', now(), now()),
('40000000-0000-0000-0000-000000000104', '10000000-0000-0000-0000-000000000102', 'CONSTRUCTION', 210000, 'Maison familiale', now(), now());

-- PROPRIÉTÉ SANS COMPOSITION
INSERT INTO propriete (
    id, utilisateur_id, type_bien, nom, adresse, code_postal, ville,
    date_acquisition, montant_acquisition, tantieme, frais_notaire,
    frais_agence, cree_le, modifie_le
) VALUES (
    '10000000-0000-0000-0000-000000000099', -- id de la propriété
    '00000000-0000-0000-0000-000000000001', -- utilisateur existant
    'MAISON',                               -- type_bien
    'Maison secondaire',                    -- nom
    '5 avenue du Lac',                      -- adresse
    '74000',                                -- code_postal
    'Annecy',                               -- ville
    '2021-09-10',                           -- date_acquisition
    450000.00,                              -- montant_acquisition
    1000.00,                                -- tantieme
    12000.00,                               -- frais notaire
    8000.00,                                -- frais agence
    NOW(), NOW()
);

-- Les documents seront créés via l'interface utilisateur avec un contenu valide

-- ===== IMMOBILISATIONS =====

-- Immobilisations pour Yussouf
INSERT INTO immobilisation (id, utilisateur_id, propriete_id, intitule, montant, date_acquisition, duree_amortissement, type_immobilisation, categorie_fiscale, valeur_terrain, commentaire, cree_le, modifie_le) VALUES
('60000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000001', 'Renovation cuisine', 15000.00, '2023-02-15', 10, 'TRAVAUX', 'TRAVAUX_10_ANS', NULL, 'Rénovation complète de la cuisine avec électroménager', NOW(), NOW()),
('60000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000001', 'Mobilier salon', 8000.00, '2023-03-01', 10, 'MOBILIER', 'MOBILIER_10_ANS', NULL, 'Canapé, table basse, meubles TV', NOW(), NOW()),
('60000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000002', 'Chaudiere gaz', 12000.00, '2023-01-10', 10, 'EQUIPEMENT', 'EQUIPEMENT_5_ANS', NULL, 'Chaudière à condensation neuve', NOW(), NOW()),
('60000000-0000-0000-0000-000000000004', '00000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000002', 'Terrain + construction', 350000.00, '2023-01-10', 25, 'BIEN_IMMOBILIER', 'BATIMENT_25_ANS', 80000.00, 'Maison avec terrain', NOW(), NOW()),
('60000000-0000-0000-0000-000000000005', '00000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000101', 'Peinture et sols', 5000.00, '2022-06-01', 5, 'TRAVAUX', 'TRAVAUX_5_ANS', NULL, 'Refaire peinture et sols', NOW(), NOW());

-- Immobilisations pour Benoit
INSERT INTO immobilisation (id, utilisateur_id, propriete_id, intitule, montant, date_acquisition, duree_amortissement, type_immobilisation, categorie_fiscale, valeur_terrain, commentaire, cree_le, modifie_le) VALUES
('60000000-0000-0000-0000-000000000006', '00000000-0000-0000-0000-000000000005', '10000000-0000-0000-0000-000000000003', 'Mobilier chambre', 3000.00, '2021-06-15', 10, 'MOBILIER', 'MOBILIER_10_ANS', NULL, 'Lit, armoire, bureau', NOW(), NOW()),
('60000000-0000-0000-0000-000000000007', '00000000-0000-0000-0000-000000000005', '10000000-0000-0000-0000-000000000003', 'Climatisation', 4000.00, '2021-07-01', 5, 'EQUIPEMENT', 'EQUIPEMENT_5_ANS', NULL, 'Système de climatisation réversible', NOW(), NOW());

-- Immobilisations pour Laura
INSERT INTO immobilisation (id, utilisateur_id, propriete_id, intitule, montant, date_acquisition, duree_amortissement, type_immobilisation, categorie_fiscale, valeur_terrain, commentaire, cree_le, modifie_le) VALUES
('60000000-0000-0000-0000-000000000008', '00000000-0000-0000-0000-000000000006', '10000000-0000-0000-0000-000000000004', 'Villa complète', 400000.00, '2020-03-15', 50, 'BIEN_IMMOBILIER', 'BATIMENT_50_ANS', 120000.00, 'Villa avec grand terrain', NOW(), NOW()),
('60000000-0000-0000-0000-000000000009', '00000000-0000-0000-0000-000000000006', '10000000-0000-0000-0000-000000000004', 'Piscine', 25000.00, '2020-04-01', 10, 'TRAVAUX', 'TRAVAUX_10_ANS', NULL, 'Piscine enterrée avec système de filtration', NOW(), NOW());

-- ===== AMORTISSEMENTS =====

-- Amortissements pour l'immobilisation 1 (Rénovation cuisine - 10 ans)
INSERT INTO amortissement (id, immobilisation_id, annee, montant_amortissement, valeur_residuelle, cumul_amortissements, taux_amortissement, cree_le, modifie_le) VALUES
('70000000-0000-0000-0000-000000000001', '60000000-0000-0000-0000-000000000001', 2023, 1500.00, 13500.00, 1500.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000002', '60000000-0000-0000-0000-000000000001', 2024, 1500.00, 12000.00, 3000.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000003', '60000000-0000-0000-0000-000000000001', 2025, 1500.00, 10500.00, 4500.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000004', '60000000-0000-0000-0000-000000000001', 2026, 1500.00, 9000.00, 6000.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000005', '60000000-0000-0000-0000-000000000001', 2027, 1500.00, 7500.00, 7500.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000006', '60000000-0000-0000-0000-000000000001', 2028, 1500.00, 6000.00, 9000.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000007', '60000000-0000-0000-0000-000000000001', 2029, 1500.00, 4500.00, 10500.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000008', '60000000-0000-0000-0000-000000000001', 2030, 1500.00, 3000.00, 12000.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000009', '60000000-0000-0000-0000-000000000001', 2031, 1500.00, 1500.00, 13500.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000010', '60000000-0000-0000-0000-000000000001', 2032, 1500.00, 0.00, 15000.00, 10.00, NOW(), NOW());

-- Amortissements pour l'immobilisation 2 (Mobilier salon - 10 ans)
INSERT INTO amortissement (id, immobilisation_id, annee, montant_amortissement, valeur_residuelle, cumul_amortissements, taux_amortissement, cree_le, modifie_le) VALUES
('70000000-0000-0000-0000-000000000011', '60000000-0000-0000-0000-000000000002', 2023, 800.00, 7200.00, 800.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000012', '60000000-0000-0000-0000-000000000002', 2024, 800.00, 6400.00, 1600.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000013', '60000000-0000-0000-0000-000000000002', 2025, 800.00, 5600.00, 2400.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000014', '60000000-0000-0000-0000-000000000002', 2026, 800.00, 4800.00, 3200.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000015', '60000000-0000-0000-0000-000000000002', 2027, 800.00, 4000.00, 4000.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000016', '60000000-0000-0000-0000-000000000002', 2028, 800.00, 3200.00, 4800.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000017', '60000000-0000-0000-0000-000000000002', 2029, 800.00, 2400.00, 5600.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000018', '60000000-0000-0000-0000-000000000002', 2030, 800.00, 1600.00, 6400.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000019', '60000000-0000-0000-0000-000000000002', 2031, 800.00, 800.00, 7200.00, 10.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000020', '60000000-0000-0000-0000-000000000002', 2032, 800.00, 0.00, 8000.00, 10.00, NOW(), NOW());

-- Amortissements pour l'immobilisation 4 (Maison Bordeaux - 25 ans, base amortissable = 350000 - 80000 = 270000)
INSERT INTO amortissement (id, immobilisation_id, annee, montant_amortissement, valeur_residuelle, cumul_amortissements, taux_amortissement, cree_le, modifie_le) VALUES
('70000000-0000-0000-0000-000000000021', '60000000-0000-0000-0000-000000000004', 2023, 10800.00, 259200.00, 10800.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000022', '60000000-0000-0000-0000-000000000004', 2024, 10800.00, 248400.00, 21600.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000023', '60000000-0000-0000-0000-000000000004', 2025, 10800.00, 237600.00, 32400.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000024', '60000000-0000-0000-0000-000000000004', 2026, 10800.00, 226800.00, 43200.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000025', '60000000-0000-0000-0000-000000000004', 2027, 10800.00, 216000.00, 54000.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000026', '60000000-0000-0000-0000-000000000004', 2028, 10800.00, 205200.00, 64800.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000027', '60000000-0000-0000-0000-000000000004', 2029, 10800.00, 194400.00, 75600.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000028', '60000000-0000-0000-0000-000000000004', 2030, 10800.00, 183600.00, 86400.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000029', '60000000-0000-0000-0000-000000000004', 2031, 10800.00, 172800.00, 97200.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000030', '60000000-0000-0000-0000-000000000004', 2032, 10800.00, 162000.00, 108000.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000031', '60000000-0000-0000-0000-000000000004', 2033, 10800.00, 151200.00, 118800.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000032', '60000000-0000-0000-0000-000000000004', 2034, 10800.00, 140400.00, 129600.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000033', '60000000-0000-0000-0000-000000000004', 2035, 10800.00, 129600.00, 140400.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000034', '60000000-0000-0000-0000-000000000004', 2036, 10800.00, 118800.00, 151200.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000035', '60000000-0000-0000-0000-000000000004', 2037, 10800.00, 108000.00, 162000.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000036', '60000000-0000-0000-0000-000000000004', 2038, 10800.00, 97200.00, 172800.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000037', '60000000-0000-0000-0000-000000000004', 2039, 10800.00, 86400.00, 183600.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000038', '60000000-0000-0000-0000-000000000004', 2040, 10800.00, 75600.00, 194400.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000039', '60000000-0000-0000-0000-000000000004', 2041, 10800.00, 64800.00, 205200.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000040', '60000000-0000-0000-0000-000000000004', 2042, 10800.00, 54000.00, 216000.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000041', '60000000-0000-0000-0000-000000000004', 2043, 10800.00, 43200.00, 226800.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000042', '60000000-0000-0000-0000-000000000004', 2044, 10800.00, 32400.00, 237600.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000043', '60000000-0000-0000-0000-000000000004', 2045, 10800.00, 21600.00, 248400.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000044', '60000000-0000-0000-0000-000000000004', 2046, 10800.00, 10800.00, 259200.00, 4.00, NOW(), NOW()),
('70000000-0000-0000-0000-000000000045', '60000000-0000-0000-0000-000000000004', 2047, 10800.00, 0.00, 270000.00, 4.00, NOW(), NOW());

-- ===== DONNÉES ADDITIONNELLES (CHARGES, RECETTES, CRÉDITS) =====

-- CHARGES supplémentaires pour Yussouf
INSERT INTO charges (id, utilisateur_id, propriete_id, intitule, montant, date_charge, nature, commentaire, cree_le, modifie_le) VALUES
('80000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000001', 'Taxe foncière 2024', 950.00, '2024-10-15', 'TAXES', 'Taxe foncière pour le studio à Paris', NOW(), NOW()),
('80000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000001', 'Charges copropriete T2 2024', 320.00, '2024-04-20', 'COPROPRIETE', 'Régularisation des charges de copropriété', NOW(), NOW()),
('80000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000002', 'Assurance PNO Bordeaux', 180.00, '2024-01-25', 'ASSURANCE', 'Assurance propriétaire non-occupant', NOW(), NOW());

-- RECETTES pour Yussouf
INSERT INTO recettes (id, utilisateur_id, propriete_id, quittance_id, intitule, date_recette, montant, type, commentaire, cree_le, modifie_le) VALUES
('90000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000101', '40000000-0000-0000-0000-000000000101', 'Loyer Mai 2024 - Marseille', '2024-05-05', 770.00, 'LOYER', 'Loyer reçu pour le mois de Mai', NOW(), NOW()),
('90000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000102', '40000000-0000-0000-0000-000000000103', 'Loyer Mai 2024 - Lille', '2024-05-07', 1220.00, 'LOYER', 'Loyer reçu pour le mois de Mai', NOW(), NOW()),
('90000000-0000-0000-0000-000000000003', '00000000-0000-0000-0000-000000000003', '10000000-0000-0000-0000-000000000002', NULL, 'Indemnité assurance DDE', '2024-03-15', 450.00, 'ASSURANCE', 'Remboursement suite à un dégât des eaux', NOW(), NOW());

-- ÉCHÉANCES pour le nouveau crédit
INSERT INTO echeance_credit (id, credit_id, date_echeance, total_echeance, interet, capital_rembourse, assurance) VALUES
('A0000000-0000-0000-0000-000000000001', '50000000-0000-0000-0000-000000000200', '2023-02-01', 1595.55, 560.00, 970.55, 65.00),
('A0000000-0000-0000-0000-000000000002', '50000000-0000-0000-0000-000000000200', '2023-03-01', 1595.55, 558.29, 972.26, 65.00),
('A0000000-0000-0000-0000-000000000003', '50000000-0000-0000-0000-000000000200', '2023-04-01', 1595.55, 556.58, 973.97, 65.00);

-- ÉCRITURES COMPTABLES (ancien modèle - supprimé pour éviter les conflits)
-- Les nouvelles écritures comptables cohérentes sont ajoutées plus bas
-- Suppression des anciens comptes et ajout du plan simplifié LMNP
DELETE FROM compte_comptable;
INSERT INTO compte_comptable (code, libelle, type, description) VALUES
('706000', 'Loyers meublés', 'Recette', 'Recettes locatives (2033-B)'),
('708000', 'Produits accessoires', 'Recette', 'Produits accessoires (2033-B)'),
('606000', 'Achats non stockés de petits matériels', 'Charge', 'Achats de petits équipements, mobilier, fournitures (2033-B)'),
('606300', 'Fournitures administratives', 'Charge', 'Fournitures administratives (2033-B)'),
('615000', 'Entretien et réparations', 'Charge', 'Entretien, réparations (2033-B Charges externes)'),
('616000', 'Primes d''assurances', 'Charge', 'Assurances (2033-B Charges externes)'),
('618000', 'Frais de gestion (honoraires, assistance...)', 'Charge', 'Honoraires, assistance, gestion (2033-B Charges externes)'),
('622000', 'Frais de gestion locative, conciergerie', 'Charge', 'Gestion locative, conciergerie (2033-B Charges externes)'),
('635100', 'Taxe foncière', 'Charge', 'Taxe foncière (2033-B Impôts et taxes)'),
('637000', 'Cotisations CFE ou autres', 'Charge', 'Cotisations CFE, autres impôts (2033-B Impôts et taxes)'),
('661100', 'Intérêts des emprunts', 'Charge', 'Intérêts d''emprunt (2033-B Charges financières)'),
('681100', 'Dotations aux amortissements', 'Charge', 'Dotations aux amortissements (2033-B/2033-C)'),
-- Passif (Capitaux propres et Dettes)
('101000', 'Capital personnel (apports du LMNP)', 'Passif', 'Apports personnels du loueur en meublé'),
('120000', 'Résultat de l''exercice précédent', 'Passif', 'Résultat de l''exercice précédent'),
('129000', 'Résultat en attente d''affectation', 'Passif', 'Résultat non encore affecté'),
('164000', 'Emprunt immobilier', 'Dettes', 'Emprunt bancaire immobilier'),
('401000', 'Fournisseurs', 'Dettes', 'Dettes envers les fournisseurs'),
('445660', 'TVA collectée', 'Dettes', 'TVA collectée à reverser'),
('445661', 'TVA déductible sur biens/services', 'Dettes', 'TVA déductible sur achats de biens et services'),
-- Comptes ajoutés pour compléter le plan LMNP simplifié :
('213000', 'Immobilisations corporelles (biens)', 'Immobilisations', 'Valeur du bien immobilier détenu'),
('215000', 'Mobilier et équipements', 'Immobilisations', 'Mobilier et équipements loués avec le bien'),
('280000', 'Amortissements des immobilisations', 'Immobilisations', 'Suivi des dotations aux amortissements'),
('411000', 'Clients', 'Tiers', 'Créances locataires (loyers non payés)'),
('445662', 'TVA déductible sur biens et services', 'Tiers', 'TVA récupérable sur achats'),
('512000', 'Banque', 'Trésorerie', 'Compte courant bancaire'),
('530000', 'Caisse', 'Trésorerie', 'Espèces détenues (rare en LMNP)');

-- Comptes complémentaires pour la cohérence frontend/backend
INSERT INTO compte_comptable (code, libelle, type, description) VALUES
('614000', 'Charges de copropriété', 'Charge', 'Quote-part propriétaire (hors travaux non déductibles)'),
('616100', 'Assurance emprunteur', 'Charge', 'Assurance emprunteur'),
('622600', 'Frais d''expert-comptable', 'Charge', 'Frais d''expert-comptable'),
('623000', 'Publicité / annonces', 'Charge', 'Publicité et annonces'),
('623100', 'Abonnement logiciel LMNP', 'Charge', 'Abonnement logiciel LMNP'),
('625100', 'Frais de déplacement', 'Charge', 'Frais de déplacement'),
('626000', 'Frais postaux, téléphone', 'Charge', 'Frais postaux, téléphone'),
('627000', 'Frais de dossier ou garantie', 'Charge', 'Frais de dossier ou garantie'),
('635800', 'Cotisation CFE', 'Charge', 'Cotisation CFE'),
('606100', 'Consommation électrique', 'Charge', 'Consommation électrique'),
('606200', 'Consommation d''eau', 'Charge', 'Consommation d''eau'),
('606400', 'Consommation de chauffage', 'Charge', 'Consommation de chauffage'),
('606800', 'Autres charges non classées', 'Charge', 'Autres charges non classées'),
('165000', 'Dépôt de garantie reçu', 'Dettes', 'Dépôt de garantie reçu (passif)'),
('777000', 'Subventions d''exploitation', 'Recette', 'Subventions et aides reçues'),
('758000', 'Produits exceptionnels divers', 'Recette', 'Produits exceptionnels divers');

-- Comptes exceptionnels pour les charges et produits exceptionnels
INSERT INTO compte_comptable (code, libelle, type, description) VALUES
('671000', 'Charges exceptionnelles sur opérations de gestion', 'Charge', 'Charges exceptionnelles liées à la gestion'),
('678000', 'Charges exceptionnelles sur opérations en capital', 'Charge', 'Charges exceptionnelles liées au capital'),
('658000', 'Charges exceptionnelles diverses', 'Charge', 'Autres charges exceptionnelles'),
('675000', 'Valeurs comptables des éléments d''actif cédés', 'Charge', 'Valeurs comptables lors de cessions'),
('675100', 'Moins-values de cession d''éléments d''actif', 'Charge', 'Moins-values lors de cessions d''actifs'),
('775000', 'Produits exceptionnels sur opérations de gestion', 'Recette', 'Produits exceptionnels liés à la gestion'),
('776000', 'Produits exceptionnels sur opérations en capital', 'Recette', 'Produits exceptionnels liés au capital'),
('758100', 'Produits exceptionnels sur opérations de gestion', 'Recette', 'Produits exceptionnels divers'),
('758200', 'Produits exceptionnels sur opérations en capital', 'Recette', 'Produits exceptionnels divers'),
('758300', 'Produits exceptionnels sur cessions d''éléments d''actif', 'Recette', 'Produits exceptionnels sur cessions');

-- ===== ÉCRITURES COMPTABLES COHÉRENTES AVEC LES DONNÉES EXISTANTES =====

-- 1. ÉCRITURE : Enregistrement des loyers reçus (Mai 2024 - Marseille)
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000001', '2024-05-05', 'Encaissement loyer Mai 2024 - Marseille', 'BQ', 'Journal de banque', 'PJ-2024-001', '2024-05-05', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000000101', 'B0000000-0000-0000-0000-000000000001', '512000', 'Banque', 770.00, 0.00, NULL, 'Encaissement loyer'),
('B0000000-0000-0000-0000-000000000102', 'B0000000-0000-0000-0000-000000000001', '706000', 'Loyers meublés', 0.00, 770.00, NULL, 'Loyer Mai 2024 Marseille');

-- 2. ÉCRITURE : Enregistrement des loyers reçus (Mai 2024 - Lille)
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000002', '2024-05-07', 'Encaissement loyer Mai 2024 - Lille', 'BQ', 'Journal de banque', 'PJ-2024-002', '2024-05-07', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000000201', 'B0000000-0000-0000-0000-000000000002', '512000', 'Banque', 1220.00, 0.00, NULL, 'Encaissement loyer'),
('B0000000-0000-0000-0000-000000000202', 'B0000000-0000-0000-0000-000000000002', '706000', 'Loyers meublés', 0.00, 1220.00, NULL, 'Loyer Mai 2024 Lille');

-- 3. ÉCRITURE : Enregistrement de l'indemnité d'assurance (DDE)
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000003', '2024-03-15', 'Indemnité assurance DDE', 'BQ', 'Journal de banque', 'PJ-2024-003', '2024-03-15', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000000301', 'B0000000-0000-0000-0000-000000000003', '512000', 'Banque', 450.00, 0.00, NULL, 'Indemnité reçue'),
('B0000000-0000-0000-0000-000000000302', 'B0000000-0000-0000-0000-000000000003', '708000', 'Produits accessoires', 0.00, 450.00, NULL, 'Indemnité assurance DDE');

-- 4. ÉCRITURE : Enregistrement de la taxe foncière
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000004', '2024-10-15', 'Taxe foncière 2024 - Studio Paris', 'OD', 'Journal des opérations diverses', 'PJ-2024-004', '2024-10-15', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000000401', 'B0000000-0000-0000-0000-000000000004', '635100', 'Taxe foncière', 950.00, 0.00, NULL, 'Taxe foncière 2024'),
('B0000000-0000-0000-0000-000000000402', 'B0000000-0000-0000-0000-000000000004', '512000', 'Banque', 0.00, 950.00, NULL, 'Paiement taxe foncière');

-- 5. ÉCRITURE : Enregistrement des charges de copropriété
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000005', '2024-04-20', 'Charges copropriété T2 2024', 'OD', 'Journal des opérations diverses', 'PJ-2024-005', '2024-04-20', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000000501', 'B0000000-0000-0000-0000-000000000005', '614000', 'Charges de copropriété', 320.00, 0.00, NULL, 'Régularisation charges copropriété'),
('B0000000-0000-0000-0000-000000000502', 'B0000000-0000-0000-0000-000000000005', '512000', 'Banque', 0.00, 320.00, NULL, 'Paiement charges copropriété');

-- 6. ÉCRITURE : Enregistrement de l'assurance PNO
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000006', '2024-01-25', 'Assurance PNO Bordeaux', 'OD', 'Journal des opérations diverses', 'PJ-2024-006', '2024-01-25', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000000601', 'B0000000-0000-0000-0000-000000000006', '616000', 'Primes d''assurances', 180.00, 0.00, NULL, 'Assurance PNO Bordeaux'),
('B0000000-0000-0000-0000-000000000602', 'B0000000-0000-0000-0000-000000000006', '512000', 'Banque', 0.00, 180.00, NULL, 'Paiement assurance PNO');

-- 7. ÉCRITURE : Enregistrement des dotations aux amortissements (2024)
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000007', '2024-12-31', 'Dotations aux amortissements 2024', 'OD', 'Journal des opérations diverses', 'PJ-2024-007', '2024-12-31', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000000701', 'B0000000-0000-0000-0000-000000000007', '681100', 'Dotations aux amortissements', 2300.00, 0.00, NULL, 'Dotations 2024 (1500+800)'),
('B0000000-0000-0000-0000-000000000702', 'B0000000-0000-0000-0000-000000000007', '280000', 'Amortissements des immobilisations', 0.00, 2300.00, NULL, 'Amortissements 2024');

-- 8. ÉCRITURE : Enregistrement des intérêts d'emprunt (échéance février 2023)
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000008', '2023-02-01', 'Échéance emprunt février 2023', 'BQ', 'Journal de banque', 'PJ-2023-001', '2023-02-01', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000000801', 'B0000000-0000-0000-0000-000000000008', '661100', 'Intérêts des emprunts', 560.00, 0.00, NULL, 'Intérêts février 2023'),
('B0000000-0000-0000-0000-000000000802', 'B0000000-0000-0000-0000-000000000008', '164000', 'Emprunt immobilier', 970.55, 0.00, NULL, 'Remboursement capital'),
('B0000000-0000-0000-0000-000000000803', 'B0000000-0000-0000-0000-000000000008', '616100', 'Assurance emprunteur', 65.00, 0.00, NULL, 'Assurance emprunteur'),
('B0000000-0000-0000-0000-000000000804', 'B0000000-0000-0000-0000-000000000008', '512000', 'Banque', 0.00, 1595.55, NULL, 'Paiement échéance');

-- 9. ÉCRITURE : Enregistrement de l'acquisition d'une immobilisation (Rénovation cuisine)
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000009', '2023-02-15', 'Acquisition rénovation cuisine', 'OD', 'Journal des opérations diverses', 'PJ-2023-002', '2023-02-15', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000000901', 'B0000000-0000-0000-0000-000000000009', '215000', 'Mobilier et équipements', 15000.00, 0.00, NULL, 'Rénovation cuisine'),
('B0000000-0000-0000-0000-000000000902', 'B0000000-0000-0000-0000-000000000009', '512000', 'Banque', 0.00, 15000.00, NULL, 'Paiement rénovation');

-- 10. ÉCRITURE : Enregistrement de l'acquisition du mobilier salon
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000010', '2023-03-01', 'Acquisition mobilier salon', 'OD', 'Journal des opérations diverses', 'PJ-2023-003', '2023-03-01', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000001001', 'B0000000-0000-0000-0000-000000000010', '215000', 'Mobilier et équipements', 8000.00, 0.00, NULL, 'Mobilier salon'),
('B0000000-0000-0000-0000-000000001002', 'B0000000-0000-0000-0000-000000000010', '512000', 'Banque', 0.00, 8000.00, NULL, 'Paiement mobilier');

-- 11. ÉCRITURE : Enregistrement de l'acquisition de la maison Bordeaux (immobilisation principale)
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000011', '2023-01-10', 'Acquisition maison Bordeaux', 'OD', 'Journal des opérations diverses', 'PJ-2023-004', '2023-01-10', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000001101', 'B0000000-0000-0000-0000-000000000011', '213000', 'Immobilisations corporelles (biens)', 350000.00, 0.00, NULL, 'Maison Bordeaux'),
('B0000000-0000-0000-0000-000000001102', 'B0000000-0000-0000-0000-000000000011', '164000', 'Emprunt immobilier', 0.00, 320000.00, NULL, 'Emprunt immobilier'),
('B0000000-0000-0000-0000-000000001103', 'B0000000-0000-0000-0000-000000000011', '512000', 'Banque', 0.00, 30000.00, NULL, 'Apport personnel');

-- 12. ÉCRITURE : Enregistrement des frais de notaire et d'agence
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000012', '2023-01-10', 'Frais acquisition maison Bordeaux', 'OD', 'Journal des opérations diverses', 'PJ-2023-005', '2023-01-10', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000001201', 'B0000000-0000-0000-0000-000000000012', '618000', 'Frais de gestion (honoraires, assistance...)', 37000.00, 0.00, NULL, 'Frais notaire et agence'),
('B0000000-0000-0000-0000-000000001202', 'B0000000-0000-0000-0000-000000000012', '512000', 'Banque', 0.00, 37000.00, NULL, 'Paiement frais acquisition');

-- 13. ÉCRITURE : Enregistrement d'un loyer impayé (Avril 2024 - Marseille)
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000013', '2024-04-30', 'Loyer impayé Avril 2024 - Marseille', 'OD', 'Journal des opérations diverses', 'PJ-2024-008', '2024-04-30', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000001301', 'B0000000-0000-0000-0000-000000000013', '411000', 'Clients', 770.00, 0.00, NULL, 'Créance locataire'),
('B0000000-0000-0000-0000-000000001302', 'B0000000-0000-0000-0000-000000000013', '706000', 'Loyers meublés', 0.00, 770.00, NULL, 'Loyer Avril 2024 Marseille');

-- 14. ÉCRITURE : Enregistrement d'un paiement partiel en espèces (non validé)
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000014', '2024-04-10', 'Paiement partiel espèces - Marseille', 'BQ', 'Journal de banque', 'PJ-2024-009', '2024-04-10', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000001401', 'B0000000-0000-0000-0000-000000000014', '530000', 'Caisse', 400.00, 0.00, NULL, 'Paiement partiel espèces'),
('B0000000-0000-0000-0000-000000001402', 'B0000000-0000-0000-0000-000000000014', '411000', 'Clients', 0.00, 400.00, NULL, 'Règlement partiel créance');

-- 15. ÉCRITURE : Enregistrement des charges d'amortissement pour 2023
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000015', '2023-12-31', 'Dotations aux amortissements 2023', 'OD', 'Journal des opérations diverses', 'PJ-2023-006', '2023-12-31', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000001501', 'B0000000-0000-0000-0000-000000000015', '681100', 'Dotations aux amortissements', 2300.00, 0.00, NULL, 'Dotations 2023 (1500+800)'),
('B0000000-0000-0000-0000-000000001502', 'B0000000-0000-0000-0000-000000000015', '280000', 'Amortissements des immobilisations', 0.00, 2300.00, NULL, 'Amortissements 2023');

-- 16. ÉCRITURE : Enregistrement du dépôt de garantie reçu
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000016', '2023-01-01', 'Dépôt de garantie reçu - Studio Paris', 'BQ', 'Journal de banque', 'PJ-2023-007', '2023-01-01', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000001601', 'B0000000-0000-0000-0000-000000000016', '512000', 'Banque', 1000.00, 0.00, NULL, 'Dépôt de garantie reçu'),
('B0000000-0000-0000-0000-000000001602', 'B0000000-0000-0000-0000-000000000016', '165000', 'Dépôt de garantie reçu', 0.00, 1000.00, NULL, 'Dépôt de garantie locataire');

-- 17. ÉCRITURE : Enregistrement des frais de gestion locative
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000017', '2024-06-15', 'Frais de gestion locative', 'OD', 'Journal des opérations diverses', 'PJ-2024-010', '2024-06-15', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000001701', 'B0000000-0000-0000-0000-000000000017', '622000', 'Frais de gestion locative, conciergerie', 150.00, 0.00, NULL, 'Frais de gestion locative'),
('B0000000-0000-0000-0000-000000001702', 'B0000000-0000-0000-0000-000000000017', '512000', 'Banque', 0.00, 150.00, NULL, 'Paiement frais gestion');

-- 18. ÉCRITURE : Enregistrement des fournitures administratives
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000018', '2024-07-20', 'Fournitures administratives', 'OD', 'Journal des opérations diverses', 'PJ-2024-011', '2024-07-20', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000001801', 'B0000000-0000-0000-0000-000000000018', '606300', 'Fournitures administratives', 85.00, 0.00, NULL, 'Fournitures administratives'),
('B0000000-0000-0000-0000-000000001802', 'B0000000-0000-0000-0000-000000000018', '512000', 'Banque', 0.00, 85.00, NULL, 'Paiement fournitures');

-- 19. ÉCRITURE : Enregistrement des travaux d'entretien
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000019', '2024-08-10', 'Travaux entretien - Studio Paris', 'OD', 'Journal des opérations diverses', 'PJ-2024-012', '2024-08-10', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000001901', 'B0000000-0000-0000-0000-000000000019', '615000', 'Entretien et réparations', 450.00, 0.00, NULL, 'Travaux entretien'),
('B0000000-0000-0000-0000-000000001902', 'B0000000-0000-0000-0000-000000000019', '512000', 'Banque', 0.00, 450.00, NULL, 'Paiement travaux');

-- 20. ÉCRITURE : Enregistrement de la cotisation CFE
INSERT INTO ecriture_comptable (id, date_ecriture, libelle, journal_code, journal_lib, numero_piece, piece_date, utilisateur_id, created_at) VALUES
('B0000000-0000-0000-0000-000000000020', '2024-12-15', 'Cotisation CFE 2024', 'OD', 'Journal des opérations diverses', 'PJ-2024-013', '2024-12-15', '00000000-0000-0000-0000-000000000003', NOW());

INSERT INTO ligne_ecriture (id, ecriture_id, compte_num, compte_libelle, debit, credit, tiers, commentaire) VALUES
('B0000000-0000-0000-0000-000000002001', 'B0000000-0000-0000-0000-000000000020', '635800', 'Cotisation CFE', 120.00, 0.00, NULL, 'Cotisation CFE 2024'),
('B0000000-0000-0000-0000-000000002002', 'B0000000-0000-0000-0000-000000000020', '512000', 'Banque', 0.00, 120.00, NULL, 'Paiement CFE');
