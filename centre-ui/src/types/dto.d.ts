export interface PaiementDTO {
  id?: string;
  quittanceId: string;
  datePaiement: string;
  montant: string;
  moyenPaiement: string;
  reference?: string;
  commentaire?: string;
  estValide: string;
  utilisateurId?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface ChargeDTO {
  id?: string;
  intitule: string;
  montant: string;
  dateCharge: string;
  proprieteId: string;
  nature: string;
  commentaire?: string;
  utilisateurId?: string;
  documentId?: string;
  documentNom?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface RecetteDTO {
  id?: string;
  intitule: string;
  montant: string;
  dateRecette: string;
  proprieteId: string;
  type: 'LOCATION' | 'EXCEPTIONNELLE' | 'QUITTANCE';
  quittanceId?: string;
  commentaire?: string;
  utilisateurId?: string;
  documentId?: string;
  documentNom?: string;
  createdAt?: string;
  updatedAt?: string;
}

export interface LigneEcritureDTO {
  id?: string;
  ecritureId?: string;
  compteNum: string;
  compteLibelle: string;
  debit: string;
  credit: string;
  tiers?: string;
  commentaire?: string;
}

export interface EcritureComptableDTO {
  id?: string;
  dateEcriture: string;
  journalCode: string;
  journalLib?: string;
  pieceDate?: string;
  numeroPiece?: string;
  libelle: string;
  lignes?: LigneEcritureDTO[];
  // autres champs existants si besoin
}

export interface DocumentDTO {
  id?: string;
  titre?: string;
  nomFichier?: string;
  mimeType?: string;
  taille?: number;
  urlFichier?: string;
  dateDocument?: string;
  typeDocument?: string;
  proprieteId?: string;
  utilisateurId?: string;
  createdAt?: string;
  updatedAt?: string;
}
