const API_BASE_URL = import.meta.env.VITE_SERVICE_BASE_URL

export interface ImmobilisationDetail {
  intitule: string
  proprieteNom: string
  montantBrut: number
  amortissementsCumules: number
  valeurNette: number
  typeImmobilisation: string
}

export interface EmpruntDetail {
  intitule: string
  proprieteNom: string
  capitalRestantDu: number
  banque: string
  dateFin: string
}

export interface TresorerieDetail {
  compte: string
  solde: number
  dateSolde: string
}

export interface BilanSimplifie {
  immobilisationsDetail: ImmobilisationDetail[]
  empruntsDetail: EmpruntDetail[]
  tresorerieDetail: TresorerieDetail[]
  creancesClients: number
  capital: number
  resultatExercice: number
  dettesFournisseurs: number
}

export const bilanSimplifieApi = {
  async calculerBilanSimplifie(annee: number, utilisateurId: string): Promise<BilanSimplifie> {
    const params = new URLSearchParams()
    params.append('annee', annee.toString())
    params.append('utilisateurId', utilisateurId)

    const response = await fetch(
      `${API_BASE_URL}/api/bilan-simplifie?${params.toString()}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
      },
    )

    if (!response.ok) {
      const error = await response.json().catch(() => ({}))
      throw new Error(error.message || 'Erreur lors du calcul du bilan simplifié')
    }

    return response.json()
  },
} 