INSERT INTO formateur (id, nom, email) VALUES (1, 'John Doe', 'john.doe@example.com') ON CONFLICT (id) DO NOTHING;
INSERT INTO formation (id, titre, description, formateur_id) VALUES (1, 'Formation 1', 'Description 1', 1) ON CONFLICT (id) DO NOTHING;
INSERT INTO formation (id, titre, description, formateur_id) VALUES (2, 'Formation 2', 'Description 2', 1) ON CONFLICT (id) DO NOTHING;

-- Insert an admin user
INSERT INTO utilisateur (id, nom, email, mot_de_passe, role) VALUES (1, 'Admin', 'admin@example.com', 'adminpassword', 'ADMINISTRATEUR') ON CONFLICT (id) DO NOTHING;
