import type { ChargeDTO, DocumentDTO, EcritureComptableDTO, RecetteDTO } from '@/types/dto'

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
      credentials: 'include',
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
      credentials: 'include',
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

/**
 * Récupérer une charge par son ID
 * @param id ID de la charge
 */
export async function fetchChargeById(id: string): Promise<ChargeDTO> {
  const response = await fetch(
    `${API_BASE_URL}/api/charge/${id}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    }
  )
  if (!response.ok) {
    throw new Error('Erreur lors de la récupération de la charge')
  }
  return response.json()
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

/**
 * Récupérer une recette par son ID
 * @param id ID de la recette
 */
export async function fetchRecetteById(id: string): Promise<RecetteDTO> {
  const response = await fetch(
    `${API_BASE_URL}/api/recette/${id}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    }
  )
  if (!response.ok) {
    throw new Error('Erreur lors de la récupération de la recette')
  }
  return response.json()
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

/**
 * Créer une écriture comptable automatiquement lors de la création d'une quittance
 * @param quittanceId ID de la quittance
 */
export async function createEcritureComptableQuittance(quittanceId: string): Promise<EcritureComptableDTO> {
  const response = await fetch(
    `${API_BASE_URL}/api/ecritures-comptables/quittance/${quittanceId}`,
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
 * Supprimer un amortissement
 * @param id ID de l'amortissement à supprimer
 */
export async function deleteAmortissement(id: string): Promise<void> {
  const response = await fetch(
    `${API_BASE_URL}/api/amortissements/${id}`,
    {
      method: 'DELETE',
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || 'Erreur lors de la suppression de l\'amortissement')
  }
}

/**
 * Modifier une écriture comptable
 * @param data Données de l'écriture comptable à modifier
 */
export async function updateEcritureComptable(data: Partial<EcritureComptableDTO>): Promise<EcritureComptableDTO> {
  const response = await fetch(
    `${API_BASE_URL}/api/ecritures-comptables`,
    {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
      body: JSON.stringify(data)
    }
  )
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || 'Erreur lors de la modification de l\'écriture comptable')
  }
  
  return response.json()
}

/**
 * Supprimer une écriture comptable
 * @param id ID de l'écriture comptable à supprimer
 */
