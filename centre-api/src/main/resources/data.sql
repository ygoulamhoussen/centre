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
('00000000-0000-0000-0000-000000000001', 'jdupont', 'Dupont', 'Jean', 'jean.dupont@example.com', '$2a$10$O7OD13VYrxx89Eb7bpVS6uSzrv6Skw8XQM9jRZTEvuMZN0z.6CEH.', now(), now()),
('00000000-0000-0000-0000-000000000002', 'cmartin', 'Martin', 'Claire', 'claire.martin@example.com', '$2a$10$O7OD13VYrxx89Eb7bpVS6uSzrv6Skw8XQM9jRZTEvuMZN0z.6CEH.', now(), now()),
('00000000-0000-0000-0000-000000000003', 'Yussouf', 'Martin', 'Claire', 'yussouf.goulamhoussen@gmail.com', '$2a$10$O7OD13VYrxx89Eb7bpVS6uSzrv6Skw8XQM9jRZTEvuMZN0z.6CEH.', now(), now()),
('00000000-0000-0000-0000-000000000004', 'Taha2', 'Martin', 'Claire', 'test@gmail.com', '$2a$10$O7OD13VYrxx89Eb7bpVS6uSzrv6Skw8XQM9jRZTEvuMZN0z.6CEH.', now(), now()),
('00000000-0000-0000-0000-000000000005', 'bmorel', 'Morel', 'Benoit', 'benoit.morel@example.com', '$2a$10$O7OD13VYrxx89Eb7bpVS6uSzrv6Skw8XQM9jRZTEvuMZN0z.6CEH.', now(), now()),
('00000000-0000-0000-0000-000000000006', 'lperrin', 'Perrin', 'Laura', 'laura.perrin@example.com', '$2a$10$O7OD13VYrxx89Eb7bpVS6uSzrv6Skw8XQM9jRZTEvuMZN0z.6CEH.', now(), now());


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



-- Crédits pour les nouvelles propriétés de Yussouf
INSERT INTO credit (id, propriete_id, banque, montant_emprunte, date_debut, date_fin, duree_mois, taux_interet_annuel, mensualite, assurance_mensuelle, frais_dossier, frais_garantie, cree_le, modifie_le) VALUES
('50000000-0000-0000-0000-000000000101', '10000000-0000-0000-0000-000000000101', 'Banque D', 120000, '2021-02-01', '2036-02-01', 180, 1.3, 650, 20, 300, 700, now(), now()),
('50000000-0000-0000-0000-000000000102', '10000000-0000-0000-0000-000000000102', 'Banque E', 250000, '2020-09-01', '2040-09-01', 240, 1.0, 1100, 35, 500, 1000, now(), now());


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



-- ===== DONNÉES ADDITIONNELLES (CHARGES, RECETTES, CRÉDITS) =====



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

-- Comptes comptables pour immobilisations (ajoutés pour conformité)
INSERT INTO compte_comptable (code, libelle, type, description) VALUES
('213100', 'Immeubles', 'Immobilisations', 'Immeubles bâtis et terrains'),
('218100', 'Mobilier', 'Immobilisations', 'Mobilier et équipements'),
('213500', 'Agencements/Travaux', 'Immobilisations', 'Travaux, aménagements, agencements'),
('215400', 'Matériel', 'Immobilisations', 'Matériel d''équipement'),
('203000', 'Frais d''acquisition', 'Immobilisations', 'Frais d''acquisition d''immobilisation');

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



-- Ajout du compte comptable 211000 (Terrains)
INSERT INTO compte_comptable (code, libelle, type, description) VALUES ('211000', 'Terrains', 'Immobilisations', 'Terrains non amortissables');
