import type { PaiementDTO } from '@/types/dto'

const API_BASE_URL = import.meta.env.VITE_SERVICE_BASE_URL

/**
 * Get payments by user ID
 * @param utilisateurId User ID
 */
export async function fetchPaiements(utilisateurId: string): Promise<PaiementDTO[]> {
  const response = await fetch(
    `${API_BASE_URL}/api/getPaiementsByUtilisateur/${utilisateurId}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des paiements')
  }
  
  return response.json()
}

/**
 * Create a new payment
 * @param data Payment data
 */
export async function createPaiement(data: Partial<PaiementDTO>): Promise<PaiementDTO> {
  const response = await fetch(
    `${API_BASE_URL}/api/savePaiement`,
    {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(data)
    }
  )
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || 'Erreur lors de la création du paiement')
  }
  
  return response.json()
}

/**
 * Update an existing payment
 * @param id Payment ID
 * @param data Updated payment data
 */
export async function updatePaiement(id: string, data: Partial<PaiementDTO>): Promise<PaiementDTO> {
  // Même endpoint que createPaiement car le backend gère la création/mise à jour dans la même méthode
  return createPaiement({ ...data, id })
}

/**
 * Delete a payment
 * @param id Payment ID
 */
export async function deletePaiement(id: string): Promise<void> {
  const response = await fetch(
    `${API_BASE_URL}/api/deletePaiement/${id}`,
    {
      method: 'DELETE',
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || 'Erreur lors de la suppression du paiement')
  }
}
