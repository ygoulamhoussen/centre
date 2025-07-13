const API_BASE_URL = import.meta.env.VITE_SERVICE_BASE_URL

export interface ProduitsChargesExceptionnelsDTO {
  utilisateurId: string
  annee: number
  produitsExceptionnels: number
  detailsProduitsExceptionnels: OperationExceptionnelleDTO[]
  chargesExceptionnelles: number
  detailsChargesExceptionnelles: OperationExceptionnelleDTO[]
  plusValuesLongTerme: number
  detailsPlusValues: OperationExceptionnelleDTO[]
  moinsValuesLongTerme: number
  detailsMoinsValues: OperationExceptionnelleDTO[]
  totalOperationsExceptionnelles: number
}

export interface OperationExceptionnelleDTO {
  libelle: string
  dateOperation: string
  montant: number
  commentaire: string
  compteComptable: string
  typeOperation: string
}

export const produitsChargesExceptionnelsApi = {
  async getProduitsChargesExceptionnels(annee: number, utilisateurId: string): Promise<ProduitsChargesExceptionnelsDTO> {
    const response = await fetch(
      `${API_BASE_URL}/api/produits-charges-exceptionnels?annee=${annee}&utilisateurId=${utilisateurId}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include'
      }
    )

    if (!response.ok) {
      throw new Error('Erreur lors de la récupération des opérations exceptionnelles')
    }

    return response.json()
  }
} 