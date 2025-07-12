const API_BASE_URL = import.meta.env.VITE_SERVICE_BASE_URL

export interface RecetteDetail {
  intitule: string
  dateRecette: string // LocalDate is serialized as string
  montant: number
  proprieteNom: string
}

export interface ChargeDetail {
  intitule: string
  dateCharge: string // LocalDate is serialized as string
  montant: number
  proprieteNom: string
  nature: string
}

export interface AmortissementDetail {
  immobilisationIntitule: string
  annee: number
  montantAmortissement: number
  proprieteNom: string
}

export interface ResultatFiscal {
  recettesLocatives: number
  chargesDeductibles: number
  amortissements: number
  resultatFiscal: number
  recettesDetail: RecetteDetail[]
  chargesDetail: ChargeDetail[]
  amortissementsDetail: AmortissementDetail[]
}

export const resultatFiscalApi = {
  async calculerResultatFiscal(annee: number, utilisateurId: string): Promise<ResultatFiscal> {
    const params = new URLSearchParams()
    params.append('annee', annee.toString())
    params.append('utilisateurId', utilisateurId)

    const response = await fetch(
      `${API_BASE_URL}/api/resultat-fiscal?${params.toString()}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
      },
    )

    if (!response.ok) {
      const error = await response.json().catch(() => ({}))
      throw new Error(error.message || 'Erreur lors du calcul du résultat fiscal')
    }

    return response.json()
  },
} 