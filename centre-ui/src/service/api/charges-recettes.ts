import type { ChargeDTO, RecetteDTO, EcritureComptableDTO } from '@/types/dto'

const API_BASE_URL = import.meta.env.VITE_SERVICE_BASE_URL

// ===== SERVICES POUR LES CHARGES =====

/**
 * Récupérer toutes les charges d'un utilisateur
 * @param utilisateurId ID de l'utilisateur
 */
export async function fetchCharges(utilisateurId: string): Promise<ChargeDTO[]> {
  const response = await fetch(
    `${API_BASE_URL}/api/getChargesByUtilisateur/${utilisateurId}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des charges')
  }
  
  return response.json()
}

/**
 * Récupérer les charges d'une propriété
 * @param proprieteId ID de la propriété
 */
export async function fetchChargesByPropriete(proprieteId: string): Promise<ChargeDTO[]> {
  const response = await fetch(
    `${API_BASE_URL}/api/getChargesByPropriete/${proprieteId}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des charges de la propriété')
  }
  
  return response.json()
}

/**
 * Créer une nouvelle charge
 * @param data Données de la charge
 */
export async function createCharge(data: Partial<ChargeDTO>): Promise<ChargeDTO> {
  const response = await fetch(
    `${API_BASE_URL}/api/charges`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(data)
    }
  )
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || 'Erreur lors de la création de la charge')
  }
  
  return response.json()
}

/**
 * Mettre à jour une charge existante
 * @param id ID de la charge
 * @param data Données mises à jour
 */
export async function updateCharge(id: string, data: Partial<ChargeDTO>): Promise<ChargeDTO> {
  const response = await fetch(
    `${API_BASE_URL}/api/charges`,
    {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ ...data, id })
    }
  )
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || 'Erreur lors de la mise à jour de la charge')
  }
  
  return response.json()
}

/**
 * Supprimer une charge
 * @param id ID de la charge
 */
export async function deleteCharge(id: string): Promise<void> {
  const response = await fetch(
    `${API_BASE_URL}/api/charges/${id}`,
    {
      method: 'DELETE',
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || 'Erreur lors de la suppression de la charge')
  }
}

// ===== SERVICES POUR LES RECETTES =====

/**
 * Récupérer toutes les recettes d'un utilisateur
 * @param utilisateurId ID de l'utilisateur
 */
export async function fetchRecettes(utilisateurId: string): Promise<RecetteDTO[]> {
  const response = await fetch(
    `${API_BASE_URL}/api/getRecettesByUtilisateur/${utilisateurId}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des recettes')
  }
  
  return response.json()
}

/**
 * Récupérer les recettes d'une propriété
 * @param proprieteId ID de la propriété
 */
export async function fetchRecettesByPropriete(proprieteId: string): Promise<RecetteDTO[]> {
  const response = await fetch(
    `${API_BASE_URL}/api/getRecettesByPropriete/${proprieteId}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des recettes de la propriété')
  }
  
  return response.json()
}

/**
 * Créer une nouvelle recette
 * @param data Données de la recette
 */
export async function createRecette(data: Partial<RecetteDTO>): Promise<RecetteDTO> {
  const response = await fetch(
    `${API_BASE_URL}/api/recettes`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(data)
    }
  )
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || 'Erreur lors de la création de la recette')
  }
  
  return response.json()
}

/**
 * Mettre à jour une recette existante
 * @param id ID de la recette
 * @param data Données mises à jour
 */
export async function updateRecette(id: string, data: Partial<RecetteDTO>): Promise<RecetteDTO> {
  const response = await fetch(
    `${API_BASE_URL}/api/recettes`,
    {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify({ ...data, id })
    }
  )
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || 'Erreur lors de la mise à jour de la recette')
  }
  
  return response.json()
}

/**
 * Supprimer une recette
 * @param id ID de la recette
 */
export async function deleteRecette(id: string): Promise<void> {
  const response = await fetch(
    `${API_BASE_URL}/api/recettes/${id}`,
    {
      method: 'DELETE',
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || 'Erreur lors de la suppression de la recette')
  }
}

// ===== SERVICES POUR LES ÉCRITURES COMPTABLES =====

/**
 * Récupérer les écritures comptables d'une propriété pour une année fiscale
 * @param proprieteId ID de la propriété
 * @param anneeFiscale Année fiscale
 */
export async function fetchEcrituresComptables(proprieteId: string, anneeFiscale: number): Promise<EcritureComptableDTO[]> {
  const response = await fetch(
    `${API_BASE_URL}/api/ecritures-comptables/${proprieteId}/${anneeFiscale}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des écritures comptables')
  }
  
  return response.json()
}

/**
 * Récupérer toutes les écritures comptables d'un utilisateur
 * @param utilisateurId ID de l'utilisateur
 */
export async function fetchEcrituresComptablesByUtilisateur(utilisateurId: string): Promise<EcritureComptableDTO[]> {
  const response = await fetch(
    `${API_BASE_URL}/api/ecritures-comptables/utilisateur/${utilisateurId}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des écritures comptables')
  }
  
  return response.json()
}

/**
 * Récupérer les écritures comptables d'un utilisateur pour une année fiscale
 * @param utilisateurId ID de l'utilisateur
 * @param anneeFiscale Année fiscale
 */
export async function fetchEcrituresComptablesByUtilisateurAndAnnee(utilisateurId: string, anneeFiscale: number): Promise<EcritureComptableDTO[]> {
  const response = await fetch(
    `${API_BASE_URL}/api/ecritures-comptables/utilisateur/${utilisateurId}/${anneeFiscale}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des écritures comptables')
  }
  
  return response.json()
}

/**
 * Créer une écriture comptable automatiquement lors de la création d'une charge
 * @param chargeId ID de la charge
 */
export async function createEcritureComptableCharge(chargeId: string): Promise<EcritureComptableDTO> {
  const response = await fetch(
    `${API_BASE_URL}/api/createEcritureComptableCharge/${chargeId}`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || 'Erreur lors de la création de l\'écriture comptable')
  }
  
  return response.json()
}

/**
 * Créer une écriture comptable automatiquement lors de la création d'une recette
 * @param recetteId ID de la recette
 */
export async function createEcritureComptableRecette(recetteId: string): Promise<EcritureComptableDTO> {
  const response = await fetch(
    `${API_BASE_URL}/api/createEcritureComptableRecette/${recetteId}`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || 'Erreur lors de la création de l\'écriture comptable')
  }
  
  return response.json()
} 