const API_BASE_URL = import.meta.env.VITE_SERVICE_BASE_URL

export interface RepartitionChargesDTO {
  utilisateurId: string
  annee: number
  achats: number
  detailsAchats: ChargeDetailDTO[]
  autresChargesExternes: number
  detailsChargesExternes: ChargeDetailDTO[]
  impotsTaxes: number
  detailsImpotsTaxes: ChargeDetailDTO[]
  chargesPersonnel: number
  detailsChargesPersonnel: ChargeDetailDTO[]
  chargesSociales: number
  detailsChargesSociales: ChargeDetailDTO[]
  dotationsAmortissements: number
  detailsAmortissements: ChargeDetailDTO[]
  autresCharges: number
  detailsAutresCharges: ChargeDetailDTO[]
  totalCharges: number
}

export interface ChargeDetailDTO {
  libelle: string
  dateCharge: string
  montant: number
  commentaire: string
  compteComptable: string
}

export const repartitionChargesApi = {
  async getRepartitionCharges(annee: number, utilisateurId: string): Promise<RepartitionChargesDTO> {
    const response = await fetch(
      `${API_BASE_URL}/api/repartition-charges?annee=${annee}&utilisateurId=${utilisateurId}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include'
      }
    )

    if (!response.ok) {
      throw new Error('Erreur lors de la récupération de la répartition des charges')
    }

    return response.json()
  }
} 