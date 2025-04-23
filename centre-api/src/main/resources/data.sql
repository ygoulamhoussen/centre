-- Jeux de données pour LMNP - PostgreSQL

-- Utilisateurs
INSERT INTO utilisateur (id, user_name, nom, prenom, email, mot_de_passe_hash, cree_le, modifie_le) VALUES
('00000000-0000-0000-0000-000000000001', 'jdupont', 'Dupont', 'Jean', 'jean.dupont@example.com', 'hash1', now(), now()),
('00000000-0000-0000-0000-000000000002', 'cmartin', 'Martin', 'Claire', 'claire.martin@example.com', 'hash2', now(), now()),
('00000000-0000-0000-0000-000000000003', 'Soybean', 'Martin', 'Claire', 'soybean@example.com', '$2a$10$q2ObuUCf3xqjlH0gAvdZd.g9tC2kRz0Z01nlnhUp7u82FcbQQRCbO', now(), now());

-- Propriétés
INSERT INTO propriete (id, utilisateur_id, type_bien, nom, adresse, complement_adresse, code_postal, ville,
    date_acquisition, date_livraison, montant_acquisition, tantieme, frais_notaire, frais_agence, cree_le, modifie_le) VALUES
('10000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'APPARTEMENT', 'Studio Paris 15e',
    '10 rue de Vaugirard', '', '75015', 'Paris', '2022-06-15', '2022-07-01', 220000, 100, 15000, 8000, now(), now()),
('10000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000002', 'MAISON', 'Maison Bordeaux',
    '15 avenue des Vins', '', '33000', 'Bordeaux', '2023-01-10', '2023-03-01', 350000, 100, 25000, 12000, now(), now());

-- LOCATAIRES (mise à jour : utilisateur_id au lieu de propriete_id)
INSERT INTO locataire (id, utilisateur_id, nom, telephone, email, adresse, complement_adresse, code_postal, ville, cree_le, modifie_le) VALUES
('20000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Alice Laurent', '0601010101', 'alice@example.com', '1 rue des Lilas', '', '75015', 'Paris', now(), now()),
('20000000-0000-0000-0000-000000000002', '00000000-0000-0000-0000-000000000002', 'Marc Durant', '0602020202', 'marc@example.com', '2 rue des Vignes', '', '33000', 'Bordeaux', now(), now());

-- Location
INSERT INTO location (id, propriete_id, locataire_id, date_debut, date_fin, loyer_mensuel, charges_mensuelles, depot_garantie, frequence_loyer, jour_echeance, cree_le, modifie_le) VALUES
('30000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', '20000000-0000-0000-0000-000000000001',
    '2023-01-01', null, 900, 100, 1000, 'MENSUEL', 5, now(), now()),
('30000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000002', '20000000-0000-0000-0000-000000000002',
    '2023-02-01', null, 1200, 150, 1500, 'MENSUEL', 10, now(), now());

-- Composition Acquisition
INSERT INTO composition_acquisition (id, propriete_id, categorie, montant, description, cree_le, modifie_le) VALUES
('40000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', 'TERRAIN', 80000, 'Terrain brut', now(), now()),
('40000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000001', 'CONSTRUCTION', 120000, 'Construction neuve', now(), now());

-- Crédits
INSERT INTO credit (id, propriete_id, banque, montant_emprunte, date_debut, date_fin, duree_mois, taux_interet_annuel, mensualite, assurance_mensuelle, frais_dossier, frais_garantie, cree_le, modifie_le) VALUES
('50000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', 'Banque A', 200000, '2022-06-01', '2037-06-01', 180, 1.2, 1000, 30, 500, 1000, now(), now());

-- QUITTANCE
INSERT INTO quittance (id, location_id, date_debut, date_fin, date_emission, montant_loyer, montant_charges, montant_total, statut, cree_le, modifie_le) VALUES
('40000000-0000-0000-0000-000000000001', '30000000-0000-0000-0000-000000000001', '2024-04-01', '2024-04-30', '2024-04-01', 900.00, 100.00, 1000.00, 'PAYÉE', NOW(), NOW());

-- PAIEMENT
INSERT INTO paiement (id, quittance_id, date_paiement, montant, moyen_paiement, reference, commentaire, est_valide, cree_le, modifie_le) VALUES
('50000000-0000-0000-0000-000000000001', '40000000-0000-0000-0000-000000000001', '2024-04-03', 1000.00, 'VIREMENT', 'VIR123456', 'Paiement complet', true, NOW(), NOW());

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
