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
  createdAt?: string;
  updatedAt?: string;
}

export interface EcritureComptableDTO {
  id?: string;
  dateEcriture: string;
  montant: string;
  type: 'CHARGE' | 'RECETTE';
  proprieteId: string;
  chargeId?: string;
  recetteId?: string;
  commentaire?: string;
  utilisateurId?: string;
  createdAt?: string;
  updatedAt?: string;
}
