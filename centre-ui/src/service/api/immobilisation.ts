import type { Amortissement, Credit, Immobilisation } from '@/types/immobilisation'
import { request } from '../request'

const API_BASE_URL = import.meta.env.VITE_SERVICE_BASE_URL

export const immobilisationApi = {
  // Récupérer toutes les immobilisations d'un utilisateur
  async getImmobilisationsByUtilisateur(utilisateurId: string): Promise<Immobilisation[]> {
    const response = await fetch(
      `${API_BASE_URL}/api/immobilisations/${utilisateurId}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
      },
    )

    if (!response.ok) {
      throw new Error('Erreur lors de la récupération des immobilisations')
    }

    return response.json()
  },

  // Récupérer les immobilisations d'une propriété
  async getImmobilisationsByPropriete(proprieteId: string): Promise<Immobilisation[]> {
    const response = await fetch(
      `${API_BASE_URL}/api/immobilisations/propriete/${proprieteId}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
      },
    )

    if (!response.ok) {
      throw new Error('Erreur lors de la récupération des immobilisations de la propriété')
    }

    return response.json()
  },

  // Créer une nouvelle immobilisation
  async createImmobilisation(immobilisation: Omit<Immobilisation, 'id' | 'createdAt' | 'updatedAt'>): Promise<Immobilisation> {
    console.error('=== CRÉATION IMMOBILISATION FRONTEND ===')
    console.error('URL:', `${API_BASE_URL}/api/immobilisations`)
    console.error('Données envoyées:', immobilisation)

    const response = await fetch(
      `${API_BASE_URL}/api/immobilisations`,
      {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(immobilisation),
      },
    )

    console.error('Status de la réponse:', response.status)
    console.error('Headers de la réponse:', response.headers)

    if (!response.ok) {
      const error = await response.json().catch(() => ({}))
      console.error('Erreur de réponse:', error)
      throw new Error(error.message || 'Erreur lors de la création de l\'immobilisation')
    }

    const result = await response.json()
    console.error('Résultat reçu:', result)
    console.error('Type du résultat:', typeof result)
    console.error('Est-ce un tableau?', Array.isArray(result))

    // Si le résultat est un tableau, prendre le premier élément (l'immobilisation)
    if (Array.isArray(result)) {
      console.error('Résultat est un tableau, prise du premier élément')
      return result[0]
    }

    return result
  },

  // Mettre à jour une immobilisation
  async updateImmobilisation(immobilisation: Immobilisation): Promise<Immobilisation> {
    const response = await fetch(
      `${API_BASE_URL}/api/immobilisations`,
      {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
        body: JSON.stringify(immobilisation),
      },
    )

    if (!response.ok) {
      const error = await response.json().catch(() => ({}))
      throw new Error(error.message || 'Erreur lors de la mise à jour de l\'immobilisation')
    }

    return response.json()
  },

  // Supprimer une immobilisation
  async deleteImmobilisation(id: string): Promise<void> {
    const response = await fetch(
      `${API_BASE_URL}/api/immobilisations/${id}`,
      {
        method: 'DELETE',
        credentials: 'include',
      },
    )

    if (!response.ok) {
      const error = await response.json().catch(() => ({}))
      throw new Error(error.message || 'Erreur lors de la suppression de l\'immobilisation')
    }
  },
}

export const amortissementApi = {
  // Récupérer tous les amortissements d'un utilisateur
  async getAmortissementsByUtilisateur(utilisateurId: string): Promise<Amortissement[]> {
    const response = await fetch(
      `${API_BASE_URL}/api/amortissements/${utilisateurId}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
      },
    )

    if (!response.ok) {
      throw new Error('Erreur lors de la récupération des amortissements')
    }

    return response.json()
  },

  // Récupérer les amortissements d'une propriété
  async getAmortissementsByPropriete(proprieteId: string): Promise<Amortissement[]> {
    const response = await fetch(
      `${API_BASE_URL}/api/amortissements/propriete/${proprieteId}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
      },
    )

    if (!response.ok) {
      throw new Error('Erreur lors de la récupération des amortissements de la propriété')
    }

    return response.json()
  },

  // Récupérer les amortissements d'une immobilisation
  async getAmortissementsByImmobilisation(immobilisationId: string): Promise<Amortissement[]> {
    const response = await fetch(
      `${API_BASE_URL}/api/amortissements/immobilisation/${immobilisationId}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
      },
    )

    if (!response.ok) {
      throw new Error('Erreur lors de la récupération des amortissements de l\'immobilisation')
    }

    return response.json()
  },

  // Récupérer les amortissements d'un utilisateur pour une année donnée
  async getAmortissementsByUtilisateurAndAnnee(utilisateurId: string, annee: number): Promise<Amortissement[]> {
    const response = await fetch(
      `${API_BASE_URL}/api/amortissements/${utilisateurId}/${annee}`,
      {
        method: 'GET',
        headers: { 'Content-Type': 'application/json' },
        credentials: 'include',
      },
    )

    if (!response.ok) {
      throw new Error('Erreur lors de la récupération des amortissements')
    }

    return response.json()
  },

  // Générer le plan d'amortissement pour une immobilisation
  async genererPlanAmortissement(immobilisationId: string): Promise<void> {
    const response = await fetch(
      `${API_BASE_URL}/api/amortissements/generer/${immobilisationId}`,
      {
        method: 'POST',
        credentials: 'include',
      },
    )

    if (!response.ok) {
      const error = await response.json().catch(() => ({}))
      throw new Error(error.message || 'Erreur lors de la génération du plan d\'amortissement')
    }
  },
}

export async function getCreditsByUtilisateur(utilisateurId: string): Promise<Credit[]> {
  const response = await fetch(`${API_BASE_URL}/api/getCreditsByUtilisateur/${utilisateurId}`, {
    method: 'GET',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
  });
  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des crédits');
  }
  return response.json();
}

export async function createCredit(credit: Partial<Credit>): Promise<Credit> {
  const response = await fetch(`${API_BASE_URL}/api/credits`, {
    method: 'POST',
    headers: { 'Content-Type': 'application/json' },
    credentials: 'include',
    body: JSON.stringify(credit),
  });
  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error.message || 'Erreur lors de la création du crédit');
  }
  return response.json();
}

export async function deleteCreditById(id: string): Promise<void> {
  const response = await fetch(`${API_BASE_URL}/api/credits/${id}`, {
    method: 'DELETE',
    credentials: 'include',
  });

  if (!response.ok) {
    const error = await response.json().catch(() => ({}));
    throw new Error(error.message || 'Erreur lors de la suppression du crédit');
  }
} 