export async function deleteEcritureComptable(id: string): Promise<void> {
  const response = await fetch(
    `${API_BASE_URL}/api/ecritures-comptables/${id}`,
    {
      method: 'DELETE',
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    const error = await response.json().catch(() => ({}))
    throw new Error(error.message || 'Erreur lors de la suppression de l\'écriture comptable')
  }
}

/**
 * Récupérer les documents d'un utilisateur
 * @param utilisateurId ID de l'utilisateur
 */
export async function fetchDocuments(utilisateurId: string): Promise<DocumentDTO[]> {
  const response = await fetch(
    `${API_BASE_URL}/api/documents/utilisateur/${utilisateurId}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include'
    }
  )
  
  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des documents')
  }
  
  return response.json()
}

/**
 * Uploader un nouveau document
 * @param file Fichier à uploader
 * @param titre Titre du document
 * @param utilisateurId ID de l'utilisateur
 * @param proprieteId ID de la propriété (optionnel)
 * @param typeDocument Type de document (optionnel)
 */
export async function uploadDocument(
  file: File,
  titre: string,
  utilisateurId: string,
  proprieteId?: string,
  typeDocument?: string
): Promise<DocumentDTO> {
  // Validation côté client de la taille du fichier
  const maxFileSize = 10 * 1024 * 1024; // 10MB
  if (file.size > maxFileSize) {
    throw new Error(`Le fichier est trop volumineux. Taille maximum autorisée : 10MB. Taille actuelle : ${formatFileSize(file.size)}`)
  }

  const formData = new FormData()
  formData.append('file', file)
  formData.append('titre', titre)
  formData.append('utilisateurId', utilisateurId)
  if (proprieteId) {
    formData.append('proprieteId', proprieteId)
  }
  if (typeDocument) {
    formData.append('typeDocument', typeDocument)
  }

  const response = await fetch(
    `${API_BASE_URL}/api/uploadDocumentFile`,
    {
      method: 'POST',
      credentials: 'include',
      body: formData
    }
  )

  if (!response.ok) {
    let errorMessage = 'Erreur lors de l\'upload du document'
    
    if (response.status === 413) {
      errorMessage = 'Le fichier est trop volumineux. Taille maximum autorisée : 10MB.'
    } else {
      try {
        const errorData = await response.json()
        errorMessage = errorData.message || errorMessage
      } catch (e) {
        // Si on ne peut pas parser la réponse JSON, on utilise le message par défaut
      }
    }
    
    throw new Error(errorMessage)
  }

  return response.json()
}

// Fonction utilitaire pour formater la taille du fichier
function formatFileSize(bytes: number): string {
  if (bytes < 1024) return bytes + ' B'
  if (bytes < 1024 * 1024) return (bytes / 1024).toFixed(1) + ' KB'
  if (bytes < 1024 * 1024 * 1024) return (bytes / (1024 * 1024)).toFixed(1) + ' MB'
  return (bytes / (1024 * 1024 * 1024)).toFixed(1) + ' GB'
}

export async function downloadDocument(documentId: string): Promise<Blob> {
  // D'abord récupérer les métadonnées du document pour avoir le type MIME
  const metadataResponse = await fetch(
    `${API_BASE_URL}/api/documents/${documentId}`,
    {
      method: 'GET',
      credentials: 'include',
    }
  )

  if (!metadataResponse.ok) {
    throw new Error('Erreur lors de la récupération des métadonnées du document')
  }

  const metadata = await metadataResponse.json()
  const mimeType = metadata.mimeType || 'application/octet-stream'

  // Ensuite récupérer le contenu base64
  const contentResponse = await fetch(
    `${API_BASE_URL}/api/documents/${documentId}/content`,
    {
      method: 'GET',
      credentials: 'include',
    }
  )

  if (!contentResponse.ok) {
    throw new Error('Erreur lors du téléchargement du document')
  }

  // Le backend renvoie le contenu en base64, on doit le décoder
  const base64Content = await contentResponse.text()
  
  // Décoder le base64 en bytes
  const binaryString = atob(base64Content)
  const bytes = new Uint8Array(binaryString.length)
  for (let i = 0; i < binaryString.length; i++) {
    bytes[i] = binaryString.charCodeAt(i)
  }
  
  // Créer un blob avec le bon type MIME
  return new Blob([bytes], { type: mimeType })
}

export async function getEcrituresComptablesWithProprieteNom(utilisateurId: string): Promise<Api.EcritureComptable[]> {
  const response = await fetch(
    `${API_BASE_URL}/api/ecritures-comptables/utilisateur/${utilisateurId}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
    }
  )

  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des écritures comptables')
  }

  return response.json()
}

export async function getChargesWithProprieteNom(utilisateurId: string): Promise<Api.Charge[]> {
  const response = await fetch(
    `${API_BASE_URL}/api/charges/${utilisateurId}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
    }
  )

  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des charges')
  }

  return response.json()
}

export async function getRecettesWithProprieteNom(utilisateurId: string): Promise<Api.Recette[]> {
  const response = await fetch(
    `${API_BASE_URL}/api/recettes/${utilisateurId}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
    }
  )

  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des recettes')
  }

  return response.json()
} 

export async function getSoldesIntermediairesGestion(annee: number, utilisateurId: string) {
  const response = await fetch(
    `${API_BASE_URL}/api/soldes-intermediaires-gestion?annee=${annee}&utilisateurId=${utilisateurId}`,
    {
      method: 'GET',
      headers: { 'Content-Type': 'application/json' },
      credentials: 'include',
    }
  )

  if (!response.ok) {
    throw new Error('Erreur lors de la récupération des soldes intermédiaires de gestion')
  }

  return response.json()
} 