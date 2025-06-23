<script setup lang="ts">
import { useUnifiedStore } from '@/store/unifiedStore'
import { useAuthStore } from '@/store/modules/auth'
import { useRoute, useRouter } from 'vue-router'
import {
  NButton,
  NCard,
  NDataTable,
  NDatePicker,
  NEmpty,
  NForm,
  NFormItem,
  NH1,
  NInput,
  NInputNumber,
  NModal,
  NSelect,
  NSpace,
  NSpin,
  NTabPane,
  NTabs,
  NIcon,
  NPopconfirm,
  useMessage,
} from 'naive-ui'
import { h, onMounted, ref } from 'vue'
import { 
  Add24Filled, 
  Edit24Filled, 
  Delete24Filled, 
  ArrowLeft24Filled, 
  Document24Filled,
  CheckmarkCircle24Filled,
  ErrorCircle24Filled,
  Dismiss24Filled as DismissIcon
} from '@vicons/fluent'
// import { useRouter } from 'vue-router'  // Will be used later


definePage({
  meta: {
    title: 'Details propriete',
    hideInMenu: true,
    activeMenu: '/propriete',
  },
})
const route = useRoute()
const router = useRouter()
const store = useUnifiedStore()
const message = useMessage()

// Initialisation des stores
const authStore = useAuthStore()

// États
const loading = ref(true)
const saving = ref(false)
const editingInfos = ref(false)
const showCompositionModal = ref(false)
const showDocumentModal = ref(false)
const uploading = ref(false)
const proprieteDetail = ref<any | null>(null)

// Récupérer l'ID de la propriété depuis les paramètres de route
const proprieteId = route.params.id as string

// Formulaires
const editForm = ref({
  id: '',
  nom: '',
  adresse: '',
  ville: '',
  codePostal: '',
  typeBien: '',
  montantAcquisition: 0,
  dateAcquisition: null as string | null,
  tantieme: 0,
  fraisNotaire: 0,
  fraisAgence: 0,
})

const compositionForm = ref({
  id: '',
  categorie: '',
  pourcentage: 0,
  montant: 0,
  description: '',
  proprieteId: '',
})

// Formulaire de document
const documentForm = ref({
  id: '',
  type: 'AUTRE',
  fichier: null as File | null,
  contenu: '',
  proprieteId: proprieteId,
  locataireId: '', // Laisser vide pour les documents de propriété
  titre: '',
  nomFichier: '',
  mimeType: '',
  taille: 0,
  dateDocument: new Date().toISOString().split('T')[0]
})

const fileInput = ref<HTMLInputElement | null>(null)

const documentTypes = [
  { value: 'BAIL', label: 'Bail de location' },
  { value: 'QUITTANCE', label: 'Quittance de loyer' },
  { value: 'FACTURE', label: 'Facture' },
  { value: 'JUSTIFICATIF', label: 'Justificatif (revenus, identité...)' },
  { value: 'CONTRAT_DE_CREDIT', label: 'Contrat de crédit' },
  { value: 'AUTRE', label: 'Autre document' },
]

const categoriesAmortissement = [
  { value: 'terrain', label: 'Terrains (non amortissable, 10-20%)' },
  { value: 'bati', label: 'Bâti (gros œuvre, 50-60%, 35 ans)' },
  { value: 'agencement', label: 'Agencements et second œuvre (20-30%, 12 ans)' },
  { value: 'mobilier', label: 'Mobilier et électroménager (variable, 7 ans)' },
  { value: 'frais', label: 'Frais d\'acquisition (5 ans)' },
]

function calculerMontant() {
  if (proprieteDetail.value?.propriete && compositionForm.value.pourcentage) {
    const montantTotal = parseFloat(proprieteDetail.value.propriete.montantAcquisition) || 0
    compositionForm.value.montant = (montantTotal * compositionForm.value.pourcentage) / 100
  }
}

// Colonnes du tableau des compositions
// Tableau des colonnes pour le tableau des compositions
const _compositionColumns = [
  { title: 'Catégorie', key: 'categorie' },
  {
    title: 'Montant',
    key: 'montant',
    render: (row: any) => formatCurrency(row.montant),
  },
  { title: 'Description', key: 'description' },
  {
    title: 'Actions',
    key: 'actions',
    render: (row: any) => h('div', { class: 'actions' }, [
      h(NButton, {
        size: 'small',
        onClick: () => editComposition(row),
      }, 'Modifier'),
      h(NButton, {
        size: 'small',
        type: 'error',
        onClick: () => deleteComposition(row.id),
      }, 'Supprimer'),
    ]),
  },
]

