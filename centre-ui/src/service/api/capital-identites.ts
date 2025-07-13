const API_BASE_URL = import.meta.env.VITE_SERVICE_BASE_URL

export interface CapitalIdentites {
  id?: string
  nomExploitant: string
  prenomExploitant: string
  adresseExploitant: string
  codePostalExploitant: string
  villeExploitant: string
  qualite: string
  apportsNumeraires: number
  apportsNature: number
  apportsIndustrie: number
  partsDetenues: string
  totalCapital: number
  utilisateurId: string
  dateCreation?: string
  dateModification?: string
}

export const capitalIdentitesApi = {
  // Récupérer les informations de capital et identités d'un utilisateur
  async getByUtilisateur(utilisateurId: string): Promise<CapitalIdentites | null> {
    const response = await fetch(
      `${API_BASE_URL}/api/capital-identites/${utilisateurId}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
      },
    )

    if (!response.ok) {
      if (response.status === 404) {
        return null // Aucune donnée trouvée
      }
      throw new Error('Erreur lors de la récupération des informations de capital et identités')
    }

    return response.json()
  },

  // Créer de nouvelles informations de capital et identités
  async create(capitalIdentites: Omit<CapitalIdentites, 'id'>): Promise<CapitalIdentites> {
    const response = await fetch(
      `${API_BASE_URL}/api/capital-identites`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(capitalIdentites),
      },
    )

    if (!response.ok) {
      const error = await response.json().catch(() => ({}))
      throw new Error(error.message || 'Erreur lors de la création des informations de capital et identités')
    }

    return response.json()
  },

  // Mettre à jour les informations de capital et identités
  async update(capitalIdentites: CapitalIdentites): Promise<CapitalIdentites> {
    const response = await fetch(
      `${API_BASE_URL}/api/capital-identites`,
      {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(capitalIdentites),
      },
    )

    if (!response.ok) {
      const error = await response.json().catch(() => ({}))
      throw new Error(error.message || 'Erreur lors de la mise à jour des informations de capital et identités')
    }

    return response.json()
  },

  // Supprimer les informations de capital et identités
  async delete(id: string): Promise<void> {
    const response = await fetch(
      `${API_BASE_URL}/api/capital-identites/${id}`,
      {
        method: 'DELETE',
        credentials: 'include',
      },
    )

    if (!response.ok) {
      const error = await response.json().catch(() => ({}))
      throw new Error(error.message || 'Erreur lors de la suppression des informations de capital et identités')
    }
  },
} 