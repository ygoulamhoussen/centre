-- Supprimer les séquences existantes
DROP SEQUENCE IF EXISTS etudiant_id_seq CASCADE;
DROP SEQUENCE IF EXISTS formateur_id_seq CASCADE;
DROP SEQUENCE IF EXISTS formation_id_seq CASCADE;
DROP SEQUENCE IF EXISTS inscription_id_seq CASCADE;
DROP SEQUENCE IF EXISTS salle_id_seq CASCADE;
DROP SEQUENCE IF EXISTS session_id_seq CASCADE;
DROP SEQUENCE IF EXISTS utilisateur_id_seq CASCADE;

-- Créer les séquences
CREATE SEQUENCE etudiant_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE formateur_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE formation_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE inscription_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE salle_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE session_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;
CREATE SEQUENCE utilisateur_id_seq START WITH 1 INCREMENT BY 1 NO MINVALUE NO MAXVALUE CACHE 1;

-- Supprimer les tables existantes
DROP TABLE IF EXISTS public.etudiant CASCADE;
DROP TABLE IF EXISTS public.utilisateur CASCADE;
DROP TABLE IF EXISTS public.formateur CASCADE;
DROP TABLE IF EXISTS public.formation CASCADE;
DROP TABLE IF EXISTS public.inscription CASCADE;
DROP TABLE IF EXISTS public.salle CASCADE;
DROP TABLE IF EXISTS public.session CASCADE;

-- Créer la table etudiant
CREATE TABLE IF NOT EXISTS public.etudiant
(
    id bigint NOT NULL DEFAULT nextval('etudiant_id_seq'::regclass),
    email character varying(255) COLLATE pg_catalog."default",
    nom character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT etudiant_pkey PRIMARY KEY (id)
)
TABLESPACE pg_default;



-- Créer la table formateur
CREATE TABLE IF NOT EXISTS public.formateur
(
    id bigint NOT NULL DEFAULT nextval('formateur_id_seq'::regclass),
    email character varying(255) COLLATE pg_catalog."default",
    nom character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT formateur_pkey PRIMARY KEY (id)
)
TABLESPACE pg_default;



-- Créer la table formation
CREATE TABLE IF NOT EXISTS public.formation
(
    id bigint NOT NULL DEFAULT nextval('formation_id_seq'::regclass),
    description character varying(255) COLLATE pg_catalog."default",
    titre character varying(255) COLLATE pg_catalog."default",
    formateur_id bigint,
    CONSTRAINT formation_pkey PRIMARY KEY (id),
    CONSTRAINT fkdnj87nt2fnhdcd00ryovre4m8 FOREIGN KEY (formateur_id)
        REFERENCES public.formateur (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;



-- Créer la table salle
CREATE TABLE IF NOT EXISTS public.salle
(
    id bigint NOT NULL DEFAULT nextval('salle_id_seq'::regclass),
    capacite integer NOT NULL,
    nom character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT salle_pkey PRIMARY KEY (id)
)
TABLESPACE pg_default;


-- Créer la table session
CREATE TABLE IF NOT EXISTS public.session
(
    id bigint NOT NULL DEFAULT nextval('session_id_seq'::regclass),
    debut timestamp without time zone,
    fin timestamp without time zone,
    formateur_id bigint,
    formation_id bigint,
    salle_id bigint,
    CONSTRAINT session_pkey PRIMARY KEY (id),
    CONSTRAINT fk19wnortskl91nl9d8h4tw7d0x FOREIGN KEY (formateur_id)
        REFERENCES public.formateur (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fk8dw89p64y5vhkpsj2ig3vq95h FOREIGN KEY (salle_id)
        REFERENCES public.salle (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkgxjpjguyud7yk8jb51ygcmay1 FOREIGN KEY (formation_id)
        REFERENCES public.formation (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;





-- Créer la table utilisateur
CREATE TABLE IF NOT EXISTS public.utilisateur
(
    id bigint NOT NULL DEFAULT nextval('utilisateur_id_seq'::regclass),
    email character varying(255) COLLATE pg_catalog."default",
    mot_de_passe character varying(255) COLLATE pg_catalog."default",
    nom character varying(255) COLLATE pg_catalog."default",
    role character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT utilisateur_pkey PRIMARY KEY (id)
)
TABLESPACE pg_default;

-- Créer la table inscription
CREATE TABLE IF NOT EXISTS public.inscription
(
    id bigint NOT NULL DEFAULT nextval('inscription_id_seq'::regclass),
    etudiant_id bigint,
    session_id bigint,
    utilisateur_id bigint,
    CONSTRAINT inscription_pkey PRIMARY KEY (id),
    CONSTRAINT fk7u3x7n97xw83vfemi95yu7oev FOREIGN KEY (etudiant_id)
        REFERENCES public.etudiant (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkb3ckm4ansfumlubaocw95dwep FOREIGN KEY (session_id)
        REFERENCES public.session (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT fkes0mtg0xssltfgqc6p8evlxpy FOREIGN KEY (utilisateur_id)
        REFERENCES public.utilisateur (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)
TABLESPACE pg_default;