// Méthodes
async function supprimerPropriete(id: string) {
  try {
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/deletePropriete/${id}`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include'
    })

    if (!response.ok) {
      const errorData = await response.json().catch(() => ({}))
      if (response.status === 400 && errorData.message) {
        // Afficher le message d'erreur spécifique du serveur
        message.error(errorData.message)
        if (errorData.locations) {
          // Afficher la liste des locations liées si disponible
          const locationsList = errorData.locations.join('\n')
          message.error(`Locations liées :\n${locationsList}`, { duration: 10000 })
        }
      } else {
        throw new Error(errorData.message || 'Erreur lors de la suppression')
      }
      return
    }

    message.success('Propriété supprimée avec succès')
    router.push('/propriete')
  } catch (error) {
    console.error('Erreur lors de la suppression :', error)
    message.error(error instanceof Error ? error.message : 'Erreur lors de la suppression de la propriété')
  }
}

async function fetchProprieteDetails() {
  try {
    loading.value = true
    console.log('Chargement des détails de la propriété ID:', proprieteId)
    const url = `${import.meta.env.VITE_SERVICE_BASE_URL}/api/getProprieteDetails/${proprieteId}`
    console.log('URL de l\'API:', url)
    
    const response = await fetch(url, {
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })
    console.log('Réponse reçue. Statut:', response.status)
    
    const responseText = await response.text()
    console.log('Réponse brute:', responseText)
    
    if (!response.ok) {
      console.error('Erreur de réponse. Contenu:', responseText)
      throw new Error(`Erreur HTTP: ${response.status}`)
    }
    
    // Essayer de parser le JSON
    try {
      const data = JSON.parse(responseText)
      console.log('Données JSON parsées:', data)
      proprieteDetail.value = data
    } catch (parseError) {
      console.error('Erreur lors de l\'analyse JSON:', parseError)
      throw new Error(`Réponse du serveur invalide (pas du JSON): ${responseText.substring(0, 200)}...`)
    }
  } catch (error) {
    console.error('Erreur lors du chargement de la propriété:', error)
    message.error('Erreur de chargement de la propriété: ' + (error instanceof Error ? error.message : String(error)))
  } finally {
    loading.value = false
  }
}

function startEditing(section?: string) {
  if (section === 'infos' && proprieteDetail.value?.propriete) {
    const propriete = proprieteDetail.value.propriete
    // S'assurer que les champs numériques sont correctement typés
    editForm.value = {
      ...propriete,
      montantAcquisition: Number(propriete.montantAcquisition) || 0,
      tantieme: Number(propriete.tantieme) || 0,
      fraisNotaire: Number(propriete.fraisNotaire) || 0,
      fraisAgence: Number(propriete.fraisAgence) || 0,
    }
    editingInfos.value = true
  }
}

async function savePropriete() {
  try {
    saving.value = true
    const authStore = useAuthStore()
    const userId = authStore.userInfo?.userId || ''
    
    if (!userId) {
      throw new Error('Utilisateur non connecté')
    }

    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/updatePropriete/${userId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      credentials: 'include',
      body: JSON.stringify(editForm.value),
    })

    if (!response.ok) {
      const error = await response.text()
      throw new Error(error || 'Erreur lors de la mise à jour')
    }

    const updatedPropriete = await response.json()

    // Mettre à jour les données locales
    if (proprieteDetail.value) {
      proprieteDetail.value.propriete = updatedPropriete
    }

    message.success('Modifications enregistrées avec succès')
    editingInfos.value = false
  }
  catch (error) {
    const errorMessage = error instanceof Error ? error.message : 'Erreur inconnue'
    message.error(errorMessage || 'Erreur lors de la mise à jour de la propriété')
    console.error('Erreur:', error)
  }
  finally {
    saving.value = false
  }
}

function cancelEditing() {
  editingInfos.value = false
}

function addComposition() {
  compositionForm.value = {
    id: '',
    categorie: '',
    pourcentage: 0,
    montant: 0,
    description: '',
    proprieteId: store.selectedProprieteId,
  }
  showCompositionModal.value = true
}

function editComposition(composition: any) {
  compositionForm.value = { ...composition, pourcentage: 0 }
  // Si on édite une composition existante, on calcule le pourcentage
  if (proprieteDetail.value?.propriete && composition.montant) {
    const montantTotal = parseFloat(proprieteDetail.value.propriete.montantAcquisition) || 1 // Éviter la division par zéro
    compositionForm.value.pourcentage = (parseFloat(composition.montant) / montantTotal) * 100
  }
  showCompositionModal.value = true
}

async function saveComposition() {
  try {
    saving.value = true
    
    // S'assurer que le montant est à jour avant sauvegarde
    if (compositionForm.value.pourcentage) {
      calculerMontant()
    }
    
    // Préparer la composition
    const compositionToSave = {
      id: compositionForm.value.id || undefined,
      categorie: compositionForm.value.categorie,
      montant: compositionForm.value.montant.toString(),
      description: compositionForm.value.description,
    };
    
    // Récupérer la propriété actuelle
    const propriete = { ...proprieteDetail.value.propriete };
    
    // Mettre à jour ou ajouter la composition
    if (compositionForm.value.id) {
      // Mise à jour d'une composition existante
      const index = propriete.compositions.findIndex((c: any) => c.id === compositionForm.value.id);
      if (index !== -1) {
        propriete.compositions[index] = {
          ...propriete.compositions[index],
          ...compositionToSave
        };
      }
    } else {
      // Ajout d'une nouvelle composition
      if (!propriete.compositions) {
        propriete.compositions = [];
      }
      propriete.compositions.push({
        ...compositionToSave,
        id: '' // L'ID sera généré par le backend
      });
    }
    
    // Mettre à jour la propriété avec les nouvelles compositions
    const authStore = useAuthStore();
    const userId = authStore.userInfo.userId;
    
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/updatePropriete/${userId}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(propriete),
    });

    if (!response.ok) {
      const error = await response.text();
      throw new Error(error || 'Erreur lors de la mise à jour de la propriété');
    }

    message.success('Composition enregistrée avec succès');
    showCompositionModal.value = false;
    await fetchProprieteDetails();
  } catch (error) {
    console.error('Erreur:', error);
    message.error(error instanceof Error ? error.message : 'Erreur lors de la sauvegarde de la composition');
  } finally {
    saving.value = false;
  }
}

async function deleteComposition(id: string) {
  try {
    if (!proprieteDetail.value?.propriete) return;
    
    // Créer une copie de la propriété
    const propriete = { ...proprieteDetail.value.propriete };
    
    // Filtrer pour supprimer la composition
    if (propriete.compositions) {
      propriete.compositions = propriete.compositions.filter((c: any) => c.id !== id);
      
      // Mettre à jour la propriété
      const authStore = useAuthStore();
      const userId = authStore.userInfo.userId;
      
      const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/updatePropriete/${userId}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(propriete),
      });

      if (!response.ok) {
        const error = await response.text();
        throw new Error(error || 'Erreur lors de la suppression de la composition');
      }
      
      // Recharger les données
      await fetchProprieteDetails();
      message.success('Composition supprimée avec succès');
    }
  } catch (error) {
    console.error('Erreur lors de la suppression :', error);
    message.error(error instanceof Error ? error.message : 'Erreur lors de la suppression de la composition');
  }
}

// Utilitaires
function formatCurrency(value: string | number | null | undefined) {
  if (value === null || value === undefined)
    return '-'
  const num = typeof value === 'string' ? Number.parseFloat(value) : value
  return new Intl.NumberFormat('fr-FR', {
    style: 'currency',
    currency: 'EUR',
  }).format(num)
}

function formatDate(dateString: string | null | undefined) {
  if (!dateString)
    return '-'
  return new Date(dateString).toLocaleDateString('fr-FR')
}

// Gestion des documents
function getDocumentIcon(type: string) {
  // Utilisez Document24Filled pour tous les types de documents
  return Document24Filled
}

function getFileExtension(filename: string) {
  return filename.split('.').pop()?.toUpperCase() || ''
}

function handleDrop(event: DragEvent) {
  const file = event.dataTransfer?.files[0]
  if (!file) {
    message.error('Aucun fichier déposé.')
    return
  }

  if (file.size > 10 * 1024 * 1024) { // 10 MB
    message.error('Le fichier est trop volumineux (max 10MB).')
    return
  }

  documentForm.value.fichier = file
  documentForm.value.nomFichier = file.name
  documentForm.value.mimeType = file.type
  documentForm.value.taille = file.size

  const reader = new FileReader()
  reader.onload = e => {
    documentForm.value.contenu = e.target?.result as string
  }
  reader.onerror = () => {
    message.error('Erreur lors de la lecture du fichier.')
  }
  reader.readAsDataURL(file)
}

function handleFileUpload(event: Event) {
  const target = event.target as HTMLInputElement
  if (!target.files || target.files.length === 0) {
    console.log('Aucun fichier sélectionné.')
    return
  }

  const file = target.files[0]
  console.log('Fichier sélectionné:', file)

  if (file.size > 10 * 1024 * 1024) { // 10 MB
    message.error('Le fichier est trop volumineux. La taille maximale est de 10 Mo.')
    target.value = ''
    return
  }

  documentForm.value.fichier = file
  documentForm.value.nomFichier = file.name
  documentForm.value.mimeType = file.type
  documentForm.value.taille = file.size
  documentForm.value.titre = file.name.split('.').slice(0, -1).join('.')

  const reader = new FileReader()
  reader.onload = (e) => {
    const result = e.target?.result as string
    documentForm.value.contenu = result.split(',')[1]
  }
  reader.onerror = (error) => {
    console.error('Erreur de lecture du fichier:', error)
    message.error('Erreur lors de la lecture du fichier.')
  }
  reader.readAsDataURL(file)
}

async function ajouterDocument() {
  if (!documentForm.value.fichier || !documentForm.value.contenu) {
    message.error('Veuillez sélectionner un fichier.')
    return
  }

  uploading.value = true
  try {
    const authStore = useAuthStore()
    const token = authStore.token || localStorage.getItem('token')
    if (!token) {
      throw new Error('Utilisateur non authentifié')
    }

    const documentData = {
      contenu: documentForm.value.contenu,
      mimeType: documentForm.value.mimeType,
      nomFichier: documentForm.value.nomFichier,
      taille: documentForm.value.taille,
      utilisateurId: authStore.userInfo?.userId,
      proprieteId: documentForm.value.proprieteId,
      locataireId: documentForm.value.locataireId,
      typeDocument: documentForm.value.type,
      titre: documentForm.value.nomFichier, // Utiliser le nom du fichier comme titre
      dateDocument: new Date().toISOString().split('T')[0] // Utiliser la date du jour
    }

    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/uploadDocument`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
        'Authorization': `Bearer ${token}`
      },
      body: JSON.stringify(documentData)
    })

    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(`Erreur ${response.status}: ${errorText}`)
    }

    message.success('Document téléversé avec succès !')
    showDocumentModal.value = false
    resetDocumentForm()
    await fetchProprieteDetails() // Recharger les documents
  } catch (error) {
    console.error('Erreur lors du téléversement du document:', error)
    message.error(error instanceof Error ? error.message : 'Une erreur est survenue.')
  } finally {
    uploading.value = false
  }
}

