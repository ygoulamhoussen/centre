-- Données de test : utilisateurs
INSERT INTO utilisateur (id, nom, prenom, email, mot_de_passe_hash, cree_le, modifie_le) VALUES
  ('00000000-0000-0000-0000-000000000001', 'Durand', 'Marie', 'marie.durand@example.com', 'hash1', NOW(), NOW()),
  ('00000000-0000-0000-0000-000000000002', 'Lemoine', 'Julien', 'julien.lemoine@example.com', 'hash2', NOW(), NOW());


-- Données de test : propriété
INSERT INTO propriete (id, utilisateur_id, type_bien, nom, adresse, complement_adresse, code_postal, ville, date_acquisition, date_livraison, montant_acquisition, tantieme, frais_notaire, frais_agence, cree_le, modifie_le) VALUES
  ('10000000-0000-0000-0000-000000000001', '00000000-0000-0000-0000-000000000001', 'Appartement', 'Appartement Paris 11', '10 rue Oberkampf', 'Bât A', '75011', 'Paris', '2022-06-15', '2022-08-01', 300000, 50, 20000, 10000, NOW(), NOW());

-- Données de test : composition
INSERT INTO composition_acquisition (id, propriete_id, categorie, montant, description, cree_le, modifie_le) VALUES
  ('20000000-0000-0000-0000-000000000001', '10000000-0000-0000-0000-000000000001', 'Terrain', 45000, 'Estimation terrain 15%', NOW(), NOW()),
  ('20000000-0000-0000-0000-000000000002', '10000000-0000-0000-0000-000000000001', 'Construction', 120000, 'Structure du bâtiment', NOW(), NOW());