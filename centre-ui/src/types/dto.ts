export interface SoldesIntermediairesGestionDTO {
  utilisateurId: string
  annee: number
  productionExercice: number
  consommationTiers: number
  valeurAjoutee: number
  chargesPersonnel: number
  excedentBrutExploitation: number
  dotationsAmortissements: number
  dotationsProvisions: number
  resultatExploitation: number
  chargesFinancieres: number
  produitsFinanciers: number
  resultatCourantAvantImpot: number
  produitsExceptionnels: number
  chargesExceptionnelles: number
  resultatExceptionnel: number
  impotsSurBenefices: number
  resultatNet: number
  detailsProduction: DetailSoldeDTO[]
  detailsConsommation: DetailSoldeDTO[]
  detailsChargesPersonnel: DetailSoldeDTO[]
  detailsChargesFinancieres: DetailSoldeDTO[]
  detailsExceptionnel: DetailSoldeDTO[]
}

export interface DetailSoldeDTO {
  libelle: string
  date: string
  montant: number
  compteNum: string
  commentaire: string
} 