function resetDocumentForm() {
  documentForm.value = {
    id: '',
    type: 'AUTRE',
    fichier: null,
    contenu: '',
    proprieteId: proprieteId,
    locataireId: '',
    titre: '',
    nomFichier: '',
    mimeType: '',
    taille: 0,
    dateDocument: new Date().toISOString().split('T')[0]
  }
  // Reset file input if possible
  const fileInput = document.getElementById('file-upload') as HTMLInputElement
  if (fileInput) {
    fileInput.value = ''
  }
}

async function supprimerDocument(id: string) {
  try {
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL}/api/documents/${id}`, {
      method: 'DELETE',
      headers: {
        'Authorization': `Bearer ${localStorage.getItem('token')}`
      }
    })

    if (!response.ok) {
      throw new Error('Erreur lors de la suppression du document')
    }

    message.success('Document supprimé avec succès')
    fetchProprieteDetails()
  } catch (error) {
    console.error('Erreur lors de la suppression du document:', error)
    message.error('Erreur lors de la suppression du document: ' + (error instanceof Error ? error.message : String(error)))
  }
}

const telechargerDocument = async (doc: any) => {
  if (typeof window === 'undefined') return;
  
  let a: HTMLAnchorElement | null = null;
  
  try {
    const authStore = useAuthStore()
    const token = authStore.token || localStorage.getItem('token') || ''
    
    const response = await fetch(`${import.meta.env.VITE_SERVICE_BASE_URL || ''}/api/documents/${doc.id}/content`, {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })

    if (!response.ok) {
      const errorText = await response.text()
      throw new Error(`Erreur ${response.status}: ${errorText || 'Erreur inconnue'}`)
    }

    // Récupérer le contenu en base64
    const base64Content = await response.text()
    
    // Récupérer le nom de fichier depuis les en-têtes ou utiliser un nom par défaut
    const contentDisposition = response.headers.get('Content-Disposition')
    const filename = contentDisposition 
      ? contentDisposition.split('filename=')[1].replace(/"/g, '')
      : doc.nomFichier || `document_${doc.id}.bin`
    
    // Créer un élément <a> invisible
    a = document.createElement('a')
    a.style.display = 'none'
    a.href = `data:${doc.mimeType || 'application/octet-stream'};base64,${base64Content}`
    a.download = filename
    
    // Ajouter l'élément au DOM, déclencher le clic
    document.body.appendChild(a)
    a.click()
    message.success('Téléchargement démarré')
  } catch (error) {
    console.error('Erreur lors du téléchargement:', error)
    message.error('Erreur lors du téléchargement du document: ' + (error instanceof Error ? error.message : String(error)))
  } finally {
    // Nettoyer après un court délai
    setTimeout(() => {
      if (a && document.body.contains(a)) {
        document.body.removeChild(a)
      }
    }, 1000)
  }
};

function nouveauDocument() {
  documentForm.value = {
    id: '',
    type: 'AUTRE',
    fichier: null,
    proprieteId: proprieteId
  }
  showDocumentModal.value = true
}

// Cycle de vie
onMounted(() => {
  fetchProprieteDetails()
})

function definePage(arg0: { meta: { title: string; hideInMenu: boolean; activeMenu: string } }) {
  throw new Error('Function not implemented.')
}
</script>

<template>
  <div class="propriete-detail">
    <div class="page-header">
      <NButton text @click="$router.push('/propriete')" class="back-button">
        <NIcon :component="ArrowLeft24Filled" size="20" />
      </NButton>
      <NH1>Détails de la propriété</NH1>
    </div>

    <NSpin :show="loading">
      <NTabs v-if="proprieteDetail" type="line" animated>
        <!-- Onglet Informations -->
        <NTabPane name="infos" tab="Informations">
          <div v-if="!editingInfos" class="action-buttons">
            <NButton type="primary" @click="startEditing('infos')" class="action-button" ghost>
              <template #icon>
                <NIcon :component="Edit24Filled" />
              </template>
              Modifier
            </NButton>
            <NPopconfirm
              @positive-click="() => supprimerPropriete(proprieteDetail.propriete.id)"
            >
              <template #trigger>
                <NButton type="error" ghost class="action-button">
                  <template #icon>
                    <NIcon :component="Delete24Filled" />
                  </template>
                  Supprimer
                </NButton>
              </template>
              Êtes-vous sûr de vouloir supprimer cette propriété ?
            </NPopconfirm>
          </div>
          <div v-else class="action-buttons">
            <NButton type="primary" :loading="saving" @click="savePropriete" class="action-button">
              <template #icon>
                <NIcon :component="Save24Filled" />
              </template>
              Enregistrer
            </NButton>
            <NButton class="action-button ml-2" @click="cancelEditing">
              <template #icon>
                <NIcon :component="Dismiss24Filled" />
              </template>
              Annuler
            </NButton>
          </div>

          <div v-if="!editingInfos" class="info-grid">
            <div class="info-label">
              Nom :
            </div>
            <div class="info-value">
              {{ proprieteDetail.propriete.nom }}
            </div>

            <div class="info-label">
              Adresse :
            </div>
            <div class="info-value">
              {{ proprieteDetail.propriete.adresse }}
            </div>

            <div class="info-label">
              Ville :
            </div>
            <div class="info-value">
              {{ proprieteDetail.propriete.ville }}
            </div>

            <div class="info-label">
              Type :
            </div>
            <div class="info-value">
              {{ proprieteDetail.propriete.typeBien }}
            </div>

            <div class="info-label">
              Montant acquisition :
            </div>
            <div class="info-value">
              {{ formatCurrency(proprieteDetail.propriete.montantAcquisition) }}
            </div>

            <div class="info-label">
              Date acquisition :
            </div>
            <div class="info-value">
              {{ formatDate(proprieteDetail.propriete.dateAcquisition) }}
            </div>

            <div class="info-label">
              Tantième :
            </div>
            <div class="info-value">
              {{ proprieteDetail.propriete.tantieme }}
            </div>
          </div>

          <NForm v-else class="edit-form">
            <div class="info-grid">
              <div class="info-label">
                Nom :
              </div>
              <div class="info-value">
                <NInput v-model:value="editForm.nom" />
              </div>

              <div class="info-label">
                Adresse :
              </div>
              <div class="info-value">
                <NInput v-model:value="editForm.adresse" />
              </div>

              <div class="info-label">
                Code postal :
              </div>
              <div class="info-value">
                <NInput v-model:value="editForm.codePostal" />
              </div>

              <div class="info-label">
                Ville :
              </div>
              <div class="info-value">
                <NInput v-model:value="editForm.ville" />
              </div>

              <div class="info-label">
                Type :
              </div>
              <div class="info-value">
                <NSelect
                  v-model:value="editForm.typeBien"
                  :options="[
                    { label: 'Appartement', value: 'APPARTEMENT' },
                    { label: 'Maison', value: 'MAISON' },
                    { label: 'Local commercial', value: 'LOCAL_COMMERCIAL' },
                  ]"
                  style="width: 100%"
                />
              </div>

              <div class="info-label">
                Montant acquisition :
              </div>
              <div class="info-value">
                <NInputNumber v-model:value="editForm.montantAcquisition" style="width: 100%" />
              </div>

              <div class="info-label">
                Date acquisition :
              </div>
              <div class="info-value">
                <NDatePicker v-model:value="editForm.dateAcquisition" style="width: 100%" />
              </div>

              <div class="info-label">
                Tantième :
              </div>
              <div class="info-value">
                <NInputNumber v-model:value="editForm.tantieme" style="width: 100%" />
              </div>
            </div>
          </NForm>
        </NTabPane>

        <!-- Onglet Compositions -->
        <NTabPane name="compositions" tab="Compositions">
          <div class="action-buttons">
            <NButton type="primary" @click="addComposition" class="action-button">
              <template #icon>
                <NIcon :component="Add24Filled" />
              </template>
              Ajouter une composition
            </NButton>
          </div>

          <NDataTable
            :columns="[
              { key: 'categorie', title: 'Catégorie' },
              {
                key: 'montant',
                title: 'Montant',
                render: (row: any) => formatCurrency(row.montant),
              },
              {
                key: 'description',
                title: 'Description',
              },
              {
                key: 'actions',
                title: 'Actions',
                render: (row: any) => h('div', { class: 'table-actions' }, [
                  h(NButton, {
                    size: 'small',
                    text: true,
                    onClick: () => editComposition(row),
                    title: 'Modifier',
                    class: 'action-icon',
                    style: { marginRight: '8px' }
                  }, () => h(NIcon, { component: Edit24Filled, size: 18 })),
                  h(NButton, {
                    size: 'small',
                    text: true,
                    onClick: () => deleteComposition(row.id),
                    title: 'Supprimer',
                    class: 'action-icon error',
                  }, () => h(NIcon, { component: Delete24Filled, size: 18 }))
                ]),
              },
            ]"
            :data="proprieteDetail?.compositions || []"
            :loading="loading"
          >
            <template #empty>
              <NEmpty description="Aucune composition trouvée" />
            </template>
          </NDataTable>

          <!-- Modal d'édition de composition -->
          <NModal v-model:show="showCompositionModal">
            <NCard
              style="width: 600px"
              title="Édition de composition"
              :bordered="false"
              size="huge"
              role="dialog"
              aria-modal="true"
            >
              <NForm :model="compositionForm">
                <NFormItem label="Catégorie">
                  <NSelect
                    v-model:value="compositionForm.categorie"
                    :options="categoriesAmortissement"
                    placeholder="Sélectionnez une catégorie"
                    clearable
                  />
                </NFormItem>
                <NFormItem label="Pourcentage du coût total">
                  <NInputNumber 
                    v-model:value="compositionForm.pourcentage"
                    :min="0"
                    :max="100"
                    :step="0.01"
                    @update:value="calculerMontant"
                    :suffix="'%'"
                  />
                </NFormItem>
                <NFormItem label="Montant calculé">
                  <NInputNumber 
                    :value="compositionForm.montant" 
                    :disabled="true"
                    :formatter="value => formatCurrency(value)"
                  />
                </NFormItem>
                <NFormItem label="Description">
                  <NInput
                    v-model:value="compositionForm.description"
                    type="textarea"
                    :autosize="{ minRows: 3 }"
                  />
                </NFormItem>
              </NForm>
              <template #footer>
                <NSpace justify="end">
                  <NButton @click="showCompositionModal = false">
                    Annuler
                  </NButton>
                  <NButton type="primary" :loading="saving" @click="saveComposition">
                    Enregistrer
                  </NButton>
                </NSpace>
              </template>
            </NCard>
          </NModal>
        </NTabPane>



        <!-- Onglet Documents -->
        <NTabPane name="documents" tab="Documents">
          <div class="action-buttons">
            <NButton type="primary" @click="nouveauDocument" class="action-button">
              <template #icon>
                <NIcon :component="Add24Filled" />
              </template>
              Ajouter un document
            </NButton>
          </div>

          <NDataTable
            :columns="[
              { 
                key: 'typeDocument', 
                title: 'Type',
                render: (row: any) => {
                  const type = documentTypes.find(t => t.value === row.typeDocument)
                  return type ? type.label : row.typeDocument || 'Non spécifié'
                }
              },
              { 
                key: 'titre', 
                title: 'Nom du document',
                render: (row: any) => row.titre || row.nomFichier || 'Sans nom'
              },
              { 
                key: 'dateDocument', 
                title: 'Date',
                render: (row: any) => formatDate(row.dateDocument)
              },
              {
                key: 'actions',
                title: 'Actions',
                render: (row: any) => h('div', { class: 'table-actions' }, [
                  h(NButton, {
                    size: 'small',
                    text: true,
                    onClick: () => telechargerDocument(row),
                    title: 'Télécharger',
                    class: 'action-icon',
                    style: { marginRight: '8px' }
                  }, () => h(NIcon, { component: getDocumentIcon(getFileExtension(row.nomFichier || '')), size: 18 })),
                  h(NPopconfirm, {
                    onPositiveClick: () => supprimerDocument(row.id)
                  }, {
                    trigger: () => h(NButton, {
                      size: 'small',
                      text: true,
                      title: 'Supprimer',
                      class: 'action-icon error',
                    }, () => h(NIcon, { component: Delete24Filled, size: 18 })),
                    default: 'Êtes-vous sûr de vouloir supprimer ce document ?'
                  })
                ]),
              },
            ]"
            :data="proprieteDetail?.documents || []"
            :loading="loading"
          />
        </NTabPane>
      </NTabs>

      <!-- Modal d'ajout de document -->
      <NModal v-model:show="showDocumentModal" :mask-closable="false">
        <NCard
          style="width: 600px; max-width: 90vw;"
          title="Ajouter un document"
          :bordered="false"
          size="huge"
          role="dialog"
          aria-modal="true"
        >
          <NForm @submit.prevent="ajouterDocument">
            <NFormItem label="Type de document" required>
              <NSelect
                v-model:value="documentForm.type"
                :options="documentTypes"
                placeholder="Sélectionnez un type"
              />
            </NFormItem>

            <NFormItem label="Fichier" required>
              <div class="w-full">
                <div 
                  class="border-2 border-dashed rounded-lg p-6 text-center cursor-pointer hover:bg-gray-50 transition-colors"
                  @dragover.prevent
                  @drop.prevent="handleDrop"
                  @click="() => fileInput?.click()"
                >
                  <input 
                    type="file" 
                    ref="fileInput"
                    class="hidden" 
                    accept=".pdf,.doc,.docx,.jpg,.jpeg,.png,image/*"
                    @change="handleFileUpload"
                  >
                  <div class="flex flex-col items-center">
                    <NIcon size="48" :depth="3" class="mb-3">
                      <Document24Filled />
                    </NIcon>
                    <NText style="font-size: 16px" class="block mb-1">
                      Cliquez pour sélectionner un fichier
                    </NText>
                    <NText depth="3" class="block mb-3">
                      ou glissez-déposez un fichier ici
                    </NText>
                    <NButton type="primary" ghost @click.stop="() => fileInput?.click()">
                      Sélectionner un fichier
                    </NButton>
                    <NText depth="3" class="block mt-3 text-xs">
                      Formats supportés: PDF, DOC, DOCX, JPG, PNG (max 10MB)
                    </NText>
                  </div>
                </div>
                
                <div v-if="documentForm.fichier" class="mt-4 p-3 border rounded">
                  <div class="flex justify-between items-center">
                    <div class="flex items-center">
                      <NIcon :component="getDocumentIcon(documentForm.fichier.name)" size="24" class="mr-2" />
                      <span>{{ documentForm.fichier.name }}</span>
                    </div>
                    <NButton text @click="() => { documentForm.fichier = null; documentForm.contenu = ''; if (fileInput) fileInput.value = ''; }">
                      <NIcon :component="DismissIcon" />
                    </NButton>
                  </div>
                </div>
              </div>
            </NFormItem>
          </NForm>
          <template #footer>
            <div class="flex justify-end gap-2">
              <NButton @click="showDocumentModal = false">Annuler</NButton>
              <NButton 
                type="primary" 
                :loading="uploading"
                :disabled="!documentForm.fichier"
                @click="ajouterDocument"
              >
                Téléverser
              </NButton>
            </div>
          </template>
        </NCard>
      </NModal>
    </NSpin>
  </div>
</template>

<style scoped>
/* Styles de base pour les champs de formulaire */
.info-value {
  width: 100%;
}

/* Tous les champs de saisie prennent 100% de la largeur */
.info-value :deep(.n-input),
.info-value :deep(.n-input-number),
.info-value :deep(.n-date-picker),
.info-value :deep(.n-select) {
  width: 100% !important;
  max-width: 100%;
}

/* Pour les éléments internes des champs */
.info-value :deep(.n-input__input),
.info-value :deep(.n-input-number-input),
.info-value :deep(.n-base-selection) {
  width: 100% !important;
}

/* Ajustement pour le sélecteur de date */
.info-value :deep(.n-date-picker) {
  width: 100%;
}

/* Pour les petits écrans */
@media (max-width: 768px) {
  .info-grid {
    grid-template-columns: 1fr;
  }
  
  .info-label {
    text-align: left;
  }
}

.propriete-detail {
  padding: 20px;
  width: 100%;
  margin: 0;
}

.page-header {
  display: flex;
  align-items: center;
  margin-bottom: 24px;
}

.back-button {
  margin-right: 12px;
  color: var(--n-text-color);
  transition: color 0.2s;
}

.back-button:hover {
  color: var(--n-text-color-hover);
}

.action-buttons {
  margin-bottom: 20px;
  display: flex;
  gap: 12px;
}

.action-button {
  display: inline-flex;
  align-items: center;
  gap: 6px;
}

.table-actions {
  display: flex;
  gap: 8px;
  justify-content: flex-end;
}

.action-icon {
  font-size: 18px;
  padding: 4px;
  border-radius: 4px;
  transition: background-color 0.2s;
  color: var(--n-text-color);
}

.action-icon:hover {
  background-color: var(--n-color-hover);
}

.action-icon.error {
  color: var(--n-color-error);
}

.action-icon.error:hover {
  background-color: var(--n-color-error-hover);
  color: white;
}

.page-content {
  max-width: 1400px;
  margin: 0 auto;
  width: 100%;
}

.info-grid {
  display: grid;
  grid-template-columns: 200px 1fr;
  gap: 10px;
  margin-top: 20px;
  width: 100%;
}

.info-label {
  font-weight: bold;
  padding: 12px;
  background-color: #f5f5f5;
  border-radius: 4px;
}

.info-value {
  padding: 12px;
  border: 1px solid #eee;
  border-radius: 4px;
}

.edit-form {
  width: 100%;
  max-width: 100%;
  margin-top: 20px;
}

.n-tabs {
  width: 100%;
}

.n-tab-pane {
  padding: 0;
  width: 100%;
}

.n-form {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(300px, 1fr));
  gap: 20px;
  width: 100%;
}

.n-form-item {
  margin-bottom: 0;
}

/* Pour les champs qui doivent prendre toute la largeur */
.full-width {
  grid-column: 1 / -1;
}

/* Ajustements pour les petits écrans */
@media (max-width: 768px) {
  .n-form {
    grid-template-columns: 1fr;
  }
  .info-grid {
    grid-template-columns: 1fr;
  }
  .info-label {
    padding-bottom: 4px;
  }
}

/* Styles pour la table des compositions */
.n-data-table {
  width: 100%;
  margin-top: 20px;
}

/* Styles pour la modale */
.n-modal {
  display: flex;
  align-items: center;
  justify-content: center;
}

.n-card {
  width: 90%;
  max-width: 800px;
  margin: 0 auto;
}

.ml-2 {
  margin-left: 8px;
}

.actions {
  display: flex;
  gap: 8px;
}
</style>
