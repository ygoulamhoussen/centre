export interface Immobilisation {
  id: string;
  intitule: string;
  montant: string;
  dateAcquisition: string;
  dureeAmortissement: string;
  typeImmobilisation: TypeImmobilisation;
  categorieFiscale: CategorieFiscale;
  valeurTerrain?: string;
  commentaire?: string;
  proprieteId: string;
  proprieteNom?: string;
  utilisateurId: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface Amortissement {
  id: string;
  immobilisationId: string;
  immobilisationIntitule?: string;
  annee: string;
  montantAmortissement: string;
  valeurResiduelle: string;
  cumulAmortissements: string;
  tauxAmortissement: string;
  createdAt?: string;
  updatedAt?: string;
}

export enum TypeImmobilisation {
  BIEN_IMMOBILIER = 'BIEN_IMMOBILIER',
  MOBILIER = 'MOBILIER',
  TRAVAUX = 'TRAVAUX',
  EQUIPEMENT = 'EQUIPEMENT',
  AUTRE = 'AUTRE'
}

export enum CategorieFiscale {
  BATIMENT_50_ANS = 'BATIMENT_50_ANS',
  BATIMENT_25_ANS = 'BATIMENT_25_ANS',
  MOBILIER_10_ANS = 'MOBILIER_10_ANS',
  MOBILIER_5_ANS = 'MOBILIER_5_ANS',
  TRAVAUX_10_ANS = 'TRAVAUX_10_ANS',
  TRAVAUX_5_ANS = 'TRAVAUX_5_ANS',
  EQUIPEMENT_5_ANS = 'EQUIPEMENT_5_ANS',
  EQUIPEMENT_3_ANS = 'EQUIPEMENT_3_ANS'
}

export interface Credit {
  id: string;
  banque: string;
  montantEmprunte: number;
  dureeMois: number;
  tauxInteretAnnuel: number;
  tauxAssuranceAnnuel: number;
  dateDebut: string;
  intitule: string;
  proprieteId: string;
  proprieteNom?: string;
  utilisateurId: string;
  createdAt?: string;
  updatedAt?: string;
} 