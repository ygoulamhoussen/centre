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
